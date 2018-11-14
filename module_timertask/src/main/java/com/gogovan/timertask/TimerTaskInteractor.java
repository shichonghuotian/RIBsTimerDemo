package com.gogovan.timertask;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.gogovan.utils.WUtils;
import com.gogovan.data.TimerTasksRepository;
import com.gogovan.data.entities.TimerTaskEntity;
import com.uber.autodispose.ObservableScoper;
import com.uber.rib.core.Bundle;
import com.uber.rib.core.Interactor;
import com.uber.rib.core.RibInteractor;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Coordinates Business Logic for {@link TimeTaskScope}.
 * <p>
 * timer logic
 */
@RibInteractor
public class TimerTaskInteractor
        extends Interactor<TimerTaskInteractor.TimeTaskPresenter, TimerTaskRouter> {

    /**
     * minimal seconds of timer
     */
    private static final int MIN_TIME_SECOND = 1;
    /**
     * maximum seconds of timer
     */
    private static final int MAX_TIME_SECOND = 24 * 60 * 60 - 1;

    /**
     * default seconds of timer
     */
    private static final int DEFAULT_TIME_SECOND = 60;


    /**
     * presenter
     */
    @Inject
    TimeTaskPresenter presenter;

    /**
     * total seconds of timer
     */
    private int allSeconds = DEFAULT_TIME_SECOND;

    /**
     * time left by seconds
     */
    private long lastSecond;

    /**
     * current status of timer
     */
    TimerStatus timerStatus = TimerStatus.Idle;

    /**
     * timer
     */
    private Rx2Timer timer;


    /**
     * task repository
     */
    @Inject
    TimerTasksRepository timerTasksRepository;

    @Override
    protected void didBecomeActive(@Nullable Bundle savedInstanceState) {
        super.didBecomeActive(savedInstanceState);
        reset();

        /**
         * auto-increasing by long tap
         * release to cancel increasing
         */
        presenter.upLongRequest()
                .flatMap(o -> {

                    return Observable.interval(100, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.computation())
                            .takeUntil(presenter.touchupObservable())
                            .concatMap(e -> secondsIncrementObservable());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .to(new ObservableScoper<>(this))
                .subscribe(o -> {

                    setTimeText(allSeconds);

                });

        /**
         * auto-reducing by long tap
         * release to cancel increasing
         */
        presenter.downLongRequest()
                .flatMap(o -> {

                    return Observable.interval(100, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.computation())
                            .takeUntil(presenter.touchupObservable())
                            .concatMap(e -> secondsDecrementObservable());

                })
                .observeOn(AndroidSchedulers.mainThread())
                .to(new ObservableScoper<>(this))
                .subscribe(o -> {


                    setTimeText(allSeconds);


                });


        /**
         * to increase when click up
         */
        presenter.upRequest()
                .filter(o -> {
                    return isTimerIdle();
                })
                .concatMap(o -> secondsIncrementObservable())
                .to(new ObservableScoper<>(this))
                .subscribe(o ->
                {
                    setTimeText(allSeconds);

                });


        /**
         * to reduce when click down
         */
        presenter.downRequest()
                .filter(o -> isTimerIdle())
                .concatMap(o -> secondsDecrementObservable())
                .to(new ObservableScoper<>(this))
                .subscribe(o -> {
                    setTimeText(allSeconds);
                });


        /**
         * start timer
         */
        presenter.startRequest().map(o -> canStartTimer())
                .to(new ObservableScoper<>(this))
                .subscribe(b -> {
                    if (b) {

                        startAction();
                    } else {
                        presenter.showEditTextError("Please input task name");
                    }
                });

        /**
         * reset timer
         */
        presenter.resetRequest()
                .filter(o -> !isTimerIdle())
                .to(new ObservableScoper<>(this))

                .subscribe(o -> {

                    stopTimer();
                    reset();
                });

    }

    /**
     * switch status after clicking startbutton
     */
    private void startAction() {

        if (this.timerStatus == TimerStatus.Idle) {
            this.timerStatus = TimerStatus.Running;

            presenter.setUpAndDownEabled(false);
            presenter.showResetButton(true);
            startTimer();

        } else if (this.timerStatus == TimerStatus.Running) {
            this.timerStatus = TimerStatus.Pause;

            stopTimer();

        } else if (this.timerStatus == TimerStatus.Pause) {
            this.timerStatus = TimerStatus.Running;

            resumeTimer();
        }

        chageStartButtonStatus(this.timerStatus);
    }

    /**
     * start timer
     */
    private void startTimer() {
        timer = Rx2Timer.builder()
                .initialDelay(0)
                .period(1) //default is 1
                .take(allSeconds)
                .unit(TimeUnit.SECONDS)
                .onEmit(count -> {
                    setTimeText(count);
                })
                .onError(e -> {
                    endTimer();
                })
                .onComplete(() -> endTimer())
                .build();

        timer.start();

    }

    /**
     * stop timer
     */
    private void stopTimer() {

        if (timer != null) {
            timer.pause();
            saveTimeTask();
        }

    }

    /**
     * resume timer
     */
    private void resumeTimer() {
        if (timer != null) {
            timer.resume();
        }

    }


    /**
     * end up timer
     */
    public void endTimer() {

        if (timer != null) {

            saveTimeTask();
            reset();
        }
    }

    /**
     * if idle
     *
     * @return
     */
    private boolean isTimerIdle() {

        return timerStatus == TimerStatus.Idle;
    }

    /**
     * if can start timer
     *
     * @return
     */
    private boolean canStartTimer() {

        return !TextUtils.isEmpty(presenter.getTimerTaskName());
    }

    /**
     * reduce time
     *
     * @return
     */
    private Observable<Integer> secondsDecrementObservable() {

        return Observable.create(emitter -> {
            if (allSeconds > MIN_TIME_SECOND) {
                allSeconds--;
                emitter.onNext(allSeconds);

            }
            emitter.onComplete();
        });
    }

    /**
     * add time
     *
     * @return
     */
    private Observable<Integer> secondsIncrementObservable() {

        return Observable.create(emitter -> {
            if (allSeconds < MAX_TIME_SECOND) {
                allSeconds++;
                emitter.onNext(allSeconds);

            }
            emitter.onComplete();
        });
    }

    @Override
    protected void willResignActive() {
        super.willResignActive();

    }


    /**
     * save timer task record
     */
    public void saveTimeTask() {

        TimerTaskEntity entity = new TimerTaskEntity();
        entity.setTaskName(presenter.getTimerTaskName());
        entity.setTotalSeconds(allSeconds);
        entity.setCreateTime(System.currentTimeMillis());
        entity.setFinishSeconds(lastSecond);
        entity.setFinished(lastSecond == 0);

        timerTasksRepository.saveTimeTask(entity)
                .observeOn(AndroidSchedulers.mainThread())
                .to(new ObservableScoper<>(this))

                .subscribe(o -> {
                    presenter.showToast("Save task success, " + entity.getFinishSeconds() + " " +
                            "seconds");

                });
    }


    /**
     * set current time
     *
     * @param seconds
     */
    private void setTimeText(long seconds) {
        lastSecond = seconds;
        presenter.setTimeText(WUtils.stringFromTime(seconds));
    }

    /**
     * reset all status
     */
    private void reset() {

        allSeconds = DEFAULT_TIME_SECOND;
        this.timerStatus = TimerStatus.Idle;
        chageStartButtonStatus(this.timerStatus);
        presenter.resetTimerTask();
        setTimeText(allSeconds);
    }


    /**
     * switch label of start button
     *
     * @param timerStatus
     */
    public void chageStartButtonStatus(TimerStatus timerStatus) {

        if (timerStatus == TimerStatus.Idle) {

            presenter.chageStartButtonStatus(R.string.str_start);
        } else if (timerStatus == TimerStatus.Running) {
            presenter.chageStartButtonStatus(R.string.str_stop);

        } else if (timerStatus == TimerStatus.Pause) {

            presenter.chageStartButtonStatus(R.string.str_pause);

        }
    }


    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface TimeTaskPresenter {


        //up shooting
        Observable<Object> upRequest();

        //up long tap
        Observable<Object> upLongRequest();

        //release
        Observable<Object> touchupObservable();

        //down shooting
        Observable<Object> downRequest();

        //down long tap
        Observable<Object> downLongRequest();

        //reset shooting
        Observable<Object> resetRequest();

        /**
         * get time task name
         *
         * @return
         */
        String getTimerTaskName();

        /**
         * start button click
         *
         * @return
         */
        Observable<Object> startRequest();

        //set time label
        void setTimeText(String text);

        /**
         * change start button label
         *
         * @param textResID
         */
        public void chageStartButtonStatus(int textResID);

        /**
         * reset timer task
         */
        void resetTimerTask();

        /**
         * show toast
         *
         * @param text
         */
        void showToast(String text);

        /**
         * show error
         *
         * @param text
         */
        void showEditTextError(String text);

        /**
         * disable/enable up/down button
         *
         * @param enabled
         */
        void setUpAndDownEabled(boolean enabled);

        /**
         * if show reset button
         *
         * @param show
         */
        void showResetButton(boolean show);
    }


}

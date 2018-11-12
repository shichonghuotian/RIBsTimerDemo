package com.gogovan.test.app.timetask;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.gogovan.test.app.common.Utils.WUtils;
import com.gogovan.test.app.common.data.TimeTasksRepository;
import com.gogovan.test.app.common.data.entities.TimeTaskEntity;
import com.uber.autodispose.ObservableScoper;
import com.uber.rib.core.Bundle;
import com.uber.rib.core.Interactor;
import com.uber.rib.core.RibInteractor;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Coordinates Business Logic for {@link TimeTaskScope}.
 * <p>
 * TODO describe the logic of this scope.
 */
@RibInteractor
public class TimeTaskInteractor
        extends Interactor<TimeTaskInteractor.TimeTaskPresenter, TimeTaskRouter> {

    private static final int MIN_TIME_SECOND = 1;
    private static final int MAX_TIME_SECOND = 24*60*60-1;
    private static final int DEFAULT_TIME_SECOND = 60;



    @Inject
    TimeTaskPresenter presenter;

    private int allSeconds = DEFAULT_TIME_SECOND;

    private long lastSecond;

    TimerStatus timerStatus = TimerStatus.Idle;

    private Rx2Timer timer;


    @Inject
    TimeTasksRepository timeTasksRepository;

    @Override
    protected void didBecomeActive(@Nullable Bundle savedInstanceState) {
        super.didBecomeActive(savedInstanceState);
        reset();


//        setTimeText(allSeconds);
        presenter.upRequest()
                .filter(o -> {
                  return   isTimerIdle();
                })
                .concatMap(o -> secondsIncrementObservable())

                .to(new ObservableScoper<>(this))
                .subscribe(o ->
                {

                    setTimeText(allSeconds);

                });


        presenter.downRequest()
                .filter(o -> isTimerIdle())
                .concatMap(o -> secondsDecrementObservable())
                .to(new ObservableScoper<>(this))
                .subscribe(o -> {
                    setTimeText(allSeconds);
                });





        presenter.startRequest().map(o -> canStartTimer())
                .to(new ObservableScoper<>(this))

                .subscribe(b -> {
                    if (b) {

                        if (this.timerStatus == TimerStatus.Idle) {
                            this.timerStatus = TimerStatus.Running;

                            startTimer();

                        } else if(this.timerStatus == TimerStatus.Running){
                            this.timerStatus = TimerStatus.Pause;

                            stopTimer();

                        }else if(this.timerStatus == TimerStatus.Pause) {
                            this.timerStatus = TimerStatus.Running;

                            resumeTimer();
                        }

                        presenter.chageStartButtonStatus(this.timerStatus);
                    } else {
                        presenter.showToast("Task name can't Null");
                    }
                });

    }

    private void startTimer() {


        timer = Rx2Timer.builder()
                .initialDelay(0)
                .period(1) //default is 1
                .take(allSeconds)
                .unit(TimeUnit.SECONDS)
                .onEmit(count -> {


                    setTimeText(count);

                })
                .onError(e -> {endTimer();})
                .onComplete(() -> endTimer())
                .build();

        timer.start();

    }

    private void stopTimer() {

        if (timer != null) {
            timer.pause();
            saveTimeTask();
        }

    }

    private void resumeTimer() {
        if (timer != null) {
            timer.resume();
        }

    }

    public void endTimer() {

        if (timer != null) {

            saveTimeTask();
            reset();
        }
    }

    private boolean isTimerIdle() {

        return timerStatus == TimerStatus.Idle;
    }

    private boolean canStartTimer() {

        return !TextUtils.isEmpty(presenter.getTimeTaskName());
    }

    private Observable<Integer> secondsDecrementObservable() {

         return    Observable.create(emitter -> {
            if(allSeconds > MIN_TIME_SECOND) {
                allSeconds--;
                emitter.onNext(allSeconds);

            }
            emitter.onComplete();
        });
    }

    private Observable<Integer> secondsIncrementObservable() {

        return    Observable.create(emitter -> {
            if(allSeconds < MAX_TIME_SECOND ) {
                allSeconds++;
                emitter.onNext(allSeconds);

            }
//            emitter.onComplete();
        });
    }

    @Override
    protected void willResignActive() {
        super.willResignActive();

    }

    public void saveTimeTask() {

        TimeTaskEntity entity = new TimeTaskEntity();
        entity.setTaskName(presenter.getTimeTaskName());
        entity.setTotalSeconds(allSeconds);
        entity.setCreateTime(System.currentTimeMillis());
        entity.setFinishSeconds(lastSecond);
        entity.setFinished(lastSecond == 0);

        timeTasksRepository.saveTimeTask(entity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    presenter.showToast("Save task success, "+ lastSecond +" seconds");

                });
    }


    private void setTimeText(long seconds) {
        lastSecond = seconds;
        presenter.setTimeText(WUtils.stringFromTime(seconds));
    }

    private void reset() {
        allSeconds = DEFAULT_TIME_SECOND;
        this.timerStatus = TimerStatus.Idle;
        presenter.resetTimeTask();
        setTimeText(allSeconds);
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface TimeTaskPresenter {


        Observable<Object> upRequest();

        Observable<Object> downRequest();

        String getTimeTaskName();

        Observable<Object> startRequest();

        void setTimeText(String text);


        public void chageStartButtonStatus(TimeTaskInteractor.TimerStatus timerStatus);

        void resetTimeTask();

        void showToast(String text);

    }

    static enum TimerStatus {

        Idle,
        Running,
        Pause,
    }
}

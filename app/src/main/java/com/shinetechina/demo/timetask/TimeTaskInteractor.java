package com.shinetechina.demo.timetask;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.shinetechina.demo.common.Utils.WUtils;
import com.shinetechina.demo.common.data.TimeTasksRepository;
import com.shinetechina.demo.common.data.entities.TimeTaskEntity;
import com.uber.autodispose.ObservableScoper;
import com.uber.rib.core.Bundle;
import com.uber.rib.core.Interactor;
import com.uber.rib.core.RibInteractor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Coordinates Business Logic for {@link TimeTaskScope}.
 * <p>
 * TODO describe the logic of this scope.
 */
@RibInteractor
public class TimeTaskInteractor
        extends Interactor<TimeTaskInteractor.TimeTaskPresenter, TimeTaskRouter> {

    private static final int MIN_TIME_SECOND = 1;
    private static final int MAX_TIME_SECOND = 60*60-1;
    private static final int DEFAULT_TIME_SECOND = 10;



    @Inject
    TimeTaskPresenter presenter;

    private int allSeconds = DEFAULT_TIME_SECOND;

    private long lastSecond;

    boolean isTimerIdle = true;
    private Disposable disposable;

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
                  return   isTimerIdle;
                })
                .concatMap(o -> secondsIncrementObservable())

                .to(new ObservableScoper<>(this))
                .subscribe(o ->
                {

                    setTimeText(allSeconds);

                });


        presenter.downRequest()
                .filter(o -> isTimerIdle)
                .concatMap(o -> secondsDecrementObservable())
                .to(new ObservableScoper<>(this))
                .subscribe(o -> {
                    setTimeText(allSeconds);
                });


        presenter.startRequest().map(o -> canStartTimer())
                .to(new ObservableScoper<>(this))

                .subscribe(b -> {
                    if (b) {
                        if (this.isTimerIdle) {
                            startTimer();

                        } else {
                            stopTimer();

                        }

                        this.isTimerIdle = !this.isTimerIdle;
                        presenter.chageStartButtonStatus(this.isTimerIdle);
                    } else {
                        presenter.showToast("Task name can't Null");
                    }
                });

    }

    protected void startTimer() {

        presenter.showToast("timer start");

        timer = Rx2Timer.builder()
                .initialDelay(0)
                .period(1) //default is 1
                .take(allSeconds)
                .unit(TimeUnit.SECONDS)
                .onEmit(count -> {


                    setTimeText(count);

                })
//                .onError(e -> System.out.println(""))
                .onComplete(() -> saveTimeTask())
                .build();

        timer.start();

    }

    protected void stopTimer() {
        presenter.showToast("timer stop");

        if (timer != null) {
            timer.stop();
            saveTimeTask();
        }

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
            emitter.onComplete();
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

                    reset();
                });
    }


    private void setTimeText(long seconds) {
        lastSecond = seconds;
        presenter.setTimeText(WUtils.stringFromTime(seconds));
    }

    private void reset() {
        allSeconds = DEFAULT_TIME_SECOND;
        isTimerIdle = true;
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


        void chageStartButtonStatus(boolean start);

        void resetTimeTask();

        void showToast(String text);

    }
}

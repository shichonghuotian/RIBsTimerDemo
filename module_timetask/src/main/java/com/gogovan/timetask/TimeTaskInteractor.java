package com.gogovan.timetask;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.gogovan.utils.WUtils;
import com.gogovan.data.TimeTasksRepository;
import com.gogovan.data.entities.TimeTaskEntity;
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
 * timer 的主要业务
 */
@RibInteractor
public class TimeTaskInteractor
        extends Interactor<TimeTaskInteractor.TimeTaskPresenter, TimeTaskRouter> {

    /**
     * timer最小的秒数
     */
    private static final int MIN_TIME_SECOND = 1;
    /**
     * 最大秒数
     */
    private static final int MAX_TIME_SECOND = 24 * 60 * 60 - 1;

    /**
     * 计时器默认秒数
     */
    private static final int DEFAULT_TIME_SECOND = 60;


    /**
     * presenter
     */
    @Inject
    TimeTaskPresenter presenter;

    /**
     * 总的时间，单位为秒
     */
    private int allSeconds = DEFAULT_TIME_SECOND;

    /**
     * 剩余的时间
     */
    private long lastSecond;

    /**
     * 当前timer的状态
     */
    TimerStatus timerStatus = TimerStatus.Idle;

    /**
     * timer
     */
    private Rx2Timer timer;


    /**
     * task 存储
     */
    @Inject
    TimeTasksRepository timeTasksRepository;

    @Override
    protected void didBecomeActive(@Nullable Bundle savedInstanceState) {
        super.didBecomeActive(savedInstanceState);
        reset();


        presenter.upLongRequest()
                .to(new ObservableScoper<>(this))
                .subscribe(o -> {

            /**
             * 长按开始自动增加
             * 松开手指后取消
             */
            Observable.interval(100, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.computation())
                    .takeUntil(presenter.touchupObservable())
                    .concatMap(e -> secondsIncrementObservable())
                    .observeOn(AndroidSchedulers.mainThread())

                    .subscribe(b -> {

                        setTimeText(allSeconds);


                    });
        });
        presenter.downLongRequest()
                .to(new ObservableScoper<>(this))
                .subscribe(o -> {

            /**
             * 长按数字递减
             * 松开手指取消
             */
            Observable.interval(100, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.computation())
                    .takeUntil(presenter.touchupObservable())
                    .concatMap(e -> secondsDecrementObservable())
                    .observeOn(AndroidSchedulers.mainThread())
                    .to(new ObservableScoper<>(this))
                    .subscribe(b -> {

                        setTimeText(allSeconds);


                    });
        });


        /**
         * 点击增加
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
         * 点击减少
         */
        presenter.downRequest()
                .filter(o -> isTimerIdle())
                .concatMap(o -> secondsDecrementObservable())
                .to(new ObservableScoper<>(this))
                .subscribe(o -> {
                    setTimeText(allSeconds);
                });


        /**
         * 开始timer
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
         * 重置计时器
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
     * 点击startbutton，切换各种状态
     *
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
     * 开启计时器
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
     * 重启
     */
    private void resumeTimer() {
        if (timer != null) {
            timer.resume();
        }

    }


    /**
     * 计时器结束
     */
    public void endTimer() {

        if (timer != null) {

            saveTimeTask();
            reset();
        }
    }

    /**
     * 判断是否空闲
     * @return
     */
    private boolean isTimerIdle() {

        return timerStatus == TimerStatus.Idle;
    }

    /**
     * 是否可以启动定时器
     * @return
     */
    private boolean canStartTimer() {

        return !TextUtils.isEmpty(presenter.getTimeTaskName());
    }

    /**
     * 减少时间
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
     * 增加时间
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
     * 保存task记录
     */
    public void saveTimeTask() {

        TimeTaskEntity entity = new TimeTaskEntity();
        entity.setTaskName(presenter.getTimeTaskName());
        entity.setTotalSeconds(allSeconds);
        entity.setCreateTime(System.currentTimeMillis());
        entity.setFinishSeconds(lastSecond);
        entity.setFinished(lastSecond == 0);

        timeTasksRepository.saveTimeTask(entity)
                .observeOn(AndroidSchedulers.mainThread())
                .to(new ObservableScoper<>(this))

                .subscribe(o -> {
                    presenter.showToast("Save task success, " + entity.getFinishSeconds() + " " +
                            "seconds");

                });
    }


    /**
     * 设置当前time的时间
     * @param seconds
     */
    private void setTimeText(long seconds) {
        lastSecond = seconds;
        presenter.setTimeText(WUtils.stringFromTime(seconds));
    }

    /**
     * 重置各种状态
     */
    private void reset() {

        allSeconds = DEFAULT_TIME_SECOND;
        this.timerStatus = TimerStatus.Idle;
        chageStartButtonStatus(this.timerStatus);
        presenter.resetTimeTask();
        setTimeText(allSeconds);
    }


    /**
     * 切换start button的文字
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


        //up点击
        Observable<Object> upRequest();

        //up 长按
        Observable<Object> upLongRequest();

        //松开
        Observable<Object> touchupObservable();

        //down 点击
        Observable<Object> downRequest();

        //down 长按
        Observable<Object> downLongRequest();

        //reset 点击
        Observable<Object> resetRequest();

        /**
         * 返回 time task 名称
         * @return
         */
        String getTimeTaskName();

        /**
         * 开始button 点击
         * @return
         */
        Observable<Object> startRequest();

        //设置time label文字
        void setTimeText(String text);

        /**
         * 改变start button文字
         * @param textResID
         */
        public void chageStartButtonStatus(int textResID);

        /**
         * 重置
         */
        void resetTimeTask();

        /**
         * 显示toast
         * @param text
         */
        void showToast(String text);

        /**
         * 显示error
         * @param text
         */
        void showEditTextError(String text);

        /**
         * 禁用/启用 up/down button
         * @param enabled
         */
        void setUpAndDownEabled(boolean enabled);

        /**
         * 是否显示resetbutton
         * @param show
         */
        void showResetButton(boolean show);
    }


}

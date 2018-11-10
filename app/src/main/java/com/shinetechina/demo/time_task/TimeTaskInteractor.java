package com.shinetechina.demo.time_task;

import android.support.annotation.Nullable;
import android.util.Log;

import com.shinetechina.demo.common.Utils.WUtils;
import com.shinetechina.demo.common.data.TimeTasksRepository;
import com.shinetechina.demo.common.data.entities.TimeTaskEntity;
import com.uber.rib.core.Bundle;
import com.uber.rib.core.Interactor;
import com.uber.rib.core.RibInteractor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Coordinates Business Logic for {@link TimeTaskScope}.
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
public class TimeTaskInteractor
    extends Interactor<TimeTaskInteractor.TimeTaskPresenter, TimeTaskRouter> {

  private static final int DEFAULT_TIME_SECOND = 10;
  @Inject TimeTaskPresenter presenter;

  private int timeSecondCount = DEFAULT_TIME_SECOND;

  private AtomicLong lastTick;

    @Inject
    TimeTasksRepository timeTasksRepository;

  @Override
  protected void didBecomeActive(@Nullable Bundle savedInstanceState) {
    super.didBecomeActive(savedInstanceState);

      Log.e("w","didBecomeActive " + timeTasksRepository);


    setTimeText(timeSecondCount);
    presenter.upRequest()
            .subscribe(o ->
                        {

                          timeSecondCount ++;
                          setTimeText(timeSecondCount);

                        });



    presenter.downRequest()
            .subscribe(o -> {

              timeSecondCount --;
              setTimeText(timeSecondCount);
            });


    presenter.startRequest().doOnNext(o -> presenter.chageStartButtonStatus
            (false)).subscribe(o -> startTimer());

  }

  protected  void startTimer() {
      lastTick = new AtomicLong(timeSecondCount-1);

      Observable.interval(1,TimeUnit.SECONDS,Schedulers.io())
              .take(timeSecondCount)
              .map((Long along) -> (lastTick.getAndDecrement()))
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(l -> {
                  setTimeText(l);
                  if(l ==0) {

                    saveTimeTask();
                  }


              });

  }


  @Override
  protected void willResignActive() {
    super.willResignActive();

  }

  public void saveTimeTask() {

    TimeTaskEntity entity = new TimeTaskEntity();
    entity.setTaskName("task_name " + System.currentTimeMillis());
    entity.setTotalSeconds(timeSecondCount);
    entity.setCreateTime(System.currentTimeMillis());
    entity.setFinishSeconds(0);
    entity.setFinished(true);

    timeTasksRepository.saveTimeTask(entity).subscribe(o -> Log.e("w","insert success"));
  }


  private void setTimeText(long seconds) {

     presenter.setTimeText(WUtils.stringFromTime(seconds));
  }

  /**
   * Presenter interface implemented by this RIB's view.
   */
  interface TimeTaskPresenter {
    Observable<Object> upRequest();
    Observable<Object> downRequest();

    //get task name
//    Observable taskName();

    String getTimeTaskName();

    Observable startRequest();

    void setTimeText(String text);


    void chageStartButtonStatus(boolean start);
    void resetTimeTask();

  }
}

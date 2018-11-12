package com.gogovan.test.app.history;

import android.support.annotation.Nullable;
import android.util.Log;

import com.gogovan.test.app.common.data.TimeTasksRepository;
import com.gogovan.test.app.common.data.entities.TimeTaskEntity;
import com.uber.rib.core.Bundle;
import com.uber.rib.core.Interactor;
import com.uber.rib.core.RibInteractor;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Coordinates Business Logic for {@link HistoryScope}.
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
public class HistoryInteractor
    extends Interactor<HistoryInteractor.HistoryPresenter, HistoryRouter> {

  @Inject HistoryPresenter presenter;

  @Inject
  TimeTasksRepository repository;
  @Override
  protected void didBecomeActive(@Nullable Bundle savedInstanceState) {
    super.didBecomeActive(savedInstanceState);


      repository.getAllTask()
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(list -> {

                Log.e("w"," " + list);
                  presenter.loadData(list);
              });
  }

  @Override
  protected void willResignActive() {
    super.willResignActive();

  }


  /**
   * Presenter interface implemented by this RIB's view.
   */
  interface HistoryPresenter {

    void loadData(List<TimeTaskEntity> list);

  }
}

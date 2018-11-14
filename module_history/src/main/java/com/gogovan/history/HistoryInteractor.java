package com.gogovan.history;

import android.support.annotation.Nullable;

import com.gogovan.data.TimeTasksRepository;
import com.gogovan.data.entities.TimeTaskEntity;
import com.uber.autodispose.ObservableScoper;
import com.uber.rib.core.Bundle;
import com.uber.rib.core.Interactor;
import com.uber.rib.core.RibInteractor;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Coordinates Business Logic for {@link HistoryScope}.
 * <p>
 * 显示历史记录
 */
@RibInteractor
public class HistoryInteractor
        extends Interactor<HistoryInteractor.HistoryPresenter, HistoryRouter> {

    @Inject
    HistoryPresenter presenter;

    @Inject
    TimeTasksRepository repository;


    @Override
    protected void didBecomeActive(@Nullable Bundle savedInstanceState) {
        super.didBecomeActive(savedInstanceState);


        //从本地取出保存的task
        repository.getAllTask()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
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

        /**
         * 加载
         *
         * @param list
         */
        void loadData(List<TimeTaskEntity> list);

    }
}

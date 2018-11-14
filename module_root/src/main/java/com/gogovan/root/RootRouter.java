package com.gogovan.root;

import android.support.annotation.Nullable;

import com.gogovan.history.HistoryBuilder;
import com.gogovan.history.HistoryRouter;
import com.gogovan.timetask.TimeTaskBuilder;
import com.gogovan.timetask.TimeTaskRouter;
import com.uber.rib.core.ViewRouter;

/**
 * Adds and removes children of {@link RootBuilder.RootScope}.
 * <p>
 *
 * 页面跳转逻辑
 */
public class RootRouter extends
        ViewRouter<RootView, RootInteractor, RootBuilder.Component> {

    private final TimeTaskBuilder timeTaskBuilder;

    @Nullable
    private TimeTaskRouter timeTaskRouter;

    private final HistoryBuilder historyBuilder;

    private HistoryRouter historyRouter;

    private RootView rootView;

    public RootRouter(
            RootView view,
            RootInteractor interactor,
            RootBuilder.Component component, TimeTaskBuilder timeTaskBuilder, HistoryBuilder
                    historyBuilder) {
        super(view, interactor, component);

        this.rootView = view;
        this.timeTaskBuilder = timeTaskBuilder;
        this.historyBuilder = historyBuilder;

    }

    /**
     * 添加time页面
     */
    void attachTimeTaskView() {
        timeTaskRouter = timeTaskBuilder.build(getView());
        attachChild(timeTaskRouter);

        this.rootView.addPageView(timeTaskRouter.getView());

    }


    /**
     * 添加历史页面
     */
    void attachHistoryView() {

        if (historyRouter == null)
            historyRouter = historyBuilder.build(getView());
        attachChild(historyRouter);
        this.rootView.addPageView(historyRouter.getView());
    }

    /**
     * 移除历史页面
     */
    void detachHistoryView() {

        if (historyRouter != null) {

            detachChild(historyRouter);

            this.rootView.removePageView(historyRouter.getView());
        }

    }

}

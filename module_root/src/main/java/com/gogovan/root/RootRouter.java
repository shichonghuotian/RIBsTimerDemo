package com.gogovan.root;

import android.support.annotation.Nullable;

import com.gogovan.history.HistoryBuilder;
import com.gogovan.history.HistoryRouter;
import com.gogovan.timertask.TimerTaskBuilder;
import com.gogovan.timertask.TimerTaskRouter;
import com.uber.rib.core.ViewRouter;

/**
 * Adds and removes children of {@link RootBuilder.RootScope}.
 * <p>
 *
 * 页面跳转逻辑
 */
public class RootRouter extends
        ViewRouter<RootView, RootInteractor, RootBuilder.Component> {

    private final TimerTaskBuilder timerTaskBuilder;

    @Nullable
    private TimerTaskRouter timerTaskRouter;

    private final HistoryBuilder historyBuilder;

    private HistoryRouter historyRouter;

    private RootView rootView;

    public RootRouter(
            RootView view,
            RootInteractor interactor,
            RootBuilder.Component component, TimerTaskBuilder timerTaskBuilder, HistoryBuilder
                    historyBuilder) {
        super(view, interactor, component);

        this.rootView = view;
        this.timerTaskBuilder = timerTaskBuilder;
        this.historyBuilder = historyBuilder;

    }

    /**
     * 添加time页面
     */
    void attachTimeTaskView() {
        timerTaskRouter = timerTaskBuilder.build(getView());
        attachChild(timerTaskRouter);

        this.rootView.addPageView(timerTaskRouter.getView());

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

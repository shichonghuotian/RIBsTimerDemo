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
 * <p>
 * page switch logic
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
     * attach time task view
     */
    void attachTimeTaskView() {
        timerTaskRouter = timerTaskBuilder.build(getView());
        attachChild(timerTaskRouter);

        this.rootView.addPageView(timerTaskRouter.getView());

    }


    /**
     * attach history view
     */
    void attachHistoryView() {

        if (historyRouter == null)
            historyRouter = historyBuilder.build(getView());
        attachChild(historyRouter);
        this.rootView.addPageView(historyRouter.getView());
    }

    /**
     * detach history view
     */
    void detachHistoryView() {

        if (historyRouter != null) {

            detachChild(historyRouter);

            this.rootView.removePageView(historyRouter.getView());
        }

    }

}

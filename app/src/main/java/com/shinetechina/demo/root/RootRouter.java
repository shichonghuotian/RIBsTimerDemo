package com.shinetechina.demo.root;

import android.support.annotation.Nullable;
import android.util.Log;

import com.shinetechina.demo.history.HistoryBuilder;
import com.shinetechina.demo.history.HistoryRouter;
import com.shinetechina.demo.timetask.TimeTaskBuilder;
import com.shinetechina.demo.timetask.TimeTaskRouter;
import com.uber.rib.core.ViewRouter;

/**
 * Adds and removes children of {@link RootBuilder.RootScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
public class RootRouter extends
    ViewRouter<RootView, RootInteractor, RootBuilder.Component> {

  private final TimeTaskBuilder timeTaskBuilder;

  @Nullable private TimeTaskRouter timeTaskRouter;

  private final HistoryBuilder historyBuilder;

  private HistoryRouter historyRouter;

  private RootView rootView;

  public RootRouter(
      RootView view,
      RootInteractor interactor,
      RootBuilder.Component component,TimeTaskBuilder timeTaskBuilder,HistoryBuilder historyBuilder) {
    super(view, interactor, component);

    this.rootView = view;
    this.timeTaskBuilder = timeTaskBuilder;
    this.historyBuilder = historyBuilder;

  }

  void attachTimeTaskView() {
    timeTaskRouter = timeTaskBuilder.build(getView());
    attachChild(timeTaskRouter);
//    getView().addView(timeTaskRouter.getView());

    this.rootView.addPageView(timeTaskRouter.getView());

  }



  void attachHistoryView() {

    if(historyRouter == null)
      historyRouter = historyBuilder.build(getView());
    attachChild(historyRouter);
//    getView().addView(historyRouter.getView());

    this.rootView.addPageView(historyRouter.getView());
  }

  void detachHistoryView() {

    if(historyRouter != null) {
      Log.e("w","detachHistoryView");

      detachChild(historyRouter);

      this.rootView.removePageView(historyRouter.getView());
    }

  }

}

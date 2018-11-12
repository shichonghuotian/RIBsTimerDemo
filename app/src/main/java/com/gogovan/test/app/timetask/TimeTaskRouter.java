package com.gogovan.test.app.timetask;

import com.uber.rib.core.ViewRouter;

/**
 * Adds and removes children of {@link TimeTaskBuilder.TimeTaskScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
public class TimeTaskRouter extends
    ViewRouter<TimeTaskView, TimeTaskInteractor, TimeTaskBuilder.Component> {

  public TimeTaskRouter(
      TimeTaskView view,
      TimeTaskInteractor interactor,
      TimeTaskBuilder.Component component) {
    super(view, interactor, component);
  }
}

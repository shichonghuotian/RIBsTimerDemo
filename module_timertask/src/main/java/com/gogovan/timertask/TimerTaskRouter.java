package com.gogovan.timertask;

import com.uber.rib.core.ViewRouter;

/**
 * Adds and removes children of {@link TimerTaskBuilder.TimeTaskScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
public class TimerTaskRouter extends
    ViewRouter<TimerTaskView, TimerTaskInteractor, TimerTaskBuilder.Component> {

  public TimerTaskRouter(
          TimerTaskView view,
          TimerTaskInteractor interactor,
          TimerTaskBuilder.Component component) {
    super(view, interactor, component);
  }
}

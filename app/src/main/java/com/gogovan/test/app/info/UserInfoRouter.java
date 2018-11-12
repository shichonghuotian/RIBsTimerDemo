package com.gogovan.test.app.info;

import com.uber.rib.core.ViewRouter;

/**
 * Adds and removes children of {@link UserInfoBuilder.UserInfoScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
public class UserInfoRouter extends
    ViewRouter<UserInfoView, UserInfoInteractor, UserInfoBuilder.Component> {

  public UserInfoRouter(
      UserInfoView view,
      UserInfoInteractor interactor,
      UserInfoBuilder.Component component) {
    super(view, interactor, component);
  }

}

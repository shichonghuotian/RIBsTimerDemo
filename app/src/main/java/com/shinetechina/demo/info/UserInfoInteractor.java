package com.shinetechina.demo.info;

import android.support.annotation.Nullable;
import android.util.Log;

import com.uber.rib.core.Bundle;
import com.uber.rib.core.Interactor;
import com.uber.rib.core.RibInteractor;
import com.uber.rib.core.Presenter;
import com.uber.rib.core.Router;

import javax.inject.Inject;

/**
 * Coordinates Business Logic for {@link UserInfoScope}.
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
public class UserInfoInteractor
    extends Interactor<UserInfoInteractor.UserInfoPresenter, UserInfoRouter> {

  @Inject UserInfoPresenter presenter;

  @Override
  protected void didBecomeActive(@Nullable Bundle savedInstanceState) {
    super.didBecomeActive(savedInstanceState);

    // TODO: Add attachment logic here (RxSubscriptions, etc.).
  }

  @Override
  protected void willResignActive() {
    super.willResignActive();

    // TODO: Perform any required clean up here, or delete this method entirely if not needed.
  }

  @Override
  public boolean handleBackPress() {

    Log.e("w","UserInfoInteractor handleBackPress");
    return super.handleBackPress();

  }

  /**
   * Presenter interface implemented by this RIB's view.
   */
  interface UserInfoPresenter { }
}

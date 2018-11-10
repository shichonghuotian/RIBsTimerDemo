package com.shinetechina.demo.root;

import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.util.Log;

import com.jakewharton.rxbinding2.support.design.widget.RxNavigationView;
import com.shinetechina.demo.R;
import com.uber.rib.core.Bundle;
import com.uber.rib.core.Interactor;
import com.uber.rib.core.RibInteractor;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Coordinates Business Logic for {@link RootScope}.
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
public class RootInteractor
    extends Interactor<RootInteractor.RootPresenter, RootRouter> {

  @Inject
  RootPresenter presenter;

  @Override
  protected void didBecomeActive(@Nullable Bundle savedInstanceState) {
    super.didBecomeActive(savedInstanceState);

    getRouter().attachTimeTaskView();

//      getRouter().attachHistoryView();


    presenter.navItemSelectionRequest()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(menuType -> {

                if(menuType ==NavMenuType.TIME) {
                    getRouter().detachHistoryView();

                }else if(menuType == NavMenuType.HISTORY){

                    getRouter().attachHistoryView();

                }


            });


  }

  @Override
  protected void willResignActive() {
    super.willResignActive();


  }

  @Override
  public boolean handleBackPress() {

    Log.e("W","back>>>");
    return false;
  }

  /**
   * Presenter interface implemented by this RIB's view.
   */
  interface RootPresenter {

    Observable navItemSelectionRequest();
  }
}

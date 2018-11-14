package com.gogovan.root;

import android.support.annotation.Nullable;

import com.uber.rib.core.Bundle;
import com.uber.rib.core.Interactor;
import com.uber.rib.core.RibInteractor;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Coordinates Business Logic for {@link RootScope}.
 *
 * 处理root 页面的业务逻辑
 */
@RibInteractor
public class RootInteractor
    extends Interactor<RootInteractor.RootPresenter, RootRouter> {

  @Inject
  RootPresenter presenter;

  @Override
  protected void didBecomeActive(@Nullable Bundle savedInstanceState) {
    super.didBecomeActive(savedInstanceState);

    //默认添加taskview
    getRouter().attachTimeTaskView();

    presenter.navItemSelectionRequest()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(o -> {
                presenter.closeDrawer();;

            })
            .subscribe(menuType -> {

                if(menuType ==NavMenuType.TIMER) {
                    getRouter().detachHistoryView();

                    presenter.setToolbarTitle(R.string.str_timer);

                }else if(menuType == NavMenuType.HISTORY){

                    getRouter().attachHistoryView();
                    presenter.setToolbarTitle(R.string.str_history);

                }


            });


  }

  @Override
  protected void willResignActive() {
    super.willResignActive();


  }

  @Override
  public boolean handleBackPress() {

    return false;
  }

  /**
   * Presenter interface implemented by this RIB's view.
   */
  interface RootPresenter {

      /**
       * item 选择
       * @return
       */
    Observable navItemSelectionRequest();

      /**
       * 关闭drawer
       */
    void closeDrawer();

      /**
       * 设置toolbar
       * @param titleResId
       */
     void setToolbarTitle(int titleResId);
  }
}

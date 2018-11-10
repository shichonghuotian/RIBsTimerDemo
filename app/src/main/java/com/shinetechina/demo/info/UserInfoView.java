package com.shinetechina.demo.info;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Top level view for {@link UserInfoBuilder.UserInfoScope}.
 */
class UserInfoView extends LinearLayout implements UserInfoInteractor.UserInfoPresenter {

  public UserInfoView(Context context) {
    this(context, null);
  }

  public UserInfoView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public UserInfoView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }


}

package com.shinetechina.demo.info;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.shinetechina.demo.R;
import com.uber.rib.core.InteractorBaseComponent;
import com.uber.rib.core.ViewBuilder;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;
import javax.inject.Scope;

import dagger.Binds;
import dagger.BindsInstance;
import dagger.Provides;

import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Builder for the {@link UserInfoScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
public class UserInfoBuilder
    extends ViewBuilder<UserInfoView, UserInfoRouter, UserInfoBuilder.ParentComponent> {

  public UserInfoBuilder(ParentComponent dependency) {
    super(dependency);
  }

  /**
   * Builds a new {@link UserInfoRouter}.
   *
   * @param parentViewGroup parent view group that this router's view will be added to.
   * @return a new {@link UserInfoRouter}.
   */
  public UserInfoRouter build(ViewGroup parentViewGroup) {
    UserInfoView view = createView(parentViewGroup);
    UserInfoInteractor interactor = new UserInfoInteractor();
    Component component = DaggerUserInfoBuilder_Component.builder()
        .parentComponent(getDependency())
        .view(view)
        .interactor(interactor)
        .build();
    return component.userinfoRouter();
  }

  @Override
  protected UserInfoView inflateView(LayoutInflater inflater, ViewGroup parentViewGroup) {
    // TODO: Inflate a new view using the provided inflater, or create a new view programatically using the
    // provided context from the parentViewGroup.
    return (UserInfoView) inflater.inflate(R.layout.user_info_rib,parentViewGroup,false);
  }

  public interface ParentComponent {
    // TODO: Define dependencies required from your parent interactor here.
  }

  @dagger.Module
  public abstract static class Module {

    @UserInfoScope
    @Binds
    abstract UserInfoInteractor.UserInfoPresenter presenter(UserInfoView view);

    @UserInfoScope
    @Provides
    static UserInfoRouter router(
      Component component,
      UserInfoView view,
      UserInfoInteractor interactor) {
      return new UserInfoRouter(view, interactor, component);
    }

    // TODO: Create provider methods for dependencies created by this Rib. These should be static.
  }

  @UserInfoScope
  @dagger.Component(modules = Module.class,
       dependencies = ParentComponent.class)
  interface Component extends InteractorBaseComponent<UserInfoInteractor>, BuilderComponent {

    @dagger.Component.Builder
    interface Builder {
      @BindsInstance
      Builder interactor(UserInfoInteractor interactor);
      @BindsInstance
      Builder view(UserInfoView view);
      Builder parentComponent(ParentComponent component);
      Component build();
    }
  }

  interface BuilderComponent  {
    UserInfoRouter userinfoRouter();
  }

  @Scope
  @Retention(CLASS)
  @interface UserInfoScope { }

  @Qualifier
  @Retention(CLASS)
  @interface UserInfoInternal { }
}

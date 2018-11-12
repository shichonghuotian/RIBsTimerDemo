package com.gogovan.test.app.history;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.shinetechina.demo.R;
import com.gogovan.test.app.common.di.AppComponent;
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
 * Builder for the {@link HistoryScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
public class HistoryBuilder
    extends ViewBuilder<HistoryView, HistoryRouter, HistoryBuilder.ParentComponent> {

  public HistoryBuilder(ParentComponent dependency) {
    super(dependency);
  }

  /**
   * Builds a new {@link HistoryRouter}.
   *
   * @param parentViewGroup parent view group that this router's view will be added to.
   * @return a new {@link HistoryRouter}.
   */
  public HistoryRouter build(ViewGroup parentViewGroup) {
    HistoryView view = createView(parentViewGroup);
    HistoryInteractor interactor = new HistoryInteractor();
    Component component = DaggerHistoryBuilder_Component.builder()
        .parentComponent(getDependency())
        .view(view)
        .interactor(interactor)
        .build();
    return component.historyRouter();
  }

  @Override
  protected HistoryView inflateView(LayoutInflater inflater, ViewGroup parentViewGroup) {
    // TODO: Inflate a new view using the provided inflater, or create a new view programatically using the
    // provided context from the parentViewGroup.
    return (HistoryView)inflater.inflate(R.layout.hishory_rib,parentViewGroup,false);
  }

  public interface ParentComponent extends AppComponent {
    // TODO: Define dependencies required from your parent interactor here.
  }

  @dagger.Module
  public abstract static class Module {

    @HistoryScope
    @Binds
    abstract HistoryInteractor.HistoryPresenter presenter(HistoryView view);

    @HistoryScope
    @Provides
    static HistoryRouter router(
      Component component,
      HistoryView view,
      HistoryInteractor interactor) {
      return new HistoryRouter(view, interactor, component);
    }

    // TODO: Create provider methods for dependencies created by this Rib. These should be static.
  }

  @HistoryScope
  @dagger.Component(modules = Module.class,
       dependencies = ParentComponent.class)
  interface Component extends InteractorBaseComponent<HistoryInteractor>, BuilderComponent {

    @dagger.Component.Builder
    interface Builder {
      @BindsInstance
      Builder interactor(HistoryInteractor interactor);
      @BindsInstance
      Builder view(HistoryView view);
      Builder parentComponent(ParentComponent component);
      Component build();
    }
  }

  interface BuilderComponent  {
    HistoryRouter historyRouter();
  }

  @Scope
  @Retention(CLASS)
  @interface HistoryScope { }

  @Qualifier
  @Retention(CLASS)
  @interface HistoryInternal { }
}

package com.gogovan.timetask;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gogovan.di.AppComponent;
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
 * Builder for the {@link TimeTaskScope}.
 * <p>
 * TODO describe this scope's responsibility as a whole.
 */
public class TimeTaskBuilder
        extends ViewBuilder<TimeTaskView, TimeTaskRouter, TimeTaskBuilder.ParentComponent> {

    public TimeTaskBuilder(ParentComponent dependency) {
        super(dependency);
    }

    /**
     * Builds a new {@link TimeTaskRouter}.
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new {@link TimeTaskRouter}.
     */
    public TimeTaskRouter build(ViewGroup parentViewGroup) {
        TimeTaskView view = createView(parentViewGroup);
        TimeTaskInteractor interactor = new TimeTaskInteractor();
        Component component = DaggerTimeTaskBuilder_Component.builder()
                .parentComponent(getDependency())
                .view(view)
                .interactor(interactor)
                .build();
        return component.timetaskRouter();
    }

    @Override
    protected TimeTaskView inflateView(LayoutInflater inflater, ViewGroup parentViewGroup) {

        return (TimeTaskView) inflater.inflate(R.layout.time_task_rib, parentViewGroup, false);
    }

    /**
     * 继承appcomponet
     */
    public interface ParentComponent extends AppComponent {

    }

    @dagger.Module
    public abstract static class Module {

        @TimeTaskScope
        @Binds
        abstract TimeTaskInteractor.TimeTaskPresenter presenter(TimeTaskView view);

        @TimeTaskScope
        @Provides
        static TimeTaskRouter router(
                Component component,
                TimeTaskView view,
                TimeTaskInteractor interactor) {
            return new TimeTaskRouter(view, interactor, component);
        }

    }

    @TimeTaskScope
    @dagger.Component(modules = Module.class,
            dependencies = ParentComponent.class)
    interface Component extends InteractorBaseComponent<TimeTaskInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            Builder interactor(TimeTaskInteractor interactor);

            @BindsInstance
            Builder view(TimeTaskView view);

            Builder parentComponent(ParentComponent component);

            Component build();
        }
    }

    interface BuilderComponent {
        TimeTaskRouter timetaskRouter();
    }

    @Scope
    @Retention(CLASS)
    @interface TimeTaskScope {
    }

    @Qualifier
    @Retention(CLASS)
    @interface TimeTaskInternal {
    }
}

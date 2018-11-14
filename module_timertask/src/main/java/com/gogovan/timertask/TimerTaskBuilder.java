package com.gogovan.timertask;

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
public class TimerTaskBuilder
        extends ViewBuilder<TimerTaskView, TimerTaskRouter, TimerTaskBuilder.ParentComponent> {

    public TimerTaskBuilder(ParentComponent dependency) {
        super(dependency);
    }

    /**
     * Builds a new {@link TimerTaskRouter}.
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new {@link TimerTaskRouter}.
     */
    public TimerTaskRouter build(ViewGroup parentViewGroup) {
        TimerTaskView view = createView(parentViewGroup);
        TimerTaskInteractor interactor = new TimerTaskInteractor();
        Component component = DaggerTimerTaskBuilder_Component.builder()
                .parentComponent(getDependency())
                .view(view)
                .interactor(interactor)
                .build();
        return component.timetaskRouter();
    }

    @Override
    protected TimerTaskView inflateView(LayoutInflater inflater, ViewGroup parentViewGroup) {

        return (TimerTaskView) inflater.inflate(R.layout.timer_task_rib, parentViewGroup, false);
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
        abstract TimerTaskInteractor.TimeTaskPresenter presenter(TimerTaskView view);

        @TimeTaskScope
        @Provides
        static TimerTaskRouter router(
                Component component,
                TimerTaskView view,
                TimerTaskInteractor interactor) {
            return new TimerTaskRouter(view, interactor, component);
        }

    }

    @TimeTaskScope
    @dagger.Component(modules = Module.class,
            dependencies = ParentComponent.class)
    interface Component extends InteractorBaseComponent<TimerTaskInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            Builder interactor(TimerTaskInteractor interactor);

            @BindsInstance
            Builder view(TimerTaskView view);

            Builder parentComponent(ParentComponent component);

            Component build();
        }
    }

    interface BuilderComponent {
        TimerTaskRouter timetaskRouter();
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

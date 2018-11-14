package com.gogovan.test.app;

import android.view.ViewGroup;

import com.gogovan.data.TimerTasksRepository;
import com.gogovan.root.RootBuilder;
import com.uber.rib.core.RibActivity;
import com.uber.rib.core.ViewRouter;

/**
 * root activity entry
 */
public class RootActivity extends RibActivity {

    @Override
    protected ViewRouter<?, ?, ?> createRouter(ViewGroup parentViewGroup) {

        SampleApplication sampleApplication = (SampleApplication)getApplication();

        RootBuilder rootBuilder = new RootBuilder(new RootBuilder.ParentComponent() {

            @Override
            public TimerTasksRepository getTimerTasksRepository() {
                return  sampleApplication.getAppComponent().getTimerTasksRepository();
            }
        });

        return rootBuilder.build(parentViewGroup);

    }
}

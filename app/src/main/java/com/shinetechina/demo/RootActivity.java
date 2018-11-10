package com.shinetechina.demo;

import android.view.ViewGroup;

import com.shinetechina.demo.common.data.TimeTasksRepository;
import com.shinetechina.demo.root.RootBuilder;
import com.uber.rib.core.RibActivity;
import com.uber.rib.core.ViewRouter;

public class RootActivity extends RibActivity {




    @Override
    protected ViewRouter<?, ?, ?> createRouter(ViewGroup parentViewGroup) {

        SampleApplication sampleApplication = (SampleApplication)getApplication();



        RootBuilder rootBuilder = new RootBuilder(new RootBuilder.ParentComponent() {

            @Override
            public TimeTasksRepository getTimeTasksRepository() {
                return  sampleApplication.getAppComponent().getTimeTasksRepository();
            }
        });

        return rootBuilder.build(parentViewGroup);

    }
}

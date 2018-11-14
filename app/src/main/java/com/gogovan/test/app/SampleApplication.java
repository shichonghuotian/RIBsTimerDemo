package com.gogovan.test.app;

import android.app.Application;

import com.gogovan.di.AppComponent;
import com.gogovan.di.DaggerAppComponent;

/**
 * application which use dagger2 to load database
 * Created by Arthur on 2018/11/8.
 */
public class SampleApplication extends Application {

    /**
     * appComponent
     */
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent =  DaggerAppComponent.builder().application(this).build();

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}

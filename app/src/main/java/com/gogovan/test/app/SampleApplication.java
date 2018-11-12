package com.gogovan.test.app;

import android.app.Application;

import com.gogovan.test.app.common.di.AppComponent;
import com.gogovan.test.app.common.di.DaggerAppComponent;

/**
 * Created by Arthur on 2018/11/8.
 */
public class SampleApplication extends Application {

    private AppComponent appComponent;


//    @Inject
//
//    @Inject
//    AppDataBase dataBase;


//    @Inject TimeTaskDao dao;



    @Override
    public void onCreate() {
        super.onCreate();

//        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();

        appComponent =  DaggerAppComponent.builder().application(this).build();
//        appComponent.inject(this);


//        Log.e("w","application oncreate "  + " " + timeTasksRepository);


    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}

package com.gogovan.test.app.common.di;

import android.app.Application;
import android.content.Context;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Arthur on 2018/11/9.
 */
@Module
public abstract class AppModule {

    //expose Application as an injectable context
    @Binds
    abstract Context bindContext(Application application);
}
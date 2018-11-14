package com.gogovan.di;

import android.app.Application;

import com.gogovan.data.TimerTasksRepository;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/**
 * Created by Arthur on 2018/11/9.
 */
@Singleton
@Component(modules = {TimerTasksRepositoryModule.class,AppModule.class})
public interface AppComponent {


     TimerTasksRepository getTimerTasksRepository();

     @Component.Builder
     interface Builder {

          @BindsInstance
          Builder application(Application application);

          AppComponent build();
     }
}

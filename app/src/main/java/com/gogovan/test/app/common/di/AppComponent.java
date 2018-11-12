package com.gogovan.test.app.common.di;

import android.app.Application;

import com.gogovan.test.app.common.data.TimeTasksRepository;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/**
 * Created by Arthur on 2018/11/9.
 */
@Singleton
@Component(modules = {TimeTasksRepositoryModule.class,AppModule.class})
public interface AppComponent {


     TimeTasksRepository getTimeTasksRepository();

//     void inject(SampleApplication application);

     @Component.Builder
     interface Builder {

          @BindsInstance
          Builder application(Application application);

          AppComponent build();
     }
}

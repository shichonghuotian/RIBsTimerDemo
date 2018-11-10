package com.shinetechina.demo.common.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.shinetechina.demo.common.data.TimeTasksRepository;
import com.shinetechina.demo.common.data.dao.TimeTaskDao;
import com.shinetechina.demo.common.data.db.AppDataBase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Arthur on 2018/11/10.
 */
@Module
public class TimeTasksRepositoryModule {
    private static final String DB_NAME = "RIBsDemo.db";


    @Singleton
    @Provides
    AppDataBase provideDb(Application context) {

        return Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class,
                DB_NAME).build();
    }

    @Singleton
    @Provides
    TimeTaskDao provideTaskDao(AppDataBase db) {

        return db.getTimeTaskDao();
    }

    @Singleton
    @Provides
    TimeTasksRepository provideRepository(TimeTaskDao timeTaskDao) {

        return new TimeTasksRepository(timeTaskDao);
    }
}

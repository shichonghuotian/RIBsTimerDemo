package com.gogovan.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.gogovan.data.TimerTasksRepository;
import com.gogovan.data.dao.TimerTaskDao;
import com.gogovan.data.db.AppDataBase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * define database singleton instance
 * Created by Arthur on 2018/11/10.
 */
@Module
public class TimerTasksRepositoryModule {
    /**
     * database name
     */
    private static final String DB_NAME = "RIBsDemo.db";


    @Singleton
    @Provides
    AppDataBase provideDb(Application context) {

        return Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class,
                DB_NAME).build();
    }

    @Singleton
    @Provides
    TimerTaskDao provideTaskDao(AppDataBase db) {

        return db.getTimerTaskDao();
    }

    @Singleton
    @Provides
    TimerTasksRepository provideRepository(TimerTaskDao timeTaskDao) {

        return new TimerTasksRepository(timeTaskDao);
    }
}

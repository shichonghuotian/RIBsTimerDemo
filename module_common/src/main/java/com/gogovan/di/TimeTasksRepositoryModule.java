package com.gogovan.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.gogovan.data.TimeTasksRepository;
import com.gogovan.data.dao.TimeTaskDao;
import com.gogovan.data.db.AppDataBase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 定义数据库的单例
 * Created by Arthur on 2018/11/10.
 */
@Module
public class TimeTasksRepositoryModule {
    /**
     * 数据库名称
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
    TimeTaskDao provideTaskDao(AppDataBase db) {

        return db.getTimeTaskDao();
    }

    @Singleton
    @Provides
    TimeTasksRepository provideRepository(TimeTaskDao timeTaskDao) {

        return new TimeTasksRepository(timeTaskDao);
    }
}

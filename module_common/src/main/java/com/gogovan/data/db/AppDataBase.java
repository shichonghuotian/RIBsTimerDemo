package com.gogovan.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.gogovan.data.dao.TimerTaskDao;
import com.gogovan.data.entities.TimerTaskEntity;

/**
 * room 数据库，
 * Created by Arthur on 2018/11/9.
 */
@Database(entities = {TimerTaskEntity.class},version =  2, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract TimerTaskDao getTimerTaskDao();
}

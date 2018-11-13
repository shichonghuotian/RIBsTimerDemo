package com.gogovan.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.gogovan.data.dao.TimeTaskDao;
import com.gogovan.data.entities.TimeTaskEntity;

/**
 * room 数据库，
 * Created by Arthur on 2018/11/9.
 */
@Database(entities = {TimeTaskEntity.class},version =  2, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract TimeTaskDao getTimeTaskDao();
}

package com.gogovan.test.app.common.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.gogovan.test.app.common.data.dao.TimeTaskDao;
import com.gogovan.test.app.common.data.entities.TimeTaskEntity;

/**
 * Created by Arthur on 2018/11/9.
 */
@Database(entities = {TimeTaskEntity.class},version =  2, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract TimeTaskDao getTimeTaskDao();


    //
}

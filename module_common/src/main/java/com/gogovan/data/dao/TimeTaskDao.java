package com.gogovan.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.gogovan.data.entities.TimeTaskEntity;

import java.util.List;

import io.reactivex.Maybe;

/**
 * task的数据库操作
 * Created by Arthur on 2018/11/9.
 */
@Dao
public interface TimeTaskDao {

    /**
     * 插入一套task
     * @param tasks
     */
    @Insert()
    void insertAll(TimeTaskEntity... tasks);

    /**
     * 查询所有的task
     * @return
     */
    @Query("select * from t_time_task")
    Maybe<List<TimeTaskEntity>> getAll();

}

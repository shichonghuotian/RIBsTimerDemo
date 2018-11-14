package com.gogovan.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.gogovan.data.entities.TimerTaskEntity;

import java.util.List;

import io.reactivex.Maybe;

/**
 * task database operations
 * Created by Arthur on 2018/11/9.
 */
@Dao
public interface TimerTaskDao {

    /**
     * insert a suite of task
     * @param tasks
     */
    @Insert()
    void insertAll(TimerTaskEntity... tasks);

    /**
     * query all tasks
     * @return
     */
    @Query("select * from t_timer_task")
    Maybe<List<TimerTaskEntity>> getAll();

}

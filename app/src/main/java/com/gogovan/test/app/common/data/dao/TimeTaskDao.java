package com.gogovan.test.app.common.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.gogovan.test.app.common.data.entities.TimeTaskEntity;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by Arthur on 2018/11/9.
 */
@Dao
public interface TimeTaskDao {

    @Insert()
    void insertAll(TimeTaskEntity... tasks);

    @Query("select * from t_time_task")
    Maybe<List<TimeTaskEntity>> getAll();

}

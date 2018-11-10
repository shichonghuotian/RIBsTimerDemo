package com.shinetechina.demo.common.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Arthur on 2018/11/9.
 */
@Entity(tableName = "t_time_task")
public class TimeTaskEntity {

    @PrimaryKey(autoGenerate = true)
    private long tid;

    @ColumnInfo(name = "createTime")
    private long createTime;


    private String taskName;

    private long totalSeconds;

    private long finishSeconds;

    private Boolean isFinished;

    @Ignore
    public TimeTaskEntity() {
    }

    @Ignore
    public TimeTaskEntity(long createTime) {
        this.createTime = createTime;
    }


    public TimeTaskEntity(long createTime, long totalSeconds, long finishSeconds, Boolean isFinished) {
        this.createTime = createTime;
        this.totalSeconds = totalSeconds;
        this.finishSeconds = finishSeconds;
        this.isFinished = isFinished;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getTotalSeconds() {
        return totalSeconds;
    }

    public void setTotalSeconds(long totalSeconds) {
        this.totalSeconds = totalSeconds;
    }

    public long getFinishSeconds() {
        return finishSeconds;
    }

    public void setFinishSeconds(long finishSeconds) {
        this.finishSeconds = finishSeconds;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public String toString() {
        return "TimeTaskEntity{" +
                "tid=" + tid +
                ", createTime=" + createTime +
                ", taskName='" + taskName + '\'' +
                ", totalSeconds=" + totalSeconds +
                ", finishSeconds=" + finishSeconds +
                ", isFinished=" + isFinished +
                '}';
    }
}

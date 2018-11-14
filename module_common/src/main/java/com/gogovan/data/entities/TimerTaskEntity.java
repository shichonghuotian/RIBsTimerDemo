package com.gogovan.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Task database entity
 * Created by Arthur on 2018/11/9.
 */
@Entity(tableName = "t_timer_task")
public class TimerTaskEntity {

    /**
     * auto-increasing identity
     */
    @PrimaryKey(autoGenerate = true)
    private long tid;

    /**
     * created time
     */
    private long createTime;

    /**
     * task name
     */
    private String taskName;

    /**
     * total time
     */
    private long totalSeconds;

    /**
     * finish time
     */
    private long finishSeconds;

    /**
     * finish flag
     */
    private Boolean isFinished;

    @Ignore
    public TimerTaskEntity() {
    }

    @Ignore
    public TimerTaskEntity(long createTime) {
        this.createTime = createTime;
    }


    public TimerTaskEntity(long createTime, long totalSeconds, long finishSeconds, Boolean isFinished) {
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
        return "TimerTaskEntity{" +
                "tid=" + tid +
                ", createTime=" + createTime +
                ", taskName='" + taskName + '\'' +
                ", totalSeconds=" + totalSeconds +
                ", finishSeconds=" + finishSeconds +
                ", isFinished=" + isFinished +
                '}';
    }
}

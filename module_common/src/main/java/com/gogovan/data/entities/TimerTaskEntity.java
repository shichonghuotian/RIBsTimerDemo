package com.gogovan.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * 定义task 数据类
 * Created by Arthur on 2018/11/9.
 */
@Entity(tableName = "t_timer_task")
public class TimerTaskEntity {

    /**
     * 自增的id
     */
    @PrimaryKey(autoGenerate = true)
    private long tid;

    /**
     * 创建时间
     */
    private long createTime;

    /**
     * task 名
     */
    private String taskName;

    /**
     * 总秒数
     */
    private long totalSeconds;

    /**
     * 在第几秒结束
     */
    private long finishSeconds;

    /**
     * 是否完成
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

package com.gogovan.data;

import com.gogovan.data.dao.TimerTaskDao;
import com.gogovan.data.entities.TimerTaskEntity;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;


/**
 * 定义一个仓库类，避免直接操作本地数据库
 * Created by Arthur on 2018/11/10.
 */
public class TimerTasksRepository {

    private final TimerTaskDao mTimerTaskDao;

    public TimerTasksRepository(TimerTaskDao mTimerTaskDao) {
        this.mTimerTaskDao = mTimerTaskDao;
    }


    /**
     * 插入记录
     * @param taskEntity
     * @return
     */
    public Observable<Boolean> saveTimeTask(final TimerTaskEntity taskEntity) {

     return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                mTimerTaskDao.insertAll(taskEntity);
                emitter.onNext(true);

                emitter.onComplete();

            }
        }).subscribeOn(Schedulers.io());

    }

    public Maybe<List<TimerTaskEntity>> getAllTask() {

        return mTimerTaskDao.getAll().subscribeOn(Schedulers.io());
    }
}

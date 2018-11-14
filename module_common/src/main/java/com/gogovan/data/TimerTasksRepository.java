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
 * define repository to avoid to operate local database directly
 * Created by Arthur on 2018/11/10.
 */
public class TimerTasksRepository {

    private final TimerTaskDao mTimerTaskDao;

    public TimerTasksRepository(TimerTaskDao mTimerTaskDao) {
        this.mTimerTaskDao = mTimerTaskDao;
    }


    /**
     * save task record
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

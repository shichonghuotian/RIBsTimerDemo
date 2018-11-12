package com.gogovan.test.app.common.data;

import com.gogovan.test.app.common.data.dao.TimeTaskDao;
import com.gogovan.test.app.common.data.entities.TimeTaskEntity;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Arthur on 2018/11/10.
 */
public class TimeTasksRepository {

    private final TimeTaskDao mTimeTaskDao;

    public TimeTasksRepository(TimeTaskDao mTimeTaskDao) {
        this.mTimeTaskDao = mTimeTaskDao;
    }


    /**
     * 插入记录
     * @param taskEntity
     * @return
     */
    public Observable<Boolean> saveTimeTask(TimeTaskEntity taskEntity) {

     return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                mTimeTaskDao.insertAll(taskEntity);
                emitter.onNext(true);

                emitter.onComplete();

            }
        }).subscribeOn(Schedulers.io());

    }

    public Maybe<List<TimeTaskEntity>> getAllTask() {

        return mTimeTaskDao.getAll().subscribeOn(Schedulers.io());
    }
}

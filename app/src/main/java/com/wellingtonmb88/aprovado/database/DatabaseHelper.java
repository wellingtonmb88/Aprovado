package com.wellingtonmb88.aprovado.database;

import com.wellingtonmb88.aprovado.database.realm.RealmDataSource;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.realm.RealmObject;
import rx.Observable;
import rx.schedulers.Schedulers;

public class DatabaseHelper<T extends RealmObject> {

    private final static ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    private final RealmDataSource<T> mRealmDataSource;

    public DatabaseHelper(RealmDataSource<T> realmDataSource) {
        mRealmDataSource = realmDataSource;
    }

    public void createIfNotExists(final T entity) {
        EXECUTOR_SERVICE.execute(new Runnable() {
            @Override
            public void run() {
                mRealmDataSource.insert(entity);
            }
        });
    }

    public void createOrUpdate(final T entity) {
        EXECUTOR_SERVICE.execute(new Runnable() {
            @Override
            public void run() {
                mRealmDataSource.createOrUpdate(entity);
            }
        });
    }

    public List<T> createOrUpdateBatchSynchronous(final List<T> list, final Class<T> clazz) {

        for (T entity : list) {
            mRealmDataSource.createOrUpdate(entity);
        }

        return mRealmDataSource.getAll(clazz);
    }

    public void delete(final Class<T> clazz, final String id) {
        EXECUTOR_SERVICE.execute(new Runnable() {
            @Override
            public void run() {
                mRealmDataSource.delete(clazz, id);
            }
        });
    }

    public void deleteAll(final Class<T> clazz) {
        EXECUTOR_SERVICE.execute(new Runnable() {
            @Override
            public void run() {
                mRealmDataSource.deleteAll(clazz);
            }
        });
    }

    public Observable<T> getById(final Class<T> clazz, final String id) {
        return Observable.just(mRealmDataSource.getEntityById(clazz, id))
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<T>> getAll(final Class<T> clazz) {

        return Observable.from(mRealmDataSource.getAll(clazz))
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io()).toList();

    }

    public Observable<List<T>> createOrUpdateBatch(final List<T> list, final Class<T> clazz) {

        return Observable.from(createOrUpdateBatchSynchronous(list, clazz))
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io()).toList();
    }
}

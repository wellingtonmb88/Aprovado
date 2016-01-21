package com.wellingtonmb88.aprovado.database.realm;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmManager<T extends RealmObject> {

    private static ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    private Realm mRealm;

    private WeakReference<Context> mContext;

    public RealmManager(Context context) {
        mContext = new WeakReference<>(context);
    }

    public void insert(final T entity) {
        Context context = mContext.get();
        if (context != null) {
            Realm realm = Realm.getInstance(context);

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    bgRealm.copyToRealm(entity);
                }
            }, null);

            realm.close();
        }
    }

    public void update(final T entity) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(entity);
            }
        }, null);
    }

    public void delete(final Class<T> type, final long id) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                T entity = bgRealm.where(type).equalTo("id", id).findFirst();
                entity.removeFromRealm();
            }
        }, null);
    }

    public void deleteAll(final Class<T> type) {

        Context context = mContext.get();
        if (context != null) {
            Realm realm = Realm.getInstance(context);

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    RealmResults<T> results = bgRealm.where(type).findAll();
                    results.clear();
                }
            }, null);

            realm.close();
        }
    }

    public T getEntityAsync(final Class<T> type, final String id) {

        T entity = null;
        Context context = mContext.get();
        if (context != null) {
            Realm realm = Realm.getInstance(context);
            entity = realm.copyFromRealm(realm.where(type).equalTo("id", id).findFirst());
            realm.close();
        }

        return entity;
    }

    public List<T> getAll(final Class<T> type) {
        final List<T> entityList = new ArrayList<>();

        Context context = mContext.get();
        if (context != null) {
            Realm realm = Realm.getInstance(context);

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    RealmResults<T> results = bgRealm.where(type).findAll();
                    List<T> list = bgRealm.copyFromRealm(results);
                    entityList.addAll(list);
                }
            }, null);

            realm.close();
        }

        return entityList;
    }

    public void close() {
        mRealm.close();
    }
}

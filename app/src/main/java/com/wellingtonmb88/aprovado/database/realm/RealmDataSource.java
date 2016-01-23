package com.wellingtonmb88.aprovado.database.realm;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmDataSource<T extends RealmObject> {

    private WeakReference<Context> mContext;

    public RealmDataSource(Context context) {
        mContext = new WeakReference<>(context);
    }

    public void insert(final T entity) {
        Context context = mContext.get();
        if (context != null) {
            Realm realm = Realm.getInstance(context);
            realm.beginTransaction();
            realm.copyToRealm(entity);
            realm.commitTransaction();
            realm.close();
        }
    }

    public void createOrUpdate(final T entity) {
        Context context = mContext.get();
        if (context != null) {
            Realm realm = Realm.getInstance(context);
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(entity);
            realm.commitTransaction();
            realm.close();
        }
    }

    public void delete(final Class<T> type, final String id) {
        Context context = mContext.get();
        if (context != null) {
            Realm realm = Realm.getInstance(context);
            realm.beginTransaction();
            T entity = realm.where(type).equalTo("id", id).findFirst();
            if(entity != null) {
                entity.removeFromRealm();
            }
            realm.commitTransaction();
            realm.close();
        }
    }

    public void deleteAll(final Class<T> type) {
        Context context = mContext.get();
        if (context != null) {
            Realm realm = Realm.getInstance(context);
            realm.beginTransaction();
            RealmResults<T> results = realm.where(type).findAll();
            results.clear();
            realm.commitTransaction();
            realm.close();
        }
    }

    public T getEntityById(final Class<T> type, final String id) {
        T entity = null;
        Context context = mContext.get();
        if (context != null) {
            Realm realm = Realm.getInstance(context);
            realm.beginTransaction();
            entity = realm.copyFromRealm(realm.where(type).equalTo("id", id).findFirst());
            realm.commitTransaction();
            realm.close();
        }

        return entity;
    }

    public List<T> getAll(final Class<T> type) {
        final List<T> entityList = new ArrayList<>();

        Context context = mContext.get();
        if (context != null) {
            Realm realm = Realm.getInstance(context);
            realm.beginTransaction();
            RealmResults<T> results = realm.where(type).findAll();
            List<T> list = realm.copyFromRealm(results);
            entityList.addAll(list);
            realm.commitTransaction();
            realm.close();
        }

        return entityList;
    }
}

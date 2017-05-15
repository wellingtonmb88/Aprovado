package com.wellingtonmb88.aprovado.database.realm;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmDataSource<T extends RealmObject> {

    public void insert(final T entity) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(entity);
        realm.commitTransaction();
        realm.close();
    }

    public void createOrUpdate(final T entity) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(entity);
        realm.commitTransaction();
        realm.close();
    }

    public void delete(final Class<T> type, final String id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        T entity = realm.where(type).equalTo("id", id).findFirst();
        if (entity != null) {
            entity.deleteFromRealm();
        }
        realm.commitTransaction();
        realm.close();
    }

    public void deleteAll(final Class<T> type) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<T> results = realm.where(type).findAll();
        results.deleteAllFromRealm();//clear();
        realm.commitTransaction();
        realm.close();
    }

    public T getEntityById(final Class<T> type, final String id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        T entity = realm.copyFromRealm(realm.where(type).equalTo("id", id).findFirst());
        realm.commitTransaction();
        realm.close();

        return entity;
    }

    public List<T> getAll(final Class<T> type) {
        final List<T> entityList = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<T> results = realm.where(type).findAll();
        List<T> list = realm.copyFromRealm(results);
        entityList.addAll(list);
        realm.close();
        return entityList;
    }
}

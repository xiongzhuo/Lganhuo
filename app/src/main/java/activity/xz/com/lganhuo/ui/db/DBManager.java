package activity.xz.com.lganhuo.ui.db;

import java.util.List;

import activity.xz.com.lganhuo.ui.model.SaveModel;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 作者：Leon
 * 时间：2017/6/22
 * 描述: 数据库工具类
 */
public class DBManager {

    /**
     * 收藏数据，保存到数据库中
     *
     * @param entity
     */
    public static void save(final SaveModel entity) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(entity);
            }
        });
    }

    /**
     * 取消收藏，从数据库中删除
     *
     * @param entity
     */
    public static void cancelSave(final SaveModel entity) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        SaveModel resultsEntity = realm.where(SaveModel.class).equalTo("_id", entity.get_id()).findFirst();
        resultsEntity.deleteFromRealm();
        realm.commitTransaction();
    }

    /**
     * 获取数据中所搜收藏记录
     *
     * @return
     */
    public static List<SaveModel> getAllData() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<SaveModel> results = realm.where(SaveModel.class).findAll();
        realm.commitTransaction();
        return results;
    }

    /**
     * 根据主键查询某条记录
     *
     * @return
     */
    public static SaveModel queryModel(String _id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        SaveModel result = realm.where(SaveModel.class).equalTo("_id", _id).findFirst();
        realm.commitTransaction();
        return result;
    }
}
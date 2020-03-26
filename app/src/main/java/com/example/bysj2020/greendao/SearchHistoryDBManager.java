package com.example.bysj2020.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Author : wxz
 * Time   : 2020/03/25
 * Desc   :
 */
public class SearchHistoryDBManager {

    private final String dbName = "searchHistory";
    private Context context;
    private DaoMaster.DevOpenHelper openHelper;
    private static SearchHistoryDBManager mInstance; //单例

    private SearchHistoryDBManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public static SearchHistoryDBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SearchHistoryDBManager.class) {
                if (mInstance == null) {
                    mInstance = new SearchHistoryDBManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取可读数据库
     *
     * @return
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     *
     * @return
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    public void insert(SearchHistoryBean bean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        SearchHistoryBeanDao dao = daoSession.getSearchHistoryBeanDao();
        dao.insertOrReplace(bean);
    }

    public void update(SearchHistoryBean bean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        SearchHistoryBeanDao dao = daoSession.getSearchHistoryBeanDao();
        dao.update(bean);
    }

    public List<SearchHistoryBean> query() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        SearchHistoryBeanDao dao = daoSession.getSearchHistoryBeanDao();
        List<SearchHistoryBean> list = dao.loadAll();
        return list;
    }

    public void deleteAll() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        daoSession.deleteAll(SearchHistoryBean.class);
    }
}

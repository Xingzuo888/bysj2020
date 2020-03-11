package com.example.bysj2020.utils;

import android.content.Context;

import com.example.bysj2020.bean.AreaCityBean;
import com.example.bysj2020.bean.AreaProvincesBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Author : wxz
 * Time   : 2020/03/09
 * Desc   : 城市选择数据的帮助类
 */
public class AreaUtils {

    private static List<AreaProvincesBean> dataList;

    /**
     * 获取城市选择器的数据
     *
     * @param context
     * @param observer
     */
    public static void getAreaDataList(Context context, Observer<List<AreaProvincesBean>> observer) {
        Observable.create((ObservableOnSubscribe<List<AreaProvincesBean>>) emitter -> {
            //读取城市的json文件
            StringBuffer json = new StringBuffer();
            try {
                InputStream inputStream = context.getResources().getAssets().open("Area.json");
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String jsonLine;
                while ((jsonLine = reader.readLine()) != null) {
                    json.append(jsonLine);
                }
                reader.close();
                inputStreamReader.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                emitter.onError(e);
            }
            dataList = new Gson().fromJson(json.toString(), new TypeToken<List<AreaProvincesBean>>() {
            }.getType());
            emitter.onNext(dataList);
            emitter.onComplete();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 获取包含不限的城市数据
     *
     * @param cityBeans
     * @return
     */
    public static List<AreaCityBean> getCityOfAll(List<AreaCityBean> cityBeans) {
        if (null != cityBeans) {
            if (cityBeans.size() > 0 && (cityBeans.get(0).getCode().equals("0") || cityBeans.get(0).getName().equals("不限"))) {
                return cityBeans;
            }
            cityBeans.add(0, new AreaCityBean("0", "不限", "unlimited", false));
        }
        return cityBeans;
    }

    /**
     * 获取城市选择器的数据
     *
     * @param context
     * @param observer
     */
    public static void getAreaData(Context context, Observer<List<AreaProvincesBean>> observer) {
        Observable.create((ObservableOnSubscribe<List<AreaProvincesBean>>) emitter -> {
            //读取城市的json文件
            StringBuffer json = new StringBuffer();
            try {
                InputStream inputStream = context.getResources().getAssets().open("Area.json");
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String jsonLine;
                while ((jsonLine = reader.readLine()) != null) {
                    json.append(jsonLine);
                }
                reader.close();
                inputStreamReader.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                emitter.onError(e);
            }
            dataList = new Gson().fromJson(json.toString(), new TypeToken<List<AreaProvincesBean>>() {
            }.getType());
            if (dataList != null) {
                //处理城市，添加一个不限
                for (AreaProvincesBean areaProvincesBean : dataList) {
                    dealCityList(areaProvincesBean);
                }
                //在头部添加一个全国
                List<AreaCityBean> cityBeanList = new ArrayList<>();
                cityBeanList.add(new AreaCityBean("0", "不限", "quanguo", "全国", false));
                dataList.add(0, new AreaProvincesBean("0", "全国", "quanguo", cityBeanList));
            }
            emitter.onNext(dataList);
            emitter.onComplete();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 获取包含不限的城市数据
     *
     * @param provincesBean
     * @return
     */
    public static List<AreaCityBean> dealCityList(AreaProvincesBean provincesBean) {
        if (provincesBean == null) {
            return null;
        }
        List<AreaCityBean> cityData = provincesBean.getCitys();
        if (null != cityData) {
            if (cityData.size() > 0 && cityData.get(0).getCode().equals("0")) {
                return cityData;
            }
            for (AreaCityBean areaCityBean : cityData) {
                areaCityBean.setShowName(areaCityBean.getName());
            }
            cityData.add(0, new AreaCityBean(provincesBean.getCode(), "不限", "unlimited", provincesBean.getName(), false));
        }
        return cityData;
    }
}

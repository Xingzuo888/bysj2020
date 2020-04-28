package com.example.bysj2020.amaplocation;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.example.bysj2020.event.LocationEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Author : wxz
 * Time   : 2020/04/27
 * Desc   : 获取定位结果
 */
public class AMapLocationClientListener implements AMapLocationListener {
    private AMapLocationClient mapLocationClient;
    private Context context;

    public AMapLocationClientListener(Context context) {
        this.context = context;
    }

    public AMapLocationClientListener(Context context, AMapLocationClient mapLocationClient) {
        this.mapLocationClient = mapLocationClient;
        this.context = context;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                String province = aMapLocation.getProvince(); //省份信息
                String city = aMapLocation.getCity(); //城市信息
                String district = aMapLocation.getDistrict();//城区信息
                if (city != null && !city.equals("")) {
                    //停止定位后，本地定位服务并不会被销毁
                    mapLocationClient.stopLocation();
                    if (city.equals("北京市") || city.equals("天津市") || city.equals("重庆市") || city.equals("上海市")) {
                        EventBus.getDefault().post(new LocationEvent(3, district));
                        return;
                    } else {
                        EventBus.getDefault().post(new LocationEvent(3, city));
                    }
                }
            } else {
//                ToastUtil.setToast(context, "定位失败");
            }
        }
    }
}

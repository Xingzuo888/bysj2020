package com.example.bysj2020.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bysj2020.R;
import com.example.bysj2020.adapter.PopupAreaSelectorLeftAdapter;
import com.example.bysj2020.adapter.PopupAreaSelectorRightAdapter;
import com.example.bysj2020.bean.AreaCityBean;
import com.example.bysj2020.bean.AreaProvincesBean;
import com.example.bysj2020.utils.AreaUtils;
import com.example.bysj2020.utils.ToastUtil;
import com.example.bysj2020.view.LinearItemDecoration;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Author : wxz
 * Time   : 2020/03/30
 * Desc   : 城市选择器弹框
 */
public class PopupAreaSelector {

    private Context context;
    private View parent;
    private PopupWindow popupWindow;
    private RecyclerView leftRecycler;
    private RecyclerView rightRecycler;
    private PopupAreaSelectorLeftAdapter leftAdapter;
    private PopupAreaSelectorRightAdapter rightAdapter;
    private List<AreaProvincesBean> provincesBeanList;
    private PopupAreaSelectorClick popupAreaSelectorClick;

    private String provinceId;
    private String provinceName;
    private String cityId;
    private String cityName;

    public PopupAreaSelector(Context context, View parent) {
        this.context = context;
        this.parent = parent;
        init();
    }

    public static PopupAreaSelector Builder(Context context, View parent) {
        return new PopupAreaSelector(context, parent);
    }

    private void init() {
        View childView = LayoutInflater.from(context).inflate(R.layout.dialog_area_selector, null);
        leftRecycler = childView.findViewById(R.id.dialog_area_selector_left);
        rightRecycler = childView.findViewById(R.id.dialog_area_selector_right);
        View line = childView.findViewById(R.id.dialog_area_selector_line);
        line.setOnClickListener(v -> dismiss());

        //设置分割线
        LinearItemDecoration rightLinearItemDecoration = new LinearItemDecoration.Builder(context)
                .setSpan(1f)
                .setColorResource(R.color.gray_8)
                .setmShowLastLine(true)
                .build();
        LinearItemDecoration leftLinearItemDecoration = new LinearItemDecoration.Builder(context)
                .setSpan(1f)
                .setColorResource(R.color.gray_f4)
                .setmShowLastLine(true)
                .build();
        rightRecycler.addItemDecoration(rightLinearItemDecoration);
        leftRecycler.addItemDecoration(leftLinearItemDecoration);

        popupWindow = new PopupWindow(childView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        getDataList();
    }

    public void show() {
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        if (Build.VERSION.SDK_INT != 24 && Build.VERSION.SDK_INT != 25) {
            popupWindow.showAsDropDown(parent, 0, 0);
        } else {
            if (Build.DISPLAY.contains("Flyme")) {
                popupWindow.showAsDropDown(parent, 0, 0);
            } else {
                popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, 0, parent.getHeight() + location[1]);
            }
        }
    }

    public boolean isShow() {
        return popupWindow.isShowing();
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    private void initLeftAdapter() {
        leftAdapter = new PopupAreaSelectorLeftAdapter(provincesBeanList, context);
        leftRecycler.setLayoutManager(new LinearLayoutManager(context));
        leftRecycler.setAdapter(leftAdapter);
        leftAdapter.addItemClickListener((view, areaProvincesBean, position) -> {
            provinceId = areaProvincesBean.getCode();
            provinceName = areaProvincesBean.getName();
            leftAdapter.setProvincesId(areaProvincesBean.getCode());
            leftAdapter.notifyDataSetChanged();
            initRightAdapter(areaProvincesBean.getCitys());
        });
    }

    private void initRightAdapter(List<AreaCityBean> citys) {
        rightAdapter = new PopupAreaSelectorRightAdapter(citys, context);
        rightRecycler.setLayoutManager(new LinearLayoutManager(context));
        rightRecycler.setAdapter(rightAdapter);
        rightAdapter.addItemClickListener((view, areaCityBean, position) -> {
            cityId = areaCityBean.getCode();
            cityName = areaCityBean.getName();
            rightAdapter.setCityId(areaCityBean.getCode());
            rightAdapter.notifyDataSetChanged();
            if (popupAreaSelectorClick != null) {
                popupAreaSelectorClick.clickBack(provinceId, provinceName, cityId, cityName);
            }
        });
    }

    private void getDataList() {
        AreaUtils.getAreaData(context, new Observer<List<AreaProvincesBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<AreaProvincesBean> areaProvincesBeans) {
                provincesBeanList = areaProvincesBeans;
                if (provincesBeanList != null && provincesBeanList.size() > 0) {
                    initLeftAdapter();
                } else {
                    onError(new Exception("暂无数据"));
                }
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.setToast(context, e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void setPopupAreaSelectorClick(PopupAreaSelectorClick popupAreaSelectorClick) {
        this.popupAreaSelectorClick = popupAreaSelectorClick;
    }

    public interface PopupAreaSelectorClick {
        public void clickBack(String provinceId, String provinceName, String cityId, String cityName);
    }
}

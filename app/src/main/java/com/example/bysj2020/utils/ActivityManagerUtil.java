package com.example.bysj2020.utils;

import android.app.Activity;

import com.example.bysj2020.bean.ActivityBean;

import java.util.ArrayList;
import java.util.List;

public class ActivityManagerUtil {
    private static List<ActivityBean> activityStack = new ArrayList<>();

    public ActivityManagerUtil() {
    }

    /**
     * 添加到销毁队列
     *
     * @param activity
     * @param activityName
     */
    public static void addDestroyActivity(Activity activity, String activityName) {
        activityStack.add(new ActivityBean(activityName, activity));
    }

    /**
     * 销毁指定activity
     *
     * @param activityName
     */
    public static void destroyActivity(String activityName) {
        int size = activityStack.size();
        for (int i = 0; i < size; i++) {
            if (activityName.equals(activityStack.get(i).getName())) {
                activityStack.get(i).getActivity().finish();
                activityStack.remove(i);
                return;
            }
        }
    }

    /**
     * 是否包含指定activity
     *
     * @param activityName
     * @return
     */
    public static boolean containsActivity(String activityName) {
        int size = activityStack.size();
        for (int i = 0; i < size; i++) {
            if (activityStack.get(i).getName().endsWith(activityName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 销毁除 activityName 以外,所有activity
     *
     * @param activityName
     */
    public static void destroyOtherActivity(String activityName) {
        int size = activityStack.size();
        for (int i = size - 1; i >= 0; i--) {
            if (!activityStack.get(i).getName().endsWith(activityName)) {
                activityStack.get(i).getActivity().finish();
                activityStack.remove(i);
            }
        }
    }

    /**
     * 指定数目回退activity
     *
     * @param count
     */
    public static void destroyActivityIndex(int count) {
        int size = activityStack.size();
        if (size >= count) {
            for (int i = size - 1; i > size - 1 - count; i--) {
                activityStack.get(i).getActivity().finish();
                activityStack.remove(i);
            }
        }
    }

    /**
     * 销毁所有activity
     */
    public static void destroyActivityAll() {
        int size = activityStack.size();
        if (size > 0) {
            for (int i = size - 1; i >= 0; i--) {
                activityStack.get(i).getActivity().finish();
                activityStack.remove(i);
            }
        }
    }

    /**
     * 获取栈顶activity的信息
     *
     * @return
     */
    public static ActivityBean getCurrRunActivity() {
        return activityStack.get(activityStack.size() - 1);
    }
}

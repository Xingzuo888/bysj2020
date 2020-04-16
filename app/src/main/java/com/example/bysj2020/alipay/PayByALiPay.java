package com.example.bysj2020.alipay;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.example.bysj2020.activity.PaySuccess;
import com.example.bysj2020.utils.ToastUtil;

/**
 * Author : wxz
 * Time   : 2020/04/12
 * Desc   :
 */
public class PayByALiPay {
    private Activity activity;
    private String orderId;

    public PayByALiPay(Activity activity) {
        this.activity = activity;
    }

    public PayByALiPay(Activity activity, String orderId) {
        this.activity = activity;
        this.orderId = orderId;
    }

    private Handler payHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            PayResult result = new PayResult(msg.obj.toString());
            String status = result.getResultStatus();
            //支付成功
            if (TextUtils.equals(status, "9000")) {
                ToastUtil.setToast(activity, "支付成功");
                activity.startActivity(new Intent(activity, PaySuccess.class).putExtra("name", activity.getLocalClassName().split("\\.")[1]).putExtra("orderId",orderId));
            } else if (TextUtils.equals(status, "8000")) {
                ToastUtil.setToast(activity, "支付结果确认中");
            } else if (TextUtils.equals(status, "6001")) {
                ToastUtil.setToast(activity, "订单已创建，预订信息查看");
                activity.finish();
            } else {
                ToastUtil.setToast(activity, "支付失败");
            }
        }
    };


    public void pay(String info) {
        Runnable runnable = () -> {
            //开启沙箱环境
            EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
            // 构造PayTask 对象
            PayTask payTask = new PayTask(activity);
            String result = payTask.pay(info, true);
            Message msg = new Message();
            msg.obj = result;
            payHandler.sendMessage(msg);
        };
        // 必须异步调用
        Thread payThread = new Thread(runnable);
        payThread.start();
    }
}

package com.example.bysj2020.https;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RxHttpObserver<T> implements Observer<HttpBean<T>> {

    private String method;

    private HttpResult<T> result;

    private HttpPageResult<T> pageResult;

    public RxHttpObserver(String method, HttpResult<T> result) {
        this.method = method;
        this.result = result;
    }

    public RxHttpObserver(String method, HttpPageResult<T> pageResult) {
        this.method = method;
        this.pageResult = pageResult;
    }

    @Override
    public void onSubscribe(Disposable d) {
        RxApiManager.get().add(method, d);
    }

    @Override
    public void onNext(HttpBean<T> httpBean) {
        if (httpBean.getState() == 0) {
            if (result == null) {
                pageResult.OnSuccess(httpBean.getData(), httpBean.getLastPage(), httpBean.getMsg());
            } else {
                result.OnSuccess(null == httpBean.getData() ? null : httpBean.getData(), httpBean.getMsg());
            }
        } else {
            if (result == null) {
                pageResult.OnFail(httpBean.getState(), httpBean.getMsg());
            } else {
                result.OnFail(httpBean.getState(), httpBean.getMsg());
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (result == null) {
            pageResult.OnFail(-1, "网络君不给了，请稍后尝试...");
        } else {
            result.OnFail(-1, "网络君不给了，请稍后尝试...");
        }
    }

    @Override
    public void onComplete() {

    }
}

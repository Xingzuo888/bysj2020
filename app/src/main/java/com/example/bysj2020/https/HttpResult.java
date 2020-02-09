package com.example.bysj2020.https;

public interface HttpResult<T> {

    /**
     * 成功回调
     *
     * @param t
     * @param msg
     */
    void OnSuccess(T t, String msg);

    /**
     * 失败回调
     *
     * @param code
     * @param msg
     */
    void OnFail(int code, String msg);
}

package com.example.bysj2020.https;

public interface HttpPageResult<T> {

    /**
     * 成功回调
     *
     * @param t
     * @param lastPage
     * @param msg
     */
    void OnSuccess(T t, int lastPage, String msg);

    /**
     * 失败回调
     *
     * @param code
     * @param msg
     */
    void OnFail(int code, String msg);
}

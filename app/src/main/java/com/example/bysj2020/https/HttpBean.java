package com.example.bysj2020.https;

public class HttpBean<T> {
    /**
     * 状态码
     */
    private int state;
    /**
     * 信息
     */
    private String msg;
    /**
     * 数据
     */
    private T data;
    /**
     * 0 不是最后一页 ，1是最后一页
     */
    private int lastPage;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }
}

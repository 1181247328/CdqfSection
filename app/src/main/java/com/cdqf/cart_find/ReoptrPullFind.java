package com.cdqf.cart_find;

public class ReoptrPullFind {
    //关闭刷新 flase = 关闭刷新
    public boolean isRefreshing = false;

    //是不是禁用刷新 true = 禁用
    public boolean isEnabled = false;

    public ReoptrPullFind(boolean isRefreshing, boolean isEnabled) {
        this.isRefreshing = isRefreshing;
        this.isEnabled = isEnabled;
    }
}

package com.cdqf.cart_find;

public class EmployeesPullFind {
    //关闭刷新 flase = 关闭刷新
    public boolean isRefreshing = false;

    //是不是禁用刷新 true = 禁用
    public boolean isEnabled = false;

    public EmployeesPullFind(boolean isRefreshing, boolean isEnabled) {
        this.isRefreshing = isRefreshing;
        this.isEnabled = isEnabled;
    }
}

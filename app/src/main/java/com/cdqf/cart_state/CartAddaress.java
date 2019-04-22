package com.cdqf.cart_state;

public class CartAddaress {

    //线下
    public static final String ADDRESS = "http://192.168.31.131";

    //登录(店长和店员)
    public static String LOGIN = ADDRESS + "/?s=Staff.login&account=ACCOUNT&password=PASSWORD";

    //店总
    public static String SHOP = ADDRESS + "/?s=order.shopowenr&shopid=SHOPID";

    //店长完成订单
    public static String SHOP_YES = ADDRESS + "?s=order.getordertype&id=ID&number=NUMBER";

    //店长完成订单
    public static String SHOP_SERVICE = ADDRESS + "?s=Service.setservice&ordernum=ORDERNUMB&staffid=STAFFID";

    //店长通知
    public static String SHOP_NOTICE = ADDRESS + "?s=Notice.admingetnotice&staffid=STAFFID&shopid=SHOPID";

    //店长添加通知
    public static String SHOP_NOTICE_ADD = ADDRESS + "?s=Notice.stenotice&content=CONTENT&staffid=STAFFID&type=TYPE";

    public static String LOSS = "";

    /******员工******/
    //服务
    public static String STAFF_SHOP = ADDRESS + "/?s=order.staff&shopid=SHOPID";

    //通知
    public static String STAFF_NOTICE = ADDRESS + "/?s=Notice.getnotice&staffid=STAFFID&shopid=SHOPID";
}

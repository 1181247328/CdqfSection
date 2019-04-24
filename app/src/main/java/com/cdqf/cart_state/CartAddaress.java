package com.cdqf.cart_state;

public class CartAddaress {

    //线下
    //public static final String ADDRESS = "http://192.168.31.131";

    //线上
    public static final String ADDRESS = "http://testapi.baodi520.com";

    //店总
    public static String SHOP = ADDRESS + "/?s=order.shopowenr&shopid=SHOPID";

    //店长完成订单
    public static String SHOP_YES = ADDRESS + "/?s=order.getordertype&id=ID&number=NUMBER";

    //店长服务订单
    public static String SHOP_SERVICE = ADDRESS + "/?s=Service.setservice&ordernum=ORDERNUMB&staffid=STAFFID";

    //店长通知
    public static String SHOP_NOTICE = ADDRESS + "/?s=Notice.admingetnotice&staffid=STAFFID&shopid=SHOPID";

    //店长添加通知
    public static String SHOP_NOTICE_ADD = ADDRESS + "/?s=Notice.stenotice&content=CONTENT&staffid=STAFFID&type=TYPE";

    //店长查看通知
    public static String SHOP_DATILE = ADDRESS + "/?s=Order.getorderinfo&ordernum=ORDERNUM";

    //店长领取
    public static String SHOP_RECEIVE = ADDRESS + "/?s=TotalGoods.receive";

    //店长耗材
    public static String SHOP_TOTAL = ADDRESS + "/?s=TotalGoods.lists";

    //店长审核列表
    public static String SHOP_AUDIT = ADDRESS + "/?s=TotalGoods.shop_approval_list";

    //店长通过
    public static String SHOP_THROUGH = ADDRESS + "/?s=TotalGoods.shop_approval";

    //店长不通过
    public static String SHOP_THROUGH_NO = ADDRESS + "/?s=TotalGoods.shop_exit";

    //店长审核记录
    public static String SHOP_RECORD = ADDRESS + "/?s=TotalGoods.goodsext_staff";

    //店长追加
    public static String SHOP_GOODS = ADDRESS + "/?s=goods.goodsall";

    public static String LOSS = ADDRESS + "";

    /******员工******/

    //员工服务
    public static String STAFF_SERVICE = ADDRESS + "/?s=Service.setservice&ordernum=ORDERNUMB&staffid=STAFFID";

    //员工耗材
    public static String STAFF_TOTAL = ADDRESS + "/?TotalGoods.staff_shop_list";

    //员工领取
    public static String STAFF_RECEIVE = ADDRESS + "/?TotalGoods.staff_receiving";

    //员工服务
    public static String STAFF_SHOP = ADDRESS + "/?s=order.staff&shopid=SHOPID";

    //员工通知
    public static String STAFF_NOTICE = ADDRESS + "/?s=Notice.getnotice&staffid=STAFFID&shopid=SHOPID";


    /******通用******/

    public static String USER_INFORMATION = ADDRESS + "/?s=staff.getstaff&staffid=STAFFID&shopid=SHOPID";

    //登录(店长和店员)
    public static String LOGIN = ADDRESS + "/?s=Staff.login&account=ACCOUNT&password=PASSWORD";
}

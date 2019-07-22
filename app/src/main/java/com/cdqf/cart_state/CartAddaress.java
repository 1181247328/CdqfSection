package com.cdqf.cart_state;

public class CartAddaress {

    //线下
    public static final String ADDRESS = "http://192.168.31.213:8001";

    //线上
    public static final String ADDRESS_THE = "https://tgapi.baodi520.com";

    //主页(店员)
    public static final String HOME = ADDRESS + "/staff/shops";

    //服务(待服务、已完成、待付款)
    public static final String service = ADDRESS + "/staff/service";

    //颜色
    public static final String COLOR = ADDRESS + "/staff/color";

    //服务项目
    public static final String SERVICE_ITEM = ADDRESS + "/staff/items";

    //添加订单
    public static final String ADD_ORDER = ADDRESS + "/staff/create";

    //订单详情
    public static final String ORDER_DATILS = ADDRESS + "/staff/order/{id}";

    //添加订单备注信息
    public static final String ORDER_NOET = ADDRESS + "/staff/orderRemarks";

    //添加会员备注信息
    public static final String USER_NOET = ADDRESS + "/staff/userRemarks";

    //员工点击服务
    public static final String USER_SERVICE = ADDRESS + "/staff/takeService";

    public static final String FILL_TYPE = ADDRESS + "";

    //提交报销
    public static final String FILL = ADDRESS + "/staff/createExamine";

    //报销记录
    public static final String RECORD = ADDRESS + "/staff/examineList";

    //审核
    public static final String EXAMiNELIST = ADDRESS + "/staff/adminExamineList";

    public static final String CART_SHOP_ID = ADDRESS + "/staff/shops/";

    //同意审核
    public static final String AGRED_AUDITS = ADDRESS + "/staff/examine";

    //会员管理
    public static final String MEMBERS = ADDRESS + "/staff/member";

    //会员下单列表
    public static final String MEMBERS_LIST = ADDRESS + "/staff/shopMenber/";

    //通过电话号码搜索直接跳进详情页
    public static final String MEMBERS_PHONE = ADDRESS + "";

    //报表
    public static final String REPORT = ADDRESS + "/staff/getReport";

    //登录
    public static final String LOGIN_NEW = ADDRESS + "/admin/login";

    //报表详情
    public static final String REPORT_DATILS = ADDRESS + "/staff/getReportEcharts/";

    //报表详情
    public static final String REPORT_DATILS_LIST = ADDRESS + "/staff/getReport/";

    //打卡
    public static final String CLOCK = ADDRESS + "/staff/attedanceShop";

    //开始打卡
    public static final String CLOCK_IN = ADDRESS + "/staff/attendance";

    //打卡记录
    public static final String CLOCK_RECORD = ADDRESS + "/staff/";

    //会员详情
    public static final String MEMBERS_DATILS = ADDRESS + "/staff/menberinfo";

    //损耗品
    public static final String LOSS_NEW = ADDRESS + "/staff/consumablesShop/";

    //出入库
    public static final String CONSUMABLES = ADDRESS + "/staff/consumables";

    //店员管理
    public static final String EMPLOYEES = ADDRESS + "/staff/shopStaff";

    //职位
    public static final String ROLL = ADDRESS + "/staff/roll";

    //添加员工
    public static final String ADD_ROLL = ADDRESS + "/staff/createStaff";

    /****************************************************/

    //店总
    public static String SHOP = ADDRESS + "/?s=order.shopowenr&shopid=SHOPID";

    //店长完成订单
    public static String SHOP_YES = ADDRESS + "/?s=order.getordertype&id=ID&number=NUMBER";

    //店长服务订单
    public static String SHOP_SERVICE = ADDRESS + "/?s=Service.setservice&ordernum=ORDERNUMB&staffid=STAFFID";

    //店长通知
    public static String SHOP_NOTICE = ADDRESS_THE + "/?s=Notice.admingetnotice&staffid=STAFFID&shopid=SHOPID";

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
    public static String SHOP_GOODS = ADDRESS_THE + "/?s=goods.goodsall";

    //店长添加员工
    public static String SHOP_POSITION = ADDRESS + "/?s=Shopowner.add_position_list";

    //上传身份证
    public static String SHOP_IDIMAGE = ADDRESS + "/?s=Upload.addImg";

    //给予优惠
    public static String SHOP_PREFERENT = ADDRESS_THE + "/?s=order.discount_list";

    //员工管理列表
    public static String SHOP_EMPLOYEES = ADDRESS + "/?s=Shopowner.shop_user_list";

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

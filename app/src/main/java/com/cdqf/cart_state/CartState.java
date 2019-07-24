package com.cdqf.cart_state;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.apkfuns.xprogressdialog.XProgressDialog;
import com.cdqf.cart.R;
import com.cdqf.cart_ble.Ble;
import com.cdqf.cart_class.AllEmployees;
import com.cdqf.cart_class.Audit;
import com.cdqf.cart_class.Audits;
import com.cdqf.cart_class.AuditsJudge;
import com.cdqf.cart_class.Clock;
import com.cdqf.cart_class.Clockin;
import com.cdqf.cart_class.Complete;
import com.cdqf.cart_class.Daily;
import com.cdqf.cart_class.Datils;
import com.cdqf.cart_class.Employees;
import com.cdqf.cart_class.Entry;
import com.cdqf.cart_class.Home;
import com.cdqf.cart_class.LossMan;
import com.cdqf.cart_class.LossNews;
import com.cdqf.cart_class.LossStaff;
import com.cdqf.cart_class.MembersDatils;
import com.cdqf.cart_class.Memebersship;
import com.cdqf.cart_class.Moth;
import com.cdqf.cart_class.MyUser;
import com.cdqf.cart_class.Notice;
import com.cdqf.cart_class.Number;
import com.cdqf.cart_class.Position;
import com.cdqf.cart_class.PositionEmployees;
import com.cdqf.cart_class.Record;
import com.cdqf.cart_class.Service;
import com.cdqf.cart_class.ServiceOrder;
import com.cdqf.cart_class.Shop;
import com.cdqf.cart_class.Through;
import com.cdqf.cart_class.ThroughsJudge;
import com.cdqf.cart_class.User;
import com.cdqf.cart_class.UserGoods;
import com.cdqf.cart_class.Vaction;
import com.cdqf.cart_class.Week;
import com.cdqf.cart_class.Withdraw;
import com.cdqf.cart_class.Work;
import com.cdqf.cart_service.Province;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 状态层
 * Created by liu on 2017/7/14.
 */

public class CartState {

    private String TAG = CartState.class.getSimpleName();

    //中间层
    private static CartState cartState = new CartState();

    public static CartState getCartState() {
        return cartState;
    }

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private XProgressDialog xProgressDialog = null;

    //头像
    private Bitmap headBitmap = null;

    private boolean isPrssor = true;

    private BluetoothAdapter mBluetoothAdapter = null;

    private Map<Integer, Ble> bleMapList = new HashMap<>();

    private Map<Integer, Boolean> titleList = new HashMap<>();

    public List<String> bleList = new CopyOnWriteArrayList<>();

    //省
    private List<String> options1Items = new ArrayList<>();

    //市
    private List<List<String>> options2Items = new ArrayList<>();

    //区
    private List<List<List<String>>> options3Items = new ArrayList<>();

    //省市区
    private List<Province> provinceList = new CopyOnWriteArrayList<Province>();

    //判断登录
    private boolean isLogin = false;

    //用户
    private User user = new User();

    //店总服务数量
    private List<Shop> shopList = new CopyOnWriteArrayList<>();

    //店长通知
    private List<Notice> noticeList = new CopyOnWriteArrayList<>();

    private MyUser myUser = new MyUser();

    //耗材(店长)
    private List<LossMan> lossManList = new CopyOnWriteArrayList<>();

    //耗材(员工)
    private List<LossStaff> lossStaffList = new CopyOnWriteArrayList<>();

    //审核列表
    private List<Audit> auditList = new CopyOnWriteArrayList<>();

    //审核记录
    private List<Record> recordList = new CopyOnWriteArrayList<>();

    //追加商品
    private List<UserGoods> userGoodsList = new CopyOnWriteArrayList<>();

    //新损耗品
    private List<LossNews> lossNewsList = new CopyOnWriteArrayList<>();

    //员工职位
    private List<Position> positionList = new CopyOnWriteArrayList<>();

    //订单详情
    private Datils datils = new Datils();

    //员工列表
    private List<Employees> employeesList = new CopyOnWriteArrayList<>();

    //图片选择
    private List<String> imagePathsList = new CopyOnWriteArrayList<>();

    private Clock clock = new Clock();


    /*************************************/
    //店名
    private List<Home> homeList = new CopyOnWriteArrayList<>();

    //待服务
    private List<Service> serviceLis = new CopyOnWriteArrayList<>();

    //已完成
    private List<Complete> completeList = new CopyOnWriteArrayList<>();

    //待付款
    private List<Entry> entryList = new CopyOnWriteArrayList<>();

    //车辆颜色
    private List<com.cdqf.cart_class.Color> colorList = new CopyOnWriteArrayList<>();

    //服务选项
    private List<ServiceOrder> serviceOrderList = new CopyOnWriteArrayList<>();

    //小轿车1,SUV = 2;
    private int model = 0;

    //待审批
    private List<Audits> auditsList = new CopyOnWriteArrayList<>();

    //已同意
    private List<Through> throughList = new CopyOnWriteArrayList<>();

    //已撤回
    private List<Withdraw> withdrawList = new CopyOnWriteArrayList<>();

    //待审批(审核)
    private List<AuditsJudge> auditsJudgeList = new CopyOnWriteArrayList<>();

    //已通过(审核)
    private List<ThroughsJudge> throughsJudgeList = new CopyOnWriteArrayList<>();

    //会员下单总数
    private List<Memebersship> memebersshipList = new CopyOnWriteArrayList<>();

    //日报
    private List<Daily> dailyList = new CopyOnWriteArrayList<>();

    //周报
    private List<Week> weekList = new CopyOnWriteArrayList<>();

    //月报
    private List<Moth> mothList = new CopyOnWriteArrayList<>();

    //车牌
    private List<Number> numberList = new CopyOnWriteArrayList<>();

    //打卡记录
    private List<Clockin> clockinList = new CopyOnWriteArrayList<>();

    //会员详情
    private MembersDatils membersDatils = new MembersDatils();

    //全部
    private List<AllEmployees> allEmployeesList = new CopyOnWriteArrayList<>();

    //上班中
    private List<Work> workList = new CopyOnWriteArrayList<>();

    //休假
    private List<Vaction> vactionList = new CopyOnWriteArrayList<>();

    //职位
    private List<PositionEmployees> positionEmployeesList = new CopyOnWriteArrayList<>();

    //报销详情图片
    private List<String> imageList = new CopyOnWriteArrayList<>();


    /**
     * 提示信息
     *
     * @param context
     * @param toast
     */
    public void initToast(Context context, String toast, boolean isShort, int type) {
        Toast initToast = null;
        if (isShort) {
            initToast = Toast.makeText(context, toast, Toast.LENGTH_SHORT);
        } else {
            initToast = Toast.makeText(context, toast, Toast.LENGTH_LONG);
        }

        switch (type) {
            case 0:
                break;
            //显示中间
            case 1:
                initToast.setGravity(Gravity.CENTER, 0, 0);
                initToast.show();
                break;
            //顶部显示
            case 2:
                initToast.setGravity(Gravity.TOP, 0, 0);
                break;
        }
        initToast.show();
    }

    public void initToast(Context context, String toast, boolean isShort, int type, int timer) {
        Toast initToast = null;
        if (isShort) {
            initToast = Toast.makeText(context, toast, Toast.LENGTH_SHORT);
        } else {
            initToast = Toast.makeText(context, toast, timer);
        }

        switch (type) {
            case 0:
                break;
            //显示中间
            case 1:
                initToast.setGravity(Gravity.CENTER, 0, 0);
                initToast.show();
                break;
            //顶部显示
            case 2:
                initToast.setGravity(Gravity.TOP, 0, 0);
                break;
        }
        initToast.show();
    }

    public String getPlantString(Context context, int resId) {
        return context.getResources().getString(resId);
    }


    /**
     * @param loading 加载图片时的图片
     * @param empty   没图片资源时的默认图片
     * @param fail    加载失败时的图片
     * @return
     */
    public DisplayImageOptions getImageLoaderOptions(int loading, int empty, int fail) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(loading)
                .showImageForEmptyUri(empty)
                .showImageOnFail(fail)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(5))
                .build();
        return options;
    }

    /**
     * 为头像而准备
     *
     * @param loading
     * @param empty
     * @param fail
     * @return
     */
    public DisplayImageOptions getHeadImageLoaderOptions(int loading, int empty, int fail) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(loading)
                .showImageForEmptyUri(empty)
                .showImageOnFail(fail)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        return options;
    }

    /**
     * 保存图片的配置
     *
     * @param context
     * @param cache   "imageLoaderworld/Cache"
     */
    public ImageLoaderConfiguration getImageLoaderConfing(Context context, String cache) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, cache);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(10)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100)
                .discCache(new UnlimitedDiskCache(cacheDir))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
                .writeDebugLogs()
                .build();
        return config;
    }

    /**
     * 初始化imageLoad
     *
     * @param context
     * @return
     */
    public ImageLoaderConfiguration getConfiguration(Context context) {
        ImageLoaderConfiguration configuration = getImageLoaderConfing(context, "imageLoaderword/Chace");
        return configuration;
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "版本有错";
        }
    }

    /**
     * 权限
     */
    public void permission(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || activity.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED
                    || activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || activity.checkSelfPermission(Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED
                    || activity.checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.CAMERA,},
                        0
                );
            }
        }
    }

    /**
     * webView处理
     *
     * @param wvDrunkCarrier
     */
    public WebSettings webSettings(WebView wvDrunkCarrier) {
        WebSettings wsDrunkCarrier = wvDrunkCarrier.getSettings();
        //自适应屏幕
        wsDrunkCarrier.setUseWideViewPort(true);
        wsDrunkCarrier.setLoadWithOverviewMode(true);
        //支持网页放大缩小
        wsDrunkCarrier.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        wsDrunkCarrier.setUseWideViewPort(true);
        wsDrunkCarrier.setLoadWithOverviewMode(true);
        wsDrunkCarrier.setSavePassword(true);
        wsDrunkCarrier.setSaveFormData(true);
        wsDrunkCarrier.setJavaScriptEnabled(true);
        wsDrunkCarrier.setGeolocationEnabled(true);
        wsDrunkCarrier.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");
        wsDrunkCarrier.setDomStorageEnabled(true);
//        wsDrunkCarrier.setBuiltInZoomControls(true);
//        wsDrunkCarrier.setSupportZoom(true);
//        wsDrunkCarrier.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //把图片加载放在最后来加载
        wsDrunkCarrier.setBlockNetworkImage(false);
        //可以加载javascript
        wsDrunkCarrier.setJavaScriptEnabled(true);
        //设置缓存模式
        wsDrunkCarrier.setAppCacheEnabled(true);
        wsDrunkCarrier.setAllowFileAccess(true);
        wsDrunkCarrier.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //开启 DOM storage API 功能
        wsDrunkCarrier.setDomStorageEnabled(true);
        //开启 database storage API 功能
        wsDrunkCarrier.setDatabaseEnabled(true);
        //开启 Application Caches 功能
        wsDrunkCarrier.setAppCacheEnabled(false);
        //是否调用jS中的代码
        wsDrunkCarrier.setJavaScriptEnabled(true);
        wsDrunkCarrier.setJavaScriptCanOpenWindowsAutomatically(true);
        wsDrunkCarrier.setAllowFileAccess(true);
        //支持多点触摸
        wsDrunkCarrier.setBuiltInZoomControls(false);
        wsDrunkCarrier.setDefaultTextEncodingName("UTF-8");
        //自动加载图片
        wsDrunkCarrier.setLoadsImagesAutomatically(true);
        wsDrunkCarrier.setLoadWithOverviewMode(true);
        wsDrunkCarrier.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wsDrunkCarrier.setUseWideViewPort(true);
        wsDrunkCarrier.setSaveFormData(true);
        wsDrunkCarrier.setSavePassword(true);
        return wsDrunkCarrier;
    }

    public ImageLoader getImageLoader(Context context) {
        imageLoader.init(getConfiguration(context));
        return imageLoader;
    }

    /**
     * 判断是不是手机号码
     *
     * @param str
     * @return
     */
    public boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("[1][3456789]\\d{9}"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    public void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;

    }

    public boolean passwordJudge(Context context, String password, String passwordTwo) {
        if (password.length() <= 0) {
            initToast(context, "请输入新密码", true, 0);
            return false;
        }
        if (password.length() >= 17) {
            initToast(context, "密码长度不大于16", true, 0);
            return false;
        }
        if (passwordTwo.length() <= 0) {
            initToast(context, "请确认密码", true, 0);
            return false;
        }
        if (passwordTwo.length() >= 17) {
            initToast(context, "确认密码长度不大于16", true, 0);
            return false;
        }
        if (!TextUtils.equals(password, passwordTwo)) {
            initToast(context, "两次密码不一致", true, 0);
            return false;
        }
        return true;
    }

    /**
     * 将Bitmap保存在本地
     *
     * @param bitmap
     */
    public void saveBitmapFile(Bitmap bitmap, String uri) {
        File file = new File(uri);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一张图片
     *
     * @param context
     */
    public void headFail(Context context, String path) {
        cartState.setHeadBitmap(null);
        if (TextUtils.isEmpty(path)) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File file = new File(path);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            context.sendBroadcast(intent);
            file.delete();
        }
    }

    /**
     * 获取随机数
     *
     * @return
     */
    public int getRandom() {
        Random random = new Random();
        return random.nextInt(1000) + 1;
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */

    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证手机号码，11位数字，1开通，第二位数必须是3456789这些数字之一 *
     *
     * @param mobileNumber
     * @return
     */
    public static boolean checkMobileNumber(String mobileNumber) {
        boolean flag = false;
        try {
            // Pattern regex = Pattern.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
            Pattern regex = Pattern.compile("^1[345789]\\d{9}$");
            Matcher matcher = regex.matcher(mobileNumber);
            flag = matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;

        }
        return flag;
    }

    public void dialogProgress(final Context context, String Toast) {
        xProgressDialog = new XProgressDialog(context, Toast);
        xProgressDialog.show();
        isPrssor = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (xProgressDialog != null) {
                    if (isPrssor) {
                        initToast(context, "加载失败,请检查网络", true, 0);
                    }
                    xProgressDialog.dismiss();
                }
            }
        }, 6000);
    }

    public void dialogProgressClose() {
        if (xProgressDialog != null) {
            isPrssor = false;
            xProgressDialog.dismiss();
        }
    }

    /**
     * 判断是不是网络地址
     *
     * @param url
     * @return
     */
    public boolean isUrl(String url) {
        return url.indexOf("http://") != -1;
    }

    /**
     * 获取年月时分
     *
     * @param onDay
     * @param start
     * @param end
     * @return
     */
    public String getOnDay(String onDay, int start, int end) {
        return onDay.substring(start, end);
    }

    /**
     * 分享
     *
     * @param context
     * @param content
     */
    public void initShar(Context context, String content) {
        Intent intent1 = new Intent(Intent.ACTION_SEND);
        intent1.putExtra(Intent.EXTRA_TEXT, content);
        intent1.setType("text/plain");
        context.startActivity(Intent.createChooser(intent1, getPlantString(context, R.string.share)));
    }

    /**
     * 判断是否是正确的车牌号
     *
     * @param license
     * @return
     */
    public boolean licensePlate(String license) {
        String car_num_Regex = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z][A-Z][警京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]?[A-Z0-9]{4}[A-Z0-9挂学警港澳]$";
        if (TextUtils.isEmpty(license)) {
            return false;
        } else if (license.matches(car_num_Regex)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 生成二维码
     *
     * @param content
     * @param width
     * @param height
     * @return
     */
    public Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 手机号码隐藏中间
     */
    public String phoneEmpty(String pNumber) {
        if (!TextUtils.isEmpty(pNumber) && pNumber.length() > 6) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pNumber.length(); i++) {
                char c = pNumber.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * 获取屏幕宽度
     */
    public int getDisPlyWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    /**
     * 获取屏幕高度
     */
    public int getDisPlyHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    /**
     * 将一个view转化为图片
     *
     * @param view
     */

    public String viewSaveToImage(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);
        Bitmap cachebmp = loadBitmapFromView(view);
        FileOutputStream fos;
        String imagePath = "";
        try {
            boolean isHasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
            if (isHasSDCard) {
                File sdRoot = Environment.getExternalStorageDirectory();
                Log.e(TAG, sdRoot.toString());
                File file = new File(sdRoot, Calendar.getInstance().getTimeInMillis() + ".png");
                fos = new FileOutputStream(file);
                imagePath = file.getAbsolutePath();
            } else throw new Exception("创建文件失败!");
            cachebmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        view.destroyDrawingCache();
        return imagePath;
    }

    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */
        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }

    public String urlEnodeUTF8(String str) {
        String result = str;
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 转换时间戳
     *
     * @param expire
     * @return
     */
    public String getFetureDate(long expire) {
        if (String.valueOf(expire).length() == 10) {
            expire = expire * 1000;
        }
        Date date = new Date(expire);
        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String result = format.format(date);
        if (result.startsWith("0")) {
            result = result.substring(1);
        }
        return result;
    }

    /**
     * 界面设置状态栏字体颜色
     */
    public void changeStatusBarTextImgColor(Activity activity, boolean isBlack) {
        if (isBlack) {
            //设置状态栏黑色字体
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            //恢复状态栏白色字体
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public void closeKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = activity.getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public List<AllEmployees> getAllEmployeesList() {
        return allEmployeesList;
    }

    public void setAllEmployeesList(List<AllEmployees> allEmployeesList) {
        this.allEmployeesList = allEmployeesList;
    }

    public Bitmap getHeadBitmap() {
        return headBitmap;
    }

    public void setHeadBitmap(Bitmap headBitmap) {
        this.headBitmap = headBitmap;
    }

    public BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public void setmBluetoothAdapter(BluetoothAdapter mBluetoothAdapter) {
        this.mBluetoothAdapter = mBluetoothAdapter;
    }

    public Map<Integer, Ble> getBleMapList() {
        return bleMapList;
    }

    public void setBleMapList(Map<Integer, Ble> bleMapList) {
        this.bleMapList = bleMapList;
    }

    public Map<Integer, Boolean> getTitleList() {
        return titleList;
    }

    public void setTitleList(Map<Integer, Boolean> titleList) {
        this.titleList = titleList;
    }

    public List<String> getBleList() {
        return bleList;
    }

    public void setBleList(List<String> bleList) {
        this.bleList = bleList;
    }

    public List<String> getOptions1Items() {
        return options1Items;
    }

    public void setOptions1Items(List<String> options1Items) {
        this.options1Items = options1Items;
    }

    public List<List<String>> getOptions2Items() {
        return options2Items;
    }

    public void setOptions2Items(List<List<String>> options2Items) {
        this.options2Items = options2Items;
    }

    public List<List<List<String>>> getOptions3Items() {
        return options3Items;
    }

    public void setOptions3Items(List<List<List<String>>> options3Items) {
        this.options3Items = options3Items;
    }

    public List<Province> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<Province> provinceList) {
        this.provinceList = provinceList;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }

    public List<Notice> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<Notice> noticeList) {
        this.noticeList = noticeList;
    }

    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }

    public List<LossMan> getLossManList() {
        return lossManList;
    }

    public void setLossManList(List<LossMan> lossManList) {
        this.lossManList = lossManList;
    }

    public List<LossStaff> getLossStaffList() {
        return lossStaffList;
    }

    public void setLossStaffList(List<LossStaff> lossStaffList) {
        this.lossStaffList = lossStaffList;
    }

    public List<Audit> getAuditList() {
        return auditList;
    }

    public void setAuditList(List<Audit> auditList) {
        this.auditList = auditList;
    }

    public List<Record> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }

    public List<UserGoods> getUserGoodsList() {
        return userGoodsList;
    }

    public void setUserGoodsList(List<UserGoods> userGoodsList) {
        this.userGoodsList = userGoodsList;
    }

    public List<LossNews> getLossNewsList() {
        return lossNewsList;
    }

    public void setLossNewsList(List<LossNews> lossNewsList) {
        this.lossNewsList = lossNewsList;
    }

    public List<Position> getPositionList() {
        return positionList;
    }

    public void setPositionList(List<Position> positionList) {
        this.positionList = positionList;
    }

    public Datils getDatils() {
        return datils;
    }

    public void setDatils(Datils datils) {
        this.datils = datils;
    }

    public List<Employees> getEmployeesList() {
        return employeesList;
    }

    public void setEmployeesList(List<Employees> employeesList) {
        this.employeesList = employeesList;
    }

    public List<String> getImagePathsList() {
        return imagePathsList;
    }

    public void setImagePathsList(List<String> imagePathsList) {
        this.imagePathsList = imagePathsList;
    }

    public List<Home> getHomeList() {
        return homeList;
    }

    public void setHomeList(List<Home> homeList) {
        this.homeList = homeList;
    }

    public List<Service> getServiceLis() {
        return serviceLis;
    }

    public void setServiceLis(List<Service> serviceLis) {
        this.serviceLis = serviceLis;
    }

    public List<Complete> getCompleteList() {
        return completeList;
    }

    public void setCompleteList(List<Complete> completeList) {
        this.completeList = completeList;
    }

    public List<Entry> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<Entry> entryList) {
        this.entryList = entryList;
    }

    public List<com.cdqf.cart_class.Color> getColorList() {
        return colorList;
    }

    public void setColorList(List<com.cdqf.cart_class.Color> colorList) {
        this.colorList = colorList;
    }

    public List<ServiceOrder> getServiceOrderList() {
        return serviceOrderList;
    }

    public void setServiceOrderList(List<ServiceOrder> serviceOrderList) {
        this.serviceOrderList = serviceOrderList;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public List<Audits> getAuditsList() {
        return auditsList;
    }

    public void setAuditsList(List<Audits> auditsList) {
        this.auditsList = auditsList;
    }

    public List<Through> getThroughList() {
        return throughList;
    }

    public void setThroughList(List<Through> throughList) {
        this.throughList = throughList;
    }

    public List<Withdraw> getWithdrawList() {
        return withdrawList;
    }

    public void setWithdrawList(List<Withdraw> withdrawList) {
        this.withdrawList = withdrawList;
    }

    public List<AuditsJudge> getAuditsJudgeList() {
        return auditsJudgeList;
    }

    public void setAuditsJudgeList(List<AuditsJudge> auditsJudgeList) {
        this.auditsJudgeList = auditsJudgeList;
    }

    public List<ThroughsJudge> getThroughsJudgeList() {
        return throughsJudgeList;
    }

    public void setThroughsJudgeList(List<ThroughsJudge> throughsJudgeList) {
        this.throughsJudgeList = throughsJudgeList;
    }

    public List<Memebersship> getMemebersshipList() {
        return memebersshipList;
    }

    public void setMemebersshipList(List<Memebersship> memebersshipList) {
        this.memebersshipList = memebersshipList;
    }

    public List<Daily> getDailyList() {
        return dailyList;
    }

    public void setDailyList(List<Daily> dailyList) {
        this.dailyList = dailyList;
    }

    public List<Week> getWeekList() {
        return weekList;
    }

    public void setWeekList(List<Week> weekList) {
        this.weekList = weekList;
    }

    public List<Moth> getMothList() {
        return mothList;
    }

    public void setMothList(List<Moth> mothList) {
        this.mothList = mothList;
    }

    public List<Number> getNumberList() {
        return numberList;
    }

    public void setNumberList(List<Number> numberList) {
        this.numberList = numberList;
    }

    public Clock getClock() {
        return clock;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public List<Clockin> getClockinList() {
        return clockinList;
    }

    public void setClockinList(List<Clockin> clockinList) {
        this.clockinList = clockinList;
    }

    public MembersDatils getMembersDatils() {
        return membersDatils;
    }

    public void setMembersDatils(MembersDatils membersDatils) {
        this.membersDatils = membersDatils;
    }

    public List<Work> getWorkList() {
        return workList;
    }

    public void setWorkList(List<Work> workList) {
        this.workList = workList;
    }

    public List<Vaction> getVactionList() {
        return vactionList;
    }

    public void setVactionList(List<Vaction> vactionList) {
        this.vactionList = vactionList;
    }

    public List<PositionEmployees> getPositionEmployeesList() {
        return positionEmployeesList;
    }

    public void setPositionEmployeesList(List<PositionEmployees> positionEmployeesList) {
        this.positionEmployeesList = positionEmployeesList;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }
}

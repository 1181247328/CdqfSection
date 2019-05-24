package com.cdqf.cart_okhttp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cdqf.cart_hear.FileUtil;
import com.cdqf.cart_state.WIFIGpRs;
import com.cdqf.cart_utils.JSONValidator;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.io.File;
import java.util.Map;
import java.util.Set;

public class OKHttpRequestWrap {

    private static final String TAG = OKHttpRequestWrap.class.getSimpleName();

    private Map<String, String> okhttpParams;

    private Context context = null;

    private JSONValidator jsonValidator = null;

    private OnHttpRequest http = null;

    public OKHttpRequestWrap(Context context) {
        this.context = context;
        jsonValidator = new JSONValidator();
    }

    /**
     * post请求
     *
     * @param url
     * @param params
     */
    public void post(String url, boolean isDialog, String them, Map<String, Object> params, OnHttpRequest onHttpRequest) {
        http = onHttpRequest;
        PostFormBuilder postFormBuilder = new PostFormBuilder();
        postFormBuilder.url(url);
        Set<String> keys = params.keySet();
        for (String key : keys) {
            Object value = params.get(key);
            if (value.getClass() == String.class) {
                postFormBuilder.addParams(key, String.valueOf(value));
            } else if (value.getClass() == Integer.class) {
                postFormBuilder.addParams(key, String.valueOf(value));
            } else if (value.getClass() == Boolean.class) {
                postFormBuilder.addParams(key, String.valueOf(value));
            } else if (value.getClass() == Double.class) {
                postFormBuilder.addParams(key, String.valueOf(value));
            } else {
                //TODO
            }
        }
        postFormBuilder.build().execute(new OKHttpStringCallback(context, isDialog, them, new OnOkHttpResponseHandler() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                if (!WIFIGpRs.isNetworkConnected(context)) {
                    Log.e(TAG, "---无网---");
                    Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response == null) {
                    Toast.makeText(context, "数据请求失败,请重新请求", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!jsonValidator.validate(response)) {
                    Toast.makeText(context, "JSON格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (http != null) {
                    http.onOkHttpResponse(response, id);
                }
            }

            /**
             * 失败
             * @param error
             */
            @Override
            public void onOkHttpError(String error) {
                if (http != null) {
                    http.onOkHttpError(error);
                }
            }
        }));
    }

    /**
     * 表单形式上传单张图片
     *
     * @param fileInput
     * @param url
     * @param isDialog
     * @param them
     * @param params
     * @param onHttpRequest
     */
    public void post(PostFormBuilder.FileInput fileInput, boolean isImage, String url, boolean isDialog, String them, Map<String, String> params, OnHttpRequest onHttpRequest) {
        http = onHttpRequest;
        PostFormBuilder postFormBuilder = new PostFormBuilder();
        if (isImage) {
            postFormBuilder.addFile("img", "head.png", new File(FileUtil.IMG_CACHE4));
            Log.e(TAG, "----" + fileInput.key + "---" + fileInput.filename + "----" + FileUtil.IMG_CACHE4);
        }
        postFormBuilder.url(url);
        postFormBuilder.params(params);
        postFormBuilder.build().execute(new OKHttpStringCallback(context, isDialog, them, new OnOkHttpResponseHandler() {
            @Override
            public void onOkHttpResponse(String response, int id) {
                if (http != null) {
                    http.onOkHttpResponse(response, id);
                }
            }

            /**
             * 失败
             * @param error
             */
            @Override
            public void onOkHttpError(String error) {
                if (http != null) {
                    http.onOkHttpError(error);
                }
            }
        }));
    }
}

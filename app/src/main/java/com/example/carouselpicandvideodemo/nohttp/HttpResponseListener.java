/*
 * Copyright 2015 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.carouselpicandvideodemo.nohttp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ParseException;
import android.util.Log;

import com.example.carouselpicandvideodemo.R;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.NotFoundCacheError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.RestResponse;

import java.net.ProtocolException;

/**
 * HttpResponseListener
 * Created by 孙亚斌 on 2017/2/14 , 9:25.
 * 单例回调
 */
public class HttpResponseListener<T> implements OnResponseListener<T> {

    private static final String TAG="HttpResponseListener";
    private Context context;
    private Activity activity;
    /**
     * Dialog.
     */
    private WaitDialog mWaitDialog;
    /**
     * Request.
     */
    private Request<?> mRequest;
    /**
     * 结果回调.
     */
    private HttpListener callback;

    /**
     * @param context     context用来实例化dialog.
     * @param request      请求对象.
     * @param httpCallback 回调对象.
     * @param canCancel    是否允许用户取消请求.
     * @param isLoading    是否显示dialog.
     */
    public HttpResponseListener(Context context, Request<?> request, HttpListener<T> httpCallback, boolean canCancel, boolean isLoading) {
        this.context = context;
        this.activity= (Activity) context;
        this.mRequest = request;
        if (context != null && isLoading) {
            mWaitDialog = new WaitDialog(context);
            mWaitDialog.setCancelable(canCancel);
            mWaitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mRequest.cancel();
                }
            });
        }
        this.callback = httpCallback;
    }



    /**
     * 开始请求, 这里显示一个dialog.
     */
    @Override
    public void onStart(int what) {
        if (mWaitDialog != null  && !mWaitDialog.isShowing())
            mWaitDialog.show();
    }

    /**
     * 结束请求, 这里关闭dialog.
     */
    @Override
    public void onFinish(int what) {
        if (mWaitDialog != null && mWaitDialog.isShowing())
            mWaitDialog.dismiss();
    }

    /**
     * 成功回调.
     */
    @Override
    public void onSucceed(int what, Response<T> response) {
        if (callback != null) {
            // 这里判断一下http响应码，这个响应码问下你们的服务端你们的状态有几种，一般是200成功。
            // w3c标准http响应码：http://www.w3school.com.cn/tags/html_ref_httpmessages.asp

            int coce = response.responseCode();
            if (coce == 200 || coce == 304) {// 如果使用http标准的304重定向到缓存的话，还要判断下304状态码。
                callback.onSucceed(what, response);
            } else { // 如果

                Log.e(TAG, "onSucceed: "+response.responseCode()+"==="+response.getHeaders());
                Log.e(TAG, "onSucceed: "+response.get());

                Response<T> error = new RestResponse<>(response.request(),
                        response.isFromCache(),
                        response.getHeaders(),
                        null,
                        response.getNetworkMillis(),
                        new Exception()); // 这里可以传一个你的自定义异常。
                onFailed(what, error); // 去让错误的回调处理。
            }
        }
    }

    /**
     * 失败回调.
     */
    @Override
    public void onFailed(int what, Response<T> response) {
        Exception exception = response.getException();
        /*if (exception instanceof NetworkError) {// 网络不好
            ToastUtil.showToast(context, R.string.error_please_check_network);
        } else if (exception instanceof TimeoutError) {// 请求超时
            //CarouselApplication.getInstance().changeUrl();
            ToastUtil.showToast(context, R.string.error_timeout);
        } else if (exception instanceof UnKnownHostError) {// 找不到服务器
            ToastUtil.showToast(context, R.string.error_not_found_server);
        } else if (exception instanceof URLError) {// URL是错的
            ToastUtil.showToast(context, R.string.error_url_error);
        } else if (exception instanceof NotFoundCacheError) {
            // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
            ToastUtil.showToast(context, R.string.error_not_found_cache);
        } else if (exception instanceof ProtocolException) {
            ToastUtil.showToast(context, R.string.error_system_unsupport_method);
        } else if (exception instanceof ParseException) {
            ToastUtil.showToast(context, R.string.error_parse_data_error);
        } else {
            ToastUtil.showToast(context, R.string.error_unknow);
        }*/
        Log.i("SUN","错误：" + exception.getMessage()+"========"+exception.toString());
        if (callback != null)
            callback.onFailed(what, response);
    }
}

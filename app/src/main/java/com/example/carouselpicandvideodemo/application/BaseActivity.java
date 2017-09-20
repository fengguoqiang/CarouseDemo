package com.example.carouselpicandvideodemo.application;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carouselpicandvideodemo.R;
import com.example.carouselpicandvideodemo.bean.UserBean;
import com.example.carouselpicandvideodemo.nohttp.CallServer;
import com.example.carouselpicandvideodemo.nohttp.HttpListener;
import com.example.carouselpicandvideodemo.utils.SunUtils;
import com.google.gson.Gson;
import com.yanzhenjie.nohttp.rest.Request;

/**
 * BaseActivity
 * Created by 孙亚斌 on 2016/12/28 , 17:11.
 * 所有Activity的鸡肋
 * use:anyActivity
 */

public abstract class BaseActivity extends AppCompatActivity {
    /**context*/
    public Context mContext = null;
    /**加载中dialog*/
    public Dialog loadDialog;
    public String BASE_URL;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(mContext==null){
            mContext = this;
        }
        //将Activity添加到activityList中
        CarouselApplication.getInstance().mActivityStack.addActivity(this);
        //BASE_URL= CarouselApplication.getInstance().getBaseUrl();
        initContentView();
        initView();
        initData();
        setListener();
    }
    protected abstract void initContentView();
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void setListener();

    public Context getmContext(){
        return mContext;
    }
    /**
     * 跳转公共方法 不带参数
     *
     * @param cls  目标class
     */
    public void startMyActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    /**
     * @param cls 目标class
     * @param keys
     * @param values
     */
    public void startMyActivity(Class<?> cls, String[] keys, String[] values) {
        Intent intent = new Intent(this, cls);
        int size = keys.length;
        for (int i = 0; i < size; i++) {
            intent.putExtra(keys[i], values[i]);
        }
        startActivity(intent);
    }

    /**
     * 加载数据时的Loading
     *
     * @param msg
     *            提示信息
     * @return
     */
    public void showLoadDialog(String msg) {
        if (isFinishing()) {
            closeLoadDialog();
            return;
        }
        if (loadDialog == null) {
            loadDialog = new Dialog(this, R.style.CustomProgressDialog);
            loadDialog
                    .setContentView(R.layout.progress_dialog_default);
            loadDialog.setCancelable(true);
            loadDialog.setCanceledOnTouchOutside(false);
            ImageView notice_icon = (ImageView) loadDialog.getWindow()
                    .findViewById(R.id.loadingImageView);
            Animation operatingAnim = AnimationUtils.loadAnimation(this,
                    R.anim.progress_round);
            LinearInterpolator lin = new LinearInterpolator();
            operatingAnim.setInterpolator(lin);
            if (operatingAnim != null) {
                notice_icon.startAnimation(operatingAnim);
            }
        }
        TextView notice_msg = (TextView) loadDialog.getWindow().findViewById(
                R.id.id_tv_loadingmsg);
        notice_msg.setText(msg);
        try {
            loadDialog.show();
        } catch (Exception e) {
            System.out.println("loading框show方法出错，异常被捕获");
            e.printStackTrace();
        }
    }

    /**
     * 关闭加载dialog
     */
    public void closeLoadDialog() {
        if (loadDialog != null) {
            try {
                loadDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("之前的activity被销毁，dialog关闭异常。该报错可以忽略");
            }
            loadDialog = null;
        }
    }

    /**
     * 设置LoadDialog 是否可取消 默认不可取消
     */
    public void setLoadDialogCancelable(boolean bool) {
        if (loadDialog != null) {
            loadDialog.setCancelable(bool);

        }
    }
    /**
     * 简易toast
     * @param str
     */
    public void showToast(String str){
        Toast.makeText(mContext,str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 简易toast 长时间显示
     * @param str
     */
    public void showLongToast(String str){
        Toast.makeText(mContext,str, Toast.LENGTH_LONG).show();
    }
    /**
     * 网络无连接
     */
    public void noNetToast(){
        Toast.makeText(mContext,"网络未连接 , 请检查网络", Toast.LENGTH_SHORT).show();
    }

    /**
     * 发起请求
     * @param what  用来标志请求, 当多个请求使用同一个Listener时, 在回调方法中会返回这个what。
     * @param request 请求对象。
     * @param callback  结果回调对象
     * @param canCancel  是否可取消
     * @param isLoading  是否显示加载框
     * @param <T>
     */
    public <T> void startRequest(int what, Request<T> request, HttpListener<T> callback, boolean canCancel, boolean isLoading) {
        request.setCancelSign(this);
        CallServer.getRequestInstance().add(this, what, request, callback, canCancel, isLoading);
    }
    /**
     * 发起请求(可取消,显示加载框)
     * @param what  用来标志请求, 当多个请求使用同一个Listener时, 在回调方法中会返回这个what。
     * @param request 请求对象。
     * @param callback  结果回调对象
     * @param <T>
     */
    public <T> void startRequest(int what, Request<T> request, HttpListener<T> callback) {
        request.setCancelSign(this);
        CallServer.getRequestInstance().add(this, what, request, callback, true, true);
    }
    /**
     * 把数据保存到缓存中
     *
     * @param bean
     */
    public void setUserInfo(UserBean bean) {
        SharedPreferences settings = getSharedPreferences(UserBean.MARK, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        UserBean oldBean = getUserInfo();
        UserBean newBean = SunUtils.replaceUserBean(bean,oldBean);
        editor.putString("logininfo", new Gson().toJson(newBean, UserBean.class));
        editor.commit();
    }

    /**
     * 从缓存中取出数据
     *
     * @param
     */
    public UserBean getUserInfo() {
        SharedPreferences settings = getSharedPreferences(UserBean.MARK, Context.MODE_PRIVATE);
        String info = settings.getString("logininfo", "");
        if(SunUtils.isEmpty(info)){
            return new UserBean("","","","","","");
        }else{
            return new Gson().fromJson(info,
                    UserBean.class);
        }
    }


    /**
     * 清除登录信息
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!慎用!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    public void clearUserInfo(){
            SharedPreferences sp = getSharedPreferences(UserBean.MARK, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();
        }

   /* *//**
     * 无网时显示提示对话框
     *//*
    public void NetWorkInfo(SplashActivity activity){
        // 判断手机是否连接网络
        if (!NetUtils.hasNetwork(activity)) {
            // 未连接
            WarningFragment warning = new WarningFragment();
            warning.show(activity.getFragmentManager(), "Warning");
            return;
        }
    }*/



    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        //CallServer.getRequestInstance().cancelBySign(this);
        super.onDestroy();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        CarouselApplication.getInstance().mActivityStack.finishActivity(this);
    }
    /**关闭所有activity
     * !!!!!!!!!!!!!!!!!!!!!!慎用!!!!!!!!!!!!!!!!!!!!!
     * */
    public void finishAll(){
        CarouselApplication.getInstance().mActivityStack.finishAllActivity();
    }
}


/*
 *
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
 * 　　　　┃　　　┃    神兽保佑,代码无bug
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 *
 * ━━━━━━感觉萌萌哒━━━━━━
 */
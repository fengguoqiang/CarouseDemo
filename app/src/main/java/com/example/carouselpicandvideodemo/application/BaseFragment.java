package com.example.carouselpicandvideodemo.application;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by 孙亚斌 on 2016/10/27 , 14:06.
 *  fragment鸡肋
 * from:
 * to:
 */
public  abstract class BaseFragment extends Fragment {
    /**context*/
    public Context mContext;
    /**加载中dialog*/
    public Dialog loadDialog;
    public String BASE_URL;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mContext==null){
            mContext = getActivity();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mContext==null){
            mContext = getActivity();
        }
        //BASE_URL= CarouselApplication.getInstance().getBaseUrl();
        initView(view);
        initData();
        setListener();
    }

    protected abstract void initView(View view);
    protected abstract void initData();
    protected abstract void setListener();
    /**
     * 跳转公共方法1 不带参数
     *
     * @param cls  目标class
     */
    public void startMyActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    /**
     * @param cls 目标class
     * @param keys
     * @param values
     */
    public void startMyActivity(Class<?> cls, String[] keys, String[] values) {
        Intent intent = new Intent(getActivity(), cls);
        int size = keys.length;
        for (int i = 0; i < size; i++) {
            intent.putExtra(keys[i], values[i]);
        }
        startActivity(intent);
    }

    /**
     * 简易toast
     * @param str
     */
    public void showToast(String str){
        Toast.makeText(getActivity(),str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 加载数据时的Loading
     *
     * @param msg
     *            提示信息
     * @return
     */
    public void showLoadDialog(String msg) {

        if (loadDialog == null) {
            loadDialog = new Dialog(getActivity(), R.style.CustomProgressDialog);
            loadDialog
                    .setContentView(R.layout.progress_dialog_default);
            loadDialog.setCancelable(true);
            loadDialog.setCanceledOnTouchOutside(false);
            ImageView notice_icon = (ImageView) loadDialog.getWindow()
                    .findViewById(R.id.loadingImageView);
            Animation operatingAnim = AnimationUtils.loadAnimation(getActivity(),
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
        CallServer.getRequestInstance().add(getActivity(), what, request, callback, canCancel, isLoading);
    } /**
     * 发起请求(可取消,显示加载框)
     * @param what  用来标志请求, 当多个请求使用同一个Listener时, 在回调方法中会返回这个what。
     * @param request 请求对象。
     * @param callback  结果回调对象
     * @param <T>
     */
    public <T> void startRequest(int what, Request<T> request, HttpListener<T> callback) {
        request.setCancelSign(this);
        CallServer.getRequestInstance().add(getActivity(), what, request, callback, true, true);
    }
    /**
     * 设置LoadDialog 是否可取消 默认不可取消
     */
    public void setLoadDialogCancelable(boolean bool) {
        if (loadDialog != null) {
            loadDialog.setCancelable(bool);
        }
    }

    /*
     * 清除登录信息
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!慎用!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
//    public void clearUserInfo(){
//        SharedPreferences sp = getActivity().getSharedPreferences(UserBean.MARK, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.clear();
//        editor.commit();
//    }
    /**
     * 把数据保存到缓存中
     *
     * @param bean
     */
    public void setUserInfo(UserBean bean) {
        SharedPreferences settings = getActivity().getSharedPreferences(UserBean.MARK, Context.MODE_PRIVATE);
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
        SharedPreferences settings = getActivity().getSharedPreferences(UserBean.MARK, Context.MODE_PRIVATE);
        String info = settings.getString("logininfo", "");
        if(SunUtils.isEmpty(info)){
            return new UserBean("","","","","","");
        }else{
            return new Gson().fromJson(info,
                    UserBean.class);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        CallServer.getRequestInstance().cancelBySign(this);
    }
}

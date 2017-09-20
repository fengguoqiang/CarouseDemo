package com.example.carouselpicandvideodemo.application;

import android.app.Application;
import android.content.Context;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;

/**
*CarouselApplication
*created at 2017/7/11 11:26 by FENG
*作用：应用Application
*/
public class CarouselApplication extends Application {
    /**调试模式*/
    protected static final boolean DEBUG = true;
    /**实例化application*/
    protected static CarouselApplication mApplication = null;
    /** 上下文 */
    protected Context mContext  = null;
    /** Activity 栈 */
    public ActivityStack mActivityStack = null;

    private String url;

	@Override
	public void onCreate() {
		super.onCreate();
        mApplication = this;// 由于Application类本身已经单例，所以直接按以下处理即可
        mContext = getApplicationContext();     // 获取上下文
        mActivityStack = new ActivityStack();   // 初始化Activity 栈
        NoHttp.initialize(this);
        Logger.setDebug(DEBUG);// 开启NoHttp的调试模式, 配置后可看到请求过程、日志和错误信息。
        Logger.setTag("NoHttp");// 设置NoHttp打印Log的tag。
	}

    /**
     * 获取当前类实例对象
     * @return
     */
    public static CarouselApplication getInstance(){
        return mApplication;
    }

    public String getUrl(){
        return url;
    }

}

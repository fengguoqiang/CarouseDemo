package com.example.carouselpicandvideodemo.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewAnimator;

import com.bumptech.glide.Glide;
import com.example.carouselpicandvideodemo.R;
import com.example.carouselpicandvideodemo.application.BaseActivity;

import java.io.File;

/**
*CarouselActivity
*created at 2017/9/20 16:59 by FENG
*作用：图片和视频轮播
*/
public class CarouselActivity extends BaseActivity {

    private ViewAnimator viewanimator;
    private ImageView iv_pic;
    private VideoView vv_video;
    private File file;
    private File[] files;
    private int i = 0;
    private String filepath;
    private int t=0;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //Bitmap bm = (Bitmap) msg.obj;
                    //iv_pic.setImageBitmap(bm);
                    if(!CarouselActivity.this.isFinishing()){
                        Glide.with(CarouselActivity.this).load(msg.obj).error(R.drawable.fengjing).animate(android.R.anim.slide_in_left).dontAnimate().into(iv_pic);
                        i++;
                        action1();
                    }
                    break;
            }
        };
    };

    @Override
    protected void initContentView() {
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.tui_guanggao_act);
    }

    @Override
    protected void initView() {
        viewanimator = (ViewAnimator) findViewById(R.id.va_view);
        iv_pic = (ImageView) findViewById(R.id.iv_xuanchuan_image);
        vv_video = (VideoView) findViewById(R.id.vv_video);
    }

    @Override
    protected void initData() {
        //获取图片和视频路径
        Intent intent = getIntent();
        file = new File(intent.getStringExtra("path"));

        //获取windows信息
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int videoWidth = dm.widthPixels;
        int videoHeight = dm.heightPixels;
        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                videoWidth, videoHeight);
        //设置视频播放屏幕
        vv_video.setLayoutParams(layoutparams);
        vv_video.getHolder().setFixedSize(videoWidth, videoHeight);
        //判断文件夹是否存在
        if (!file.exists()) {
            Toast.makeText(this, "您没有内容浏览", Toast.LENGTH_SHORT).show();
        } else {
            files = file.listFiles();
            action1();
        }
    }

    @Override
    protected void setListener() {

    }

    public void action1() {
        if (i < files.length) {
            filepath = files[i].getAbsolutePath().toString();
            if (filepath.toLowerCase().endsWith(".jpg") || filepath.toLowerCase().endsWith(".png") // 图片判读需要改进
                    || filepath.toLowerCase().endsWith(".gif")) {
                viewanimator.setDisplayedChild(0);
                //bitmap = bitmap = BitmapFactory.decodeFile(filepath);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = filepath;
                if (t > 3000) {
                    t = 3000;
                }
                handler.sendMessageDelayed(msg, t);
                t += 3000;
            } else {
                play();
            }
        } else {
            i = 0;
            action1();
        }
    }

    public void play() {
        viewanimator.setDisplayedChild(1);
        File file = new File(filepath);
        MediaController mc = new MediaController(CarouselActivity.this);
        if (file.exists()) {
            vv_video.setVideoPath(file.getAbsolutePath());
            vv_video.setMediaController(mc);
            mc.setMediaPlayer(vv_video);
            vv_video.requestFocus();
            vv_video.start();
            vv_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    i++;
                    action1();
                }
            });
            vv_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer arg0) {

                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction()==KeyEvent.KEYCODE_BACK){
            handler.removeMessages(0);
        }
        return super.onKeyDown(keyCode, event);
    }
}

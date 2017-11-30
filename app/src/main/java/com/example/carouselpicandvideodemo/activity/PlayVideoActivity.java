package com.example.carouselpicandvideodemo.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import com.example.carouselpicandvideodemo.R;
import com.example.carouselpicandvideodemo.application.BaseActivity;

/**
 * PlayVideoActivity
 * created at 2017/11/23 16:02 by FENG
 * 作用：视频播放
 */

public class PlayVideoActivity extends BaseActivity {

    private VideoView videoView;

    @Override
    protected void initContentView() {
        setContentView(R.layout.play_video_layout);
    }

    @Override
    protected void initView() {
        videoView = findViewById(R.id.vv_video);
    }

    @Override
    protected void initData() {
        //获取windows信息
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int videoWidth = dm.widthPixels;
        int videoHeight = dm.heightPixels;
        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                videoWidth, videoHeight);
        //设置视频播放屏幕
        videoView.setLayoutParams(layoutparams);
        videoView.getHolder().setFixedSize(videoWidth, videoHeight);
        play();
    }

    public void play() {
        MediaController mc = new MediaController(PlayVideoActivity.this);
        videoView.setVideoURI(Uri.parse("http://60.223.238.80:8080/ECPse/corresponding/evideo.mp4"));
        videoView.setMediaController(mc);
        mc.setMediaPlayer(videoView);
        videoView.requestFocus();
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(PlayVideoActivity.this, "播放完成", Toast.LENGTH_LONG).show();
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer arg0) {

            }
        });

    }

    @Override
    protected void setListener() {

    }
}

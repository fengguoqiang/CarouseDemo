package com.example.carouselpicandvideodemo.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.Toast;

import com.example.carouselpicandvideodemo.R;
import com.example.carouselpicandvideodemo.application.BaseActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import java.io.File;
import java.util.List;

/**
*MainActivity
*created at 2017/11/23 15:02 by FENG
*作用：docx文档下载
*/

public class MainActivity extends BaseActivity {

    private Button btn_doc;
    private Button btn_video;
    private ProgressDialog pd;
    private Button btn_down;
    private String Current_path=Environment.getExternalStorageDirectory().getAbsolutePath() + "/中科云媒文档介绍.docx";
    private Intent intent;

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_my_main);
    }

    @Override
    protected void initView() {
        btn_doc = findViewById(R.id.btn_doc);
        btn_video = findViewById(R.id.btn_video);
        btn_down = findViewById(R.id.btn_down);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
       btn_doc.setOnClickListener(v -> {
           Intent intent=new Intent();
           intent.setAction(Intent.ACTION_VIEW);
           Uri uri=Uri.parse("http://60.223.238.80:8080/ECPse/corresponding/EPCIntroduce.docx");
           intent.setDataAndType(uri,"application/msword");
           startActivity(intent);
       });
       btn_down.setOnClickListener(v -> {
           AndPermission.with(MainActivity.this).permission(Manifest.permission.WRITE_EXTERNAL_STORAGE).requestCode(200).callback(listener).start();
       });

       btn_video.setOnClickListener(v -> {
           intent = new Intent(MainActivity.this,PlayVideoActivity.class);
           startActivity(intent);
       });
    }


    private PermissionListener listener=new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode){
                case 200:
                    downAPK();
                    break;
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode){
                case 200:
                    showToast("您拒绝使用该功能");
                    break;
            }
        }
    };

    protected void downAPK() {
        showProgressDialog();
        // 下载APK，并且替换安装
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // sdcard存在
            FinalHttp finalhttp = new FinalHttp();
            finalhttp.download("http://60.223.238.80:8080/ECPse/corresponding/EPCIntroduce.docx",Current_path, new AjaxCallBack<File>() {

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    t.printStackTrace();
                    Toast.makeText(getApplicationContext(), "下载失败",
                            Toast.LENGTH_SHORT).show();
                    if(pd.isShowing()){
                        pd.dismiss();
                    }
                    super.onFailure(t, errorNo, strMsg);
                }

                @Override
                public void onLoading(long count, long current) {
                    super.onLoading(count, current);
                    // 当前下载百分比
                    int max = (int) count / 1024 / 1024;
                    pd.setMax(max);
                    int progress = (int) current / 1024 / 1024;
                    pd.setProgress(progress);
                    if (progress == max) {
                        pd.dismiss();
                    }
                }

                @Override
                public void onSuccess(File t) {
                    super.onSuccess(t);
                    Intent intent=new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    File file=new File(Current_path);
                    Uri uri=Uri.parse(file.getAbsolutePath());
                    intent.setDataAndType(uri,"application/msword");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "没有sdcard，请安装上在试或",
                    Toast.LENGTH_SHORT).show();
            return;
        }
    }

    protected void showProgressDialog() {
        pd = new ProgressDialog(MainActivity.this);
        pd.setCancelable(false);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setTitle("下载");
        pd.setMessage("下载中.....");
        pd.setProgress(0);
        pd.show();
    }
}

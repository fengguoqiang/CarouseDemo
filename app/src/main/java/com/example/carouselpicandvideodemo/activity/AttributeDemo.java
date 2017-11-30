package com.example.carouselpicandvideodemo.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import com.example.carouselpicandvideodemo.R;
import com.example.carouselpicandvideodemo.application.BaseActivity;
import com.example.carouselpicandvideodemo.bean.Person;
import com.example.carouselpicandvideodemo.nohttp.HttpListener;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
*AttributeDemo
*created at 2017/11/28 17:34 by FENG
*作用：手机号码归属地批量查询
*/
public class AttributeDemo extends BaseActivity {

    private static final String TAG = "AttributeDemo";//tag标识
    private Button btn_attribute;//按钮，归属地查询开始
    private ArrayList<Person> list;//存储所有的手机号码对象
    private String CurrentPhone;//现查询的手机号码
    private int index = 0;//现查询的手机号码索引
    private int num=0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //开始查询
                    CurrentPhone = list.get(index).getPhone();
                    AttributeAction(CurrentPhone);
                    break;
                case 1:
                    //查询下一个
                    index++;
                    //防止索引超标
                    if(index>list.size()){
                        Log.e(TAG, "成功查询到的手机号码个数 "+num);
                        //保存数据
                        SavaData();
                    }else{
                        //查询下一个
                        CurrentPhone=list.get(index).getPhone();
                        AttributeAction(CurrentPhone);
                    }
                    break;
            }
        }
    };

    @Override
    protected void initContentView() {
        //设置布局
        setContentView(R.layout.attribute_layout);
    }

    @Override
    protected void initView() {
        //控件实例化
        btn_attribute = findViewById(R.id.btn_attribute);
    }

    @Override
    protected void initData() {

    }

    /**
     * 获取要查询的手机号码数据
     */
    private void getData() {
        Request<String> request = NoHttp.createStringRequest("http://192.168.1.100:8080/javaweb/servlet/AttributeDemo", RequestMethod.POST);
        startRequest(0, request, callback, false, false);
    }

    /**
     * 访问网络返回的数据处理
     */
    private HttpListener<String> callback = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            switch (what) {
                case 0:
                    Log.e(TAG, "onSucceed: " + response.get());//日志输出获取到的所有手机号码
                    //解析并存储
                    try {
                        JSONObject jo = new JSONObject(response.get());
                        JSONArray ja = jo.getJSONArray("phones");
                        list = new ArrayList<Person>();
                        for (int i = 0; i < ja.length(); i++) {
                            Person person = new Person();
                            person.setPhone(ja.getString(i));
                            list.add(person);
                        }
                        Log.e(TAG, "onSucceed: " + list.get(0));
                        //发一个消息，开始查询
                        handler.sendEmptyMessage(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    Toast.makeText(AttributeDemo.this,"第"+num+"个",Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jo = new JSONObject(response.get());
                        JSONObject jsonObject = jo.getJSONObject("result");
                        String result = jsonObject.getString("status");
                        if (result.endsWith("1")) {
                            num++;
                            Person person = list.get(index);
                            person.setProvince(jsonObject.getString("province"));
                            person.setCity(jsonObject.getString("city"));

                            Log.e(TAG, "onSucceed: "+person.toString());
                        } else {

                        }
                        handler.sendEmptyMessage(1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }

        @Override
        public void onFailed(int what, Response<String> response) {

        }
    };

    /**
     * 查询数据归属地
     * @param phone
     */
    public void AttributeAction(String phone) {
        Request<String> request = NoHttp.createStringRequest("https://way.jd.com/shujujia/mobile", RequestMethod.POST);
        request.add("mobile", phone);
        request.add("appkey", "14c1afcb83296e7aa717470a62b37379");
        startRequest(1, request, callback, false, false);
    }

    /**
     * 保存查找到的手机号码归属地信息
     */
    public void SavaData(){
        try {
            FileOutputStream fileOutputStream =this.openFileOutput("attribute", Context.MODE_PRIVATE);
            for(Person person:list){
                fileOutputStream.write(person.toString().getBytes());
            }
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "initData: " + "创建失败");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "initData: " + "输入错误");
            e.printStackTrace();
        }
        Toast.makeText(AttributeDemo.this,"查询完成，成功查询到num个手机号码的归属地",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void setListener() {
        btn_attribute.setOnClickListener(v -> {
            //获取要查询的手机号码数据
            getData();
        });
    }

}

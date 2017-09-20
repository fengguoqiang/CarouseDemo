package com.example.carouselpicandvideodemo.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.example.carouselpicandvideodemo.bean.UserBean;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
*SunUtils
*created at 2017/9/4 9:14 by FENG
*作用：工具类
*/
@SuppressLint("SimpleDateFormat")
public class SunUtils {


    /**
     * 判断第三方应用是否安装
     * @param openPackageName 要验证的应用包名
     * @param mActivity
     */
    public static boolean isInstalledApplication(String openPackageName, Context mActivity) {
        String packageName1="";
        PackageManager pm = mActivity.getPackageManager();//获取应用包管理器
        List<ApplicationInfo> packs = pm.getInstalledApplications(0); //获取已经安装应用的ApplicationInfo

        for (ApplicationInfo pi : packs) {
            String bb = pi.processName;
            if (bb.equals(openPackageName)) {
                return true;
            }
        }
        return false;
    }

    // 字符串的非空
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || "null".equals(input))
            return true;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证密码
     *
     * @param password
     * @return
     */
    public static boolean isPassword(String password) {
        Pattern p = Pattern.compile("^/w{6,16}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     * 判断是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) { // 不是数字
            return false;
        }
    }

    public static String checkInt(String num) {
        return (num == null || !SunUtils.isNumeric(num)) ? "0" : num;
    }

    /**
     * String检查Integer数据
     */
    public static Integer isInteger(String num) {
        return Integer.parseInt(checkInt(num));
    }

    /**
     * 检查Double数据
     */
    public static Double isDouble(Double num) {
        return isDouble(num + "");
    }

    /**
     * String检查Double数据
     */
    public static Double isDouble(String num) {
        return Double.parseDouble(checkDouble(num));
    }

    /**
     * 方法名: getDouble2 描述作用: String检查Double数据
     *
     * @param str
     * @return 小数点2位的double
     * @throws
     */
    public static double formatDouble(String str) {
        return formatDouble(isDouble(str));
    }

    /**
     * 方法名: doubleAdd 描述作用: String检查Double数据
     *
     * @param
     * @return 小数点2位的double
     * @throws
     */
    public static double doubleAdd(double... str) {
        double x = 0.00;
        for (double d : str) {
            x = x + d;
        }
        return formatDouble(x);
    }

    /**
     * 方法名: getDouble2 描述作用: String检查Double数据
     *
     * @param num
     * @return 小数点2位的double
     * @throws
     */
    public static String getDouble2(String num) {
        double a = isDouble(num);
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(a);
    }

    /**
     * 方法名: getDouble2 描述作用: String检查Double数据
     *
     * @param num
     * @return 小数点2位的double
     * @throws
     */
    public static String getDouble2(double num) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }

    /**
     * double取两位
     *
     * @param
     * @param
     * @return
     */
    public static double formatDouble(double a) {
        DecimalFormat df = new DecimalFormat("0.00");
        return isDouble(df.format(a));
    }

    public static String formatDoubleOne(double a) {
        DecimalFormat df = new DecimalFormat("######0.0");
        return df.format(a);
    }


    public static String checkDouble(String num) {
        return (num == null || "NaN".equals(num) || !isDoubleNumeric(num)) ? "0" : num;
    }

    /**
     * 判断是否为double类型
     *
     * @param str
     * @return
     */
    public static boolean isDoubleNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) { // 不是数字
            return false;
        }
    }

    /**
     * @param @param  inputText
     * @param @return
     * @return boolean
     * @throws
     * @Title: isPhone
     * @Description: 判断是否为手机号
     */
    public static boolean isPhone(String inputText) {
        Pattern p = Pattern.compile("^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8]))\\d{8}$");


        Matcher m = p.matcher(inputText);
        return m.matches();
    }

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return true有网   false无网
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager context1;
        NetworkInfo context2;
        return context != null ? (context1 = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)) != null && (context2 = context1.getActiveNetworkInfo()) != null && context2.isConnected() && context2.getState() == NetworkInfo.State.CONNECTED : false;
    }

    /**
     * 是否wifi网络
     *
     * @param context
     * @return true标识当前是wifi
     */
    public static boolean isWifiConnected(Context context) {
        NetworkInfo context1;
        return context != null && (context1 = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(1)) != null ? context1.isAvailable() : false;
    }

    /**
     * 是否移动网络
     *
     * @param context
     * @return true标识当前是移动网络
     */
    public static boolean isMobileConnected(Context context) {
        NetworkInfo context1;
        return context != null && (context1 = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(0)) != null ? context1.isAvailable() : false;
    }

    /**
     * 获取当前网络类型(ConnectivityManager.TYPE_MOBILE  ConnectivityManager.TYPE_WIFI)
     * @param context
     * @return
     */
    public static int getConnectedType(Context context) {
        NetworkInfo context1;
        return context != null && (context1 = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo()) != null && context1.isAvailable() ? context1.getType() : -1;
    }

    /**
     * 获取包名
     *
     * @param context
     * @return packageName
     */
    public static String getPackageName(Context context) {
        return context.getApplicationInfo().packageName;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return 版本号
     * @throws PackageManager.NameNotFoundException
     */
    public static String getVersionName1(Context context) throws PackageManager.NameNotFoundException {//获取版本号
        PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        return pi.versionName;
    }

    /**
     * 检查当前项目的Versionname
     */
    public static double getVersionName(Context context) {
        String newVersionName=null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            newVersionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return Double.valueOf(newVersionName);
    }

    /**
     * 获取版本号(内部识别号)
     * @param context
     * @return 识别号
     * @throws PackageManager.NameNotFoundException
     */
    public static int getVersionCode(Context context)throws PackageManager.NameNotFoundException{
        PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        return pi.versionCode;
    }
        public static boolean isIP(String addr) {
        if(addr.length() >= 7 && addr.length() <= 15 && !"".equals(addr)) {
            String var1 = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
            return Pattern.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}").matcher(addr).find();
        } else {
            return false;
        }
    }
    
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dp) {
        return (int ) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }

    /**
     * 获取屏幕分辨率的宽度
     * 
     * @param
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE );
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics( outMetrics);
        return outMetrics .widthPixels ;
    }
    /**
     * 获取屏幕分辨率的高度
     * 
     * @param
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Context context) {
    	Display mDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
    	return mDisplay.getHeight();
    }
    
    /**
     * 比较两个 yyyy-MM-dd HH:mm:ss 格式的日期字符串时间前后
     * 
     * @param date1
     * @param date2
     * @return true:"date1在date2后" , false:"date1在date2前"
     */
    public static boolean dateComparator2(String date1, String date2) {
        return dateComparator(date1, date2, "yyyy-MM-dd HH:mm:ss");
    }
    
    public static boolean dateComparator(String date1, String date2, String str) {
        DateFormat df = new SimpleDateFormat(str);
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                return false;
            } else if (dt1.getTime() < dt2.getTime()) {
                return true;
            } else {
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
    
    /**
     * 得到几天后的时间
     * 
     * @param d
     * @param day
     * @return
     */
    public static String getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return DateToStr(now.getTime());
    }
    
    /**
     * 日期转换成Java字符串
     * 
     * @param date
     * @return str
     */
    public static String DateToStr(Date date) {
        String str = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            str = format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
    
    /**
     * 日期转换成Java字符串
     * 
     * @param date
     * @return str
     */
    public static String DateToStr1(Date date) {
        String str = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd  HH:mm");
            str = format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
    
    /**
     * 日期转换成Java字符串,获取小时
     * 
     * @param date
     * @return str
     */
    public static String getHH(Date date) {
        String str = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH");
            str = format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
    
    /**
     * 获取当前时间，格式为 :yyyy-MM-dd HH:mm:ss
     * 
     * @return
     */
    public static String getCurrentDate() {
        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(now);
        return date;
    }
    /**
     * 获取当前时间，格式自定义
     *
     * @return
     */
    public static String getCurrentDate(String format) {
        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String date = dateFormat.format(now);
        return date;
    }

    /**
     * 获取当前时间，格式为 :yyyyMMddHHmmss
     * @return
     */
    public static String getCurrentDateForFileName() {
        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = dateFormat.format(now);
        return date;
    }
    /**
     * 字符串转日期
     */
    public static Date StrToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    
    /**
     * 得到当前时间之后的几个小时时间
     * 
     * @param differhour
     * @return
     */
    public static String getCurrentHourAfter(int differhour) {
        long currenttime = new Date().getTime();
        Date dat1 = new Date(currenttime + 1000 * 60 * 60 * differhour);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("HH");
        return format1.format(dat1) + " " + format2.format(dat1) + ":00-" + (isInteger(format2.format(dat1)) + 2)
                + ":00";
    }
    
    /**
     * 得到当前时间之后的几个小时时间
     * 
     * @param differhour 单位是分钟
     * @return
     */
    public static String getCurrentHourbefor(String selecttime, int differhour) {
        long currenttime = SunUtils.StrToDate(selecttime).getTime();
        Date dat1 = new Date(currenttime - 1000  * 60 * differhour);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format1.format(dat1);
    }
    
    public static String getSendTime(String str) {
        Date data = StrToDate(str);
        String time = DateToStr1(data);
        return time + " " + getHH(data) + ":00-" + (SunUtils.isInteger(getHH(data)) + 2) + ":00";
    }
    
    /**
     * 获取YYYYMMDD格式
     * 
     * @return
     */
    public static String getDayshms() {
        SimpleDateFormat sdfDayshms = new SimpleDateFormat("yyyyMMddhhmmss");
        return sdfDayshms.format(new Date());
    }
    
    public static String getRunRandom(){
        return "cx" + getDayshms();
    }
    
    public static String get32UUID() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }
    
    /**
     * 把字符串的金额（元为单位），转成分为单位
     * @return
     */
    public static String getPart(String money){
        double mon = isDouble(money);
        return (mon * 100) + "";
    }
    /**
     * 该方法是用于相同对象不同属性值的合并，如果两个相同对象中同一属性都有值，那么newBean中的值会覆盖oldBean重点的值
     * @param newBean   后获取的对象bean
     * @param oldBean    被合并替换的bean
     * @return oldBean,合并后的对象
     */
    public static UserBean replaceUserBean(UserBean newBean, UserBean oldBean){
        Class sourceBeanClass = newBean.getClass();
        if(oldBean==null){
            oldBean=new UserBean();
        }
        Class targetBeanClass = oldBean.getClass();

        Field[] sourceFields = sourceBeanClass.getDeclaredFields();
        Field[] targetFields = sourceBeanClass.getDeclaredFields();
        for(int i=0; i<sourceFields.length; i++){
            Field sourceField = sourceFields[i];
            Field targetField = targetFields[i];
            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            try {
                if( !(sourceField.get(newBean) == null) &&  !"serialVersionUID".equals(sourceField.getName().toString())){
                    targetField.set(oldBean,sourceField.get(newBean));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return oldBean;
    }

    /**
     * MD5加密
     * @param plaintext
     * @return
     */
    public  static String MD5(String plaintext) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] btInput = plaintext.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 隐藏中间字符
     * @param str 需要隐藏的String
     * @return 中间三个*
     */
    public static String getHideString(String str){
        return str.substring(0,1)+"***"+str.substring(str.length()-1,str.length());
    }


    /**
     * 学历int转文字
     * @param cEdu
     * @return
     */
    public static String getStringEdu (int cEdu){
        String edu = "";
        switch (cEdu){
            case 0:
                edu = "幼儿园";
                break;
            //1 小学,2 初中,3 高中,4 大专,5 本科,6 硕士, 7 博士，8 博士后:
            case 1:
                edu = "小学";
                break;
            case 2:
                edu ="初中";
                break;
            case 3:
                edu ="高中";
                break;
            case 4:
                edu ="大专";
                break;
            case 5:
                edu ="本科";
                break;
            case 6:
                edu ="硕士";
                break;
            case 7:
                edu ="博士";
                break;
            case 8:
                edu ="博士后";
                break;
        }
        return edu;
    }

//    public static int getIntEdu(String strEdu){
//        if(strEdu.equals("小学")){
//            return 1;
//        }else if(strEdu.equals("初中")){
//            return 2;
//        }else if(strEdu.equals("高中")){
//            return 3;
//        }else if(strEdu.equals("大专")){
//            return 4;
//        }else if(strEdu.equals("本科")){
//            return 5;
//        }else if(strEdu.equals("硕士")){
//            return 6;
//        }else if(strEdu.equals("博士")){
//            return 7;
//        }else if(strEdu.equals("博士后")){
//            return 8;
//        }
//        else{
//            return 0;
//        }
//
//    }

    public static String getStringAge(int age){
        String edu = "";
        switch (age){
            case 0:
                edu = "未设置";
                break;
            //0:未设置；1：小于20岁,2：20-30岁，3:30-40岁,4：40-50岁，5：50-60岁,6大于60岁
            case 1:
                edu = "小于20岁";
                break;
            case 2:
                edu ="20-30岁";
                break;
            case 3:
                edu ="30-40岁";
                break;
            case 4:
                edu ="40-50岁";
                break;
            case 5:
                edu ="50-60岁";
                break;
            case 6:
                edu ="大于60岁";
                break;
        }
        return edu;
    }

//    public static int getIntAge(String age){
//        if(age.equals("小于20岁")){
//            return 1;
//        }else if(age.equals("20-30岁")){
//            return 2;
//        }else if(age.equals("30-40岁")){
//            return 3;
//        }else if(age.equals("40-50岁")){
//            return 4;
//        }else if(age.equals("50-60岁")){
//            return 5;
//        }else if(age.equals("大于60岁")){
//            return 6;
//        }else{
//            return 0;
//        }
//
//    }


    /**
     * 计算签名
     * @param entity
     * @return
     */
//    public static String sign(RequestParams entity){
//        List<KeyValue> list=entity.getStringParams();
//        Collections.sort(list, new Comparator<KeyValue>() {
//            @Override
//            public int compare(KeyValue lhs, KeyValue rhs) {
//                return lhs.key.compareToIgnoreCase(rhs.key);
//            }
//        });
//        String str = "";
//        for (int i = 0; i < list.size(); i++) {
//            str += list.get(i).key+list.get(i).getValueStr();
//        }
//        return MD5(str+ Constants.APP_KEY_SECRET);
//    }


}

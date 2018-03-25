package com.marchsoft.organization.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utils {
    private Utils() {

    }

    public static String convertListToString(List<String> list, String separator) {
        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String i : list) {
            sb.append(i).append(separator);
        }
        sb.setLength(sb.length() - separator.length());
        return sb.toString();
    }
    /**
     * 从view 得到图片
     *
     * @param view
     * @return
     */
    // public static Bitmap getBitmapFromView(View view) {
    // view.destroyDrawingCache();
    // view.measure(View.MeasureSpec.makeMeasureSpec(0,
    // View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
    // .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    // view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    // view.setDrawingCacheEnabled(true);
    // Bitmap bitmap = view.getDrawingCache(true);
    // return bitmap;
    // }
    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.TRANSPARENT);
        view.draw(canvas);
        return returnedBitmap;
    }

    public static String inputStream2String(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        int n;
        while ((n = in.read(b)) != -1) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static void callPhone(Context context, String phone) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + Uri.encode(phone)));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(callIntent);
    }

    /**
     * 是否是手机号
     *
     * @param mobile
     * @return
     */
    public static boolean isMobileNO(String mobile) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");

        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    /**
     * 是否是身份证
     *
     * @param idCard
     * @return
     */
    public static boolean isIdCardNo(String idCard) {
        Pattern p = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
        Matcher m = p.matcher(idCard);
        return m.matches();
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static int maxGongYuShu(int a, int b) {
        if (a > b) {
            int temp = a;
            a = b;
            b = temp;
        }
        int n1 = a, n2 = b;
        while (n2 % n1 != 0) {

            int c = n1;
            n1 = n2 % n1;
            n2 = c;
        }
        return n1;
    }

    public static String maskMobile(String mobile) {
        if (mobile == null) {
            return "";
        }
        return mobile.replaceAll("(?<=\\d{3})\\d(?=\\d{4})", "*");
    }

    //判断email格式是否正确
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     *去掉所有html标签内容
     * @param content:html内容
     * @return
     */
    public static String stripHtml(String content) {
        content = content.replaceAll("<p .*?>", "\r\n");// <p>段落替换为换行
        content = content.replaceAll("<br\\s*/?>", "\r\n");// <br><br/>替换为换行
        content = content.replaceAll("\\<.*?>", "");//去掉其它的<>之间的东西
        return content;
    }
    public static String changeHtml(String html){
        String head = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><title></title></head><body>";
        String end = "</body></html>";
        html = head+html+end;
        return html;
    }
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}

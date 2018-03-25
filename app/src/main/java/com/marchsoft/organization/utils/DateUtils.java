package com.marchsoft.organization.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by w on 2016/3/7.
 */
public class DateUtils {
    private static SimpleDateFormat sf = null;
 /*获取系统时间 格式为："yyyy/MM/dd "*/

public static String getCurrentDate() {
         Date d = new Date();
         sf = new SimpleDateFormat("yyyy年MM月dd日");
         return sf.format(d);
      }

    /*时间戳转换成字符窜*/
    public static Date getDateToString(long time) throws ParseException {
            Long d = new Long(time*1000);
            sf = new SimpleDateFormat("yyyy年MM月dd日");
            String format = sf.format(d);
            Date date = sf.parse(format);
            return date;
            }
        /*将字符串转为时间戳*/
        public static long getStringToDate(String time) {
            sf = new SimpleDateFormat("yyyy年MM月dd日");
            Date date = new Date();
            try{
                date = sf.parse(time);
                } catch(ParseException e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
                }
            return (date.getTime())/1000;
            }
}

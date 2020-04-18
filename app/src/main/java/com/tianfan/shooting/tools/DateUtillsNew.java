package com.tianfan.shooting.tools;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-26 10:09
 * @Description 时间工具类
 */
public class DateUtillsNew {
    /**
     * 获取今天
     * @return String
     * */
    public static String getToday(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
    /**
     * 获取昨天
     * @return String
     * */
    public static String getYestoday(){
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        Date time=cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time);
    }
    /**
     * 获取本月开始日期
     * @return String
     * **/
    public static String getMonthStart(){
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date time=cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time)+" 00:00:00";
    }
    /**
     * 获取本月最后一天
     * @return String
     * **/
    public static String getMonthEnd(){
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date time=cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time)+" 23:59:59";
    }
    /**
     * 获取本周的第一天
     * @return String
     * **/
    public static String getWeekStart(){
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH, 0);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        Date time=cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time)+" 00:00:00";
    }
    /**
     * 获取本周的最后一天
     * @return String
     * **/
    public static String getWeekEnd(){
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date time=cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time)+" 23:59:59";
    }
    /**
     * 获取本年的第一天
     * @return String
     * **/
    public static String getYearStart(){
        return new SimpleDateFormat("yyyy").format(new Date())+"-01-01";
    }

    /**
     * 获取本年的最后一天
     * @return String
     * **/
    public static String getYearEnd(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,calendar.getActualMaximum(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date currYearLast = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(currYearLast)+" 23:59:59";
    }

    //Date转字符串
    public static String dateToStrLong(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }
}


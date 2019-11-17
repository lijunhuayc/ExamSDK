package com.ljh.custom.base_library.utils;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Desc: 日期时间工具类
 * Created by ${junhua.li} on 2017/06/14 14:43.
 * Email: lijunhuayc@sina.com
 */
public class DateUtils {
    /**
     * @return 返回 "yyyy-MM-dd HH:mm:ss" 格式的当前时间字符串
     */
    public static final String DATE_FORMATTER_ALL_SIGN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMATTER_DAY_H_M = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMATTER_DAY = "yyyy-MM-dd";
    public static final String DATE_FORMATTER_DAY_CHINA = "yyyy年MM月dd日";

    /**
     * 车酷服务端时间格式化
     *
     * @param date      yyyy-MM-dd HH:mm:ss
     * @param formatStr 指定格式化格式
     * @return
     */
    public static String getDateFormatterForCheku(String date, String formatStr) {
        return getFormatDateTimeStr(parseTimeStr(date), formatStr);
    }

    public static String getCurrentFormatTimeAllSIGN() {
        return getFormatTimeStr(new Date().getTime());
    }

    public static String getFormatTimeStr(long millisecond) {
        return getFormatDateTimeStr(millisecond, DATE_FORMATTER_ALL_SIGN);
    }

    public static String getFormatDateTimeStr(long millisecond, String formatStr) {
        if (millisecond <= 1000) {
            return "未知时间";
        }
        return new SimpleDateFormat(formatStr, Locale.CHINA).format(new Date(millisecond));
    }

    public static long parseTimeStr(String dateTimeStr) {
        return parseTimeStr(dateTimeStr, DATE_FORMATTER_ALL_SIGN);
    }

    public static long parseTimeStr(String dateTimeStr, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr, Locale.CHINA);
        try {
            return sdf.parse(dateTimeStr).getTime();
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getFormatDateStr(long millisecond) {
        return getFormatDateTimeStr(millisecond, DATE_FORMATTER_DAY);
    }

    public static long parseDateStr(String dateTimeStr) {
        return parseDateStr(dateTimeStr, DATE_FORMATTER_DAY);
    }

    public static long parseDateStr(String dateTimeStr, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr, Locale.CHINA);
        try {
            return sdf.parse(dateTimeStr).getTime();
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 获取当天起始时间戳
     *
     * @return
     */
    public static Long getStartTime() {
        Calendar todayStart = Calendar.getInstance(Locale.CHINA);
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime().getTime();
    }

    /**
     * 获取当天结束时间戳
     *
     * @return
     */
    public static Long getEndTime() {
        Calendar todayEnd = Calendar.getInstance(Locale.CHINA);
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime().getTime();
    }

    /**
     * 返回两个日期之间相差多少天。 注意开始日期和结束日期要统一起来。
     *
     * @param startDate 格式"yyyy-MM-dd"
     * @param endDate   格式"yyyy-MM-dd"
     * @return 整数。
     */
    public static int getDiffDays(String startDate, String endDate) {
        long diff = 0;
        SimpleDateFormat ft = new SimpleDateFormat(DATE_FORMATTER_ALL_SIGN, Locale.CHINA);
        try {
            Date sDate = ft.parse(startDate + " 00:00:00");
            Date eDate = ft.parse(endDate + " 00:00:00");
            diff = eDate.getTime() - sDate.getTime();
            diff = diff / 86400000;// 1000*60*60*24;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int) diff;
    }

    /**
     * 获取两个时间串时间的差值，单位为小时
     *
     * @param startTime 开始时间 yyyy-MM-dd HH:mm:ss
     * @param endTime   结束时间 yyyy-MM-dd HH:mm:ss
     * @return 两个时间的差值(秒)
     */
    public static int getDiffHour(String startTime, String endTime) {
        long diff = 0;
        SimpleDateFormat ft = new SimpleDateFormat(DATE_FORMATTER_ALL_SIGN, Locale.CHINA);
        try {
            Date startDate = ft.parse(startTime);
            Date endDate = ft.parse(endTime);
            diff = startDate.getTime() - endDate.getTime();
            diff = diff / (1000 * 60 * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Long.valueOf(diff).intValue();
    }

    /**
     * 返回指定时间加指定小时数后的日期时间。
     * <p/>
     * 格式：yyyy-MM-dd HH:mm:ss
     *
     * @return 时间.
     */
    public static String getDateByAddHour(String datetime, int minute) {
        String returnTime = null;
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat ft = new SimpleDateFormat(DATE_FORMATTER_ALL_SIGN, Locale.CHINA);
        Date date;
        try {
            date = ft.parse(datetime);
            cal.setTime(date);
            cal.add(GregorianCalendar.MINUTE, minute);
            returnTime = ft.format(cal.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnTime;
    }

    /**
     * 获取两个时间串时间的差值，单位为秒
     *
     * @param startTime 开始时间 yyyy-MM-dd HH:mm:ss
     * @param endTime   结束时间 yyyy-MM-dd HH:mm:ss
     * @return 两个时间的差值(秒)
     */
    public static long getDiff(String startTime, String endTime) {
        long diff = 0;
        SimpleDateFormat ft = new SimpleDateFormat(DATE_FORMATTER_ALL_SIGN, Locale.CHINA);
        try {
            Date startDate = ft.parse(startTime);
            Date endDate = ft.parse(endTime);
            diff = startDate.getTime() - endDate.getTime();
            diff = diff / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;
    }

    /**
     * @param time
     * @return
     * @see #secToTime(int) 与此方法功能相同
     */
    public static String sec2Time(int time) {
        if (time < 0) {
            return "00:00";
        } else {
            int totalSeconds = (time / 1000);
            int seconds = totalSeconds % 60;
            int minutes = (totalSeconds / 60) % 60;
            int hours = totalSeconds / 3600;
            return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
        }
    }

    public static String secToTime(int time) {
        String timeStr;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0) {
            return "00:00";
        } else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
            return timeStr;
        }
    }

    public static String unitFormat(int i) {
        if (i >= 0 && i < 10) {
            return "0" + Integer.toString(i);
        } else {
            return "" + i;
        }
    }

    /**
     * 获取系统时间，保存文件以系统时间戳命名
     */
    public static String getRecordDate() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH);
        int day = ca.get(Calendar.DATE);
        int minute = ca.get(Calendar.MINUTE);
        int hour = ca.get(Calendar.HOUR);
        int second = ca.get(Calendar.SECOND);
        String date = "" + year + (month + 1) + day + hour + minute + second;
        return date;
    }

    //================================================================
    // 从原DateUtil中提取过来的方法与变量
    //
    //================================================================
    public static final String YYYYMMDD_HHMM = "yyyy-MM-dd HH:mm";
    public static final String MMDD = "MM-dd";
    public static final String HHMM = "HH:mm";
    public static final String HHMMSS = "HH:mm:ss";
    public static final int WEEKDAYS = 7;
    public static String[] WEEK = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    /**
     * 字符串获取对应的date
     *
     * @param paramString
     * @return
     */
    public static Date stringToDateFormat(String paramString, String params) {
        try {
            // SimpleDateFormat localDate = new SimpleDateFormat(YYYYMMDD);
            SimpleDateFormat localDate = new SimpleDateFormat(params);
            return localDate.parse(paramString);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return null;
    }

    /**
     * 日期变量转成对应的星期字符串
     *
     * @param date
     * @return
     */
    public static String DateToWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayIndex < 1 || dayIndex > WEEKDAYS) {
            return null;
        }

        return WEEK[dayIndex - 1];
    }

    /**
     * 将date转换成string
     */
    public static String dateToStringFormat(Date date, String params) {
        SimpleDateFormat format = new SimpleDateFormat(params);
        return format.format(date);
    }

    /**
     * 根据当前日期获取这一周的日期集合
     */
    public static List<Date> dateToWeekList(Date date) {
        int day = date.getDay();
        Date fdate;
        List<Date> list = new ArrayList<Date>();
        Long fTime = date.getTime() - day * 24 * 3600000;
        for (int a = 1; a <= 7; a++) {
            fdate = new Date();
            fdate.setTime(fTime + (a * 24 * 3600000));
            list.add(a - 1, fdate);
        }
        return list;
    }

    public static List<Date> dateToWeekList2(Date date) {
        List<Date> listDate = new ArrayList<Date>();
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0) {
            dayOfWeek = 0;
        }

        if (0 == dayOfWeek) {
            calendar.add(Calendar.DAY_OF_MONTH, -7);
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, -dayOfWeek);
        }
        for (int i = 0, len = 7; i < len; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            listDate.add(i, calendar.getTime());
        }

        return listDate;
    }

    public static String week(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String week = "星期一";
        switch (dayOfWeek) {
            case 1:
                week = "星期日";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
        }
        return week;
    }

    /**
     * 根据当前日期字符串获得这一周的日期
     *
     * @param paramString
     */
    public static List<Date> stringToWeekList(String paramString) {

        return dateToWeekList2(stringToDateFormat(paramString, DATE_FORMATTER_DAY));
    }

    /**
     * YYYYMMDD转化为MMDD
     *
     * @param paramString
     */
    public static String stringDateFormat(String paramString) {
        return paramString.substring(5);
    }

    /**
     * 截取日期中的年
     *
     * @param paramString
     * @return
     */
    public static String subYearString(String paramString) {
        return paramString.substring(0, 5);
    }

    /**
     * @return (mm-dd)
     */
    public static String subMounthDay(String name, String paramString) {
        StringBuffer sb = new StringBuffer();
        sb.append(name).append("-(").append(stringDateFormat(paramString)).append(")");
        return sb.toString();
    }

    /**
     * 根据当前日期获取本周、前七天
     */
    public static List<Date> sevenDays() {
        List<Date> listDate = new ArrayList<Date>();
        Calendar c = Calendar.getInstance(Locale.CHINA);
        c.setTime(new Date(System.currentTimeMillis()));
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;

        for (int i = 7, len = 0; i > len; i--) {
            // 日期的DATE减去10 就是往后推10 天 同理 +10 就是往后推十天
            c = Calendar.getInstance(Locale.CHINA);
            c.add(Calendar.DATE, -i);
            listDate.add(c.getTime());
        }
        for (int i = 0, len = 8 - dayOfWeek; i <= len; i++) {
            c = Calendar.getInstance(Locale.CHINA);
            c.add(Calendar.DATE, i);
            listDate.add(c.getTime());
        }

        return listDate;
    }

    public static String format(String inFormat, long time) {
        return DateFormat.format(inFormat, time).toString();
    }

    public static String formatTime(String inFormat, long time) {
        return new SimpleDateFormat(inFormat, Locale.CHINA).format(time);
    }

    /**
     * 时间转换（ms->hh:mm:ss）
     *
     * @param time (ms)
     * @return hh:mm:ss or mm:ss
     */
    public static String generateTime(int time) {
        int totalSeconds = (int) (time / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds).toString();
        }
    }

    /**
     * 时间转换（ms->hh:mm:ss）
     *
     * @param totalSeconds (ms)
     * @return hh:mm:ss or mm:ss
     */
    public static String generateTime1(int totalSeconds) {
        // int totalSeconds = (int) (time / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return hours + "小时" + minutes + "分" + seconds + "秒";
        } else if (minutes > 0) {
            return minutes + "分" + seconds + "秒";
        } else {
            return seconds + "秒";
        }
    }

    /**
     * 获取当前日期的最后时间
     *
     * @param date YYYY-MM-DD hh:mm:ss
     * @return
     */
    public static String getDateLastTime(String date) {

        try {
            String ymdString = date.substring(0, 10);
            String hmsString = "23:59:59";
            String dateString = ymdString + " " + hmsString;
            return dateString;
        } catch (Exception e) {
            return null;
        }
    }

    /*public static long getCurrentTime(){
    }*/

    /**
     * 获取今天以前的第N天的最后一毫秒时间
     *
     * @param days 正数表示今天前的第几天,负数表示今天后的第几天,0表示今天
     * @return
     */
    public static long getTodayBeforDayLastMilliSecond(int days) {
        Calendar instance = Calendar.getInstance();
        if (days != 0) {
            long time = instance.getTimeInMillis();
            long diff = days * 24 * 60 * 60 * 1000;
            instance.setTimeInMillis(time - diff);
        }
        instance.set(Calendar.HOUR_OF_DAY, 23);
        instance.set(Calendar.MINUTE, 59);
        instance.set(Calendar.SECOND, 59);
        instance.set(Calendar.MILLISECOND, 999);
        return instance.getTimeInMillis();
    }

    /**
     * 获取今天的最后一毫秒时间
     *
     * @return //23:59:59.999 今天的最后一毫秒
     */
    public static long getTodayLastMilliSecond() {
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.HOUR_OF_DAY, 23);
        instance.set(Calendar.MINUTE, 59);
        instance.set(Calendar.SECOND, 59);
        instance.set(Calendar.MILLISECOND, 999);
        return instance.getTimeInMillis();
    }

    /**
     * 将已经格式化的时间重新格式化为新的格式
     *
     * @param time    已格式化好多时间字符串
     * @param bformat 之前的格式
     * @param afromat 转换后的格式
     * @return 新的格式化时间字符串
     */
    public static String transTimeFromat(String time, String bformat, String afromat) {
        long _time = time2Long(time, bformat);
        return format(afromat, _time);
    }

    /**
     * 将格式化时间转为时间戳
     */
    public static long time2Long(String time, String format) {
        Date date = stringToDateFormat(time, format);
        if (date == null) {
            return 0;
        }
        return date.getTime();
    }

    public static String getCurrentYear() {
        Calendar data = Calendar.getInstance();
        return String.valueOf(data.get(Calendar.YEAR));
    }

    public static int checkTimer(long start, long len) {
        long now = Calendar.getInstance().getTime().getTime();
        if (now >= start && now <= (start + len)) {
            return 0;
        }
        if (now < start) {
            return -1;
        }
        if (now > (start + len)) {
            return 1;
        }
        return -2;
    }

    //比较时间
    public static Long[] compare_date(String lastTime, String currentTime) {
        java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            Date dt1 = df.parse(lastTime);
            Date dt2 = df.parse(currentTime);
            long LastTimelong = dt1.getTime();
            long currentTimeLong = dt2.getTime();
            long diff;
            diff = currentTimeLong - LastTimelong;
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            return new Long[]{day, hour, min, sec};
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * 获取两个日期之间的间隔天数
     *
     * @return 天数之差
     */
    public static int getGapCount(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

    //获取前天，昨天等
    public static Date getNextDay(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, num);
        date = calendar.getTime();
        return date;
    }
}

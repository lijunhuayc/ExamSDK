package com.ljh.custom.base_library.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Desc: 普通工具类
 * Created by ${junhua.li} on 2016/05/13 15:30.
 * Email: lijunhuayc@sina.com
 */
public class CommonUtils {
    public static final String TIME_FORMAT_SERVER = "yyyy-MM-dd HH:mm:ss";  //接口时间格式1  "2018-04-04 02:50:05"

    public static SimpleDateFormat getSimpleDateFormat(String pattern) {
        return new SimpleDateFormat(pattern, Locale.CHINA);
    }

    /**
     * @param timeStr      接口时间字符串
     * @param targetFormat 目标时间格式
     * @return 转换后的时间字符串
     */
    public static String convertTime(String timeStr, String targetFormat) {
        return convertTime(timeStr, TIME_FORMAT_SERVER, targetFormat);
    }

    public static String convertTime(String timeStr, String sourceFormat, String targetFormat) {
        Date date;
        try {
            date = getSimpleDateFormat(sourceFormat).parse(timeStr);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return getSimpleDateFormat(targetFormat).format(date);
    }

//    /**
//     * @param view
//     * @param context
//     * @author ljh @desc View抖动动画【左右】
//     */
//    public static void shakeView(View view, Context context) {
//        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
//        view.startAnimation(shake);
//    }

    public static String cent2YuanStr(String moneyCent) {
        long cent;
        try {
            cent = Long.parseLong(moneyCent);
        } catch (Exception e) {
            cent = 0;
        }
        return cent2YuanStr(cent);
    }

    /**
     * 分为单位的钱转换为元为单位的字符串
     *
     * @param moneyCent long型金额值
     * @return 返回字符串型金额值
     */
    public static String cent2YuanStr(long moneyCent) {
        if (moneyCent <= 0) {
            Timber.d("Param moneyCent must be greater than zero.");
            return "0";
        }
        String s = "";
        DecimalFormat df = new DecimalFormat("0.00");
        s += df.format(((double) moneyCent) / 100);
        return s;
    }

    /**
     * 分为单位的钱转换为万元为单位的字符串
     *
     * @param moneyCent long型金额值
     * @return 返回字符串型金额值
     */
    public static String cent2WanYuanStr(long moneyCent) {
        if (moneyCent <= 0) {
            Timber.d("Param moneyCent must be greater than zero.");
            return "0";
        }
        String s = "";
        DecimalFormat df = new DecimalFormat("0.00");
        s += df.format(((double) moneyCent) / (100 * 10000));
        return s;
    }

    /**
     * 元为单位的字符串转换为分的钱
     *
     * @param moneyYuan
     * @return
     */
    public static long yuanStr2Cent(CharSequence moneyYuan) {
        try {
            double priceTotal = Double.parseDouble(moneyYuan.toString());    //输入的金额(元) [两位小数]
            return (long) (priceTotal * 100);//数据通道金额单位为分, UI交互显示时金额单位为元
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 保留两位小数(末位非0)并按货币格式转换(452,414.25)
     *
     * @param money
     * @return
     */
    public static String convertMoneyKeepTwoDecimals(double money) {
        DecimalFormat bothFormat = new DecimalFormat("#0.##");
        NumberFormat nonzeroFormat = DecimalFormat.getInstance(Locale.CHINA);
        return nonzeroFormat.format(Float.parseFloat(bothFormat.format(money)));
    }

    /**
     * 防重复点击
     */
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 350) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    private static int clickNum = 0;//点击次数

    /**
     * 多次点击
     *
     * @return boolean
     */
    public static synchronized boolean isFastMultiClick(int totalNum) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        Timber.d("diff = %s", timeD);
        if (0 < timeD && timeD < 300) {
            lastClickTime = time;
            clickNum++;
            Timber.d("clickNum = %s, times = %s", clickNum, totalNum);
            if (clickNum == totalNum - 1) {
                return true;
            } else {
                return false;
            }
        }
        lastClickTime = time;
        clickNum = 0;
        return false;
    }

    public static String createUUid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

//    /**
//     * 获取完整类名
//     *
//     * @param context
//     * @return
//     */
//    public static String getRunActivityName(Context context) {
//        return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1).get(0).topActivity.getClassName();
//    }
//
//    public static String getRunPackageName(Context context) {
//        return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1).get(0).topActivity.getPackageName();
//    }
}

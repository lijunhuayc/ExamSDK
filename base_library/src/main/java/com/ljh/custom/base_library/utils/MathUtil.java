package com.ljh.custom.base_library.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathUtil {
    /**
     * @param d
     * @return 格式化double数据只保留小数点后两位
     */
    public static double formatDouble(double d) {
        BigDecimal bg = new BigDecimal(d);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * @param yuan
     * @return 金额单位 元转换为分
     */
    public static long convertYuan2Fen(double yuan) {
        return Double.valueOf(formatDouble(yuan) * 100).longValue();
    }

    /**
     * @param wan
     * @return 金额单位 万转换为分
     */
    public static long convertWan2Fen(double wan) {
        return Double.valueOf(formatDouble(wan) * 100 * 10000).longValue();
    }

    /**
     * @param fen
     * @return 金额单位 分转换为元
     */
    public static double convertFen2Yuan(long fen) {
        return formatDouble(fen / 100D);
    }

    /**
     * @param fen
     * @return 金额单位 分转换为万
     */
    public static double convertFen2Wan(long fen) {
        return formatDouble(fen / (100D * 10000D));
    }

    /**
     * @param amount 金额（单位元）
     * @return 格式化金额数据
     */
    public static CharSequence formatMoney(double amount) {
        return formatMoney(amount, true);
    }

    public static CharSequence getMoneyString(double amount) {
        //DecimalFormat df = new DecimalFormat("0.00");
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(0);
        df.setGroupingUsed(false);
        df.setRoundingMode(RoundingMode.HALF_UP);
        return new StringBuilder()
                .append(df.format(amount));
    }

    /**
     * @param amount 金额（单位元）
     * @return 格式化金额数据
     */
    public static CharSequence formatMoney(double amount, boolean withUnit) {
        DecimalFormat df = new DecimalFormat("#,##0.00 ");
      /*  DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setGroupingSize(3);
        df.setRoundingMode(RoundingMode.HALF_UP);*/
        if (withUnit) {
            return new StringBuilder()
                    .append(df.format(amount))
                    .append("元");
        } else {
            return new StringBuilder()
                    .append(df.format(amount));
        }
    }

    public static int getIntegerNumber(CharSequence s) {
        int i = 0;
        if (!TextUtils.isEmpty(s)) {
            String a = s.toString();
            if (a.contains(",")) {
                a = a.replaceAll(",", "");
            }
            i = Integer.valueOf(a);
        }
        return i;
    }

    public static double getNumber(CharSequence s) {
        double d = 0D;
        if (!TextUtils.isEmpty(s)) {
            String a = s.toString();
            if (a.contains(",")) {
                a = a.replaceAll(",", "");
            }
            if (a.startsWith(".")) {
                a = "0" + a;
            }
            if (a.endsWith(".")) {
                a = a + "0";
            }
            d = Double.valueOf(a);
        }
        return d;
    }

    public static String getOrderNumberFormatString(int number) {
        if (number <= 0) {
            return null;
        }
        if (number > 999) {
            return "(999+)";
        }
        return new StringBuilder("(")
                .append(number)
                .append(")").toString();
    }
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
}

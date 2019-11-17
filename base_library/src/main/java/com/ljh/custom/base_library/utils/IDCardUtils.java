package com.ljh.custom.base_library.utils;

import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import java.util.Calendar;
import java.util.regex.Pattern;

import static com.ljh.custom.base_library.utils.RegexpUtils.CELLPHONE_NO_REGEXP;

/**
 * Desc: 身份证验证的工具（支持15位或18位身份证）
 * Created by ${junhua.li} on 2016/03/28 17:12.
 * Email: lijunhuayc@sina.com
 */
public class IDCardUtils {
    private static final String TAG = IDCardUtils.class.getSimpleName();
    private final static SparseArray<String> zoneNum = new SparseArray<>();

    static {
        zoneNum.put(11, "北京");
        zoneNum.put(12, "天津");
        zoneNum.put(13, "河北");
        zoneNum.put(14, "山西");
        zoneNum.put(15, "内蒙古");
        zoneNum.put(21, "辽宁");
        zoneNum.put(22, "吉林");
        zoneNum.put(23, "黑龙江");
        zoneNum.put(31, "上海");
        zoneNum.put(32, "江苏");
        zoneNum.put(33, "浙江");
        zoneNum.put(34, "安徽");
        zoneNum.put(35, "福建");
        zoneNum.put(36, "江西");
        zoneNum.put(37, "山东");
        zoneNum.put(41, "河南");
        zoneNum.put(42, "湖北");
        zoneNum.put(43, "湖南");
        zoneNum.put(44, "广东");
        zoneNum.put(45, "广西");
        zoneNum.put(46, "海南");
        zoneNum.put(50, "重庆");
        zoneNum.put(51, "四川");
        zoneNum.put(52, "贵州");
        zoneNum.put(53, "云南");
        zoneNum.put(54, "西藏");
        zoneNum.put(61, "陕西");
        zoneNum.put(62, "甘肃");
        zoneNum.put(63, "青海");
        zoneNum.put(64, "宁夏");
        zoneNum.put(65, "新疆");
        zoneNum.put(71, "台湾");
        zoneNum.put(81, "香港");
        zoneNum.put(82, "澳门");
        zoneNum.put(91, "外国");
    }

    private final static char[] PARITYBIT = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
    private final static int[] Wi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};

    /**
     * 纯数字字符串 验证
     *
     * @param digitalStr
     * @return
     */
    public static boolean isDigital(String digitalStr) {
        if (digitalStr == null) {
            Log.d(TAG, "数字字符串获取异常，null");
            return false;
        }
        if (Pattern.compile(RegexpUtils.DIGITAL_REGEXP).matcher(digitalStr).find()) {
            return true;
        }
        Log.d(TAG, "数字字符串格式异常");
        return false;
    }

    /**
     * 金额 验证
     *
     * @param digitalStr
     * @return
     */
    public static boolean isMoney(String digitalStr) {
        if (digitalStr == null) {
            Log.d(TAG, "金额字符串获取异常，null");
            return false;
        }
        if (Pattern.compile(RegexpUtils.MONEY_REGEXP_PLUS).matcher(digitalStr).find()) {
            return true;
        }
        Log.d(TAG, "金额字符串格式异常");
        return false;
    }

    /**
     * 邮箱 验证
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null) {
            Log.d(TAG, "金额字符串获取异常，null");
            return false;
        }
        if (Pattern.compile(RegexpUtils.EMAIL_REGEXP).matcher(email).find()) {
            return true;
        }
        Log.d(TAG, "金额字符串格式异常");
        return false;
    }

    /**
     * 电话号码验证
     *
     * @param phoneStr
     * @return
     */
    public static boolean isPhoneNo(String phoneStr) {
        if (phoneStr == null) {
            Log.e(TAG, "The phone number can't be empty");
            return false;
        }
        if (Pattern.compile(CELLPHONE_NO_REGEXP).matcher(phoneStr).find() ||
                Pattern.compile(RegexpUtils.TELEPHONE_NO_REGEXP).matcher(phoneStr).find()) {
            return true;
        }
        Log.e(TAG, "Incorrect phone number format.");
        return false;
    }

    /**
     * 手机号验证
     *
     * @param phoneStr Cell Phone NO.
     * @return boolean
     */
    public static boolean isCellPhoneNo(CharSequence phoneStr) {
        if (TextUtils.isEmpty(phoneStr)) {
            Log.e(TAG, "The cell-phone number can't be empty");
            return false;
        }
        if (Pattern.compile(CELLPHONE_NO_REGEXP).matcher(phoneStr).find()) {
            return true;
        }
        Log.e(TAG, "Incorrect cell-phone number format.");
        return false;
    }

    /**
     * 验车是否是一个电话号码（手机号、座机号、400号）
     *
     * @param phone
     * @return
     */
    public static boolean isTel(String phone) {
        if (phone == null || phone.length() == 0)
            return false;
        if (phone.matches(CELLPHONE_NO_REGEXP)) {//连续的11位数字,以1开头
            return true;
        } else if (phone.matches("\\d{4}-\\d{8}|\\d{4}-\\d{7}|\\d(3)-\\d(8)")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 身份证号、姓名 验证
     *
     * @param nameStr
     * @return
     */
    public static boolean isChineseName(String nameStr) {
        if (nameStr == null) {
            Log.d(TAG, "身份证号或者姓名获取异常，null");
            return false;
        }
        if (Pattern.compile(RegexpUtils.XM_REGEXP).matcher(nameStr).find()) {
            return true;
        }
        Log.d(TAG, "姓名格式错误");
        return false;
    }

    /**
     * 身份证号 验证
     *
     * @param idCardStr 号码或者姓名字符串
     * @return 是否有效 null和"" 都是false
     */
    public static boolean isIdCard(String idCardStr) {
        Log.d(TAG, "即将验证的身份证号码：" + idCardStr);
        if(TextUtils.isEmpty(idCardStr)){
            return false;
        }
        //15或者18位时，判断身份证格式
        if (idCardStr.length() != 15 && idCardStr.length() != 18) {
            Log.d(TAG, "身份证号位数不对(可能是姓名)");
            return false;
        }

        if (!TextUtils.isDigitsOnly(idCardStr.substring(0, idCardStr.length() - 1))) {
            return false;
        }
        if (zoneNum.indexOfKey(Integer.valueOf(idCardStr.substring(0, 2))) < 0) {    //校验区位码
            Log.d(TAG, "身份证号（前两位）区位码不对");
            return false;
        }

        final int iyear = Integer.parseInt(idCardStr.length() == 15 ? "19" + idCardStr.substring(6, 8) : idCardStr.substring(6, 10));
        final int imonth = Integer.parseInt(idCardStr.length() == 15 ? idCardStr.substring(8, 10) : idCardStr.substring(10, 12));
        final int iday = Integer.parseInt(idCardStr.length() == 15 ? idCardStr.substring(10, 12) : idCardStr.substring(12, 14));
        if (!validateData(iyear, imonth, iday)) {        //验证日期合法，1900年以后
            Log.d(TAG, "身份证号生日位（7-12/7-14位）不合法");
            return false;
        }
        if (idCardStr.length() == 15) {
            return true;
        }

        idCardStr = idCardStr.toUpperCase();    //转换大写
        String Ai = idCardStr.substring(0, idCardStr.length() - 1);    //去掉末尾
        final char[] aiChars = Ai.toCharArray();
        int power = 0;
        for (int i = 0; i < aiChars.length; i++) {
            if (aiChars[i] < '0' || aiChars[i] > '9') {
                Log.d(TAG, "身份证号（尾号除外）包含（非0-9的）无效字符");
                return false;
            } else {
                power += (aiChars[i] - '0') * Wi[i];
            }
        }

        return idCardStr.charAt(idCardStr.length() - 1) == PARITYBIT[power % 11];
    }

    /**
     * 验证日期合法性
     *
     * @param iyear
     * @param imonth
     * @param iday
     * @return
     */
    private static boolean validateData(int iyear, int imonth, int iday) {
        int[] bigMonth = {1, 3, 5, 7, 8, 10, 12};
        int[] smallMonth = {4, 6, 9, 11};

        if (iyear < 1900 || iyear > Calendar.getInstance().get(Calendar.YEAR)) {    //1900年前的PASS，超过今年的PASS
            Log.d(TAG, "IDCardUtil-validate: year is error!");
            return false;
        }
        if (imonth < 1 || imonth > 12) {
            Log.d(TAG, "IDCardUtil-validate: month is > 12 or < 1 !");
            return false;
        }
        if (imonth == 2) {
            if ((iyear % 4 == 0 && iyear % 100 != 0) || iyear % 400 == 0) {    //闰年
                if (iday < 1 || iday > 29) {
                    Log.d(TAG, "IDCardUtil-validate: 闰年2月日期错误！");
                    return false;
                }
            } else {
                if (iday < 1 || iday > 28) {
                    Log.d(TAG, "IDCardUtil-validate: 非闰年2月日期错误！");
                    return false;
                }
            }
        }
        for (int i : bigMonth) {        //大月
            if (imonth == i) {
                if (iday < 1 || iday > 31) {
                    Log.d(TAG, "IDCardUtil-validate: 大月日期错误！");
                    return false;
                }
            }
        }
        for (int i : smallMonth) {        //小月
            if (imonth == i) {
                if (iday < 1 || iday > 30) {
                    Log.d(TAG, "IDCardUtil-validate: 小月日期错误！");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 身份证转换, 15-->18, 若是18位则直接返回
     *
     * @param idCardStr
     * @return idCardStr.length == 18
     */
    public static String idCardConvertTo18(String idCardStr) {
        idCardStr = idCardStr.trim();
        if (idCardStr.length() == 15 && isIdCard(idCardStr)) {
            idCardStr = idCardStr.substring(0, 6) + "19" + idCardStr.substring(6, idCardStr.length());
            final char[] aiChars = idCardStr.toCharArray();        //讲17位String转为char[]
            int power = 0;
            for (int i = 0; i < aiChars.length; i++) {
                if (aiChars[i] < '0' || aiChars[i] > '9') {
                    Log.d(TAG, "身份证号（尾号除外）包含（非0-9的）无效字符");
                } else {
                    power += (aiChars[i] - '0') * Wi[i];
                }
            }
            idCardStr += PARITYBIT[power % 11];        //加上校验码
            Log.d(TAG, "15位转换18位结果：" + idCardStr);
        } else {
            Log.d(TAG, "不是正确的身份证号码，不能转换：" + idCardStr);
        }
        return idCardStr;
    }

    /**
     * 18-->15位身份证转换[暂时不使用了]
     *
     * @param idCardStr
     * @return idCardStr.length == 15
     */
    public static String idCardConvertTo15(String idCardStr) {
        idCardStr = idCardStr.trim();
        if (isIdCard(idCardStr) && idCardStr.length() == 18) {
            idCardStr = idCardStr.substring(0, idCardStr.length() - 1);
            idCardStr = idCardStr.replace(idCardStr.substring(6, 8), "");
            Log.d(TAG, "18位转换15位结果：" + idCardStr);
        } else {
            Log.d(TAG, "不是正确的身份证号码，不能转换到旧式号码：" + idCardStr);
        }
        return idCardStr;
    }

//	public static void main(String[] args) {
//		for(int i=0;i<10;i++){
//			final String s = "35068119860212101"+i;  
//			System.out.println(s+" --> "+isIdCard(null, s));		
//		}
//		final String s = "350681860212101";  
//		System.out.println(s+" --> "+idCardConvert(null, s));
//	}
}

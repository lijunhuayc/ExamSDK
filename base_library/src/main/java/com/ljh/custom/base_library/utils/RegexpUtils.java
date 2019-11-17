package com.ljh.custom.base_library.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Desc: 正则表达式规则类
 * Created by ${junhua.li} on 2016/03/28 17:12.
 * Email: lijunhuayc@sina.com
 */
public class RegexpUtils {
    /**
     * 姓名 验证正则表达式 (2-16个汉字)
     */
    public static final String XM_REGEXP = "^[\\u4e00-\\u9fa5]{2,16}$";//限定长度的汉字
    public static final String Chinese_REGEXP = "[^\\u4E00-\\u9FA5]";//汉字
    public static final String LETTER_Chinese_FIGURE_REGEXP = "[^a-zA-Z0-9\\u4E00-\\u9FA5]";//字母数字和汉字
    /**
     * 生日 验证正则表达式 (2-4个汉字)
     */
    public static final String BIRTHDAY_REGEXP = "^\\d{8}$";
    /**
     * 车牌号验证 正则表达式
     */
    public static final String CP_NO_REGEXP = "^[0-9|a-z|A-Z]{4,5}$";
    /**
     * 手机号验证 正则表达式
     */
    public static final String CELLPHONE_NO_REGEXP = "^1\\d{10}$";
    /**
     * 座机号验证 正则表达式
     */
    public static final String TELEPHONE_NO_REGEXP = "^(0[0-9]{2,3}\\-*)?([2-9][0-9]{6,7})(\\-[0-9]{1,4})?$";
    /**
     * 数字验证 正则表达式
     */
    public static final String DIGITAL_REGEXP = "^[\\d]*$";
    public static final String MONEY_REGEXP_PLUS = "^[0-9]+(\\.([0-9]{2}|[0-9]{1}))?$";
    /**
     * 邮箱验证 正则表达式
     */
    public static final String EMAIL_REGEXP = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";

    /**
     * Desc: 正则过滤汉字
     *
     * @param str
     */
    public static String filterChinese(String str) {
        Pattern p = Pattern.compile(RegexpUtils.Chinese_REGEXP);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

}

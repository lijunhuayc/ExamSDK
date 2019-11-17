package com.ljh.custom.base_library.widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.ljh.custom.base_library.utils.MathUtil;

import java.lang.reflect.Field;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Desc:千分位输入框
 * Created by JiaKun.Yang
 * Date: 2018/05/07 10:22
 */
public class ThousandthEditText extends android.support.v7.widget.AppCompatEditText {
    public ThousandthEditText(Context context) {
        super(context);
        init();
    }

    public ThousandthEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ThousandthEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private TextWatcher textWatcher;
    private double maxAmount = 99999999;
    private boolean forceMax = false;

    public void setForceMax(boolean pForceMax) {
        forceMax = pForceMax;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    private void init() {
        setLongClickable(false);
        setTextIsSelectable(false);
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        setSingleLine(true);
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        setText(s);
                        setSelection(s.length());
                    }
                }
                if (s.toString().trim().equals(".")) {
                    s = "0" + s;
                    setText(s);
                    setSelection(s.length());
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        String s1 = s.subSequence(0, 1).toString();
                        setText(s1);
                        setSelection(s1.length());
                    }
                }
                int selection = getSelectionStart();
                String str = s.toString();
                String tail = "";
                String main = str;
                if (str.contains(".")) {
                    int index = str.lastIndexOf(".");
                    tail = str.substring(index, str.length());
                    main = str.substring(0, index);
                }
                int commaCount = 0;
                int dealCommaCount = 0;
                int actualSelection = selection;
                String s1 = format(main).toString();
                if (!main.equals(s1)) {
                    if (main.contains(",")) {
                        for (int i = 0; i < main.length(); i++) {
                            char singleItem = main.charAt(i);
                            if (',' == singleItem) {
                                commaCount++;
                                if ((i + 1) > selection) {
                                    commaCount--;
                                    break;
                                }
                            }
                        }
                        actualSelection = selection - commaCount;
                    }
                    for (int i = 0; i < s1.length(); i++) {
                        char singleItem = s1.charAt(i);
                        if (',' == singleItem) {
                            dealCommaCount++;
                            if ((i + 1) - dealCommaCount > actualSelection) {
                                dealCommaCount--;
                                break;
                            }
                        }
                    }
                    actualSelection += dealCommaCount;
                    setText(new StringBuilder(s1).append(tail));
                    if (actualSelection < 0) {
                        actualSelection = 0;
                    }
                    setSelection(actualSelection > getText().length() ? getText().length() : actualSelection);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        removeTextChangedListener(textWatcher);
        addTextChangedListener(textWatcher);
    }

    int length = 0;

    public int getMaxLength() {
        if (length == 0) {
            try {
                InputFilter[] inputFilters = getFilters();
                for (InputFilter filter : inputFilters) {
                    Class<?> c = filter.getClass();
                    if (c.getName().equals("android.text.InputFilter$LengthFilter")) {
                        Field[] f = c.getDeclaredFields();
                        for (Field field : f) {
                            if (field.getName().equals("mMax")) {
                                field.setAccessible(true);
                                length = (Integer) field.get(filter);
                            }
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (length == 0) {
            length = 20;
        }
        return length;
    }

    DecimalFormat df;

    {
        df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setGroupingSize(3);
        df.setRoundingMode(RoundingMode.HALF_UP);
    }

    /**
     * @param d 用于double格式化去掉科学计数法
     */
    public void setText(double d) {
        setText(d, false);
    }

    /**
     * @param d
     * @param isInt 是否展示Int型
     */
    public void setText(double d, boolean isInt) {
        isInt = isInt || d == Double.valueOf(d).intValue();
        //如果展示int型，小数点保留0位;如果展示double型，小数点保留2位
        df.setMaximumFractionDigits(isInt ? 0 : 2);
        setText(df.format(d));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                int selection = getSelectionStart() - 1;
                if (!TextUtils.isEmpty(getText()) && selection < getText().length() && selection >= 0) {
                    char a = getText().charAt(selection);
                    if (',' == a) {
                        setSelection(selection);
                        return super.onKeyDown(keyCode, event);
                    }
                }
            }
        } else if (isNumberKeyCode(keyCode)) {
            if (MathUtil.getNumber(getText() + getKeyCodeText(keyCode)) >= maxAmount) {
                if (forceMax) {
                    setText(maxAmount);
                    setSelection(length());
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private String getKeyCodeText(int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
                return "0";
            case KeyEvent.KEYCODE_1:
                return "1";
            case KeyEvent.KEYCODE_2:
                return "2";
            case KeyEvent.KEYCODE_3:
                return "3";
            case KeyEvent.KEYCODE_4:
                return "4";
            case KeyEvent.KEYCODE_5:
                return "5";
            case KeyEvent.KEYCODE_6:
                return "6";
            case KeyEvent.KEYCODE_7:
                return "7";
            case KeyEvent.KEYCODE_8:
                return "8";
            case KeyEvent.KEYCODE_9:
                return "9";
            default:
                return "";
        }
    }

    private boolean isNumberKeyCode(int keyCode) {
        return keyCode == KeyEvent.KEYCODE_0
                || keyCode == KeyEvent.KEYCODE_1
                || keyCode == KeyEvent.KEYCODE_2
                || keyCode == KeyEvent.KEYCODE_3
                || keyCode == KeyEvent.KEYCODE_4
                || keyCode == KeyEvent.KEYCODE_5
                || keyCode == KeyEvent.KEYCODE_6
                || keyCode == KeyEvent.KEYCODE_7
                || keyCode == KeyEvent.KEYCODE_8
                || keyCode == KeyEvent.KEYCODE_9;
    }

    private CharSequence format(CharSequence s) {
        if (TextUtils.isEmpty(s)) {
            return "";
        }
        s = s.toString().replaceAll(",", "");
        double a = Double.valueOf(s.toString());
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setGroupingSize(3);
        df.setRoundingMode(RoundingMode.HALF_UP);
        String temp = df.format(a);
        if (temp.length() > getMaxLength()) {
            return format(temp.substring(0, temp.length() - 1));
        }
        return temp;
    }

    public double getNumber() {
        double d = 0D;
        if (!TextUtils.isEmpty(getText())) {
            String a = getText().toString();
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
}

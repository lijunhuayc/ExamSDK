package com.ljh.custom.base_library.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Desc: 通用K-V对象
 * Created by Junhua.Li
 * Date: 2018/03/27 14:57
 */
public class KeyValueModel implements Parcelable {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "KeyValueModel{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.value);
    }

    public KeyValueModel() {
    }

    public KeyValueModel(String key, String value) {
        this.key = key;
        this.value = value;
    }

    protected KeyValueModel(Parcel in) {
        this.key = in.readString();
        this.value = in.readString();
    }

    public static final Creator<KeyValueModel> CREATOR = new Creator<KeyValueModel>() {
        @Override
        public KeyValueModel createFromParcel(Parcel source) {
            return new KeyValueModel(source);
        }

        @Override
        public KeyValueModel[] newArray(int size) {
            return new KeyValueModel[size];
        }
    };
}

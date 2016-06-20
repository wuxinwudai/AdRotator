package com.wuxinwudai.ar;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * AdInfo 类存放广告信息
 * @author 吾心无待 于2016年04月01日
 */
public class AdInfo implements Parcelable {
    private String title;
    private String imageUri;
    private View.OnClickListener onClickListener;

    /**
     * 获取广告标题
     * @return 广告标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置广告标题
     * @param title 广告标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取图片地址
     * @return
     */
    public String getImageUri() {
        return imageUri;
    }

    /**
     * 设置图片地址
     * @param imageUri 图片地址
     */
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    /**
     * 获取广告单击事件
     * @return 单击事件监听器
     */
    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    /**
     * 设置广告单击事件
     * @param onClickListener 单击事件监听器
     */
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.imageUri);
    }

    public AdInfo() {
    }

    /**
     * 工厂方法创建 AdInfo 类的一个新实例
     * @param title 标题
     * @param imageUri 图片地址
     * @param listener 单击事件监听器
     * @return
     */
    public static AdInfo create(String title,String imageUri,View.OnClickListener listener){
        AdInfo ad = new AdInfo();
        ad.setImageUri(imageUri);
        ad.setTitle(title);
        ad.setOnClickListener(listener);
        return ad;
    }

    protected AdInfo(Parcel in) {
        this.title = in.readString();
        this.imageUri = in.readString();
    }

    public static final Parcelable.Creator<AdInfo> CREATOR = new Parcelable.Creator<AdInfo>() {
        @Override
        public AdInfo createFromParcel(Parcel source) {
            return new AdInfo(source);
        }

        @Override
        public AdInfo[] newArray(int size) {
            return new AdInfo[size];
        }
    };
}

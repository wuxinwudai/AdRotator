package com.wuxinwudai.ar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;

import com.bumptech.glide.Glide;
import com.wuxinwudai.vpi.ViewPagerIndicator;

/**
 * AdRotator 类为广告轮播插件
 * @author 吾心无待 于2016年03月31日
 */
public class AdRotator extends FrameLayout {
    private final ViewPager mViewPager;//ViewPager
    private final ViewPagerIndicator mIndicator;//分页指示器
    private String[] mImageUris;//显示的广告图片
    private OnClickListener[] mListeners;//单击事件监听器
    /**
     * 构造函数，初始化 AdRotator 类的一个新实例
     * @param context 上下文对象
     */
    public AdRotator(Context context) {
        this(context, null);
    }

    /**
     * 构造函数，初始化 AdRotator 类的一个新实例
     * @param context 上下文对象
     * @param attrs 属性列表
     */
    public AdRotator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造函数，初始化 AdRotator 类的一个新实例
     * @param context 上下文对象
     * @param attrs 属性列表
     * @param defStyleAttr 资源ID
     */
    public AdRotator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mViewPager = new ViewPager(context);
        mIndicator = new ViewPagerIndicator(context);
        mViewPager.addOnPageChangeListener(mIndicator);//设置滚动页面监听
    }

    @Override
    protected void onAttachedToWindow() {
        mViewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,30);//设置距离底部30像素
        params.gravity = Gravity.BOTTOM;
        mIndicator.setLayoutParams(params);
        this.addView(mViewPager);
        this.addView(mIndicator);
        setViewPagerAdapter();
        mIndicator.notifyViewPager(mViewPager);//设置单击事件回调
        mIndicator.setAutoPlay(true);//自动轮播
        super.onAttachedToWindow();
    }

    /**
     * 获取广告图片资源列表
     * @return 广告图片资源列表
     */
    public String[] getImageUris() {
        return mImageUris;
    }

    /**
     * 设置广告图片资源列表
     * @param mImageUris 广告图片列表
     */
    public void setImageUris(String[] mImageUris) {
        this.mImageUris = mImageUris;
        setViewPagerAdapter();
    }

    /**
     * 获取广告对应的单击事件监听器列表
     * @return 单击事件监听器列表
     */
    public OnClickListener[] getListeners() {
        return mListeners;
    }

    /**
     * 设置广告图片对应的单击事件监听器列表
     * @param mListeners 单击事件监听器列表
     */
    public void setListeners(OnClickListener[] mListeners) {
        this.mListeners = mListeners;
    }

    private void setViewPagerAdapter(){
        if (mImageUris != null) {
            mViewPager.setAdapter(new PagerAdapter() {
                @Override
                public int getCount() {
                    return mImageUris.length;
                }

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view == object;
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    ImageView iv = new ImageView(getContext());
                    iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    Glide.with(getContext()).load(mImageUris[position]).into(iv);//移除依赖
                    //iv.setImageURI(Uri.parse(mImageUris[position]));
                    if (mListeners != null && mListeners.length > position && mListeners[position] != null) {
                        iv.setOnClickListener(mListeners[position]);
                    }
                   container.addView(iv);
                    return iv;
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    container.removeView((View) object);
                }
            });
        }
    }

    /**
     * 获取 ViewPagerIndicator
     * @return
     */
    public ViewPagerIndicator getIndicator(){
        return mIndicator;
    }
}

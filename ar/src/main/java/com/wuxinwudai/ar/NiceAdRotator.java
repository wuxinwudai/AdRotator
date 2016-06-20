package com.wuxinwudai.ar;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wuxinwudai.ar.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
/**
 * NiceAdRotator 类为广告轮播插件
 * @author 吾心无待 于2016年04月1日
 */
public final class NiceAdRotator extends FrameLayout implements ViewPager.OnPageChangeListener {
    private final static String TAG = "NiceAdRotator";
    private final ViewPager mViewPager;//ViewPager
    private final LinearLayout mIndicator;//分页指示器
    private final LinearLayout mLinearLayout;//用于底部文字和指示器
    private final TextView mtvTitle;//广告标题
    private ArrayList<AdInfo> mAds;//显示的广告图片
    private int mDotMargin;//点之间的距离
    private int mDotImageSrc;//点图片，Selector资源
    private int mDotWidth;//点图片宽度
    private int mCurTag;//当前tag
    private int mPlayInterval;//轮播时间间隔
    private HashMap<String,Bitmap> mCache = new HashMap<String,Bitmap>();
    private Handler mMyHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        }
    };
    private ScheduledExecutorService  mExecutor = Executors.newSingleThreadScheduledExecutor();
    private Task  mTask = new Task();
    private OnClickListener mDotClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int move = (int)v.getTag() - mCurTag;
            if (move > 0){
                while (move-- > 0){
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() +  1,false);//使用 mViewPager.setCurrentItem(mViewPager.getCurrentItem() +  move);卡屏
                }
            }else{
                while (move++ < 0){
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() -  1,false);
                }
            }
            mCurTag = (int)v.getTag();
        }
    };
    /**
     * 构造函数，初始化 NiceAdRotator 类的一个新实例
     * @param context 上下文对象
     */
    public NiceAdRotator(Context context) {
        this(context, null);
    }

    /**
     * 构造函数，初始化 NiceAdRotator 类的一个新实例
     * @param context 上下文对象
     * @param attrs 属性列表
     */
    public NiceAdRotator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造函数，初始化 NiceAdRotator 类的一个新实例
     * @param context 上下文对象
     * @param attrs 属性列表
     * @param defStyleAttr 资源ID
     */
    public NiceAdRotator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mViewPager = new ViewPager(context);
        mIndicator = new LinearLayout(context);
        mLinearLayout = new LinearLayout(context);
        mtvTitle = new TextView(context);
        mViewPager.addOnPageChangeListener(this);//设置滚动页面监听

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.NiceAdRotator);
        mDotWidth = ta.getInteger(R.styleable.NiceAdRotator_DotWidth, 10);
        mDotImageSrc = ta.getResourceId(R.styleable.NiceAdRotator_DotImageSrc, NO_ID);
        mDotMargin = ta.getInteger(R.styleable.NiceAdRotator_DotMargin, 5);
        mPlayInterval = ta.getInteger(R.styleable.NiceAdRotator_PlayInterval,5);
        ta.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        mViewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        mLinearLayout.setLayoutParams(params);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setPadding(10, 8, 10, 5);
        mLinearLayout.setBackgroundResource(R.drawable.mask);
        mtvTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mtvTitle.setTextSize(16);
        mtvTitle.setSingleLine(true);
        mtvTitle.setEllipsize(TextUtils.TruncateAt.END);
        mtvTitle.setGravity(Gravity.CENTER);
        mIndicator.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mIndicator.setOrientation(LinearLayout.HORIZONTAL);
        mIndicator.setGravity(Gravity.CENTER);
        mLinearLayout.addView(mtvTitle);
        mLinearLayout.addView(mIndicator);
        this.addView(mViewPager);
        this.addView(mLinearLayout);
        super.onAttachedToWindow();
        mExecutor.scheduleWithFixedDelay(mTask, 2, mPlayInterval, TimeUnit.SECONDS);
    }

    /**
     * 获取广告信息列表
     * @return 获取广告信息
     */
    public ArrayList<AdInfo> getAds() {
        return mAds;
    }

    /**
     * 设置广告信息列表
     * @param mAds
     */
    public void setAds(ArrayList<AdInfo> mAds) {
        this.mAds = mAds;
        setViewPagerAdapter();
        initDots();
    }


    private void setViewPagerAdapter(){
        if (mAds != null) {
            mViewPager.setAdapter(new PagerAdapter() {
                @Override
                public int getCount() {
                    return Integer.MAX_VALUE;
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
                    AdInfo ad = mAds.get(position % mAds.size());//取余数
                    Glide.with(getContext()).load(ad.getImageUri()).into(iv);
                    //iv.setImageURI(Uri.parse(ad.getImageUri()));
                    if (ad.getOnClickListener() != null) {
                        iv.setOnClickListener(ad.getOnClickListener());
                    }
                    ((ViewPager) container).addView(iv);
                    mtvTitle.setText(ad.getTitle());
                    return iv;
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    ((ViewPager) container).removeView((View) object);
                }
            });
            //默认在1亿多
            mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - ((Integer.MAX_VALUE / 2) % mAds.size()));
        }
    }

    //初始化指示点
    private void initDots(){
        for (int i = 0; i < mAds.size(); i++) {
            ImageView view = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDotWidth, mDotWidth);
            params.setMargins(mDotMargin, 3, mDotMargin, 3);
            view.setLayoutParams(params);
            view.setTag(i);
            view.setImageResource(mDotImageSrc == NO_ID ? R.drawable.selector_dot_info : mDotImageSrc);
            view.setOnClickListener(mDotClickListener);
            if(i==0)view.setEnabled(false);
            mIndicator.addView(view);
        }
    }
    //更新广告信息
    private void updateAdInfo(){
        int currentPage = mViewPager.getCurrentItem() % mAds.size();
        mtvTitle.setText(mAds.get(currentPage).getTitle());
        for (int i = 0; i < mIndicator.getChildCount(); i++) {
            View v = mIndicator.getChildAt(i);
            v.setEnabled(i != currentPage);//设置setEnabled为true的话 在选择器里面就会对应的使用白色颜色
            if (!v.isEnabled()){
                mCurTag = (int)v.getTag();
            }
        }

    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        updateAdInfo();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class Task implements Runnable{
        @Override
        public void run() {
            mMyHandler.sendEmptyMessage(0);
        }
    }
}

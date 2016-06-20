package com.wuxinwudai.ar.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wuxinwudai.ar.AdInfo;
import com.wuxinwudai.ar.AdRotator;
import com.wuxinwudai.ar.NiceAdRotator;

import java.util.ArrayList;

/**
 * @author 吾心无待 于2016年04月01日
 */
public class NiceAdRotatorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nice_ad_rotator);
        NiceAdRotator ad = (NiceAdRotator)findViewById(R.id.ad);
        ArrayList<AdInfo> ads = new ArrayList<AdInfo>(){
            {
                add(AdInfo.create("苟利国家生死以，岂因祸福避趋之","http://e.hiphotos.baidu.com/zhidao/pic/item/78310a55b319ebc40bbc7c3e8326cffc1f171654.jpg",null));
                add(AdInfo.create("人生自古谁无死，留取丹心照汗青","http://i1.qhimg.com/t01937479b9b519c562.jpg",null));
                add(AdInfo.create("位卑未敢忘忧国，事定犹须待阖棺","http://imgsrc.baidu.com/forum/pic/item/4ac597dda144ad34b9940179d0a20cf430ad8508.jpg",null));
                add(AdInfo.create("我劝天公重抖擞，不拘一格降人才","http://imgsrc.baidu.com/forum/pic/item/659079310a55b31944ec34f343a98226cefc17f6.jpg",null));

            }
        };
        ad.setAds(ads);
    }
}

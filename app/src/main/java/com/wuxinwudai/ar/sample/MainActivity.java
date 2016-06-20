package com.wuxinwudai.ar.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.wuxinwudai.ar.AdRotator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AdRotator ad = (AdRotator)findViewById(R.id.ad);
        String[] images = {"http://e.hiphotos.baidu.com/zhidao/pic/item/78310a55b319ebc40bbc7c3e8326cffc1f171654.jpg",
                "http://i1.qhimg.com/t01937479b9b519c562.jpg",
                "http://imgsrc.baidu.com/forum/pic/item/4ac597dda144ad34b9940179d0a20cf430ad8508.jpg",
                "http://imgsrc.baidu.com/forum/pic/item/659079310a55b31944ec34f343a98226cefc17f6.jpg"};
        ad.setImageUris(images);
    }
}

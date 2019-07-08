package com.frxs.order;

import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.ewu.core.utils.ImageLoader;
import com.frxs.order.application.FrxsApplication;
import com.pacific.adapter.PagerAdapterHelper;
import com.pacific.adapter.ViewPagerAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Created by Chentie on 2016/11/28.
 * 代购商品图片展示页面
 */
public class PreSaleGoodsPhotoViewActivity extends FrxsActivity {

    private ViewPager photosViewPager;
    private ArrayList<String> imageExtList;
    private String backImgPath;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_view;
    }

    @Override
    protected void initViews() {
        imageExtList = getIntent().getStringArrayListExtra("imageExtList");
        backImgPath = getIntent().getStringExtra("IMG_PATH");
        if (!TextUtils.isEmpty(backImgPath)){
            ArrayList<String> backList = new ArrayList<String>();
            backList.add(backImgPath);
            imageExtList = backList;
        }
        photosViewPager = (ViewPager) findViewById(R.id.pre_sale_simple);

        photosViewPager.setAdapter(new ViewPagerAdapter<String>(this, imageExtList, R.layout.item_photo_view) {
            @Override
            protected void convert(PagerAdapterHelper helper, String item) {
                // 当前图片是否是http开头，不是默认加上
                if (!item.startsWith("http://", 0)){
                    item = "http://" + item;
                }
                ImageLoader.loadImage(FrxsApplication.context, item, (ImageView) helper.getView(R.id.photo_iv) ,R.mipmap.showcase_product_default,R.mipmap.showcase_product_default);
                helper.setOnClickListener(R.id.photo_iv, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PreSaleGoodsPhotoViewActivity.this.finish();
                    }
                });
            }
        });

        // 页面圆形导标
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        // 图片数量多于一张时显示图片向导标识
        if (imageExtList.size() > 1) {
            indicator.setViewPager(photosViewPager);
        }
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }


}

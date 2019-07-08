package com.frxs.order;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ewu.core.utils.LogUtils;
import com.ewu.core.widget.slidingtabs.SlidingTabLayout;
import com.frxs.order.adapter.TabPagerAdapter;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.fragment.ActivityGoodsFragment;
import com.frxs.order.model.PromotiaonActivityDetail;
import com.frxs.order.model.PromotionActivityGroup;
import com.frxs.order.model.PromotionProduct;
import com.frxs.order.model.UserInfo;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

/**
 * Created by ewu on 2016/9/9.
 */
public class PromotionDetailActivity extends FrxsActivity implements SlidingTabLayout.TabAdapter {

    private TextView activityExtendTv;
    private View activityInfoLayout;
    private SlidingTabLayout goodsGroupTabs;
    private ViewPager groupGoodsViewPager;
    private TabPagerAdapter groupGoodsFragPagerAdapter;

    private TextView activityNameTv;
    private TextView activityValidityTv;
    private TextView activityRuleTv;

    private View viewLine;

    private List<Fragment> pagerFragmentList = new ArrayList<Fragment>();

    private List<PromotionActivityGroup> promotionActivityGroupList;

    private List<PromotionProduct> promotionActivityGiftList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_promotion_detail;
    }

    @Override
    protected void initViews() {
        ((TextView) findViewById(R.id.title_tv)).setText(R.string.title_promotion);
        activityExtendTv = (TextView) findViewById(R.id.promotion_extend_view);
        activityInfoLayout = findViewById(R.id.activity_info_ll);
        activityNameTv = (TextView) findViewById(R.id.activity_name_tv);
        activityValidityTv = (TextView) findViewById(R.id.activity_validity_tv);
        activityRuleTv = (TextView) findViewById(R.id.activity_rule_tv);
        goodsGroupTabs = (SlidingTabLayout) findViewById(R.id.goods_group_tab);
        groupGoodsViewPager = (ViewPager) findViewById(R.id.group_goods_viewpager);
        viewLine = findViewById(R.id.view_line);
//
//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, R.layout.fragment_activity_goods) {
//            @Override
//            protected void convert(PagerAdapterHelper helper, Object item) {
//
//            }
//        };

        groupGoodsFragPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), pagerFragmentList);
        groupGoodsViewPager.setAdapter(groupGoodsFragPagerAdapter);

        goodsGroupTabs.setCustomTabView(R.layout.view_group_tab_item, R.id.textview, R.id.message_count_tv);
        goodsGroupTabs.setDistributeEvenly(true);
        goodsGroupTabs.setTabAdapter(this);
        goodsGroupTabs.setSelectedIndicatorColors(getResources().getColor(R.color.frxs_red));
        goodsGroupTabs.setViewPager(groupGoodsViewPager);
    }

    @Override
    protected void initEvent() {
        activityExtendTv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (null != intent) {
            String promotionId = intent.getStringExtra("PromotionID");
            if (!TextUtils.isEmpty(promotionId)) {
                showProgressDialog();
                requestGetPromotionActivityDetails(promotionId);
            }
        }
    }

    public void updapteGroupCartCount(int index, int productId, int addCount) {
        if (null != promotionActivityGroupList && promotionActivityGroupList.size() > index) {
            List<PromotionProduct> groupList = promotionActivityGroupList.get(index).getWPromotionProductsList();
            for (PromotionProduct item : groupList) {
                if (item.getProductId() == productId) {
                    int cartQty = item.getCartQty() + addCount;
                    item.setCartQty(cartQty);
                    break;
                }
            }
        }

        showPromotionGroupCartCount(index);
    }

    private void showPromotionGroupCartCount(int index) {
        if (null != promotionActivityGroupList && promotionActivityGroupList.size() > index) {
            List<PromotionProduct> groupList = promotionActivityGroupList.get(index).getWPromotionProductsList();
            int totalCount = 0;
            for (PromotionProduct item : groupList) {
                totalCount += item.getCartQty();// 商品总数
            }
            goodsGroupTabs.setMessageCount(index, totalCount);
        }
    }

    private void initPromotionActivityGroups(PromotiaonActivityDetail activityDetail) {
        if (activityDetail != null) {
            pagerFragmentList.clear();

            promotionActivityGroupList = activityDetail.getWPromotionModelGroupList();// 促销商品
            promotionActivityGiftList = activityDetail.getWPromotionGiftProductsList();// 赠品

            if (null != promotionActivityGroupList && promotionActivityGroupList.size() > 0) {
                int groupSize = promotionActivityGroupList.size();
                boolean isTabView = false;

                for (PromotionActivityGroup groupItem : promotionActivityGroupList) {
                    if (groupItem.getWPromotionProductsList().size() > 1) {
                        isTabView = true;
                        break;
                    }
                }

                if (isTabView) {
                    for (int i = 0; i < groupSize; i++) {
                        ActivityGoodsFragment activityGoodsFragment = new ActivityGoodsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("product_list", (Serializable) promotionActivityGroupList.get(i).getWPromotionProductsList());
                        bundle.putSerializable("is_tab", true);
                        bundle.putSerializable("index", i); //标识TAB的位置
                        activityGoodsFragment.setArguments(bundle);
                        pagerFragmentList.add(activityGoodsFragment);
                    }

                    if (null != promotionActivityGiftList && promotionActivityGiftList.size() > 0) {
                        ActivityGoodsFragment activityGiftGoodsFragment = new ActivityGoodsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("product_list", (Serializable) promotionActivityGiftList);
                        bundle.putSerializable("is_tab", true); //是否是 tabfragment
                        activityGiftGoodsFragment.setArguments(bundle);
                        pagerFragmentList.add(activityGiftGoodsFragment);
                    }

                    groupGoodsFragPagerAdapter.setPagerItems(pagerFragmentList);
                    goodsGroupTabs.notifyDataSetChanged();
                    groupGoodsFragPagerAdapter.notifyDataSetChanged();
                } else {
                    List<PromotionProduct> productList = new ArrayList<PromotionProduct>();
                    for (int i = 0; i < groupSize; i++) {
                        productList.addAll(promotionActivityGroupList.get(i).getWPromotionProductsList());
                    }

                    ActivityGoodsFragment activityGiftGoodsFragment = new ActivityGoodsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("product_list", (Serializable) productList);
                    bundle.putSerializable("is_tab", false);
                    if (null != promotionActivityGiftList && promotionActivityGiftList.size() > 0) {
                        bundle.putSerializable("gift_list", (Serializable) promotionActivityGiftList);

                    }
                    activityGiftGoodsFragment.setArguments(bundle);
                    pagerFragmentList.add(activityGiftGoodsFragment);
                    groupGoodsFragPagerAdapter.setPagerItems(pagerFragmentList);
                    goodsGroupTabs.notifyDataSetChanged();
                    goodsGroupTabs.hideTabStrip(true);
                    groupGoodsFragPagerAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.promotion_extend_view:
                if (activityInfoLayout.isShown()) {
                    activityInfoLayout.setVisibility(View.GONE);
                    viewLine.setVisibility(View.GONE);
                    Drawable drawable = getResources().getDrawable(R.mipmap.detail_unfold);
                    activityExtendTv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                } else {
                    activityInfoLayout.setVisibility(View.VISIBLE);
                    viewLine.setVisibility(View.VISIBLE);
                    Drawable drawable = getResources().getDrawable(R.mipmap.detail_fold);
                    activityExtendTv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                }
                break;
            default:
                break;
        }
    }

    private void requestGetPromotionActivityDetails(String promotionId) {
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        AjaxParams params = new AjaxParams();
        params.put("ShopId", getShopID());
        params.put("WarehouseId", String.valueOf(getWID()));
        params.put("PromotionId", promotionId);
        params.put("UserId", userInfo.getUserId());
        params.put("UserName", userInfo.getUserName());

        getService().GetPromotionActivityDetails(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<PromotiaonActivityDetail>>() {

            @Override
            public void onResponse(ApiResponse<PromotiaonActivityDetail> result, int code, String msg) {

                PromotiaonActivityDetail activityDetail = result.getData();
                if (null != activityDetail) {
                    renderViewWithData(activityDetail);

                    initPromotionActivityGroups(activityDetail);
                    if (promotionActivityGroupList != null && promotionActivityGroupList.size() >= 1) {
                        for (int i = 0; i < promotionActivityGroupList.size(); i++) {
                            showPromotionGroupCartCount(i);
                        }
                    }
                }

                dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<ApiResponse<PromotiaonActivityDetail>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
            }
        });
    }

    private void renderViewWithData(PromotiaonActivityDetail activityDetail) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//标准年月日时分秒格式
        SimpleDateFormat sdfPro = new SimpleDateFormat("yyyy-MM-dd HH:mm");//活动时间预期格式
        try {
            //字符串转日期
            Date proStart = sdf.parse(activityDetail.getBeginTime());//活动开始时间
            Date proEnd = sdf.parse(activityDetail.getEndTime());//活动结束时间
            //日期转字符串
            String strProStart = sdfPro.format(proStart);
            String strProEnd = sdfPro.format(proEnd);
            activityValidityTv.setText(String.format(getString(R.string.activity_validity), strProStart, strProEnd));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        activityNameTv.setText(activityDetail.getPromotionName());
        activityRuleTv.setText(activityDetail.getPromotionRules());
    }

    @Override
    public CharSequence getTitle(int position) {
        LogUtils.i(position+"\\\\");
        if (pagerFragmentList.size() > position) {
            if (promotionActivityGroupList.size() > position) {
                String groupCodeStr = promotionActivityGroupList.get(position).getWPromotionModelGroupModel().getGroupCode();
                return String.format(getString(R.string.activity_group_name), groupCodeStr);
            } else {
                int isGift =  promotionActivityGiftList.get(0).getIsGift();
                if (1 == isGift){
                    return getString(R.string.activity_gift_goup_name);
                }else{
                    return getString(R.string.activity_match_goup_name);
                }
            }
        } else {
            return "";
        }
    }

    @Override
    public int getImageId(int position) {
        return 0;
    }

    @Override
    public int getTabWidth(int position) {
        return 0;
    }

    @Override
    public void loadBitmap(View imageView, int position, boolean isSelect) {

    }
}

package com.frxs.order;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ewu.core.utils.ImageLoader;
import com.ewu.core.utils.LogUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.EmptyView;
import com.ewu.core.widget.PtrFrameLayoutEx;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.model.Promotion;
import com.frxs.order.model.PromotionProduct;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit2.Call;

/**
 * 促销活动列表 by Tiepier
 */
public class PromotionListActivity extends MaterialStyleActivity {

    private EmptyView emptyView;//无数据交互

    private TextView tvTitle;//标题

    private ListView lvPromotion;//促销活动列表

    private PtrFrameLayoutEx mPtrFrameLayoutEx;//下拉刷新控件

    private int mPageIndex = 1;//页序

    private final int mPageSize = 30;//页大小

    private Adapter<Promotion> quickAdapter;

    @Override
    public int getPtrFrameLayoutId() {
        return R.id.goods_ptr_frame_ll;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_promotion_list;
    }

    @Override
    protected void initViews() {
        super.initViews();
        emptyView = (EmptyView) findViewById(R.id.emptyview);
        lvPromotion = (ListView) findViewById(R.id.lv_promotion_list);
        mPtrFrameLayoutEx = (PtrFrameLayoutEx) mPtrFrameLayout;
        tvTitle = (TextView) findViewById(R.id.title_tv);

        tvTitle.setText("促销活动列表");

    }

    @Override
    protected void initEvent() {
        //下拉刷新监听事件
        mPtrFrameLayoutEx.setOnLoadListener(new PtrFrameLayoutEx.OnLoadListener() {
            @Override
            public void onLoad() {
                mPageIndex += 1;
                showProgressDialog();
                requestPromotionList();
            }
        });
        mPtrFrameLayout.setPinContent(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                boolean iReturn = PtrDefaultHandler.checkContentCanBePulledDown(frame, lvPromotion, header);
                return iReturn;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPageIndex = 1;
                requestPromotionList();
            }
        });

    }

    //促销活动列表数据请求
    private void requestPromotionList() {
        AjaxParams params = new AjaxParams();
        params.put("UserName", FrxsApplication.getInstance().getUserInfo().getUserName());
        params.put("UserID", getUserID());
        params.put("WarehouseId", getWID());
        params.put("ShopID", getShopID());
        params.put("PageIndex", mPageIndex);
        params.put("PageSize", mPageSize);
        getService().GetPromotion(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<List<Promotion>>>() {
            @Override
            public void onResponse(ApiResponse<List<Promotion>> result, int code, String msg) {
                refreshComplete();
                dismissProgressDialog();
                List<Promotion> respData = result.getData();
                if (null != respData) {
                    if (null != respData) {
                        quickAdapter.replaceAll(respData);

                        if (mPageIndex == 1) {
                            quickAdapter.replaceAll(respData);
                        } else {
                            quickAdapter.addAll(respData);
                        }
                        emptyView.setVisibility(View.GONE);

                    } else {
                        if (mPageIndex == 1) {
                            initEmptyView(EmptyView.MODE_NODATA);
                        } else {
                            ToastUtils.show(PromotionListActivity.this, R.string.tips_pageending);
                        }
                    }
                } else {
                    if (mPageIndex == 1) {
                        initEmptyView(EmptyView.MODE_NODATA);
                    } else {
                        ToastUtils.show(PromotionListActivity.this, R.string.tips_pageending);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Promotion>>> call, Throwable t) {
                super.onFailure(call, t);
                refreshComplete();

                dismissProgressDialog();

                initEmptyView(EmptyView.MODE_ERROR);
            }
        });
    }

    @Override
    protected void initData() {
        requestPromotionList();
        quickAdapter = new Adapter<Promotion>(this, R.layout.item_promotion_list) {
            @Override
            protected void convert(final AdapterHelper helper, final Promotion item) {

                //促销活动名称
                helper.setText(R.id.tv_promotion_name, item.getPromotionName());
                //促销活动时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//标准年月日时分秒格式
                SimpleDateFormat sdfPromotionTime = new SimpleDateFormat("yyyy.MM.dd");//促销时间预期格式
                try {
                    //字符串转日期
                    Date proBeginTime = sdf.parse(item.getBeginTime());//促销活动开始时间
                    Date proEndTime = sdf.parse(item.getEndTime());//促销活动结束时间
                    //日期转字符串
                    String strBetinTime = sdfPromotionTime.format(proBeginTime);
                    String strEndTime = sdfPromotionTime.format(proEndTime);
                    helper.setText(R.id.tv_promotion_time, strBetinTime + "-" + strEndTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (item.getItems() != null && item.getItems().size() > 0) {
                    switch (item.getItems().size()) {
                        case 1:
                            //促销商品图片处理
                            if (!TextUtils.isEmpty(item.getItems().get(0).getImageUrl200x200())) {
                                ImageLoader.loadImage(PromotionListActivity.this, item.getItems().get(0).getImageUrl200x200(), (ImageView) helper.getView(R.id.img_promotion_1), R.mipmap.showcase_product_default, R.mipmap.showcase_product_default);
                            } else {
                                ImageLoader.loadImage(PromotionListActivity.this, (ImageView) helper.getView(R.id.img_promotion_1), R.mipmap.showcase_product_default);
                            }
                            helper.setVisible(R.id.img_promotion_1, View.VISIBLE);
                            helper.setVisible(R.id.img_promotion_2, View.INVISIBLE);
                            helper.setVisible(R.id.img_promotion_3, View.INVISIBLE);
                            break;
                        case 2:
                            //促销商品图片处理
                            if (!TextUtils.isEmpty(item.getItems().get(0).getImageUrl200x200())) {
                                ImageLoader.loadImage(PromotionListActivity.this, item.getItems().get(0).getImageUrl200x200(), (ImageView) helper.getView(R.id.img_promotion_1), R.mipmap.showcase_product_default, R.mipmap.showcase_product_default);
                            } else {
                                ImageLoader.loadImage(PromotionListActivity.this, (ImageView) helper.getView(R.id.img_promotion_1), R.mipmap.showcase_product_default);
                            }
                            if (!TextUtils.isEmpty(item.getItems().get(1).getImageUrl200x200())) {
                                ImageLoader.loadImage(PromotionListActivity.this, item.getItems().get(1).getImageUrl200x200(), (ImageView) helper.getView(R.id.img_promotion_2), R.mipmap.showcase_product_default, R.mipmap.showcase_product_default);
                            } else {
                                ImageLoader.loadImage(PromotionListActivity.this, (ImageView) helper.getView(R.id.img_promotion_2), R.mipmap.showcase_product_default);
                            }
                            helper.setVisible(R.id.img_promotion_1, View.VISIBLE);
                            helper.setVisible(R.id.img_promotion_2, View.VISIBLE);
                            helper.setVisible(R.id.img_promotion_3, View.INVISIBLE);
                            break;
                        default:
                            //促销商品图片处理
                            if (!TextUtils.isEmpty(item.getItems().get(0).getImageUrl200x200())) {
                                ImageLoader.loadImage(PromotionListActivity.this, item.getItems().get(0).getImageUrl200x200(), (ImageView) helper.getView(R.id.img_promotion_1), R.mipmap.showcase_product_default, R.mipmap.showcase_product_default);
                            } else {
                                ImageLoader.loadImage(PromotionListActivity.this, (ImageView) helper.getView(R.id.img_promotion_1), R.mipmap.showcase_product_default);
                            }
                            if (!TextUtils.isEmpty(item.getItems().get(1).getImageUrl200x200())) {
                                ImageLoader.loadImage(PromotionListActivity.this, item.getItems().get(1).getImageUrl200x200(), (ImageView) helper.getView(R.id.img_promotion_2), R.mipmap.showcase_product_default, R.mipmap.showcase_product_default);
                            } else {
                                ImageLoader.loadImage(PromotionListActivity.this, (ImageView) helper.getView(R.id.img_promotion_2), R.mipmap.showcase_product_default);
                            }
                            if (!TextUtils.isEmpty(item.getItems().get(2).getImageUrl200x200())) {
                                ImageLoader.loadImage(PromotionListActivity.this, item.getItems().get(2).getImageUrl200x200(), (ImageView) helper.getView(R.id.img_promotion_3), R.mipmap.showcase_product_default, R.mipmap.showcase_product_default);
                            } else {
                                ImageLoader.loadImage(PromotionListActivity.this, (ImageView) helper.getView(R.id.img_promotion_3), R.mipmap.showcase_product_default);
                            }
                            helper.setVisible(R.id.img_promotion_1, View.VISIBLE);
                            helper.setVisible(R.id.img_promotion_2, View.VISIBLE);
                            helper.setVisible(R.id.img_promotion_3, View.VISIBLE);
                            break;
                    }
                }
                //查看详情
                helper.setOnClickListener(R.id.tv_promotion_more, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PromotionListActivity.this, PromotionDetailActivity.class);
                        intent.putExtra("PromotionID", item.getPromotionID());
                        startActivity(intent);
                    }
                });
            }
        };

        lvPromotion.setAdapter(quickAdapter);
    }

    //无数据交互处理
    private void initEmptyView(int mode) {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setMode(mode);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                requestPromotionList();
            }
        });
    }

}

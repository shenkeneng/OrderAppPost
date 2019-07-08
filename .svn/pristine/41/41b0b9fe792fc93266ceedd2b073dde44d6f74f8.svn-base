package com.frxs.order;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.BadgeView;
import com.ewu.core.widget.EmptyView;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.comms.GlobelDefines;
import com.frxs.order.model.AttributePicture;
import com.frxs.order.model.PictureDetails;
import com.frxs.order.model.PostEditCart;
import com.frxs.order.model.PostInfo;
import com.frxs.order.model.Product;
import com.frxs.order.model.ProductAttribute;
import com.frxs.order.model.ProductInfo;
import com.frxs.order.model.ProductWProductsDetailsGetRespData;
import com.frxs.order.model.ShopInfo;
import com.frxs.order.model.WProduct;
import com.frxs.order.model.WProductExt;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.utils.DensityUtils;
import com.frxs.order.widget.CountEditText;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;

/**
 * Created by ewu on 2016/5/4.
 */
public class ProductDetailActivity extends FrxsActivity {

    private EmptyView emptyView;

    private WProductExt product;

    private ProductInfo curProductInfo;

    private ProductWProductsDetailsGetRespData productDetailsData;

    private TextView productNameTv;

    private TextView productSubName;//商品副标题

    private TextView productPointsTv;

    private TextView customerServiceTv;

    private TextView unitValueTv;

    private ViewPager picsViewPager;

    private TextView indicatorTv;

    private TextView selectedParamsTv;

    private View selectedParamsLayout;

    private CountEditText countEditText;

    private TextView orderedNumTv;

    private View shopCartBtn;

    private ImageView cartIv;

    private BadgeView badgeView;

    private TextView tvPackageValue;

    private TextView tvCodeValue;

    private TextView tvAdvisePrice;

    private TextView tvGoodsDesc;

    private TextView tvGoodsBuy;//商品购买

    private ArrayList<String> imageUrls = new ArrayList<String>();

    private int shopCartCount = 0;

    private int productCartCount = 0; //商品在购物车中的数量（即已定数量）

    private String productId;

    private RelativeLayout rlPromotion;//促销商品模块

    private TextView tvPromotionName;//促销活动名称

    private TextView tvPromotionFlag;//促销商品标识符

    private View viewLine;

    private TextView tvTipsQty;//起订量

    private LinearLayout llPreQty;//起订量模块

    private TextView tvDeliveryPrice;//配送价格

    private TextView tvBarCode;//国际条码

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    protected void initViews() {
        ((TextView) findViewById(R.id.title_tv)).setText(R.string.goods_detail_title);
        emptyView = (EmptyView) findViewById(R.id.emptyview);
        picsViewPager = (ViewPager) findViewById(R.id.pics_view_pager);
        indicatorTv = (TextView) findViewById(R.id.pager_indicator_tv);
        productNameTv = (TextView) findViewById(R.id.goods_title);
        productSubName = (TextView) findViewById(R.id.goods_subtitle);
        productPointsTv = (TextView) findViewById(R.id.tv_goods_integral);
        customerServiceTv = (TextView) findViewById(R.id.customer_service_value_tv);
        unitValueTv = (TextView) findViewById(R.id.unit_value_tv);
        selectedParamsTv = (TextView) findViewById(R.id.selected_params_tv);
        selectedParamsLayout = findViewById(R.id.selected_sku_layout);
        countEditText = (CountEditText) findViewById(R.id.count_edit_text);
        countEditText.setMaxCount(9999);
        orderedNumTv = (TextView) findViewById(R.id.goods_stock_info);
        shopCartBtn = findViewById(R.id.good_cartrl);
        cartIv = (ImageView) findViewById(R.id.good_cart_iv);
        tvPackageValue = (TextView) findViewById(R.id.package_value_tv);
        tvCodeValue = (TextView) findViewById(R.id.code_value_tv);
        tvGoodsDesc = (TextView) findViewById(R.id.goods_desc);
        tvAdvisePrice = (TextView) findViewById(R.id.tv_advise_price);
        tvGoodsBuy = (TextView) findViewById(R.id.order_btn);
        rlPromotion = (RelativeLayout) findViewById(R.id.rl_promotion);
        tvPromotionName = (TextView) findViewById(R.id.tv_promotion_name);
        tvPromotionFlag = (TextView) findViewById(R.id.tv_good_discount);
        viewLine = findViewById(R.id.view_line);
        tvTipsQty = (TextView) findViewById(R.id.tv_tips_preqty);
        llPreQty = (LinearLayout) findViewById(R.id.ll_preqty);
        tvDeliveryPrice = (TextView) findViewById(R.id.tv_delivery_price);
        tvBarCode = (TextView) findViewById(R.id.barcode_value_tv);

        badgeView = new BadgeView(this, cartIv);
        badgeView.setTextSize(8);
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
    }

    @Override
    protected void initEvent() {
        selectedParamsLayout.setOnClickListener(this);
        findViewById(R.id.order_btn).setOnClickListener(this);
        shopCartBtn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (null != intent) {
            product = (WProductExt) intent.getSerializableExtra("product");
            requestProductDetail();
        }

        if (product != null) {
            String productId = product.getProductId();
            if (!TextUtils.isEmpty(productId)) {
                this.productId = productId;
                requestGetSaleCartCount();
            }

            //商品冻结业务处理
            if (product.getWStatus() == 3) {
                tvGoodsBuy.setEnabled(false);
                tvGoodsBuy.setTextColor(Color.GRAY);
                tvGoodsBuy.setBackgroundResource(R.drawable.shape_button_gray);
            } else {
                tvGoodsBuy.setEnabled(true);
                tvGoodsBuy.setTextColor(Color.RED);
                tvGoodsBuy.setBackgroundResource(R.drawable.shape_button_red);
            }
        }
    }

    private void initBadgeView() {
        shopCartCount = FrxsApplication.getInstance().getShopCartCount();
        showBadgeView(shopCartCount);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initBadgeView();
        requestGetSaleCartCount();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (GlobelDefines.REQ_CODE_PRODUCT_DETAIL == requestCode) {
            if (GlobelDefines.RESULT_CODE_SKU == resultCode) {
                ProductInfo selectedSubproduct = (ProductInfo) data.getSerializableExtra("selected_subproduct");
                if (null != selectedSubproduct) {
                    curProductInfo = selectedSubproduct;
                    renderViewWithData();

                    Product product = curProductInfo.getProduct();
                    if (product != null) {
                        productId = String.valueOf(product.getProductId());
                        requestGetSaleCartCount();
                    }
                }
            }
        }
    }

    private void showBadgeView(int count) {
        if (count > 0) {
            if (count > 100) {
                badgeView.setText("99+");
            } else {
                badgeView.setText(String.valueOf(count));
            }
            badgeView.show();
        } else {
            badgeView.hide();
        }
    }

    private void requestProductDetail() {
        AjaxParams params = new AjaxParams();
        params.put("WID", getWID());
        params.put("ShopID", getShopID());
        if (product != null) {
            params.put("ProductId", product.getProductId());
        }

        getService().WProductsB2BDetailsGet(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<ProductWProductsDetailsGetRespData>>() {
            @Override
            public void onResponse(ApiResponse<ProductWProductsDetailsGetRespData> result, int code, String msg) {
                dismissProgressDialog();

                ProductWProductsDetailsGetRespData respData = result.getData();
                if (null != respData) {
                    productDetailsData = respData;
                    curProductInfo = respData.getCurrentProduct();
                    if (null != curProductInfo) {
                        //子商品促销活动ID、起订量(最大、最小)赋值
                        List<ProductInfo> subProductList = productDetailsData.getSubProducts();
                        for (int i = 0; i < subProductList.size(); i++) {
                            subProductList.get(i).setPromotionID(curProductInfo.getPromotionID());
                            subProductList.get(i).setMinPreQty(curProductInfo.getMinPreQty());
                            subProductList.get(i).setMaxaPreQty(curProductInfo.getMaxaPreQty());
                        }

                        renderViewWithData();
                    }

                    emptyView.setVisibility(View.GONE);
                } else {
                    initEmptyView(EmptyView.MODE_NODATA);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ProductWProductsDetailsGetRespData>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
                initEmptyView(EmptyView.MODE_ERROR);
            }
        });
    }

    private void initEmptyView(int mode) {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setMode(mode);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDetailActivity.this.showProgressDialog();
                requestProductDetail();
            }
        });
    }

    private void requestGetSaleCartCount() {
        AjaxParams params = new AjaxParams();
        params.put("ShopID", getShopID());
        params.put("WarehouseId", String.valueOf(getWID()));
        params.put("UserId", getUserID());
        params.put("ProductId", productId); //获取当前单个商品在购物车中的数量

        getService().SaleCartCount(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<Integer>>() {
            @Override
            public void onResponse(ApiResponse<Integer> result, int code, String msg) {
                productCartCount = result.getData();
                orderedNumTv.setText(String.valueOf(productCartCount));
            }

            @Override
            public void onFailure(Call<ApiResponse<Integer>> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    private void reqBuyGoods(final int count) {
        showProgressDialog();
        // 处理单次购买商品数据
        ShopInfo info = FrxsApplication.getInstance().getmCurrentShopInfo();

        PostInfo postInfo = new PostInfo();
        if (curProductInfo != null) {
            if (curProductInfo.getProduct() != null) {
                postInfo.setProductID(String.valueOf(curProductInfo.getProduct().getProductId()));
            }
        }
        postInfo.setPreQty(count);
        postInfo.setWID(info.getWID());
        postInfo.setWarehouseId(info.getWID());
        postInfo.setIsGift(0);

        PostEditCart editCart = new PostEditCart();
        editCart.setEditType(0);
        editCart.setShopID(info.getShopID());
        editCart.setUserId(FrxsApplication.getInstance().getUserInfo().getUserId());
        editCart.setUserName(FrxsApplication.getInstance().getUserInfo().getUserName());
        editCart.setWarehouseId(info.getWID());
        editCart.setCart(postInfo);

        getService().SaleCartEditSingle(editCart).enqueue(new SimpleCallback<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> result, int code, String msg) {
                dismissProgressDialog();
                if (result != null) {
                    if (result.getFlag().equals("0")) {
                        ToastUtils.show(ProductDetailActivity.this, "加入购物车成功");
                        FrxsApplication.getInstance().addShopCartCount(count);
                        if (null != product) {
                            FrxsApplication.getInstance().updateSaleCartProduct(product.getProductId(), count);
                        }
                        shopCartCount += count;
                        productCartCount += count;
                        showBadgeView(shopCartCount);

                        orderedNumTv.setText(String.valueOf(productCartCount));

                        countEditText.setCount(1);
                    } else {
                        ToastUtils.show(ProductDetailActivity.this, result.getInfo());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
            }
        });
    }

    private void renderViewWithData() {
        imageUrls.clear();

        List<ProductAttribute> productAttributeList = curProductInfo.getProductAttributes();
        if (null != productAttributeList && productAttributeList.size() > 0) {
            selectedParamsLayout.setVisibility(View.VISIBLE);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < productAttributeList.size(); i++) {
                sb.append("\"" + productAttributeList.get(i).getValueStr() + "\"");
            }
            selectedParamsTv.setText(sb.toString());
        } else {
            selectedParamsLayout.setVisibility(View.GONE);
        }

        AttributePicture attributePicture = curProductInfo.getAttributePicture();
        if (null != attributePicture) {
            String imgUrl = attributePicture.getImageUrlOrg();
            if (!TextUtils.isEmpty(imgUrl)) {
                imageUrls.add(imgUrl);
            }
        }

        List<PictureDetails> pictureDetailsList = curProductInfo.getPictureDetails();
        if (null != pictureDetailsList && pictureDetailsList.size() > 0) {
            for (PictureDetails item : pictureDetailsList) {
                String imgUrl = item.getImageUrlOrg();
                if (!TextUtils.isEmpty(imgUrl)) {
                    imageUrls.add(imgUrl);
                }
            }
        }

        indicatorTv.setText(1 + "/" + imageUrls.size());

        picsViewPager.setAdapter(new PagerAdapter() {

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);// 删除页卡
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {

                ImageView img = new ImageView(ProductDetailActivity.this);
                img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                img.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(ProductDetailActivity.this).load(imageUrls.get(position)).into(img);

                container.addView(img, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                return img;
            }

            @Override
            public int getCount() {
                return imageUrls.size();
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }
        });

        picsViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                indicatorTv.setText(arg0 + 1 + "/" + imageUrls.size());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        WProduct wProduct = curProductInfo.getWProduct();
        //配送价格
        double price = MathUtils.mul(wProduct.getSalePrice(), wProduct.getBigPackingQty());
        double deliveryPrice = price * (1 + wProduct.getShopAddPerc());
        tvDeliveryPrice.setText("配送价格:￥" + MathUtils.twolittercountString(deliveryPrice) + "/" + wProduct.getBigUnit());
        //商品名称
        if (wProduct.getIsNoStock() == 0) {
            productNameTv.setText(curProductInfo.getProduct().getProductName());
        } else if (wProduct.getIsNoStock() == 1){
            productNameTv.setText(Html.fromHtml(getResources().getString(R.string.pre_good_tag) + curProductInfo.getProduct().getProductName()));
        }
        if (!TextUtils.isEmpty(curProductInfo.getWProduct().getProductName2())) {
            productSubName.setVisibility(View.VISIBLE);
            productSubName.setText(curProductInfo.getWProduct().getProductName2());
        } else {
            productSubName.setVisibility(View.GONE);
        }
        unitValueTv.setText(wProduct.getBigUnit());// 单位
        tvPackageValue.setText("1x" + DensityUtils.subZeroAndDot(MathUtils.twolittercountString(wProduct.getBigPackingQty())) + wProduct.getUnit());

        //促销商品业务处理
        if (curProductInfo.getIsGift() == 1) {
            //促销活动标识
            tvPromotionFlag.setVisibility(View.VISIBLE);
            //促销活动名称
            rlPromotion.setVisibility(View.VISIBLE);
            tvPromotionName.setText(curProductInfo.getWPromotion().getPromotionName());
            rlPromotion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ProductDetailActivity.this, PromotionDetailActivity.class);
                    intent.putExtra("PromotionID", curProductInfo.getPromotionID());
                    startActivity(intent);
                }
            });
            viewLine.setVisibility(View.VISIBLE);
        } else {
            tvPromotionFlag.setVisibility(View.INVISIBLE);
            rlPromotion.setVisibility(View.GONE);
            viewLine.setVisibility(View.GONE);
        }
        //起订量业务处理
        if (curProductInfo.getMinPreQty() != null && curProductInfo.getMaxaPreQty() != null) {
            tvTipsQty.setVisibility(View.VISIBLE);
            if (curProductInfo.getMinPreQty() == 0) {
                String strMaxQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(curProductInfo.getMaxaPreQty()));//最大起订量
                tvTipsQty.setText("最大订购量：" + strMaxQty);
            } else if (curProductInfo.getMaxaPreQty() == 0) {
                String strMinQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(curProductInfo.getMinPreQty()));//最小起订量
                tvTipsQty.setText("最小起订量：" + strMinQty);
            } else if (curProductInfo.getMinPreQty() == 0 && curProductInfo.getMaxaPreQty() == 0) {
                tvTipsQty.setVisibility(View.VISIBLE);
            } else {
                String strMinQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(curProductInfo.getMinPreQty()));//最小起订量
                String strMaxQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(curProductInfo.getMaxaPreQty()));//最大起订量
                tvTipsQty.setText(strMinQty + "≤订购量≤" + strMaxQty);
            }
        } else if (curProductInfo.getMinPreQty() != null && curProductInfo.getMaxaPreQty() == null) {
            String strMinQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(curProductInfo.getMinPreQty()));//最小起订量
            tvTipsQty.setText("最小起订量：" + strMinQty);
        } else if (curProductInfo.getMaxaPreQty() != null && curProductInfo.getMinPreQty() == null) {
            String strMaxQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(curProductInfo.getMaxaPreQty()));//最大起订量
            tvTipsQty.setText("最大订购量：" + strMaxQty);
        } else {
            llPreQty.setVisibility(View.GONE);
        }

        // 门店建议零售价
        if (wProduct.getMarketPrice() > 0) {
            tvAdvisePrice.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(wProduct.getMarketUnit())) {
                tvAdvisePrice.setText(MathUtils.twolittercountString(wProduct.getMarketPrice()) + "元/" + wProduct.getMarketUnit());
            } else {
                tvAdvisePrice.setText(MathUtils.twolittercountString(wProduct.getMarketPrice()) + "元");
            }
        } else {
            tvAdvisePrice.setVisibility(View.GONE);
        }

        // 积分
        if (wProduct.getShopPoint() > 0) {
            //有积分商品显示促销活动标识
            tvPromotionFlag.setVisibility(View.VISIBLE);
            productPointsTv.setVisibility(View.VISIBLE);
            productPointsTv.setText(String.format(getResources().getString(R.string.points), MathUtils.mul(wProduct.getShopPoint(), wProduct.getBigPackingQty())));
        } else {
            productPointsTv.setVisibility(View.GONE);
        }

        // 商品条码
        Product product = curProductInfo.getProduct();
        if (product != null) {
            String sku = product.getSKU();
            if (!TextUtils.isEmpty(sku)) {
                tvCodeValue.setText(sku);
            } else {
                tvCodeValue.setText("暂无");
            }
        } else {
            tvCodeValue.setText("暂无");
        }

        //国际条码
        if (curProductInfo.getProductsBarCodes().size() > 0) {
            String strBarCode = curProductInfo.getProductsBarCodes().get(0).getBarCode();
            if (!TextUtils.isEmpty(strBarCode)) {
                tvBarCode.setText(strBarCode);
            } else {
                tvBarCode.setText("暂无");
            }
        } else {
            tvBarCode.setText("暂无");
        }

        // 售后
        switch (wProduct.getSaleBackFlag()) {
            case 0:// 不可退
            {
                customerServiceTv.setText("不可退");// 是否可退
                break;
            }
            case 1:// 可退
            {
                customerServiceTv.setText("可退");// 是否可退
                break;
            }
            case 2:// 开箱不可退
            {
                customerServiceTv.setText("开箱不可退");// 是否可退
                break;
            }
            case 3:// 保质期60天可退
            {
                customerServiceTv.setText("保质期60天可退");// 是否可退
                break;
            }
            default:
                customerServiceTv.setText("暂无");
                break;
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.selected_sku_layout: {

                Intent intent = new Intent(this, ProductSkuActivity.class);
                intent.putExtra("product_details", productDetailsData);
                startActivityForResult(intent, GlobelDefines.REQ_CODE_PRODUCT_DETAIL);
                break;
            }
            case R.id.order_btn: {
                reqBuyGoods(countEditText.getCount());
                break;
            }
            case R.id.good_cartrl: {
                Intent intent = new Intent(this, StoreCartActivity.class);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }
}

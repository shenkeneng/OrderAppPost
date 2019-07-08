package com.frxs.order;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ewu.core.utils.ImageLoader;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.ewu.core.widget.EmptyView;
import com.ewu.core.widget.takephoto.model.TImage;
import com.ewu.core.widget.takephoto.model.TResult;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.comms.GlobelDefines;
import com.frxs.order.model.AttributePicture;
import com.frxs.order.model.PictureDetails;
import com.frxs.order.model.PostEditBackCart;
import com.frxs.order.model.Product;
import com.frxs.order.model.ProductAttribute;
import com.frxs.order.model.ProductInfo;
import com.frxs.order.model.ProductWProductsDetailsGetRespData;
import com.frxs.order.model.SaleBackCart;
import com.frxs.order.model.SaleBackCartInfo;
import com.frxs.order.model.ShopImgPath;
import com.frxs.order.model.UrlList;
import com.frxs.order.model.UserInfo;
import com.frxs.order.model.WProduct;
import com.frxs.order.model.WProductExt;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.RequestListener;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.widget.CountEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

import static com.frxs.order.R.id.tv_submit_goods;

/**
 * Created by shenpei on 2017/6/8.
 * 退货商品详情页
 */

public class BackGoodsInfoActivity extends UploadPictureActivity {

    private TextView submitGoodsTv;

    private WProductExt productReturn;// 退货商品

    private ProductWProductsDetailsGetRespData productDetailsData;

    private ProductInfo curProductInfo;

    private EmptyView emptyView;

    private ViewPager picsViewPager;// 商品展示

    private TextView indicatorTv;// 指示器

    private TextView productNameTv;// 商品名称

    private TextView goodSkuTv;//　编码

    private TextView barCodeTv;// 条码

    private TextView tvSalePrice;// 价格

    private TextView backPointTv;// 退货积分

    private View selectedParamsLayout;// 规格

    private TextView selectedParamsTv;

    private TextView goodsCountTv;// 数量

    private CountEditText countEditText;

    private TextView goodsUnitTv;// 单位

    private Spinner reasonTv;// 退货原因

    private EditText describeTv;//问题描述

    private ArrayList<String> imageUrls = new ArrayList<String>();

    private int reasonIndex = -1;

    private String reasonName = "";

    private LinearLayout selectorReason;

    private TextView imgAdd;

    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;

    private HashMap<String, TImage> imgMap = new HashMap<>();  //映射上传服务器图片和本地图片

    private int countdown = 1; //循环请求上传图片等待框计数

    private HashMap<String, SaleBackCart.SaleBackCartBean> backMap = new HashMap<String, SaleBackCart.SaleBackCartBean>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rg_info;
    }

    @Override
    protected void initViews() {
        super.initViews();

        TextView titleTv = (TextView) findViewById(R.id.title_tv);
        titleTv.setText("退货商品详情");
        emptyView = (EmptyView) findViewById(R.id.emptyview);
        submitGoodsTv = (TextView) findViewById(tv_submit_goods);
        picsViewPager = (ViewPager) findViewById(R.id.pics_view_pager);
        indicatorTv = (TextView) findViewById(R.id.pager_indicator_tv);
        productNameTv = (TextView) findViewById(R.id.goods_title);
        goodSkuTv = (TextView) findViewById(R.id.tv_goods_sku);
        barCodeTv = (TextView) findViewById(R.id.tv_bar_code);
        tvSalePrice = (TextView) findViewById(R.id.tv_delivery_price);
        backPointTv = (TextView) findViewById(R.id.tv_back_point);
        selectedParamsLayout = findViewById(R.id.selected_sku_layout);
        selectedParamsTv = (TextView) findViewById(R.id.selected_params_tv);
        goodsCountTv = (TextView) findViewById(R.id.tv_goods_count);
        countEditText = (CountEditText) findViewById(R.id.count_edit_text);
        countEditText.setMaxCount(GlobelDefines.MAX_BACK_COUNT);
        goodsUnitTv = (TextView) findViewById(R.id.tv_goods_unit);
        selectorReason = (LinearLayout) findViewById(R.id.ll_reason_selector);
        reasonTv = (Spinner) findViewById(R.id.sp_reason);
        describeTv = (EditText) findViewById(R.id.tv_describe);
        imgAdd = (TextView) findViewById(R.id.im_add);
        imageView1 = (ImageView) findViewById(R.id.imgShow1);
        imageView2 = (ImageView) findViewById(R.id.imgShow2);
        imageView3 = (ImageView) findViewById(R.id.imgShow3);

        setMaxPictureNum(3);
    }

    @Override
    protected void initEvent() {
        submitGoodsTv.setOnClickListener(this);
        selectedParamsLayout.setOnClickListener(this);
        selectorReason.setOnClickListener(this);
        imgAdd.setOnClickListener(this);
        reasonTv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] reasonArr = getResources().getStringArray(R.array.selector_back_reason);

                switch (position) {  //商品临期 01 商品破损 02 到货差异 03 质量问题 04 其他原因 99
                    case 0://商品临期 01
                        reasonIndex = 01;
                        reasonName = reasonArr[0];
                        break;

                    case 1://商品破损 02
                        reasonIndex = 02;
                        reasonName = reasonArr[1];
                        break;

                    case 2://到货差异 03
                        reasonIndex = 03;
                        reasonName = reasonArr[2];
                        break;

                    case 3://质量问题 04
                        reasonIndex = 04;
                        reasonName = reasonArr[3];
                        break;

                    case 4://其他原因 99
                        reasonIndex = 99;
                        reasonName = reasonArr[4];
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void initData() {
        productReturn = (WProductExt) getIntent().getSerializableExtra("product");
        backMap = (HashMap<String, SaleBackCart.SaleBackCartBean>) getIntent().getSerializableExtra("GOODS");
        if (productReturn != null && backMap != null){
            if (backMap.get(productReturn.getProductId()) != null){
                submitGoodsTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_rount_corner_rectangle));
            }
        }
        requestProductDetail();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case tv_submit_goods: {//提交到退货车
                submitSaleBackCart();
                break;
            }

            case R.id.selected_sku_layout: {
                Intent intent = new Intent(this, ProductSkuActivity.class);
                intent.putExtra("product_details", productDetailsData);
                startActivityForResult(intent, GlobelDefines.REQ_CODE_PRODUCT_DETAIL);
                break;
            }

            case R.id.im_add: {// 添加图片
                showPopWindow(1);
                break;
            }

            default:
                break;
        }
    }

    /**
     * 提交到退货车
     */
    private void submitSaleBackCart() {
        showProgressDialog();
        UserInfo userInfo = FrxsApplication.getInstance().getUserInfo();
        final Product product = curProductInfo.getProduct();
        SaleBackCartInfo saleBackCartInfo = new SaleBackCartInfo();
        saleBackCartInfo.setWID(getWID());
        saleBackCartInfo.setShopID(getShopID());
        saleBackCartInfo.setProductID(product.getProductId());
        saleBackCartInfo.setQty(countEditText.getCount());
        saleBackCartInfo.setBackReasonCode(reasonIndex);// 退货原因对应码
        saleBackCartInfo.setBackReasonName(reasonName);// 退货名字
        saleBackCartInfo.setBackReasonDes(describeTv.getText().toString().trim());

        /**
         * 上传图片（最多三张）
         */
        List<UrlList> list = new ArrayList<UrlList>();
        for (int i = 0; i < imgPath.size(); i++) {
            UrlList urlList = new UrlList();
            urlList.setApplyBackCartID("");
            urlList.setAttUrl(imgPath.get(i));
            list.add(urlList);
        }
        saleBackCartInfo.setUrlList(list);
        PostEditBackCart postEditBackCart = new PostEditBackCart();
        postEditBackCart.setEditType(0);
        postEditBackCart.setShopId(getShopID());
        postEditBackCart.setUserId(getUserID());
        postEditBackCart.setUserName(userInfo.getUserName());
        postEditBackCart.setWID(getWID());
        postEditBackCart.setSaleBackCart(saleBackCartInfo);

        getService().SaleBackCartEditSingle(postEditBackCart).enqueue(new SimpleCallback<ApiResponse<Objects>>() {

            @Override
            public void onResponse(ApiResponse<Objects> result, int code, String msg) {
                dismissProgressDialog();
                if (result.getFlag().equals("0")) {
                    ToastUtils.show(BackGoodsInfoActivity.this, "提交成功！");
                    SaleBackCart.SaleBackCartBean goodBack = new SaleBackCart.SaleBackCartBean();
                    goodBack.setProductID(product.getProductId());
                    if (backMap == null){
                        backMap = new HashMap<String, SaleBackCart.SaleBackCartBean>();
                    }
                    backMap.put(product.getProductId(), goodBack);
                    setResult(GlobelDefines.RESULT_BACK_SKU, getIntent().putExtra("GOODS", backMap));
                    finish();

                } else {
                    ToastUtils.show(BackGoodsInfoActivity.this, result.getInfo());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                dismissProgressDialog();
                ToastUtils.show(BackGoodsInfoActivity.this, t.getMessage());
            }
        });
    }

    /**
     * 获取商品详情
     */
    private void requestProductDetail() {
        showProgressDialog();
        AjaxParams params = new AjaxParams();
        params.put("WID", getWID());
        params.put("ShopID", getShopID());
        params.put("ProductId", productReturn.getProductId());

        getService().WProductsB2BDetailsGet(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<ProductWProductsDetailsGetRespData>>() {
            @Override
            public void onResponse(ApiResponse<ProductWProductsDetailsGetRespData> result, int code, String msg) {
                dismissProgressDialog();
                ProductWProductsDetailsGetRespData respData = result.getData();
                if (null != respData) {
                    productDetailsData = respData;
                    curProductInfo = respData.getCurrentProduct();
                    if (null != curProductInfo) {
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

                ImageView img = new ImageView(BackGoodsInfoActivity.this);
                img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                img.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(BackGoodsInfoActivity.this).load(imageUrls.get(position)).into(img);

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
        double deliveryPrice = wProduct.getSalePrice() * (1 + wProduct.getShopAddPerc());
        tvSalePrice.setText("￥" + MathUtils.twolittercountString(deliveryPrice) + "/" + wProduct.getUnit());
        //退货积分（单位积分）
        if (wProduct.getShopPoint() > 0) {
            backPointTv.setVisibility(View.VISIBLE);
            backPointTv.setText("退货积分：-" + MathUtils.twolittercountString(wProduct.getShopPoint()) + "/" + wProduct.getUnit());
        }
        //商品名称
        productNameTv.setText(curProductInfo.getProduct().getProductName());
        /*if (!TextUtils.isEmpty(curProductInfo.getWProduct().getProductName2())) {
            productSubName.setVisibility(View.VISIBLE);
            productSubName.setText(curProductInfo.getWProduct().getProductName2());
        } else {
            productSubName.setVisibility(View.GONE);
        }*/

        // 商品编码
        Product product = curProductInfo.getProduct();
        if (product != null) {
            String sku = product.getSKU();
            if (!TextUtils.isEmpty(sku)) {
                goodSkuTv.setText("编码：" + sku);
            } else {
                goodSkuTv.setText("编码：暂无");
            }
        } else {
            goodSkuTv.setText("编码：暂无");
        }

        //国际条码
        if (curProductInfo.getProductsBarCodes().size() > 0) {
            String strBarCode = curProductInfo.getProductsBarCodes().get(0).getBarCode();
            if (!TextUtils.isEmpty(strBarCode)) {
                barCodeTv.setText("条码：" + strBarCode);
            } else {
                barCodeTv.setText("条码：暂无");
            }
        } else {
            barCodeTv.setText("条码：暂无");
        }
    }

    private void initEmptyView(int mode) {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setMode(mode);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackGoodsInfoActivity.this.showProgressDialog();
                requestProductDetail();
            }
        });
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        if (result != null) {
            ArrayList<TImage> images = result.getImages();
            if (images != null) {
                showProgressDialog();
                countdown = images.size();
                for (int i = 0; i < images.size(); i++) {
                    final TImage image = images.get(i);
                    subBackImg(image, new RequestListener() {
                        @Override
                        public void handleRequestResponse(ApiResponse result) {
                            if (result.getFlag().equals("SUCCESS")){
                                if (result.getData() != null) {
                                    String imgUrl = ((ShopImgPath)result.getData()).getImgPath();
                                    if (imgPath.size() >= getMaxPictureNum()) {
                                        imgMap.remove(imgPath.get(0));
                                        imgPath.remove(0);
                                    }
                                    imgPath.add(imgUrl);
                                    imgMap.put(imgUrl, image);
                                    for (int i = 0; i < imgPath.size(); i++ ) {
                                        ImageView imageView = null;
                                        if (i == 0) {
                                            imageView = imageView1;
                                        } else if (i == 1) {
                                            imageView = imageView2;
                                        } else {
                                            imageView = imageView3;
                                        }
                                        ImageLoader.loadImage(BackGoodsInfoActivity.this, imgMap.get(imgPath.get(i)).getCompressPath(), imageView);
                                    }
                                }
                            }

                            countdown -= 1;
                            if (countdown <= 0) {
                                dismissProgressDialog();
                            }
                        }

                        @Override
                        public void handleExceptionResponse(String errMsg) {

                        }
                    });
                }
            }
        }
    }

}

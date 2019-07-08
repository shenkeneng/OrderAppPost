package com.frxs.order;

import android.content.Intent;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ewu.core.utils.MathUtils;
import com.ewu.core.widget.NoScrollListView;
import com.ewu.core.widget.flowlayout.FlowLayout;
import com.ewu.core.widget.flowlayout.TagAdapter;
import com.ewu.core.widget.flowlayout.TagFlowLayout;
import com.frxs.order.comms.GlobelDefines;
import com.frxs.order.model.Attribute;
import com.frxs.order.model.AttributeValue;
import com.frxs.order.model.ProductAttribute;
import com.frxs.order.model.ProductInfo;
import com.frxs.order.model.ProductWProductsDetailsGetRespData;
import com.frxs.order.model.WProduct;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;

import java.util.List;
import java.util.Set;

/**
 * Created by ewu on 2016/5/5.
 */
public class ProductSkuActivity extends BaseDialogActivity{

    private NoScrollListView goodsParamsLv;

    private ProductWProductsDetailsGetRespData productDetailsData;

    private SparseArray<Integer> mParams = new SparseArray<Integer>();

    private ProductInfo curProductInfo;

    private TextView goodsTitle;

    private TextView selectedParamsTv;

    private Button confirmBtn;

//    private TextView platformFeeTv;

    private TextView productPointsTv;

    private TextView salePriceTv;

    private ImageView productImgIv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_sku;
    }

    @Override
    protected void initViews() {
        goodsParamsLv = (NoScrollListView) findViewById(R.id.good_param_listview);
        goodsTitle = (TextView) findViewById(R.id.tv_goods_describe);
        selectedParamsTv = (TextView) findViewById(R.id.selected_params_tv);
//        platformFeeTv = (TextView) findViewById(R.id.tv_platform_rate);
        salePriceTv = (TextView) findViewById(R.id.tv_goods_price);
        productPointsTv = (TextView) findViewById(R.id.tv_goods_integral);
        productImgIv = (ImageView) findViewById(R.id.img_goods);
        confirmBtn = (Button) findViewById(R.id.confirm_btn);
    }

    @Override
    protected void initEvent() {
        confirmBtn.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        if (null != intent)
        {
            productDetailsData = (ProductWProductsDetailsGetRespData) intent.getSerializableExtra("product_details");
            curProductInfo = productDetailsData.getCurrentProduct();
            initSelectedParams();
            initProductUI(curProductInfo);

            goodsParamsLv.setAdapter(new Adapter<Attribute>(this, productDetailsData.getAttributes(), R.layout.item_sku_plus) {
                @Override
                protected void convert(AdapterHelper helper, final Attribute item) {
                    helper.setText(R.id.good_standard, item.getAttributeName());
                    final TagFlowLayout skuFlowLayout = helper.getView(R.id.sku_flowlayout);
                    skuFlowLayout.setAdapter(new TagAdapter<AttributeValue>(item.getValues()) {
                        @Override
                        public View getView(FlowLayout parent, int position, AttributeValue item) {
                            TextView tagView = (TextView) LayoutInflater.from(ProductSkuActivity.this).inflate(R.layout.item_flow_view,
                                    skuFlowLayout, false);
                            tagView.setText(item.getValueStr());
                            return tagView;
                        }

                        @Override
                        public boolean setSelected(int position, AttributeValue attrValue) {
                            int valueId = mParams.get(item.getAttributeId());
                            return valueId == attrValue.getValuesId();
                        }
                    });

                    skuFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
                        @Override
                        public void onSelected(Set<Integer> selectPosSet) {
                            Object[] test = selectPosSet.toArray();
                            if (test.length > 0) {
                                AttributeValue selectedItem = item.getValues().get((Integer) selectPosSet.toArray()[0]);
                                mParams.put(item.getAttributeId(), selectedItem.getValuesId());

                                ProductInfo selectedProductInfo = getSelectedProduct();
                                if (null != selectedProductInfo)
                                {
                                    curProductInfo = selectedProductInfo;
                                    confirmBtn.setEnabled(true);
                                }
                                else
                                {
                                    confirmBtn.setEnabled(false);
                                }

                                initProductUI(selectedProductInfo);
                            }
                        }
                    });
                }
            });
        }

    }

    private void initSelectedParams()
    {
        List<ProductAttribute>  productAttributeList = curProductInfo.getProductAttributes();
        if (null != productAttributeList)
        {
            for (ProductAttribute proAttributeDetail : productAttributeList)
            {
                mParams.put(proAttributeDetail.getAttributeId(), proAttributeDetail.getValuesId());
            }
        }
    }

    private void initProductUI(ProductInfo productInfo)
    {
        if (null != productInfo) {
            goodsTitle.setText(productInfo.getProduct().getProductName());

            Glide.with(this).load(productInfo.getAttributePicture().getImageUrl200x200()).into(productImgIv);

            WProduct wProduct = productInfo.getWProduct();
            salePriceTv.setText("¥" + MathUtils.twolittercountString(wProduct.getSalePrice() * wProduct.getBigPackingQty()));
            // 积分
            if (wProduct.getShopPoint() > 0) {
                productPointsTv.setVisibility(View.VISIBLE);
                productPointsTv.setText(String.format(getResources().getString(R.string.points), MathUtils.mul(wProduct.getShopPoint(),wProduct.getBigPackingQty())));
            }else
            {
                productPointsTv.setVisibility(View.GONE);
            }
        }

        List<ProductAttribute> productAttributeList = curProductInfo.getProductAttributes();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < productAttributeList.size(); i++) {
            sb.append("\"" + productAttributeList.get(i).getValueStr() + "\"");
        }

        selectedParamsTv.setText(sb.toString());
    }

    private ProductInfo getSelectedProduct()
    {
        List<ProductInfo> subProductList = productDetailsData.getSubProducts();
        for (ProductInfo product : subProductList)
        {
            boolean missMatch = true;
            List<ProductAttribute> attributes = product.getProductAttributes();
            if (null != attributes)
            {
                for (ProductAttribute attributeValue : attributes)
                {
                    try
                    {
                        int attrValue = mParams.get(attributeValue.getAttributeId());
                        if (attrValue != attributeValue.getValuesId())
                        {
                            missMatch = false;
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        missMatch = false;
                    }

                }

                if (missMatch)
                {
                    return product;
                }
            }
        }

        return null;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.confirm_btn:
            {
                ProductInfo selectedProductInfo = getSelectedProduct();
                Intent intent = new Intent();
                intent.putExtra("selected_subproduct", selectedProductInfo);
                setResult(GlobelDefines.RESULT_CODE_SKU, intent);
                finish();
                break;
            }
            default:
                break;
        }
    }
}

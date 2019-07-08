package com.frxs.order.fragment;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.ewu.core.utils.MathUtils;
import com.ewu.core.utils.ToastUtils;
import com.frxs.order.ProductDetailActivity;
import com.frxs.order.R;
import com.frxs.order.adapter.PinnedSectionProductListAdapter;
import com.frxs.order.application.FrxsApplication;
import com.frxs.order.listener.MyOnTouchListener;
import com.frxs.order.model.PostEditCart;
import com.frxs.order.model.PostInfo;
import com.frxs.order.model.ProductWProductsGetToB2BRespData;
import com.frxs.order.model.SectionListItem;
import com.frxs.order.model.ShopCategories;
import com.frxs.order.model.ShopInfo;
import com.frxs.order.model.WProductExt;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.frxs.order.utils.DensityUtils;
import com.frxs.order.widget.CountEditText;
import com.frxs.order.widget.MyListView;
import com.pacific.adapter.AdapterHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit2.Call;

/**
 * 订购商品二三级级分类 By Tiepier
 */
public class SubCategoryNewFragment extends MaterialStyleFragment implements MyOnTouchListener {

    private MyListView lvSubCategory;
    //    private Adapter<ShopCategories> quickAdapter;
    private PinnedSectionProductListAdapter pinnedSecListAdapter;
    //    private ShopCategories currentCategory; //表示当前二级目录
    private List<ShopCategories> mCategoryList;
    private List<SectionListItem> sectionList = new ArrayList<SectionListItem>();
    private HashMap<String, Integer> childIndexer = new HashMap<String, Integer>();

    private int mCurrentCatePosition = -1; //当前商品分类的位置

    private int mCurrentSubCatePosition = -1; //次级商品分类的位置

    private CategoryNewFragment mParentFragment;

    private int mPageIndex = 1;

    private final int mPageSize = Integer.MAX_VALUE;

    private boolean mLoadReady = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sub_new_category;
    }

    @Override
    public int getPtrFrameLayoutId() {
        return R.id.goods_ptr_frame_ll;
    }

    public void notifyDataSetChanged() {
        if (null != pinnedSecListAdapter) {
            pinnedSecListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        notifyDataSetChanged();
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        mParentFragment = (CategoryNewFragment) this.getParentFragment();

        lvSubCategory = (MyListView) view.findViewById(R.id.sub_category_list_view);
    }

    @Override
    protected void initEvent() {
        lvSubCategory.setMyTouchListener(this);

        mPtrFrameLayout.setPinContent(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPageIndex = 1;
                mCurrentSubCatePosition = 0;
                requestProductList();
            }
        });

        lvSubCategory.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (pinnedSecListAdapter.getCount() > 0) {
                    SectionListItem sectionItem = (SectionListItem) pinnedSecListAdapter.getItem(firstVisibleItem);
                    mParentFragment.setSelectedChildCategory(mCurrentCatePosition, sectionItem.getSectionId());
                }
            }
        });
    }

    @Override
    protected void initData() {
//        quickAdapter = new Adapter<ShopCategories>(mActivity, R.layout.item_sub_category) {
//            @Override
//            protected void convert(AdapterHelper helper, final ShopCategories item) {
//                helper.setText(R.id.tv_sub_category, item.getCategoryName());
//                helper.setOnClickListener(R.id.tv_sub_category, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(mActivity, ProductListActivity.class);
//                        intent.putExtra("CategoryId1", item.getParentCategoryId());
//                        intent.putExtra("CategoryId2", item.getCategoryId());
//                        mActivity.startActivity(intent);
//                    }
//                });
//
//                List<ShopCategories> subCategoryList = item.getSubCategoriesList();
//
//                NoScrollGridView subCateGridView = helper.getView(R.id.gridView);
//                helper.setAdapter(R.id.gridView, new Adapter<ShopCategories>(mActivity, R.layout.item_sub_category_sub, subCategoryList) {
//                    @Override
//                    protected void convert(AdapterHelper helper, ShopCategories subItem) {
//                        helper.setText(R.id.tv_sub_category, subItem.getCategoryName());
//                    }
//                });
//                subCateGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        /*
//                         * 此处position的位置不是从0开始的，如果Head添加了一个View从1开始，如果Head添加了两个View,则从2开始
//                         * ，依次类推
//                         * 遇到这种问题，我们直接采用parent.getAdapter().getItem(position)获得被点击Item对象
//                         */
//                        ShopCategories categories = (ShopCategories) parent.getAdapter().getItem(position);
//                        Intent intent = new Intent(mActivity, ProductListActivity.class);
//                        intent.putExtra("CategoryId1", item.getParentCategoryId());
//                        intent.putExtra("CategoryId2", categories.getParentCategoryId());
//                        intent.putExtra("CategoryId3", categories.getCategoryId());
//                        mActivity.startActivity(intent);
//                    }
//                });
//            }
//        };
        pinnedSecListAdapter = new PinnedSectionProductListAdapter(mActivity, R.layout.item_product) {
            @Override
            protected void convert(AdapterHelper helper, SectionListItem item) {
                final WProductExt productExt = (WProductExt) item.getItem();
                if (item.type == SectionListItem.SECTION) {
                    helper.setText(R.id.subcate_tv, item.getSection());
                    helper.setVisible(R.id.subcate_tv, View.VISIBLE);
                    helper.setVisible(R.id.goods_info_layout, View.GONE);
                } else {
                    final TextView tvGoodsBuy = helper.getView(R.id.tv_goods_buy);
                    helper.setVisible(R.id.subcate_tv, View.GONE);
                    //商品名称（0：有库存；1：无库存）
                    if (productExt.getIsNoStock() == 0) {
                        helper.setText(R.id.tv_goods_title, productExt.getProductName());
                    } else if (productExt.getIsNoStock() == 1){
                        helper.setText(R.id.tv_goods_title, Html.fromHtml(getResources().getString(R.string.pre_good_tag) + productExt.getProductName()));
                    }
                    //商品图片处理
                    if (!TextUtils.isEmpty(productExt.getImageUrl200x200())) {
                        helper.setImageUrl(R.id.goods_img, productExt.getImageUrl200x200());//商品图片
                    }
                    //促销商品业务处理（0：非促销商品；1：促销商品）
                    if (productExt.getIsGift() == 1 || productExt.getProPoints() > 0) {
                        helper.setVisible(R.id.tv_good_discount, View.VISIBLE);
                    } else {
                        helper.setVisible(R.id.tv_good_discount, View.GONE);
                    }
                    //起订量业务处理
                    TextView tvTipsQty = helper.getView(R.id.tv_tips_preqty);
                    if (productExt.getMinPreQty() != null && productExt.getMaxaPreQty() != null) {
                        tvTipsQty.setVisibility(View.VISIBLE);
                        if (productExt.getMinPreQty() == 0) {
                            String strMaxQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(productExt.getMaxaPreQty()));//最大起订量
                            tvTipsQty.setText("最大订购量：" + strMaxQty);
                        } else if (productExt.getMaxaPreQty() == 0) {
                            String strMinQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(productExt.getMinPreQty()));//最小起订量
                            tvTipsQty.setText("最小起订量：" + strMinQty);
                        }
                        else if (productExt.getMinPreQty() == 0 && productExt.getMaxaPreQty() == 0) {
                            tvTipsQty.setVisibility(View.VISIBLE);
                        }else {
                            String strMinQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(productExt.getMinPreQty()));//最小起订量
                            String strMaxQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(productExt.getMaxaPreQty()));//最大起订量
                            tvTipsQty.setText(strMinQty + "≤订购量≤" + strMaxQty);
                        }
                    } else if (productExt.getMinPreQty() != null && productExt.getMaxaPreQty() == null) {
                        String strMinQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(productExt.getMinPreQty()));//最小起订量
                        tvTipsQty.setText("最小起订量：" + strMinQty);
                    } else if (productExt.getMaxaPreQty() != null && productExt.getMinPreQty() == null) {
                        String strMaxQty = DensityUtils.subZeroAndDot(MathUtils.twolittercountString(productExt.getMaxaPreQty()));//最大起订量
                        tvTipsQty.setText("最大订购量：" + strMaxQty);
                    } else {
                        tvTipsQty.setVisibility(View.GONE);
                    }

                    //商品冻结业务处理
                    if (productExt.getWStatus() == 3) {
                        helper.setVisible(R.id.sold_out_img, View.VISIBLE);
                        tvGoodsBuy.setBackgroundResource(R.mipmap.add_disable_btn);
                        tvGoodsBuy.setEnabled(false);
                    } else {
                        helper.setVisible(R.id.sold_out_img, View.GONE);
                        tvGoodsBuy.setBackgroundResource(R.mipmap.add_cart_circle_btn);
                        tvGoodsBuy.setEnabled(true);
                    }

                    double count = FrxsApplication.getInstance().getSaleCartProductCount(productExt.getProductId());
                    if (count > 0) {
                        tvGoodsBuy.setText(MathUtils.doubleTrans(count));
                    } else {
                        tvGoodsBuy.setText("");
                    }

                    helper.setText(R.id.tv_goods_code, "编码:" + productExt.getSKU());//商品编码
                    helper.setText(R.id.package_value_tv, "1x" + productExt.getBigPackingQty() + productExt.getUnit());//商品包装规格
                    //商品积分处理
                    if (productExt.getBigShopPoint() > 0) {
                        helper.setVisible(R.id.tv_goods_integral, View.VISIBLE);
                        helper.setText(R.id.tv_goods_integral, String.format(getResources().getString(R.string.points), productExt.getBigShopPoint()));
                    } else {
                        helper.setVisible(R.id.tv_goods_integral, View.GONE);

                    }
                    double deliveryPrice = productExt.getBigSalePrice()*(1+productExt.getShopAddPerc());
                    helper.setText(R.id.tv_goods_price, MathUtils.twolittercountString(deliveryPrice) + "/" + productExt.getBigUnit());//商品价格/单位
                    final CountEditText countEditText = helper.getView(R.id.count_edit_text);//商品加减
                    countEditText.setCount(1);
                    //商品购买
                    tvGoodsBuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            reqBuyGoods(productExt, countEditText.getCount(), tvGoodsBuy);
                        }
                    });
                    //商品详情
                    helper.setOnClickListener(R.id.ll_goods_info, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                            intent.putExtra("product", productExt);
                            mActivity.startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public int getLayoutResId(int viewType) {
                return R.layout.item_product;
            }
        };

        lvSubCategory.setAdapter(pinnedSecListAdapter);
    }

    /**
     * 商品购买
     */
    private void reqBuyGoods(final WProductExt ext, final int addCount, final TextView tvGoodsBuy) {
        mActivity.showProgressDialog();
        // 处理单次购买商品数据
        ShopInfo info = FrxsApplication.getInstance().getmCurrentShopInfo();
        PostInfo postInfo = new PostInfo();
        postInfo.setProductID(ext.getProductId());
        postInfo.setPreQty(addCount);
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
                mActivity.dismissProgressDialog();
                if (result != null) {
                    if (result.getFlag().equals("0")) {
                        ToastUtils.show(mActivity, "加入购物车成功");
                        FrxsApplication.getInstance().addShopCartCount(addCount);
                        FrxsApplication.getInstance().updateSaleCartProduct(ext.getProductId(), addCount);
                        double count = FrxsApplication.getInstance().getSaleCartProductCount(ext.getProductId());
                        //容错处理
                        if (count <= 0) {
                            count = addCount;
                        }
                        tvGoodsBuy.setText(MathUtils.doubleTrans(count));
                    } else {
                        ToastUtils.show(mActivity, result.getInfo());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                super.onFailure(call, t);
                mActivity.dismissProgressDialog();
            }
        });
    }

    public void setPosition(int groupPosition, int childPosition) {
        int oldCatePosition = mCurrentCatePosition;
        mCurrentCatePosition = groupPosition;
        mCurrentSubCatePosition = childPosition;

        if (oldCatePosition != groupPosition) {
            mActivity.showProgressDialog();
            requestProductList();
        } else {
            //点击三级目录，商品列表listview跳转到点击的三级目录商品分类
            String childCategoryName = mCategoryList.get(groupPosition).getSubCategoriesList().get(childPosition).getCategoryName();
            if (!TextUtils.isEmpty(childCategoryName)) {
                int position = childIndexer.get(childCategoryName) == null ? 0 : childIndexer.get(childCategoryName);
                lvSubCategory.setSelection(position);
            }
        }
    }

    public void setData(List<ShopCategories> categories) {
        mCategoryList = categories;

        if (null != categories) {
            setPosition(0, 0);
        }
    }

    private void requestProductList() {
        mLoadReady = false;

        AjaxParams params = new AjaxParams();
        params.put("UserID", getUserID());
        params.put("WID", getWID());
        params.put("CategoryId1", mCategoryList.get(mCurrentCatePosition).getParentCategoryId());
        params.put("CategoryId2", mCategoryList.get(mCurrentCatePosition).getCategoryId());
        params.put("ShopID", getShopID());
        params.put("PageIndex", mPageIndex);
        params.put("PageSize", mPageSize);

        getService().ProductWProductsGetToB2B(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<ProductWProductsGetToB2BRespData>>() {
            @Override
            public void onResponse(ApiResponse<ProductWProductsGetToB2BRespData> result, int code, String msg) {
                refreshComplete();
                mLoadReady = true;

                mActivity.dismissProgressDialog();
                ProductWProductsGetToB2BRespData respData = result.getData();
                if (null != respData) {
                    List<WProductExt> goodsList = respData.getItemList();
                    if (null != goodsList && goodsList.size() > 0) {
                        addSectionListItem(goodsList);

                        pinnedSecListAdapter.replaceAll(sectionList);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ProductWProductsGetToB2BRespData>> call, Throwable t) {
                super.onFailure(call, t);
                refreshComplete();
                mLoadReady = true;

                mActivity.dismissProgressDialog();
            }
        });
    }

    private void addSectionListItem(List<WProductExt> productList) {
        sectionList.clear();
        childIndexer.clear();

        //  对接口返回的数据按照三级目录进行排序
        TreeMap<String, ArrayList> cateProdcutMap = sort((ArrayList) productList);
        ArrayList<WProductExt> sortList = new ArrayList<WProductExt>();
        for (ShopCategories item : mCategoryList.get(mCurrentCatePosition).getSubCategoriesList()) {
            List<WProductExt> products = cateProdcutMap.get(item.getCategoryId());
            if (null != products && products.size() > 0) {
                sortList.addAll(products);
            }
        }

        for (int i = 0; i < sortList.size(); i++) {
            WProductExt item = sortList.get(i);
            int sectionPosition = 0;
            int listPosition = 0;

            int currentSectionId = item.getCategoryId3();
            int previewSectionId = sectionList.size() > 0 ? sectionList.get(sectionList.size() - 1).getSectionId() : -1;
            if (previewSectionId != currentSectionId) {
                SectionListItem firstSectionItem = new SectionListItem(item, SectionListItem.SECTION, item.getCategoryName3(),
                        currentSectionId);
                firstSectionItem.sectionPosition = sectionPosition;
                firstSectionItem.listPosition = listPosition++;
                childIndexer.put(item.getCategoryName3(), i + childIndexer.size());
                sectionList.add(firstSectionItem);

                sectionPosition++;
            }

            SectionListItem listItem = new SectionListItem(item, SectionListItem.ITEM, item.getCategoryName3(), currentSectionId);
            listItem.sectionPosition = sectionPosition;
            listItem.listPosition = listPosition++;
            sectionList.add(listItem);
        }
    }

    public TreeMap<String, ArrayList> sort(ArrayList list) {
        TreeMap tm = new TreeMap();
        for (int i = 0; i < list.size(); i++) {
            WProductExt item = (WProductExt) list.get(i);
            if (tm.containsKey(item.getCategoryId3())) {//
                ArrayList productList = (ArrayList) tm.get(item.getCategoryId3());
                productList.add(item);
            } else {
                ArrayList tem = new ArrayList();
                tem.add(item);
                tm.put(item.getCategoryId3(), tem);
            }

        }
        return tm;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void pullupFromBottomEvent(MotionEvent event) {
        Log.e("WMTest", "pullupFromBottomEvent");
        if (mLoadReady) {
            if (mCurrentCatePosition < mCategoryList.size() - 1) {
                int groupPosition = mCurrentCatePosition + 1;
                setPosition(groupPosition, 0);
                mParentFragment.switchGroupItemUI(groupPosition);
            }
        }
    }

    @Override
    public void pulldownFromTopEvent(MotionEvent event) {

    }

    @Override
    public void scrollupEvent(MotionEvent event) {

    }

    @Override
    public void scrolldownEvent(MotionEvent event) {

    }
}

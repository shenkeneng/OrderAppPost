package com.frxs.order.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ewu.core.widget.EmptyView;
import com.frxs.order.CaptureActivity;
import com.frxs.order.FrxsActivity;
import com.frxs.order.ProductSearchActivity;
import com.frxs.order.R;
import com.frxs.order.model.ShopCategories;
import com.frxs.order.model.ShopCategoriesGetRespData;
import com.frxs.order.rest.model.AjaxParams;
import com.frxs.order.rest.model.ApiResponse;
import com.frxs.order.rest.service.SimpleCallback;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


/**
 * 订购商品一级分类 By Tiepier
 */
public class CategoryFragment extends FrxsFragment {
    private EmptyView emptyView;
    private ListView lvCategory;
    private Adapter<ShopCategories> quickAdapter;
    private List<ShopCategories> categoryList = new ArrayList<ShopCategories>();
    private SubCategoryFragment subCategoryFrag;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initViews(View view) {
        emptyView = (EmptyView) view.findViewById(R.id.emptyview);
        lvCategory = (ListView) view.findViewById(R.id.lv_first_category);
        TextView imgRight = (TextView) view.findViewById(R.id.right_btn);
        imgRight.setVisibility(View.GONE);
        view.findViewById(R.id.title_search).setOnClickListener(this);
        view.findViewById(R.id.left_btn).setOnClickListener(this);

        subCategoryFrag = (SubCategoryFragment) getChildFragmentManager().findFragmentById(R.id.contanier);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        quickAdapter = new Adapter<ShopCategories>(mActivity, R.layout.item_category) {
            @Override
            protected void convert(AdapterHelper helper, ShopCategories item) {
                helper.setText(R.id.tv_category, item.getCategoryName());
            }
        };
        lvCategory.setAdapter(quickAdapter);

        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lvCategory.smoothScrollToPosition(position);
                List<ShopCategories> subCategoryList = categoryList.get(position).getSubCategoriesList();
                subCategoryFrag.setData(subCategoryList);
            }
        });

        requestCategoryList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn: {
                Intent intent = new Intent(mActivity, CaptureActivity.class);
                ((FrxsActivity)mActivity).hasCameraPermissions(intent, false);
                //mActivity.startActivity(intent);
                break;
            }
            case R.id.title_search: {
                Intent intent = new Intent(mActivity, ProductSearchActivity.class);
                mActivity.startActivity(intent);
                break;
            }
        }

    }

    public void requestCategoryList() {
        AjaxParams params = new AjaxParams();
        params.put("WID", getWID());
        params.put("ShopID", getShopID());
        params.put("WarehouseId", getWID());

        getService().ShopCategoriesGet(params.getUrlParams()).enqueue(new SimpleCallback<ApiResponse<ShopCategoriesGetRespData>>() {
            @Override
            public void onResponse(ApiResponse<ShopCategoriesGetRespData> result, int code, String msg) {
                ShopCategoriesGetRespData resultData = result.getData();
                if (null != resultData)
                {
                    List<ShopCategories> tempList = resultData.getShopCategoriesList();
                    if (null != tempList && tempList.size() > 0)
                    {
                        for (ShopCategories item: tempList) {
                            if (0 == item.getDepth())
                            {
                                recursiveCategory(item, tempList);
                                categoryList.add(item);
                            }
                        }

                        if (categoryList.size() > 0) {
                            emptyView.setVisibility(View.GONE);
                            quickAdapter.replaceAll(categoryList);
                            lvCategory.setItemChecked(0, true);
                            subCategoryFrag.setData(categoryList.get(0).getSubCategoriesList());
                        }
                        else
                        {
                            initEmptyView(EmptyView.MODE_NODATA);
                        }
                    }
                }
                else
                {
                    initEmptyView(EmptyView.MODE_NODATA);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ShopCategoriesGetRespData>> call, Throwable t) {
                super.onFailure(call, t);
                initEmptyView(EmptyView.MODE_ERROR);
            }
        });

    }

    private void initEmptyView(int mode)
    {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setMode(mode);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.showProgressDialog();
                requestCategoryList();
            }
        });
    }


    private void recursiveCategory(ShopCategories category, List<ShopCategories> dataList)
    {
        List<ShopCategories> subCategoryList = getChildren(category.getCategoryId(), dataList);
        category.setSubCategoriesList(subCategoryList);
        if (!subCategoryList.isEmpty())
        {
            for (ShopCategories item : subCategoryList) {
                recursiveCategory(item, dataList);
            }
        }
    }

    private List<ShopCategories> getChildren(int categoryId, List<ShopCategories> dataList)
    {
        List<ShopCategories> children = new ArrayList<ShopCategories>();
        for (ShopCategories item : dataList ) {
            if (item.getParentCategoryId() == categoryId)
            {
                children.add(item);
            }
        }

        return children;
    }
}

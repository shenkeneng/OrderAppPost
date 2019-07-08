package com.frxs.order.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.ewu.core.utils.CommonUtils;
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
import com.pacific.adapter.ExpandableAdapter;
import com.pacific.adapter.ExpandableAdapterHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


/**
 * 订购商品一级分类 By Tiepier
 */
public class CategoryNewFragment extends FrxsFragment {
    private EmptyView emptyView;
    private ExpandableListView lvCategory;
//    private Adapter<ShopCategories> quickAdapter;
    private ExpandableAdapter<ShopCategories, ShopCategories> expandableAdapter;
    private List<ShopCategories> categoryList = new ArrayList<ShopCategories>();
    private SubCategoryNewFragment subCategoryFrag;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new_category;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && null != subCategoryFrag) {
            subCategoryFrag.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initViews(View view) {
        emptyView = (EmptyView) view.findViewById(R.id.emptyview);
        lvCategory = (ExpandableListView) view.findViewById(R.id.lv_first_category);
        TextView imgRight = (TextView) view.findViewById(R.id.right_btn);
        imgRight.setVisibility(View.GONE);
        view.findViewById(R.id.title_search).setOnClickListener(this);
        view.findViewById(R.id.left_btn).setOnClickListener(this);

        subCategoryFrag = (SubCategoryNewFragment) getChildFragmentManager().findFragmentById(R.id.contanier);
    }

    @Override
    protected void initEvent() {
        lvCategory.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < expandableAdapter.getGroupCount(); i++) {
                    if (groupPosition != i) {
                        lvCategory.collapseGroup(i);
                    }
                }
            }
        });

        lvCategory.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long id) {
                if (!lvCategory.isGroupExpanded(groupPosition)) {
                    switchGroupItemUI(groupPosition);

                    subCategoryFrag.setPosition(groupPosition, 0);
                }
                return true;
            }
        });

        lvCategory.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                if (CommonUtils.isFastDoubleClick()) {
                    return true;
                }

                switchChildItemUI(groupPosition, childPosition);
                expandableAdapter.notifyDataSetChanged();
                subCategoryFrag.setPosition(groupPosition, childPosition);
                return true;
            }
        });

//        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                lvCategory.smoothScrollToPosition(position);
//                List<ShopCategories> subCategoryList = categoryList.get(position).getSubCategoriesList();
//                subCategoryFrag.setData(subCategoryList);
//            }
//        });
    }

    @Override
    protected void initData() {
//        quickAdapter = new Adapter<ShopCategories>(mActivity, R.layout.item_category) {
//            @Override
//            protected void convert(AdapterHelper helper, ShopCategories item) {
//                helper.setText(R.id.tv_category, item.getCategoryName());
//            }
//        };
//        lvCategory.setAdapter(quickAdapter);

        expandableAdapter = new ExpandableAdapter<ShopCategories, ShopCategories>(mActivity, R.layout.item_category, R.layout.item_child_category) {
            @Override
            protected List<ShopCategories> getChildren(int groupPosition) {
                return get(groupPosition).getSubCategoriesList();
            }

            @Override
            protected void convertGroupView(boolean isExpanded, ExpandableAdapterHelper helper, ShopCategories item) {
                helper.setText(R.id.tv_category, item.getCategoryName());
            }

            @Override
            protected void convertChildView(boolean isLastChild, ExpandableAdapterHelper helper, ShopCategories item) {
                helper.setText(R.id.tv_category, item.getCategoryName());

                if (item.isSelected())
                {
                    helper.setTextColor(R.id.tv_category, Color.parseColor("#de251f"));
                    helper.setBackgroundRes(R.id.tv_category, R.drawable.icon_select);
                }
                else
                {
                    helper.setTextColor(R.id.tv_category, Color.parseColor("#323232"));
                    helper.setBackgroundColor(R.id.tv_category, Color.parseColor("#00000000"));
                }
            }
        };

        lvCategory.setGroupIndicator(null);
        lvCategory.setAdapter(expandableAdapter);

        requestCategoryList();
    }

    public void switchGroupItemUI(int groupPosition)
    {
        if (!lvCategory.isGroupExpanded(groupPosition)) {
            lvCategory.smoothScrollToPosition(groupPosition);
            lvCategory.setItemChecked(groupPosition, true);
            setSelectedChild(groupPosition, 0);
            lvCategory.expandGroup(groupPosition);
            expandableAdapter.notifyDataSetChanged();
        }
    }

    public void switchChildItemUI(int groupPosition, int childPosition)
    {
        setSelectedChild(groupPosition, childPosition);
    }

    public void setSelectedChild(int groupPosition, int childPosition) {
        int count = expandableAdapter.getChildrenCount(groupPosition);
        for (int i = 0; i < count; i++) {
            ShopCategories item = (ShopCategories) expandableAdapter.getChild(groupPosition, i);
            if (i == childPosition) {
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
        }
    }

    public void setSelectedChildCategory(int groupPosition, int childCategoryId) {
        List<ShopCategories> childCategoryList = categoryList.get(groupPosition).getSubCategoriesList();
        for (ShopCategories item : childCategoryList) {
            if (item.getCategoryId() == childCategoryId) {
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
        }

        expandableAdapter.notifyDataSetChanged();
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
                            if (1 == item.getDepth())
                            {
                                recursiveCategory(item, tempList);
                                categoryList.add(item);
                            }
                        }

                        if (categoryList.size() > 0) {
                            emptyView.setVisibility(View.GONE);
                            expandableAdapter.replaceAll(categoryList);
                            lvCategory.expandGroup(0);
                            setSelectedChild(0, 0);
                            lvCategory.setItemChecked(0, true);
                            subCategoryFrag.setData(categoryList);
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

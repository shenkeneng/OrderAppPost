package com.frxs.order.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ewu.core.widget.NoScrollGridView;
import com.frxs.order.ProductListActivity;
import com.frxs.order.R;
import com.frxs.order.model.ShopCategories;
import com.pacific.adapter.Adapter;
import com.pacific.adapter.AdapterHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 订购商品二三级级分类 By Tiepier
 */
public class SubCategoryFragment extends FrxsFragment {

    private ListView lvSubCategory;
    private Adapter<ShopCategories> quickAdapter;
    private NoScrollGridView mPopGrid;
    private List<ShopCategories> shopCategories = new ArrayList<ShopCategories>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sub_category;
    }

    @Override
    protected void initViews(View view) {
        lvSubCategory = (ListView) view.findViewById(R.id.sub_category_list_view);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        quickAdapter = new Adapter<ShopCategories>(mActivity, R.layout.item_sub_category) {
            @Override
            protected void convert(AdapterHelper helper, final ShopCategories item) {
                helper.setText(R.id.tv_sub_category, item.getCategoryName());
                helper.setOnClickListener(R.id.tv_sub_category, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, ProductListActivity.class);
                        intent.putExtra("CategoryId1", item.getParentCategoryId());
                        intent.putExtra("CategoryId2", item.getCategoryId());
                        mActivity.startActivity(intent);
                    }
                });

                List<ShopCategories> subCategoryList = item.getSubCategoriesList();

                NoScrollGridView subCateGridView = helper.getView(R.id.gridView);
                helper.setAdapter(R.id.gridView, new Adapter<ShopCategories>(mActivity, subCategoryList, R.layout.item_sub_category_sub) {
                    @Override
                    protected void convert(AdapterHelper helper, ShopCategories subItem) {
                        helper.setText(R.id.tv_sub_category, subItem.getCategoryName());
                    }
                });
                subCateGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        /*
                         * 此处position的位置不是从0开始的，如果Head添加了一个View从1开始，如果Head添加了两个View,则从2开始
                         * ，依次类推
                         * 遇到这种问题，我们直接采用parent.getAdapter().getItem(position)获得被点击Item对象
                         */
                        ShopCategories categories = (ShopCategories) parent.getAdapter().getItem(position);
                        Intent intent = new Intent(mActivity, ProductListActivity.class);
                        intent.putExtra("CategoryId1", item.getParentCategoryId());
                        intent.putExtra("CategoryId2", categories.getParentCategoryId());
                        intent.putExtra("CategoryId3", categories.getCategoryId());
                        mActivity.startActivity(intent);
                    }
                });
            }
        };
        lvSubCategory.setAdapter(quickAdapter);
    }

    public void setData(List<ShopCategories> categories)
    {
        shopCategories.clear();

        if (null != categories) {
            shopCategories.addAll(categories);
        }
        quickAdapter.replaceAll(shopCategories);
    }

    @Override
    public void onClick(View v) {

    }

}

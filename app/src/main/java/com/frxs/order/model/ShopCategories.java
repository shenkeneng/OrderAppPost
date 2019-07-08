package com.frxs.order.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ewu on 2016/5/9.
 */
public class ShopCategories implements Serializable {

    @SerializedName("CategoryId")
    private int CategoryId;

    @SerializedName("CategoryName")
    private String CategoryName;

    @SerializedName("ParentCategoryId")
    private int ParentCategoryId;

    @SerializedName("Depth")
    private int Depth;

    private List<ShopCategories> subCategoriesList; //次级别分类目录

    private boolean isSelected;

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public int getParentCategoryId() {
        return ParentCategoryId;
    }

    public void setParentCategoryId(int parentCategoryId) {
        ParentCategoryId = parentCategoryId;
    }

    public int getDepth() {
        return Depth;
    }

    public void setDepth(int depth) {
        Depth = depth;
    }

    public List<ShopCategories> getSubCategoriesList() {
        return subCategoriesList;
    }

    public void setSubCategoriesList(List<ShopCategories> subCategoriesList) {
        this.subCategoriesList = subCategoriesList;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

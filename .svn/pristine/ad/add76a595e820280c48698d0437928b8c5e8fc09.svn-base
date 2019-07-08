package com.frxs.order.utils;

import com.ewu.core.utils.SortListObjectUtil;
import com.frxs.order.model.CartGoodsDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chentie on 2017/5/11.
 */

public class ListToGroup {

    public static List<List<CartGoodsDetail>> subArray(List<CartGoodsDetail> sortArray) {
        try {
            // 将全部商品赋值给新的变量，不改变全部商品原来的排序。
            List<CartGoodsDetail> array = new ArrayList<CartGoodsDetail>();
            array.addAll(sortArray);
            // 根据商品一级分类序列化集合
            SortListObjectUtil.sort(array, "getCategoryName1", SortListObjectUtil.ORDER_BY_ASC);
            List<CartGoodsDetail> subArray = new ArrayList<CartGoodsDetail>();
            CartGoodsDetail obj = null;
            CartGoodsDetail obj2 = null;
            List<List<CartGoodsDetail>> aArray = new ArrayList<List<CartGoodsDetail>>();

            if (array.size() == 1) {
                obj = array.get(0);
                subArray.add(obj);
                aArray.add(subArray);
                return aArray;
            }

            for (int i = 0; i < array.size(); i++) {
                String currentStr = array.get(i).getCategoryName1();
                String previewStr = (i - 1) >= 0 ? array.get(i - 1).getCategoryName1() : " ";
                if (!previewStr.equals(currentStr)) {
                    subArray = new ArrayList<CartGoodsDetail>();
                    aArray.add(subArray);
                }

                subArray.add(array.get(i));
            }
            return aArray;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

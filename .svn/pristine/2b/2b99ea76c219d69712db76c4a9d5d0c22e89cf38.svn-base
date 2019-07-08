package com.frxs.order.webview;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.frxs.order.CommMyWebViewActivity;
import com.frxs.order.HomeActivity;
import com.google.gson.JsonObject;

public class JsObject {
    private static JsObject jsObject;
    private Context mContext;

    public JsObject(Context context) {
        mContext = context;
    }

    public static synchronized JsObject getInstance(Context context) {
        if (jsObject == null) {
            jsObject = new JsObject(context);
        }

        return jsObject;
    }

    public void back2LoginActivity() {
        if (mContext != null && mContext instanceof CommMyWebViewActivity) {
            ((CommMyWebViewActivity)mContext).finish();
        }
    }

    public void back2ShopInfoActivity() {
        if (mContext != null && mContext instanceof CommMyWebViewActivity) {
            ((CommMyWebViewActivity)mContext).finish();
        }
    }

    public void back2HomeActivity() {
        if (mContext != null && mContext instanceof CommMyWebViewActivity) {
            Intent intent = new Intent(mContext, HomeActivity.class);
            mContext.startActivity(intent);
        }
    }

    public void uploadPicture(JsonObject param) {
        int imgType = param.get("imgType").getAsInt();
        if (mContext != null && mContext instanceof CommMyWebViewActivity) {
            ((CommMyWebViewActivity)mContext).showPopWindow(imgType);
        }
    }

//    public void go2ActivityDetail(JsonObject param) {
//        String productindex = param.get("productindex").getAsString();
//
//        if (mContext != null && !TextUtils.isEmpty(productindex)) {
//            UrlDecorator urldecorator = new UrlDecorator(RequestApi.BASE_URL + "freeDetail.action");
//            urldecorator.add("source", String.valueOf(RequestApi.SOURCE_ANDROID));
////			urldecorator.add("categoryindex", "8"); //4 餐饮 8 客房 21免费大餐 22免费教育 25宴会 27美食
//            urldecorator.add("productindex", productindex);
//            urldecorator.add("pagenum", "1");
//            urldecorator.add("pagecount", String.valueOf(RequestApi.DEFALT_PAGE_COUNT));
//
//            Intent intent = new Intent(mContext, CommMyWebViewActivity.class);
//            intent.putExtra("need_share", true);
////			intent.putExtra("project_name", item.getProject_name());
////			intent.putExtra("project_desc", item.getProj_scaledes());
//            intent.putExtra("title", mContext.getString(R.string.activity_detail));
//            intent.putExtra("h5_url", urldecorator.toString());
//            mContext.startActivity(intent);
//        }
//    }
//
//    public void go2EstateDetail(JsonObject param) {
//        String projectindex = param.get("projectindex").getAsString();
//
//        if (mContext != null && !TextUtils.isEmpty(projectindex)) {
//            UrlDecorator urldecorator = new UrlDecorator(RequestApi.BASE_URL + "housingestates.action");
//            urldecorator.add("source", String.valueOf(RequestApi.SOURCE_ANDROID));
////			urldecorator.add("categoryindex", "8"); //4 餐饮 8 客房 21免费大餐 22免费教育 25宴会 27美食
//            urldecorator.add("projectindex", projectindex);
//            urldecorator.add("pagenum", "1");
//            urldecorator.add("pagecount", String.valueOf(RequestApi.DEFALT_PAGE_COUNT));
//
//            Intent intent = new Intent(mContext, CommMyWebViewActivity.class);
//            intent.putExtra("need_share", true);
////			intent.putExtra("project_name", item.getProject_name());
////			intent.putExtra("project_desc", item.getProj_scaledes());
//            intent.putExtra("title", mContext.getString(R.string.estate_detail));
//            intent.putExtra("h5_url", urldecorator.toString());
//            mContext.startActivity(intent);
//        }
//    }
//
//
//    public void go2PreorderRoom(JsonObject param) {
//        if (mContext != null && null != param) {
//            UserInfoMessage userInfo = ElifeApplication.getInstance().getUserInfo();
//            if (null != userInfo && userInfo.getMemberBasicInfo() != null) {
//                String hotelName = param.get("hotel_name").getAsString();
//                String roomType = param.get("room_type").getAsString();
//                String bedType = param.get("bed_type").getAsString();
//                String breakfastType = param.get("breakfast_type").getAsString();
//                String price = param.get("fee_xy").getAsString();
//                String productIndex = param.get("productindex").getAsString();
//                String projectIndex = param.get("projectindex").getAsString();
//
//                Intent intent = new Intent(mContext, PreorderActivity.class);
//                intent.putExtra("hotel_name", hotelName);
//                intent.putExtra("room_type", roomType);
//                intent.putExtra("bed_type", bedType);
//                intent.putExtra("breakfast_type", breakfastType);
//                intent.putExtra("room_price", price);
//                intent.putExtra("productindex", productIndex);
//                intent.putExtra("projectindex", projectIndex);
//                mContext.startActivity(intent);
//            } else {
//                Intent intent = new Intent(mContext, LoginActivity.class);
//                mContext.startActivity(intent);
//            }
//        }
//    }
//
//    public void go2MakeAppointment(JsonObject param) {
//        String activityindex = param.get("activityindex").getAsString();
//        String start_time = param.get("start_time").getAsString();
//        String end_time = param.get("end_time").getAsString();
//        String proj_address = (null != param.get("proj_address")) ? param.get("proj_address").getAsString() : "";
//        String prod_name = (null != param.get("prod_name")) ? param.get("prod_name").getAsString() : "";
//
//        if (mContext != null && !TextUtils.isEmpty(activityindex)) {
//            Intent intent = new Intent(mContext, AppointmentActivity.class);
//            intent.putExtra("activityindex", activityindex);
//            intent.putExtra("start_time", start_time);
//            intent.putExtra("end_time", end_time);
//            intent.putExtra("proj_address", proj_address);
//            intent.putExtra("prod_name", prod_name);
//            mContext.startActivity(intent);
//        }
//    }
//
//
//    public void go2ViewMap(JsonObject param) {
//        String map_url = param.get("map_url").getAsString();
//
//        if (mContext != null && !TextUtils.isEmpty(map_url)) {
//            Intent intent = new Intent(mContext, CommMyWebViewActivity.class);
//            intent.putExtra("title", mContext.getString(R.string.view_map));
//            intent.putExtra("h5_url", map_url);
//            mContext.startActivity(intent);
//        }
//    }
//
//    public void go2FreeParty(JsonObject param) {
//        String title = param.get("title").getAsString();
//
//        if (mContext != null) {
//            Intent intent = new Intent(mContext, FreePartyActivity.class);
//            intent.putExtra("title", null != title ? title : "");
//            mContext.startActivity(intent);
//        }
//    }
//
//    public void go2OrderConfirm(JsonObject param) {
//        String projectindex = param.get("projectIndex").getAsString();
//        JsonArray productListArray = param.get("productList").getAsJsonArray();
//
//        if (mContext != null && !TextUtils.isEmpty(projectindex) && productListArray.size() > 0) {
//            ArrayList<OrderEntity> productList = new ArrayList<OrderEntity>();
//            Gson gson = new Gson();
//
//            for (JsonElement obj : productListArray) {
//                OrderEntity item = gson.fromJson(obj, OrderEntity.class);
//                item.setQty("1");
//                item.setDealPoint(item.getFee_jf());
//                productList.add(item);
//            }
//
//            Intent intent = new Intent(mContext, OrderConfirmActivity.class);
//            intent.putExtra("projectindex", projectindex);
//            intent.putExtra("productList", productList);
//            mContext.startActivity(intent);
//        }
//    }


    public void showMessage(JsonObject param) {
        String message = param.get("message").getAsString();
        Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}

package com.frxs.order.rest.service;


import com.frxs.order.model.AppVersionGetRespData;
import com.frxs.order.model.ApplySaleBackInfo;
import com.frxs.order.model.ApplySaleBackOrder;
import com.frxs.order.model.AttachedShopInfo;
import com.frxs.order.model.BaseCartGoodsInfo;
import com.frxs.order.model.Bill;
import com.frxs.order.model.BillDetails;
import com.frxs.order.model.CartGoodsDetail;
import com.frxs.order.model.ColumnSwitchSet;
import com.frxs.order.model.GetPreSaleProductListRsp;
import com.frxs.order.model.MyPreSaleProductListRsp;
import com.frxs.order.model.OrderPreProducts;
import com.frxs.order.model.OrderShopGetRespData;
import com.frxs.order.model.OrderShopQueryRespData;
import com.frxs.order.model.PostEditBackCart;
import com.frxs.order.model.PostEditCart;
import com.frxs.order.model.PostOrderCancel;
import com.frxs.order.model.PostOrderEditAll;
import com.frxs.order.model.PostPrePay;
import com.frxs.order.model.PrefectShopInfo;
import com.frxs.order.model.ProductWProductsDetailsGetRespData;
import com.frxs.order.model.ProductWProductsGetToB2BRespData;
import com.frxs.order.model.PromotiaonActivityDetail;
import com.frxs.order.model.Promotion;
import com.frxs.order.model.SaleBackCart;
import com.frxs.order.model.SaleBackOrder;
import com.frxs.order.model.SaleBackOrderList;
import com.frxs.order.model.SaleCartGetRespData;
import com.frxs.order.model.SaleOrderGetTrackRespData;
import com.frxs.order.model.ShopApplyInfoStatus;
import com.frxs.order.model.ShopBillNoPayOrderList;
import com.frxs.order.model.ShopCategoriesGetRespData;
import com.frxs.order.model.ShopImgPath;
import com.frxs.order.model.ShopInfo;
import com.frxs.order.model.ShopPointExchangeDetails;
import com.frxs.order.model.ShopPointIncome;
import com.frxs.order.model.UnreadMessag;
import com.frxs.order.model.UserInfo;
import com.frxs.order.model.UserInfoStatus;
import com.frxs.order.model.WAdvertisementGetListModelRespData;
import com.frxs.order.model.WXNoPayQuery;
import com.frxs.order.model.Warehouse;
import com.frxs.order.model.WarehouseLine;
import com.frxs.order.model.WarehouseMessage;
import com.frxs.order.model.WarehouseMessageShopGetListRespData;
import com.frxs.order.model.WarehouseSysParams;
import com.frxs.order.rest.model.ApiResponse;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService<T> {

    //登录
    @FormUrlEncoded
    @POST("api/User/UserLogin")
    Call<ApiResponse<UserInfo>> UserLogin(@FieldMap Map<String, Object> params);

    //门店资料是否完善接口
    @FormUrlEncoded
    @POST("api/Shop/IsPrefectShopInfo")
    Call<ApiResponse<PrefectShopInfo>> IsPrefectShopInfo(@FieldMap Map<String, Object> params);

    //修改密码
    @FormUrlEncoded
    @POST("api/User/UserEditPassword")
    Call<ApiResponse<String>> UserEditPassword(@FieldMap Map<String, Object> params);

    //获取我的栏目显示开关接口
    @FormUrlEncoded
    @POST("api/User/MyColumnDisplaySwitch")
    Call<ApiResponse<ColumnSwitchSet>> MyColumnDisplaySwitch(@FieldMap Map<String, Object> params);

    //仓库信息
    @FormUrlEncoded
    @POST("api/Warehouse/WarehouseGet")
    Call<ApiResponse<Warehouse>> WarehouseGet(@FieldMap Map<String, Object> params);

    //门店送货路线
    /*@FormUrlEncoded
    @POST("api/Warehouse/WarehouseLineShopGet")
    Call<ApiResponse<WarehouseLine>> WarehouseLineShopGet(@FieldMap Map<String, Object> params);*/

    //消息分页查询
    @FormUrlEncoded
    @POST("api/Warehouse/WarehouseMessageShopIsNew")
    Call<ApiResponse<String>> WarehouseMessageShopIsNew(@FieldMap Map<String, Object> params);

    //消息分页查询
    @FormUrlEncoded
    @POST("api/Warehouse/WarehouseMessageShopGetList")
    Call<ApiResponse<WarehouseMessageShopGetListRespData>> WarehouseMessageShopGetList(@FieldMap Map<String, Object> params);


    //订单取消
    @POST("api/Order/OrderShopCancel")
    Call<ApiResponse<String>> OrderShopCancel(@Body PostOrderCancel orderCancel);

    //订单详情
    @FormUrlEncoded
    @POST("api/Order/vOrderGetAction")
    Call<ApiResponse<OrderShopGetRespData>> OrderGetAction(@FieldMap Map<String, Object> params);

    //订单跟踪
    @FormUrlEncoded
    @POST("api/Order/SaleOrderGetTrack")
    Call<ApiResponse<SaleOrderGetTrackRespData>> SaleOrderGetTrack(@FieldMap Map<String, Object> params);

    //订单查询(订单列表)
    @FormUrlEncoded
    @POST("api/Order/OrderQuery")
    Call<ApiResponse<OrderShopQueryRespData>> OrderQuery(@FieldMap Map<String, Object> params);

    //再次购买
    @FormUrlEncoded
    @POST("api/Order/OrderReBuy")
    Call<ApiResponse<String>> OrderReBuy(@FieldMap Map<String, Object> params);

    //确认收货
    @FormUrlEncoded
    @POST("api/Order/OrderPreFinished")
    Call<ApiResponse<String>> OrderPreFinished(@FieldMap Map<String, Object> params);

    //订单编辑（整单编辑）
    @POST("api/Order/OrderShopEditAll")
    Call<ApiResponse<String>> OrderShopEditAll(@Body PostOrderEditAll postOrderEditAll);

    //订单编辑(单条编辑)
    @POST("api/Order/OrderShopEditRemark")
    Call<ApiResponse<String>> OrderShopEditRemark(@Body PostEditCart editCart);

    //销售结算单据列表查询
    @FormUrlEncoded
    @POST("api/Order/SaleSettleGetList")
    Call<ApiResponse<Bill>> SaleSettleGetList(@FieldMap Map<String, Object> params);

    //销售结算单据明细查询
    @FormUrlEncoded
    @POST("api/Order/SaleSettleGetListActionGetModel")
    Call<ApiResponse<BillDetails>> SaleSettleGetListActionGetModel(@FieldMap Map<String, Object> params);

    //订单创建
    @FormUrlEncoded
    @POST("api/Order/OrderShopCreateBusiness")
    Call<ApiResponse<String>> OrderShopCreateBusiness(@FieldMap Map<String, Object> params);

    //用户是否存在未确认订单
    @FormUrlEncoded
    @POST("api/Order/OrderShopExistUnConfirmOrder")
    Call<ApiResponse<String>> OrderShopExistUnConfirmOrder(@FieldMap Map<String, Object> params);


    //获取分类商品
    @FormUrlEncoded
    @POST("api/WProducts/ProductWProductsGetToB2B")
    Call<ApiResponse<ProductWProductsGetToB2BRespData>> ProductWProductsGetToB2B(@FieldMap Map<String, Object> params);

    //商品详情
    @FormUrlEncoded
    @POST("api/WProducts/WProductsB2BDetailsGet")
    Call<ApiResponse<ProductWProductsDetailsGetRespData>> WProductsB2BDetailsGet(@FieldMap Map<String, Object> params);


    //首页数据
    @FormUrlEncoded
    @POST("api/WAdvertisement/WAdvertisementGetListModel")
    Call<ApiResponse<List<WAdvertisementGetListModelRespData>>> WAdvertisementGetListModel(@FieldMap Map<String, Object> params);


    //消息实体获取
    @FormUrlEncoded
    @POST("api/Warehouse/WarehouseMessageGetModel")
    Call<ApiResponse<WarehouseMessage>> WarehouseMessageGetModel(@FieldMap Map<String, Object> params);

    //获取购物车列表
    @FormUrlEncoded
    @POST("api/SaleCart/SaleCartGet")
    Call<ApiResponse<SaleCartGetRespData>> SaleCartGet(@FieldMap Map<String, Object> params);

    //获取购物车商品数量
    @FormUrlEncoded
    @POST("api/SaleCart/GetSaleCartProductPreQtyList")
    Call<ApiResponse<BaseCartGoodsInfo>> SaleCartGetPreQty(@FieldMap Map<String, Object> params);

    //删除购物车列表
    @FormUrlEncoded
    @POST("api/SaleCart/SaleCartDelete")
    Call<ApiResponse<String>> SaleCartDelete(@FieldMap Map<String, Object> params);

    //单行修改购物车商品
    @POST("api/SaleCart/SaleCartEditSingle")
    Call<ApiResponse<List<CartGoodsDetail>>> SaleCartEditSingle(@Body PostEditCart editCart);

    //计算购物车商品总数量
    @FormUrlEncoded
    @POST("api/SaleCart/SaleCartCount")
    Call<ApiResponse<Integer>> SaleCartCount(@FieldMap Map<String, Object> params);

    //获取运营分类
    @FormUrlEncoded
    @POST("api/ShopCategories/ShopCategoriesGet")
    Call<ApiResponse<ShopCategoriesGetRespData>> ShopCategoriesGet(@FieldMap Map<String, Object> params);

    //版本更新
    @FormUrlEncoded
    @POST("api/AppVersion/AppVersionUpdateGet")
    Call<ApiResponse<AppVersionGetRespData>> AppVersionUpdateGet(@FieldMap Map<String, Object> params);

    //促销活动列表
    @FormUrlEncoded
    @POST("api/WPromotion/WPromotionsBuyGiftsForAppGet")
    Call<ApiResponse<List<Promotion>>> GetPromotion(@FieldMap Map<String, Object> params);

    //获取组合买赠促销活动详情
    @FormUrlEncoded
    @POST("api/WPromotion/WPromotionsBuyGiftsDetailsForAppGet")
    Call<ApiResponse<PromotiaonActivityDetail>> GetPromotionActivityDetails(@FieldMap Map<String, Object> params);

    //获取门店信息(起订金额)
    @FormUrlEncoded
    @POST("api/Shop/ShopModelGet")
    Call<ApiResponse<ShopInfo>> GetShopInfo(@FieldMap Map<String, Object> params);


    //获取预售商品列表
    @FormUrlEncoded
    @POST("api/PreSaleProductShopBuyDetail/GetProductsList")
    Call<ApiResponse<GetPreSaleProductListRsp>> GetPreSaleProductList(@FieldMap Map<String, Object> params);

    //获取我的预售商品列表
    @FormUrlEncoded
    @POST("api/PreSaleProductShopBuyDetail/GetList")
    Call<ApiResponse<MyPreSaleProductListRsp>> GetMyPreSaleProductList(@FieldMap Map<String, Object> params);

    //修改状态或删除
    @FormUrlEncoded
    @POST("api/PreSaleProductShopBuyDetail/ModifyOrDel")
    Call<ApiResponse<Object>> ModifyOrDelPreSaleProduct(@FieldMap Map<String, Object> params);

    //预售商品提交
    @FormUrlEncoded
    @POST("api/PreSaleProductShopBuyDetail/Add")
    Call<ApiResponse<Object>> AddPreSaleProduct(@FieldMap Map<String, Object> params);

    //预售商品提交
    @FormUrlEncoded
    @POST("api/PreSaleProductShopBuyDetail/bShowPreSale")
    Call<ApiResponse<String>> NeedShowPreSale(@FieldMap Map<String, Object> params);

    //获取当前门店未支付账单
    @FormUrlEncoded
    @POST("api/Pay/NoPayQuery")
    Call<ApiResponse<WXNoPayQuery>> GetNoPayQuery(@FieldMap Map<String, Object> params);

    //获取微信预支付信息
    @POST("api/Pay/PrePay")
    Call<ApiResponse<String>> GetPrepayInfo(@Body PostPrePay editCart);

    //获取门店信息
    @FormUrlEncoded
    @POST("api/Shop/ShopModelGet")
    Call<ApiResponse<UserInfoStatus>> GetUserInfo(@FieldMap Map<String, Object> params);

    //获取附属门店信息
    @FormUrlEncoded
    @POST("api/Shop/AttachedShopList")
    Call<ApiResponse<List<AttachedShopInfo>>> GetAttachedShopList(@FieldMap Map<String, Object> params);

    //获取门店未读消息
    @FormUrlEncoded
    @POST("api/Warehouse/GetUnreadMessage")
    Call<ApiResponse<UnreadMessag>> GetUserUnreadMessage(@FieldMap Map<String, Object> params);

    //积分收入明细
    @FormUrlEncoded
    @POST("api/ShopPoint/GetShopPointDetails")
    Call<ApiResponse<ShopPointIncome>> GetShopPointDetails(@FieldMap Map<String, Object> params);

    //积分兑换明细
    @FormUrlEncoded
    @POST("api/ShopPoint/GetShopPointExchangeDetails")
    Call<ApiResponse<ShopPointExchangeDetails>> GetShopPointExchangeDetails(@FieldMap Map<String, Object> params);

    //门店送货路线
    @FormUrlEncoded
    @POST("api/Warehouse/GetShopDeliveryCycle")
    Call<ApiResponse<WarehouseLine>> GetShopDeliveryCycle(@FieldMap Map<String, Object> params);

    //门店账单开关
    @FormUrlEncoded
    @POST("api/User/GetWarehouseSysParams")
    Call<ApiResponse<List<WarehouseSysParams>>> GetWarehouseSysParams(@FieldMap Map<String, Object> params);

    //获取门店订单中的预订商品
    @FormUrlEncoded
    @POST("api/SaleOrderBook/GetSaleOrderBookProducts")
    Call<ApiResponse<OrderPreProducts>> GetSaleOrderPreProducts(@FieldMap Map<String, Object> params);

    //备注门店订单中的预订商品
    //@FormUrlEncoded
    @POST("api/SaleOrderBook/UpdateSaleOrderBookProducts")
    Call<ApiResponse<Object>> EditSaleOrderPreProducts(@Body PostEditCart editCart);

    //删除门店订单中的预订商品
    //@FormUrlEncoded
    @POST("api/SaleOrderBook/DeleteSaleOrderBookProducts")
    Call<ApiResponse<Object>> DelSaleOrderPreProducts(@Body PostEditCart editCart);

    //修改绑定手机发送验证码
    @FormUrlEncoded
    @POST("api/SMSData/SendValidCode")
    Call<ApiResponse<Integer>> SendValidCode (@FieldMap Map<String, Object> params);

    //修改绑定手机复验验证码
    @FormUrlEncoded
    @POST("api/SMSData/HasValidCode")
    Call<ApiResponse<String>> IsValidCode (@FieldMap Map<String, Object> params);

    //退货车中的增删改
    //@FormUrlEncoded
    @POST("api/SaleBackCart/SaleBackCartEditSingle")
    Call<ApiResponse<Object>> SaleBackCartEditSingle (@Body PostEditBackCart editCart);

    //退货车商品列表接口
    @FormUrlEncoded
    @POST("api/SaleBackCart/SaleBackCartList")
    Call<ApiResponse<SaleBackCart>> GetSaleBackCartList (@FieldMap Map<String, Object> params);

    //提交退货申请单
    @FormUrlEncoded
    @POST("api/SaleBackCart/SubmitApplyForSaleBack")
    Call<ApiResponse<Boolean>> SubmitApplyForSaleBack (@FieldMap Map<String, Object> params);

    //获取退货申请单分页数据
    @FormUrlEncoded
    @POST("api/SaleBackCart/GetApplyForSaleBackPageList")
    Call<ApiResponse<ApplySaleBackOrder>> GetApplySaleBackList (@FieldMap Map<String, Object> params);

    //门店退货申请取消
    @FormUrlEncoded
    @POST("api/SaleBackCart/CancelApplyForSaleBack")
    Call<ApiResponse<Boolean>> CancelApplySaleBack (@FieldMap Map<String, Object> params);

    //获取退货申请单详细
    @FormUrlEncoded
    @POST("api/SaleBackCart/GetApplyForSaleBackDetailed")
    Call<ApiResponse<ApplySaleBackInfo>> GetApplySaleBackInfo (@FieldMap Map<String, Object> params);

    //获取退货单分页数据
    @FormUrlEncoded
    @POST("api/SaleBackCart/GetSaleBackPrePageData")
    Call<ApiResponse<SaleBackOrder>> GetSaleBackList (@FieldMap Map<String, Object> params);

    //获取门店退货订单信息
    @FormUrlEncoded
    @POST("api/SaleBackCart/GetSaleBackInfo")
    Call<ApiResponse<SaleBackOrderList>> GetSaleBackInfo (@FieldMap Map<String, Object> params);

    //退货申请单中是否有此商品
    @FormUrlEncoded
    @POST("api/SaleBackCart/UnConfirmApplyForSaleBack")
    Call<ApiResponse<String>> IsRepeatGoodsInBackApplyOrder(@FieldMap Map<String, Object> params);

    //获取退货商品附件
    @FormUrlEncoded
    @POST("api/SaleBackCart/GetDetailsAttsByApplyBackID")
    Call<ApiResponse<ArrayList<String>>> GetDetailsAttsByApplyBackID(@FieldMap Map<String, Object> params);

    //修改绑定手机
    @FormUrlEncoded
    @POST("api/User/EditBindingPhone")
    Call<ApiResponse<Integer>> EditBindingPhone (@FieldMap Map<String, Object> params);

    //找回密码
    @FormUrlEncoded
    @POST("api/User/RecoverPassword")
    Call<ApiResponse<Integer>> FindPassWord (@FieldMap Map<String, Object> params);

    //修改绑定银行卡
    @FormUrlEncoded
    @POST("api/User/EditBankAccount")
    Call<ApiResponse<Integer>> EditBindingBankCard (@FieldMap Map<String, Object> params);

    //获取门店资料状态
    @FormUrlEncoded
    @POST("api/User/GetShopApplyInfo")
    Call<ApiResponse<ShopApplyInfoStatus>> GetShopApplyInfoStatus (@FieldMap Map<String, Object> params);

    //上传图片
    @FormUrlEncoded
    @POST("Image/SaveShopImagesByBase64String")
    Call<ApiResponse<ShopImgPath>> SubmitShopImage (@FieldMap Map<String, Object> params);

    //获取门店账单未支付订单
    @FormUrlEncoded
    @POST("api/Order/GetUnPaidList4OrderPlatform")
    Call<ApiResponse<ShopBillNoPayOrderList>> GetNoPayOrderList (@FieldMap Map<String, Object> params);

    //获取门店账单订单列表
    @FormUrlEncoded
    @POST("api/Order/GetCustomerVoucherDetailPageList")
    Call<ApiResponse<ShopBillNoPayOrderList>> GetShopBillList (@FieldMap Map<String, Object> params);

    //获取门店合并支付拓展标识
    @FormUrlEncoded
    @POST("api/User/GetShopMergePaySwitch")
    Call<ApiResponse<JsonObject>> GetShopMergePaySwitch (@FieldMap Map<String, Object> params);

//
//    @Multipart
//    @POST("api/Values/GetImages")
//    Call<ApiResponse> GetImages(@PartMap Map<String, RequestBody> parts);


}

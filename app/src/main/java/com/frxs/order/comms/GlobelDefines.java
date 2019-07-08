package com.frxs.order.comms;

/**
 * 某某某类
 * 
 * @ClassName: GlobelDefines
 * @Description: 定义全局常量
 * @author: ewu
 * @date: 2015-3-3
 * 
 */
public class GlobelDefines
{
	
	public static final String PREFS_NAME = "MyFrefsFile";

	public static final String SALE_CART_FILE_NAME = "SaleCart.txt";
	
	public static final String KEY_USER = "key_user";
	
	public static final String KEY_FIRST_ENTER = "key_first_enter";
	
	public static final String KEY_ENVIRONMENT = "key_environment";

	public static final String KEY_USER_ACCOUNT = "key_user_account";

	public static final String KEY_DOWNLOAD_ID = "key_download_id";

	public static final String KEY_PAY_TYPE = "key_pay_type";

	public static final String KEY_SHOP_BILL = "key_shop_bill";

	/**
	 * 从多规格产品setResult ResultCode
	 */
	public static final int REQ_CODE_PRODUCT_DETAIL = 100;

	/**
	 * 购物车 requestCode
	 */
	public static final int REQ_CODE_STORE_CART = 101;

	/**
	 * 登录页面 requestCode
	 */
	public static final int REQ_CODE_LOGIN = 102;

	/**
	 * Splash页面 requestCode
	 */
	public static final int REQ_CODE_SPLASH = 103;

	/**
	 * 从多规格产品setResult ResultCode
	 */
	public static final int RESULT_CODE_SKU = 200;

	/**
	 * 退货setResult ResultCode
	 */
	public static final int RESULT_BACK_SKU = 6;

	/**
	 * 网络请求成功
	 */
	public static final String FLAG_SUCCESS = "SUCCESS";// "FAIL",
	
	/**
	 * 网络请求失败
	 */
	public static final String FLAG_FAIL = "FAIL";// "FAIL",
	
	

	public static final String SP_USERNAME = "sp_username";
	
	
	public static final String SP_PASSWORD = "sp_password";
	
	public static String INSTOCK = "0";// 正常（不缺货）
	
	public static String OUTSTOCK = "1";// 缺货

	public static int MAX_BACK_COUNT = 9999;// 最大的退货数量

	/**
	 * 门店账单业务参数
	 */
	public static final int REQ_SHOP_BILL = 215;
}

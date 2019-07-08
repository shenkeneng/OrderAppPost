package com.frxs.order.comms;

/**
 * Created by ewu on 2016/3/23.
 */
public class Config {
    public static final String PREFS_NAME = "MyFrefsFile";

    public static final String KEY_USER = "key_user";

    public static final String KEY_LIST = "key_list";

    public static final String KEY_HIST = "key_history"; // 商品搜索历史

    public static final String SEARCH_PREFS_NAME = "SearchPrefsFile";

    // 远程服务器网络 (0:线上环境、1：测试站点、2：演示环境、3：迁移环境、4:集成开发环境、5：TF本机、6:DL本机)
    public static int networkEnv = 0;

    public static String getBaseUrl() {
        return getBaseUrl(networkEnv);
    }

    public static String getBaseUrl(int networkEnv) {
        String BASE_URL = "";
        if (networkEnv == 0) {
            BASE_URL = "http://orderapi.erp2.frxs.com/";
        }  else if (networkEnv == 1) {
            BASE_URL = "http://b2btest.frxs.cn/";
        } else if (networkEnv == 2) {
             BASE_URL = "http://yfborderapi.erp2.frxs.com/";// 预发布环境
        } else if (networkEnv == 3) {
            BASE_URL = "http://f3test_z_order_api.frxs.com/";
        } else if (networkEnv == 4) {
            BASE_URL = "http://192.168.8.142:8089/";
        } else if (networkEnv == 5) {
            BASE_URL = "http://192.168.8.242:8085/";

        } else if (networkEnv == 6) {
            //BASE_URL = "http://f3dh1.frxs.com/";// 演示环境
            //BASE_URL = "http://192.168.8.63:8088/";//DL
            //BASE_URL = "http://192.168.8.135:8056/";//TT
            //BASE_URL = "http://192.168.8.210:8099/";//DF
            BASE_URL = "http://192.168.8.246:8081/";//ZL
            //BASE_URL = "http://192.168.8.156:8089/";//DDY
            //BASE_URL = "http://192.168.8.214:8022/";//GZJ

        } else {
            BASE_URL = "http://orderapi.erp2.frxs.com/";
        }
        return BASE_URL;

    }

    public static String getWXAppID() {
        String APP_ID = "";
        if (networkEnv > 0){
            APP_ID = "wx9db0fc18ba13ebfc";// debug
        } else {
            APP_ID = "wx0ce0a16d75efef5d";// 正式
        }
        return APP_ID;
    }

    public static String getSubimgUrl(){
        String SUBIMG_SERVER = "";// 获取当前图片上传地址
        if (networkEnv > 0){
            SUBIMG_SERVER = "http://itestimage.frxs.cn/";//测试环境
            //SUBIMG_SERVER = "http://192.168.8.142:8082/";//集成环境
        } else {
            SUBIMG_SERVER = "http://imagesup.erp2.frxs.com/";
        }

        return SUBIMG_SERVER;
    }
}

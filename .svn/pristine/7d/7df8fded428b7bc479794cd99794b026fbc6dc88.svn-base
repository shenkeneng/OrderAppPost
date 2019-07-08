package com.frxs.order.rest;

import com.frxs.order.BuildConfig;
import com.frxs.order.comms.Config;
import com.frxs.order.rest.service.ApiService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ewu on 2016/2/18.
 */
public class RestClient {

    private ApiService apiService;

    public RestClient(String baseUrl)
    {
        // 目前Retrofit2.0必须依靠okhttp提供的日志系统
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // 正式环境下的releade apk 不输出网络log
        if (!BuildConfig.isDebug && 0 == Config.networkEnv){
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }else{
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(30000, TimeUnit.MILLISECONDS)
                .readTimeout(30000, TimeUnit.MILLISECONDS)
                .build();

//        int timeout = okHttpClient.connectTimeoutMillis();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public ApiService getApiService()
    {
        return apiService;
    }
}

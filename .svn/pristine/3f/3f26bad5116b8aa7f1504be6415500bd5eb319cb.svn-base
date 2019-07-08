package com.frxs.order.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.util.Log;



public class UrlDecorator
{
	
    private StringBuilder url;
    
    public UrlDecorator(String url) {
        this.url = new StringBuilder(url);
    }
    
    /**
     * 将参数拼装在url问号前面
     * @param param
     * @return
     */
    public UrlDecorator append(String paramValue) {
        if (paramValue != null && !"".equals(paramValue)) {
            int i = this.url.indexOf("?");
            if(i > 0) {
            	if(this.url.substring(i - 1, i).equals("/"))
            	{
            		this.url.insert(i, paramValue);
            	}
            	else
            	{
            		this.url.insert(i, "/" + paramValue);
            	}
            	// 重复添加了
//                this.url.insert(i, paramValue + "/");
            } else {
            	if(!this.url.toString().endsWith("/"))
            	{
            		this.url.append("/");
            	}
                this.url.append(paramValue);
            }
        }else{
//        	throw new RuntimeException("错误：URL路径中出现空字符串或者null对象");
        	Log.e("UrlDecorator", "错误：URL路径中出现空字符串或者null对象");
        }
        return this;
    }
    
    /**
     * 将参数拼装在url问号后面
     * @param paramName
     * @param paramValue
     * @return
     */
    public UrlDecorator add(String paramName, String paramValue) {
        if (this.url.indexOf("?") <= 0) {
            this.url.append("?");
        }
        if (paramValue != null && !"".equals(paramValue)) {
            if (!this.url.toString().endsWith("?")) {
                this.url.append("&");
            }
            this.url.append(paramName).append("=").append(encode(paramValue.trim()));
        }
        return this;
    }
    
    //URI不允许有空格等字符，如果参数值有空格，需要此方法转换
    private String encode(String str){
    	try {
			return URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			
			//针对不支持的编码时报错，utf-8应该是支持的
			Log.e("参数转码异常", e.toString());
			throw new RuntimeException(e);
		}
    }

    public String toString() {
        return this.url.toString();
    }
    
    public StringBuilder getUrl()
    {
        return url;
    }

    public void setUrl(StringBuilder url)
    {
        this.url = url;
    }

    public boolean replaceHost(String newHost)
    {
    	if(null == newHost) {
    		return false;
    	}
    	String uri = url.toString();
    	boolean isHttpsRequest = false;
    	if(uri.startsWith("http://")) {
    		uri = uri.substring(7);
    	} else if(uri.startsWith("https://")) {
    		uri = uri.substring(8);
    		isHttpsRequest = true;
    	}
    	
		int index = uri.indexOf(':');
		if( index > 0) {
			uri = uri.substring(index);
			StringBuilder sb = new StringBuilder();
			if(isHttpsRequest) {
				sb.append("https://");
			} else {
				sb.append("http://");
			}
			sb.append(newHost);
			sb.append(uri);
			this.url = sb;
			return true;
		} else {
			index = uri.indexOf('/');
			if(index > 0) {
				uri = uri.substring(index);
    			StringBuilder sb = new StringBuilder();
    			if(isHttpsRequest) {
    				sb.append("https://");
    			} else {
    				sb.append("http://");
    			}
    			sb.append(newHost);
    			sb.append(uri);
    			this.url = sb;
    			return true;
			} else {
				StringBuilder sb = new StringBuilder();
    			if(isHttpsRequest) {
    				sb.append("https://");
    			} else {
    				sb.append("http://");
    			}
    			sb.append(newHost);
    			sb.append("/");
    			this.url = sb;
    			return true;
			}
		}
    }
}

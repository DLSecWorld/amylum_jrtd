/**
 * 
 *//*
package com.wangku.miaodan.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

*//**
 * liujiabao
 *//*
public class HttpClientUtil {
	//post json格式的数据
	@SuppressWarnings("finally")
	public static String post(String url, String content){
		if(StringUtils.isEmpty(url)){
			return "";
		}
		byte[] byteContent = null;
        try {
        	byteContent = content.getBytes("UTF-8");
        } catch (Exception e) {
            return "";
        }
		String result = "";
		// 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost    
        HttpPost httppost = new HttpPost(url);
		
        try {  
        	EntityBuilder bulder = EntityBuilder.create().setContentType(ContentType.APPLICATION_JSON).setContentEncoding("UTF-8").setBinary(byteContent);
            HttpEntity entity = bulder.build();
            httppost.setEntity(entity);
            System.out.println("executing request " + httppost.getURI());  
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {  
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {  
                    result = EntityUtils.toString(resEntity, "UTF-8");
                }  
            } finally {  
                response.close();  
                return result;
            }  
        } catch (Exception e) {  
        	e.printStackTrace();
        } finally {  
            try {  
                httpclient.close(); 
                return result;
            } catch (IOException e) {  
                e.printStackTrace();
                return result;
            }  
        }
	}
	
	//post 表单格式的数据
	@SuppressWarnings("finally")
	public static String post(String url, Map<String, String> params){
		if(StringUtils.isEmpty(url)){
			return "";
		}
		String result = "";
		// 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost    
        HttpPost httppost = new HttpPost(url);
        // 创建参数队列    
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        if(params != null){
        	for(String key : params.keySet()){
            	formparams.add(new BasicNameValuePair(key, params.get(key)));
            }
        }
        
        UrlEncodedFormEntity uefEntity;
        try {  
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);  
            System.out.println("executing request " + httppost.getURI());  
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {  
                HttpEntity entity = response.getEntity();
                if (entity != null) {  
                    result = EntityUtils.toString(entity, "UTF-8");
                }  
            } finally {  
                response.close();  
                return result;
            }  
        } catch (Exception e) {  
        	e.printStackTrace();
        } finally {  
            try {  
                httpclient.close(); 
                return result;
            } catch (IOException e) {  
                e.printStackTrace();
                return result;
            }  
        }
	}
	
	@SuppressWarnings("finally")
	public static String get(String url){
		if(StringUtils.isEmpty(url)){
			return "";
		}
		String result = "";
		CloseableHttpClient httpclient = HttpClients.createDefault();
        try {  
            // 创建httpget  
            HttpGet httpget = new HttpGet(url);
            // 执行get请求   
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {  
                // 获取响应实体    
                HttpEntity entity = response.getEntity();
                if (entity != null) {  
                    result = EntityUtils.toString(entity);
                }  
            } finally {  
                response.close();
                return result;
            }  
        } catch (Exception e) {  
            e.printStackTrace(); 
        } finally {  
            try {  
                httpclient.close();
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                return result;
            }  
        }  
	}
	
	
	//post xml的数据
	@SuppressWarnings("finally")
	public static String postXml(String url, String xml){
		if(StringUtils.isEmpty(url)){
			return "";
		}
		String result = "";
		// 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost    
        HttpPost httppost = new HttpPost(url);
		
        byte[] byteContent = null;
        try {
        	byteContent = xml.getBytes("UTF-8");
        } catch (Exception e) {
            return "";
        }
        try {  
        	EntityBuilder bulder = EntityBuilder.create().setContentType(ContentType.TEXT_XML).setContentEncoding("UTF-8").setBinary(byteContent);
            HttpEntity entity = bulder.build();
            httppost.setEntity(entity);
            System.out.println("executing request " + httppost.getURI());  
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {  
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {  
                    result = EntityUtils.toString(resEntity, "UTF-8");
                }  
            } finally {  
                response.close();  
                return result;
            }  
        } catch (Exception e) {  
        	e.printStackTrace();
        } finally {  
            try {  
                httpclient.close(); 
                return result;
            } catch (IOException e) {  
                e.printStackTrace();
                return result;
            }  
        }
	}
	

}
*/
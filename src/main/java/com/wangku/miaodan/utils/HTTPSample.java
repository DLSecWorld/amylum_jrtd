/*package com.wangku.miaodan.utils;

*//**
 * Created by liujiabao on 2016/3/30.
 *//*

import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConstants;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HTTPSample {

    private static CloseableHttpClient httpClient = null;


    private static final String APPLICATION_JSON = "application/json";

    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";


    public static CloseableHttpClient httpClient(){
        if(httpClient!=null) return  HTTPSample.httpClient;
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        // Increase max total connection to 200
        cm.setMaxTotal(200);
        // Increase default max connection per route to 20
        cm.setDefaultMaxPerRoute(20);
        HTTPSample.httpClient = HttpClients.custom().setConnectionManager(cm).build();
        return httpClient;
    }
    public static String httpJsonGet(String url, Map<Object,Object> requestParams) {

        HttpGet httpGet = null;
        String result = "";
        try {
            // 参数设置
            StringBuilder builder = new StringBuilder(url);
            builder.append("?");
            for (Map.Entry<Object,Object> entry : requestParams.entrySet()) {
                builder.append(entry.getKey());
                builder.append("=");
                builder.append( entry.getValue());
                builder.append("&");
            }

            String tmpUrl = builder.toString();
            tmpUrl = tmpUrl.substring(0, tmpUrl.length() - 1);

            httpGet = new HttpGet(tmpUrl);
            httpGet.addHeader("Content-Type","application/json;charset=UTF-8");
//            System.out.println("executing request " + httpGet.getURI());
//            System.out.println("-------------------------------------");

            HttpResponse response = httpClient().execute(httpGet);

            // reponse header
//            System.out.println(response.getStatusLine().getStatusCode());

            Header[] headers = response.getAllHeaders();
//            for (Header header : headers) {
//                System.out.println(header.getName() + ": " + header.getValue());
//            }

//            System.out.println();

            // 网页内容
            HttpEntity httpEntity = response.getEntity();
//            System.out.println(EntityUtils.toString(httpEntity));
            if(response.getStatusLine().getStatusCode()==200){
                result = EntityUtils.toString(httpEntity);
            }else{
                result=null;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpGet != null) {
                httpGet.abort();
            }
        }
        return result;
    }
    public static String httpGet(String url, Map<Object,Object> requestParams) {

        HttpGet httpGet = null;
        String result = "";
        try {
            // 参数设置
            StringBuilder builder = new StringBuilder(url);
            builder.append("?");
            for (Map.Entry<Object,Object> entry : requestParams.entrySet()) {
                builder.append(entry.getKey());
                builder.append("=");
                builder.append( entry.getValue());
                builder.append("&");
            }

            String tmpUrl = builder.toString();
            tmpUrl = tmpUrl.substring(0, tmpUrl.length() - 1);

            httpGet = new HttpGet(tmpUrl);
//            System.out.println("executing request " + httpGet.getURI());
//            System.out.println("-------------------------------------");

            HttpResponse response = httpClient().execute(httpGet);

            // reponse header
//            System.out.println(response.getStatusLine().getStatusCode());

            Header[] headers = response.getAllHeaders();
//            for (Header header : headers) {
//                System.out.println(header.getName() + ": " + header.getValue());
//            }

//            System.out.println();

            // 网页内容
            HttpEntity httpEntity = response.getEntity();
//            System.out.println(EntityUtils.toString(httpEntity));
            if(response.getStatusLine().getStatusCode()==200){
                result = EntityUtils.toString(httpEntity);
            }else{
                result=null;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpGet != null) {
                httpGet.abort();
            }
        }
        return result;
    }

    public static String httpPostJson(String posturl, Map<String, String> requestParams, String urlEncode) {
        try {
            //创建连接
            URL url = new URL(posturl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

            connection.connect();

            //POST请求
            DataOutputStream out = new DataOutputStream(
                    connection.getOutputStream());
            JSONObject obj = new JSONObject();
            for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                obj.put((String) entry.getKey(), (String) entry.getValue());
            }
            out.write(obj.toString().getBytes());
            out.flush();
            out.close();

            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            reader.close();
            // 断开连接
            connection.disconnect();
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String httpPost(String url, Map<String, String> requestParams, String urlEncode) {

        HttpPost httpPost = null;
        String result = "";
        try {
            // 参数设置
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                params.add(new BasicNameValuePair((String) entry.getKey(),
                        (String) entry.getValue()));
            }

            httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params, urlEncode));

//            System.out.println("executing request " + httpPost.getURI());
//            System.out.println("-------------------------------------");

            // reponse header
            HttpResponse response = httpClient().execute(httpPost);
//            System.out.println(response.getStatusLine().getStatusCode());

            Header[] headers = response.getAllHeaders();
//            for (Header header : headers) {
//                System.out.println(header.getName() + ": " + header.getValue());
//            }

//            System.out.println();

            // 网页内容
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpPost != null) {
                httpPost.abort();
            }
        }
        return result;
    }
    public static String doPut(String urlStr,Map<Object,Object> paramMap) throws Exception{
        // 参数设置
        StringBuilder builder = new StringBuilder(urlStr);
        builder.append("?");
        for (Map.Entry<Object,Object> entry : paramMap.entrySet()) {
            builder.append(entry.getKey());
            builder.append("=");
            builder.append( entry.getValue());
            builder.append("&");
        }
        String tmpUrl = builder.toString();
        tmpUrl = tmpUrl.substring(0, tmpUrl.length() - 1);
        URL url = new URL(tmpUrl);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line ;
        String result ="";
        while( (line =br.readLine()) != null ){
            result += "/n"+line;
        }
        br.close();
        return result;
    }

}
*/
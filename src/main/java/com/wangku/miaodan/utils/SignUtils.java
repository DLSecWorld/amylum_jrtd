package com.wangku.miaodan.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * SignUtils
 *
 * @author zhaomeng
 * @date 2017/5/10
 */
public class SignUtils {

    public static String getSign(Map<String, String> params, String paternerKey) throws UnsupportedEncodingException {
        return MD5Utils.getMD5(createSign(params, false) + "&key=" + paternerKey).toUpperCase();
    }

    /**
     * 构造签名
     *
     * @param params
     * @param encode
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String createSign(Map<String, String> params, boolean encode) throws UnsupportedEncodingException {
        Set<String> keysSet = params.keySet();
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        StringBuffer temp = new StringBuffer();
        boolean first = true;
        for (Object key : keys) {
            if (key == null || StringUtil.isEmpty(params.get(key))) // 参数为空不参与签名
                continue;
            if (first) {
                first = false;
            } else {
                temp.append("&");
            }
            temp.append(key).append("=");
            Object value = params.get(key);
            String valueStr = "";
            if (null != value) {
                valueStr = value.toString();
            }
            if (encode) {
                temp.append(URLEncoder.encode(valueStr, "UTF-8"));
            } else {
                temp.append(valueStr);
            }
        }
        return temp.toString();
    }
}

package com.wangku.miaodan.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 创建时间：2016年11月2日 下午7:12:44
 * 
 * @author andy
 * @version 2.2
 */

public class PayUtil {

	/**
	 * 生成订单号
	 * 订单号14位时间+8位随机数
	 * @return
	 */
	public static String getTradeNo() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String str = simpleDateFormat.format(date);
		Random random = new Random();
		int num = (int) (random.nextDouble() * 90000000) + 10000000;
		return str + num;
	}

	/**
	 * 退款单号
	 * 
	 * @return
	 */
	public static String getRefundNo() {
		// 自增8位数 00000001
		return getTradeNo();
	}

	/**
	 * 退款单号
	 * 
	 * @return
	 */
	public static String getTransferNo() {
		// 自增8位数 00000001
		return getTradeNo();
	}

	/**
	 * 返回客户端ip
	 * 
	 * @param request
	 * @return
	 */
	public static String getRemoteAddrIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtil.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (StringUtil.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}

	/**
	 * 获取服务器的ip地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getLocalIp(HttpServletRequest request) {
		return request.getLocalAddr();
	}

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

	/**
	 * 创建支付随机字符串
	 * @return
	 */
	public static String getNonceStr(){
		return RandomUtil.randomString(RandomUtil.LETTER_NUMBER_CHAR, 32);
	}
	
	/**
	 * 支付时间戳
	 * @return
	 */
	public static String payTimestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}
}

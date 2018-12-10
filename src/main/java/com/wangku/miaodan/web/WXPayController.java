package com.wangku.miaodan.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

public class WXPayController {

/*	@Autowired
	PartnerConfig config;*/

	private static final Logger LOG = Logger.getLogger(WXPayController.class);

	private static final String ORDER_PAY = "https://api.mch.weixin.qq.com/pay/unifiedorder"; // 统一下单

	private static final String ORDER_PAY_QUERY = "https://api.mch.weixin.qq.com/pay/orderquery"; // 支付订单查询

	//JSAPI支付参数
	private static final String APP_ID = "wxef215e573ac9c61c";
	private static final String MCH_ID = "1473937202";		//商户号
	private static final String API_SECRET = "8ba334f893ad4226ae819641944985f9";	//支付秘钥
	private static final String ORDER_SECRET = "7ba334f893ad4226ae819641944985f9";	//订单验证秘钥


	@RequestMapping("/recharge")
	@ResponseBody
	public JSONObject recharge(HttpServletRequest request) {
		
		return null;
	}

/*
	@RequestMapping(value = "/pay", method = RequestMethod.GET)
	public JSONObject orderPay(HttpServletRequest request, HttpServletResponse response, String loginCode,Long orderId) {
		LOG.info("[/order/pay]");
		//来源终端,1:PC,2:微信H5,3:安卓,4:IOS
		String terminalType = request.getParameter("terminalType");
		LOG.info("WxPay type: " + terminalType);
		String openId = request.getParameter("openId");
		LOG.info("传入的openid: " + openId);
		JSONObject resultJson = new JSONObject();
		if(StringUtil.isEmpty(loginCode)||null == orderId){
			resultJson.put("code",-1);
			resultJson.put("msg","参数不正确");
			resultJson.put("result",null);
			return  resultJson;
		}
		//判断终端类型字段是否为空
		if(StringUtil.isEmpty(terminalType)) {
			resultJson.put("code",-1);
			resultJson.put("msg","参数不正确,缺失终端类型参数");
			resultJson.put("result",null);
			return  resultJson;
		}
		//判断微信H5是否传入openid参数
		if(terminalType.equals("2") && StringUtil.isEmpty(openId)) {
			resultJson.put("code",-1);
			resultJson.put("msg","参数不正确,缺失openId参数");
			resultJson.put("result",null);
			return  resultJson;
		}
		Map<String, String> restmap = null;
		boolean flag = true; // 是否订单创建成功
		String commodityName="";
		Double cashnum=null;
		String prepay_id="";
		try {
			Map map = new HashMap();
			map.put("orderCode", orderId);
//			String s = HTTPSample.httpJsonGet("http://hdporders-api.99114.com/order/public/beforePay", map);
			String s = HTTPSample.httpJsonGet(config.QUERY_ORDER_INFO, map);
			LOG.info("订单查询接口返回"+s);
			if(!StringUtil.isEmpty(s)){
				JSONObject jsonObject = JSONObject.parseObject(s);

				if(jsonObject.get("code").equals(200)){
					JSONObject order = jsonObject.getJSONObject("order");
					//判断订单状态是否可支付
					if (order.getInteger("organStatus") == 2){
						resultJson.put("code",-101);
						resultJson.put("msg","请求错误，该订单已经支付。");
						resultJson.put("result",null);
						return resultJson;
					}else if (order.getInteger("organStatus") == -1){
						resultJson.put("code",-101);
						resultJson.put("msg","请求错误，该订单已经超时关闭。");
						resultJson.put("result",null);
						return resultJson;
					}
					commodityName=order.getString("name_goods");
					cashnum=order.getDouble("money_order");
					LOG.info("下单商品名称为 ["+commodityName+"] 长度:"+commodityName.length());
					if(commodityName != null && commodityName.length() > 128) {
						commodityName = commodityName.substring(0, 127);
					}
				}else{
					LOG.info("订单创建失败：订单查询失败"+s);
					resultJson.put("code",-1);
					resultJson.put("msg","订单查询失败");
					resultJson.put("result",null);
				}
			}else{
				LOG.info("订单创建失败：接口返回为空");
				resultJson.put("code",-1);
				resultJson.put("msg","订单查询失败");
				resultJson.put("result",null);
			}
		}catch (Exception e){
			LOG.info("订单创建失败,系统出错");
			resultJson.put("code",-1);
			resultJson.put("msg","订单查询失败");
			resultJson.put("result",null);
		}
		if(!resultJson.isEmpty()){
			return resultJson;
		}
		PaymentWxUnifiedorder unifiedorder = paymentWxPayService.findUnifiedOrderByOutTradeNoAndResultCode(orderId.toString(),"SUCCESS");
		//if(unifiedorder==null){
			try {
				unifiedorder=new PaymentWxUnifiedorder();
				String total_fee = BigDecimal.valueOf(cashnum).multiply(BigDecimal.valueOf(100))
						.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
				Map<String, String> parm = new HashMap<String, String>();
				
				parm.put("device_info", "WEB");
				parm.put("nonce_str", PayUtil.getNonceStr());
				parm.put("sign_type", "MD5");
				parm.put("body", commodityName);
				parm.put("attach", "hdpPay");
				parm.put("out_trade_no", orderId+"");
				parm.put("total_fee", total_fee);
				parm.put("spbill_create_ip", "127.0.0.1");//PayUtil.getRemoteAddrIp(request)
				parm.put("notify_url", config.ipStr+"/wk/wsms/payment/wxPay/notify"); //微信服务器异步通知支付结果地址  下面的order/notify 方法
				
				//判断请求的来源(App/公众号)，调用不同的支付账号
				if(StringUtil.isNotEmpty(terminalType) && (terminalType.equals("3")||terminalType.equals("4"))) {
					parm.put("trade_type", "APP");
					parm.put("appid", APP_ID_APP);
					parm.put("mch_id", MCH_ID_APP);
					parm.put("sign", PayUtil.getSign(parm, API_SECRET_APP));
				}else {
					Map openIdMap=new HashMap();
					openIdMap.put("loginCode",loginCode);
					openIdMap.put("appId",APP_ID);
					//原有的openid需要根据用户信息获取绑定的openid，此逻辑不合理，已经改由前端传入
//					String openId = HTTPSample.httpGet(config.HDPUSER_URL+"weixin/getOpendId",openIdMap);
					if(StringUtil.isEmpty(openId)){
						LOG.info("订单创建失败：用户未登录，刷新页面");
						resultJson.put("code",-1);
						resultJson.put("msg","订单创建失败");
						resultJson.put("result",null);
						return resultJson;
					}
					parm.put("appid", APP_ID);
					parm.put("mch_id", MCH_ID);
					parm.put("openid",openId);
					parm.put("trade_type", "JSAPI");
					parm.put("sign", PayUtil.getSign(parm, API_SECRET));
				}
				
				String reqXml = XmlUtil.xmlFormat(parm, false);
				LOG.info("微信统一下单接口请求:" + reqXml);
				String restxml = HttpUtils.post(ORDER_PAY, reqXml);
				restmap = XmlUtil.xmlParse(restxml);
				LOG.info("微信统一下单接口返回："+restmap);
				unifiedorder.setId(TimeIdHelper.getIdByCurrentTimeMillisNext());
				unifiedorder.setAppId(restmap.get("appid"));
				unifiedorder.setMchId(restmap.get("mch_id"));
				unifiedorder.setNonceStr(restmap.get("nonce_str"));
				unifiedorder.setSign(restmap.get("sign"));
				unifiedorder.setResultCode(restmap.get("result_code"));
				unifiedorder.setErrCode(restmap.get("err_code"));
				unifiedorder.setErrCodeDes(restmap.get("err_code_des"));
				unifiedorder.setOutTradeNo(orderId.toString());
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
			if (CollectionUtil.isNotEmpty(restmap) && "SUCCESS".equals(restmap.get("result_code"))) {
				prepay_id=restmap.get("prepay_id");
				unifiedorder.setTradeType(restmap.get("trade_type"));
				unifiedorder.setPrepayId(restmap.get("prepay_id"));
				unifiedorder.setCodeUrl(restmap.get("code_url"));
				flag = true;
			}else{
				flag = false;
			}
			//paymentWxPayService.saveUnifiedorder(unifiedorder);
//		}else {
//			prepay_id = unifiedorder.getPrepayId();
//		}
		Map<String, String> payMap = new HashMap<String, String>();
		
		try {
			if(StringUtil.isNotEmpty(terminalType) && (terminalType.equals("3") || terminalType.equals("4"))) {
				payMap.put("appid", APP_ID_APP);
				payMap.put("partnerid", MCH_ID_APP);
				payMap.put("prepayid", prepay_id);
				payMap.put("package", "Sign=WXPay");
				payMap.put("noncestr", PayUtil.getNonceStr());
				payMap.put("timestamp", PayUtil.payTimestamp());
				payMap.put("sign", PayUtil.getSign(payMap, API_SECRET_APP));
//			}else if(StringUtil.isNotEmpty(terminalType)&&terminalType.equals("4")) {
//				payMap.put("partnerId", MCH_ID_APP);
//				payMap.put("prepayId", prepay_id);
//				payMap.put("package", "Sign=WXPay");
//				payMap.put("sign", PayUtil.getSign(payMap, API_SECRET_APP));
			}
			else {
				payMap.put("package", "prepay_id="+prepay_id);
				payMap.put("appId", APP_ID);
				payMap.put("signType", "MD5");
				payMap.put("nonceStr", PayUtil.getNonceStr());
				payMap.put("timeStamp", PayUtil.payTimestamp());
				payMap.put("sign", PayUtil.getSign(payMap, API_SECRET));
			}
		} catch (Exception e) {
			flag = false;
		}
		if (flag) {
			LOG.info("订单获取成功："+payMap);
			resultJson.put("code",200);
			resultJson.put("msg","订单获取成功");
			resultJson.put("result",payMap);
			return resultJson;
		} else {
			if (CollectionUtil.isNotEmpty(restmap)) {
				LOG.info("订单创建失败：" + restmap.get("err_code") + ":" + restmap.get("err_code_des"));
			}
			resultJson.put("code",500);
			resultJson.put("msg","订单创建失败");
			resultJson.put("result",null);
			return resultJson;
		}
	}


	*//**
	 * 查询支付结果
	 *
	 * @param request
	 * @param response
	 * @param tradeno 商品订单号
	 *//*
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public JSONObject orderPayQuery(HttpServletRequest request, HttpServletResponse response, String tradeno) {
		LOG.info("[/order/pay/query]");
		Boolean flag=false;
		JSONObject resultJson = new JSONObject();
		if(StringUtil.isEmpty(tradeno)){
			resultJson.put("code",-1);
			resultJson.put("msg","参数不正确");
			resultJson.put("result",null);
			return  resultJson;
		}
		PaymentWxPayDataLog byOutTradeNo = paymentWxPayService.findByOutTradeNo(tradeno);
		if(byOutTradeNo==null){
			flag=true;
		}else{
			if(byOutTradeNo.getResultCode().equals("SUCCESS")){
				resultJson.put("code",200);
				resultJson.put("msg","订单支付成功");
				resultJson.put("result","SUCCESS");
				return resultJson;
			}else {
				flag=true;
			}
		}
		if(flag){
			Map<String, String> restmap = null;
			try {
				Map<String, String> parm = new HashMap<String, String>();

				parm.put("appid", APP_ID);
				parm.put("mch_id", MCH_ID);
				//parm.put("transaction_id", tradeid);
				parm.put("out_trade_no", tradeno);
				parm.put("nonce_str", PayUtil.getNonceStr());
				parm.put("sign", PayUtil.getSign(parm, API_SECRET));
				String restxml = HttpUtils.post(ORDER_PAY_QUERY, XmlUtil.xmlFormat(parm, false));
				restmap = XmlUtil.xmlParse(restxml);
				LOG.info(tradeno+"JSAPI订单查询结果"+restmap);
				
				//JSAPI支付参数   查询为空    使用app支付再次查询
				if(CollectionUtil.isEmpty(restmap) || !"SUCCESS".equals(restmap.get("result_code"))) {
					Map<String, String> appParm = new HashMap<String, String>();

					appParm.put("appid", APP_ID_APP);
					appParm.put("mch_id", MCH_ID_APP);
					appParm.put("out_trade_no", tradeno);
					appParm.put("nonce_str", PayUtil.getNonceStr());
					appParm.put("sign", PayUtil.getSign(appParm, API_SECRET_APP));
					String apprestxml = HttpUtils.post(ORDER_SECRET_APP, XmlUtil.xmlFormat(appParm, false));
					restmap = XmlUtil.xmlParse(apprestxml);
					LOG.info(tradeno+"APP订单查询结果"+restmap);
				}
				
				
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
			if (CollectionUtil.isNotEmpty(restmap) && "SUCCESS".equals(restmap.get("result_code"))) {
				// 订单查询成功 处理业务逻辑
				LOG.info("订单查询：订单" + restmap.get("out_trade_no") + "支付成功");
				Map map = new HashMap();
				map.put("orderCode", tradeno);
				String s = HTTPSample.httpJsonGet(config.QUERY_ORDER_INFO, map);
				if(!StringUtil.isEmpty(s)){
					JSONObject jsonObject = JSONObject.parseObject(s);
					if(jsonObject.get("code").equals("200")){
						PaymentWxPayDataLog p = new PaymentWxPayDataLog();
						p.setId(TimeIdHelper.getIdByCurrentTimeMillisNext());
						p.setAppId(restmap.get("appid"));
						p.setMchId(restmap.get("mch_id"));
						p.setDeviceInfo(restmap.get("device_info"));
						p.setResultCode(restmap.get("result_code"));
						p.setErrCode(restmap.get("err_code"));
						p.setErrCodeDes(restmap.get("err_code_des"));
						p.setOpenid(restmap.get("openid"));
						p.setIsSubscribe(restmap.get("is_subscribe"));
						p.setTradeType(restmap.get("trade_type"));
						p.setBankType(restmap.get("bank_type"));
						p.setTotalFee(Integer.valueOf(restmap.get("total_fee")));
						String settlement_total_fee = restmap.get("settlement_total_fee");
						if(!StringUtil.isEmpty(settlement_total_fee)){
							p.setSettlementTotalFee(Integer.valueOf(settlement_total_fee));
						}
						p.setFeeType(restmap.get("fee_type"));
						p.setCashFee(Integer.valueOf(restmap.get("cash_fee")));
						p.setCashFeeType(restmap.get("cash_fee_type"));
						p.setTransactionId(restmap.get("transaction_id"));
						p.setOutTradeNo(restmap.get("out_trade_no"));
						p.setTimeEnd(restmap.get("time_end"));
						paymentWxPayService.savePayDataLog(p);
						// 通过商户订单判断是否该订单已经处理 如果处理跳过 如果未处理先校验sign签名 再进行订单业务相关的处理
						JSONObject order = jsonObject.getJSONObject("order");
						if(order.get("organStatus").equals(1)){
							String sing = restmap.get("sign"); // 返回的签名
							restmap.remove("sign");
							String signnow = null;
							try {
								signnow = PayUtil.getSign(restmap, restmap.get("trade_type").equals("APP")?API_SECRET_APP:API_SECRET);
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							if (signnow.equals(sing)) {
								LOG.info("订单支付通知： 支付成功，订单号" + tradeno);
								//只负责通知
								//通知订单支付成功
//								Map orderMap = new HashMap();
//								orderMap.put("result_code",restmap.get("result_code"));
//								orderMap.put("err_code",restmap.get("err_code"));
//								orderMap.put("err_code_des",restmap.get("err_code_des"));
//								orderMap.put("trade_type",restmap.get("trade_type"));
//								orderMap.put("out_trade_no",restmap.get("out_trade_no"));
//								orderMap.put("time_end",restmap.get("time_end"));
//								orderMap.put("total_fee",restmap.get("total_fee"));
								HTTPSample.httpPostJson(config.ORDER_WXNOTIFY_URL, restmap, "UTF-8");
								resultJson.put("code",200);
								resultJson.put("msg","订单支付成功");
								resultJson.put("result","SUCCESS");
								//result.put("return_code","SUCCESS");
							} else {
								LOG.info("订单支付通知：签名错误");
								resultJson.put("code",-1);
								resultJson.put("msg","签名错误");
								resultJson.put("result","FAIL");
							}
						}else{
							resultJson.put("code",-1);
							resultJson.put("msg","订单支付成功");
							resultJson.put("result","SUCCESS");
						}
					}else{
						LOG.error("订单查询失败"+s);
						resultJson.put("code",-1);
						resultJson.put("msg","订单查询失败");
						resultJson.put("result","FAIL");
					}
				}else{
					LOG.error("订单查询接口返回为空");
					resultJson.put("code",-1);
					resultJson.put("msg","订单查询接口返回为空");
					resultJson.put("result","FAIL");
				}
			} else {
				if (CollectionUtil.isNotEmpty(restmap)) {
					LOG.info("订单支付失败：" + restmap.get("err_code") + ":" + restmap.get("err_code_des"));
				}
				resultJson.put("code",-1);
				resultJson.put("msg","订单支付失败");
				resultJson.put("result","FAIL");
			}
		}
		return resultJson;
	}

	@RequestMapping(value = "/wechatQueryInfo", method = RequestMethod.POST)
	public JSONObject wechatQueryInfo(HttpServletRequest request, HttpServletResponse response, String tradeno) throws Exception {

		Map<String, String> restmap = null;
		JSONObject itemJSONObj = new JSONObject();
		try {
			Map<String, String> parm = new HashMap<String, String>();

			parm.put("appid", APP_ID);
			parm.put("mch_id", MCH_ID);
			parm.put("out_trade_no", tradeno);
			parm.put("nonce_str", PayUtil.getNonceStr());
			parm.put("sign", PayUtil.getSign(parm, API_SECRET));
			String restxml = HttpUtils.post(ORDER_PAY_QUERY, XmlUtil.xmlFormat(parm, false));
			restmap = XmlUtil.xmlParse(restxml);
			LOG.info(tradeno + "JSAPI订单查询结果" + restmap);

			//JSAPI支付参数   查询为空    使用app支付再次查询
			if (CollectionUtil.isEmpty(restmap) || !"SUCCESS".equals(restmap.get("result_code"))) {
				Map<String, String> appParm = new HashMap<String, String>();
				appParm.put("appid", APP_ID_APP);
				appParm.put("mch_id", MCH_ID_APP);
				appParm.put("out_trade_no", tradeno);
				appParm.put("nonce_str", PayUtil.getNonceStr());
				appParm.put("sign", PayUtil.getSign(appParm, API_SECRET_APP));
				String apprestxml = HttpUtils.post(ORDER_SECRET_APP, XmlUtil.xmlFormat(appParm, false));
				restmap = XmlUtil.xmlParse(apprestxml);
				LOG.info(tradeno + "APP订单查询结果" + restmap);
			}

			itemJSONObj = JSONObject.parseObject(JSON.toJSONString(restmap));

		}catch (Exception e){

		}
		return itemJSONObj;
	}
	*//**
	 * 订单支付微信服务器异步通知
	 *
	 * @param request
	 * @param response
	 *//*
	@RequestMapping("/notify")
	public String orderPayNotify(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("[/order/pay/notify]");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml");
		Map resultMap = new HashMap();
		try {
			ServletInputStream in = request.getInputStream();
			String resxml = FileUtil.readInputStream2String(in);
			Map<String, String> restmap = XmlUtil.xmlParse(resxml);
			LOG.info("支付结果通知：" + restmap);
			if ("SUCCESS".equals(restmap.get("return_code"))) {
				// 订单支付成功 业务处理
				String out_trade_no = restmap.get("out_trade_no"); // 商户订单号
				PaymentWxPayDataLog byOutTradeNo = paymentWxPayService.findByOutTradeNo(out_trade_no);
				if(byOutTradeNo==null) {
					PaymentWxPayDataLog p = new PaymentWxPayDataLog();
					p.setId(TimeIdHelper.getIdByCurrentTimeMillisNext());
					p.setAppId(restmap.get("appid"));
					p.setMchId(restmap.get("mch_id"));
					p.setDeviceInfo(restmap.get("device_info"));
					p.setResultCode(restmap.get("result_code"));
					p.setErrCode(restmap.get("err_code"));
					p.setErrCodeDes(restmap.get("err_code_des"));
					p.setOpenid(restmap.get("openid"));
					p.setIsSubscribe(restmap.get("is_subscribe"));
					p.setTradeType(restmap.get("trade_type"));
					p.setBankType(restmap.get("bank_type"));
					p.setTotalFee(Integer.valueOf(restmap.get("total_fee")));
					String settlement_total_fee = restmap.get("settlement_total_fee");
					if(!StringUtil.isEmpty(settlement_total_fee)){
						p.setSettlementTotalFee(Integer.valueOf(settlement_total_fee));
					}
					p.setFeeType(restmap.get("fee_type"));
					p.setCashFee(Integer.valueOf(restmap.get("cash_fee")));
					p.setCashFeeType(restmap.get("cash_fee_type"));
					p.setTransactionId(restmap.get("transaction_id"));
					p.setOutTradeNo(restmap.get("out_trade_no"));
					p.setTimeEnd(restmap.get("time_end"));
					paymentWxPayService.savePayDataLog(p);
				}
				Map map = new HashMap();
				map.put("orderCode", out_trade_no);
				String s = HTTPSample.httpJsonGet(config.QUERY_ORDER_INFO, map);
				LOG.info("订单查询信息："+s);
				if(!StringUtil.isEmpty(s)){
					JSONObject jsonObject = JSONObject.parseObject(s);
					if(jsonObject.get("code").equals(200)){
						// 通过商户订单判断是否该订单已经处理 如果处理跳过 如果未处理先校验sign签名 再进行订单业务相关的处理
						JSONObject order = jsonObject.getJSONObject("order");
						if(order.get("organStatus").equals(1)){
							String sing = restmap.get("sign"); // 返回的签名
							restmap.remove("sign");
							String signnow = PayUtil.getSign(restmap, restmap.get("trade_type").equals("APP")?API_SECRET_APP:API_SECRET);
							if (signnow.equals(sing)) {
								LOG.info("订单支付通知： 支付成功，订单号" + out_trade_no);
								//通知订单支付成功
//								Map orderMap = new HashMap();
//								orderMap.put("result_code",restmap.get("result_code"));
//								orderMap.put("err_code",restmap.get("err_code"));
//								orderMap.put("err_code_des",restmap.get("err_code_des"));
//								orderMap.put("trade_type",restmap.get("trade_type"));
//								orderMap.put("out_trade_no",restmap.get("out_trade_no"));
//								orderMap.put("time_end",restmap.get("time_end"));
//								orderMap.put("total_fee",restmap.get("total_fee"));
								//只负责通知
								HTTPSample.httpPostJson(config.ORDER_WXNOTIFY_URL, restmap, "UTF-8");
								resultMap.put("return_code","SUCCESS");
							} else {
								LOG.info("订单支付通知：签名错误");
								resultMap.put("return_code","FAIL");
							}
						}else{
							resultMap.put("return_code","SUCCESS");
						}
					}else{
						LOG.error("订单查询失败"+s);
						resultMap.put("return_code","FAIL");
					}
				}else{
					LOG.error("订单查询接口返回为空");
					resultMap.put("return_code","FAIL");
				}
			} else {
				LOG.error("订单支付通知：支付失败，" + restmap.get("err_code") + ":" + restmap.get("err_code_des"));
				resultMap.put("return_code","FAIL");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			resultMap.put("return_code","FAIL");
		}
		return XmlUtil.xmlFormat(resultMap,true);
	}

	*//**
	 * 订单退款 需要双向证书验证
	 *
	 * @param request
	 * @param response
	 * @param orderNo  商家订单号
	 *//*
	@RequestMapping(value = "/refund", method = RequestMethod.POST)
	public JSONObject orderPayRefund(HttpServletRequest request, HttpServletResponse response,String refundNo ,String orderNo,String refundNum,String totalNum,String refundSign) {
		LOG.info("[/pay/refund]");
		JSONObject resultJson = new JSONObject();
		if (null == refundNum && StringUtil.isEmpty(orderNo)) {
			resultJson.put("code",-1);
			resultJson.put("msg","订单号或退款金额不能为空");
			resultJson.put("result","FAIL");
			LOG.info("订单退款失败："+resultJson);
			return resultJson;
		}
		if (null == refundSign || StringUtil.isEmpty(refundSign)) {
			resultJson.put("code",-1);
			resultJson.put("msg","签名不能为空");
			resultJson.put("result","FAIL");
			LOG.info("订单退款失败："+resultJson);
			return resultJson;
		}
		//验证签名
		try {
			Map signMap = new HashMap();
			signMap.put("refundNo",refundNo);
			signMap.put("orderNo",orderNo);
			signMap.put("refundNum",refundNum);
			signMap.put("totalNum",totalNum);
			 String sign = SignUtils.getSign(signMap, ORDER_SECRET);
			if(!refundSign.equals(sign)){
				resultJson.put("code",-1);
				resultJson.put("msg","签名错误");
				resultJson.put("result","FAIL");
				LOG.info("订单退款失败："+resultJson);
				return resultJson;
			}
		} catch (UnsupportedEncodingException e) {
			LOG.info("订单退款失败：" + e.getMessage() + ":" + e);
			resultJson.put("code",-1);
			resultJson.put("msg",e.getMessage());
			resultJson.put("result","FAIL");
			LOG.info("订单退款失败："+resultJson);
			return resultJson;
		}
		Map<String, String> restmap = null;
		PaymentWxRefund refund=new PaymentWxRefund();
		try {
			String refund_fee = BigDecimal.valueOf(Double.valueOf(refundNum)).multiply(BigDecimal.valueOf(100))
					.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			String total_free = BigDecimal.valueOf(Double.valueOf(totalNum)).multiply(BigDecimal.valueOf(100))
					.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			Map<String, String> parm = new HashMap<String, String>();
			parm.put("appid", APP_ID);
			parm.put("mch_id", MCH_ID);
			parm.put("nonce_str", PayUtil.getNonceStr());
			parm.put("out_trade_no", orderNo);//订单号
			parm.put("out_refund_no", refundNo); //退款单号
			parm.put("total_fee", total_free); // 订单总金额 从业务逻辑获取
			parm.put("refund_fee", refund_fee); // 退款金额
			parm.put("op_user_id", MCH_ID);
			parm.put("refund_account", "REFUND_SOURCE_RECHARGE_FUNDS");//退款方式
			parm.put("sign", PayUtil.getSign(parm, API_SECRET));
			String restxml = HttpUtils.posts(ORDER_REFUND, XmlUtil.xmlFormat(parm, false));
			restmap = XmlUtil.xmlParse(restxml);
			LOG.info("退款接口请求响应："+restmap);
			refund.setId(TimeIdHelper.getIdByCurrentTimeMillisNext());
			refund.setAppId(APP_ID);
			refund.setMchId(MCH_ID);
			refund.setOpUserId(MCH_ID);
			refund.setOutTradeNo(orderNo);
			refund.setOutRefundNo(refundNo);
			refund.setTotalFee(Integer.valueOf(total_free));
			refund.setRefundFee(Integer.valueOf(refund_fee));
			refund.setRefundAccount("NOT KNOW");
			refund.setRefundFeeType(restmap.get("refund_fee_type"));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		Map<String, String> refundMap = new HashMap<>();
		if (CollectionUtil.isNotEmpty(restmap) && "SUCCESS".equals(restmap.get("result_code"))) {
			refund.setRefundStatus(1);
			refundMap.put("transaction_id", restmap.get("transaction_id"));
			refundMap.put("out_trade_no", restmap.get("out_trade_no"));
			refundMap.put("refund_id", restmap.get("refund_id"));
			refundMap.put("out_refund_no", restmap.get("out_refund_no"));
			LOG.info("订单退款：订单" + restmap.get("out_trade_no") + "退款成功，商户退款单号" + restmap.get("out_refund_no") + "，微信退款单号"
					+ restmap.get("refund_id"));
			resultJson.put("code",200);
			resultJson.put("msg","订单退款成功");
			resultJson.put("result","SUCCESS");
		} else {
			refund.setRefundStatus(0);
			if (CollectionUtil.isNotEmpty(restmap)) {
				LOG.info("订单退款失败：" + restmap.get("return_msg") + ":" + restmap.get("return_code"));
			}
			resultJson.put("code",500);
			resultJson.put("msg","订单退款失败");
			resultJson.put("result","FAIL");
		}
		paymentWxPayService.saveRefundLog(refund);
        LOG.info("订单退款结果："+resultJson);
		return resultJson;
	}

	*//**
	 * 订单退款查询
	 * @param request
	 * @param response
	 * @param tradeid 微信订单号
	 * @param tradeno 商户订单号
	 * @param refundid 微信退款号
	 * @param refundno 商家退款号
	 * @param callback
	 *//*
	@RequestMapping(value = "/pay/refund/query", method = RequestMethod.POST)
	public void orderPayRefundQuery(HttpServletRequest request, HttpServletResponse response, String refundid,
									String refundno, String tradeid, String tradeno, String callback) {
		LOG.info("[/pay/refund/query]");
		if (StringUtil.isEmpty(tradeid) && StringUtil.isEmpty(tradeno)
				&& StringUtil.isEmpty(refundno) && StringUtil.isEmpty(refundid)) {
//			WebUtil.response(response, WebUtil.packJsonp(callback, JSON
//					.toJSONString(new JsonResult(-1, "退单号或订单号不能为空", new ResponseData()), SerializerFeatureUtil.FEATURES)));
		}

		Map<String, String> restmap = null;
		try {
			Map<String, String> parm = new HashMap<String, String>();
			parm.put("appid", APP_ID);
			parm.put("mch_id", MCH_ID);
			parm.put("transaction_id", tradeid);
			parm.put("out_trade_no", tradeno);
			parm.put("refund_id", refundid);
			parm.put("out_refund_no", refundno);
			parm.put("nonce_str", PayUtil.getNonceStr());
			parm.put("sign", PayUtil.getSign(parm, API_SECRET));

			String restxml = HttpUtils.post(ORDER_REFUND_QUERY, XmlUtil.xmlFormat(parm, false));
			restmap = XmlUtil.xmlParse(restxml);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		Map<String, String> refundMap = new HashMap<>();
		if (CollectionUtil.isNotEmpty(restmap) && "SUCCESS".equals(restmap.get("result_code")) && "SUCCESS".equals(restmap.get("result_code"))) {
			// 订单退款查询成功 处理业务逻辑
			LOG.info("退款订单查询：订单" + restmap.get("out_trade_no") + "退款成功，退款状态"+ restmap.get("refund_status_0"));
			refundMap.put("transaction_id", restmap.get("transaction_id"));
			refundMap.put("out_trade_no", restmap.get("out_trade_no"));
			refundMap.put("refund_id", restmap.get("refund_id_0"));
			refundMap.put("refund_no", restmap.get("out_refund_no_0"));
			refundMap.put("refund_status", restmap.get("refund_status_0"));
//			WebUtil.response(response, WebUtil.packJsonp(callback, JSON
//					.toJSONString(new JsonResult(1, "订单退款成功", new ResponseData(null, refundMap)), SerializerFeatureUtil.FEATURES)));
		} else {
			if (CollectionUtil.isNotEmpty(restmap)) {
				LOG.info("订单退款失败：" + restmap.get("err_code") + ":" + restmap.get("err_code_des"));
			}
//			WebUtil.response(response, WebUtil.packJsonp(callback, JSON
//					.toJSONString(new JsonResult(-1, "订单退款失败", new ResponseData()), SerializerFeatureUtil.FEATURES)));
		}
	}

	
*/
}

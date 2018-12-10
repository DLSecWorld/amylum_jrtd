package com.wangku.miaodan.web;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jpay.ext.kit.IpKit;
import com.jpay.ext.kit.PaymentKit;
import com.jpay.ext.kit.StrKit;
import com.jpay.util.HttpUtils;
import com.jpay.weixin.api.WxPayApi;
import com.jpay.weixin.api.WxPayApi.TradeType;
import com.jpay.weixin.api.WxPayApiConfig;
import com.wangku.miaodan.core.interceptor.LoginInterceptor;
import com.wangku.miaodan.core.model.PaymentOrder;
import com.wangku.miaodan.core.model.Recharge;
import com.wangku.miaodan.core.service.IPaymentOrderService;
import com.wangku.miaodan.core.service.IRechargeService;
import com.wangku.miaodan.core.service.IUserService;
import com.wangku.miaodan.utils.FileUtil;
import com.wangku.miaodan.utils.XmlUtil;

@Controller
@RequestMapping("/pay")
public class PayMentController {
	
	private static final Logger LOG = Logger.getLogger(PayMentController.class);
	
	private static final String APP_ID = "wx48d18bd5531afd7f";
	private static final String MCH_ID = "1513645561";		//商户号
	private static final String APP_SECRET =   "661b8f722c83876622cec0b96afc3942";	//商户秘钥
	private static final String API_SECRET =   "73deac76e4824640b38595049f2ff61e";	//支付秘钥
	
	private static final String GET_OAUTH_CODE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=%s&scope=%s&state=%s#wechat_redirect";
	
	private static final String GET_OAUTH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
	
	private static final String NOTIFY_URL = "https://mp.dogao.cn/pay/order/notify";
	
	@Autowired
	private IRechargeService rechargeService;
	
	@Autowired
	private IPaymentOrderService paymentService;
	
	@Autowired
	private IUserService userService;
	
	@RequestMapping("/getopenid")
	private void getOpenId(HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
		String redirectUrl = request.getParameter("redirecturl");
		try {
			if (code != null) {
				String result = HttpUtils.get(String.format(GET_OAUTH_TOKEN, APP_ID, APP_SECRET, code));
				Map access = JSON.parseObject(result, Map.class);
				Object object = access.get("openid");
				String openId = access.get("openid") == null? "": access.get("openid").toString();
				if (redirectUrl.indexOf("?") >= 0 && !openId.isEmpty()) {
					response.sendRedirect(redirectUrl + "&openid=" + openId);
				} else if (redirectUrl.indexOf("?") < 0 && !openId.isEmpty()){
					response.sendRedirect(redirectUrl + "?openid=" + openId);
				}
			} else {
	            String redirectUrl4Vx = "https://mp.dogao.cn/pay/getoprnid?redirectUrl=" + redirectUrl;
	            String url = String.format(GET_OAUTH_CODE, APP_ID, URLEncoder.encode(redirectUrl,"UTF-8"), "code", "snsapi_base", "123");
				response.sendRedirect(url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("")
	@ResponseBody
	public Map<String, Object> pay(Long orderId, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		String mobile = LoginInterceptor.getMobile(request);
		// 验证用户
		if (mobile == null) {
			result.put("code", 602);
			result.put("msg", "用户未登录，请刷新页面");
			return result;
		}
		
		Recharge recharge = rechargeService.getDetailByIdAndMobile(orderId, LoginInterceptor.getMobile(request));
		
		// 验证订单是否存在
		if (recharge == null) {
			result.put("code", 601);
			result.put("msg", "订单不存在");
			return result;
		}
		
		Map<String, String> params = createUOrder(recharge, request, userService.getDetailByMobile(mobile).getOpenId());
		LOG.info("微信统一下单接口:" + params);	
		String restXml = WxPayApi.pushOrder(false, params);
		LOG.info("微信统一下单接口返回值:" + restXml);
		Map<String, String> restMap = PaymentKit.xmlToMap(restXml);
		String returnCode = restMap.get("return_code");
		String returnMsg = restMap.get("return_msg");
		if (!PaymentKit.codeIsOK(returnCode)) {
			LOG.error("订单创建失败:" +  returnMsg);
			result.put("code", 605);
			result.put("msg", "订单创建失败:" + returnMsg);		
			return result;
		}
		
		String resultCode = restMap.get("result_code");
		if (!PaymentKit.codeIsOK(resultCode)) {
			LOG.error("订单创建失败:" +  returnMsg);
			result.put("code", 605);
			result.put("msg", "订单创建失败:" + returnMsg);	
			return result;
		}
		
		String prepayId = restMap.get("prepay_id");
		String mwebUrl = restMap.get("mweb_url");
		LOG.info("微信统一订单创建成功, prepay_id:" + prepayId + ",mweb_url:" + mwebUrl);
		
		// 微信支付订单创建成功
		PaymentOrder order = new PaymentOrder();
		order.setAppid(restMap.get("appid"));
		order.setMchId(restMap.get("mch_id"));
		order.setNonceStr(restMap.get("nonce_str"));
		order.setSign(restMap.get("sign"));
		order.setNumber(recharge.getNumber());
		order.setTradeType(restMap.get("trade_type"));
		long tim = System.currentTimeMillis()/1000;
		result.put("code", 200);
		result.put("msg", "订单创建成功");
		Map<String, String> map = new HashMap<String, String>();
		map.put("appId", APP_ID);
		map.put("nonceStr", recharge.getNumber());
		map.put("timeStamp", String.valueOf(tim));
		map.put("package",  "prepay_id=" + restMap.get("prepay_id"));
		map.put("signType", "MD5");
		map.put("paySign", PaymentKit.createSign(map, API_SECRET));
		result.put("pack", "prepay_id=" + restMap.get("prepay_id"));
		result.put("order", map);
		return result;
	}
	
	@RequestMapping("/order/notify")
	@ResponseBody
	public String notify(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml");
		Map resultMap = new HashMap();
		try {
			String resxml = FileUtil.readInputStream2String(request.getInputStream());
			Map<String, String> restmap = XmlUtil.xmlParse(resxml);
			LOG.info("支付结果通知：" + restmap);
			String out_trade_no = restmap.get("out_trade_no");
			if ("SUCCESS".equals(restmap.get("return_code"))) {
				if (rechargeService.getdetailByNumber(out_trade_no).getStatus() == 0) {
					userService.recharge(restmap);
					rechargeService.notifySuccessByNumber(out_trade_no);
				} else {
					LOG.error("订单支付通知:支付已完成....");
				}
				resultMap.put("return_code", "SUCCESS");
			} else {
				rechargeService.notifyFailByNumber(out_trade_no);
				LOG.error("订单支付通知：支付失败，" + out_trade_no  + restmap.get("err_code") + ":" + restmap.get("err_code_des"));
				resultMap.put("return_code","FAIL");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			resultMap.put("return_code","FAIL");
		}
		return XmlUtil.xmlFormat(resultMap,true);
	}	
	
	private Map<String, String> createUOrder(Recharge recharge, HttpServletRequest request, String openId) {
		String ip = IpKit.getRealIp(request);
		if (StrKit.isBlank(ip)) {
			ip = "127.0.0.1";
		}
		WxPayApiConfig apiConfig = WxPayApiConfig.New()
				.setAppId(APP_ID)// 公众号ID
				.setMchId(MCH_ID)// 商户号ID
				.setPaternerKey(API_SECRET)
				.setTradeType(TradeType.JSAPI)// 交易类型()
				.setOpenId(openId)
				.setNonceStr(recharge.getNumber())// 随机字符串(订单ID)
				.setBody("今日推单充值")//商品简单描述
				.setAttach(recharge.getProduct().toString())// 附加数据
				.setOutTradeNo(recharge.getNumber())// 商户侧订单ID
				.setTotalFee(String.valueOf(recharge.getSum().multiply(new BigDecimal(100)).intValue()))// 交易总额
				.setSpbillCreateIp(ip)// 用户端IP
				.setNotifyUrl(NOTIFY_URL);// 支付成功异步通知地址
		return apiConfig.build();
	}
	
}

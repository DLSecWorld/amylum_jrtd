package com.wangku.miaodan.utils.message.aliyun;

import org.apache.log4j.Logger;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * 描述：MessageUtils.java
 */
public class MessageUtils {
	
	private static final Logger log = Logger.getLogger(MessageUtils.class);
	
    private static final String PRODUCT = "Dysmsapi";
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";
    private static final String ACCESS_KEY_ID = "LTAIbYIUeStqt7ui";
    private static final String ACCESS_KEY_SECRET = "4JVG4dM3Kew9v4RRCX0xGvdGb24gFH";
    private static final String SIGN_NAME = "今日推单";
    private static final String TEMPALTE_CODE = "SMS_151500224";
    private static IAcsClient client = null;
    
    static {
        System.setProperty("sun.net.client.defaultConnectTimeout", "20000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
		} catch (Exception e) {
			e.printStackTrace();
		}
        client = new DefaultAcsClient(profile);   	
    } 
    
    public static void sendSms(String mobile,String verifyCode) {
    	log.info("发送短信[" + verifyCode + "]到手机号:" + mobile);
        SendSmsRequest request = new SendSmsRequest();
		request.setSignName(SIGN_NAME);
		request.setTemplateCode(TEMPALTE_CODE);        
        request.setPhoneNumbers(mobile);
        request.setTemplateParam("{\"code\":\"" + verifyCode + "\"}");
        SendSmsResponse response = null;
		try {
			response = client.getAcsResponse(request);
			log.info("成功发送短信[" + verifyCode + "]到手机号:" + mobile + "; 返回值参数[code=" + response.getCode() + ",Message=" + response.getMessage() + ",requestId=" + response.getRequestId() + ",bizid=" + response.getBizId());
			// 发送完成进行 短信发送日志记录入库(消息), 用于后续进行结果查证和问题排查
		} catch (Exception e) {
			log.error("发送短信[" + verifyCode + "]到手机号:" + mobile + ", 系统报错:" + e.getStackTrace());
			e.printStackTrace();
		}
    }

}

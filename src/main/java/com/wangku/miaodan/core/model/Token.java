package com.wangku.miaodan.core.model;

public class Token {

	private String mobile;

	private String ticket;

	private String verifyCode;

	private Long verifyInvalidTime;

	public Token() {

	}

	public Token(String mobile, String ticket, String verifycode,
			long verifyInvalidTime) {
		this.mobile = mobile;
		this.ticket = ticket;
		this.verifyCode = verifycode;
		this.verifyInvalidTime = verifyInvalidTime;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public Long getVerifyInvalidTime() {
		return verifyInvalidTime;
	}

	public void setVerifyInvalidTime(Long verifyInvalidTime) {
		this.verifyInvalidTime = verifyInvalidTime;
	}

}
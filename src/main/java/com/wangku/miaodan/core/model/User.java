package com.wangku.miaodan.core.model;

import java.util.Date;

public class User {
	
	private Long id;

	private String name;

	private String identity;

	private String pic;

	private String openId;

	private String mobile;

	private String city;

	private String compName;

	private String compTel;

	private String micrMess;

	private String picIdentity;

	private String picIdentityBack;

	private String picCard;

	private String picLogo;

	private Integer status;

	private String reason;

	private int times;

	private int tdTimes;

	private Date addTime;

	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getCompTel() {
		return compTel;
	}

	public void setCompTel(String compTel) {
		this.compTel = compTel;
	}

	public String getMicrMess() {
		return micrMess;
	}

	public void setMicrMess(String micrMess) {
		this.micrMess = micrMess;
	}

	public String getPicIdentity() {
		return picIdentity;
	}

	public void setPicIdentity(String picIdentity) {
		this.picIdentity = picIdentity;
	}

	public String getPicIdentityBack() {
		return picIdentityBack;
	}

	public void setPicIdentityBack(String picIdentityBack) {
		this.picIdentityBack = picIdentityBack;
	}

	public String getPicCard() {
		return picCard;
	}

	public void setPicCard(String picCard) {
		this.picCard = picCard;
	}

	public String getPicLogo() {
		return picLogo;
	}

	public void setPicLogo(String picLogo) {
		this.picLogo = picLogo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getTdTimes() {
		return tdTimes;
	}

	public void setTdTimes(int tdTimes) {
		this.tdTimes = tdTimes;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
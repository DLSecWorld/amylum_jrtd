package com.wangku.miaodan.core.model;

import java.util.Date;

public class Requit {
	
    private Long id;
    
    private Long userId;
    
    private Long orderId;
    
    private String name;
    
    private String mobile;

    private String productName;

    private String productMobile;
    
    private int sum;

    private String requitReason;
    
    private String other;

    private Byte status;

    private String reason;

    private Date addTime;

    private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductMobile() {
		return productMobile;
	}

	public void setProductMobile(String productMobile) {
		this.productMobile = productMobile;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	public String getRequitReason() {
		return requitReason;
	}

	public void setRequitReason(String requitReason) {
		this.requitReason = requitReason;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	@Override
	public String toString() {
		return "Requit [id=" + id + ", userId=" + userId + ", orderId="
				+ orderId + ", name=" + name + ", mobile=" + mobile
				+ ", productName=" + productName + ", productMobile="
				+ productMobile + ", sum=" + sum + ", requitReason="
				+ requitReason + ", other=" + other + ", status=" + status
				+ ", reason=" + reason + ", addTime=" + addTime
				+ ", updateTime=" + updateTime + "]";
	}
    
}
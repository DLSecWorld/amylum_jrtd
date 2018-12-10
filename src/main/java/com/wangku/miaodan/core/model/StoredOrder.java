package com.wangku.miaodan.core.model;

import java.util.Date;

public class StoredOrder {
    private Long id;

    private String mobile;

    private Long orderId;
    
    private Integer isTd;

    private Date addTime;

    public Integer getIsTd() {
		return isTd;
	}

	public void setIsTd(Integer isTd) {
		this.isTd = isTd;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}
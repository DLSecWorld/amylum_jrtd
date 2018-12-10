package com.wangku.miaodan.core.model;

import java.math.BigDecimal;
import java.util.Date;

public class Recharge {

	private Long id;

	private String name;// 用户姓名

	private String number;

	private String weiXinNumber;

	private String mobile;

	private Integer product;

	private BigDecimal sum;

	private Date paySuccessTime;

	private Integer numProduct;

	private BigDecimal price;

	private int status;

	private Date addTime;

	private int times;

	private int tdTimes;

	public Recharge() {

	}

	public Recharge(String number, String mobile, int product, int numProduct,
			BigDecimal price, BigDecimal sum) {
		this.number = number;
		this.mobile = mobile;
		this.product = product;
		this.numProduct = numProduct;
		this.price = price;
		this.sum = sum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getWeiXinNumber() {
		return weiXinNumber;
	}

	public void setWeiXinNumber(String weiXinNumber) {
		this.weiXinNumber = weiXinNumber;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getProduct() {
		return product;
	}

	public void setProduct(Integer product) {
		this.product = product;
	}

	public BigDecimal getSum() {
		return sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}

	public Date getPaySuccessTime() {
		return paySuccessTime;
	}

	public void setPaySuccessTime(Date paySuccessTime) {
		this.paySuccessTime = paySuccessTime;
	}

	public Integer getNumProduct() {
		return numProduct;
	}

	public void setNumProduct(Integer numProduct) {
		this.numProduct = numProduct;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
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

}
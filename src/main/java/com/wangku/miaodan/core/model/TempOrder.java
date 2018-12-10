package com.wangku.miaodan.core.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class TempOrder {

	private String name;

	private String mobile;

	private String city;

	private Integer age;

	private Integer sum;

	private String vocation;

	private String workTime;

	private String sex;

	private String monthIncome;

	private String wagesType;

	private String creditCard;

	private String car;

	private String building;

	private String accumulationFund;

	private String socialInsurance;

	private String lifeInsurance;

	private String identyNumber;

	private String weiLiDai;

	private Date applyTime;

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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getSum() {
		return sum;
	}

	public void setSum(Integer sum) {
		this.sum = sum;
	}

	public String getVocation() {
		return vocation;
	}

	public void setVocation(String vocation) {
		this.vocation = vocation;
	}

	public String getWorkTime() {
		return workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMonthIncome() {
		return monthIncome;
	}

	public void setMonthIncome(String monthIncome) {
		this.monthIncome = monthIncome;
	}

	public String getWagesType() {
		return wagesType;
	}

	public void setWagesType(String wagesType) {
		this.wagesType = wagesType;
	}

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getAccumulationFund() {
		return accumulationFund;
	}

	public void setAccumulationFund(String accumulationFund) {
		this.accumulationFund = accumulationFund;
	}

	public String getSocialInsurance() {
		return socialInsurance;
	}

	public void setSocialInsurance(String socialInsurance) {
		this.socialInsurance = socialInsurance;
	}

	public String getLifeInsurance() {
		return lifeInsurance;
	}

	public void setLifeInsurance(String lifeInsurance) {
		this.lifeInsurance = lifeInsurance;
	}

	public String getIdentyNumber() {
		return identyNumber;
	}

	public void setIdentyNumber(String identyNumber) {
		this.identyNumber = identyNumber;
	}

	public String getWeiLiDai() {
		return weiLiDai;
	}

	public void setWeiLiDai(String weiLiDai) {
		this.weiLiDai = weiLiDai;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
	
	public Order translateOrder() {
		Order order = new Order();
		order.setName(name);
		order.setMobile(mobile);
		order.setCity(city);
		order.setAge(age);
		order.setIdentyNumber(identyNumber);
		order.setSum(new BigDecimal(sum));
		order.setVocation("企业主".equals(vocation)? (byte)2:"个体户".equals(vocation)?(byte)1:(byte)0);
		order.setWorkTime("一年以上".equals(workTime)?(byte)2:"一年以内".equals(workTime)?(byte)1:(byte)0);
		order.setSex("女".equals(sex)? (byte)0:(byte)1);
		order.setMonthIncome("1万以上".equals(monthIncome)? (byte)4:"8001到1万".equals(monthIncome)?(byte)3:"5001到8000".equals(monthIncome)?(byte)2:"3000到5000".equals(monthIncome)?(byte)1:(byte)0);
		order.setWagesType("银行代发".equals(wagesType)?(byte)0:(byte)1);
		order.setCreditCard("有".equals(creditCard)?(byte)1:(byte)0);
		order.setCar("有".equals(car)?(byte)1:(byte)0);
		order.setBuilding("有".equals(building)?(byte)1:(byte)0);
		order.setAccumulationFund("有".equals(accumulationFund)?(byte)1:(byte)0);
		order.setSocialInsurance("有".equals(socialInsurance)?(byte)1:(byte)0);
		order.setLifeInsurance("有".equals(lifeInsurance)?(byte)1:(byte)0);
		order.setWeiLiDai("有".equals(weiLiDai)?(byte)1:(byte)0);
		order.setApplyTime(applyTime);
		return order;
	}

}

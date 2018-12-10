package com.wangku.miaodan.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SearchBean {

	// 搜索类型(null 所有类型 1所有订单， 2 优质订单 3 申请时间超过一天的)
	private Integer type;

	// 订单状态(null 所有状态， 0 可抢订单 1 已抢订单)
	private Integer status;

	// 是否启用用户ID
	private boolean userId = false;

	// 排序字段
	private String sortBy = "applyTime";

	// 排序方式
	private String sort = "desc";

	private String city;

	private Byte vocation;
	// 接收后需要转换成数组
	private String car;
	
	private List<String> carList;

	private String building;
	
	private List<String> buildingList;

	private String lifeInsurance;
	
	private List<String> lifeInsuranceList;

	private Byte weiLiDai;

	private Byte creditCard;

	private String accumulationFund;
	
	private List<String> accumulationFundList;

	private String socialInsurance;
	
	private List<String> socialInsuranceList;

	private Byte wagesType;

	private Byte workTime;

	private Byte monthIncome;

	// 最小额度
	private Double sumStart;

	// 最大额度
	private Double sumEnd;
	
	// 当前日期
	private String now = LocalDateTime.now().plusDays(-1)
			.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	
	private String nowDate = LocalDate.now().toString() + " 00:00:00";

	// 当前页码
	private Integer page;

	// 页面数据尺寸
	private Integer size;
	
	public String getNowDate() {
		return nowDate;
	}

	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public boolean isUserId() {
		return userId;
	}

	public void setUserId(boolean userId) {
		this.userId = userId;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Byte getVocation() {
		return vocation;
	}

	public void setVocation(Byte vocation) {
		this.vocation = vocation;
	}

	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
		this.carList = car == null? null:Arrays.asList(car.split(","));
	}

	public List<String> getCarList() {
		return carList;
	}

	public String getBuilding() {
		return building;
	}
	

	public void setBuilding(String building) {
		this.building = building;
		this.buildingList = building == null? null:Arrays.asList(building.split(","));
	}
	
	public List<String> getBuildingList() {
		return this.buildingList;
	}	

	public String getLifeInsurance() {
		return lifeInsurance;
	}

	public void setLifeInsurance(String lifeInsurance) {
		this.lifeInsurance = lifeInsurance;
		this.lifeInsuranceList = lifeInsurance == null? null:Arrays.asList(lifeInsurance.split(","));
	}

	public List<String> getLifeInsuranceList() {
		return lifeInsuranceList;
	}

	public Byte getWeiLiDai() {
		return weiLiDai;
	}

	public void setWeiLiDai(Byte weiLiDai) {
		this.weiLiDai = weiLiDai;
	}

	public Byte getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(Byte creditCard) {
		this.creditCard = creditCard;
	}

	public String getAccumulationFund() {
		return accumulationFund;
	}

	public void setAccumulationFund(String accumulationFund) {
		this.accumulationFund = accumulationFund;
		this.accumulationFundList = accumulationFund == null? null: Arrays.asList(accumulationFund.split(","));
	}

	public List<String> getAccumulationFundList() {
		return accumulationFundList;
	}

	public String getSocialInsurance() {
		return socialInsurance;
	}

	public void setSocialInsurance(String socialInsurance) {
		this.socialInsurance = socialInsurance;
		this.socialInsuranceList = socialInsurance == null? null: Arrays.asList(socialInsurance.split(","));
	}

	public List<String> getSocialInsuranceList() {
		return socialInsuranceList;
	}

	public Byte getWagesType() {
		return wagesType;
	}

	public void setWagesType(Byte wagesType) {
		this.wagesType = wagesType;
	}

	public Byte getWorkTime() {
		return workTime;
	}

	public void setWorkTime(Byte workTime) {
		this.workTime = workTime;
	}

	public Byte getMonthIncome() {
		return monthIncome;
	}

	public void setMonthIncome(Byte monthIncome) {
		this.monthIncome = monthIncome;
	}

	public Double getSumStart() {
		return sumStart;
	}

	public void setSumStart(Double sumStart) {
		this.sumStart = sumStart;
	}

	public Double getSumEnd() {
		return sumEnd;
	}

	public void setSumEnd(Double sumEnd) {
		this.sumEnd = sumEnd;
	}

	public String getNow() {
		return now;
	}

	public void setNow(String now) {
		this.now = now;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "SearchBean [type=" + type + ", status=" + status + ", userId="
				+ userId + ", sortBy=" + sortBy + ", sort=" + sort + ", city="
				+ city + ", vocation=" + vocation + ", car=" + car
				+ ", carList=" + carList + ", building=" + building
				+ ", buildingList=" + buildingList + ", lifeInsurance="
				+ lifeInsurance + ", lifeInsuranceList=" + lifeInsuranceList
				+ ", weiLiDai=" + weiLiDai + ", creditCard=" + creditCard
				+ ", accumulationFund=" + accumulationFund
				+ ", accumulationFundList=" + accumulationFundList
				+ ", socialInsurance=" + socialInsurance
				+ ", socialInsuranceList=" + socialInsuranceList
				+ ", wagesType=" + wagesType + ", workTime=" + workTime
				+ ", monthIncome=" + monthIncome + ", sumStart=" + sumStart
				+ ", sumEnd=" + sumEnd + ", now=" + now + ", page=" + page
				+ ", size=" + size + "]";
	}

}

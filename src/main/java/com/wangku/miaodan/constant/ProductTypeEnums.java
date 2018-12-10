package com.wangku.miaodan.constant;

import java.math.BigDecimal;


public enum ProductTypeEnums {
	
	PRODUCT_ONE("0精品/30淘单", 1, 0, 30, new BigDecimal(0.01)),
	PRODUCT_TWO("15精品/0淘单", 2, 15, 0, new BigDecimal(0.01)),
	PRODUCT_THREE("30精品/15淘单", 3, 30, 15, new BigDecimal(0.01)),
	PRODUCT_FOUR("66精品/30淘单", 4, 66, 30, new BigDecimal(0.01)),
	PRODUCT_FIVE("102精品/30淘单", 5, 102, 30, new BigDecimal(0.01)),
	PRODUCT_SIX("153精品/30淘单", 6, 153, 30, new BigDecimal(0.01));
	
	private String desc;
	
	private int type;
	
	private int jpTimes;
	
	private int tdTimes;
	
	private BigDecimal price;
	
	private ProductTypeEnums(String desc, int type, int jpTimes, int tdTimes, BigDecimal price) {
		this.desc = desc;
		this.type = type;
		this.jpTimes = jpTimes;
		this.tdTimes = tdTimes;
		this.price = price;
	}
	
	public static ProductTypeEnums getByType(long type) {
		if (type == 1) {
			return PRODUCT_ONE;
		} else if (type == 2) {
			return PRODUCT_TWO;
		} else if (type == 3){
			return PRODUCT_THREE;
		} else if (type == 4){
			return PRODUCT_FOUR;
		} else if (type == 5){
			return PRODUCT_FIVE;
		} else {
			return PRODUCT_SIX;
		} 
			
	}

	public String getDesc() {
		return desc;
	}

	public int getType() {
		return type;
	}

	public int getJpTimes() {
		return jpTimes;
	}

	public int getTdTimes() {
		return tdTimes;
	}

	public BigDecimal getPrice() {
		return price;
	}
	
}

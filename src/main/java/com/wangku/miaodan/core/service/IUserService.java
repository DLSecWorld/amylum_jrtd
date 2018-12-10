package com.wangku.miaodan.core.service;

import java.util.List;
import java.util.Map;

import com.wangku.miaodan.core.model.Recharge;
import com.wangku.miaodan.core.model.User;

public interface IUserService {

	public boolean checkCanStore(String mobile, boolean isTD);

	public long storeOrder(Long orderId, String mobile, boolean isTD);

	public void addUser(String mobile);	
	
	public void incrTimesByMobile(String mobile, int times);

	public void update(User user);
	
	public User getDetailByMobile(String mobile);

	public Long addRecharge(Recharge recharge);

	public void recharge(Map<String, String> restmap);

	public void addOpenId(String mobile, String openId);

	public boolean isAuth(String mobile);

	public List<User> list(String city, String status, String addTime, int start, int size);

	public void checkAuth(Long userId, int status);

	public long count(String city, String status, String addTime);

	public User getDetailById(Long id);

}

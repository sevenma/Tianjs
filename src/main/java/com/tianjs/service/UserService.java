package com.tianjs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tianjs.mapper.TRoleMapper;
import com.tianjs.mapper.TUserMapper;
import com.tianjs.model.Permission;
import com.tianjs.model.Role;
import com.tianjs.model.User;

@Service
public class UserService {

	@Autowired
	private TUserMapper userMapper;

	@Autowired
	private TRoleMapper  tRoleMapper;
	
	public void changePassword(Long userId, String newPassword) {
		// sysUserMapper.changePassword(userId, newPassword);
	}

	public User findByUsername(String username) {
		return userMapper.findByUsername(username);
	}
	
	public User findById(String id) {
		return userMapper.findById(id);
	}
	public List<Role> findRoleByUserId(String id) {
		return userMapper.findRoleByUserId(id);
	}

	@Transactional(rollbackFor = {Exception.class}) // 可多个异常 进行事务管理
	public String save(User user) {
		if (user == null || user.getUsername().equals("") || user.getPassword().equals("")) {
			return "用户信息不能为空。";
		} else if (userMapper.findByUsername(user.getUsername()) == null) {
			userMapper.insert(user);
			return "注册成功";
		} else {
			return "该用户名已被使用";
		}
	}
	
	public List<Permission> findPermissionByRoleId(String id){
		return tRoleMapper.findPermissionByRoleId(id);
	}
}

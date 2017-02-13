package com.tianjs.model;

import java.beans.Transient;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.StringUtils;
public class User {

	private Integer id;
	@NotEmpty(message = "用户名不能为空")
	private String username;
	@NotEmpty(message = "密码不能为空")
	private String password;
	private List<Role> roleList;// 一个用户具有多个角色
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Transient
	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public User() {
		super();
	}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Transient
	public Set<String> getRolesName() {
		List<Role> roles = getRoleList();
		Set<String> set = new HashSet<String>();
		for (Role role : roles) {
			set.add(role.getRolename());
		}
		return set;
	}
	
	@Transient
	 public boolean isEmpty() {
	        return StringUtils.isEmpty(this.username)||StringUtils.isEmpty(this.password);
	    }
}

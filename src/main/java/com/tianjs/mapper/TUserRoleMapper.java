package com.tianjs.mapper;

import java.util.List;

import com.tianjs.model.UserRole;

public interface TUserRoleMapper {
    public UserRole findById(String id);
    public void insert(UserRole userRole);
    public List<UserRole> findPageBy(UserRole userRole);
}

package com.tianjs.mapper;

import java.util.List;

import com.tianjs.model.Permission;

public interface TPermissionMapper {
    public Permission findById(String id);
    public void insert(Permission permission);
    public List<Permission> findPageBy(Permission permission);
}

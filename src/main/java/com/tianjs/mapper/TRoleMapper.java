package com.tianjs.mapper;

import java.util.List;

import com.tianjs.model.Permission;
import com.tianjs.model.Role;

public interface TRoleMapper {
    public Role findById(String id);
    public void insert(Role role);
    public List<Role> findPageBy(Role role);
    public List<Permission> findPermissionByRoleId(String id);
}

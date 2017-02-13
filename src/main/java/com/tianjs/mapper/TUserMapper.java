package com.tianjs.mapper;

import java.util.List;

import com.tianjs.model.Role;
import com.tianjs.model.User;

public interface TUserMapper {
    public User findById(String id);
    public List<Role> findRoleByUserId(String id);
    public User findByUsername(String userName);
    public void insert(User user);
    public List<User> findPageBy(User user);
}

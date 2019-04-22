package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.UserMapper;
import com.qf.entity.User;
import com.qf.service.IUserService;
import com.qf.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserserviceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public int insertUser(User user) {
        user.setPassword(MD5Util.md5(user.getPassword()));
        return userMapper.insert(user);
    }

    @Override
    public User loginUser(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("password", MD5Util.md5(password));
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public int jihuoUser(String username) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        user.setStatus(1);
        //修改用户信息
        int result = userMapper.updateById(user);
        return result;
    }
}

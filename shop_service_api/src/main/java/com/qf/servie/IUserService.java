package com.qf.servie;

import com.qf.entity.User;

public interface IUserService {
    int insertUser(User user);

    User loginUser(String username, String password);


    int jihuoUser(String username);

//    User getById(int id);
//
//    int delete(int id);
//
//    int update(User user);

}

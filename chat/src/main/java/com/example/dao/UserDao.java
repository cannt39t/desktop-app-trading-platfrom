package com.example.dao;

import com.example.models.User;

import java.sql.SQLException;

public interface UserDao {

    User get(int id) throws SQLException;

    void add(User user);


    void updateBalance(int id, int balance);

}

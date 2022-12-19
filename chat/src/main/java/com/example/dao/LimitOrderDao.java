package com.example.dao;

import com.example.models.LimitOrder;

import java.sql.SQLException;

public interface LimitOrderDao {

    LimitOrder get(int id) throws SQLException;

    void add(LimitOrder limitOrder);

}

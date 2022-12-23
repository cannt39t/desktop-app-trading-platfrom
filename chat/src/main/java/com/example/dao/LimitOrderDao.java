package com.example.dao;

import com.example.models.LimitOrder;

import java.sql.SQLException;
import java.util.List;

public interface LimitOrderDao {

    LimitOrder get(int id) throws SQLException;

    void add(LimitOrder limitOrder);

    List<LimitOrder> getAllSell() throws SQLException;

    List<LimitOrder> getAllBuy() throws SQLException;

    void delete(int id);

    void updateVolume(int id, int volume);
}

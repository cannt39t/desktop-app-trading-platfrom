package com.example.dao.impl;

import com.example.dao.LimitOrderDao;
import com.example.models.LimitOrder;
import com.example.models.Position;
import com.example.util.PostgresConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LimitOrderDaoImpl implements LimitOrderDao {

    private final Connection connection = PostgresConnectionUtil.getConnection();

    @Override
    public LimitOrder get(int id) throws SQLException {
        return null;
    }

    @Override
    public void add(LimitOrder limitOrder) {
        String sql = "INSERT INTO limit_order (side, volume, at_price, user_id, quotation_id) VALUES (?::side, ?, ?, ?, ?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, limitOrder.getSide());
            preparedStatement.setInt(2,  limitOrder.getVolume());
            preparedStatement.setInt(3, limitOrder.getAtPrice());
            preparedStatement.setInt(4, limitOrder.getUserId());
            preparedStatement.setInt(5, limitOrder.getQuotationId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LimitOrderDao limitOrderDao = new LimitOrderDaoImpl();
        limitOrderDao.add(new LimitOrder(1,"Buy", 10, 3000, 3, 1));
    }
}

package com.example.dao.impl;

import com.example.dao.LimitOrderDao;
import com.example.models.LimitOrder;
import com.example.models.Position;
import com.example.util.PostgresConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LimitOrderDaoImpl implements LimitOrderDao {

    private final Connection connection = PostgresConnectionUtil.getConnection();

    @Override
    public LimitOrder get(int id) throws SQLException {
        return null;
    }

    public List<LimitOrder> getAllSell() throws SQLException {
        Statement statement = connection.createStatement();
        String sql = """
                select * from limit_order
                where side = 'Sell'
                order by at_price;
        """;
        ResultSet resultSet = statement.executeQuery(sql);

        List<LimitOrder> allLimitSells = new ArrayList<>();

        while (resultSet.next()) {
            LimitOrder user = new LimitOrder(
                    resultSet.getInt("id"),
                    resultSet.getString("side"),
                    resultSet.getInt("volume"),
                    resultSet.getInt("at_price"),
                    resultSet.getInt("user_id"),
                    resultSet.getInt("quotation_id")
            );
            allLimitSells.add(user);
        }

        return allLimitSells;

    }

    public List<LimitOrder> getAllBuy() throws SQLException {
        Statement statement = connection.createStatement();
        String sql = """
                select * from limit_order
                where side = 'Buy'
                order by at_price desc;
        """;
        ResultSet resultSet = statement.executeQuery(sql);

        List<LimitOrder> allLimitSells = new ArrayList<>();

        while (resultSet.next()) {
            LimitOrder user = new LimitOrder(
                    resultSet.getInt("id"),
                    resultSet.getString("side"),
                    resultSet.getInt("volume"),
                    resultSet.getInt("at_price"),
                    resultSet.getInt("user_id"),
                    resultSet.getInt("quotation_id")
            );
            allLimitSells.add(user);
        }

        return allLimitSells;

    }

    @Override
    public void delete(int id) {
        String sql = """
                    delete from limit_order
                    where id = ?;
                """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateVolume(int id, int volume) {
        String sql = """
                update limit_order
                set volume = ?
                where id = ?
                """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, volume);
            preparedStatement.setInt(2,  id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

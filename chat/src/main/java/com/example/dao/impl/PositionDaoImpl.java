package com.example.dao.impl;

import com.example.dao.PositionDao;
import com.example.models.GeneralPosition;
import com.example.models.Position;
import com.example.util.PostgresConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PositionDaoImpl implements PositionDao {

    private final Connection connection = PostgresConnectionUtil.getConnection();

    @Override
    public Position get(int id) throws SQLException {
        String sql = "SELECT * FROM position WHERE user_id = ? AND close_or_not = false;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        Position position_from_db = null;

        try {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                position_from_db = new Position(
                        resultSet.getInt("id"),
                        resultSet.getString("side"),
                        resultSet.getInt("volume"),
                        resultSet.getInt("at_price"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("quotation_id"),
                        resultSet.getBoolean("close_or_not")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return position_from_db;
    }

    public GeneralPosition getCurrentPosition(int userID) throws SQLException {
        String sql = """
                with pnl as (
                                 select price as price
                                 from quotation
                                 where id = 1
                             ),
                             
                                  at_price as (
                                      select at_price as at_price
                                      from position
                                      where user_id = ? and close_or_not = false
                                  ),
                             
                                  volume as (
                                     select volume as volume
                                     from position
                                     where user_id = ? and close_or_not = false
                                 )
                             
                             select user_id as user_id, side as side, avg(position.volume) as avg_volume, avg(position.at_price) as avg_price, avg((round(((pnl.price::float - at_price.at_price::float) / pnl.price::float)::numeric, 2) + 1) * position.at_price * position.volume - position.at_price * position.volume) as pnl
                             from pnl, at_price, volume, position
                             where user_id = ? and close_or_not = false
                             group by side, user_id
            """;

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        GeneralPosition general_position_from_db = null;

        try {
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, userID);
            preparedStatement.setInt(3, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                general_position_from_db = new GeneralPosition(
                        resultSet.getInt("user_id"),
                        resultSet.getString("side"),
                        resultSet.getInt("avg_volume"),
                        resultSet.getInt("avg_price"),
                        resultSet.getInt("pnl")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return general_position_from_db;
    }

    @Override
    public void add(Position position) throws SQLException {
        if (getCurrentPosition(position.getUserId()) != null && getCurrentPosition(position.getUserId()).getSide().equals(position.getSide())){
            String sql = "INSERT INTO position (side, volume, at_price, user_id, quotation_id, close_or_not) VALUES (?::side, ?, ?, ?, ?, ?);";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, position.getSide());
                preparedStatement.setInt(2,  position.getVolume());
                preparedStatement.setInt(3, position.getAtPrice());
                preparedStatement.setInt(4, position.getUserId());
                preparedStatement.setInt(5, position.getQuotationId());
                preparedStatement.setBoolean(6, position.isCloseOrNot());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public GeneralPosition hasOpenPosition(int id) throws SQLException {
        return getCurrentPosition(id);
    }

    public static void main(String[] args) throws SQLException {
        PositionDao positionDao = new PositionDaoImpl();
        System.out.println(positionDao.getCurrentPosition(2));
        positionDao.add(new Position(8,"Buy", 12, 3000, 2, 1, false));
        System.out.println(positionDao.getCurrentPosition(2));
    }
}

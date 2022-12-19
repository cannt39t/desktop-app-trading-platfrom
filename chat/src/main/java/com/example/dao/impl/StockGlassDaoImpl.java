package com.example.dao.impl;

import com.example.dao.StockGlassDao;
import com.example.models.StockGlass;
import com.example.models.User;
import com.example.util.PostgresConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public class StockGlassDaoImpl implements StockGlassDao {

    private final Connection connection = PostgresConnectionUtil.getConnection();

    @Override
    public StockGlass get() throws SQLException {
        LinkedHashMap<String, String> glass = new LinkedHashMap<>();
        String sql = """
                WITH t AS (
                    select at_price, sum(volume)
                    from limit_order
                    where side = 'Sell'
                    group by side, at_price
                    order by at_price
                    limit 4
                )
                SELECT * FROM t ORDER BY at_price DESC;
                """;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                glass.put(
                    resultSet.getString("at_price"),
                    resultSet.getString("sum")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = """
            with buy_price as (
                select min(at_price) as buy_price
                from limit_order
                where side = 'Sell'
            ),
            
            sell_price as (
             select max(at_price) as sell_price
             from limit_order
             where side = 'Buy'
            ),
            
            price as (
             select price as price
             from quotation
             where id = 1
            )
            
            select( buy_price - sell_price)::text as dif,
                   round((((buy_price::float - sell_price::float) / price * 100)::float)::numeric, 2)::text  as proc
            from buy_price, sell_price, price;
            """;
        preparedStatement = connection.prepareStatement(sql);
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                glass.put(
                        resultSet.getString("dif"),
                        resultSet.getString("proc").concat("%")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = """
            select at_price, sum(volume)
            from limit_order
            where side = 'Buy'
            group by side, at_price
            order by at_price desc
            limit 4;
            """;
        preparedStatement = connection.prepareStatement(sql);
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                glass.put(
                        resultSet.getString("at_price"),
                        resultSet.getString("sum")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new StockGlass(glass);
    }

    public static void main(String[] args) {
        StockGlassDao stockGlassDao = new StockGlassDaoImpl();
        try {
            stockGlassDao.get().getPriceToVolume().entrySet().forEach(System.out::println);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

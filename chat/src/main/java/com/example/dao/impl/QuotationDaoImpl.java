package com.example.dao.impl;

import com.example.dao.QuotationDao;
import com.example.models.Quotation;
import com.example.models.User;
import com.example.util.PostgresConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuotationDaoImpl implements QuotationDao {

    private final Connection connection = PostgresConnectionUtil.getConnection();

    @Override
    public Quotation get(int id) throws SQLException {
        String sql = "SELECT * FROM quotation WHERE id = ? LIMIT 1;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        Quotation quotation_from_db = null;

        try {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                quotation_from_db = new Quotation(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("price")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quotation_from_db;
    }

    public static void main(String[] args) throws SQLException {
        QuotationDao quotationDao = new QuotationDaoImpl();
        System.out.println(quotationDao.get(1));
    }
}

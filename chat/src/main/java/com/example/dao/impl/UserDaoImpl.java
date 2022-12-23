package com.example.dao.impl;

import com.example.dao.UserDao;
import com.example.models.User;
import com.example.util.PostgresConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {

    private final Connection connection = PostgresConnectionUtil.getConnection();

    @Override
    public User get(int id) throws SQLException {
        // SELECT * FROM users WHERE id = 1 LIMIT 1
        String sql = "SELECT * FROM users WHERE id = ? LIMIT 1;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        User user_from_db = null;

        try {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                user_from_db = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("balance")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user_from_db;
    }

    @Override
    public void add(User user) {
        // INSERT INTO users (name);
        String sql = "INSERT into users (name) VALUES (?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBalance(int id, int balance) {
        String sql = """
                update users
                set balance = ?
                where id = ?
                """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, balance);
            preparedStatement.setInt(2,  id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UserDaoImpl userDao = new UserDaoImpl();
        // userDao.add(new User(2,"whale2", 100));
        try {
            User user = userDao.get(1);
            System.out.println(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package com.example.dao;

import com.example.models.StockGlass;

import java.sql.SQLException;

public interface StockGlassDao {

    StockGlass get() throws SQLException;

}

package com.example.dao;

import com.example.models.GeneralPosition;
import com.example.models.Position;

import java.sql.SQLException;

public interface PositionDao {

    Position get(int id) throws SQLException;

    void add(Position position) throws SQLException;

    GeneralPosition hasOpenPosition(int id) throws SQLException;

    GeneralPosition getCurrentPosition(int userID) throws SQLException;

}

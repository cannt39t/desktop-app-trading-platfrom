package com.example.dao;

import com.example.models.Quotation;

import java.sql.SQLException;

public interface QuotationDao {

    Quotation get(int id) throws SQLException;

    void changePrice(int id, int price);

}

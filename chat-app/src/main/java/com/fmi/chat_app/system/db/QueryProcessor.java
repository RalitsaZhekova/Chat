package com.fmi.chat_app.system.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QueryProcessor<T> {

    private final JdbcTemplate jdbcTemplate;

    private StringBuilder sqlQuery;
    private ArrayList<String> colCollection;
    private ArrayList<String> placeholderCollection;
    private ArrayList<Object> valuesCollection;

    public QueryProcessor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public StringBuilder getSqlQuery() {
        return sqlQuery;
    }

    public ArrayList<String> getColCollection() {
        return colCollection;
    }

    public ArrayList<String> getPlaceholderCollection() {
        return placeholderCollection;
    }

    public ArrayList<Object> getValuesCollection() {
        return valuesCollection;
    }

    public int processQuery() {
        return this.jdbcTemplate.update(this.sqlQuery.toString(), this.valuesCollection.toArray());
    }

    public List<T> processSelect(RowMapper<T> rowMapper) {
        return this.jdbcTemplate.query(this.sqlQuery.toString(), this.valuesCollection.toArray(), rowMapper);
    }

    public void initNewQuery() {
        this.sqlQuery = new StringBuilder();
        this.colCollection = new ArrayList<>();
        this.placeholderCollection = new ArrayList<>();
        this.valuesCollection = new ArrayList<>();
    }

    public void setQueryColValue(String colName, Object value) {
        this.colCollection.add(colName);
        this.placeholderCollection.add("?");
        this.valuesCollection.add(value);
    }

    public void buildColValue(String colName, String operator) {
        this.sqlQuery.append(colName).append(operator).append("?");
    }

    public void buildColValue(String colName) {
        this.buildColValue(colName, "=");
    }

}

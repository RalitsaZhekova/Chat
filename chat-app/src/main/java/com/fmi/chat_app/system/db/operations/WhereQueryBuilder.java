package com.fmi.chat_app.system.db.operations;

import com.fmi.chat_app.system.db.QueryProcessor;

public class WhereQueryBuilder<T> {

    private QueryProcessor queryProcessor;

    public WhereQueryBuilder(QueryProcessor queryProcessor) {
        this.queryProcessor = queryProcessor;
    }

    public T where(String colName, String operator, Object value) {
        this.queryProcessor.getSqlQuery().append(" WHERE ");
        this.queryProcessor.buildColValue(colName, operator);
        this.queryProcessor.setQueryColValue(colName, value);
        return (T) this;
    }

    public T where(String colName, Object value) {
        return this.where(colName, "=", value);
    }

    public T andWhere(String colName, String operator, Object value) {
        this.queryProcessor.getSqlQuery().append(" AND ( ");
        this.queryProcessor.buildColValue(colName, operator);
        this.queryProcessor.getSqlQuery().append(" ) ");
        this.queryProcessor.setQueryColValue(colName, value);
        return (T) this;
    }

    public T andWhere(String colName, Object value) {
        return this.andWhere(colName, "=", value);
    }

    public T orWhere(String colName, String operator, Object value) {
        this.queryProcessor.getSqlQuery().append(" OR ( ");
        this.queryProcessor.buildColValue(colName, operator);
        this.queryProcessor.getSqlQuery().append(" ) ");
        this.queryProcessor.setQueryColValue(colName, value);
        return (T) this;
    }

    public T orWhere(String colName, Object value) {
        return this.orWhere(colName, "=", value);
    }

}

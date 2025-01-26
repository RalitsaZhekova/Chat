package com.fmi.chat_app.system.db.operations;

import com.fmi.chat_app.system.db.QueryProcessor;

public class UpdateQueryBuilder extends WhereQueryBuilder<UpdateQueryBuilder> {

    private QueryProcessor queryProcessor;
    private String tableName;

    public UpdateQueryBuilder(QueryProcessor queryProcessor, String tableName) {
        super(queryProcessor);
        this.queryProcessor = queryProcessor;
        this.tableName = tableName;

        this.queryProcessor.initNewQuery();
        this.queryProcessor.getSqlQuery().append("UPDATE ").append(tableName).append(" SET ");
    }

    public UpdateQueryBuilder set(String colName, Object value) {

        if (!this.queryProcessor.getColCollection().isEmpty()) {
            this.queryProcessor.getSqlQuery().append(", ");
        }

        this.queryProcessor.buildColValue(colName);
        this.queryProcessor.setQueryColValue(colName, value);
        return this;

    }

    public int update() {
        return this.queryProcessor.processQuery();
    }
}

package com.fmi.chat_app.system.db.operations;

import com.fmi.chat_app.system.db.QueryProcessor;

public class InsertQueryBuilder {

    private QueryProcessor queryProcessor;
    private String tableName;

    public InsertQueryBuilder(QueryProcessor queryProcessor, String tableName) {
        this.queryProcessor = queryProcessor;
        this.tableName = tableName;

        this.queryProcessor.initNewQuery();
        this.queryProcessor.getSqlQuery().append("INSERT INTO ")
                .append(tableName);
    }

    public InsertQueryBuilder withValue(String colName, Object value) {

        this.queryProcessor.setQueryColValue(colName, value);

        return this;

    }

    public boolean insert() {

        String colDefinition = String.join(",", this.queryProcessor.getColCollection());
        String valDefinition = String.join(",", this.queryProcessor.getPlaceholderCollection());

        this.queryProcessor.getSqlQuery().append("(").
                append(colDefinition)
                .append(") VALUES (")
                .append(valDefinition)
                .append(")");

        int resultCount = this.queryProcessor.processQuery();
        return resultCount > 0;

    }
}

package com.fmi.chat_app.system.db.operations;

import com.fmi.chat_app.system.db.QueryProcessor;

public class DeleteQueryBuilder extends WhereQueryBuilder<DeleteQueryBuilder> {

    private QueryProcessor queryProcessor;
    private String tableName;

    public DeleteQueryBuilder(QueryProcessor queryProcessor, String tableName) {
        super(queryProcessor);
        this.queryProcessor = queryProcessor;
        this.queryProcessor.initNewQuery();
        this.queryProcessor.getSqlQuery().append("DELETE FROM ").append(tableName);
    }

    public int delete() {
        return this.queryProcessor.processQuery();
    }
}

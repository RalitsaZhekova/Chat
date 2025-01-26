package com.fmi.chat_app.system.db;

import com.fmi.chat_app.system.db.operations.DeleteQueryBuilder;
import com.fmi.chat_app.system.db.operations.InsertQueryBuilder;
import com.fmi.chat_app.system.db.operations.SelectQueryBuilder;
import com.fmi.chat_app.system.db.operations.UpdateQueryBuilder;
import org.springframework.stereotype.Service;

@Service
public class QueryBuilder<T> {

    private final QueryProcessor<T> queryProcessor;

    public QueryBuilder(QueryProcessor<T> queryProcessor) {
        this.queryProcessor = queryProcessor;
    }

    //INSERT
    public InsertQueryBuilder insertInto(String table) {
        return new InsertQueryBuilder(this.queryProcessor, table);
    }

    //UPDATE
    public UpdateQueryBuilder updateTable(String table) {
        return new UpdateQueryBuilder(this.queryProcessor, table);
    }

    //DELETE
    public DeleteQueryBuilder deleteTable(String table) {
        return new DeleteQueryBuilder(this.queryProcessor, table);
    }

    //Select
    public SelectQueryBuilder<T> select(String ...columns) {
        return new SelectQueryBuilder<T>(this.queryProcessor, columns);
    }

    public SelectQueryBuilder<T> selectAll() {
        return new SelectQueryBuilder<T>(this.queryProcessor, "*");
    }

}

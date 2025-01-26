package com.fmi.chat_app.system.db.operations;

import com.fmi.chat_app.system.db.QueryProcessor;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class SelectQueryBuilder<T>  extends WhereQueryBuilder<SelectQueryBuilder<T>> {

    private final QueryProcessor<T> queryProcessor;

    public SelectQueryBuilder(QueryProcessor<T> queryProcessor, String ...columns) {
        super(queryProcessor);
        this.queryProcessor = queryProcessor;

        this.queryProcessor.initNewQuery();
        this.queryProcessor.getSqlQuery()
                .append("SELECT ")
                .append(String.join(", ", columns));
    }

    public SelectQueryBuilder<T> from(String table) {
        this.queryProcessor.getSqlQuery()
                .append(" FROM ")
                .append(table);
        return this;
    }

    public List<T> fetchAll(RowMapper<T> rowMapper) {
        return this.queryProcessor.processSelect(rowMapper);
    }

    public T fetch(RowMapper<T> rowMapper) {

        try {
            return this.fetchAll(rowMapper).get(0);
        }
        catch (Exception e) {
            return null;
        }
    }

    public SelectQueryBuilder<T> join(String table, String condition) {

        this.queryProcessor.getSqlQuery()
                .append(" JOIN ")
                .append(table)
                .append(" ON ")
                .append(condition);
        return this;

    }

    public SelectQueryBuilder<T> join(String joinType, String table, String condition) {

        this.queryProcessor.getSqlQuery()
                .append(" ")
                .append(joinType)
                .append(" JOIN ")
                .append(table)
                .append(" ON ")
                .append(condition);
        return this;

    }

    public SelectQueryBuilder<T> limit(int limit) {

        this.queryProcessor.getSqlQuery()
                .append(" LIMIT ")
                .append(limit);
        return this;

    }

    public SelectQueryBuilder<T> offset(int offset) {

        this.queryProcessor.getSqlQuery()
                .append(" OFFSET ")
                .append(offset);
        return this;

    }
}

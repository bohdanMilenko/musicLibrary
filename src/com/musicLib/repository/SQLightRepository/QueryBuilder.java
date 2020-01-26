package com.musicLib.repository.SQLightRepository;

import static com.musicLib.repository.SQLightRepository.MetaData.ORDER_DESC;
import static com.musicLib.repository.SQLightRepository.MetaData.ORDER_NONE;

public class QueryBuilder {

    private static final String selectStart = "SELECT ";
    private static final String addFrom = " FROM ";
    private static final String innerJoin = " INNER JOIN ";
    private static final String joinOn = " ON ";
    private static final String whereClause = " WHERE ";
    private static final String andCondition = " AND ";
    private static final String orCondition = " OR ";



    private static final String deleteFrom = "DELETE FROM ";
    private static final String insertInto = "INSERT INTO ";

    private StringBuilder query = new StringBuilder();

    public QueryBuilder() {

    }

    public QueryBuilder startQuery(String tableName, String columnName) {
        query.append(selectStart).append(tableName).append(".").append(columnName);
        return this;
    }

    public QueryBuilder addSelection(String tableName, String columnName) {
        query.append(", ").append(tableName).append(".").append(columnName);
        return this;
    }

    public QueryBuilder queryFrom(String tableName) {
        query.append(addFrom).append(tableName);
        return this;
    }

    public QueryBuilder innerJoinOn(String initialTable, String initialColumnName, String secondTable, String secondColumnName){
        query.append(innerJoin).append(initialTable).append(joinOn).append(initialTable).append(".").append(initialColumnName)
                .append(" = ").append(secondTable).append(".").append(secondColumnName);
        return this;
    }

    public QueryBuilder specifyFirstCondition(String criteriaTableName, String criteriaColumnName){
        query.append(whereClause).append(criteriaTableName).append(".").append(criteriaColumnName).
                append(" = ?");
        return this;
    }

    public QueryBuilder addANDCondition(String criteriaTableName, String criteriaColumnName){
        query.append(andCondition).append(criteriaTableName).append(".").append(criteriaColumnName).
                append(" = ?");
        return this;
    }

    public QueryBuilder addORCondition(String criteriaTableName, String criteriaColumnName){
        query.append(orCondition).append(criteriaTableName).append(".").append(criteriaColumnName).
                append(" = ?");
        return this;
    }

    @Override
    public String toString() {
        return query.toString();
    }

    public static StringBuilder buildQueryWithCondition(String queryBody, String criteriaTableName, String criteriaColumnName) {
        StringBuilder sb = new StringBuilder(queryBody);
        sb.append(" WHERE ").append(criteriaTableName).append(".").append(criteriaColumnName).
                append(" = ").append("?");
        return sb;
    }


    public static StringBuilder addStringCondition(String query, String criteriaTableName, String criteriaColumnName) {
        StringBuilder sb = new StringBuilder(query);
        sb.append(" AND ").append(criteriaTableName).append(".").append(criteriaColumnName).
                append(" = ").append("\"?\"");
        return sb;
    }

    public static StringBuilder addIntCondition(String query, String criteriaTableName, String criteriaColumnName) {
        StringBuilder sb = new StringBuilder(query);
        sb.append(" AND ").append(criteriaTableName).append(".").append(criteriaColumnName).
                append(" = ?");
        return sb;
    }

    public static void orderingQuery(StringBuilder sb, int sortingOrder, String table, String column) {
        if (sortingOrder != ORDER_NONE) {
            sb.append(" ORDER BY ");
            sb.append(table).append(".").append(column);
            sb.append(" COLLATE NOCASE ");
            if (sortingOrder == ORDER_DESC) {
                sb.append(" DESC");
            } else {
                sb.append(" ASC");
            }
        }
    }
}

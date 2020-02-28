package com.musicLib.repository.SQLightRepository;

import static com.musicLib.repository.SQLightRepository.MetaDataSQL.*;

 class QueryBuilder {

    private static final String SELECT = "SELECT ";
    private static final String FROM = " FROM ";
    private static final String INNER_JOIN = " INNER JOIN ";
    private static final String ON = " ON ";
    private static final String WHERE = " WHERE ";
    private static final String AND = " AND ";
    private static final String OR = " OR ";
    private static final String DELETE_FROM = "DELETE FROM ";
    private static final String INSERT_INTO = "INSERT INTO ";

    //TODO UTILS
    private StringBuilder query = new StringBuilder();

     QueryBuilder() {

    }

     QueryBuilder startQuery(String tableName, String columnName) {
        query.append(SELECT).append(tableName).append(".").append(columnName);
        return this;
    }

     QueryBuilder startQueryAll() {
        query.append(SELECT).append(" * ");
        return this;
    }

     QueryBuilder addSelection(String tableName, String columnName) {
        query.append(", ").append(tableName).append(".").append(columnName);
        return this;
    }

     QueryBuilder queryFrom(String tableName) {
        query.append(FROM).append(tableName);
        return this;
    }

     QueryBuilder innerJoinOn(String initialTable, String initialColumnName, String secondTable, String secondColumnName) {
        query.append(INNER_JOIN).append(initialTable).append(ON).append(initialTable).append(".").append(initialColumnName)
                .append(" = ").append(secondTable).append(".").append(secondColumnName);
        return this;
    }

     QueryBuilder specifyFirstCondition(String criteriaTableName, String criteriaColumnName) {
        query.append(WHERE).append(criteriaTableName).append(".").append(criteriaColumnName).
                append(" = ?");
        return this;
    }

     QueryBuilder addANDCondition(String criteriaTableName, String criteriaColumnName) {
        query.append(AND).append(criteriaTableName).append(".").append(criteriaColumnName).
                append(" = ?");
        return this;
    }

     QueryBuilder addORCondition(String criteriaTableName, String criteriaColumnName) {
        query.append(OR).append(criteriaTableName).append(".").append(criteriaColumnName).
                append(" = ?");
        return this;
    }


     QueryBuilder insertTo(String criteriaTableName) {
        query.append(INSERT_INTO).append(criteriaTableName);
        return this;
    }

     QueryBuilder insertSpecifyColumns(String columnName) {
        query.append(" (").append(columnName).append(") VALUES (?)");
        return this;
    }

     QueryBuilder insertSpecifyColumns(String columnName1, String columnName2) {
        query.append(" (").append(columnName1).append(",").append(columnName2).append(") VALUES (?,?)");
        return this;
    }

     QueryBuilder insertSpecifyColumns(String columnName1, String columnName2, String columnName3) {
        query.append(" (").append(columnName1).append(",").append(columnName2).append(",").append(columnName3).append(") VALUES (?,?,?)");
        return this;
    }

     QueryBuilder deleteFrom(String tableName){
        query.append(DELETE_FROM).append(tableName);
        return this;
    }

    @Override
     public String toString() {
        return query.toString();
    }

     static StringBuilder buildQueryWithCondition(String queryBody, String criteriaTableName, String criteriaColumnName) {
        StringBuilder sb = new StringBuilder(queryBody);
        sb.append(" WHERE ").append(criteriaTableName).append(".").append(criteriaColumnName).
                append(" = ").append("?");
        return sb;
    }


     static StringBuilder addStringCondition(String query, String criteriaTableName, String criteriaColumnName) {
        StringBuilder sb = new StringBuilder(query);
        sb.append(" AND ").append(criteriaTableName).append(".").append(criteriaColumnName).
                append(" = ").append("\"?\"");
        return sb;
    }

     static StringBuilder addIntCondition(String query, String criteriaTableName, String criteriaColumnName) {
        StringBuilder sb = new StringBuilder(query);
        sb.append(" AND ").append(criteriaTableName).append(".").append(criteriaColumnName).
                append(" = ?");
        return sb;
    }

     static void orderingQuery(StringBuilder sb, int sortingOrder, String table, String column) {
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

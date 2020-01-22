package com.musicLib.repository.SQLightRepository;

public class QueryBuilder {

    private static String buildQueryWithStringCondition(String queryBody, String tableName, String columnName, String criteriaName){
        StringBuilder sb = new StringBuilder(queryBody);
        sb.append(" WHERE ").append(tableName).append(".").append(columnName).
                append(" = ").append("\"").append(criteriaName).append("\"");
        return sb.toString();
    }

    private static String buildQueryWithIntCondition(String queryBody, String tableName, String columnName, int criteriaName){
        StringBuilder sb = new StringBuilder(queryBody);
        sb.append(" WHERE ").append(tableName).append(".").append(columnName).
                append(" = ").append(criteriaName);
        return sb.toString();
    }

    private static String addStringCondition(String query, String tableName, String columnName, String criteriaName){
        StringBuilder sb = new StringBuilder(query);
        sb.append(" AND ").append(tableName).append(".").append(columnName).
                append(" = ").append("\"").append(criteriaName).append("\"");
        return sb.toString();
    }
    private static String addIntCondition(String query, String tableName, String columnName, String criteriaName){
        StringBuilder sb = new StringBuilder(query);
        sb.append(" AND ").append(tableName).append(".").append(columnName).
                append(" = ").append(criteriaName);
        return sb.toString();
    }
}

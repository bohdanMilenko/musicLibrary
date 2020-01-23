package com.musicLib.repository.SQLightRepository;

import static com.musicLib.repository.SQLightRepository.MetaData.ORDER_DESC;
import static com.musicLib.repository.SQLightRepository.MetaData.ORDER_NONE;

public class QueryBuilder {

    public static StringBuilder buildQueryWithCondition(String queryBody, String criteriaTableName, String criteriaColumnName){
        StringBuilder sb = new StringBuilder(queryBody);
        sb.append(" WHERE ").append(criteriaTableName).append(".").append(criteriaColumnName).
                append(" = ").append("?");
        return sb;
    }


    public static StringBuilder addStringCondition(String query, String criteriaTableName, String criteriaColumnName){
        StringBuilder sb = new StringBuilder(query);
        sb.append(" AND ").append(criteriaTableName).append(".").append(criteriaColumnName).
                append(" = ").append("\"?\"");
        return sb;
    }
    public static StringBuilder addIntCondition(String query, String criteriaTableName, String criteriaColumnName){
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

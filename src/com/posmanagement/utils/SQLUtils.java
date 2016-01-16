package com.posmanagement.utils;

import java.util.ArrayList;
import java.util.Iterator;

public class SQLUtils {
    public static class WhereCondition {
        public WhereCondition(String object, String compareOpt, String value) {
            object_ = object;
            compareOpt_ = compareOpt;
            value_ = value;
            isValid_ =  true;
        }

        public WhereCondition(String object, String compareOpt, String value, boolean isValid) {
            object_ = object;
            compareOpt_ = compareOpt;
            value_ = value;
            isValid_ =  isValid;
        }

        public boolean isValid() {
            return isValid_ && object_ != null && compareOpt_ != null && value_ != null;
        }

        private String object_;
        private String compareOpt_;
        private String value_;
        private boolean isValid_;
    }

    public static String ConvertToSqlString(String normalString) {
        return "'" + normalString + "'";
    }

    public static String BuildWhereCondition(ArrayList<WhereCondition> whereConditions) {
        String whereSql = new String();

        if (whereConditions == null || whereConditions.size() <= 0) {
            return whereSql;
        }

        Iterator<WhereCondition> iterator = whereConditions.iterator();
        while (iterator.hasNext()) {
            WhereCondition whereCondition = iterator.next();
            if (!whereCondition.isValid()) {
                continue;
            }
            if (!whereSql.isEmpty()) {
                whereSql += " and ";
            }
            whereSql += whereCondition.object_ + " " +whereCondition.compareOpt_ + " " +whereCondition.value_;
        }
        return whereSql;
    }
}

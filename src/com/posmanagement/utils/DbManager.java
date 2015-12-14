package com.posmanagement.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class DbManager {
    private static String URL = "jdbc:mysql://localhost:3306/posmanagement?autoReconnect=true&useUnicode=true&characterEncoding=utf8";
    private static String USERNAME = "root";
    private static String USERPWD = "";

    public static synchronized DbManager createPosDbManager() throws SQLException, ClassNotFoundException {
        return new DbManager(URL, USERNAME, USERPWD);
    }

    private DbManager(String url, String user, String password) throws SQLException, ClassNotFoundException {
        loadDriver();
        connection_ = DriverManager.getConnection(url, user, password);
        connection_.setAutoCommit(false);
    }

    protected void finalize() {
        if (connection_ != null) {
            try {
                connection_.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean executeUpdate(String strSql) throws SQLException {
        Statement statement = null;
        try {
            statement = connection_.createStatement();
            if (0 < statement.executeUpdate(strSql)) {
                connection_.commit();
                return true;
            }
        }
        catch (SQLException sqlException) {
            connection_.rollback();
            throw sqlException;
        }
        finally {
            if (statement != null) {
                statement.close();
            }
        }
        return false;
    }

    public boolean executeUpdate(String strSql, HashMap<Integer, Object> prams) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection_.prepareStatement(strSql);
            fillSqlParamets(preparedStatement, prams);
            if (0 < preparedStatement.executeUpdate()) {
                connection_.commit();
                return true;
            }
        } catch (SQLException sqlException) {
            connection_.rollback();
            throw sqlException;
        }
        finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
        return false;
    }

    public ArrayList<HashMap<String, Object>> executeSql(String strSql) throws SQLException {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection_.createStatement();
            resultSet = statement.executeQuery(strSql);
            if (null != resultSet) {
                return convertResultSetToArrayList(resultSet);
            }
        }
        finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }

        return null;
    }

    public ArrayList<HashMap<String, Object>> executeSql(String strSql, HashMap<Integer, Object> prams) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection_.prepareStatement(strSql);
            fillSqlParamets(preparedStatement, prams);
            resultSet = preparedStatement.executeQuery();
            if (null != resultSet) {
                return convertResultSetToArrayList(resultSet);
            }
        }
        finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
        return null;
    }

    private static void loadDriver()
    {
        try {
            Class.forName(DRIVERNAME);
        }
        catch (ClassNotFoundException classNotFound) {
            classNotFound.printStackTrace();
        }
    }

    private PreparedStatement fillSqlParamets(PreparedStatement pStatement, HashMap<Integer, Object> parametMap)
            throws SQLException, ClassNotFoundException {
        if (null != parametMap) {
            if (0 <= parametMap.size()) {
                for (int i = 1; i <= parametMap.size(); i++) {
                    if (parametMap.get(i).getClass() == Class.forName("java.lang.String")) {
                        pStatement.setString(i, parametMap.get(i).toString());
                        continue;
                    }
                    if (parametMap.get(i).getClass() == Class.forName("java.sql.Date")) {
                        pStatement.setDate(i, java.sql.Date.valueOf(parametMap.get(i).toString()));
                        continue;
                    }
                    if (parametMap.get(i).getClass() == Class.forName("java.sql.Timestamp")) {
                        pStatement.setTimestamp(i, java.sql.Timestamp.valueOf(parametMap.get(i).toString()));
                        continue;
                    }
                    if (parametMap.get(i).getClass() == Class.forName("java.sql.Time")) {
                        pStatement.setTime(i, java.sql.Time.valueOf(parametMap.get(i).toString()) );
                        continue;
                    }
                    if (parametMap.get(i).getClass() == Class.forName("java.lang.Boolean")) {
                        pStatement.setBoolean(i, (Boolean) (parametMap.get(i)));
                        continue;
                    }
                    if (parametMap.get(i).getClass() == Class.forName("java.lang.Integer")) {
                        pStatement.setInt(i, (Integer) parametMap.get(i));
                        continue;
                    }
                    if (parametMap.get(i).getClass() == Class.forName("java.lang.Float")) {
                        pStatement.setFloat(i, (Float) parametMap.get(i));
                        continue;
                    }
                    if (parametMap.get(i).getClass() == Class.forName("java.lang.Double")) {
                        pStatement.setDouble(i, (Double) parametMap.get(i));
                        continue;
                    }
                    throw new ClassCastException();
                }
            }
        }
        return pStatement;
    }

    private ArrayList<HashMap<String, Object>> convertResultSetToArrayList(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        ArrayList<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();
        while (rs.next()) {
            LinkedHashMap <String, Object> resultHash = new LinkedHashMap <String, Object>();
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                resultHash.put(metaData.getColumnLabel(i + 1).toUpperCase(), rs
                        .getString(metaData.getColumnLabel(i + 1)));
            }
            resultList.add(resultHash);
        }
        return resultList;
    }

    private Connection connection_;

    private static String DRIVERNAME = "com.mysql.jdbc.Driver";
}
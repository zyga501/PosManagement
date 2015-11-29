package com.posmanagement.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class DbManager {
    public static synchronized DbManager getDbManager(String url) throws Exception{
        if (!dbManagerMap.containsKey(url)) {
            dbManagerMap.put(url, new DbManager(url));
        }
        return dbManagerMap.get(url);
    }

    /**
     * 执行SQL语句操作(更新数据 无参数)
     */
    public boolean executeUpdate(String strSql) throws SQLException {
        // getConnection(_DRIVER,_URL,_USER_NA,_PASSWORD);
        boolean flag = false;
        statement_ = connection_.createStatement();
        try {
            if (0 < statement_.executeUpdate(strSql)) {
                flag = true;
                connection_.commit();
            }
        } catch (SQLException ex) {
            flag = false;
            connection_.rollback();
            throw ex;
        }
        return flag;

    }

    /**
     * 执行SQL语句操作(更新数据 有参数)
     */
    public boolean executeUpdate(String strSql, HashMap<Integer, Object> prams)
            throws SQLException, ClassNotFoundException {
        // getConnection(_DRIVER,_URL,_USER_NA,_PASSWORD);
        boolean flag = false;
        try {
            preparedStatement_ = connection_.prepareStatement(strSql);
            setParamets(preparedStatement_, prams);
            if (0 < preparedStatement_.executeUpdate()) {
                flag = true;
                connection_.commit();
            }
        } catch (SQLException ex) {
            flag = false;
            connection_.rollback();
            throw ex;
        } catch (ClassNotFoundException ex) {
            connection_.rollback();
            throw ex;
        }
        return flag;

    }

    /**
     * 执行SQL语句操作(查询数据 无参数)
     *
     */
    public ArrayList<HashMap<String, Object>> executeSql(String strSql)
            throws Exception {
        statement_ = connection_.createStatement();
        resultSet_ = statement_.executeQuery(strSql);
        connection_.commit();
        if (null != resultSet_) {
            return convertResultSetToArrayList(resultSet_);
        }
        return null;
    }

    /**
     * 执行SQL语句操作(查询数据 有参数)
     *
     * @throws Exception
     */
    public ArrayList<HashMap<String, Object>> executeSql(String strSql,
                                                         HashMap<Integer, Object> prams) throws Exception {
        preparedStatement_ = connection_.prepareStatement(strSql);
        setParamets(preparedStatement_, prams);
        resultSet_ = preparedStatement_.executeQuery();
        connection_.commit();
        if (null != resultSet_) {
            return convertResultSetToArrayList(resultSet_);
        }
        return null;
    }

    /**
     * 执行存储过程(查询数据 无参数)
     *
     * @throws Exception
     */
    public ArrayList<HashMap<String, Object>> executeProcedureQuery(
            String procName) throws Exception {
        String callStr = "{call " + procName + "}";// 构造执行存储过程的sql指令
        CallableStatement cs = connection_.prepareCall(callStr);
        resultSet_ = cs.executeQuery();
        connection_.commit();
        cs.close();
        return convertResultSetToArrayList(resultSet_);
    }

    /**
     * 执行存储过程(查询数据,带参数)返回结果集合
     *
     * @param procName
     *            存储过程名称
     * @param parameters
     *            参数对象数组
     * @return 数组列表对象
     * @throws Exception
     */
    public ArrayList<HashMap<String, Object>> executeProcedureQuery(
            String procName, Object[] parameters) throws Exception {
        int parameterPoint = 0;
        // 获取存储过程信息列表集合
        ArrayList<HashMap<String, Object>> procedureInfo = getProcedureInfo(procName);
        // 获取存储过程的完全名称
        String procedureCallName = getProcedureCallName(procName,parameters.length);
        // 获取连接对象
        //getConnection();
        // 初始化 存储过程 执行对象
        CallableStatement cs = connection_.prepareCall(procedureCallName);
        // 参数下标变量
        int index = 0;
        // 获取 存储过程信息列表集合的 迭代器 对象
        Iterator<HashMap<String, Object>> iter = procedureInfo.iterator();
        // 遍历存储过程信息列表集合
        while (iter.hasNext()) {
            HashMap<String, Object> hm = iter.next();
            parameterPoint++;
            // 如果参数是输入参数 way = 0
            if (hm.get("WAY").equals("0")) {
                // 设置参数到cs
                cs.setObject(parameterPoint, parameters[index]);
                // 参数下标+1
                index++;
            }
        }
        // 释放这个对象,做为第二次使用
        procedureInfo = null;
        resultSet_ = cs.executeQuery();
        connection_.commit();
        procedureInfo = convertResultSetToArrayList(resultSet_);
        cs.close();
        return procedureInfo;

    }

    /**
     * 执行存储过程(更新，查询数据[简单查询、非纪录集]，返回输出参数[非纪录集])
     *
     * @throws Exception
     */
    public Object[] executeProcedureUpdate(String procName, Object[] parameters)
            throws Exception {
        CallableStatement cs = null;
        Object []returnVal = null;
        try {
            // 获取 存储过程 调用全名
            String fullPCallName = getProcedureCallName(procName,parameters.length);
            //获取存储过程参数信息
            ArrayList<HashMap<String, Object>> p_Call_Info_List = getProcedureInfo(procName);
            //获取连接
            // getConnection();
            //创建 存储过程 执行对象
            cs = connection_.prepareCall(fullPCallName);
            //数组下标
            int index = 1;
            //输出参数下标 纪录
            ArrayList<Integer> outPutIndexList = new ArrayList<Integer>();
            for(HashMap<String, Object> tempHash:p_Call_Info_List)
            {
                if("0".equals(tempHash.get("WAY")))
                {
                    //设置输入参数
                    cs.setObject(index, parameters[index-1]);
                }
                else
                {
                    //注册输出参数
                    cs.registerOutParameter(index, convertToSqlType(tempHash.get("TYPENAME").toString()));
                    //纪录输出参数的下标
                    outPutIndexList.add(index);
                }
                index++;
            }
            //-------------------- 执行 -----------------
            if(!cs.execute())
            {
                returnVal = new Object[outPutIndexList.size()];
                //取输 出参数的 返回值
                for(int i = 0 ;i<outPutIndexList.size();i++)
                {
                    returnVal[i] = cs.getObject(outPutIndexList.get(i));
                }
                connection_.commit();//提交
            }
        } catch (Exception e) {
            connection_.rollback();
            throw e;
        }
        return returnVal;
    }

    protected void finalize( ) {
        close_DB_Object();
    }

    /**
     * 关闭数据对象
     */
    private void close_DB_Object() {
        if (null != resultSet_) {
            try {
                resultSet_.close();
            } catch (SQLException ex) {
                resultSet_ = null;
            }
        }
        if (null != statement_) {
            try {
                statement_.close();
            } catch (SQLException ex) {
                statement_ = null;
            }
        }
        if (null != preparedStatement_) {
            try {
                preparedStatement_.close();
            } catch (SQLException ex) {
                preparedStatement_ = null;
            }
        }
        if (connection_ != null) {
            try {
                connection_.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private DbManager(String urls) throws Exception {
        initConnection(urls);
    }

    private void initConnection(String url) throws Exception {
        try {
            Class.forName(_DRIVER);
            if (url.equals(""))
                connection_ = DriverManager.getConnection(_URL, _USER_NA, _PASSWORD);
            else
                connection_ = DriverManager.getConnection(url);
            connection_.setAutoCommit(false);
        } catch (ClassNotFoundException ex) {
            throw ex;
        } catch (SQLException ex) {
            throw ex;
        }
    }

    /**
     * 设置Sql 指令参数
     */
    private PreparedStatement setParamets(PreparedStatement pStatement, HashMap<Integer, Object> paramets)
            throws ClassNotFoundException, SQLException {
        // 如果参数为空
        if (null != paramets) {
            // 如果参数个数为0
            if (0 <= paramets.size()) {
                for (int i = 1; i <= paramets.size(); i++) {
                    try {
                        // 字符类型 String
                        if (paramets.get(i).getClass() == Class.forName("java.lang.String")) {
                            pStatement.setString(i, paramets.get(i).toString());
                            continue;
                        }
                        // 日期类型 Date
                        if (paramets.get(i).getClass() == Class.forName("java.sql.Date")) {
                            pStatement.setDate(i, java.sql.Date.valueOf(paramets
                                    .get(i).toString()));
                            continue;
                        }
                        //
                        if (paramets.get(i).getClass() == Class.forName("java.sql.Timestamp")) {
                            pStatement.setTimestamp(i, java.sql.Timestamp.valueOf(paramets.get(i).toString()));
                            continue;
                        }
                        // 布尔类型 Boolean
                        if (paramets.get(i).getClass() == Class.forName("java.lang.Boolean")) {
                            pStatement.setBoolean(i, (Boolean) (paramets.get(i)));
                            continue;
                        }
                        // 整型 int
                        if (paramets.get(i).getClass() == Class.forName("java.lang.Integer")) {
                            pStatement.setInt(i, (Integer) paramets.get(i));
                            continue;
                        }
                        // 浮点 float
                        if (paramets.get(i).getClass() == Class.forName("java.lang.Float")) {
                            pStatement.setFloat(i, (Float) paramets.get(i));
                            continue;
                        }
                        // 双精度型 double
                        if (paramets.get(i).getClass() == Class.forName("java.lang.Double")) {
                            pStatement.setDouble(i, (Double) paramets.get(i));
                            continue;
                        }
                    } catch (ClassNotFoundException ex) {
                        throw ex;
                    } catch (SQLException ex) {
                        throw ex;
                    }
                }
            }
        }
        return pStatement;
    }

    /**
     * 转换记录集对象为数组列表对象
     *
     * @param rs
     *            纪录集合对象
     * @return 数组列表对象
     * @throws Exception
     */
    private ArrayList<HashMap<String, Object>> convertResultSetToArrayList(
            ResultSet rs) throws Exception {
        // 获取rs 集合信息对象
        ResultSetMetaData rsmd = rs.getMetaData();
        // 创建数组列表集合对象
        ArrayList<HashMap<String, Object>> tempList = new ArrayList<HashMap<String, Object>>();
        LinkedHashMap <String, Object> tempHash = null;//不要主动hash排序
        // 填充数组列表集合
        while (rs.next()) {
            // 创建键值对集合对象
            tempHash = new LinkedHashMap <String, Object>();
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                // 遍历每列数据，以键值形式存在对象tempHash中
                tempHash.put(rsmd.getColumnLabel(i + 1).toUpperCase(), rs
                        .getString(rsmd.getColumnLabel(i + 1)));
            }
            // 第一个键值对，存储在tempList列表集合对象中
            tempList.add(tempHash);
        }
        return tempList;// 返回填充完毕的数组列表集合对象
    }

    /**
     * 从数据库得到存储过程信息
     *
     * @param procName
     *            存储过程名称
     * @return 数组列表对象
     * @throws Exception
     */
    private ArrayList<HashMap<String, Object>> getProcedureInfo(String procName)
            throws Exception {
        return this.executeSql("select Syscolumns.isoutparam as Way,systypes.name as TypeName from sysobjects,syscolumns,systypes where systypes.xtype=syscolumns.xtype and syscolumns.id=sysobjects.id and sysobjects.name='"
                + procName + "' order by Syscolumns.isoutparam");
    }

    /**
     * 从数据库得到存储过程参数个数
     *
     * @param procName
     *            存储过程名称
     * @return 数组列表对象
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private int getParametersCount(String procName) throws Exception {
        int returnVal = 0;
        for (HashMap<String, Object> tempHas : this
                .executeSql("select count(*) as RowsCount from sysobjects,syscolumns,systypes where systypes.xtype=syscolumns.xtype and syscolumns.id=sysobjects.id and sysobjects.name='"
                        + procName + "'")) {
            returnVal = Integer.parseInt(tempHas.get("ROWSCOUNT").toString());
        }
        return returnVal;
    }

    /**
     * 得到调用存储过程的全名
     *
     * @param procName
     *            存储过程名称
     * @return 调用存储过程的全名
     * @throws Exception
     */
    private String getProcedureCallName(String procName, int prametCount)
            throws Exception {
        String procedureCallName = "{call " + procName;
        for (int i = 0; i < prametCount; i++) {
            if (0 == i) {
                procedureCallName = procedureCallName + "(?";
            }
            if (0 != i) {
                procedureCallName = procedureCallName + ",?";
            }
        }
        procedureCallName = procedureCallName + ")}";
        return procedureCallName;
    }

    /**
     * 得到数据类型的整型值
     *
     * @param typeName
     *            类型名称
     * @return 数据类型的整型值
     */
    private int convertToSqlType(String typeName) {
        if (typeName.equals("varchar"))
            return Types.VARCHAR;
        if (typeName.equals("int"))
            return Types.INTEGER;
        if (typeName.equals("bit"))
            return Types.BIT;
        if (typeName.equals("float"))
            return Types.FLOAT;
        return 0;
    }

    // 数据库连接对象
    private Connection connection_;
    // SQL语句对象
    private Statement statement_;
    // 带参数的Sql语句对象
    private PreparedStatement preparedStatement_;
    // 记录集对象
    private ResultSet resultSet_;

    /** ***********************手动设置的连接参数********************************* */
    private static String _DRIVER = "com.mysql.jdbc.Driver";
    private static String _URL = "jdbc:mysql://localhost:3306/posmanagement?autoReconnect=true&useUnicode=true&characterEncoding=utf8";
    private static String _USER_NA = "root";
    private static String _PASSWORD = "";

    private static HashMap<String, DbManager> dbManagerMap = new HashMap<String, DbManager>();
}
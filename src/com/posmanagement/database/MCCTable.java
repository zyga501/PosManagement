package com.posmanagement.database;

import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class MCCTable {
    public static void main(String[] args) throws Exception {
        SqlSession sqlSession = Database.SqlSessionFactory().openSession(false);
        String statement = "com.posmanagement.database.mapping.mccMapper.getMCC";
        Object mcctable = sqlSession.selectOne(statement, "02d29419-a0ad-46db-943b-636aed5840e9");
        List<MCCTable> table = sqlSession.selectList(statement);
        sqlSession.close();
        System.out.println(table);
    }

    public String getUuid() {
        return uuid_;
    }

    public void setUuid(String uuid_) {
        this.uuid_ = uuid_;
    }

    public int getMcc() {
        return mccCode_;
    }

    public void setMcc(int mccCode_) {
        this.mccCode_ = mccCode_;
    }

    public boolean isStatus() {
        return status_;
    }

    public void setStatus(boolean status_) {
        this.status_ = status_;
    }

    private String uuid_;
    private int mccCode_;
    private boolean status_;
}

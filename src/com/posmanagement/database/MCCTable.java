package com.posmanagement.database;

public class MCCTable {

    public String getUuid_() {
        return uuid_;
    }

    public void setUuid_(String uuid_) {
        this.uuid_ = uuid_;
    }

    public int getMccCode_() {
        return mccCode_;
    }

    public void setMccCode_(int mccCode_) {
        this.mccCode_ = mccCode_;
    }

    public boolean isStatus_() {
        return status_;
    }

    public void setStatus_(boolean status_) {
        this.status_ = status_;
    }

    private String uuid_;
    private int mccCode_;
    private boolean status_;
}

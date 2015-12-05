package com.posmanagement.action;

import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;

import java.util.Map;

public abstract class JsonActionSupport extends ActionSupport {
    protected final static String ACTIONFINISHED = "actionFinished";

    private String actionResult;

    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult( Map resultMap) {
        actionResult = JSONObject.fromObject(resultMap).toString();
    }
}

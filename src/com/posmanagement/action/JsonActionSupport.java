package com.posmanagement.action;

import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;

import java.util.Map;

public abstract class JsonActionSupport extends ActionSupport {
    protected final static String ACTIONFINISHED = "ajaxActionFinished";

    private String ajaxActionResult;

    public String getAjaxActionResult() {
        return ajaxActionResult;
    }

    public void setAjaxActionResult( Map resultMap) {
        ajaxActionResult = JSONObject.fromObject(resultMap).toString();
    }
}

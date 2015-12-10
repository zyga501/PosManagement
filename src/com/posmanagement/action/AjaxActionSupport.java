package com.posmanagement.action;

import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;

import java.util.Map;

public abstract class AjaxActionSupport extends ActionSupport {
    private final static String AJAXACTIONCOMPLETED = "ajaxActionCompleted";
    private String ajaxActionResult;

    public String getAjaxActionResult() {
        return ajaxActionResult;
    }

    public String AjaxActionComplete() {
        return AJAXACTIONCOMPLETED;
    }
    public String AjaxActionComplete( Map resultMap) {
        ajaxActionResult = JSONObject.fromObject(resultMap).toString();
        return AJAXACTIONCOMPLETED;
    }
}

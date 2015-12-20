package com.posmanagement.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;

import java.util.Map;

public abstract class AjaxActionSupport extends ActionSupport {
    private final static String AJAXACTIONCOMPLETED = "ajaxActionCompleted";
    private String ajaxActionResult;
    private Map parameterMap;

    public void validate() {
        // lazyvalidate
        ActionContext context = ActionContext.getContext();
        parameterMap = context.getParameters();
    }

    public Object getParameter(String paramterName) {
        if (parameterMap == null || parameterMap.size() == 0) {
            return null;
        }

        if (parameterMap.containsKey(paramterName)) {
            return ((Object[]) parameterMap.get(paramterName))[0];
        }

        return null;
    }

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

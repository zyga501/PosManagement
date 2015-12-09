package com.posmanagement.action;

import com.posmanagement.utils.DbManager;
import com.posmanagement.webui.IndustryList;

import java.util.HashMap;
import java.util.Map;

public class IndustryAction extends AjaxActionSupport{
    private final static String INDUSTRYMANAGER = "industryManager";

    private String industryList;
    private String industryName;
    private String industryEnabled;

    public String getIndustryList() {
        return industryList;
    }

    public void setIndustryName(String _industryName) {
        industryName = _industryName;
    }

    public void setIndustryEnabled(String _industryEnabled) {
        industryEnabled = _industryEnabled;
    }

    public String Init() throws Exception {
        industryList = new IndustryList().generateHTMLString();
        return INDUSTRYMANAGER;
    }

    public String AddIndustry() throws Exception {
        Map map = new HashMap();
        if (industryName.length() == 0) {
            map.put("errorMessage", getText("addindustry.industryError"));
        }
        else {
            Map parametMap = new HashMap();
            parametMap.put(1, industryName);
            if (industryEnabled != null)
                parametMap.put(2, new String("on"));
            else
                parametMap.put(2, new String("off"));
            DbManager.createPosDbManager().executeUpdate("insert into industrytb(name,enabled) values(?,?)", (HashMap<Integer, Object>) parametMap);
            map.put("industryList", new IndustryList().generateHTMLString());
        }

        return AjaxActionComplete(map);
    }
}

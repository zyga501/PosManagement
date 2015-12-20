package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.webui.IndustryList;

import java.util.HashMap;
import java.util.Map;

public class IndustryAction extends AjaxActionSupport{
    private final static String INDUSTRYMANAGER = "industryManager";

    private String industryList;
    private String industryName;
    private String industryEnabled;
    private String uiMode;

    public String getIndustryList() {
        return industryList;
    }

    public void setIndustryName(String _industryName) {
        industryName = _industryName;
    }

    public void setIndustryEnabled(String _industryEnabled) {
        industryEnabled = _industryEnabled;
    }

    public void setUiMode(String _uiMode) {
        uiMode = _uiMode;
    }

    public String Init() throws Exception {
        industryList = new IndustryList(IndustryList.UIMode.TABLELIST).generateHTMLString();
        return INDUSTRYMANAGER;
    }

    public String FetchIndustryList() throws Exception {
        Map map = new HashMap();
        if (uiMode != null && uiMode.compareTo("SELECTLIST") == 0) {
            map.put("industryList", new IndustryList(IndustryList.UIMode.SELECTLIST).generateHTMLString());
        }
        else {
            map.put("industryList", new IndustryList(IndustryList.UIMode.TABLELIST).generateHTMLString());
        }

        return AjaxActionComplete(map);
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
            PosDbManager.executeUpdate("insert into industrytb(name,enabled) values(?,?)", (HashMap<Integer, Object>) parametMap);
            map.put("industryList", new IndustryList(IndustryList.UIMode.TABLELIST).generateHTMLString());
        }

        return AjaxActionComplete(map);
    }
}

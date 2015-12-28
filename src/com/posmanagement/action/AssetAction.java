package com.posmanagement.action;

import com.opensymphony.xwork2.ActionContext;
import com.posmanagement.utils.PosDbManager;
import com.posmanagement.webui.AssetList;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssetAction extends AjaxActionSupport {
    public final static String ASSETMANAGER = "assetManager";

    private String assetList;

    public String getAssetList() {
        return assetList;
    }

    public String Init() throws Exception {
        assetList = new AssetList().generateHTMLString();
        return ASSETMANAGER;
    }

    public String addAsset() throws Exception {
        Map map = new HashMap();
        try {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(getParameter("cardCode").toString());
            if (!isNum.matches()) {
                throw new IllegalArgumentException();
            }
            Map parametMap = new HashMap();
            parametMap.put(1, getParameter("assetMaster").toString());
            parametMap.put(2, getParameter("bankUUID").toString());
            parametMap.put(3, getParameter("cardCode").toString());
            parametMap.put(4, getParameter("balance").toString());
            parametMap.put(5, getParameter("signPwd").toString());
            parametMap.put(6, getParameter("cashPwd").toString());
            parametMap.put(7, getParameter("transferPwd").toString());
            parametMap.put(8, getParameter("atmCashPwd").toString());
            ActionContext ctx = ActionContext.getContext();
            HttpServletRequest request = (HttpServletRequest) ctx
                    .get(ServletActionContext.HTTP_REQUEST);
            HttpSession session = request.getSession(false);
            parametMap.put(9, session.getAttribute("userID"));
            PosDbManager.executeUpdate("insert into assettb(cardmaster,bankuuid,cardno,firstbalance,ebanksignpwd,ebankcashpwd," +
                    "ebanktransferpwd,atmcashpwd,salesman) values(?,?,?,?,?,?,?,?,?)", (HashMap<Integer, Object>)parametMap);
            map.put("assetList", new AssetList().generateHTMLString());
        }
        catch (Exception exception) {
            map.put("errorMessage", getText("AssetAction.InfoError"));
        }

        return AjaxActionComplete(map);
    }
}

package com.posmanagement.action;

import com.opensymphony.xwork2.ActionContext;
import com.posmanagement.utils.DbManager;
import com.posmanagement.utils.StringUtils;
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
    private String assetMaster;
    private String cardCode;
    private String bankName;
    private String balance;
    private String signPwd;
    private String cashPwd;
    private String transferPwd;
    private String atmCashPwd;

    public String getAssetList() {
        return assetList;
    }

    public void setAssetMaster(String _assetMaster) {
        assetMaster = _assetMaster;
    }

    public void setCardCode(String _cardCode) {
        cardCode = _cardCode;
    }

    public void setBankName(String _bankName) {
        bankName = _bankName;
    }

    public void setBalance(String _balance) {
        balance = _balance;
    }

    public void setSignPwd(String _signPwd) {
        signPwd = _signPwd;
    }

    public void setCashPwd(String _cashPwd) {
        cashPwd = _cashPwd;
    }

    public void setTransferPwd(String _transferPwd) {
        transferPwd = _transferPwd;
    }

    public void setAtmCashPwd(String _atmCashPwd) {
        atmCashPwd = _atmCashPwd;
    }

    public String Init() throws Exception {
        assetList = new AssetList().generateHTMLString();
        return ASSETMANAGER;
    }

    public String addAsset() throws Exception {
        Map map = new HashMap();
        if (StringUtils.isEmpty(assetMaster) || StringUtils.isEmpty(cardCode) ||
                StringUtils.isEmpty(bankName) || StringUtils.isEmpty(balance) ||
                StringUtils.isEmpty(signPwd) || StringUtils.isEmpty(cashPwd) ||
                StringUtils.isEmpty(transferPwd) || StringUtils.isEmpty(atmCashPwd)) {
            map.put("errorMessage", getText("AssetAction.InfoError"));
        }
        else {
            try {
                Pattern pattern = Pattern.compile("[0-9]*");
                Matcher isNum = pattern.matcher(cardCode);
                if (!isNum.matches()) {
                    throw new IllegalArgumentException();
                }
                Double.parseDouble(balance);
                Integer.parseInt(signPwd);
                Integer.parseInt(cashPwd);
                Integer.parseInt(cashPwd);
                Integer.parseInt(transferPwd);
                Integer.parseInt(atmCashPwd);
                Map parametMap = new HashMap();
                parametMap.put(1, assetMaster);
                parametMap.put(2, bankName);
                parametMap.put(3, cardCode);
                parametMap.put(4, balance);
                parametMap.put(5, signPwd);
                parametMap.put(6, cashPwd);
                parametMap.put(7, transferPwd);
                parametMap.put(8, atmCashPwd);
                ActionContext ctx = ActionContext.getContext();
                HttpServletRequest request = (HttpServletRequest) ctx
                        .get(ServletActionContext.HTTP_REQUEST);
                HttpSession session = request.getSession(false);
                parametMap.put(9, session.getAttribute("userID"));
                DbManager.createPosDbManager().executeUpdate("insert into assettb(cardmaster,bankname,cardno,firstbalance,ebanksignpwd,ebankcashpwd," +
                        "ebanktransferpwd,atmcashpwd,salesman) values(?,?,?,?,?,?,?,?,?)", (HashMap<Integer, Object>)parametMap);
                map.put("assetList", new AssetList().generateHTMLString());
            }
            catch (Exception exception) {
                map.put("errorMessage", getText("AssetAction.InfoError"));
            }
        }

        return AjaxActionComplete(map);
    }
}

package com.posmanagement.action;

import com.posmanagement.utils.DbManager;
import com.posmanagement.webui.AssetsList;
import com.posmanagement.webui.CardsList;

import java.util.HashMap;
import java.util.Map;

public class CardsAction extends AjaxActionSupport {
    private final static String CARDSMANAGER = "cardsManager";

    private String cardsList;

    public Map getCardinfo() {
        return cardinfo;
    }

    public void setCardinfo(Map cardinfo) {
        this.cardinfo = cardinfo;
    }

    private Map cardinfo;
/*
    private String paneltitle;
    private String inserttime;
    private String cardserial;
    private String cardno;
    private String bankname;
    private String creditamount;
    private String tempamount;
    private String templimitdate;
    private String useamount;
    private String billdate;
    private String pin;
    private String telpwd;
    private String tradepwd;
    private String enchashmentpwd;
    private String billafterdate;
    private String lastrepaymentdate;
    private String billemail;
    private String sfqy;
    private String commissioncharge;
    private String cardmaster;
    private String identityno;
    private String identitypicfront;
    private String identitypicback;
    private String cmaddress;
    private String cmtel;
    private String cmseccontact;
    private String salesman;
    private String memos;

    public String getPaneltitle() {
        return paneltitle;
    }

    public void setPaneltitle(String paneltitle) {
        this.paneltitle = paneltitle;
    }

    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
    }

    public String getCardserial() {
        return cardserial;
    }

    public void setCardserial(String cardserial) {
        this.cardserial = cardserial;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getCreditamount() {
        return creditamount;
    }

    public void setCreditamount(String creditamount) {
        this.creditamount = creditamount;
    }

    public String getTempamount() {
        return tempamount;
    }

    public void setTempamount(String tempamount) {
        this.tempamount = tempamount;
    }

    public String getTemplimitdate() {
        return templimitdate;
    }

    public void setTemplimitdate(String templimitdate) {
        this.templimitdate = templimitdate;
    }

    public String getUseamount() {
        return useamount;
    }

    public void setUseamount(String useamount) {
        this.useamount = useamount;
    }

    public String getBilldate() {
        return billdate;
    }

    public void setBilldate(String billdate) {
        this.billdate = billdate;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getTelpwd() {
        return telpwd;
    }

    public void setTelpwd(String telpwd) {
        this.telpwd = telpwd;
    }

    public String getTradepwd() {
        return tradepwd;
    }

    public void setTradepwd(String tradepwd) {
        this.tradepwd = tradepwd;
    }

    public String getEnchashmentpwd() {
        return enchashmentpwd;
    }

    public void setEnchashmentpwd(String enchashmentpwd) {
        this.enchashmentpwd = enchashmentpwd;
    }

    public String getBillafterdate() {
        return billafterdate;
    }

    public void setBillafterdate(String billafterdate) {
        this.billafterdate = billafterdate;
    }

    public String getLastrepaymentdate() {
        return lastrepaymentdate;
    }

    public void setLastrepaymentdate(String lastrepaymentdate) {
        this.lastrepaymentdate = lastrepaymentdate;
    }

    public String getBillemail() {
        return billemail;
    }

    public void setBillemail(String billemail) {
        this.billemail = billemail;
    }

    public String getSfqy() {
        return sfqy;
    }

    public void setSfqy(String sfqy) {
        this.sfqy = sfqy;
    }

    public String getCommissioncharge() {
        return commissioncharge;
    }

    public void setCommissioncharge(String commissioncharge) {
        this.commissioncharge = commissioncharge;
    }

    public String getCardmaster() {
        return cardmaster;
    }

    public void setCardmaster(String cardmaster) {
        this.cardmaster = cardmaster;
    }

    public String getIdentityno() {
        return identityno;
    }

    public void setIdentityno(String identityno) {
        this.identityno = identityno;
    }

    public String getIdentitypicfront() {
        return identitypicfront;
    }

    public void setIdentitypicfront(String identitypicfront) {
        this.identitypicfront = identitypicfront;
    }

    public String getIdentitypicback() {
        return identitypicback;
    }

    public void setIdentitypicback(String identitypicback) {
        this.identitypicback = identitypicback;
    }

    public String getCmaddress() {
        return cmaddress;
    }

    public void setCmaddress(String cmaddress) {
        this.cmaddress = cmaddress;
    }

    public String getCmtel() {
        return cmtel;
    }

    public void setCmtel(String cmtel) {
        this.cmtel = cmtel;
    }

    public String getCmseccontact() {
        return cmseccontact;
    }

    public void setCmseccontact(String cmseccontact) {
        this.cmseccontact = cmseccontact;
    }

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }

    public String getMemos() {
        return memos;
    }

    public void setMemos(String memos) {
        this.memos = memos;
    }

*/
    public String getCardsList() {
        return cardsList;
    }

    public String Init() throws Exception {
        cardsList = new CardsList().generateHTMLString();
        return CARDSMANAGER;
    }

    public String AddCards() throws Exception {
        Map map = new HashMap();
        if (cardinfo.size()  == 0) {
            map.put("errorMessage", getText("addrates.ratesError"));
        }
        else {
            try {
                DbManager.getDafaultDbManager().executeUpdate("insert into cardtb(rates,enabled) values(?,?)", (HashMap<Integer, Object>) cardinfo);
                map.put("cardsList", new AssetsList().generateHTMLString());
            }
            catch (NumberFormatException exception) {
                map.put("errorMessage", getText("addrates.ratesFormatError"));
            }
        }

        setAjaxActionResult(map);
        return AjaxActionSupport.ACTIONFINISHED;
    }
}

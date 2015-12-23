package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.Readconfig;
import com.posmanagement.webui.BillList;
import com.posmanagement.webui.CardList;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CardAction extends AjaxActionSupport {
    private final static String CARDMANAGER = "cardManager";
    private final static String FETCHCARD = "fetchCard";

    private String cardList;
    private File filesfz1;
    private File filesfz2;

    private String newid ;
    private Map cardmanager;
    private String cardno;

    public Map getCardmanager() {
        return cardmanager;
    }

    public void setCardmanager(Map cardmanager) {
        this.cardmanager = cardmanager;
    }

    public String getNewid() {
        return newid;
    }

    public void setNewid(String newid) {
        this.newid = newid;
    }

    public File getFilesfz1() {
        return filesfz1;
    }

    public void setFilesfz1(File filesfz1) {
        this.filesfz1 = filesfz1;
    }

    public File getFilesfz2() {
        return filesfz2;
    }

    public void setFilesfz2(File filesfz2) {
        this.filesfz2 = filesfz2;
    }

    public String getCardList() {
        return cardList;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getCardno() {
        return cardno;
    }

    public String Init() throws Exception {
        cardList = new CardList().generateHTMLString();
        return CARDMANAGER;
    }

    public String FetchCard()
    {
        if (null==newid || (newid.equals(""))) return "";
        Map para= new HashMap();
        para.put(1,newid);
        try {
            ArrayList<HashMap<String, Object>> hashMaps = PosDbManager.executeSql("select * from cardtb where cid=?",( HashMap<Integer, Object>) para);
            if (hashMaps.size()<=0) return "";
            cardmanager = new HashMap();
            for (Object keyName:hashMaps.get(0).keySet())
                cardmanager.put(keyName.toString().toLowerCase(),hashMaps.get(0).get(keyName));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return FETCHCARD;
    }
    public String UpdateZsf() throws Exception {
        Map map = new HashMap();
        if(newid.equals("")) {

        }
        else
        {
            try {
                //从本地硬盘读取一张读片
                if (null != filesfz1) {
                    filesfz1.renameTo(new File(Readconfig.getfileds("imgpath"),newid+"_1.jpg"));
                }
                if (null != filesfz2) {
                    filesfz1.renameTo(new File(Readconfig.getfileds("imgpath"),newid+"_2.jpg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return AjaxActionComplete(map);
    }

    public String AddCard() throws Exception {
        Map map = new HashMap();
        if (cardmanager.size() == 0) {
        }
        else {
            Map para = new HashMap();
            try {
                //for (int i=0;i<cardmanager.size();i++) {
                int i=1;
                for (Object key : cardmanager.keySet()) {
                    System.out.print("'"+cardmanager.get(key)+"',");
                    para.put(i++,cardmanager.get(key).toString() );
                }
                if (!PosDbManager.executeUpdate("insert into cardtb(inserttime,cardserial,cardno,bankname,creditamount," +
                        "tempamount,templimitdate,useamount,billdate,pin,telpwd,tradepwd,enchashmentpwd," +
                        "billafterdate,lastrepaymentdate,billemail,status,commissioncharge,cardmaster,identityno," +
                        "cmaddress,cmtel,cmseccontact,salesman,memos) values(?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",(HashMap<Integer, Object>)  para))
                    map.put("errorMessage", getText("addrate.rateFormatError"));
                else
                    map.put("cardList", new CardList().generateHTMLString());
                map.put("newid",para.get(3));
            }
            catch (NumberFormatException exception) {
                map.put("errorMessage", getText("addrate.rateFormatError"));
            }
        }

        return AjaxActionComplete(map);
    }

    public String FetchMaster() throws Exception {
        if (null==cardno || cardno.trim().equals(""))
            return "";
        Map map = new HashMap();
        map.put("cardMaster", new CardList().generateMasterString(cardno));
        return AjaxActionComplete(map);
    }
}
package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.Readconfig;
import com.posmanagement.utils.UserUtils;
import com.posmanagement.webui.CardUI;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        cardList = new CardUI(super.getUserID()).generateCardTable("");
        getRequest().setAttribute("pagecount", (cardList.split("<tr").length-1)/CardUI.pagecontent+1);
        return CARDMANAGER;
    }

    public String FetchCardList(){
        String wherestr = " where 1=1 ";
        Map map = new HashMap();
        int i = 0;
        if (null!=getParameter("cardno") && (!getParameter("cardno").toString().trim().equals(""))){
            wherestr += "and cardno like '%"+getParameter("cardno")+"%'";
        }
        if (null!=getParameter("bankname")&& (!getParameter("bankname").toString().trim().equals(""))) {
            wherestr += "and banktb.name like '%"+getParameter("bankname")+"%'";
        }
        if (null!=getParameter("cardmaster")&& (!getParameter("cardmaster").toString().trim().equals(""))){
            wherestr += "and cardmaster  like '%"+getParameter("cardmaster")+"%'";
        }
        if (null!=getParameter("salesman")&& (!getParameter("salesman").toString().trim().equals(""))) {
            wherestr += "and userinfo.unick  like '%"+getParameter("salesman")+"%'";
        }

        try {
            ArrayList<HashMap<String, Object>> rect = PosDbManager.executeSql("select count(*) as cnt from cardtb inner join banktb " +
                    "on cardtb.bankuuid=banktb.uuid inner join userinfo on userinfo.uid=cardtb.salesmanuuid " +
                    wherestr);
            if (rect.size()<=0)
                map.put("pagecount",0);
            map.put("pagecount",Integer.parseInt(rect.get(0).get("CNT").toString())/CardUI.pagecontent+1);
            int curr = Integer.parseInt(null==getParameter("currpage")?"1":getParameter("currpage").toString());
            cardList = new CardUI(super.getUserID()).generateCardTable(wherestr+" limit "+String.valueOf((curr-1)*CardUI.pagecontent)+","+CardUI.pagecontent);
            map.put("cardList",cardList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxActionComplete(map);
    }

    public String FetchCard(){
        if (null==newid || (newid.equals(""))) return "";
        Map para= new HashMap();
        para.put(1,newid);
        try {
            ArrayList<HashMap<String, Object>> hashMaps = PosDbManager.executeSql("select cardtb.*,banktb.name bankname," +
                    "userinfo.unick salesman  from cardtb inner join banktb " +
                    "on cardtb.bankuuid=banktb.uuid inner join userinfo on userinfo.uid=cardtb.salesmanuuid" +
                    "  where cardno=?",( HashMap<Integer, Object>) para);
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
                    filesfz2.renameTo(new File(Readconfig.getfileds("imgpath"),newid+"_2.jpg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return AjaxActionComplete(map);
    }

    public String editCard() throws Exception {
        Map map = new HashMap();
        if (null==newid)
        {
            map.put("errorMessage", getText("addrate.rateFormatError"));
            return AjaxActionComplete(map);
        }
        if (cardmanager.size() == 0) {
        }
        else {
            Map para = new HashMap();
            try {
                //for (int i=0;i<cardmanager.size();i++) {
                int i=1;
                String[] strary=(new String("cardno,bankname,creditamount,tempamount,templimitdate," +
                        "useamount,billdate,pin,telpwd,tradepwd,enchashmentpwd,billafterdate,lastrepaymentdate," +
                        "billemail,status,commissioncharge,cardmaster,identityno,cmaddress,cmtel,cmseccontact," +
                        "memos,repaylimit,repaynum,repayinterval")).split(",");
                for (String key : strary) {
                    System.out.print("'"+((String[])cardmanager.get(key))[0]+"',");
                    para.put(i++,((String[])cardmanager.get(key))[0] );
                }
                para.put(i,newid);
                if (!PosDbManager.executeUpdate("update cardtb set cardno=?,bankuuid=?,creditamount=?," +
                        "tempamount=?,templimitdate=?,useamount=?,billdate=?,pin=?,telpwd=?,tradepwd=?,enchashmentpwd=?," +
                        "billafterdate=?,lastrepaymentdate=?,billemail=?,status=?,commissioncharge=?,cardmaster=?,identityno=?,"+
                        "cmaddress=?,cmtel=?,cmseccontact=?,memos=?,repaylimit=?,repaynum=?,repayinterval=?  where cardno=?",(HashMap<Integer, Object>)  para))
                    map.put("errorMessage", getText("addrate.rateFormatError"));
                {
                    cardList = new CardUI(super.getUserID()).generateCardTable("");
                    map.put("cardList",cardList);
                }
                map.put("newid",para.get(3));
            }
            catch (NumberFormatException exception) {
                map.put("errorMessage", getText("addrate.rateFormatError"));
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
                String[] strary=(new String("cardno,bankname,creditamount,tempamount,templimitdate," +
                        "useamount,billdate,pin,telpwd,tradepwd,enchashmentpwd,billafterdate,lastrepaymentdate," +
                        "billemail,status,commissioncharge,cardmaster,identityno,cmaddress,cmtel,cmseccontact," +
                        "memos")).split(",");
                para.put(i++, UUID.randomUUID().toString());
                para.put(i++, super.getUserID());
                for (String key : strary) {
                    System.out.print("'"+((String[])cardmanager.get(key))[0]+"',");
                    para.put(i++,((String[])cardmanager.get(key))[0] );
                }
                if (!PosDbManager.executeUpdate("insert into cardtb(uuid,salesmanuuid,cardno,bankuuid,creditamount," +
                        "tempamount,templimitdate,useamount,billdate,pin,telpwd,tradepwd,enchashmentpwd," +
                        "billafterdate,lastrepaymentdate,billemail,status,commissioncharge,cardmaster,identityno," +
                        "cmaddress,cmtel,cmseccontact,memos) values(?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",(HashMap<Integer, Object>)  para))
                    map.put("errorMessage", getText("addrate.rateFormatError"));
                else {
                    String userID = super.getUserID();
                    if (super.getUserName().equals("admin")) {
                        userID = "";
                    }
                    map.put("cardList", new CardUI(userID).generateCardTable(""));
                }
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
        map.put("cardMaster", new CardUI(super.getUserID()).generateMasterString(cardno));
        return AjaxActionComplete(map);
    }
}
package com.posmanagement.action;

import com.posmanagement.utils.DbManager;
import com.posmanagement.utils.Readconfig;
import com.posmanagement.webui.CardList;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CardAction extends AjaxActionSupport {
    private final static String CARDMANAGER = "cardManager";

    private String cardList;
    private File filesfz1;
    private File filesfz2;
    private String newid ;
    private String[] cardinfo  ;

    public String getNewid() {
        return newid;
    }

    public void setNewid(String newid) {
        this.newid = newid;
    }

    public String[] getCardinfo() {
        return cardinfo;
    }

    public void setCardinfo(String[] cardinfo) {
        this.cardinfo = cardinfo;
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

    public String Init() throws Exception {
        cardList = new CardList().generateHTMLString();
        return CARDMANAGER;
    }

    public String UpdateZsf() throws Exception {
        Map map = new HashMap();
        if(newid.equals(""))
            map.put("errorMessage", getText("addrate.rateError"));
        else
        {
            try {
                //从本地硬盘读取一张读片
                if (null != filesfz1) {
                    filesfz1.renameTo(new File(Readconfig.getfileds("imgpath"),newid+"_1.jpg"));
                }
                if (null != filesfz2) {
                    filesfz1.renameTo(new File(Readconfig.getfileds("imgpath"),newid+"_1."+ filesfz1.getName().split(".")[1]));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return AjaxActionComplete(map);
    }

    public String AddCard() throws Exception {
        Map map = new HashMap();
        if (cardinfo.length  == 0) {
            map.put("errorMessage", getText("addrate.rateError"));
        }
        else {
            Map para = new HashMap();
            try {
                for (int i=0;i<cardinfo.length;i++) {
                    System.out.print("'"+cardinfo[i]+"',");
                    para.put(i+1, cardinfo[i] );
                }
                if (!DbManager.createPosDbManager().executeUpdate("insert into cardtb(inserttime,cardserial,cardno,bankname,creditamount," +
                        "tempamount,templimitdate,useamount,billdate,pin,telpwd,tradepwd,enchashmentpwd," +
                        "billafterdate,lastrepaymentdate,billemail,sfqy,commissioncharge,cardmaster,identityno," +
                        "cmaddress,cmtel,cmseccontact,salesman,memos) values(?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",(HashMap<Integer, Object>)  para))
                    map.put("errorMessage", getText("addrate.rateFormatError"));
                else
                    map.put("cardList", new CardList().generateHTMLString());
                map.put("newid",para.get(3));//cardno
            }
            catch (NumberFormatException exception) {
                map.put("errorMessage", getText("addrate.rateFormatError"));
            }
        }

        return AjaxActionComplete(map);
    }
}
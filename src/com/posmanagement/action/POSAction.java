package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.UUIDUtils;
import com.posmanagement.webui.PosUI;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PosAction extends AjaxActionSupport {
    public final static String POSMANAGER = "posManager";
    private final static String FETCHPOS = "fetchPOS";

    private String posList;
    private String status;
    private String newid;
    private Map posManager;

    public Map getPosManager() {
        return posManager;
    }

    public void setPosManager(Map posManager) {
        this.posManager = posManager;
    }

    public String getPosList() {
        return posList;
    }

    public void setPosList(String posList) {
        this.posList = posList;
    }

    public String getNewid() {
        return newid;
    }

    public void setNewid(String newid) {
        this.newid = newid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String Init() throws Exception {
        posList = new PosUI().generateTable();
        return POSMANAGER;
    }

    public String FetchPOS(){
        if (null==newid || (newid.equals(""))) return "";
        Map para= new HashMap();
        para.put(1,newid);
        try {
            ArrayList<HashMap<String, Object>> hashMaps = PosDbManager.executeSql("SELECT * from postb where uuid=? ",( HashMap<Integer, Object>) para);
            if (hashMaps.size()<=0) return "";
            posManager = new HashMap();
            for (Object keyName:hashMaps.get(0).keySet())
                posManager.put(keyName.toString().toLowerCase(),hashMaps.get(0).get(keyName));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return FETCHPOS;
    }
    public String editPos(){
        Map map = new HashMap();
        if (null==newid)
        {
            map.put("errorMessage", "error");
            return AjaxActionComplete(map);
        }
            Map parametMap = new HashMap();
            try {
                parametMap.put(1, (String) getParameter("salesman"));
                parametMap.put(2, (String) getParameter("posname"));
                parametMap.put(3, (String) getParameter("industryname"));
                parametMap.put(4, (String) getParameter("rate"));
                parametMap.put(5, (String) getParameter("corporation"));
                parametMap.put(6, (String) getParameter("topqk"));
                parametMap.put(7, (String) getParameter("mcc"));
                parametMap.put(8, (String) getParameter("posserver"));
                parametMap.put(9, (String) getParameter("recipientbank"));
                parametMap.put(10, (String) getParameter("recipientaccount"));
                parametMap.put(11, (String) getParameter("startdatetm"));
                parametMap.put(12, (String) getParameter("usecount"));
                parametMap.put(13, (String) getParameter("useamount"));
                parametMap.put(14, (String) getParameter("status"));
                parametMap.put(15, (String) getParameter("newid"));

                    if (PosDbManager.executeUpdate("update postb set salesmanuname=?, posname=?,industryuuid=?,rateuuid=?,corporation=?,topqk=?,mccuuid=?," +
                            "posserveruuid=?,recipientbankuuid=?,recipientaccount=?,startdatetm=?,usecount=?," +
                            "useamount=?,status=? where uuid=?", (HashMap<Integer, Object>)parametMap)) {
                       // map.put("errorMessage","error");
                    }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                map.put("errorMessage", "error");
            }

        return AjaxActionComplete(map);
    }

    public String addPos(){
        Map map = new HashMap();
        Map parametMap = new HashMap();
        newid =UUIDUtils.generaterUUID();
        parametMap.put(1, newid);
        parametMap.put(2, (String) getParameter("posname"));
        parametMap.put(3, (String) getParameter("industryname"));
        parametMap.put(4, (String) getParameter("rate"));
        parametMap.put(5, (String) getParameter("corporation"));
        parametMap.put(6, (String) getParameter("topqk"));
        parametMap.put(7, (String) getParameter("mcc"));
        parametMap.put(8, (String) getParameter("posserver"));
        parametMap.put(9, (String) getParameter("recipientbank"));
        parametMap.put(10, (String) getParameter("recipientaccount"));
        parametMap.put(11, (String) getParameter("startdatetm"));
        parametMap.put(12, (String) getParameter("usecount"));
        parametMap.put(13, (String) getParameter("useamount"));
        parametMap.put(14, (String) getParameter("status"));
        parametMap.put(15, (String) getParameter("salesman"));

        try {
            if (PosDbManager.executeUpdate("insert into postb(uuid,posname,industryuuid,rateuuid,corporation,topqk,mccuuid," +
                    "posserveruuid,recipientbankuuid,recipientaccount,startdatetm,usecount,useamount,status,salesmanuuid)" +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", (HashMap<Integer, Object>)parametMap)) {
                map.put("posList", new PosUI().generateSelect());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return AjaxActionComplete(map);
    }
}

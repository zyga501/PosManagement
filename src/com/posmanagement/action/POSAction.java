package com.posmanagement.action;

import com.posmanagement.utils.PosDbManager;
import com.posmanagement.utils.StringUtils;
import com.posmanagement.utils.UUIDUtils;
import com.posmanagement.utils.UserUtils;
import com.posmanagement.webui.PosUI;
import com.posmanagement.webui.WebUI;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class POSAction extends AjaxActionSupport {
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
        posList = new PosUI(super.getUserID()).generateTable("");
        getRequest().setAttribute("pagecount", (posList.split("<tr").length-1)/WebUI.DEFAULTITEMPERPAGE+1);
        return POSMANAGER;
    }


    public String FetchPosList(){
        String wherestr = " where 1=1 ";
        Map map = new HashMap();
        int i = 0;
        if (null!=getParameter("posname") && (!getParameter("posname").toString().trim().equals(""))){
            wherestr += "and posname like '%"+getParameter("posname")+"%'";
        }
        if (null!=getParameter("industryname")&& (!getParameter("industryname").toString().trim().equals(""))) {
            wherestr += "and industrytb.`name` like '%"+getParameter("industryname")+"%'";
        }
        if (null!=getParameter("rate")&& (!getParameter("rate").toString().trim().equals(""))){
            wherestr += "and rate  = '"+getParameter("rate")+"'";
        }
        if (null!=getParameter("posserver")&& (!getParameter("posserver").toString().trim().equals(""))) {
            wherestr += "and posservertb.`name`   like '%"+getParameter("posserver")+"%'";
        }

        try {
            ArrayList<HashMap<String, Object>> rect = PosDbManager.executeSql("select count(*) as cnt   FROM   " +
                    " POSTB   " +
                    " INNER JOIN posservertb ON posservertb.uuid = POSTB.posserveruuid   " +
                    " INNER JOIN industrytb ON POSTB.industryuuid = industrytb.uuid   " +
                    " INNER JOIN ratetb ON POSTB.rateuuid = ratetb.uuid  " +
                    " INNER JOIN userinfo ON POSTB.salemanuuid = userinfo.uid   " +
                    " INNER JOIN mcctb ON mcctb.uuid = POSTB.mccuuid " +
                    wherestr);
            if (rect.size()<=0)
                map.put("pagecount",0);
            map.put("pagecount",Integer.parseInt(rect.get(0).get("CNT").toString())/ WebUI.DEFAULTITEMPERPAGE+1);
            int curr = Integer.parseInt(null==getParameter("currpage")?"1":getParameter("currpage").toString());
            posList = new PosUI(super.getUserID()).generateTable(wherestr+" limit "+String.valueOf((curr-1)*WebUI.DEFAULTITEMPERPAGE)+","+WebUI.DEFAULTITEMPERPAGE);
            map.put("posList",posList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxActionComplete(map);
    }

    public String FetchPOS(){
        if (null==newid || (newid.equals(""))) return "";
        Map para= new HashMap();
        para.put(1,newid);
        try {
            ArrayList<HashMap<String, Object>> hashMaps = PosDbManager.executeSql("SELECT a.*,c.unick as saleman,(select CONCAT(COUNT(b.amount),';',SUM(b.amount))" +
                    "  from  swingcard b where a.uuid=b.posuuid )used from postb a inner join userinfo c on" +
                    " c.uid=a.salemanuuid  where a.uuid=? ",( HashMap<Integer, Object>) para);
            if (hashMaps.size()<=0) return "";
            posManager = new HashMap();
            for (Object keyName:hashMaps.get(0).keySet())
                if (keyName.toString().toLowerCase().equals("used")) {
                    if (null!=hashMaps.get(0).get(keyName)) {
                        String[] usevalue = hashMaps.get(0).get(keyName).toString().split(";");
                        posManager.put("usedcount", usevalue[0]);
                        posManager.put("usedsum", usevalue[1]);
                    }
                    else{
                        posManager.put("usedcount","0" );
                        posManager.put("usedsum", "0");
                    }
                }
                else
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
                int i = 1;
                parametMap.put(i++, (String) getParameter("posname"));
                parametMap.put(i++, (String) getParameter("industryname"));
                parametMap.put(i++, (String) getParameter("rate"));
                parametMap.put(i++, (String) getParameter("corporation"));
                parametMap.put(i++, (String) getParameter("mcc"));
                parametMap.put(i++, (String) getParameter("posserver"));
                parametMap.put(i++,StringUtils.convertNullableString( getParameter("status")).equals("on")?"enable":"disable");
                parametMap.put(i++, (String) getParameter("newid"));

                    if (!PosDbManager.executeUpdate("update postb set  posname=?,industryuuid=?,rateuuid=?,corporation=?,mccuuid=?," +
                            "posserveruuid=?,status=?,startdatetm=now() where uuid=?", (HashMap<Integer, Object>)parametMap)) {
                        map.put("errorMessage","error");
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
        if (UserUtils.issaleman(getUserID())) {
            Map parametMap = new HashMap();
            int i = 1;
            newid = UUIDUtils.generaterUUID();
            parametMap.put(i++, newid);
            parametMap.put(i++, (String) getParameter("posname"));
            parametMap.put(i++, (String) getParameter("industryname"));
            parametMap.put(i++, (String) getParameter("rate"));
            parametMap.put(i++, (String) getParameter("corporation"));
            parametMap.put(i++, (String) getParameter("mcc"));
            parametMap.put(i++, (String) getParameter("posserver"));
            parametMap.put(i++, StringUtils.convertNullableString(getParameter("status")).equals("on") ? "enable" : "disable");
            parametMap.put(i++, getUserID());

            try {
                if (PosDbManager.executeUpdate("insert into postb(uuid,posname,industryuuid,rateuuid,corporation,mccuuid," +
                        "posserveruuid,status,salemanuuid)" +
                        "values(?,?,?,?,?,?,?,?,?)", (HashMap<Integer, Object>) parametMap)) {
                    map.put("posList", new PosUI(super.getUserID()).generateSelect());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        map.put("errorMessage", getText("global.noright"));
        return AjaxActionComplete(map);
    }
}

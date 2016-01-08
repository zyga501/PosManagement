package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserMenu extends WebUI {
    public UserMenu(String userID) {
        userID_ = userID;
    }

    public String generateHTMLString() throws Exception {
        return generateMainMenu(0);
    }

    private  String generateMainMenu(int pid) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchMenuByPid(pid);
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            String subMenu = generateSubMenu(Integer.parseInt(dbRet.get(index).get("ID").toString()));
            if (subMenu.length() != 0) {
                htmlString += generateMainMenuHtml(dbRet.get(index).get("ID").toString(),
                        dbRet.get(index).get("MENUNAME").toString(),
                        subMenu);
            }
        }
        return htmlString;
    }
    private String generateSubMenu(int pid) throws Exception {
        ArrayList<HashMap<String, Object>> dbRet = fetchMenuByPid(pid);
        if (dbRet.size() <= 0)
            return new String("");

        String htmlString = "";
        for (int index = 0; index < dbRet.size(); ++index) {
            htmlString += generateSubMenuHtml(dbRet.get(index).get("WEBPATH").toString(), dbRet.get(index).get("MENUNAME").toString());
        }
        return htmlString;
    }

    private String generateMainMenuHtml(String menuID, String menuName, String subMenu) {
        return new UIContainer("dl")
                .addAttribute("id", "1menu-article" + menuID)
                .addElement(new UIContainer("dt")
                            .addElement(new UIContainer("i", "&#xe616;")
                                        .addAttribute("class", "Hui-iconfont"))
                            .addElement("", menuName)
                            .addElement(new UIContainer("i", "&#xe6d5;")
                                        .addAttribute("class", "Hui-iconfont menu_dropdown-arrow")))
                .addElement(new UIContainer("dd")
                            .addElement(new UIContainer("ul")
                                        .addElement("", subMenu))).generateUI();
    }

    private  String generateSubMenuHtml(String webPath, String menuName) {
        return new UIContainer("li")
            .addElement(new UIContainer("a", menuName)
                        .addAttribute("_href", "./404.jsp", webPath.length() == 0)
                        .addAttribute("_href", webPath, webPath.length() != 0)).generateUI();
    }

    private ArrayList<HashMap<String, Object>> fetchMenuByPid(int pid) throws Exception {
        Map parametMap = new HashMap();
        parametMap.put(1,userID_);
        parametMap.put(2,pid);
        return PosDbManager.executeSql("select distinct aa.* from menutree aa, (select a.id,d.id as pid " +
                " from menutree d inner join  menutree a " +
                "  on d.id=a.preid inner join permission b on a.webpath =b.actionname  " +
                " inner join permissiondetail c on c.pid=b.pid inner join userinfo e on e.rid=c.rid  where  " +
                "  e.uid=?) b where aa.preid=? and (aa.id=b.id or aa.id =b.pid)" +
                " order by menuorder", (HashMap<Integer, Object>)parametMap);
       // return PosDbManager.executeSql("select * from menutree where preid=? order by menuorder", (HashMap<Integer, Object>)parametMap);
    }
    private String userID_; // TODO for role
}

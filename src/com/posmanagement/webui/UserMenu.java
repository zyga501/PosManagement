package com.posmanagement.webui;

import com.posmanagement.utils.PosDbManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserMenu {
    public UserMenu(int userID) {
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
        parametMap.put(1,pid);
        return PosDbManager.executeSql("select * from menutree where preid=? order by menuorder", (HashMap<Integer, Object>)parametMap);
    }

    private int userID_; // TODO for role
}

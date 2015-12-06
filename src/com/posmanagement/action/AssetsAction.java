package com.posmanagement.action;

import com.posmanagement.utils.DbManager;
import com.posmanagement.webui.AssetsList;

import java.util.HashMap;
import java.util.Map;

public class AssetsAction extends AjaxActionSupport {
    private final static String ASSETSMANAGER = "assetsManager";

    private String assetsList;
    private String assets;
    private String assetsEnabled;

    public String getAssetsList() {
        return assetsList;
    }

    public void setAssets(String _assets) {
        assets = _assets;
    }

    public void setAssetsEnabled(String _assetsEnabled) {
        assetsEnabled = _assetsEnabled;
    }

    public String Init() throws Exception{
        assetsList = new AssetsList().generateHTMLString();
        return ASSETSMANAGER;
    }

    public String AddAssets() throws Exception {
        Map map = new HashMap();
        if (assets.length() == 0) {
            map.put("errorMessage", getText("addassets.assetsError"));
        }
        else {
            try {
                Double.parseDouble(assets);
                Map parametMap = new HashMap();
                parametMap.put(1, assets);
                if (assetsEnabled != null)
                    parametMap.put(2, new String("on"));
                else
                    parametMap.put(2, new String("off"));
                DbManager.getDafaultDbManager().executeUpdate("insert into mcctb(mcc,enabled) values(?,?)", (HashMap<Integer, Object>) parametMap);
                map.put("assetsList", new AssetsList().generateHTMLString());
            }
            catch (NumberFormatException exception) {
                map.put("errorMessage", getText("addassets.assetsFormatError"));
            }
        }

        setAjaxActionResult(map);
        return AjaxActionSupport.ACTIONFINISHED;
    }
}

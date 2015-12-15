package com.posmanagement.action;

import com.posmanagement.webui.AssetList;

public class AssetAction extends AjaxActionSupport {
    public final static String ASSETMANAGER = "assetManager";

    private String assetList;

    public String getAssetList() {
        return assetList;
    }

    public String Init() throws Exception {
        assetList = new AssetList().generateHTMLString();
        return ASSETMANAGER;
    }
}

package com.posmanagement.action;

import com.posmanagement.utils.DbManager;
import com.posmanagement.webui.AssetsList;
import com.posmanagement.webui.RatesList;

import java.util.HashMap;
import java.util.Map;

public class RatesAction extends AjaxActionSupport {
    private final static String RATESMANAGER = "ratesManager";

    private String ratesList;
    private String rates;
    private String ratesEnabled;

    public String getRatesList() {
        return ratesList;
    }

    public void setRates(String _rates) {
        rates = _rates;
    }

    public void setRatesEnabled(String _ratesEnabled) {
        ratesEnabled = _ratesEnabled;
    }

    public String Init() throws Exception {
        ratesList = new RatesList().generateHTMLString();
        return RATESMANAGER;
    }

    public String AddRates() throws Exception {
        Map map = new HashMap();
        if (rates.length() == 0) {
            map.put("errorMessage", getText("addrates.ratesError"));
        }
        else {
            try {
                Double.parseDouble(rates);
                Map parametMap = new HashMap();
                parametMap.put(1, rates);
                if (ratesEnabled != null)
                    parametMap.put(2, new String("on"));
                else
                    parametMap.put(2, new String("off"));
                DbManager.createPosDbManager().executeUpdate("insert into ratestb(rates,enabled) values(?,?)", (HashMap<Integer, Object>) parametMap);
                map.put("ratesList", new AssetsList().generateHTMLString());
            }
            catch (NumberFormatException exception) {
                map.put("errorMessage", getText("addrates.ratesFormatError"));
            }
        }

        return AjaxActionComplete(map);
    }
}

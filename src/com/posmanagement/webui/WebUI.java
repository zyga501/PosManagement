package com.posmanagement.webui;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.TextProviderFactory;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.util.ValueStack;
import com.posmanagement.utils.SQLUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

class Multilanguage implements TextProvider, LocaleProvider
{
    private TextProvider getTextProvider() {
        if(this.textProvider == null) {
            TextProviderFactory tpf = new TextProviderFactory();
            if(this.container != null) {
                this.container.inject(tpf);
            }

            this.textProvider = tpf.createInstance(this.getClass(), this);
        }

        return this.textProvider;
    }

    public Locale getLocale() {
        ActionContext ctx = ActionContext.getContext();
        if(ctx != null) {
            return ctx.getLocale();
        }
        else {
            return null;
        }
    }

    public boolean hasKey(String key) {
        return this.getTextProvider().hasKey(key);
    }

    public String getText(String aTextName) {
        return this.getTextProvider().getText(aTextName);
    }

    public String getText(String aTextName, String defaultValue) {
        return this.getTextProvider().getText(aTextName, defaultValue);
    }

    public String getText(String aTextName, String defaultValue, String obj) {
        return this.getTextProvider().getText(aTextName, defaultValue, obj);
    }

    public String getText(String aTextName, List<?> args) {
        return this.getTextProvider().getText(aTextName, args);
    }

    public String getText(String key, String[] args) {
        return this.getTextProvider().getText(key, args);
    }

    public String getText(String aTextName, String defaultValue, List<?> args) {
        return this.getTextProvider().getText(aTextName, defaultValue, args);
    }

    public String getText(String key, String defaultValue, String[] args) {
        return this.getTextProvider().getText(key, defaultValue, args);
    }

    public String getText(String key, String defaultValue, List<?> args, ValueStack stack) {
        return this.getTextProvider().getText(key, defaultValue, args, stack);
    }

    public String getText(String key, String defaultValue, String[] args, ValueStack stack) {
        return this.getTextProvider().getText(key, defaultValue, args, stack);
    }

    public ResourceBundle getTexts() {
        return this.getTextProvider().getTexts();
    }

    public ResourceBundle getTexts(String aBundleName) {
        return this.getTextProvider().getTexts(aBundleName);
    }

    private transient TextProvider textProvider;
    private Container container;
}

public class WebUI extends Multilanguage {
    public static int DEFAULTITEMPERPAGE = 15;

    public void setUiConditions(ArrayList<SQLUtils.WhereCondition> uiConditions) {
        uiConditions_ = uiConditions;
    }

    public void clearUIConditions() {
        if (uiConditions_ != null) {
            uiConditions_.clear();
        }
    }

    ArrayList<SQLUtils.WhereCondition> uiConditions_;
}

package com.posmanagement.webui;

import java.util.*;

public class UIContainer extends WebUI {
    public UIContainer() {
    }

    public UIContainer(String element) {
        this(element, null);
    }

    public UIContainer(String element, String value) {
        if (element != null && element.length() > 0) {
            uiStart = "<" + element + " ";
            uiEnd = "</"+ element + ">";
        }
        uiValue = value;
    }

    public UIContainer addElement(UIContainer childObject) {
        uiElements.add(childObject);
        return this;
    }

    public UIContainer addElement(String element) {
        uiElements.add(new UIContainer(element));
        return this;
    }

    public UIContainer addElement(String element, String value) {
        uiElements.add(new UIContainer(element, value));
        return this;
    }

    public UIContainer addElement(String element, String value, String defaultValue) {
        if (value == null || value.isEmpty()) {
            value = defaultValue;
        }
        uiElements.add(new UIContainer(element, value));
        return this;
    }

    public UIContainer addAttribute(String attributeKey, String attributeValue) {
        uiAttributes.put(attributeKey, attributeValue);
        return this;
    }

    public UIContainer addAttribute(String attributeKey, String attributeValue, boolean ifNeed) {
        if (ifNeed) {
            uiAttributes.put(attributeKey, attributeValue);
        }
        return this;
    }

    public String generateUI() {
        String uiString = new String();
        if (uiStart != null) {
            uiString = uiStart;
            uiString += parseAttributes();
            uiString += ">";
        }
        if (uiValue != null)
            uiString += uiValue;
        String childUI = "";
        for (int index = 0; index < uiElements.size(); ++index) {
            childUI += uiElements.get(index).generateUI();
        }
        uiString += childUI;
        if (uiEnd != null) {
            uiString += uiEnd;
        }
        return uiString;
    }

    public String toString() {
        return generateUI();
    }

    private String parseAttributes() {
        String uiString = "";
        Iterator<Map.Entry<String, String>> keyValueParits = uiAttributes.entrySet().iterator();
        while (keyValueParits.hasNext()) {
            Map.Entry<String, String> keyValue = keyValueParits.next();
            uiString += keyValue.getKey();
            uiString += "=\"" + keyValue.getValue() + "\"";
        }
        return uiString;
    }

    private List<UIContainer> uiElements = new ArrayList<UIContainer>();
    private Map<String, String> uiAttributes = new HashMap<String, String>();
    private String uiStart;
    private String uiValue;
    private String uiEnd;
}

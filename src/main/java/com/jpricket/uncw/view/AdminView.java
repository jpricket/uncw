package com.jpricket.uncw.view;

public class AdminView {
    public AdminView() {

    }

    public String getHtml() {
        return "<html>" +
            "  <a href=\"admin\\load\">Click here to load the cache</a>" +
            "  <a href=\"admin\\refresh\">Click here to refresh the cache from UNCW</a>" +
            "</html>";
    }
}

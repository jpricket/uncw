package com.jpricket.uncw.data.model;

import org.jsoup.nodes.Element;

/**
 * Created by jpricket on 6/10/2017.
 */
public class Subject {
    private String code;
    private String name;

    public Subject(Element element) {
        name = element.text();
        code = element.attr("value");
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "code=" + code + "\nname=" + name;
    }
}

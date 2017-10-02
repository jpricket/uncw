package com.jpricket.uncw.data.model;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

public class Instructor {
    private String firstName;
    private String lastName;
    private String department;
    private String refNumber;
    private String overallQuality;
    private String difficulty;
    private String url;

    public Instructor() {
    }

    public Instructor(Element element) {
        final String ref = element.attr("href");
        refNumber = StringUtils.substringAfter(ref, "=");
        final String[] names = element.select("span.main").first().text().split(",");
        lastName = StringUtils.trim(names[0]);
        if (names.length > 1) {
            firstName = StringUtils.trim(names[1]);
        }
        final String[] subs = element.select("span.sub").first().text().split(",");
        if (subs.length > 1) {
            department = StringUtils.trim(subs[1]);
        }
        url = "http://www.ratemyprofessors.com" + ref;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public String getDepartment() {
        return department;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getOverallQuality() {
        return overallQuality;
    }

    public String getUrl() {
        return url;
    }
}

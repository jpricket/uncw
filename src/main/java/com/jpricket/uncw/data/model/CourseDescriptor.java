package com.jpricket.uncw.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CourseDescriptor {
    private String refNumber;
    private String subject;
    private String course;
    private String section;
    private String campus;
    private String creditHours;
    private String title;
    private String seatsRemaining;
    private String waitListCapacity;
    private String waitListActual;
    private String waitListRemaining;
    private String reservedSectionRemaining;
    private String instructor;
    private String dates;
    private String session;
    private String location;
    private String attribute;

    private CourseSchedule schedule;

    public CourseDescriptor() {
    }

    public CourseDescriptor(final Element element) {
        int index = 0;
        Elements cols = element.select("td");
        refNumber = cols.get(index++).text();
        subject = cols.get(index++).text();
        course = cols.get(index++).text();
        section = cols.get(index++).text();
        campus = cols.get(index++).text();
        creditHours = cols.get(index++).text();
        title = cols.get(index++).text();
        final String days = cols.get(index++).text();
        final String time;
        if (days.contains("TBA")) {
            time = "TBA";
        } else {
            time = cols.get(index++).text();
        }
        schedule = new CourseSchedule(days, time);
        seatsRemaining = cols.get(index++).text();
        waitListCapacity = cols.get(index++).text();
        waitListActual = cols.get(index++).text();
        waitListRemaining = cols.get(index++).text();
        reservedSectionRemaining = cols.get(index++).text();
        instructor = cols.get(index++).text();
        dates = cols.get(index++).text();
        session = cols.get(index++).text();
        if (!session.contains("TBA")) {
            location = cols.get(index++).text();
            attribute = cols.get(index++).text();
        }
    }

    public String getRefNumber() {
        return refNumber;
    }

    public String getSubject() {
        return subject;
    }

    public String getCourse() {
        return course;
    }

    @JsonIgnore
    public String getName() {
        return subject + course;
    }

    public String getSection() {
        return section;
    }

    public String getCampus() {
        return campus;
    }

    public String getCreditHours() {
        return creditHours;
    }

    public String getTitle() {
        return title;
    }

    public String getSeatsRemaining() {
        return seatsRemaining;
    }

    public String getWaitListCapacity() {
        return waitListCapacity;
    }

    public String getWaitListActual() {
        return waitListActual;
    }

    public String getWaitListRemaining() {
        return waitListRemaining;
    }

    public String getReservedSectionRemaining() {
        return reservedSectionRemaining;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getDates() {
        return dates;
    }

    public String getSession() {
        return session;
    }

    public String getLocation() {
        return location;
    }

    public String getAttribute() {
        return attribute;
    }

    public CourseSchedule getSchedule() {
        return schedule;
    }

    @Override
    public String toString() {
        return "refNumber=" + refNumber + "\n" +
            "subject=" + subject + "\n" +
            "course=" + course + "\n" +
            "section=" + section + "\n" +
            "campus=" + campus + "\n" +
            "creditHours=" + creditHours + "\n" +
            "title=" + title + "\n" +
            "schedule=" + schedule.toDisplayString() + "\n" +
            "seatsRemaining=" + seatsRemaining + "\n" +
            "waitListCapacity=" + waitListCapacity + "\n" +
            "waitListActual=" + waitListActual + "\n" +
            "waitListRemaining=" + waitListRemaining + "\n" +
            "reservedSectionRemaining=" + reservedSectionRemaining + "\n" +
            "instructor=" + instructor + "\n" +
            "dates=" + dates + "\n" +
            "session=" + session + "\n" +
            "location=" + location + "\n" +
            "attribute=" + attribute;
    }
}
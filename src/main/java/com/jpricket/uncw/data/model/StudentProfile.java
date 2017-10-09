package com.jpricket.uncw.data.model;

public class StudentProfile {
    private String id;
    private String name;
    private String year;
    private String[] courseCredits;
    private String[] registeredClasses;
    private String[] majors;
    private String[] optionalClasses;

    public StudentProfile(final String id, final String name, final String year,
                          final String[] courseCredits,
                          final String[] registeredClasses,
                          final String[] majors,
                          final String[] optionalClasses) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.courseCredits = courseCredits;
        this.registeredClasses = registeredClasses;
        this.majors = majors;
        this.optionalClasses = optionalClasses;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public String[] getCourseCredits() {
        return courseCredits;
    }

    public String[] getOptionalClasses() {
        return optionalClasses;
    }

    public String[] getRegisteredClasses() {
        return registeredClasses;
    }

    public String[] getMajors() {
        return majors;
    }
}

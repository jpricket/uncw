package com.jpricket.uncw.data.model;

public class StudentProfile {
    private String id;
    private String name;
    private String year;
    private String[] registeredClasses;
    private String[] requiredClasses;
    private String[] optionalClasses;

    public StudentProfile(final String id, final String name, final String year,
                          final String[] registeredClasses,
                          final String[] requiredClasses,
                          final String[] optionalClasses) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.registeredClasses = registeredClasses;
        this.requiredClasses = requiredClasses;
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

    public String[] getOptionalClasses() {
        return optionalClasses;
    }

    public String[] getRegisteredClasses() {
        return registeredClasses;
    }

    public String[] getRequiredClasses() {
        return requiredClasses;
    }
}

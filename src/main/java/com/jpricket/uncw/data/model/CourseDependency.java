package com.jpricket.uncw.data.model;

public class CourseDependency {
    public enum Relationship {
        Prerequisite,
        Corequisite
    }

    private final String course;
    private final String dependentCourse;
    private final Relationship relationship;

    public CourseDependency(final String course,
                            final String dependentCourse,
                            final Relationship relationship) {
        this.course = course;
        this.dependentCourse = dependentCourse;
        this.relationship = relationship;
    }

    public String getCourse() {
        return course;
    }

    public String getDependentCourse() {
        return dependentCourse;
    }

    public Relationship getRelationship() {
        return relationship;
    }
}

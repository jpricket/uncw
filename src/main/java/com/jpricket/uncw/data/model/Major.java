package com.jpricket.uncw.data.model;

import java.util.List;

public class Major {
    public static class CourseGroup {
        private final String name;
        private final String description;
        private final int count;
        private final String[] courses;
        public CourseGroup(final String name, final String description, final int count, final String... courses) {
            this.name = name;
            this.description = description;
            this.count = count;
            this.courses = courses;
        }

        public String getName() {
            return name;
        }

        public int getCount() {
            return count;
        }

        public String[] getCourses() {
            return courses;
        }

        public String getDescription() {
            return description;
        }
    }
    private final String id;
    private final String name;
    private final String college;
    private final List<CourseGroup> courseGroups;
    public Major(final String id, final String name, final String college, final List<CourseGroup> courseGroups) {
        this.id = id;
        this.name = name;
        this.college = college;
        this.courseGroups = courseGroups;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<CourseGroup> getCourseGroups() {
        return courseGroups;
    }

    public String getCollege() {
        return college;
    }
}

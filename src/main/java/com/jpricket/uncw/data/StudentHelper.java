package com.jpricket.uncw.data;

import com.jpricket.uncw.data.model.CourseDependency;
import com.jpricket.uncw.data.model.Major;
import com.jpricket.uncw.data.model.StudentProfile;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentHelper {
    public static List<String> getPossibleCourses(final StudentProfile student) {
        final List<String> possibleCourses = new ArrayList<>();
        final List<String> coursesTaken = new ArrayList<>();
        coursesTaken.addAll(Arrays.asList(student.getCourseCredits()));
        coursesTaken.addAll(Arrays.asList(student.getRegisteredClasses()));
        final String[] taken = coursesTaken.toArray(new String[coursesTaken.size()]);
        for(final String majorId : student.getMajors()) {
            final Major major = Store.getInstance().getMajor(majorId);
            if (major != null) {
                for (final Major.CourseGroup group : major.getCourseGroups()) {
                    possibleCourses.addAll(getPossibleCourses(taken, group.getCourses()));
                }
            }
        }
        possibleCourses.addAll(getPossibleCourses(taken, student.getOptionalClasses()));
        return possibleCourses;
    }

    public static List<String> getPossibleCourses(final String[] coursesTaken, final String[] coursesNeeded) {
        final List<CourseDependency> dependencies = Store.getInstance().getDependencies();
        final List<String> possibleCourses = new ArrayList<>();
        for(final String c : coursesNeeded) {
            if (contains(coursesTaken, c)) {
                continue;
            }
            boolean reqsMet = true;
            List<String> prereqs = getDependencies(dependencies, c, CourseDependency.Relationship.Prerequisite);
            for(final String prereq : prereqs) {
                if (!contains(coursesTaken, prereq)) {
                    reqsMet = false;
                    break;
                }
            }
            if (!reqsMet) {
                continue;
            }

            possibleCourses.add(c);
        }

        return possibleCourses;
    }

    private static boolean contains(final String[] courses, final String searchString) {
        for (final String c : courses) {
            if (StringUtils.equalsIgnoreCase(c, searchString)) {
                return true;
            }
        }

        return false;
    }

    private static List<String> getDependencies(final List<CourseDependency> dependencies, final String course, final CourseDependency.Relationship relationship) {
        final List<String> courseDependencies = new ArrayList<>();
        for (final CourseDependency d : dependencies) {
            if (StringUtils.equalsIgnoreCase(d.getCourse(), course) && d.getRelationship() == relationship) {
                courseDependencies.add(d.getDependentCourse());
            }
        }
        return courseDependencies;
    }
}

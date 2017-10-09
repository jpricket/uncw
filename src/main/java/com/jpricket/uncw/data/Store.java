package com.jpricket.uncw.data;

import com.jpricket.uncw.data.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Store {
    final Cache cache;
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(Store.class);

    private Store() {
        final String cacheLocation = "c:\\users\\jpricket\\desktop\\classCache3";
        try {
            cache = new Cache(cacheLocation);
            cache.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class StoreHolder {
        static final Store INSTANCE = new Store();
    }

    public static Store getInstance() {
        return StoreHolder.INSTANCE;
    }

    public void ensureLoaded() {
        if (!cache.hasStudents()) {
            try {
                cache.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void refreshCache(final String term) throws IOException {
        this.ensureLoaded();
        List<Instructor> instructors = cache.getInstructors();
        List<StudentProfile> students = cache.getStudents();

        cache.clearCache();
        List<CourseSection> courses = new ArrayList<>();
        final List<Subject> subjects = WebReader.getSubjects(term);
        for (final Subject s : subjects) {
            try {
                courses.addAll(WebReader.getCourses(term, s.getCode()));
            } catch (Exception ex) {
                logger.warn("Unable to load sections for term=" + term + ", subject=" + s.getName() + ". Details: " + ex.getMessage());
            }
        }

        cache.initialize(courses, instructors, students);
        cache.save();
    }

    public Major getMajor(final String majorId) {
        for(final Major m : cache.getMajors()) {
            if (StringUtils.equalsIgnoreCase(m.getId(), majorId)) {
                return m;
            }
        }
        return null;
    }

    public List<CourseDependency> getDependencies() {
        return cache.getDependencies();
    }

    public List<CourseSection> getCourses() {
        return cache.getCourses();
    }

    public StudentProfile getStudent(int studentId) {
        return cache.hasStudents() ? cache.getStudents().get(studentId) : null;
    }
}

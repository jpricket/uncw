package com.jpricket.uncw.data;

import com.jpricket.uncw.data.model.CourseSection;
import com.jpricket.uncw.data.model.StudentProfile;

import java.io.IOException;
import java.util.List;

public class Store {
    final Cache cache;

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

    public List<CourseSection> getCourses() {
        return cache.getCourses();
    }

    public StudentProfile getStudent(int studentId) {
        return cache.hasStudents() ? cache.getStudents().get(studentId) : null;
    }
}

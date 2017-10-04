package com.jpricket.uncw.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpricket.uncw.data.model.CourseSection;
import com.jpricket.uncw.data.model.Instructor;
import com.jpricket.uncw.data.model.StudentProfile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cache {
    private final String cacheFolder;
    private final String courseFolder;

    private List<CourseSection> courses;
    private List<Instructor> instructors;
    private List<StudentProfile> students;

    public Cache(final String cacheFolder) throws IOException {
        this.cacheFolder = cacheFolder;
        final Path p = Paths.get(cacheFolder, "courses");
        this.courseFolder = p.toString();
        Files.createDirectories(p);
    }

    public void load() throws IOException {
        instructors = readInstructors();
        courses = readCourses();
        //TODO students = readStudentProfiles();
        students = new ArrayList<>();
        students.add(new StudentProfile("10001", "Matthew Prickett", "first", 
                new String[0],
                new String[] { "csc231", "csc242", "spn303", "chm101", "chml101",
                        "phy201", "phyl201", "gly101", "glyl100"},
                new String[] {"art101", "art105", "hst101", "crw201", "eng233", "fst110",
                        "thr130", "thr230", "ebd280", "pls101",
                        "gly120", "glyl120", "mat261", "mat275", "mat335" }
                ));
    }

    public boolean hasCourses() {
        return courses != null && courses.size() > 0;
    }

    public boolean hasInstructors() {
        return instructors != null && instructors.size() > 0;
    }

    public boolean hasStudents() {
        return students != null && students.size() > 0;
    }

    public List<CourseSection> getCourses() {
        return courses;
    }

    public List<Instructor> getInstructors() {
        return instructors;
    }

    public List<StudentProfile> getStudents() {
        return students;
    }

    public void initialize(List<CourseSection> courses, List<Instructor> instructors, List<StudentProfile> students) {
        if (courses != null) {
            this.courses = courses;
        }
        if (instructors != null) {
            this.instructors = instructors;
        }
        if (students != null) {
            this.students = students;
        }
    }

    public void save() throws IOException {
        writeCourses(courses);
        writeInstructors(instructors);
        //TODO writeStudents(students);
    }

    public void clearCache() throws IOException {
        FileUtils.cleanDirectory(new File(cacheFolder));
    }

    private List<CourseSection> readCourses() throws IOException {
        final List<CourseSection> courses = new ArrayList<CourseSection>();
        final File folder = new File(courseFolder);
        if (folder.exists()) {
            for(final File f: folder.listFiles()) {
                Collections.addAll(courses, readCourses(f.getName()));
            }
        }
        return courses;
    }

    private CourseSection[] readCourses(final String filename) throws IOException {
        final List<String> lines = Files.readAllLines(Paths.get(courseFolder, filename));
        final ObjectMapper mapper = new ObjectMapper();
        CourseSection[] courses = mapper.readValue("[" + StringUtils.join(lines, ",\n") + "]", CourseSection[].class);
        return courses;
    }

    private List<Instructor> readInstructors() throws IOException {
        final List<Instructor> instructors = new ArrayList<Instructor>();
        final File file = Paths.get(cacheFolder, "instructors.json").toFile();
        if (file.exists()) {
            final List<String> lines = Files.readAllLines(file.toPath());
            final ObjectMapper mapper = new ObjectMapper();
            Collections.addAll(instructors,
                    mapper.readValue("[" + StringUtils.join(lines, ",\n") + "]", Instructor[].class));
        }
        return instructors;
    }

    private void writeCourses(final List<CourseSection> courses) throws IOException {
        if (courses != null) {
            for (final CourseSection cd : courses) {
                writeCourse(cd);
            }
        }
    }

    private void writeCourse(final CourseSection course) throws IOException {
        final String path = courseFolder + "\\" + course.getSubject() + course.getCourse() + ".json";
        final FileWriter writer = new FileWriter(path, true);
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String json = mapper.writeValueAsString(course);
            writer.write(json + "\n");
        } finally {
            writer.close();
        }
    }

    private void writeInstructors(final List<Instructor> instructors) throws IOException {
        if (instructors != null) {
            final String path = cacheFolder + "\\instructors.json";
            final FileWriter writer = new FileWriter(path, true);
            try {
                final ObjectMapper mapper = new ObjectMapper();
                for (final Instructor instructor : instructors) {
                    final String json = mapper.writeValueAsString(instructor);
                    writer.write(json + "\n");
                }
            } finally {
                writer.close();
            }
        }
    }
}


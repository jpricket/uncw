package com.jpricket.uncw.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpricket.uncw.data.model.*;
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
    private List<CourseDependency> dependencies;
    private List<Major> majors;

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
                new String[] { "csc131", "mat161", "mat162", "eng101", "eng201", "hst105", "hst106" },
                new String[] { "csc133", "stt215" },
                new String[] { "bscs"},
                new String[] { "spn303", "hon120", "hon121",
                        "ant207", "art101", "art105", "hst101", "crw201", "eng233", "fst110",
                        "thr130", "thr230", "ebd280", "pls101",
                        "gly120", "glyl120", "mat275", "mat335", "mus110", "mus111", "musl111" }
                ));
        //TODO load course relationships from file
        dependencies = new ArrayList<>();
        dependencies.add(new CourseDependency("csc231", "csc131", CourseDependency.Relationship.Prerequisite));
        dependencies.add(new CourseDependency("csc231", "csc133", CourseDependency.Relationship.Corequisite));
        dependencies.add(new CourseDependency("csc242", "csc131", CourseDependency.Relationship.Prerequisite));
        dependencies.add(new CourseDependency("csc242", "csc133", CourseDependency.Relationship.Prerequisite));
        dependencies.add(new CourseDependency("csc331", "csc231", CourseDependency.Relationship.Prerequisite));
        dependencies.add(new CourseDependency("csc340", "csc231", CourseDependency.Relationship.Prerequisite));
        dependencies.add(new CourseDependency("csc340", "mat162", CourseDependency.Relationship.Prerequisite));
        dependencies.add(new CourseDependency("csc342", "csc231", CourseDependency.Relationship.Prerequisite));
        dependencies.add(new CourseDependency("csc342", "csc242", CourseDependency.Relationship.Prerequisite));
        dependencies.add(new CourseDependency("csc360", "csc231", CourseDependency.Relationship.Prerequisite));
        dependencies.add(new CourseDependency("csc360", "csc242", CourseDependency.Relationship.Prerequisite));
        dependencies.add(new CourseDependency("csc380", "csc133", CourseDependency.Relationship.Prerequisite));
        dependencies.add(new CourseDependency("csc380", "csc231", CourseDependency.Relationship.Prerequisite));
        dependencies.add(new CourseDependency("csc380", "mat161", CourseDependency.Relationship.Prerequisite));
        dependencies.add(new CourseDependency("csc385", "eng101", CourseDependency.Relationship.Prerequisite));
        dependencies.add(new CourseDependency("csc434", "csc331", CourseDependency.Relationship.Prerequisite));
        dependencies.add(new CourseDependency("csc434", "csc360", CourseDependency.Relationship.Prerequisite));
        dependencies.add(new CourseDependency("csc450", "csc331", CourseDependency.Relationship.Prerequisite));
        dependencies.add(new CourseDependency("csc455", "csc331", CourseDependency.Relationship.Prerequisite));
        dependencies.add(new CourseDependency("mat162", "mat161", CourseDependency.Relationship.Prerequisite));

        //TODO load majors from files
        majors = new ArrayList<>();
        final List<Major.CourseGroup> groups = new ArrayList<>();
        groups.add(new Major.CourseGroup("Core Courses", "", 0,
                "csc131", "csc133", "csc231", "csc242", "csc331",
                "csc340", "csc342", "csc360", "csc380", "csc385",
                "csc434", "csc450", "csc455", "mat161", "mat162",
                "stt215"));
        groups.add(new Major.CourseGroup("Optional Courses", "Choose nine additional hours of 300-400 level CSC courses approved by an advisor", 3,
                "csc320", "csc344", "csc370", "csc475"));
        majors.add(new Major("bscs", "B.S. Computer Science", "Arts & Sciences",
                groups));
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

    public List<CourseDependency> getDependencies() {
        return dependencies;
    }

    public List<Major> getMajors() {
        return majors;
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
        // put back the courses folder
        Files.createDirectories(Paths.get(courseFolder));
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


package com.jpricket.uncw.data;

import com.jpricket.uncw.data.model.CourseSection;
import com.jpricket.uncw.data.model.Instructor;
import com.jpricket.uncw.data.model.Subject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class WebReader {
    public static List<CourseSection> getCourses(String term, String subjectCode) throws IOException {
        List<CourseSection> courses = new ArrayList<CourseSection>();
        Document doc = Jsoup.connect("https://seanet.uncw.edu/TEAL/swkfccl.P_GetCrse")
                .header("Referer","https://seanet.uncw.edu/TEAL/swkfccl.p_sel_term_date")
                .requestBody("term_in=" + term +
                        "&sel_subj=dummy&sel_day=dummy&sel_schd=dummy&sel_insm=dummy&sel_camp=dummy&sel_levl=dummy&sel_sess=dummy" +
                        "&sel_instr=dummy&sel_ptrm=dummy&sel_attr=dummy" +
                        "&sel_subj=" + subjectCode +
                        "&sel_crse=&sel_title=&sel_schd=%25&sel_insm=%25&sel_from_cred=&sel_to_cred=&sel_camp=%25" +
                        "&sel_levl=%25&sel_ptrm=%25&sel_instr=%25&sel_sess=%25&sel_attr=%25&begin_hh=0&begin_mi=0" +
                        "&begin_ap=a&end_hh=0&end_mi=0&end_ap=a")
                .post();
        final Elements elements = doc.select("tr:has(td[class=dddefault])");
        CourseSection lastCourse = null;
        for (final Element element : elements) {
            final CourseSection cs = new CourseSection((element));
            if (lastCourse != null && cs.getRefNumber().length() < 2) {
                // This is not a new course but more of the schedule
                lastCourse.getSchedule().add(cs.getSchedule().getDays().get(0), cs.getSchedule().getTimes().get(0));
            } else {
                courses.add(cs);
                lastCourse = cs;
            }
        }
        return courses;
    }

    //https://seanet.uncw.edu/TEAL/bwckctlg.p_display_courses?term_in=201820&one_subj=CSC&sel_crse_strt=133&sel_crse_end=133&sel_subj=&sel_levl=&sel_schd=&sel_coll=&sel_divs=&sel_dept=&sel_attr=

    public static List<Subject> getSubjects(String term) throws IOException {
        final List<Subject> subjects = new ArrayList<Subject>();
        final Document doc = Jsoup.connect("https://seanet.uncw.edu/TEAL/swkfccl.p_sel_term_date")
                .header("Referer", "https://seanet.uncw.edu/TEAL/twbkwbis.P_GenMenu?name=homepage")
                .requestBody("call_proc_in=swkfccl.p_sel_crse_search&term_in=" + term)
                .post();
        final Elements elements = doc.select("SELECT[NAME=sel_subj] > OPTION");
        for(final Element e: elements) {
            subjects.add(new Subject(e));
        }
        return subjects;
    }

    public static List<Instructor> getInstructors() throws IOException {
        final List<Instructor> instructors = new ArrayList<Instructor>();
        final String query = "http://www.ratemyprofessors.com/search.jsp?query=&queryoption=HEADER&stateselect=&country=&dept=&queryBy=teacherName&facetSearch=&schoolName=university+of+north+carolina+wilmington&offset=";
        int offset = 0;
        while(true) {
            try {
                final String query2 = query + offset;
                final Document doc = Jsoup.connect(query2)
                        //.requestBody("call_proc_in=swkfccl.p_sel_crse_search&term_in=" + term)
                        .post();
                final Elements elements = doc.select("a[href^=/ShowRatings.jsp?tid=]");
                for (final Element e : elements) {
                    instructors.add(new Instructor(e));
                }
                if (elements.size() < 20) {
                    // found the end of the list
                    break;
                }
                offset += elements.size();
            } catch (UnknownHostException ex) {
                // Keep trying
                continue;
            }
        }
        return instructors;
    }
}

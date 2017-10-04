package com.jpricket.uncw.data;

import com.jpricket.uncw.data.model.CourseSection;
import com.jpricket.uncw.data.model.CourseSchedule;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CourseFilter {
    private final List<CourseSection> courses;
    private final List<FilterExpression> filters;

    public static CourseFilter create(final List<CourseSection> courses) {
        return new CourseFilter(courses);
    }

    public CourseFilter(final List<CourseSection> courses) {
        this.courses = courses;
        this.filters = new ArrayList<>();
    }

    public CourseFilter where(final String field, final String operator, final String value) {
        filters.add(new FilterExpression(field, operator, value));
        return this;
    }

    public CourseFilter in(final String... refNumbers) {
        filters.add(new FilterExpression("refnumber", "in", StringUtils.join(refNumbers, ",")));
        return this;
    }

    public List<CourseSection> go() {
        final List<CourseSection> filteredCourses = new ArrayList<>();
        for(final CourseSection cd: courses) {
            boolean include = false;
            for(final FilterExpression exp: filters) {
                if (!exp.evaluate(cd)) {
                    include = false;
                    break;
                }
                include = true;
            }
            if (include) {
                filteredCourses.add(cd);
            }
        }
        return filteredCourses;
    }

    private class FilterExpression {
        private final String field;
        private final String operator;
        private final String value;

        public FilterExpression(final String field, final String operator, final String value) {
            this.field = field;
            this.operator = operator;
            this.value = value;
        }

        public boolean evaluate(final CourseSection courseSection) {
            final String fieldValue = getFieldValue(courseSection, field);
            switch (operator.toLowerCase()) {
                case "=": return StringUtils.equalsIgnoreCase(fieldValue, value);
                case "<": return StringUtils.compareIgnoreCase(fieldValue, value) < 0;
                case "<=": return StringUtils.compareIgnoreCase(fieldValue, value) <= 0;
                case ">": return StringUtils.compareIgnoreCase(fieldValue, value) > 0;
                case ">=": return StringUtils.compareIgnoreCase(fieldValue, value) >= 0;
                case "in": return StringUtils.containsIgnoreCase(value, fieldValue);
                case "contains": return StringUtils.containsIgnoreCase(fieldValue, value);
                case "starts with": return StringUtils.startsWithIgnoreCase(fieldValue, value);
                case "ends with": return StringUtils.endsWithIgnoreCase(fieldValue, value);
            }
            return false;
        }

        private String getFieldValue(final CourseSection courseSection, final String field) {
            switch(field.toLowerCase()) {
                case "name": return courseSection.getName();
                case "attribute": return courseSection.getAttribute();
                case "campus": return courseSection.getCampus();
                case "course": return courseSection.getCourse();
                case "credithours": return courseSection.getCreditHours();
                case "dates": return courseSection.getDates();
                case "instructor": return courseSection.getInstructor();
                case "location": return courseSection.getLocation();
                case "refnumber": return courseSection.getRefNumber();
                case "reservedsectionremaining": return courseSection.getReservedSectionRemaining();
                case "schedule": return courseSection.getSchedule().toString();
                case "schedule.days": return StringUtils.join(courseSection.getSchedule().getDayNames(),",");
                case "schedule.starttime": {
                    CourseSchedule.ClassTime time = courseSection.getSchedule().getStartTime();
                    String comparableTime = String.format("%02d%02d", time.Hour, time.Minute);
                    return comparableTime;
                }
                case "seatsremaining": return courseSection.getSeatsRemaining();
                case "section": return courseSection.getSection();
                case "session": return courseSection.getSession();
                case "subject": return courseSection.getSubject();
                case "title": return courseSection.getTitle();
                case "waitlistactual": return courseSection.getWaitListActual();
                case "waitlistcapacity": return courseSection.getWaitListCapacity();
                case "waitlistremaining": return courseSection.getWaitListRemaining();
            }
            return "";
        }


    }
}

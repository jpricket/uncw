package com.jpricket.uncw.data;

import com.jpricket.uncw.data.model.CourseDescriptor;
import com.jpricket.uncw.data.model.CourseSchedule;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CourseFilter {
    private final List<CourseDescriptor> courses;
    private final List<FilterExpression> filters;

    public static CourseFilter create(final List<CourseDescriptor> courses) {
        return new CourseFilter(courses);
    }

    public CourseFilter(final List<CourseDescriptor> courses) {
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

    public List<CourseDescriptor> go() {
        final List<CourseDescriptor> filteredCourses = new ArrayList<>();
        for(final CourseDescriptor cd: courses) {
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

        public boolean evaluate(final CourseDescriptor courseDescriptor) {
            final String fieldValue = getFieldValue(courseDescriptor, field);
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

        private String getFieldValue(final CourseDescriptor courseDescriptor, final String field) {
            switch(field.toLowerCase()) {
                case "name": return courseDescriptor.getName();
                case "attribute": return courseDescriptor.getAttribute();
                case "campus": return courseDescriptor.getCampus();
                case "course": return courseDescriptor.getCourse();
                case "credithours": return courseDescriptor.getCreditHours();
                case "dates": return courseDescriptor.getDates();
                case "instructor": return courseDescriptor.getInstructor();
                case "location": return courseDescriptor.getLocation();
                case "refnumber": return courseDescriptor.getRefNumber();
                case "reservedsectionremaining": return courseDescriptor.getReservedSectionRemaining();
                case "schedule": return courseDescriptor.getSchedule().toString();
                case "schedule.days": return StringUtils.join(courseDescriptor.getSchedule().getDayNames(),",");
                case "schedule.starttime": {
                    CourseSchedule.ClassTime time = courseDescriptor.getSchedule().getStartTime();
                    String comparableTime = String.format("%02d%02d", time.Hour, time.Minute);
                    return comparableTime;
                }
                case "seatsremaining": return courseDescriptor.getSeatsRemaining();
                case "section": return courseDescriptor.getSection();
                case "session": return courseDescriptor.getSession();
                case "subject": return courseDescriptor.getSubject();
                case "title": return courseDescriptor.getTitle();
                case "waitlistactual": return courseDescriptor.getWaitListActual();
                case "waitlistcapacity": return courseDescriptor.getWaitListCapacity();
                case "waitlistremaining": return courseDescriptor.getWaitListRemaining();
            }
            return "";
        }


    }
}

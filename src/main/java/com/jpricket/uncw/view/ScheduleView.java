package com.jpricket.uncw.view;

import com.jpricket.uncw.data.Cache;
import com.jpricket.uncw.data.CourseFilter;
import com.jpricket.uncw.data.model.CourseDescriptor;
import com.jpricket.uncw.data.model.CourseSchedule;
import com.jpricket.uncw.data.model.StudentProfile;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScheduleView {
    private List<Block> blocks = new ArrayList<>(10);
    private List<Block> optionalBlocks = new ArrayList<>(50);
    private Color[] colors = new Color[] {
            Color.cyan, Color.lightGray, Color.orange, Color.pink, Color.blue, Color.green
    };
    private int nextColor = 0;

    public ScheduleView() {
    }

    public ScheduleView(final Cache cache, final StudentProfile student) {
        List<CourseDescriptor> filteredCourses =
                CourseFilter.create(cache.getCourses())
                    .in(student.getRegisteredClasses())
                    .go();
        for(final CourseDescriptor course: filteredCourses) {
            addClass(course, true);
        }
        // Add optional classes
        String classes = String.join("; ", student.getRequiredClasses()) + "; " +
                String.join("; ", student.getOptionalClasses());
        addClasses(CourseFilter.create(cache.getCourses())
                .where("name", "in", classes)
                .go());
    }

    public void addClasses(final List<CourseDescriptor> courses) {
        for(CourseDescriptor course: courses) {
            addClass(course, false);
        }
    }

    public void addClass(final CourseDescriptor course, final boolean scheduled) {
        Color c = colors[nextColor++];
        if (nextColor >= colors.length) {
            nextColor = 0;
        }
        int index = 0;
        for(final CourseSchedule.ClassTime time: course.getSchedule().getClassTimes()) {
            addBlock(time, course, c, "b" + course.getRefNumber() + "_" + index, scheduled);
            index++;
        }
    }

    public String getHtml() {
        final StringBuilder sb = new StringBuilder();
        addHeader(sb);
        addScript(sb);
        startColumn1(sb);
        addScheduledClassSection(sb);
        addOptionalClassSection(sb);
        startColumn2(sb);
        addScheduleTable(sb);
        endColumns(sb);
        return sb.toString();
    }

    private void addHeader(final StringBuilder sb) {
        sb.append("<html>\n");
    }

    private void addScript(final StringBuilder sb) {
        startScript(sb);
        addToggleFunctions(sb, this.blocks, "");
        addToggleFunctions(sb, this.optionalBlocks, "opt_");
        endScript(sb);
    }

    private void startScript(final StringBuilder sb) {
        sb.append("    <script>\n" +
                "        function toggleBlock(block) {\n" +
                "            let cell = document.getElementById(block.locationId);\n" +
                "            let elem = document.getElementById(block.id);\n" +
                "            if (elem) {\n" +
                "                cell.removeChild(elem);            \n" +
                "            } else {\n" +
                "                let innerHtml = cell.innerHTML;\n" +
                "                innerHtml += `<div id=\"${block.id}\" style=\"position:absolute; width:140px; height:${block.height}px; background-color: #${block.color}; opacity:${block.opacity}; text-align:center; vertical-align:middle\" title=\"${block.hint}\">${block.name}</div>`;\n" +
                "                cell.innerHTML = innerHtml;\n" +
                "            }\n" +
                "        }\n" +
                "        function checkAllBoxes() {\n" +
                "            let i = 0;\n" +
                "            while(true) {\n" +
                "                let elem = document.getElementById(\"checkbox\" + i);\n" +
                "                if (elem) {\n" +
                "                    elem.click();\n" +
                "                } else {\n" +
                "                    break;\n" +
                "                }\n" +
                "                i++;\n" +
                "            }\n" +
                "        }\n" +
                "        function toggle_group(num, caption) {\n" +
                "            let groupId = 'group' + num; \n" +
                "            let groupElem = document.getElementById(groupId);\n" +
                "            if (groupElem) {\n" +
                "                let contentsId = groupId + '_contents';\n" +
                "                let contentsElem = document.getElementById(contentsId);\n" +
                "                if (contentsElem.style.display === 'none') {\n" +
                "                    // Expand the item\n" +
                "                    groupElem.innerText = '- ' + caption;\n" +
                "                    contentsElem.style.display = 'inline';\n" +
                "                } else {\n" +
                "                    // Collapse the item\n" +
                "                    groupElem.innerText = '+ ' + caption;\n" +
                "                    contentsElem.style.display = 'none';\n" +
                "                }\n" +
                "            }\n" +
                "        }\n");
    }

    private void addToggleFunctions(final StringBuilder sb, final List<Block> blocks, final String prefix) {
        if (blocks != null && blocks.size() > 0) {
            String refNumber = "";
            for (final Block b : blocks) {
                if (!b.getRefNumber().equals(refNumber)) {
                    // End the last function
                    if (StringUtils.isNotEmpty(refNumber)) {
                        sb.append("        }\n");
                    }
                    // Start a new function
                    refNumber = b.getRefNumber();
                    sb.append(String.format("        function toggle_%s%s(){\n", prefix, refNumber));
                }
                sb.append(String.format("            toggleBlock({ locationId: \"%s\", height: %d, id: \"%s\", name: \"%s\", color: \"%s\", opacity:%f,\n" +
                                "                hint: \"%s\",\ncreditHours: %d            });\n",
                        b.locationId, b.height, b.id, b.caption, b.getColorAsString(), b.opacity, b.hint, b.creditHours));
            }
            // End the last function
            sb.append("        }\n");
        }
    }

    private void endScript(final StringBuilder sb) {
        sb.append("        setTimeout(function() {\n" +
                "            checkAllBoxes();\n" +
                "        });\n");
        sb.append("    </script>\n");
    }

    private void startColumn1(final StringBuilder sb) {
        sb.append("    <table>\n" +
                "      <tr>\n" +
                "        <td width=\"33%\" style=\"vertical-align:top\">\n");
    }

    private void startColumn2(final StringBuilder sb) {
        sb.append("    </td>\n" +
                "    <td width=\"67%\" style=\"vertical-align:top\">\n");
    }

    private void endColumns(final StringBuilder sb) {
        sb.append("        </td>\n" +
                "      </tr>\n" +
                "    </table>\n");
    }

    private void addScheduledClassSection(final StringBuilder sb) {
        sb.append("    <section style=\"border-right-color: Black; border-right-width: 2px;\"><h1>Classes</h1>\n");
        sb.append("        <ul style=\"list-style-type:none\">\n");
        String refNumber = "";
        int count = 0;
        for(final Block b: blocks) {
            final String newRefNumber = b.getRefNumber();
            if (!newRefNumber.equals(refNumber)) {
                refNumber = newRefNumber;
                sb.append(String.format("        <li><input id=\"checkbox%d\" type=\"checkbox\" onclick=\"toggle_%s()\">%s (%s)</input></li>\n",
                        count++, newRefNumber, b.caption, newRefNumber));
            }
        }
        sb.append("        </ul>\n");
        sb.append("    </section>\n");
    }

    private void addOptionalClassSection(final StringBuilder sb) {
        sb.append("    <section style=\"border-right-color: Black; border-right-width: 2px;\"><h1>Optional Classes</h1>\n");
        String courseName = "";
        String refNumber = "";
        int group = 0;
        for(Block b: this.optionalBlocks) {
            final String newCourseName = b.caption;
            if (!newCourseName.equals(courseName)) {
                // End the last course
                if (StringUtils.isNotEmpty(courseName)) {
                    sb.append("        </div>\n");
                }
                // Start a new course
                courseName = newCourseName;
                sb.append(String.format("        <div id=\"group%d\" onclick=\"toggle_group(%d, '%s')\" style=\"cursor:pointer\">+ %s</div>\n",
                        group, group, courseName, courseName));
                sb.append(String.format("        <div id=\"group%d_contents\" style=\"display:none\">\n", group));
                sb.append("        <ul style=\"list-style-type:none\">\n");
                group++;
            }
            final String newRefNumber = b.getRefNumber();
            if (!newRefNumber.equals(refNumber)) {
                // Add a checkbox for this refNumber
                refNumber = newRefNumber;
                sb.append(String.format("        <li><input type=\"checkbox\" onclick=\"toggle_opt_%s()\">%s - %s</input></li>\n",
                        refNumber, refNumber, b.classDescription));
            }
        }
        sb.append("        </div>\n");
        sb.append("        </ul>\n");
        sb.append("    </section>\n");
    }

    private void addScheduleTable(final StringBuilder sb) {
        sb.append("    <table width=\"800px\" style=\"border-spacing:0px\">\n" +
                "        <tr>\n" +
                "            <th width=\"80px\"></th>\n" +
                "            <th width=\"144px\">Monday</th>\n" +
                "            <th width=\"144px\">Tuesday</th>\n" +
                "            <th width=\"144px\">Wednesday</th>\n" +
                "            <th width=\"144px\">Thursday</th>\n" +
                "            <th width=\"144px\">Friday</th>\n" +
                "        </tr>\n");
        for(int row = 0; row < 144; row++) {
            sb.append("        <tr height=\"5px\" style=\"overflow:visible; vertical-align:top;\">\n");
            sb.append("            <td width=\"80px\">");
            if (row % 6 == 0) {
                sb.append(String.format("<div style=\"position:absolute; width:inherit;\">%s</div>", getTime(row)));
            }
            sb.append("</td>\n");
            for(int col = 0; col < 5; col++) {
                sb.append(String.format("            <td width=\"%s\" id=\"%s\"></td>\n", "144px", getRowId(row, col)));
            }
            sb.append("        </tr>\n");
        }
        sb.append("    </table>\n</html>\n");
    }

    private String getRowId(final int row, final int column) {
        final String time = getTime(row);
        final String day = new String[] { "m", "t", "w", "h", "f" }[column];
        return day + time.replace(":", "");
    }

    private String getTime(final int row) {
        final int hour = (row / 12) + 8;
        final int min = (row % 12 == 0) ? 0 : (row % 12)*5;
        return String.format("%02d:%02d", hour, min);
    }

    private void addBlock(final CourseSchedule.ClassTime time, final CourseDescriptor course, final Color color,
                          final String blockId, final boolean scheduled) {
        final int dayIndex = getDayIndex(time.Day);
        final int timeIndex = getTimeIndex(time.Hour, time.Minute);
        final double opacity = scheduled ? 1.0 : 0.5;
        final String hint = String.format("%s - %s hours (%s)\\n%s %s - %s\\nLocation: %s", course.getTitle(),
                course.getCreditHours(), course.getRefNumber(),
                time.Day.toString(), getDisplayTime(time.Hour, time.Minute, 0),
                getDisplayTime(time.Hour, time.Minute, time.DurationMinutes), course.getLocation());
        final String classDescription = course.getSchedule().toDisplayString();
        final Block b = new Block(blockId, getRowId(timeIndex, dayIndex), time.DurationMinutes,
                course.getName(), color, hint, opacity, classDescription, (int)Float.parseFloat(course.getCreditHours()));
        if (scheduled) {
            this.blocks.add(b);
        } else {
            this.optionalBlocks.add(b);
        }
    }

    private String getDisplayTime(int hour, int minutes, final int addMinutes) {
        if (addMinutes != 0) {
            hour += (minutes + addMinutes) / 60;
            minutes = (minutes + addMinutes) % 60;
        }
        return String.format("%d:%02d", hour, minutes);
    }

    private int getDayIndex(final CourseSchedule.Day day) {
        switch(day) {
            case Monday: return 0;
            case Tuesday: return 1;
            case Wednesday: return 2;
            case Thursday: return 3;
            case Friday: return 4;
            default: return 0;
        }
    }

    private int getTimeIndex(final int hour, final int minute) {
        int index = (hour - CourseSchedule.FirstClassHour) * 12;
        index += minute / 5;
        return index;
    }

    private class Block {
        public int height;
        public String caption;
        public Color color;
        public String hint;

        public String locationId;
        public String id;
        public double opacity;
        public String classDescription;
        public int creditHours;


        public Block(final String id, final String locationId, final int height, final String caption,
                     final Color color, final String hint, final double opacity, final String classDescription,
                     final int creditHours) {
            this.locationId = locationId;
            this.id = id;
            this.height = height;
            this.caption = caption;
            this.color = color;
            this.hint = hint;
            this.opacity = opacity;
            this.classDescription = classDescription;
            this.creditHours = creditHours;
        }

        public String getColorAsString() {
            return Integer.toHexString(color.getRGB()).substring(2);
        }

        public String getRefNumber() {
            return id.substring(1, id.indexOf('_'));
        }
    }
}

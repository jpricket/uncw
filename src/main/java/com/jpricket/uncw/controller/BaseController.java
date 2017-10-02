package com.jpricket.uncw.controller;

import com.jpricket.uncw.data.Cache;
import com.jpricket.uncw.data.model.StudentProfile;
import com.jpricket.uncw.view.ScheduleView;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class BaseController {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(BaseController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String getRoot(ModelMap model) {

        model.addAttribute("message", "Welcome");
        logger.debug("root");

        // Spring uses InternalResourceViewResolver and return back index.jsp
        return "<html>\n" +
                "<body>\n" +
                "<h2>Hello Root!</h2>\n" +
                "</body>\n" +
                "</html>";
    }

    @RequestMapping(value = "/student/{id}/schedule", method = RequestMethod.GET)
    @ResponseBody
    public String welcomeName(@PathVariable String id, ModelMap model) throws IOException {
        final String cacheLocation = "c:\\users\\jpricket\\desktop\\classCache3";
        final Cache cache = new Cache(cacheLocation);
        cache.load();
        final StudentProfile student = cache.getStudents().get(0);
        final ScheduleView view = new ScheduleView(cache, student);
        return view.getHtml();
    }

}
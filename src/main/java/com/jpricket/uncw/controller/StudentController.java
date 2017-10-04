package com.jpricket.uncw.controller;

import com.jpricket.uncw.data.Store;
import com.jpricket.uncw.view.AdminView;
import com.jpricket.uncw.view.ScheduleView;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StudentController {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(StudentController.class);

    @RequestMapping(value = "/student/{id}/schedule", method = RequestMethod.GET)
    @ResponseBody
    public String getSchedule(@PathVariable String id, ModelMap model) {
        final ScheduleView view = new ScheduleView(Integer.parseInt(id));
        return view.getHtml();
    }

}
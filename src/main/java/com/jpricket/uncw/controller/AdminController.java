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

import java.io.IOException;

@Controller
public class AdminController {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(AdminController.class);

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    @ResponseBody
    public String admin(ModelMap model) {
        final AdminView view = new AdminView();
        return view.getHtml();
    }

    @RequestMapping(value = "/admin/load", method = RequestMethod.GET)
    public ModelAndView adminLoad(ModelMap model) {
        Store.getInstance().ensureLoaded();
        return new ModelAndView("redirect:/admin");
    }

    @RequestMapping(value = "/admin/refresh", method = RequestMethod.GET)
    public ModelAndView adminRefresh(ModelMap model) throws IOException {
        final String term = "201820"; // Year is plus one if fall term; 10 == fall, 20 == spring, 60 == summer
        Store.getInstance().refreshCache(term);
        return new ModelAndView("redirect:/admin");
    }

}
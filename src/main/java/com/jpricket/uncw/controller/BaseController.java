package com.jpricket.uncw.controller;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
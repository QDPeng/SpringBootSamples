package com.didispace.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author 程序猿DD
 * @version 1.0.0
 * @blog http://blog.didispace.com
 */
@Controller
public class HelloController {

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello World";
    }

    @RequestMapping("/")
    public ModelAndView index(ModelMap map) {
//        map.addAttribute("host", "http://blog.didispace.com");
//        return "index";
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("host", "http://blog.didispace.com");
        mv.addObject("clz", "red");

        return mv;
    }

}
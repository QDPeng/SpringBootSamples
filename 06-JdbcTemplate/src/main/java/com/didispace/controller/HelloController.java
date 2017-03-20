package com.didispace.controller;


import com.didispace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 程序猿DD
 * @version 1.0.0
 * @blog http://blog.didispace.com
 */
@Controller
public class HelloController {
    @Autowired
    UserService userService;

    @RequestMapping("/hello")
    public String hello() throws Exception {
        return null;
    }


}
package com.leo.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author zhaoqt
 */
@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "hello";
    }

    @GetMapping("/ping")
    public String ping() {
        return "ping";
    }

    @RequestMapping("/open/questionnaire")
    public String questionnaire() {
        return "ping";
    }


    @GetMapping("/livenessProbe")
    public String livenessProbe() {
        System.out.println("livenessProbe");
        throw new RuntimeException("123");
    }

    @GetMapping("/readinessProbe")
    public String readinessProbe() {
        System.out.println("readinessProbe");
        return "ok";
    }

    @GetMapping("/startupProbe")
    public String startupProbe() {
        return "ok";
    }


}

package com.leo.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title: TestController
 * @Author lzl
 */

@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping("/get")
    public String get() {

        System.out.println("nihao LEO");

        return "nihao LEO";
    }

    @GetMapping("/put")
    public String put() {

        System.out.println("太好了 LEO");

        return "太好了 LEO";
    }


}

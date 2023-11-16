package com.leo.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @Title: TestController
 * @Author lzl
 */

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @GetMapping("/get")
    public String get() {

        ArrayList<String> resultList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            try {
                redisTemplate.opsForValue().set("k" + i, "v" + i);
                resultList.add("set value success: " + i);

                Object val = redisTemplate.opsForValue().get("k" + i);
                resultList.add("get value success: " + val);

                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                resultList.add("error: " + e.getMessage());
            }
        }
        resultList.add("finished...");

        return resultList.toString();
    }


}

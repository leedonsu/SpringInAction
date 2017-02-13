package com.kakao.module.basic;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicContoller {

    @RequestMapping("/")
    public String init(){
        return "Hello 돈나명정미";
    }

}

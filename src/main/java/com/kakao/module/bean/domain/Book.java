package com.kakao.module.bean.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Louis on 2017-02-27.
 */
@Getter
@Setter
public class Book {
    private String name;
    private Integer price;

    public void init(){
        this.name = "스프링";
        this.price = 10000;
    }

}

package com.kakao.module.bean.domain;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

/**
 * Created by Louis on 2017-02-27.
 */
@Getter
@Setter
public class Car {

    private String name;
    private int price;

    @ConstructorProperties({"name", "price"})
    public Car(String name, int price){
        this.name = name;
        this.price = price;
    }

}

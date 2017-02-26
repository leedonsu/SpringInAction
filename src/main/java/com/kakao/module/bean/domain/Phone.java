package com.kakao.module.bean.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by Louis on 2017-02-27.
 */
@Getter
@Setter
public class Phone implements InitializingBean {

    private String name;
    private int price;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.name = "아이폰";
        this.price = 70000;
    }
}

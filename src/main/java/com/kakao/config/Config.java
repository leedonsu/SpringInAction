package com.kakao.config;

import com.kakao.module.bean.domain.Book;
import com.kakao.module.bean.domain.Car;
import com.kakao.module.bean.domain.Phone;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Created by Louis on 2017-02-27.
 */
@Configuration
@ComponentScan(basePackages = "com.kakao.module.bean")
public class Config {

    // https://en.wikipedia.org/wiki/JSR_250
    // JSR-330 metadata.
    @Scope("prototype")
    @Bean(initMethod = "init")
    public Book getBook(){
        return new Book();
    }

    @Bean
    public Phone getPhone(){
        return new Phone();
    }

    @Bean
    public Car getCar(){
        return new Car("아우디", 20000);
    }

}

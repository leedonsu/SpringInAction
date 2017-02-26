package com.kakao;

import com.kakao.module.bean.domain.Book;
import com.kakao.module.bean.domain.Phone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(Application.class, args);

        // BeanFactory
        Book book = app.getBean(Book.class);
        Book book2 = app.getBean(Book.class);
        Phone phone = app.getBean(Phone.class);
        Phone phone2 = app.getBean(Phone.class);

        log.info("book1 name : " + book.toString());
        log.info("book2 name : " + book2.toString());
        log.info("phone1 name : " + phone.toString());
        log.info("phone2 name : " + phone2.toString());

    }
}

package com.kakao.concert;

import org.aspectj.lang.annotation.*;

/**
 * Created by kakao on 2017. 3. 29..
 */
@Aspect
public class Audience {

    @Pointcut("execution(* com.kakao.concert.Performance.perform(..))")
    public void performance(){}

    @Before("performance()")
    public void silenceCellPhones() {
        System.out.println("폰 끄삼!");
    }

    @Before("performance()")
    public void takeSeats() {
        System.out.println("착석!");
    }

    @AfterReturning("performance()")
    public void applause() {
        System.out.println("짝! 짝! 짝!");
    }

    @AfterThrowing("performance()")
    public void demandRefund() {
        System.out.println("환불..ㅠㅜ");
    }
}

package com.kakao.concert;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Created by kakao on 2017. 3. 29..
 */
@Aspect
public class Audience {

    @Before("execution(** com.kakao.concert.Performance.perform(..))")
    public void silenceCellPhones() {
        System.out.println("폰 끄삼!");
    }

    @Before("execution(** com.kakao.concert.Performance.perform(..))")
    public void takeSeats() {
        System.out.println("착석!");
    }

    @AfterReturning("execution(** com.kakao.concert.Performance.perform(..))")
    public void applause() {
        System.out.println("짝! 짝! 짝!");
    }

    @AfterThrowing("execution(** com.kakao.concert.Performance.perform(..))")
    public void demandRefund() {
        System.out.println("환불..ㅠㅜ");
    }
}

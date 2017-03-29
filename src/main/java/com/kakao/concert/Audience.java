package com.kakao.concert;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * Created by kakao on 2017. 3. 29..
 */
@Aspect
public class Audience {

    @Pointcut("execution(* com.kakao.concert.Performance.perform(..))")
    public void performance() {
    }

    @Around("performance()")
    public void watchPerformance(ProceedingJoinPoint jp) {
        try {
            System.out.println("폰 끄삼!");
            System.out.println("착석!");
            jp.proceed();
            System.out.println("짝! 짝! 짝!");
        } catch (Throwable e) {
            System.out.println("환불..ㅠㅜ");
        }
    }
}

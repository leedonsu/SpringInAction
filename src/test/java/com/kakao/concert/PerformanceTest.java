package com.kakao.concert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by kakao on 2017. 3. 29..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ConcertConfig.class)
public class PerformanceTest {

    @Autowired
    Performance performance;

    @Test
    public void test(){
        performance.perform();
    }

}
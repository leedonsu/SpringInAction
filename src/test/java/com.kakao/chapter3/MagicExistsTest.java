package com.kakao.chapter3;

import static org.junit.Assert.*;

import com.kakao.module.chapter3.MagicConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by astone35 on 2017. 3. 8..
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=MagicConfig.class)
public class MagicExistsTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void shouldNotBeNull() {
        assertTrue(context.containsBean("magicBean"));
    }

}

package com.kakao.chapter2;

import com.kakao.module.chapter2.CDPlayerConfig;
import com.kakao.module.chapter2.CompactDisc;
import com.kakao.module.chapter2.MediaPlayer;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by ellie on 17. 2. 20..
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=CDPlayerConfig.class)
public class CDPlayerTest {
    @Autowired
    private CompactDisc cd;

    @Test
    public void cdShouldNotBeNull() {
        Assert.assertNotNull(cd);
    }

    @Rule
    public final SystemOutRule log = new SystemOutRule().enableLog();

    @Autowired
    private MediaPlayer player;

    @Test
    public void play() {
        player.play();

        String[] logStr = log.getLog().split("\n");
        Assert.assertEquals(
                "Playing Sgt. Pepper's Lonely Hearts Club Band by The Beatles",
                logStr[logStr.length-1]
        );
    }
}

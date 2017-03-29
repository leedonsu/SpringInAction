package com.kakao.soundsystem;

import com.kakao.concert.ConcertConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by jeongwoopark on 2017. 3. 30..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=SoundSystemConfig.class)
public class TrackCounterTest {

    @Autowired
    CompactDisc cd;
    @Autowired
    private TrackCounter counter;

    @Test
    public void testTrackCounter(){
        cd.playTrack(1);
        cd.playTrack(2);
        cd.playTrack(2);
        cd.playTrack(2);
        cd.playTrack(4);
        cd.playTrack(4);

        assertEquals(1,counter.getPlayCount(1));
        assertEquals(3,counter.getPlayCount(2));
        assertEquals(0,counter.getPlayCount(3));
        assertEquals(2,counter.getPlayCount(4));
    }
}
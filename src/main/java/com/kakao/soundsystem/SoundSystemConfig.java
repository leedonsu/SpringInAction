package com.kakao.soundsystem;

import com.kakao.concert.Audience;
import com.kakao.concert.Performance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kakao on 2017. 3. 30..
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan
public class SoundSystemConfig {
    @Bean
    public CompactDisc sgtPeppers(){
        BlankDisc cd = new BlankDisc();
        cd.setTitle("title");
        cd.setArtist("xman");
        List<String> tracks = new ArrayList<String>();
        tracks.add("1");
        tracks.add("2");
        tracks.add("3");
        tracks.add("4");
        tracks.add("5");

        cd.setTracks(tracks);
        return cd;
    }
    @Bean
    public TrackCounter trackCounter(){
        return new TrackCounter();
    }
}

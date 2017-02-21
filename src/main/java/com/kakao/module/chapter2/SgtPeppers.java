package com.kakao.module.chapter2;

import org.springframework.stereotype.Component;

/**
 * Created by ellie on 17. 2. 20..
 */

@Component
public class SgtPeppers implements CompactDisc {
    private String title = "Sgt. Pepper's Lonely Hearts Club Band";
    private String artist = "The Beatles";

    public void play() {
        System.out.println("Playing " + title + " by " + artist);
    }
}

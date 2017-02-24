package com.kakao.module.chapter2;

import org.springframework.stereotype.Component;

/**
 * Created by ellie on 17. 2. 20..
 */

public class BlankDisc implements CompactDisc {
    private String title;
    private String artist;

    public BlankDisc(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public void play() {
        System.out.println("Playing " + title + " by " + artist);
    }
}

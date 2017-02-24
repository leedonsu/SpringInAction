package com.kakao.module.chapter2;

import java.util.List;

/**
 * Created by ellie on 17. 2. 20..
 */

public class BlankDisc2 implements CompactDisc {
    private String title;
    private String artist;
    private List<String> tracks;

    public BlankDisc2(String title, String artist, List<String> tracks) {
        this.title = title;
        this.artist = artist;
        this.tracks = tracks;
    }

    public void play() {
        System.out.println("Playing " + title + " by " + artist);
        for (String track : tracks) {
            System.out.println("-Track : " + track);
        }
    }
}

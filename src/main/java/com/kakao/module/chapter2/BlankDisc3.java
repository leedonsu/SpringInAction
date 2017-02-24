package com.kakao.module.chapter2;

import java.util.List;

/**
 * Created by ellie on 17. 2. 20..
 */

public class BlankDisc3 implements CompactDisc {
    private String title;
    private String artist;
    private List<String> tracks;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTracks(List<String> tracks) {
        this.tracks = tracks;
    }

    public void play() {
        System.out.println("Playing " + title + " by " + artist);
        for (String track : tracks) {
            System.out.println("-Track : " + track);
        }
    }
}

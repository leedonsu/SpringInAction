package com.kakao.soundsystem;

import java.util.List;

/**
 * Created by jeongwoopark on 2017. 3. 30..
 */
public class CompactDisc {
    protected List<String> tracks;

    public void playTrack(int trackNumber){
        System.out.println(tracks.get(trackNumber-1));
    }

    public void setTracks(List<String> tracks) {
        this.tracks = tracks;
    }
}

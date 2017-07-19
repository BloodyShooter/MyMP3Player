package org.gvozdetscky.logic;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

import java.util.Map;

/**
 * Created by Egor on 19.07.2017.
 */
public class Player implements BasicPlayerListener {

    private long duration;
    private int bytesLen;
    private String songName;

    @Override
    public void opened(Object o, Map map) {
        duration = (long) Math.round((Long) map.get("duration") / 1000000);
        bytesLen = Math.round((Integer) map.get("mp3.length.bytes"));

        songName = map.get("title") != null ? map.get("title").toString(): "нет";
    }

    @Override
    public void progress(int i, long l, byte[] bytes, Map map) {

    }

    @Override
    public void stateUpdated(BasicPlayerEvent basicPlayerEvent) {

    }

    @Override
    public void setController(BasicController basicController) {

    }
}

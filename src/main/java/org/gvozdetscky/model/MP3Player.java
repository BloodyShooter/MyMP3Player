package org.gvozdetscky.model;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import java.io.File;

/**
 * Created by Egor on 19.07.2017.
 *
 * Это есть наш плеер, который предназначен для управления воспроизведением музыки.
 */
public class MP3Player {

    private BasicPlayer player = new BasicPlayer();

    private String currentFileName;
    private double currentVolumeValue;

    public void play(String fileName) {
        try {
            if (currentFileName != null && currentFileName.equals(fileName) && player.getStatus() == BasicPlayer.PAUSED) {
                player.resume();
                return;
            }

            currentFileName = fileName;
            player.open(new File(fileName));
            player.play();
            player.setGain(currentVolumeValue);

        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            player.stop();
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        try {
            player.pause();
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }

    public void setVolume(double currentValue, double maximumValue) {
        try {
            this.currentVolumeValue = currentValue;

            if (currentValue == 0) {
                player.setGain(0);
            } else {
                player.setGain(calcValue(currentValue, maximumValue));
            }
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }

    private double calcValue(double currentValue, double maximumValue) {
        currentVolumeValue = currentValue / maximumValue;
        return currentVolumeValue;
    }
}

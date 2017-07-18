package org.gvozdetscky.model;

import java.io.Serializable;

/**
 * Created by Egor on 18.07.2017.
 */
public class MP3 implements Serializable {
    private String name;
    private String path;

    public MP3(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "MP3{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}

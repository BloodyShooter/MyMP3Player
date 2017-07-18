package org.gvozdetscky.model;

import java.io.Serializable;

/**
 * Created by Egor on 18.07.2017.
 */
public class MP3 implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MP3 mp3 = (MP3) o;

        if (name != null ? !name.equals(mp3.name) : mp3.name != null) return false;
        return path != null ? path.equals(mp3.path) : mp3.path == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }
}

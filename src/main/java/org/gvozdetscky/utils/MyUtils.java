package org.gvozdetscky.utils;

import javafx.collections.ObservableList;
import org.gvozdetscky.model.MP3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Egor on 18.07.2017.
 */
public class MyUtils {

    public static String getStringNameAndPathFile() {
        return null;
    }

    public static void serialize(ObservableList<MP3> items, String fileName) {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            List<MP3> saveItems = new ArrayList<>();
            saveItems.addAll(items);
            oos.writeObject(saveItems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<MP3> deserialize(String fileName) {
        List<MP3> loadItems = new ArrayList<>();

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            loadItems = (ArrayList<MP3>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return loadItems;
    }
}

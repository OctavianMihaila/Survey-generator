package com.example.project;

import org.json.simple.JSONObject;

import java.io.*;
import java.io.FileWriter;
import java.io.IOException;

public class JSONWriteRead {
    /**
     * Creating a file with a given filename if that file does
     * not exist and writing a JSONObject in it.
     * @param obj JSONObject to be written in file
     * @param filename
     * @return returns true if a file with the given filename already exists and false otherwise.
     */
    public static boolean WriteJSON(JSONObject obj, String filename) {
        String filePath = "src/Database/" + filename + ".json";

        File FilenameToCheck = new File(filePath);
        if (FilenameToCheck.exists()) {
            return true;
        }

        try (FileWriter file = new FileWriter("src/Database/" + filename + ".json")) {
            file.write(obj.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}

package com.example.project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.image.BufferedImageFilter;
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

        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

        // Writing to a json file
        try (FileWriter file = new FileWriter("src/Database/" + filename + ".json")) {
            file.write(gson.toJson(JsonParser.parseString(obj.toJSONString())));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Creating a file with a given filename if that file does
     * not exits. Otherwise append a new question to the existing file.
     * @param obj
     * @param filename
     * @return
     */
    public static void WriteWithAppend(JSONObject obj, String filename) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        String filePath = "src/Database/" + filename + ".json";
        File FilenameToCheck = new File(filePath);
        JSONParser jsonParser = new JSONParser();
        JSONArray objArray = null;

        if (FilenameToCheck.exists()) {
            // Appending new object to existing json file
            try (FileReader reader = new FileReader(filePath)) {
                Object object = jsonParser.parse(reader);
                objArray = (JSONArray) object;
                objArray.add(obj);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            objArray = new JSONArray();
            objArray.add(obj);
        }

        // Writing to a json file.
        try (FileWriter file = new FileWriter("src/Database/" + filename + ".json")) {
            file.write(gson.toJson(JsonParser.parseString(objArray.toJSONString())));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return;
    }
}

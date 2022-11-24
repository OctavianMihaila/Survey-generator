package com.example.project;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static Boolean Authentication(String username, String password) {

        String filePath = "src/Database/" + username + ".json";
        File FilenameToCheck = new File(filePath);
        JSONParser jsonParser = new JSONParser();
        JSONObject user = null;

        if (Files.exists(Paths.get(filePath))) {
            try (FileReader reader = new FileReader(filePath)) {
                Object object = jsonParser.parse(reader);
                user = (JSONObject) object;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (user != null && user.get("Password").equals(password)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}

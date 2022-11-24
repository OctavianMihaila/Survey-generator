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
import java.util.List;
import java.util.Map;

public class Question {

    private int id;
    private String text;
    private String type;
    private Map<String, Boolean> answers;

    public int getId() {
        return id;
    }

    public Question(int id, String text, String type, Map<String, Boolean> answers) {
        this.id = id;
        this.text = text;
        this.type = type;
        this.answers = answers;
    }

    public static Boolean CheckAlreadyExists(String text) {
        String filePath = "src/Database/Questions.json";
        JSONParser jsonParser = new JSONParser();
        JSONArray objArray = null;
        File FilenameToCheck = new File(filePath);
        if (!Files.exists(Paths.get(filePath))) {
            return false;
        }

        try (FileReader reader = new FileReader(filePath)) {
            Object object = jsonParser.parse(reader);
            objArray = (JSONArray) object;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < objArray.size(); i++) {
            if (((JSONObject)objArray.get(i)).get("text").equals(text)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checking if an an id is used in a list of questions.
     * @param questions list of Question objects
     * @param id
     * @return
     */
    public Boolean CheckIdExists(List<Question> questions, Integer id) {
        for (Question q: questions) {
            if (q.getId() == id) {
                return true;
            }
        }

        return false;
    }
}

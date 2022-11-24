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
import java.util.ArrayList;

public class Quiz {
    private int id;
    private String name;
    private ArrayList<Integer> questionsIDs;

    public static int countIDs = 1; // Used to find the quiz that has to be deleted

    public Quiz(int id, String name, ArrayList<Integer> questionsIDs) {
        this.id = id;
        this.name = name;
        this.questionsIDs = questionsIDs;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Boolean CheckAlreadyExists(String text) {
        String filePath = "src/Database/Quizes.json";
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
        if (objArray == null) {
            return false;
        }
        for (int i = 0; i < objArray.size(); i++) {
            if (((JSONObject)objArray.get(i)).get("quizName").equals(text)) {
                return true;
            }
        }

        return false;
    }

    public static Boolean DeleteQuiz(String ID) {
        String filePath = "src/Database/Quizes.json";
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
        if (objArray == null) {
            return false;
        }

        countIDs = 1;
        // Mapping from JSON to Quiz objects and deleting a quiz with a specific ID.
        for (int i = 0; i < objArray.size(); i++) {
            JSONObject obj = (JSONObject)objArray.get(i);
            Quiz quiz = new Quiz(countIDs, (String)obj.get("quizName"), (ArrayList<Integer>)obj.get("questionsIDs"));
            countIDs++;

            if (quiz.getId() == (ID.charAt(ID.lastIndexOf("'") - 1) - 48)) {
                objArray.remove(obj);
                JSONWriteRead.WriteWithAppend(null, "Quizes", objArray);
                return true;
            }
        }

        return false;
    }
}

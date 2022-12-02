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

    private Boolean is_completed;

    private String quizCreator;

    static final int NUMBER_OFFSET = 48;

    public static int countIDs = 1; // Used to find the quiz that has to be deleted

    public Quiz(int id, String name, ArrayList<Integer> questionsIDs,
                Boolean is_completed, String quizCreator) {
        this.id = id;
        this.name = name;
        this.questionsIDs = questionsIDs;
        this.is_completed = is_completed;
        this.quizCreator = quizCreator;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getQuestionsIDs() {
        return questionsIDs;
    }

    public Boolean getIs_completed() {
        return is_completed;
    }

    public String getQuizCreator() {
        return quizCreator;
    }

    /**
     * Checks if a quiz with a specific text already exists.
     * @param text
     * @return
     */
    public static Boolean CheckAlreadyExists(String text) {
        String filePath = "src/Database/Quizzes.json";
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

    /**
     * Mapping from JSON to Quiz objects and deleting a quiz with a specific ID.
     * @param objArray
     * @param ID
     * @return
     */
    public static Boolean DeleteFromJSON(JSONArray objArray, String ID) {
        countIDs = 1;
        for (int i = 0; i < objArray.size(); i++) {
            JSONObject obj = (JSONObject)objArray.get(i);
            Quiz quiz = new Quiz(countIDs, (String)obj.get("quizName"),
                    (ArrayList<Integer>)obj.get("questionsIDs"),
                    Boolean.parseBoolean((String)obj.get("is_completed")),
                    (String)obj.get("quizCreator"));
            countIDs++;

            if (quiz.getId() == (ID.charAt(ID.lastIndexOf("'") - 1) - NUMBER_OFFSET)) {
                objArray.remove(obj);
                JSONWriteRead.WriteWithAppend(null, "Quizzes", objArray);
                return true;
            }
        }

        return false;
    }

    /**
     * Deletes a quiz from Quizzes.json
     * @param ID
     * @return
     */
    public static Boolean DeleteQuiz(String ID) {
        String filePath = "src/Database/Quizzes.json";
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

        // Mapping from JSON to Quiz objects and deleting a quiz with a specific ID.
        if (Quiz.DeleteFromJSON(objArray, ID)) {
            return true;
        } else {
            return false;
        }
    }
}

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

public class User {
    private String username;
    private String password;

    private ArrayList<String> completedQuizes;

    private Float grade;

    // Stores the number of answered questions for the current quiz.
    private Integer nrQuestionsAnswered;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Float getGrade() {
        return grade;
    }
    public Integer getNrQuestionsAnswered() {
        return nrQuestionsAnswered;
    }

    public void IncNrQuestionsAnswered() {
        this.nrQuestionsAnswered++;
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

    /**
     * Add a quiz name to the completed quizes arraylist.
     * @param quizName
     */
    public void AddNewCompletedQuiz(String quizName) {
        this.completedQuizes.add(quizName);
    }

    /**
     * Check if a quiz has already been completed by a user
     * @param quizName
     * @return
     */
    public Boolean CheckAlreadyCompleted(String quizName) {
        if (this.completedQuizes.contains(quizName)) {
            return true;
        }

        return false;
    }

}

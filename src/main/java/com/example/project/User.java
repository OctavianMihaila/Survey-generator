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
import java.util.Iterator;
import java.util.Map;

public class User {
    private String username;
    private String password;

    private Map<String, WrapperQuizResult> completedQuizes;

    private Float grade;

    // Stores the number of answered questions for the current quiz.
    private Integer nrQuestionsAnswered;

    public User(String username, String password, Map<String, WrapperQuizResult> completedQuizes) {
        this.username = username;
        this.password = password;
        this.completedQuizes = completedQuizes;
    }

    public Map<String, WrapperQuizResult> getCompletedQuizes() {
        return completedQuizes;
    }

    public Integer getCompletedQuizesLength() {
        return this.completedQuizes.size();
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
    public void AddNewCompletedQuiz(String quizName, WrapperQuizResult quizResult) {
        this.completedQuizes.put(quizName, quizResult);
    }

    /**
     * Check if a quiz has already been completed by a user
     * @param quizName
     * @return
     */
    public Boolean CheckAlreadyCompleted(String quizName) {
        if (this.completedQuizes.containsKey(quizName)) {
            return true;
        }

        return false;
    }

    /**
     * Converting a user to a JSONObject.
     * @return
     */
    public JSONObject ConvertUserToJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("User", this.username);
        obj.put("Password", this.password);

        Iterator<Map.Entry<String, WrapperQuizResult>> iterator =
                this.completedQuizes.entrySet().iterator();

        JSONArray results = new JSONArray();
        while (iterator.hasNext()) {
            Map.Entry<String, WrapperQuizResult> entry = iterator.next();
            JSONObject quizResult = new JSONObject();
            quizResult.put("QuizId", entry.getValue().getQuizId());
            quizResult.put("Score", entry.getValue().getScore());
            quizResult.put("IndexInList", entry.getValue().getIndexInList());
            quizResult.put("QuizName", entry.getKey());
            results.add(quizResult);
        }

        obj.put("WrapperQuizResult", results);
        return obj;
    }

}

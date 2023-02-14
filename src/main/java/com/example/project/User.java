package com.example.project;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

    private Map<String, WrapperQuizResult> completedQuizzes;

    public User(String username, String password, Map<String, WrapperQuizResult> completedQuizzes) {
        this.username = username;
        this.password = password;
        this.completedQuizzes = completedQuizzes;
    }

    public Map<String, WrapperQuizResult> getCompletedQuizzes() {
        return completedQuizzes;
    }

    public Integer getCompletedQuizzesLength() {
        return this.completedQuizzes.size();
    }

    public static Boolean authentication(String username, String password) {

        String filePath = "src/Database/" + username + ".json";
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
     * Add a quiz name to the completed quizzes arraylist.
     * @param quizName
     */
    public void addNewCompletedQuiz(String quizName, WrapperQuizResult quizResult) {
        this.completedQuizzes.put(quizName, quizResult);
    }

    /**
     * Check if a quiz has already been completed by a user
     * @param quizName
     * @return
     */
    public Boolean checkAlreadyCompleted(String quizName) {
        if (this.completedQuizzes.containsKey(quizName)) {
            return true;
        }

        return false;
    }

    /**
     * Converting a user to a JSONObject.
     * @return
     */
    public JSONObject convertUserToJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("User", this.username);
        obj.put("Password", this.password);

        Iterator<Map.Entry<String, WrapperQuizResult>> iterator =
                this.completedQuizzes.entrySet().iterator();

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

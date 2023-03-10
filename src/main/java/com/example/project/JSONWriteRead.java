package com.example.project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONWriteRead {
    /**
     * Creating a file with a given filename if that file does
     * not exist and writing a JSONObject in it.
     *
     * @param obj      JSONObject to be written in file
     * @param filename
     * @return returns true if a file with the given filename already exists and false otherwise.
     */
    public static boolean writeJSON(JSONObject obj, String filename) {
        String filePath = "src/Database/" + filename + ".json";

        File filenameToCheck = new File(filePath);
        if (filenameToCheck.exists()) {
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
     * not exist. Otherwise append a new question to the existing file.
     *
     * @param obj
     * @param filename
     * @return
     */
    public static void writeWithAppend(JSONObject obj, String filename, JSONArray objArr) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        String filePath = "src/Database/" + filename + ".json";
        File filenameToCheck = new File(filePath);
        JSONParser jsonParser = new JSONParser();
        JSONArray objArray = null;

        if (filenameToCheck.exists()) {
            // Appending new object to existing json file
            try (FileReader reader = new FileReader(filePath)) {
                Object object = jsonParser.parse(reader);

                if (objArr == null) {
                    objArray = (JSONArray) object;
                    objArray.add(obj);
                } else {
                    objArray = objArr;

                }

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

    }

    /**
     * Mapping questions/quizzes from JSON into Question/Quiz objects.
     *
     * @param filename specifies what type of objects to create.
     * @param <T>
     * @return List of Quizzes/Questions.
     */
    public static <T> List<T> MappingJSON(String filename) {
        String filePath = "src/Database/" + filename + ".json";
        File filenameToCheck = new File(filePath);
        JSONParser jsonParser = new JSONParser();
        JSONArray objArray = null;
        int counterIDs = 1;
        List<T> items = new ArrayList<T>();

        if (filenameToCheck.exists()) {
            try (FileReader reader = new FileReader(filePath)) {
                Object object = jsonParser.parse(reader);
                objArray = (JSONArray) object;

            } catch (FileNotFoundException e) {
                System.out.println("File does not exist");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (objArray == null) {
                System.out.println("Empty file(Mapping JSON)");
                return null;
            }
        } else {
            return null;
        }

        if (filename.equals("Questions")) {
            for (int i = 0; i < objArray.size(); i++) {
                JSONObject obj = (JSONObject) objArray.get(i);
                Question question = new Question(counterIDs, (String) obj.get("text"),
                        (String) obj.get("type"), (Map<String, Boolean>) obj.get("Answers"));
                items.add((T) question);
                counterIDs++;
            }

            return items;
        } else if (filename.equals("Quizzes")) {
            for (int i = 0; i < objArray.size(); i++) {
                JSONObject obj = (JSONObject) objArray.get(i);
                Quiz quiz = new Quiz(counterIDs, (String) obj.get("quizName"),
                        (ArrayList<Integer>) obj.get("questionsIDs"),
                        Boolean.parseBoolean((String) obj.get("is_completed")),
                        (String) obj.get("quizCreator"));
                items.add((T) quiz);
                counterIDs++;
            }

            return items;
        } else {
            System.out.println("Invalid filename");
            return null;
        }
    }

    /**
     * Mapping a json file that represents a user into a User object.
     *
     * @param filename
     * @return
     */
    public static User readUser(String filename) {
        String filePath = "src/Database/" + filename + ".json";
        File filenameToCheck = new File(filePath);
        JSONParser jsonParser = new JSONParser();
        JSONObject obj = null;

        if (filenameToCheck.exists()) {
            try (FileReader reader = new FileReader(filePath)) {
                Object object = jsonParser.parse(reader);
                obj = (JSONObject) object;

            } catch (FileNotFoundException e) {
                System.out.println("File does not exist");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (obj == null) {
            System.out.println("User does not exist");
            return null;
        }

        Map<String, WrapperQuizResult> completedQuizzes = new HashMap<>();
        JSONArray results = (JSONArray) obj.get("WrapperQuizResult");
        if (results != null) {
            for (int i = 0; i < results.size(); i++) {
                JSONObject o = ((JSONObject) results.get(i));
                WrapperQuizResult quizResult = new WrapperQuizResult((Number) o.get("QuizId"),
                        (Number) o.get("Score"), (Number) o.get("IndexInList"));
                completedQuizzes.put((String) o.get("QuizName"), quizResult);
            }
        }

        return new User((String) obj.get("User"), (String) obj.get("Password"), completedQuizzes);
    }
}

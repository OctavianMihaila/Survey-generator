package com.example.project;

import java.util.*;
import org.json.simple.JSONObject;
public class Parser {
    public static JSONObject ParseCreateUser(String[] args) {
        JSONObject confirmation = new JSONObject();

        // Checking if username and password are provided.
        if (args.length < 2) {
            confirmation.put("message", "'status' : 'error', 'message' : 'Please provide username'");
            return confirmation;
        }
        if (args.length < 3) {
            confirmation.put("message", "'status' : 'error', 'message' : 'Please provide password'");
            return confirmation;
        }
        String username = args[1];
        String password = args[2];
        // Getting rid of the parameters (-u or -p).
        username = username.substring(username.lastIndexOf(" ") + 1);
        password = password.substring(password.lastIndexOf(" ") + 1);

        JSONObject user = new JSONObject();
        user.put("User", username);
        user.put("Password", password);

        if (JSONWriteRead.WriteJSON(user, username) == true) {
            confirmation.put("message", "'status' : 'error', 'message' : 'User already exists'");
        } else {
            confirmation.put("message", "'status' : 'ok', 'message' : 'User created successfully'");
        }
        return confirmation;
    }

    public static JSONObject ParseCreateQuestion(String[] args) {
        JSONObject confirmation = new JSONObject();

        // Checking if username and password are provided.
        if (args.length < 3) {
            confirmation.put("message", "'status':'error','message':'You need to be authenticated'");
            return confirmation;
        }
        if (args.length < 4) {
            confirmation.put("message", "'status':'error','message':'No question text provided'");
            return confirmation;
        }
        if (args.length < 5) {
            confirmation.put("message", "'status':'error','message':'No answer provided'");
            return confirmation;
        }
        if (args.length < 7) {
            confirmation.put("message", "'status':'error','message':'Only one answer provided'");
            return confirmation;
        }
        if (args.length > 14) {
            confirmation.put("message", "'status':'error','message':'More than 5 answers were submitted'");
            return confirmation;
        }
        String username = args[1].substring(args[1].lastIndexOf(" ") + 1);
        String password = args[2].substring(args[2].lastIndexOf(" ") + 1);
        String text = args[3];
        String type = args[4];
        // TO DO: Check authentification (do a method in USER class) check if username exists or if pass is wrong
        // TO DO: Check if question already exists (do a method in question class), check by text
        //
        Integer count = 0; // Counts how many true answer are in a single-answer question.
        Map<String, Boolean> answers = new HashMap<String, Boolean>();
        for (int i = 5; i < args.length; i += 2) {
            if (answers.containsKey(args[i])) { // Checking if same answer is provided multiple times.
                confirmation.put("message", "'status':'error','message':" +
                        "'Same answer provided more than once'");
                return confirmation;
            }

            if (!args[i].contains("'")) { // Checking if answer i has no answer description
                confirmation.put("message", "'status':'error','message':" +
                        "'Answer i has no answer description'");
                return confirmation;
            }
            if (!args[i + 1].contains("'")) { // Checking if answer i has no answer correct flag.
                confirmation.put("message", "'status':'error','message':" +
                        "'Answer i has no answer correct flag'");
                return confirmation;
            }

            int indexValue = args[i + 1].lastIndexOf("'");
            int val = args[i + 1].charAt(indexValue - 1) - 48;
            if (type.equals("'single'") && val == 1) {
                count++;
                if (count == 2) { // Checking if a single-answer question has more the one correct answer.
                    confirmation.put("message", "'status':'error','message':" +
                            "'Single correct answer question has more than one correct answer'");
                    return confirmation;
                }
            }
            answers.put(args[i], Integer.toString(val).equals("1") ? true : false);
        }

        JSONObject question = new JSONObject();
        question.put("text", text);
        question.put("type", type);
        question.put("Answers", answers);

        JSONWriteRead.WriteJSON(question, username);


        confirmation.put("message", "'status' : 'ok', 'message' : 'Question added successfully'");

        return confirmation;
    }
}

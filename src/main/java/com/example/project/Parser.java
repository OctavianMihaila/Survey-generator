package com.example.project;

import java.util.*;
import org.json.simple.JSONObject;
public class Parser {
    /**
     * Doing the checks to see if the input is valid.
     * In case of a valid input creates a json file that contains user's details.
     * @param args
     * @return JSONObject that contains the text confirmation.
     */
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

    /**
     * Doing the checks to see if the input is valid.
     * If the input is valid, then the question is appended to the existing Questions.json.
     * @param args
     * @return JSONObject that contains the text confirmation.
     */
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
        if (args.length < 6) {
            confirmation.put("message", "'status':'error','message':'No answer provided'");
            return confirmation;
        }
        if (args.length < 8) {
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
        if (!text.contains("-text")) {
            confirmation.put("message", "'status':'error','message':'No question text provided'");
            return confirmation;
        }
        if (!type.contains("-type")) {
            confirmation.put("message", "'status':'error','message':'No question type provided'");
            return confirmation;
        }
        // TO DO: Check authentification (do a method in USER class) check if username exists or if pass is wrong
        // TO DO: Check if question already exists (do a method in question class), check by text
        //
        Integer count = 0; // Counts how many true answer are in a single-answer question.
        Map<String, Boolean> answers = new HashMap<String, Boolean>();
        for (int i = 5; i < args.length; i += 2) {
            // TO DO: Method to check if a answer text is present multiple times. Iterate and check map.
            if (answers.containsKey(args[i])) { // Checking if same answer is provided multiple times.

                confirmation.put("message", "'status':'error','message':" +
                        "'Same answer provided more than once'");
                return confirmation;
            }

            if (args[i].contains("is-correct")) { // Checking if answer i has no answer description
                confirmation.put("message", "'status':'error','message':" +
                        "'Answer " + args[i].charAt(args[i].lastIndexOf("i") - 2) + " has no answer description'");
                return confirmation;
            }
            if (!args[i + 1].contains("correct")) { // Checking if answer i has no answer correct flag.
                confirmation.put("message", "'status':'error','message':" +
                        "'Answer " + args[i].charAt(args[i].lastIndexOf("-") + 1) +  " has no answer correct flag'");
                return confirmation;
            }

            int indexValue = args[i + 1].lastIndexOf("'");
            int val = args[i + 1].charAt(indexValue - 1) - 48;
            if (type.equals("-type 'single'") && val == 1) {
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

        JSONWriteRead.WriteWithAppend(question, "Questions");

        confirmation.put("message", "'status' : 'ok', 'message' : 'Question added successfully'");

        return confirmation;
    }

    /**
     * Doing the checks to see if the input is valid.
     * @param args
     * @return JSONObject that contains the text confirmation.
     */
    public static JSONObject GetQuestionIdByText(String[] args) {
        JSONObject confirmation = new JSONObject();

        // Checking if username and password are provided.
        if (args.length < 3) {
            confirmation.put("message", "'status':'error','message':'You need to be authenticated'");
            return confirmation;
        }
        // TO DO: Check for authentification validty.
        // TO DO: Check if question exists. (text) -> not found
        // TO DO: Return id and put in confirmation

        confirmation.put("message", "'status' : 'ok', 'message' : 'ID TO SET'");
        return confirmation;
    }

    /**
     * Doing the checks to see if the input is valid.
     * In case of a valid input, returns all questions.
     * @param args
     * @return JSONObject that contains the text confirmation.
     */
    public static JSONObject GetAllQuestions(String[] args) {
        JSONObject confirmation = new JSONObject();

        // Checking if username and password are provided.
        if (args.length < 3) {
            confirmation.put("message", "'status':'error','message':'You need to be authenticated'");
            return confirmation;
        }

        // TO DO: Check for authentification validty.
        // TO DO: Return all the questions and put in confirmation

        confirmation.put("message", "'status' : 'ok', 'message' : 'TO PUT ALL QUESTIONS'");
        return confirmation;
    }

    /**
     * Doing the checks to see if the input is valid.
     * In case of a valid input, then the quiz is appended to the existing Quizes.json
     * @param args
     * @return JSONObject that contains the text confirmation.
     */
    public static JSONObject CreateQuiz(String[] args) {
        JSONObject confirmation = new JSONObject();

        // Checking if username and password are provided.
        if (args.length < 3) {
            confirmation.put("message", "'status':'error','message':'You need to be authenticated'");
            return confirmation;
        }
        if (args.length > 14) { // Checking if quiz has more than 10 questions.
            confirmation.put("message", "'status':'error','message':'Quizz has more than 10 questions'");
            return confirmation;
        }

        // TO DO: Checking authentification (username, password). if not good it has to RETURN.
        // TO DO: Check if quiz name already exists
        // TO DO: Check question id does not exists

        String quizName = args[3];
        ArrayList<Integer> questionsIDs = new ArrayList<>();
        for (int i = 4; i < args.length; i++) {
            int indexValue = args[i].lastIndexOf("'");
            Integer val = args[i].charAt(indexValue - 1) - 48;
            questionsIDs.add(val);
        }

        JSONObject quiz = new JSONObject();
        quiz.put("quizName", quizName);
        quiz.put("questionsIDs", questionsIDs);

        JSONWriteRead.WriteWithAppend(quiz, "Quizes");
        confirmation.put("message", "'status' : 'ok', 'message' : 'Quizz added successfully'");

        return confirmation;
    }

    /**
     * Doing the checks to see if the input is valid.
     * In case of a valid input, returns the quiz with the specified id
     * @param args
     * @return JSONObject that contains the text confirmation.
     */
    public static JSONObject GetQuizIdByName(String[] args) {
        JSONObject confirmation = new JSONObject();

        // Checking if username and password are provided.
        if (args.length < 3) {
            confirmation.put("message", "'status':'error','message':'You need to be authenticated'");
            return confirmation;
        }

        // TO DO: Check authentification
        // TO DO: Check if quiz already exists
        // TO DO: Search for quiz

        confirmation.put("message", "'status':'ok','message': 'TO LIST QUIZ ID'");
        return confirmation;

    }

    /**
     * Doing the checks to see if the input is valid.
     * In case of a valid input, returns all the quizes.
     * @param args
     * @return JSONObject that contains the text confirmation.
     */
    public static JSONObject GetAllQuizes(String[] args) {
        JSONObject confirmation = new JSONObject();

        // Checking if username and password are provided.
        if (args.length < 3) {
            confirmation.put("message", "'status':'error','message':'You need to be authenticated'");
            return confirmation;
        }

        // TO DO: Check authentification
        // TO DO: Check if quiz does not exist (FOR BONUS, also do that for questions)
        // TO DO: Return all quizes

        confirmation.put("message", "'status':'ok','message': 'TO PUT QUIZEZ''");
        return confirmation;

    }

    /**
     * Doing the checks to see if the input is valid.
     * In case of a valid input, returns the quiz detaild for the quiz with the specificed id.
     * @param args
     * @return JSONObject that contains the text confirmation.
     */
    public static JSONObject GetQuizDetailsByID(String[] args) {
        JSONObject confirmation = new JSONObject();

        // Checking if username and password are provided.
        if (args.length < 3) {
            confirmation.put("message", "'status':'error','message':'You need to be authenticated'");
            return confirmation;
        }

        // TO DO: Check authentification
        // TO DO: Check if quiz does not exist
        // TO DO: Return the quiz details (without the value of truth for the questions).

        confirmation.put("message", "'status':'ok','message': 'TO PUT QUIZEZ''");
        return confirmation;

    }

    /**
     * Doing the checks to see if the input is valid.
     * In case of a valid inout, returns the answers for the specified quiz.
     * @param args
     * @return JSONObject that contains the text confirmation.
     */
    public static JSONObject GetQuizAnswers(String[] args) {
        JSONObject confirmation = new JSONObject();

        // Checking if username and password are provided.
        if (args.length < 3) {
            confirmation.put("message", "'status':'error','message':'You need to be authenticated'");
            return confirmation;
        }

        // TO DO: Check authentification
        // TO DO: Check if quiz does not exist
        // TO DO: Return the quiz details (without the value of truth for the questions).

        confirmation.put("message", "'status':'ok','message': 'TO PUT QUIZEZ''");
        return confirmation;

    }

    /**
     * Doing the checks to see if the input is valid.
     * In case of a valid input, submits a quiz and returns the result in confirmation.
     * @param args
     * @return JSONObject that contains the text confirmation (with result).
     */
    public static JSONObject SubmitAnswers(String[] args) {
        JSONObject confirmation = new JSONObject();

        // Checking if username and password are provided.
        if (args.length < 3) {
            confirmation.put("message", "'status':'error','message':'You need to be authenticated'");
            return confirmation;
        }

        // TO DO: Check authentification
        // TO DO: Check if quiz does not exist
        // TO DO: Raspunsul pentru intrebarea I nu are identifcator de raspuns al intrebarii asociate....
        // TO DO: Case in which user has allready completed the quiz
        // TO DO: Case in which user is the creator of the quiz.
        // TO DO: Calculat points and PRINT

        confirmation.put("message", "'status':'ok','message': 'PRINT THE RESULT(points)'");
        return confirmation;
    }

    /**
     * Doing the checks to see if the input is valid.
     * In case of a valid input, delete a specified quiz.
     * @param args
     * @return JSONObject that contains the text confirmation.
     */
    public static JSONObject DeleteQuiz(String[] args) {
        JSONObject confirmation = new JSONObject();

        // Checking if username and password are provided.
        if (args.length < 3) {
            confirmation.put("message", "'status':'error','message':'You need to be authenticated'");
            return confirmation;
        }
        if (args.length < 4) {
            confirmation.put("message", "'status':'error','message':'No quizz identifier was provided'");
            return confirmation;
        }

        // TO DO: Check authentification
        // TO DO: Check if quiz does not exist
        // TO DO: DELETE THE QUIZ FROM FILE AND DELETE THE OBJECT


        confirmation.put("message", "'status':'ok','message': 'Quizz deleted successfully'");
        return confirmation;

    }

    /**
     * Doing the checks to see if the input is valid.
     * In case of a valid input, return the all the solutions to the completed quizes of a user.
     * @param args
     * @return JSONObject that contains the text confirmation.
     */
    public static JSONObject GetMySolutions(String[] args) {
        JSONObject confirmation = new JSONObject();

        // Checking if username and password are provided.
        if (args.length < 3) {
            confirmation.put("message", "'status':'error','message':'You need to be authenticated'");
            return confirmation;
        }

        // TO DO: Check authentification
        // TO DO: Return quiz-name + score + index in list. (Search in user's submits or smth)


        confirmation.put("message", "'status':'ok','message': 'TO DO: quiz-name + score + index'");
        return confirmation;
    }
}



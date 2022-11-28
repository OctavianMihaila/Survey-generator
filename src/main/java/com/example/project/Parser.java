package com.example.project;

import java.awt.desktop.QuitEvent;
import java.io.File;
import java.lang.reflect.Array;
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
        user.put("CompletedQuizes", new HashMap<String, WrapperQuizResult>());

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

        String username = args[1].substring(args[1].lastIndexOf(" ") + 1);
        String password = args[2].substring(args[2].lastIndexOf(" ") + 1);
        if (args.length < 4) {
            if (User.Authentication(username, password) == false) { // Check if credentials are valid.
                confirmation.put("message", "'status':'error','message':'Login failed'");
                return confirmation;
            }
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

        String text = args[3];
        String type = args[4];

        if (!text.contains("-text")) {
            if (User.Authentication(username, password) == false) { // Check if credentials are valid.
                confirmation.put("message", "'status':'error','message':'Login failed'");
                return confirmation;
            }

            confirmation.put("message", "'status':'error','message':'No question text provided'");
            return confirmation;
        }
        if (!type.contains("-type")) {
            confirmation.put("message", "'status':'error','message':'No question type provided'");
            return confirmation;
        }
        Integer count = 0; // Counts how many true answer are in a single-answer question.
        Map<String, Boolean> answers = new HashMap<String, Boolean>();
        for (int i = 5; i < args.length; i += 2) {
            // TO DO: Method to check if a answer text is present multiple times. Iterate and check map.
            if (Question.CheckAnswerExists(answers, args[i])) { // Checking if same answer is provided multiple times.
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

        if (User.Authentication(username, password) == false) { // Check if credentials are valid.
            confirmation.put("message", "'status':'error','message':'Login failed'");
            return confirmation;
        }

        if (Question.CheckAlreadyExists(text) == true) { // Check if question is present in Question.json.
            confirmation.put("message", "'status':'error','message':'Question already exists'");
            return confirmation;
        }

        JSONWriteRead.WriteWithAppend(question, "Questions", null);

        confirmation.put("message", "'status' : 'ok', 'message' : 'Question added successfully'");

        return confirmation;
    }

    /**
     * Doing the checks to see if the input is valid.
     * @param args
     * @return JSONObject that contains the text confirmation.
     */
    public static JSONObject ParseGetQuestionIdByText(String[] args) {
        JSONObject confirmation = new JSONObject();

        if (args.length < 3) { // Checking if username and password are provided.
            confirmation.put("message", "'status':'error','message':'You need to be authenticated'");
            return confirmation;
        }
        String username = args[1].substring(args[1].lastIndexOf(" ") + 1);
        String password = args[2].substring(args[2].lastIndexOf(" ") + 1);


        if (User.Authentication(username, password) == false) {
            confirmation.put("message", "'status':'error','message':'Login failed'");
            return confirmation;
        }
        // TO DO: Check if question exists. (text) -> not found
        // TO DO: Return id and put in confirmation
        List<Question> questions = JSONWriteRead.MappingJSON("Questions");
        if (questions != null) {
            for (Question q: questions) {
                if (q.getText().equals(args[3])) {
                    confirmation.put("message", "'status' : 'ok', 'message' : '" + q.getId() + "'");
                    return confirmation;
                }
            }
        }
        confirmation.put("message", "'status' : 'error', 'message' : 'Question does not exist'");
        return confirmation;
    }

    /**
     * Doing the checks to see if the input is valid.
     * In case of a valid input, returns all questions.
     * @param args
     * @return JSONObject that contains the text confirmation.
     */
    public static JSONObject ParseGetAllQuestions(String[] args) {
        JSONObject confirmation = new JSONObject();

        // Checking if username and password are provided.
        if (args.length < 3) {
            confirmation.put("message", "'status':'error','message':'You need to be authenticated'");
            return confirmation;
        }
        String username = args[1].substring(args[1].lastIndexOf(" ") + 1);
        String password = args[2].substring(args[2].lastIndexOf(" ") + 1);

        if (User.Authentication(username, password) == false) {
            confirmation.put("message", "'status':'error','message':'Login failed'");
            return confirmation;
        }
        // Getting all the questions from the database.
        List<Question> questions = JSONWriteRead.MappingJSON("Questions");
        ArrayList<String> questionStrings = new ArrayList<>();
        for (Question q: questions) { // formatting to the required template.
            questionStrings.add("{" + "\"question_id\" : \"" +
                    q.getId() + "\", " + "\"question_name\" : " + "\"" +
                    q.getText().substring(7, q.getText().length() - 1) + "\"" + "}");
        }

        confirmation.put("message", "'status' : 'ok', 'message' : '" + questionStrings + "'");
        return confirmation;
    }

    /**
     * Doing the checks to see if the input is valid.
     * In case of a valid input, then the quiz is appended to the existing Quizes.json
     * @param args
     * @return JSONObject that contains the text confirmation.
     */
    public static JSONObject ParseCreateQuiz(String[] args) {
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
        String username = args[1].substring(args[1].lastIndexOf(" ") + 1);
        String password = args[2].substring(args[2].lastIndexOf(" ") + 1);

        if (User.Authentication(username, password) == false) {
            confirmation.put("message", "'status':'error','message':'Login failed'");
            return confirmation;
        }
        String text = args[3];
        if (Quiz.CheckAlreadyExists(text) == true) { // Checking if quiz already exists.
            confirmation.put("message", "'status':'error','message':'Quizz name already exists'");
            return confirmation;
        }

        // Getting all the questions from the database.
        List<Question> questions = JSONWriteRead.MappingJSON("Questions");
        Boolean found = false;
        int id = -1;
        for (int i = 4; i < args.length; i++) { // Checking if question id exists.
            id = args[i].charAt(args[i].lastIndexOf("'") - 1) - 48;
            if (questions != null && questions.get(0).CheckIdExists(questions, id)) {
                found = true;
            } else {
                found = false;
                break;
            }
        }

        if (found == false && id != -1) {
            confirmation.put("message", "'status':'error','message':'Question ID for question " + id + " does not exist'");
            return confirmation;
        }

        String quizName = args[3];
        ArrayList<Integer> questionsIDs = new ArrayList<>();
        for (int i = 4; i < args.length; i++) {
            int indexValue = args[i].lastIndexOf("'");
            Integer val = Integer.parseInt(String.valueOf(args[i].charAt(indexValue - 1)));
            questionsIDs.add(val);
        }

        JSONObject quiz = new JSONObject();
        quiz.put("questionsIDs", questionsIDs);
        quiz.put("is_completed", "False");
        quiz.put("quizName", quizName);
        quiz.put("quizCreator", username);



        JSONWriteRead.WriteWithAppend(quiz, "Quizes", null);
        confirmation.put("message", "'status' : 'ok', 'message' : 'Quizz added succesfully'");

        return confirmation;
    }

    /**
     * Doing the checks to see if the input is valid.
     * In case of a valid input, returns the quiz with the specified id
     * @param args
     * @return JSONObject that contains the text confirmation.
     */
    public static JSONObject ParseGetQuizIdByName(String[] args) {
        JSONObject confirmation = new JSONObject();


        if (args.length < 3) { // Checking if username and password are provided.
            confirmation.put("message", "'status':'error','message':'You need to be authenticated'");
            return confirmation;
        }
        String username = args[1].substring(args[1].lastIndexOf(" ") + 1);
        String password = args[2].substring(args[2].lastIndexOf(" ") + 1);

        if (User.Authentication(username, password) == false) {
            confirmation.put("message", "'status':'error','message':'Login failed'");
            return confirmation;
        }

        List<Quiz> quizes = JSONWriteRead.MappingJSON("Quizes");
        if (quizes == null) {
            confirmation.put("message", "'status':'error','message': 'Quizz does not exist'");
            return confirmation;
        }

        String text = args[3];
        // Searching for quiz with the specific name;
        for (Quiz q: quizes) {
            if (q.getName().equals(text)) {
                confirmation.put("message", "'status':'ok','message': '" + q.getId() + "'");
                return confirmation;
            }
        }

        confirmation.put("message", "'status':'error','message': 'Quizz does not exist'");
        return confirmation;

    }

    /**
     * Doing the checks to see if the input is valid.
     * In case of a valid input, returns all the quizes.
     * @param args
     * @return JSONObject that contains the text confirmation.
     */
    public static JSONObject ParseGetAllQuizes(String[] args) {
        JSONObject confirmation = new JSONObject();

        if (args.length < 3) { // Checking if username and password are provided.
            confirmation.put("message", "'status':'error','message':'You need to be authenticated'");
            return confirmation;
        }
        String username = args[1].substring(args[1].lastIndexOf(" ") + 1);
        String password = args[2].substring(args[2].lastIndexOf(" ") + 1);

        if (User.Authentication(username, password) == false) {
            confirmation.put("message", "'status':'error','message':'Login failed'");
            return confirmation;
        }

        // TO DO: Check if quiz does not exist (FOR BONUS, also do that for questions)

        // Getting all the quizes from the database.
        List<Quiz> quizes = JSONWriteRead.MappingJSON("Quizes");
        ArrayList<String> quizStrings = new ArrayList<>();
        for (Quiz q: quizes) { // formatting to the required template.
            quizStrings.add("{" + "\"quizz_id\" : \"" +
                    q.getId() + "\", " + "\"quizz_name\" : " + "\"" +
                    q.getName().substring(7, q.getName().length() - 1) + "\", " + "\"is_completed\" : " + "\""
                    + q.getIs_completed().toString().substring(0, 1).toUpperCase() +
                    q.getIs_completed().toString().substring(1) + "\"" + "}");
        }

        confirmation.put("message", "'status' : 'ok', 'message' : '" + quizStrings + "'");
        return confirmation;

    }

    /**
     * Doing the checks to see if the input is valid.
     * In case of a valid input, returns the quiz detaild for the quiz with the specified id.
     * @param args
     * @return JSONObject that contains the text confirmation.
     */
    public static JSONObject ParseGetQuizDetailsByID(String[] args) {
        JSONObject confirmation = new JSONObject();

        if (args.length < 3) { // Checking if username and password are provided.
            confirmation.put("message", "'status':'error','message':'You need to be authenticated'");
            return confirmation;
        }
        String username = args[1].substring(args[1].lastIndexOf(" ") + 1);
        String password = args[2].substring(args[2].lastIndexOf(" ") + 1);

        if (User.Authentication(username, password) == false) {
            confirmation.put("message", "'status':'error','message':'Login failed'");
            return confirmation;
        }
        Integer id =  args[3].charAt(args[3].lastIndexOf("'") - 1) - 48;
        List<Quiz> quizes = JSONWriteRead.MappingJSON("Quizes");
        for (Quiz q: quizes) {
            if (q.getId() == id) {
                // look for the questions contained in the quiz (by Question ID).
                List<Question> questions = JSONWriteRead.MappingJSON("Questions");
                if (questions == null) {
                    System.out.println("there are no questions");
                    return null;
                }

                ArrayList<String> questionStrings = new ArrayList<>();
                Integer idCount = 1; // Generate ids for answers;
                List<Integer> questionIDs = (List<Integer>) q.getQuestionsIDs();
                for (int i = 0; i < questionIDs.size(); i++) { // Generate the required template.
                    Number idToCheck = questionIDs.get(i);
                    Question question = questions.get(0).FindQuestionById(questions, idToCheck);

                    // Building the answers string for every question, according to the required template.
                    Map<String, Boolean> answers = question.getAnswers();
                    ArrayList<String> sortedAnswers = new ArrayList<>();
                    Iterator<Map.Entry<String, Boolean>> iterator = answers.entrySet().iterator();

                    while (iterator.hasNext()) { // Convert Map keys to arraylist.
                        Map.Entry<String, Boolean> entry = iterator.next();
                        sortedAnswers.add(entry.getKey());
                    }
                    Collections.reverse(sortedAnswers);

                    ArrayList<String> answersString = new ArrayList<>();

                    for (String answer: sortedAnswers) {
                        answersString.add("{\"answer_name\":\"" +
                                answer.substring(answer.lastIndexOf(" ") + 2,
                                        answer.lastIndexOf("'")) + "\", " +
                                "\"answer_id\":\"" + idCount + "\"}");
                        idCount++;
                    }

                    questionStrings.add("{\"question-name\":" + question.getText().substring(6).
                            replace("'", "\"") + ", \"question_index\":\""
                            + question.getId() + "\", \"question_type\":" +
                            question.getType().substring(6).replace("'", "\"")
                            + ", \"answers\":\"" + answersString +  "\"}");
                }

                confirmation.put("message", "'status':'ok','message':'" + questionStrings + "'");
                return confirmation;


            }
        }

        confirmation.put("message", "'status':'ok','message': 'TO PUT QUIZEZ''");
        return confirmation;

    }

    /**
     * Doing the checks to see if the input is valid.
     * In case of a valid inout, returns the answers for the specified quiz.
     * @param args
     * @return JSONObject that contains the text confirmation.
     */
    public static JSONObject ParseGetQuizAnswers(String[] args) {
        JSONObject confirmation = new JSONObject();

        if (args.length < 3) { // Checking if username and password are provided.
            confirmation.put("message", "'status':'error','message':'You need to be authenticated'");
            return confirmation;
        }
        String username = args[1].substring(args[1].lastIndexOf(" ") + 1);
        String password = args[2].substring(args[2].lastIndexOf(" ") + 1);

        if (User.Authentication(username, password) == false) {
            confirmation.put("message", "'status':'error','message':'Login failed'");
            return confirmation;
        }
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
    public static JSONObject ParseSubmitAnswers(String[] args) {
        JSONObject confirmation = new JSONObject();

        if (args.length < 3) { // Checking if username and password are provided.
            confirmation.put("message", "'status':'error','message':'You need to be authenticated'");
            return confirmation;
        }
        String username = args[1].substring(args[1].lastIndexOf(" ") + 1);
        String password = args[2].substring(args[2].lastIndexOf(" ") + 1);
        if (User.Authentication(username, password) == false) {
            confirmation.put("message", "'status':'error','message':'Login failed'");
            return confirmation;
        }
        if (args.length < 4) {
            confirmation.put("message", "'status':'error','message':'No quizz identifier was provided'");
            return confirmation;
        }

        List<Quiz> quizes = JSONWriteRead.MappingJSON("Quizes");
        if (quizes == null) {
            confirmation.put("message", "'status':'error','message':'No quiz was found'");
            return confirmation;
        }
        Integer quizToCompleteId = Integer.parseInt(String.valueOf(args[3].
                charAt(args[3].lastIndexOf(" ") + 2)));
        for (Quiz q: quizes) { // Looking for the quiz that has to be completed.
            if (q.getId() == quizToCompleteId) {
                /* First check if the user is the one that created the quiz.
                Users can't complete their own quizes. */
                if (q.getQuizCreator().equals(username)) {
                    confirmation.put("message", "'status':'error','message':'You cannot answer your own quizz'");
                    return confirmation;
                }

                // Check if the user already completed this quiz.
                User user = JSONWriteRead.ReadUser(username);
                if (user.CheckAlreadyCompleted(q.getName())) {
                    confirmation.put("message", "'status':'error','message':'You already submitted this quizz'");
                    return confirmation;
                }

                Float grade = 0.f; // Stores the final result.

                // Store all the answer ids in a list.
                ArrayList<Integer> answerIdList = new ArrayList<>();
                for (int i = 4; i < args.length; i++) {
                    answerIdList.add(Integer.parseInt(String.
                            valueOf(args[i].charAt(args[i].lastIndexOf(" ") + 2))));
                }

                List<Question> questions = JSONWriteRead.MappingJSON("Questions");
                for (Question question: questions) { // Calculate score for every question.
                    Float answerResult = 0.f;
                    try {
                        answerResult = question.AnswerTheQuestion(answerIdList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    /* subtracts the number of answers the question had from
                    the rest of answerIds (offset). This is done because every
                    question has its own answers list, with incremented identifiers,
                    but from input a single incremental identifier list is received. */
                    if (answerIdList.size() > 0) {
                        Utils.UpdateOffset(answerIdList, question.getAnswers().size());
                    }

                    grade += answerResult;
                }

                grade /=  q.getQuestionsIDs().size();
                if (grade < 0) { // Can't have a negative score for a quiz.
                    grade = 0.f;
                }

                if (answerIdList.size() != 0) { // Checking if all the answers are found and processed.
                    confirmation.put("message", "'status':'ok','message':'Answer ID for answer i does not exist'");
                    return confirmation;
                }

                // Add quiz to the user's completedQuizes map.
                user.AddNewCompletedQuiz(q.getName(), new WrapperQuizResult(q.getId(),
                        Math.round(grade), user.getCompletedQuizesLength() + 1));

                // Update the user json file.
                File oldUser = new File("src/Database/" + username + ".json");
                oldUser.delete(); // Deleting the old user JSON.
                JSONObject obj = user.ConvertUserToJSONObject();
                JSONWriteRead.WriteJSON(obj, username);

                confirmation.put("message", "'status':'ok','message':'" + Math.round(grade) + " points'");
                return confirmation;
            }
        }

        confirmation.put("message", "'status':'error','message':'No quiz was found'");
        return confirmation;
    }

    /**
     * Doing the checks to see if the input is valid.
     * In case of a valid input, delete a specified quiz.
     * @param args
     * @return JSONObject that contains the text confirmation.
     */
    public static JSONObject ParseDeleteQuiz(String[] args) {
        JSONObject confirmation = new JSONObject();

        // Checking if username and password are provided.
        if (args.length < 3) {
            confirmation.put("message", "'status':'error','message':'You need to be authenticated'");
            return confirmation;
        }

        String username = args[1].substring(args[1].lastIndexOf(" ") + 1);
        String password = args[2].substring(args[2].lastIndexOf(" ") + 1);
        if (args.length < 4) {
            if (User.Authentication(username, password) == false) {
                confirmation.put("message", "'status':'error','message':'Login failed'");
                return confirmation;
            }
            confirmation.put("message", "'status':'error','message':'No quizz identifier was provided'");
            return confirmation;
        }

        if (User.Authentication(username, password) == false) {
            confirmation.put("message", "'status':'error','message':'Login failed'");
            return confirmation;
        }

        String text = args[3];
        Boolean exist = Quiz.DeleteQuiz(text);
        if (exist == true) {
            confirmation.put("message", "'status':'ok','message':'Quizz deleted successfully'");
            return confirmation;
        } else {
            confirmation.put("message", "'status':'error','message':'No quiz was found'");
            return confirmation;
        }
    }

    /**
     * Doing the checks to see if the input is valid.
     * In case of a valid input, return the all the solutions to the completed quizes of a user.
     * @param args
     * @return JSONObject that contains the text confirmation.
     */
    public static JSONObject ParseGetMySolutions(String[] args) {
        JSONObject confirmation = new JSONObject();

        // Checking if username and password are provided.
        if (args.length < 3) {
            confirmation.put("message", "'status':'error','message':'You need to be authenticated'");
            return confirmation;
        }
        String username = args[1].substring(args[1].lastIndexOf(" ") + 1);
        String password = args[2].substring(args[2].lastIndexOf(" ") + 1);

        if (User.Authentication(username, password) == false) {
            confirmation.put("message", "'status':'error','message':'Login failed'");
            return confirmation;
        }
        User user = JSONWriteRead.ReadUser(username);
        Map<String, WrapperQuizResult> completedQuizes = user.getCompletedQuizes();
        ArrayList<String> resultStrings = new ArrayList<>();

        Iterator<Map.Entry<String, WrapperQuizResult>> iterator =
                completedQuizes.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, WrapperQuizResult> entry = iterator.next();
            resultStrings.add("{\"quiz-id\" : \"" + entry.getValue().getQuizId() +
                    "\", \"quiz-name\" : \"" + entry.getKey().substring(7, entry.getKey().length() - 1) +
                    "\", \"score\" : \"" + entry.getValue().getScore() +
                    "\", \"index_in_list\" : \"" + entry.getValue().getIndexInList() + "\"}");
        }

        confirmation.put("message", "'status':'ok','message': '" + resultStrings + "'");
        return confirmation;
    }
}



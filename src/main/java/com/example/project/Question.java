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
import java.util.*;

public class Question {

    private int id;
    private String text;
    private String type;
    private Map<String, Boolean> answers;

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }

    public Map<String, Boolean> getAnswers() {
        return answers;
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

    /**
     * Looking for a question with a specific ID
     * @param questions
     * @param id
     * @return
     */
    public Question FindQuestionById(List<Question> questions, Number id) {
        for (Question q: questions) {
            if (id.intValue() == q.getId()) {
                return q;
            }
        }

        return null;
    }

    /**
     * Checking if a answer is already present in the answers map.
     * @param answers
     * @param newAnswer
     * @return
     */
    public static Boolean CheckAnswerExists(Map<String, Boolean> answers, String newAnswer) {
        Iterator<Map.Entry<String, Boolean>> iterator = answers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Boolean> entry = iterator.next();
            if (entry.getKey().substring(entry.getKey().lastIndexOf(" ") + 2).
                    equals(newAnswer.substring(newAnswer.lastIndexOf(" ") + 2))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Calculating grade for a question based on the given answers.
     * The approach is to look for the answer ids in the the
     * questions's list of answers and when a match is found, the
     * grade is calculated and returned for a simple answer type
     * question, while the id is deleted from the answerIdList.
     * On the other hand, for a multiple answer type question,
     * in case of a match, the is is deleted and the grade receives
     * an update based on the validity of the answer. This process
     * continues until there are no more answers to the question or
     * the answerIdList becomes empty.
     * @param answerIdList
     * @return
     */
    public Float AnswerTheQuestion(ArrayList<Integer> answerIdList) throws InvalidQuestionTypeException {
        /* Map converted to TreeMap to reverse the order of the
        answers and obtain the initial order given at the input */
        Iterator<Map.Entry<String, Boolean>> iterator = Utils.
                ConvertMapToTreeMap(this.answers).entrySet().iterator();

        if (this.type.equals("-type 'single'")) {
            Integer answerIdToCheck = answerIdList.get(0);

            // Looking for the selected answer in the answers to the question.
            while (iterator.hasNext()) {
                Map.Entry<String, Boolean> entry = iterator.next();
                if (answerIdToCheck == 1) {
                    if (entry.getValue() == true) {
                        answerIdList.remove(0);
                        return 100.f;
                    } else {
                        answerIdList.remove(0);
                        return -100.f;
                    }
                }
                answerIdToCheck--;
            }

            return 0.f;

        } else if (this.type.equals("-type 'multiple'")) {
            /* For questions with multiple answers, every answer is checked and
            the grade is updated based on the correct/wrong answer shares. */
            Float grade = 0.f;
            long nrCorectAnswers = this.answers.entrySet().stream().filter(a -> a.getValue() == true).count();
            long nrWrongAnswers = this.answers.entrySet().stream().filter(a -> a.getValue() == false).count();
            Float correctAnswerShare = Float.valueOf((1 / Float.valueOf(nrCorectAnswers)));
            Float wrongAnswerShare = Float.valueOf((1 / Float.valueOf(nrWrongAnswers)));
            Integer answerIdToCheck = answerIdList.get(0);

            while (iterator.hasNext()) { // Looking for the selected answer in the answers to the question.
                Map.Entry<String, Boolean> entry = iterator.next();
                if (answerIdToCheck == 1) {
                    if (entry.getValue() == true) {
                        grade += correctAnswerShare;
                        answerIdList.remove(0);
                        if (answerIdList.isEmpty()) {
                            return grade * 100.f;
                        } else { // Get another answerId to check
                            answerIdToCheck = answerIdList.get(0);
                        }
                    } else {
                        grade -= wrongAnswerShare;
                        answerIdList.remove(0);
                        if (answerIdList.isEmpty()) {
                            return grade * 100.f;
                        } else { // Get another answerId to check
                            answerIdToCheck = answerIdList.get(0);
                        }
                    }
                }

                answerIdToCheck--;
            }

            return grade * 100.f;

        } else {
            throw new InvalidQuestionTypeException();
        }
    }
    public class InvalidQuestionTypeException extends Exception {
        public void InvalidQuestionTypeException() {
            System.out.println("Invalid question type");
            System.exit(0);
        }
    }
}

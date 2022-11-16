package com.example.project;

import org.json.simple.JSONObject;
import  org.json.simple.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException.*;
import java.util.Arrays;

public class Tema1 {

	public static void main(final String[] args) throws IllegalArgumentException {

		if(args == null)
		{
			System.out.print("Hello world!");
			return;
		}

		String request = args[0];
		JSONObject confirmation = null;

		switch (request) {
			case "-create-user":
				confirmation = Parser.ParseCreateUser(args);
				break;

			case "-create-question":
				confirmation = Parser.ParseCreateQuestion(args);
				break;

			case "-get-question-id-by-text":
				confirmation = Parser.GetQuestionIdByText(args);
				break;

			case "-get-all-questions":
				confirmation = Parser.GetAllQuestions(args);
				break;

			case "-create-quizz":
				confirmation = Parser.CreateQuiz(args);
				break;

			case "-get-quizz-by-name":
				confirmation = Parser.GetQuizIdByName(args);
				break;

			case "-get-all-quizzes":
				confirmation = Parser.GetAllQuizes(args);
				break;

			case "-get-quizz-details-by-id":
				confirmation = Parser.GetQuizDetailsByID(args);
				break;

			case "-delete-quizz":
				confirmation = Parser.DeleteQuiz(args);
				break;

			case "-get-my-solutions":
				confirmation = Parser.GetMySolutions(args);
				break;

			case "-cleanup-all":
				Arrays.stream(new File("src/Database/").listFiles()).forEach(File::delete);
				confirmation = null;
				break;

			default:
				System.out.println("Invalid request");
		}

		if (confirmation != null) {
			System.out.print("{" + confirmation.get("message") + "}");
		}
	}
}

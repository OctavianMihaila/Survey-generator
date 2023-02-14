package com.example.project;

import org.json.simple.JSONObject;
import java.io.File;
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
				confirmation = Parser.parseCreateUser(args);
				break;

			case "-create-question":
				confirmation = Parser.parseCreateQuestion(args);
				break;

			case "-get-question-id-by-text":
				confirmation = Parser.parseGetQuestionIdByText(args);
				break;

			case "-get-all-questions":
				confirmation = Parser.parseGetAllQuestions(args);
				break;

			case "-create-quizz":
				confirmation = Parser.parseCreateQuiz(args);
				break;

			case "-get-quizz-by-name":
				confirmation = Parser.parseGetQuizIdByName(args);
				break;

			case "-get-all-quizzes":
				confirmation = Parser.parseGetAllQuizzes(args);
				break;

			case "-get-quizz-details-by-id":
				confirmation = Parser.parseGetQuizDetailsByID(args);
				break;

			case "-submit-quizz":
				confirmation = Parser.parseSubmitAnswers(args);
				break;

			case "-delete-quizz-by-id":
				confirmation = Parser.parseDeleteQuiz(args);
				break;

			case "-get-my-solutions":
				confirmation = Parser.parseGetMySolutions(args);
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

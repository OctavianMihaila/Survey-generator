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
				confirmation = Parser.ParseCreateUser(args);
				break;

			case "-create-question":
				confirmation = Parser.ParseCreateQuestion(args);
				break;

			case "-get-question-id-by-text":
				confirmation = Parser.ParseGetQuestionIdByText(args);
				break;

			case "-get-all-questions":
				confirmation = Parser.ParseGetAllQuestions(args);
				break;

			case "-create-quizz":
				confirmation = Parser.ParseCreateQuiz(args);
				break;

			case "-get-quizz-by-name":
				confirmation = Parser.ParseGetQuizIdByName(args);
				break;

			case "-get-all-quizzes":
				confirmation = Parser.ParseGetAllQuizes(args);
				break;

			case "-get-quizz-details-by-id":
				confirmation = Parser.ParseGetQuizDetailsByID(args);
				break;

			case "-submit-quizz":
				confirmation = Parser.ParseSubmitAnswers(args);
				break;

			case "-delete-quizz-by-id":
				confirmation = Parser.ParseDeleteQuiz(args);
				break;

			case "-get-my-solutions":
				confirmation = Parser.ParseGetMySolutions(args);
				break;

			case "-cleanup-all":
				Arrays.stream(new File("src/Database/").listFiles()).forEach(File::delete);
				if (confirmation == null) {
					confirmation = new JSONObject();
				}
				confirmation.put("message", "'status':'error','message': 'Cleanup finished successfully'");
				break;

			default:
				System.out.println("Invalid request");
		}

		if (confirmation != null) {
			System.out.print("{" + confirmation.get("message") + "}");
		}
	}
}

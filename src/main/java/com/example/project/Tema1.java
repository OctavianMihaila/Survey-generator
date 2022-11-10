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
		}


		String request = args[0];
//		if(request.equals("-create-user")) {
//			Parser.ParseCreateUser(args);
//		}

		JSONObject confirmation = null;

		switch (request) {
			case "-create-user":
				confirmation = Parser.ParseCreateUser(args);
				break;

			case "-create-question":
				confirmation = Parser.ParseCreateQuestion(args);
				break;

			case "-get-question-id-by-text":
				System.out.println("TODO");
				confirmation = null;
				break;

			case "-get-all-questions":
				System.out.println("TODO");
				confirmation = null;
				break;

			case "-create-quizz":
				System.out.println("TODO");
				confirmation = null;
				break;

			case "-get-quizz-by-name":
				System.out.println("TODO");
				confirmation = null;
				break;

			case "-get-all-quizzes":
				System.out.println("TODO");
				confirmation = null;
				break;

			case "-get-quizz-details-by-id":
				System.out.println("TODO");
				confirmation = null;
				break;

			case "-delete-quizz":
				System.out.println("TODO");
				confirmation = null;
				break;

			case "-get-my-solutions":
				System.out.println("TODO");
				confirmation = null;
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

# Survey Generator

A simple survey generator application that allows users to create and manage quizzes with single or multiple correct answers. Users can also answer quizzes created by others.

## Table of Contents

- [Description](#description)
- [Structure](#structure)
- [Implementation](#implementation)

## Description

The Survey Generator is an application that enables users to create, manage, and answer quizzes. Users can log into the application and perform various actions, including:

- Create questions with single or multiple correct answers.
- Create quizzes based on the questions added by any user.
- Answer quizzes created by other users (only once).

## Structure

The application provides several commands to manage quizzes and questions. The most important commands include:

- **Create Question:** Users can add new questions with single or multiple response types to the system, making them visible to all users.

- **Create Quiz:** Users can add new questionnaires to the system, using questions created by any user.

- **Delete Quiz:** Users can remove their own quizzes from the system.

- **Submit Quiz:** Users can answer quizzes from the system, regardless of who created them.

- **Return My Answers:** Users can access detailed statistics containing the results of completed quizzes.

## Implementation

The Survey Generator is implemented using the following components:

- **Parser Class:** This class contains methods that perform parsing for various types of requests. It checks for invalid input and generates confirmation messages using an instance of the `JSONObject` class.

- **JSONWriteRead Class:** This class handles file operations, specifically with the database directory. Questions, quizzes, and user data are stored in individual files.

- **Quiz Completion:** The process of completing a quiz involves checking every answer for each question, based on the question's type. After checking a question, the number of answers the question had is subtracted from the remaining answer IDs (offset).

For more details about the implementation, please refer to the provided JavaDoc documentation and comments inside the methods.

This application is a powerful tool for creating and managing surveys and quizzes. It provides flexibility in question types and user interactions, making it a versatile solution for various use cases.

Feel free to explore the code and documentation for a deeper understanding of how the Survey Generator works and how to use it effectively.

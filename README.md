# Description

        -> Simple survey generator. Users of this program will log into the application on any
           system call other than user creation. Users logged into the system will be able to:
           
            >> create questions (with only one correct answer, or with several correct answers),
           
            >> create quizzes based on previously added questions, and will be able to answer
               others' quizzes, only once.

# Structure

        -> The quizzes generator allows the user to create and manage quizzes using some commands.
           The most important of the commands are:

            >> Create Question: add a new question(with a single or multiple response type)
               in the system visible to all users.

            >> Create Quiz: add a new questionnaire to the system using questions created by any user.

            >> Delete Quiz: Removes a own quiz from the system.

            >> Submit Quiz: Answer to a quiz from the system, regardless of which other user created it.

            >> Return my answers: Returns a detailed statistics containing the results of completed quizzes.

# Implementation:

        -> Parser class contains methods that perform the parse for every type of request.
           So, in those methods the checks for invalid input are done and confirmation messages
           are created using an instance of JSONObject class.

        -> JSONWriteRead class perform the work with the files (Database directory). I choosed to
           store all the questions in a file, the same for the quizzes and the users in individual files.

        -> The process of completing a quiz is done by checking every answer from every question,
           based on the questions's type. After checking a question, subtracting the number of answers
           the question had from the rest of answerIds (offset).

        -> More details about the implementation can be found in javadoc and comments inside methods.

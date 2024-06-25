package com.example.quiz.repositories;

import com.example.quiz.entities.Quiz;

public interface QuizRepository {
    /***
     * Create a quiz and return the quiz id that people can join
     * @param title
     * @param description
     * @param category
     * @param difficulty
     * @param time
     * @param limit
     * @return
     */
    public String createQuiz(String title, String description, String category, String difficulty, int time, int limit);

    /***
     * End the quiz
     * @param quizId
     */
    public void endQuiz(String quizId);

    public Quiz getQuizById(String quizId);
}

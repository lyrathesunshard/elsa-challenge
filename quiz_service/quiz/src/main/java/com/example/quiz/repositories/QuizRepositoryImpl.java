package com.example.quiz.repositories;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.quiz.entities.Quiz;

@Repository
public class QuizRepositoryImpl implements QuizRepository {

    @Override
    public String createQuiz(String title, String description, String category, String difficulty, int time, int limit) {
        // just fake the quiz id
        UUID quizId = UUID.randomUUID();
        String quizIdString = quizId.toString();
        return quizIdString;
    }

    @Override
    public void endQuiz(String quizId) {
        // return reward, save the result, etc
    }

    @Override
    public Quiz getQuizById(String quizId) {
        // mock the quiz
        Quiz quiz = Quiz.builder()
                .quizId(quizId)
                .title("Quiz Title")
                .description("Quiz Description")
                .category("Category")
                .difficulty("Difficulty")
                .timePerQuestion(10)
                .limit(100)
                .startTime(System.currentTimeMillis())
                .endTime(System.currentTimeMillis() + 1000 * 60 * 60)
                .build();

        return quiz;
    }
    
}

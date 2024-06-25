package com.example.quiz.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.quiz.entities.Question;
import com.example.quiz.entities.Quiz;

@Repository
/**
 * QuestionRepositoryImpl
  for mock question list, real implementation will be done in future, with some questions shuffling strategies
 */
public class QuestionRepositoryImpl implements QuestionRepository {

    @Override
    public List<Question> getQuestionsByQuiz(Quiz quiz) {
        // mock question list for quiz
        List<Question> questions = List.of(
            Question.builder()
                .questionId("1")
                .question("What is the capital of India?")
                .answer("New Delhi")
                .options(new String[] {"Mumbai", "New Delhi", "Kolkata", "Chennai"})
                .build(),
            Question.builder()
                .questionId("2")
                .question("What is the capital of Australia?")
                .answer("Canberra")
                .options(new String[] {"Sydney", "Melbourne", "Canberra", "Brisbane"})
                .build(),
            Question.builder()
                .questionId("3")
                .question("What is the capital of Japan?")
                .answer("Tokyo")
                .options(new String[] {"Osaka", "Kyoto", "Tokyo", "Hiroshima"})
                .build()
        );
        return questions;
    }
    
}

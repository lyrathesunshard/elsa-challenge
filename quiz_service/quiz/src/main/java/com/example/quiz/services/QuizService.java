package com.example.quiz.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quiz.entities.Question;
import com.example.quiz.entities.Quiz;
import com.example.quiz.payloads.create_quiz.CreateQuizRequest;
import com.example.quiz.repositories.QuestionRepositoryImpl;
import com.example.quiz.repositories.QuizRepository;

import okhttp3.OkHttpClient;

@Service
public class QuizService {
    private final OkHttpClient httpClient = new OkHttpClient();

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuestionRepositoryImpl questionRepository;

    public String createQuiz(CreateQuizRequest createQuizRequest) {
        return quizRepository.createQuiz(createQuizRequest.getTitle(), createQuizRequest.getDescription(), createQuizRequest.getCategory(), createQuizRequest.getDifficulty(), createQuizRequest.getTime(), createQuizRequest.getLimit());
    }

    public void endQuiz(String quizId) {
        quizRepository.endQuiz(quizId);
    }

    public Quiz getQuizById(String quizId) {
        return quizRepository.getQuizById(quizId);
    }

    public List<Question> getQuestionsByQuiz(Quiz quiz) {
        return questionRepository.getQuestionsByQuiz(quiz);
    }

}

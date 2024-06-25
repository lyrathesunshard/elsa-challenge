package com.example.quiz.repositories;

import java.util.List;

import com.example.quiz.entities.Question;
import com.example.quiz.entities.Quiz;

public interface QuestionRepository {
    public List<Question> getQuestionsByQuiz(Quiz quiz);
}

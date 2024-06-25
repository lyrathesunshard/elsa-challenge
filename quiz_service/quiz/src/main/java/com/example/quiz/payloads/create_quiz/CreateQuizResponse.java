package com.example.quiz.payloads.create_quiz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateQuizResponse {
    private String quizId;
}

package com.example.quiz.payloads.create_quiz;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateQuizRequest {
    private String title;
    private String description;
    private String category;
    private String difficulty;
    private int time;
    private int limit;
}

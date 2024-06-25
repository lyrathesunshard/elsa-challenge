package com.example.quiz.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Quiz {
    private String quizId;
    private String title;
    private String description;
    private String category;
    private String difficulty;
    private long timePerQuestion;
    private int limit;
    private long startTime;
    private long endTime;
}

package com.example.quiz.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Question {
    private String questionId;
    private String question;
    private String answer;
    private String[] options;
}

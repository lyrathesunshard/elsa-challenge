package com.example.quiz.payloads.quiz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class QuizMessage {

    public static enum QuizMessageType {
        JOIN, JOIN_CONFIRM, QUESTION, ANSWER, SCORE_UPDATE, END, ERROR
    }

    private QuizMessageType type;
    private String quizId;
    private String message;

}

package com.example.quiz.services;

import org.springframework.stereotype.Service;

import okhttp3.OkHttp;
import okhttp3.OkHttpClient;

@Service
public class ScoringService {
    private final OkHttpClient httpClient = new OkHttpClient();

    public int getScoreByAnswer(String quizId, String questionId, String answer) {
        // get the score by calling the scoring service
        int randomScore = (int) (Math.random() * 10);
        return randomScore;
    }
}

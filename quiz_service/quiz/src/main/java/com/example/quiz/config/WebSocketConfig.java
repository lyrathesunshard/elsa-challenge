package com.example.quiz.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.example.quiz.services.QuizService;
import com.example.quiz.services.ScoringService;
import com.example.quiz.socket.QuizWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(quizWebSocketHandler(), "/quiz").setAllowedOrigins("*");
    }

    @Autowired
    QuizService quizService;
    @Autowired
    ScoringService scoringService;

    @Bean
    public QuizWebSocketHandler quizWebSocketHandler() {
        return new QuizWebSocketHandler(quizService, scoringService);
    }
}


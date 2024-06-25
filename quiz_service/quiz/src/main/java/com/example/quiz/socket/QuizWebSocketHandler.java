package com.example.quiz.socket;

import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.quiz.entities.Question;
import com.example.quiz.entities.Quiz;
import com.example.quiz.payloads.quiz.QuizMessage;
import com.example.quiz.payloads.quiz.QuizMessage.QuizMessageType;
import com.example.quiz.services.QuizService;
import com.example.quiz.services.ScoringService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class QuizWebSocketHandler extends TextWebSocketHandler {

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final QuizService quizService;
    private final ScoringService scoringService;
    
    private Quiz quiz;
    private String currentQuestion;
    private int currentQuestionIndex;
    private long questionStartTime;
    private long questionEndTime;
    private List<Question> questions = new ArrayList<>();

    public QuizWebSocketHandler(QuizService quizService, ScoringService scoringService) {
        this.quizService = quizService;
        this.scoringService = scoringService;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        String quizId = extractQuizId(session);
        if (quizId != null) {
            quiz = quizService.getQuizById(quizId);
            if (quiz != null) {
                this.questions = quizService.getQuestionsByQuiz(quiz);
                runQuiz();
            } else {
                for (WebSocketSession s : sessions.values()) {
                    endQuizWithErrorMessage(s, "Quiz not found");
                }
            }
        } else {
            for (WebSocketSession s : sessions.values()) {
                endQuizWithErrorMessage(s, "Invalid quiz ID");
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
    }
    
    private void endQuizWithErrorMessage(WebSocketSession session, String message) {
        QuizMessage errorMessage = QuizMessage.builder().type(QuizMessageType.ERROR).quizId(quiz.getQuizId()).message(message).build();
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(errorMessage)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String extractQuizId(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri != null) {
            String query = uri.getQuery();
            if (query != null && query.startsWith("quizId=")) {
                return query.substring(7);
            }
        }
        return null;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        QuizMessage quizMessage = objectMapper.readValue(payload, QuizMessage.class);

        switch (quizMessage.getType()) {
            case JOIN:
                handleJoinQuiz(session, quizMessage);
                break;
            case ANSWER:
                handleAnswer(session, quizMessage);
                break;
            default:
                break;
        }
    }

    private void runQuiz() {
        while (currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            currentQuestion = question.getQuestionId();
            questionStartTime = System.currentTimeMillis();
            questionEndTime = questionStartTime + quiz.getTimePerQuestion();
            QuizMessage questionMessage = QuizMessage.builder().type(QuizMessageType.QUESTION).quizId(quiz.getQuizId()).message(question.getQuestion()).build();
            sessions.values().forEach(session -> {
                try {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(questionMessage)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            try {
                Thread.sleep(quiz.getTimePerQuestion());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentQuestionIndex++;
        }
        handleEndQuiz();
    }

    private void handleEndQuiz() {
        QuizMessage endMessage = QuizMessage.builder().type(QuizMessageType.END).quizId(quiz.getQuizId()).message("Quiz ended, rewards will be send to your mailbox").build();
        sessions.values().forEach(session -> {
            try {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(endMessage)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Receives leaderboard updates and sends them to all connected clients
     * @param update
     * @param quizId
     */
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "leaderboard.#{quiz.id}", durable = "true"),
        exchange = @Exchange(value = "quiz.leaderboard", type = ExchangeTypes.TOPIC),
        key = "leaderboard.#"
    ))
    public void receiveLeaderboardUpdate(String update, @Header("quiz_id") String quizId) {
        sessions.values().forEach(session -> {
            try {
                session.sendMessage(new TextMessage(update));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Handles a JOIN message from a client
     * @param session
     * @param message
     * @throws Exception
     */
    private void handleJoinQuiz(WebSocketSession session, QuizMessage message) throws Exception {
        // Logic to join a quiz
        String quizId = message.getQuizId();
        // Validate quiz ID, create or join session, etc.
        
        QuizMessage response = QuizMessage.builder().type(QuizMessageType.JOIN_CONFIRM).quizId(quizId).build();
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
    }

    /**
     * Handles an ANSWER message from a client
     * @param session
     * @param message
     * @throws Exception
     */
    private void handleAnswer(WebSocketSession session, QuizMessage message) throws Exception {
        // Logic to process an answer
        String answer = message.getMessage();
        // Send score update
        QuizMessage scoreUpdate = QuizMessage.builder().type(QuizMessageType.SCORE_UPDATE).quizId(message.getQuizId()).build();
        scoreUpdate.setMessage(String.valueOf(scoringService.getScoreByAnswer(message.getQuizId(), currentQuestion, answer)));
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(scoreUpdate)));
    }

}
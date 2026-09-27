package com.codsoft.quiz.ui;

import com.codsoft.quiz.model.QuizResult;
import com.codsoft.quiz.service.QuizManager;
import com.codsoft.quiz.util.AppConstants;

import javax.swing.*;
import java.awt.*;

/**
 * MainFrame
 * 
 * Top-level window frame of the Quiz Application.
 * Employs a CardLayout to transition cleanly among:
 * 1. Welcome Screen
 * 2. Active Quiz Screen
 * 3. Final Result & Review Screen
 */
public class MainFrame extends JFrame {

    public static final String CARD_WELCOME = "WELCOME";
    public static final String CARD_QUIZ = "QUIZ";
    public static final String CARD_RESULT = "RESULT";

    private final QuizManager quizManager;

    private CardLayout cardLayout;
    private JPanel cardsPanel;

    private WelcomePanel welcomePanel;
    private QuizPanel quizPanel;
    private ResultPanel resultPanel;

    public MainFrame() {
        this.quizManager = new QuizManager();
        initFrame();
    }

    private void initFrame() {
        setTitle(AppConstants.APP_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(AppConstants.WINDOW_WIDTH, AppConstants.WINDOW_HEIGHT);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null); // Center on screen

        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        // Instantiate cards
        welcomePanel = new WelcomePanel(this);
        quizPanel = new QuizPanel(this, quizManager);
        resultPanel = new ResultPanel(this);

        cardsPanel.add(welcomePanel, CARD_WELCOME);
        cardsPanel.add(quizPanel, CARD_QUIZ);
        cardsPanel.add(resultPanel, CARD_RESULT);

        add(cardsPanel, BorderLayout.CENTER);

        // Default screen is Welcome
        showWelcomeScreen();
    }

    /**
     * Navigates to the initial Welcome screen.
     */
    public void showWelcomeScreen() {
        quizPanel.stopTimer();
        cardLayout.show(cardsPanel, CARD_WELCOME);
    }

    /**
     * Starts a new quiz session and displays the first question.
     */
    public void startQuiz() {
        quizManager.startNewQuiz();
        quizPanel.displayCurrentQuestion();
        cardLayout.show(cardsPanel, CARD_QUIZ);
    }

    /**
     * Displays the Result screen with computed score metrics.
     */
    public void showResultScreen(QuizResult result) {
        quizPanel.stopTimer();
        resultPanel.displayResult(result);
        cardLayout.show(cardsPanel, CARD_RESULT);
    }

    /**
     * Resets the quiz session and restarts from Question 1.
     */
    public void restartQuiz() {
        quizPanel.stopTimer();
        startQuiz();
    }
}

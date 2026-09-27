package com.codsoft.quiz.service;

import com.codsoft.quiz.model.Question;
import com.codsoft.quiz.model.QuizResult;
import com.codsoft.quiz.model.UserAnswer;
import com.codsoft.quiz.util.AppConstants;
import com.codsoft.quiz.util.QuestionBank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * QuizManager
 * 
 * Handles core business logic for the quiz lifecycle:
 * - Loading and shuffling questions
 * - Tracking current question index
 * - Recording user submissions and timeouts
 * - Computing final results
 * 
 * Keeps business logic decoupled from the Swing presentation layer.
 */
public class QuizManager {

    private final List<Question> questions;
    private final List<UserAnswer> userAnswers;
    private int currentQuestionIndex;
    private boolean quizActive;

    /**
     * Initializes a new QuizManager with questions from QuestionBank.
     */
    public QuizManager() {
        this.questions = new ArrayList<>();
        this.userAnswers = new ArrayList<>();
        this.currentQuestionIndex = 0;
        this.quizActive = false;
    }

    /**
     * Starts or restarts a new quiz session.
     * Loads configured number of questions from the QuestionBank.
     */
    public void startNewQuiz() {
        questions.clear();
        userAnswers.clear();
        questions.addAll(QuestionBank.getQuizQuestions(AppConstants.QUIZ_QUESTION_COUNT, true));
        currentQuestionIndex = 0;
        quizActive = true;
    }

    /**
     * Starts quiz with an explicit list of questions (useful for testing or custom sets).
     */
    public void startNewQuizWithQuestions(List<Question> customQuestions) {
        if (customQuestions == null || customQuestions.isEmpty()) {
            throw new IllegalArgumentException("Questions list cannot be null or empty.");
        }
        questions.clear();
        userAnswers.clear();
        questions.addAll(customQuestions);
        currentQuestionIndex = 0;
        quizActive = true;
    }

    /**
     * Returns the currently active Question.
     */
    public Question getCurrentQuestion() {
        if (!quizActive || currentQuestionIndex >= questions.size()) {
            return null;
        }
        return questions.get(currentQuestionIndex);
    }

    /**
     * Returns 1-based question number for display (e.g. 1 to 10).
     */
    public int getCurrentQuestionNumber() {
        return currentQuestionIndex + 1;
    }

    /**
     * Returns total number of questions in current quiz session.
     */
    public int getTotalQuestions() {
        return questions.size();
    }

    /**
     * Submits an answer selected by the user for the current question.
     *
     * @param selectedOptionIndex 0-based index of chosen option (0-3)
     * @return The recorded UserAnswer
     */
    public UserAnswer submitAnswer(int selectedOptionIndex) {
        if (!quizActive || isQuizFinished()) {
            throw new IllegalStateException("Quiz is not active or has already finished.");
        }

        Question current = getCurrentQuestion();
        UserAnswer answer = new UserAnswer(current, selectedOptionIndex, false);
        userAnswers.add(answer);
        currentQuestionIndex++;

        if (isQuizFinished()) {
            quizActive = false;
        }
        return answer;
    }

    /**
     * Records an automatic timeout when the countdown timer reaches zero.
     * Marks the current question as unanswered / timed out.
     *
     * @return The recorded UserAnswer
     */
    public UserAnswer recordTimeout() {
        if (!quizActive || isQuizFinished()) {
            throw new IllegalStateException("Quiz is not active or has already finished.");
        }

        Question current = getCurrentQuestion();
        UserAnswer answer = new UserAnswer(current, -1, true);
        userAnswers.add(answer);
        currentQuestionIndex++;

        if (isQuizFinished()) {
            quizActive = false;
        }
        return answer;
    }

    /**
     * Checks if all questions have been answered or timed out.
     */
    public boolean isQuizFinished() {
        return currentQuestionIndex >= questions.size();
    }

    /**
     * Checks if the quiz is currently active.
     */
    public boolean isQuizActive() {
        return quizActive;
    }

    /**
     * Computes and returns the overall QuizResult.
     */
    public QuizResult calculateResult() {
        return new QuizResult(userAnswers);
    }

    /**
     * Returns unmodifiable list of all user answers recorded so far.
     */
    public List<UserAnswer> getUserAnswers() {
        return Collections.unmodifiableList(userAnswers);
    }

    /**
     * Returns current question progress percentage (0 - 100).
     */
    public int getProgressPercentage() {
        if (questions.isEmpty()) return 0;
        return (int) (((double) currentQuestionIndex / questions.size()) * 100);
    }
}

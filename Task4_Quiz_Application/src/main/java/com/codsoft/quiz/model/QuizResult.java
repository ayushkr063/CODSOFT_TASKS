package com.codsoft.quiz.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * QuizResult
 * 
 * Aggregates the overall performance and score of a completed quiz session.
 * Computes totals, correct/incorrect/unanswered breakdowns, percentage,
 * and maintains the immutable collection of individual question reviews.
 */
public class QuizResult {

    private final int totalQuestions;
    private final int correctCount;
    private final int incorrectCount;
    private final int unansweredCount;
    private final int score;
    private final double percentage;
    private final List<UserAnswer> userAnswers;

    /**
     * Constructs a QuizResult from a completed list of UserAnswer instances.
     *
     * @param userAnswers List of user answers for each question
     */
    public QuizResult(List<UserAnswer> userAnswers) {
        Objects.requireNonNull(userAnswers, "User answers list cannot be null.");
        this.userAnswers = Collections.unmodifiableList(new ArrayList<>(userAnswers));
        this.totalQuestions = userAnswers.size();

        int correct = 0;
        int incorrect = 0;
        int unanswered = 0;

        for (UserAnswer ans : userAnswers) {
            switch (ans.getStatus()) {
                case CORRECT:
                    correct++;
                    break;
                case INCORRECT:
                    incorrect++;
                    break;
                case UNANSWERED:
                    unanswered++;
                    break;
            }
        }

        this.correctCount = correct;
        this.incorrectCount = incorrect;
        this.unansweredCount = unanswered;
        this.score = correct; // 1 mark per correct answer
        this.percentage = (totalQuestions > 0) ? ((double) correct / totalQuestions) * 100.0 : 0.0;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public int getIncorrectCount() {
        return incorrectCount;
    }

    public int getUnansweredCount() {
        return unansweredCount;
    }

    public int getScore() {
        return score;
    }

    public double getPercentage() {
        return percentage;
    }

    public List<UserAnswer> getUserAnswers() {
        return userAnswers;
    }

    /**
     * Returns an evaluative performance summary badge string.
     */
    public String getPerformanceGrade() {
        if (percentage >= 90.0) return "Excellent (O)";
        if (percentage >= 75.0) return "Very Good (A+)";
        if (percentage >= 60.0) return "Good (A)";
        if (percentage >= 50.0) return "Satisfactory (B)";
        return "Needs Improvement (C)";
    }

    /**
     * Returns a personalized feedback message.
     */
    public String getFeedbackMessage() {
        if (percentage >= 90.0) {
            return "Outstanding! You possess an exceptional grasp of core Java fundamentals.";
        } else if (percentage >= 75.0) {
            return "Well done! You have a solid understanding of Java principles.";
        } else if (percentage >= 50.0) {
            return "Good effort! Review the incorrect questions below to solidify your knowledge.";
        } else {
            return "Keep practicing! Study the explanations in the review section below to improve.";
        }
    }
}

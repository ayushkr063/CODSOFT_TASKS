package com.codsoft.quiz.model;

import java.util.Objects;

/**
 * UserAnswer
 * 
 * Captures the user's interaction and response for an individual question.
 * Records the selected option, whether the answer was submitted in time,
 * or if the question timed out without a selection.
 */
public class UserAnswer {

    public enum Status {
        CORRECT,
        INCORRECT,
        UNANSWERED
    }

    private final Question question;
    private final int selectedOptionIndex; // -1 if unanswered / timed out
    private final boolean timedOut;
    private final Status status;

    /**
     * Constructs a record of the user's answer.
     *
     * @param question            The question being answered
     * @param selectedOptionIndex Index of the option selected (0-3), or -1 if none
     * @param timedOut            Whether the timer expired before manual submission
     */
    public UserAnswer(Question question, int selectedOptionIndex, boolean timedOut) {
        this.question = Objects.requireNonNull(question, "Question cannot be null.");
        this.selectedOptionIndex = selectedOptionIndex;
        this.timedOut = timedOut;

        if (selectedOptionIndex < 0 || selectedOptionIndex >= question.getOptions().size()) {
            this.status = Status.UNANSWERED;
        } else if (question.isCorrect(selectedOptionIndex)) {
            this.status = Status.CORRECT;
        } else {
            this.status = Status.INCORRECT;
        }
    }

    public Question getQuestion() {
        return question;
    }

    public int getSelectedOptionIndex() {
        return selectedOptionIndex;
    }

    public boolean isTimedOut() {
        return timedOut;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isAnswered() {
        return status != Status.UNANSWERED;
    }

    public boolean isCorrect() {
        return status == Status.CORRECT;
    }

    /**
     * Returns the user's chosen answer text or a descriptive placeholder.
     */
    public String getSelectedAnswerText() {
        if (selectedOptionIndex >= 0 && selectedOptionIndex < question.getOptions().size()) {
            return "(" + Question.getOptionLetter(selectedOptionIndex) + ") " + 
                   question.getOption(selectedOptionIndex);
        }
        return timedOut ? "Timed Out (No Answer)" : "Unanswered";
    }

    /**
     * Returns the correct answer text with letter prefix.
     */
    public String getCorrectAnswerText() {
        return "(" + Question.getOptionLetter(question.getCorrectAnswerIndex()) + ") " + 
               question.getCorrectAnswerText();
    }
}

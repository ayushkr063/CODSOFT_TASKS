package com.codsoft.quiz.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Question
 * 
 * Represents a single multiple-choice question in the quiz.
 * Follows encapsulation principles by keeping fields private and providing
 * defensive copies of mutable collection properties.
 */
public class Question {

    private final int id;
    private final String questionText;
    private final List<String> options;
    private final int correctAnswerIndex;
    private final String topic;
    private final String explanation;

    /**
     * Constructs a new Question.
     *
     * @param id                 Unique question identifier
     * @param questionText       The text of the question
     * @param options            List of 4 answer options
     * @param correctAnswerIndex 0-based index of the correct answer (0 to 3)
     * @param topic              Topic category of the question
     * @param explanation        Brief conceptual explanation
     */
    public Question(int id, String questionText, List<String> options, 
                    int correctAnswerIndex, String topic, String explanation) {
        if (id <= 0) {
            throw new IllegalArgumentException("Question ID must be positive.");
        }
        this.id = id;
        this.questionText = Objects.requireNonNull(questionText, "Question text cannot be null.").trim();
        
        Objects.requireNonNull(options, "Options cannot be null.");
        if (options.size() != 4) {
            throw new IllegalArgumentException("Each question must have exactly 4 options. Provided: " + options.size());
        }
        this.options = Collections.unmodifiableList(new ArrayList<>(options));

        if (correctAnswerIndex < 0 || correctAnswerIndex >= 4) {
            throw new IllegalArgumentException("Correct answer index must be between 0 and 3. Provided: " + correctAnswerIndex);
        }
        this.correctAnswerIndex = correctAnswerIndex;
        this.topic = (topic != null && !topic.trim().isEmpty()) ? topic.trim() : "General Java";
        this.explanation = (explanation != null) ? explanation.trim() : "";
    }

    public int getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getOption(int index) {
        if (index < 0 || index >= options.size()) {
            throw new IndexOutOfBoundsException("Option index out of bounds: " + index);
        }
        return options.get(index);
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public String getCorrectAnswerText() {
        return options.get(correctAnswerIndex);
    }

    public String getTopic() {
        return topic;
    }

    public String getExplanation() {
        return explanation;
    }

    /**
     * Validates whether the given 0-based option index matches the correct answer.
     *
     * @param selectedIndex The index selected by the user
     * @return true if correct, false otherwise
     */
    public boolean isCorrect(int selectedIndex) {
        return selectedIndex == this.correctAnswerIndex;
    }

    /**
     * Converts an index to its letter representation (0 -> 'A', 1 -> 'B', etc.).
     *
     * @param index 0-based option index
     * @return String letter label (A, B, C, D)
     */
    public static String getOptionLetter(int index) {
        if (index >= 0 && index < 26) {
            return String.valueOf((char) ('A' + index));
        }
        return "?";
    }

    @Override
    public String toString() {
        return "Question #" + id + " [" + topic + "]: " + questionText;
    }
}

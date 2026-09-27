package com.codsoft.quiz;

import com.codsoft.quiz.model.Question;
import com.codsoft.quiz.model.QuizResult;
import com.codsoft.quiz.model.UserAnswer;
import com.codsoft.quiz.service.QuizManager;
import com.codsoft.quiz.util.AppConstants;
import com.codsoft.quiz.util.QuestionBank;

import java.util.Arrays;
import java.util.List;

/**
 * QuizAppTest
 * 
 * Standalone verification test suite.
 * Validates models, services, timer configurations, and scoring calculations
 * using standard Java without requiring external testing dependencies.
 */
public class QuizAppTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("    RUNNING QUIZ APPLICATION AUTOMATED TESTS     ");
        System.out.println("=================================================");

        testQuestionModelValidation();
        testQuestionBankLoading();
        testQuizManagerHappyPath();
        testQuizManagerTimeoutHandling();
        testScoreAndPercentageCalculation();
        testQuizRestartLogic();
        testAppConstants();

        System.out.println("=================================================");
        System.out.println("TEST SUMMARY:");
        System.out.println("Total Tests Passed: " + testsPassed);
        System.out.println("Total Tests Failed: " + testsFailed);
        System.out.println("=================================================");

        if (testsFailed > 0) {
            System.err.println("Some tests failed!");
            System.exit(1);
        } else {
            System.out.println("ALL TESTS PASSED SUCCESSFULLY! (100% Correctness)");
        }
    }

    private static void assertTrue(String testName, boolean condition) {
        if (condition) {
            System.out.println(" [PASS] " + testName);
            testsPassed++;
        } else {
            System.err.println(" [FAIL] " + testName);
            testsFailed++;
        }
    }

    private static void assertEquals(String testName, Object expected, Object actual) {
        if (expected == null && actual == null) {
            System.out.println(" [PASS] " + testName);
            testsPassed++;
        } else if (expected != null && expected.equals(actual)) {
            System.out.println(" [PASS] " + testName);
            testsPassed++;
        } else {
            System.err.println(" [FAIL] " + testName + " | Expected: " + expected + ", Actual: " + actual);
            testsFailed++;
        }
    }

    private static void testQuestionModelValidation() {
        System.out.println("\n--- Testing Question Model ---");
        Question q = new Question(
            101,
            "What is OOP?",
            Arrays.asList("Object Oriented", "Only Operations", "Open Option", "None"),
            0,
            "OOP Basics",
            "OOP stands for Object-Oriented Programming"
        );

        assertEquals("Question ID matches", 101, q.getId());
        assertEquals("Correct option index", 0, q.getCorrectAnswerIndex());
        assertEquals("Correct answer text", "Object Oriented", q.getCorrectAnswerText());
        assertTrue("isCorrect returns true for index 0", q.isCorrect(0));
        assertTrue("isCorrect returns false for index 1", !q.isCorrect(1));
        assertEquals("Option letter for 0 is A", "A", Question.getOptionLetter(0));
        assertEquals("Option letter for 3 is D", "D", Question.getOptionLetter(3));

        // Validation for invalid options count
        boolean exceptionThrown = false;
        try {
            new Question(102, "Bad question", Arrays.asList("Opt 1", "Opt 2"), 0, "Test", "Exp");
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue("Rejects question with fewer than 4 options", exceptionThrown);
    }

    private static void testQuestionBankLoading() {
        System.out.println("\n--- Testing QuestionBank ---");
        List<Question> allQuestions = QuestionBank.getAllQuestions();
        assertTrue("QuestionBank contains at least 10 questions", allQuestions.size() >= 10);
        assertTrue("QuestionBank has 12 questions", allQuestions.size() == 12);

        List<Question> quizSet = QuestionBank.getQuizQuestions(AppConstants.QUIZ_QUESTION_COUNT, false);
        assertEquals("getQuizQuestions returns requested count", AppConstants.QUIZ_QUESTION_COUNT, quizSet.size());
    }

    private static void testQuizManagerHappyPath() {
        System.out.println("\n--- Testing QuizManager Happy Path ---");
        QuizManager manager = new QuizManager();

        Question q1 = new Question(1, "Q1", Arrays.asList("A", "B", "C", "D"), 0, "Topic", "Exp");
        Question q2 = new Question(2, "Q2", Arrays.asList("A", "B", "C", "D"), 1, "Topic", "Exp");
        manager.startNewQuizWithQuestions(Arrays.asList(q1, q2));

        assertTrue("Quiz is active", manager.isQuizActive());
        assertEquals("Current question number is 1", 1, manager.getCurrentQuestionNumber());
        assertEquals("Total questions is 2", 2, manager.getTotalQuestions());

        // Submit correct answer for Q1
        UserAnswer a1 = manager.submitAnswer(0);
        assertTrue("Q1 answer is correct", a1.isCorrect());
        assertEquals("Q1 status is CORRECT", UserAnswer.Status.CORRECT, a1.getStatus());

        // Now on Q2
        assertEquals("Current question number is 2", 2, manager.getCurrentQuestionNumber());

        // Submit incorrect answer for Q2
        UserAnswer a2 = manager.submitAnswer(3);
        assertTrue("Q2 answer is incorrect", !a2.isCorrect());
        assertEquals("Q2 status is INCORRECT", UserAnswer.Status.INCORRECT, a2.getStatus());

        assertTrue("Quiz is finished", manager.isQuizFinished());
        assertTrue("Quiz is no longer active", !manager.isQuizActive());

        QuizResult result = manager.calculateResult();
        assertEquals("Total questions in result is 2", 2, result.getTotalQuestions());
        assertEquals("Correct count is 1", 1, result.getCorrectCount());
        assertEquals("Incorrect count is 1", 1, result.getIncorrectCount());
        assertEquals("Unanswered count is 0", 0, result.getUnansweredCount());
        assertEquals("Final score is 1", 1, result.getScore());
        assertEquals("Percentage is 50.0%", 50.0, result.getPercentage());
    }

    private static void testQuizManagerTimeoutHandling() {
        System.out.println("\n--- Testing Timeout Handling ---");
        QuizManager manager = new QuizManager();

        Question q1 = new Question(1, "Q1", Arrays.asList("A", "B", "C", "D"), 2, "Topic", "Exp");
        manager.startNewQuizWithQuestions(Arrays.asList(q1));

        UserAnswer timeoutAns = manager.recordTimeout();
        assertTrue("Answer marked as timed out", timeoutAns.isTimedOut());
        assertEquals("Status is UNANSWERED", UserAnswer.Status.UNANSWERED, timeoutAns.getStatus());
        assertTrue("Answer is not correct", !timeoutAns.isCorrect());
        assertTrue("Answer contains timeout text", timeoutAns.getSelectedAnswerText().contains("Timed Out"));

        QuizResult result = manager.calculateResult();
        assertEquals("Unanswered count is 1", 1, result.getUnansweredCount());
        assertEquals("Score is 0", 0, result.getScore());
        assertEquals("Percentage is 0.0%", 0.0, result.getPercentage());
    }

    private static void testScoreAndPercentageCalculation() {
        System.out.println("\n--- Testing Detailed Score & Grade Calculation ---");
        // Create 10 mock questions
        List<Question> qList = QuestionBank.getAllQuestions().subList(0, 10);
        QuizManager manager = new QuizManager();
        manager.startNewQuizWithQuestions(qList);

        // 7 correct, 2 incorrect, 1 timeout
        for (int i = 0; i < 7; i++) {
            manager.submitAnswer(qList.get(i).getCorrectAnswerIndex());
        }
        for (int i = 7; i < 9; i++) {
            // wrong answer
            int wrong = (qList.get(i).getCorrectAnswerIndex() + 1) % 4;
            manager.submitAnswer(wrong);
        }
        // 1 timeout
        manager.recordTimeout();

        QuizResult res = manager.calculateResult();
        assertEquals("Total questions is 10", 10, res.getTotalQuestions());
        assertEquals("Correct count is 7", 7, res.getCorrectCount());
        assertEquals("Incorrect count is 2", 2, res.getIncorrectCount());
        assertEquals("Unanswered count is 1", 1, res.getUnansweredCount());
        assertEquals("Score is 7", 7, res.getScore());
        assertEquals("Percentage is 70.0%", 70.0, res.getPercentage());
        assertEquals("Grade is Good (A)", "Good (A)", res.getPerformanceGrade());
    }

    private static void testQuizRestartLogic() {
        System.out.println("\n--- Testing Quiz Restart Logic ---");
        QuizManager manager = new QuizManager();
        manager.startNewQuiz();
        assertEquals("New quiz starts at question 1", 1, manager.getCurrentQuestionNumber());

        manager.submitAnswer(0);
        assertEquals("Moves to question 2", 2, manager.getCurrentQuestionNumber());

        // Restart
        manager.startNewQuiz();
        assertEquals("Restarted quiz resets to question 1", 1, manager.getCurrentQuestionNumber());
        assertEquals("User answers reset to 0", 0, manager.getUserAnswers().size());
        assertTrue("Quiz active after restart", manager.isQuizActive());
    }

    private static void testAppConstants() {
        System.out.println("\n--- Testing AppConstants ---");
        assertTrue("QUESTION_TIME_LIMIT is 20s", AppConstants.QUESTION_TIME_LIMIT == 20);
        assertTrue("QUIZ_QUESTION_COUNT is 10", AppConstants.QUIZ_QUESTION_COUNT == 10);
        assertTrue("APP_TITLE is defined", AppConstants.APP_TITLE != null && !AppConstants.APP_TITLE.isEmpty());
    }
}

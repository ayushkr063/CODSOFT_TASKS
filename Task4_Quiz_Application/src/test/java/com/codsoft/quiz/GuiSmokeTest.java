package com.codsoft.quiz;

import com.codsoft.quiz.model.QuizResult;
import com.codsoft.quiz.model.UserAnswer;
import com.codsoft.quiz.service.QuizManager;
import com.codsoft.quiz.ui.MainFrame;
import com.codsoft.quiz.ui.QuizPanel;
import com.codsoft.quiz.ui.ResultPanel;
import com.codsoft.quiz.ui.WelcomePanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * GuiSmokeTest
 * 
 * Verifies that all Swing UI components (MainFrame, WelcomePanel, QuizPanel, ResultPanel)
 * instantiate cleanly on the Event Dispatch Thread (EDT) without graphical or threading errors.
 */
public class GuiSmokeTest {

    public static void main(String[] args) throws Exception {
        System.out.println("Running Swing GUI Component Verification...");

        SwingUtilities.invokeAndWait(() -> {
            try {
                // 1. Verify MainFrame creation
                MainFrame frame = new MainFrame();
                System.out.println(" [PASS] MainFrame instantiated successfully");

                // 2. Start quiz on frame
                frame.startQuiz();
                System.out.println(" [PASS] Quiz transitioned successfully from Welcome to Quiz screen");

                // 3. Simulate QuizManager completion and Result Screen display
                QuizManager manager = new QuizManager();
                manager.startNewQuiz();
                for (int i = 0; i < manager.getTotalQuestions(); i++) {
                    if (i % 2 == 0) {
                        manager.submitAnswer(manager.getCurrentQuestion().getCorrectAnswerIndex());
                    } else {
                        manager.recordTimeout();
                    }
                }
                QuizResult result = manager.calculateResult();
                frame.showResultScreen(result);
                System.out.println(" [PASS] ResultPanel loaded and displayed successfully");

                // 4. Verify Restart functionality
                frame.restartQuiz();
                System.out.println(" [PASS] Quiz restarted successfully");

                // 5. Clean disposal
                frame.dispose();
                System.out.println(" [PASS] MainFrame disposed cleanly");
                System.out.println("SWING GUI SMOKE TEST COMPLETED SUCCESSFULLY!");
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });

        System.exit(0);
    }
}

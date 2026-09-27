package com.codsoft.quiz.ui;

import com.codsoft.quiz.model.Question;
import com.codsoft.quiz.model.QuizResult;
import com.codsoft.quiz.model.UserAnswer;
import com.codsoft.quiz.util.AppConstants;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 * ResultPanel
 * 
 * Displays the comprehensive final score report and detailed question-by-question review.
 * Presents key statistics (Total, Correct, Incorrect, Unanswered, Percentage, Grade)
 * and offers actions to restart the quiz or exit the application.
 */
public class ResultPanel extends JPanel {

    private final MainFrame mainFrame;

    private JPanel statsPanel;
    private JPanel reviewListPanel;
    private JLabel scoreBannerLabel;
    private JLabel feedbackLabel;

    public ResultPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 16));
        setBackground(AppConstants.COLOR_BACKGROUND);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // 1. TOP SUMMARY CARD
        JPanel topCard = createTopSummaryCard();
        add(topCard, BorderLayout.NORTH);

        // 2. CENTER SCROLLABLE REVIEW PANEL
        JPanel reviewContainer = createReviewContainer();
        add(reviewContainer, BorderLayout.CENTER);

        // 3. BOTTOM BUTTONS (Restart / Exit)
        JPanel bottomActionPanel = createBottomActions();
        add(bottomActionPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopSummaryCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(AppConstants.COLOR_CARD_BG);
        card.setBorder(new CompoundBorder(
            new LineBorder(AppConstants.COLOR_CARD_BORDER, 1, true),
            new EmptyBorder(18, 24, 18, 24)
        ));

        JLabel titleLabel = new JLabel("QUIZ COMPLETED");
        titleLabel.setFont(AppConstants.FONT_TITLE);
        titleLabel.setForeground(AppConstants.COLOR_SECONDARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreBannerLabel = new JLabel("Your Score: 0 / 0 (0%)");
        scoreBannerLabel.setFont(AppConstants.FONT_SUBTITLE);
        scoreBannerLabel.setForeground(AppConstants.COLOR_PRIMARY);
        scoreBannerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        feedbackLabel = new JLabel("Well done!");
        feedbackLabel.setFont(AppConstants.FONT_BODY);
        feedbackLabel.setForeground(AppConstants.COLOR_TEXT_MUTED);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Statistics Grid Panel
        statsPanel = new JPanel(new GridLayout(1, 5, 12, 0));
        statsPanel.setOpaque(false);
        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsPanel.setMaximumSize(new Dimension(800, 70));

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(scoreBannerLabel);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(feedbackLabel);
        card.add(Box.createRigidArea(new Dimension(0, 14)));
        card.add(statsPanel);

        return card;
    }

    private JPanel createReviewContainer() {
        JPanel container = new JPanel(new BorderLayout(0, 8));
        container.setOpaque(false);

        JLabel reviewTitle = new JLabel("Question-Wise Detailed Review");
        reviewTitle.setFont(AppConstants.FONT_SECTION);
        reviewTitle.setForeground(AppConstants.COLOR_SECONDARY);
        container.add(reviewTitle, BorderLayout.NORTH);

        reviewListPanel = new JPanel();
        reviewListPanel.setLayout(new BoxLayout(reviewListPanel, BoxLayout.Y_AXIS));
        reviewListPanel.setBackground(AppConstants.COLOR_BACKGROUND);

        JScrollPane scrollPane = new JScrollPane(reviewListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        container.add(scrollPane, BorderLayout.CENTER);
        return container;
    }

    private JPanel createBottomActions() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        panel.setOpaque(false);

        // Exit Button
        JButton exitBtn = new JButton("EXIT APPLICATION");
        exitBtn.setFont(AppConstants.FONT_BUTTON);
        exitBtn.setForeground(AppConstants.COLOR_TEXT_DARK);
        exitBtn.setBackground(new Color(241, 245, 249));
        exitBtn.setOpaque(true);
        exitBtn.setBorderPainted(false);
        exitBtn.setFocusPainted(false);
        exitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitBtn.setPreferredSize(new Dimension(180, 42));
        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Are you sure you want to exit?", 
                "Exit Confirmation", 
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Restart Button
        JButton restartBtn = new JButton("RESTART QUIZ");
        restartBtn.setFont(AppConstants.FONT_BUTTON);
        restartBtn.setForeground(AppConstants.COLOR_TEXT_LIGHT);
        restartBtn.setBackground(AppConstants.COLOR_PRIMARY);
        restartBtn.setOpaque(true);
        restartBtn.setBorderPainted(false);
        restartBtn.setFocusPainted(false);
        restartBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        restartBtn.setPreferredSize(new Dimension(180, 42));

        restartBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                restartBtn.setBackground(AppConstants.COLOR_PRIMARY_HOVER);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                restartBtn.setBackground(AppConstants.COLOR_PRIMARY);
            }
        });

        restartBtn.addActionListener(e -> mainFrame.restartQuiz());

        panel.add(exitBtn);
        panel.add(restartBtn);
        return panel;
    }

    /**
     * Populates the result screen with data from the completed QuizResult.
     */
    public void displayResult(QuizResult result) {
        if (result == null) return;

        scoreBannerLabel.setText(String.format("Final Score: %d / %d  (%.1f%%)  •  Grade: %s",
                result.getScore(), result.getTotalQuestions(), result.getPercentage(), result.getPerformanceGrade()));
        feedbackLabel.setText(result.getFeedbackMessage());

        // Update Stats Grid
        statsPanel.removeAll();
        statsPanel.add(createStatCard("Total", String.valueOf(result.getTotalQuestions()), AppConstants.COLOR_SECONDARY, new Color(241, 245, 249)));
        statsPanel.add(createStatCard("Correct", String.valueOf(result.getCorrectCount()), AppConstants.COLOR_SUCCESS, AppConstants.COLOR_SUCCESS_BG));
        statsPanel.add(createStatCard("Incorrect", String.valueOf(result.getIncorrectCount()), AppConstants.COLOR_DANGER, AppConstants.COLOR_DANGER_BG));
        statsPanel.add(createStatCard("Unanswered", String.valueOf(result.getUnansweredCount()), AppConstants.COLOR_WARNING, AppConstants.COLOR_WARNING_BG));
        statsPanel.add(createStatCard("Accuracy", String.format("%.0f%%", result.getPercentage()), AppConstants.COLOR_PRIMARY, new Color(238, 242, 255)));
        statsPanel.revalidate();
        statsPanel.repaint();

        // Populate Question Reviews
        reviewListPanel.removeAll();
        List<UserAnswer> answers = result.getUserAnswers();

        for (int i = 0; i < answers.size(); i++) {
            UserAnswer ans = answers.get(i);
            JPanel itemCard = createReviewItemCard(i + 1, ans);
            reviewListPanel.add(itemCard);
            reviewListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        reviewListPanel.revalidate();
        reviewListPanel.repaint();
    }

    private JPanel createStatCard(String label, String value, Color textColor, Color bgColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(bgColor);
        card.setBorder(new CompoundBorder(
            new LineBorder(textColor, 1, true),
            new EmptyBorder(8, 10, 8, 10)
        ));

        JLabel valLbl = new JLabel(value);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valLbl.setForeground(textColor);
        valLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(AppConstants.COLOR_TEXT_MUTED);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(valLbl);
        card.add(Box.createRigidArea(new Dimension(0, 2)));
        card.add(lbl);
        return card;
    }

    private JPanel createReviewItemCard(int qIndex, UserAnswer ans) {
        Question q = ans.getQuestion();

        JPanel card = new JPanel(new BorderLayout(12, 6));
        card.setBackground(AppConstants.COLOR_CARD_BG);
        card.setBorder(new CompoundBorder(
            new LineBorder(AppConstants.COLOR_CARD_BORDER, 1, true),
            new EmptyBorder(12, 16, 12, 16)
        ));
        card.setMaximumSize(new Dimension(Short.MAX_VALUE, 150));

        // Top line: Question number + Status Pill
        JPanel headerLine = new JPanel(new BorderLayout());
        headerLine.setOpaque(false);

        JLabel qNum = new JLabel("Question " + qIndex + ": " + q.getQuestionText());
        qNum.setFont(AppConstants.FONT_BODY_BOLD);
        qNum.setForeground(AppConstants.COLOR_TEXT_DARK);

        JLabel statusBadge = createStatusBadge(ans.getStatus());

        headerLine.add(qNum, BorderLayout.CENTER);
        headerLine.add(statusBadge, BorderLayout.EAST);

        // Body: User answer vs Correct answer + Explanation
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);

        JLabel userAnsLbl = new JLabel("Your Answer: " + ans.getSelectedAnswerText());
        userAnsLbl.setFont(AppConstants.FONT_BODY);
        if (ans.getStatus() == UserAnswer.Status.CORRECT) {
            userAnsLbl.setForeground(AppConstants.COLOR_SUCCESS);
        } else if (ans.getStatus() == UserAnswer.Status.INCORRECT) {
            userAnsLbl.setForeground(AppConstants.COLOR_DANGER);
        } else {
            userAnsLbl.setForeground(AppConstants.COLOR_WARNING);
        }

        JLabel correctAnsLbl = new JLabel("Correct Answer: " + ans.getCorrectAnswerText());
        correctAnsLbl.setFont(AppConstants.FONT_BODY_BOLD);
        correctAnsLbl.setForeground(AppConstants.COLOR_SUCCESS);

        detailsPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        detailsPanel.add(userAnsLbl);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        detailsPanel.add(correctAnsLbl);

        if (!q.getExplanation().isEmpty()) {
            JLabel expLbl = new JLabel("💡 " + q.getExplanation());
            expLbl.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            expLbl.setForeground(AppConstants.COLOR_TEXT_MUTED);
            detailsPanel.add(Box.createRigidArea(new Dimension(0, 4)));
            detailsPanel.add(expLbl);
        }

        card.add(headerLine, BorderLayout.NORTH);
        card.add(detailsPanel, BorderLayout.CENTER);

        return card;
    }

    private JLabel createStatusBadge(UserAnswer.Status status) {
        JLabel badge = new JLabel();
        badge.setFont(AppConstants.FONT_BADGE);
        badge.setOpaque(true);

        switch (status) {
            case CORRECT:
                badge.setText("  CORRECT  ");
                badge.setForeground(AppConstants.COLOR_SUCCESS);
                badge.setBackground(AppConstants.COLOR_SUCCESS_BG);
                badge.setBorder(new LineBorder(AppConstants.COLOR_SUCCESS, 1, true));
                break;
            case INCORRECT:
                badge.setText("  INCORRECT  ");
                badge.setForeground(AppConstants.COLOR_DANGER);
                badge.setBackground(AppConstants.COLOR_DANGER_BG);
                badge.setBorder(new LineBorder(AppConstants.COLOR_DANGER, 1, true));
                break;
            case UNANSWERED:
                badge.setText("  UNANSWERED  ");
                badge.setForeground(AppConstants.COLOR_WARNING);
                badge.setBackground(AppConstants.COLOR_WARNING_BG);
                badge.setBorder(new LineBorder(AppConstants.COLOR_WARNING, 1, true));
                break;
        }
        return badge;
    }
}

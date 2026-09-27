package com.codsoft.quiz.ui;

import com.codsoft.quiz.util.AppConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * WelcomePanel
 * 
 * Initial landing screen providing quiz overview, rules, instructions,
 * and the primary call-to-action button to start the quiz.
 */
public class WelcomePanel extends JPanel {

    private final MainFrame mainFrame;

    public WelcomePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(AppConstants.COLOR_BACKGROUND);

        // Center card wrapper
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        // Card Container
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(AppConstants.COLOR_CARD_BG);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppConstants.COLOR_CARD_BORDER, 1, true),
            new EmptyBorder(40, 50, 40, 50)
        ));
        cardPanel.setPreferredSize(new Dimension(680, 540));
        cardPanel.setMaximumSize(new Dimension(680, 540));

        // 1. Tag / Badge
        JLabel badgeLabel = new JLabel("CODSOFT JAVA DEVELOPMENT INTERNSHIP");
        badgeLabel.setFont(AppConstants.FONT_BADGE);
        badgeLabel.setForeground(AppConstants.COLOR_PRIMARY);
        badgeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 2. Main Title
        JLabel titleLabel = new JLabel("Task 4: Quiz Application With Timer");
        titleLabel.setFont(AppConstants.FONT_TITLE);
        titleLabel.setForeground(AppConstants.COLOR_TEXT_DARK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 3. Subtitle
        JLabel subtitleLabel = new JLabel("Evaluate your Core Java, OOP, Collections & Exception Handling skills");
        subtitleLabel.setFont(AppConstants.FONT_BODY);
        subtitleLabel.setForeground(AppConstants.COLOR_TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 4. Rules & Instructions Box
        JPanel rulesPanel = new JPanel();
        rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.Y_AXIS));
        rulesPanel.setBackground(new Color(241, 245, 249)); // Slate 100
        rulesPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
            new EmptyBorder(16, 20, 16, 20)
        ));
        rulesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel rulesHeader = new JLabel("Quiz Rules & Guidelines");
        rulesHeader.setFont(AppConstants.FONT_SECTION);
        rulesHeader.setForeground(AppConstants.COLOR_SECONDARY);
        rulesHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        rulesPanel.add(rulesHeader);
        rulesPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        rulesPanel.add(createRuleItem("• Questions: " + AppConstants.QUIZ_QUESTION_COUNT + " Multiple-Choice Questions covering Java fundamentals."));
        rulesPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        rulesPanel.add(createRuleItem("• Timer: Exactly " + AppConstants.QUESTION_TIME_LIMIT + " seconds allotted per question."));
        rulesPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        rulesPanel.add(createRuleItem("• Timeout: Questions automatically submit as unanswered if the timer expires."));
        rulesPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        rulesPanel.add(createRuleItem("• Scoring: +1 point per correct answer; no negative marking."));
        rulesPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        rulesPanel.add(createRuleItem("• Summary: Complete question-by-question review provided at the end."));

        // 5. Start Quiz Button
        JButton startButton = new JButton("START QUIZ NOW");
        startButton.setFont(AppConstants.FONT_BUTTON);
        startButton.setForeground(AppConstants.COLOR_TEXT_LIGHT);
        startButton.setBackground(AppConstants.COLOR_PRIMARY);
        startButton.setOpaque(true);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startButton.setPreferredSize(new Dimension(240, 48));
        startButton.setMaximumSize(new Dimension(240, 48));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover effect
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startButton.setBackground(AppConstants.COLOR_PRIMARY_HOVER);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButton.setBackground(AppConstants.COLOR_PRIMARY);
            }
        });

        startButton.addActionListener(e -> mainFrame.startQuiz());

        // 6. Footer label
        JLabel footerLabel = new JLabel("Built with Java Swing • Desktop Application");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(AppConstants.COLOR_TEXT_MUTED);
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Assemble Card
        cardPanel.add(badgeLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(titleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        cardPanel.add(subtitleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        cardPanel.add(rulesPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 28)));
        cardPanel.add(startButton);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        cardPanel.add(footerLabel);

        centerWrapper.add(cardPanel);
        add(centerWrapper, BorderLayout.CENTER);
    }

    private JLabel createRuleItem(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppConstants.FONT_BODY);
        label.setForeground(AppConstants.COLOR_TEXT_DARK);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
}

package com.codsoft.quiz.ui;

import com.codsoft.quiz.model.Question;
import com.codsoft.quiz.service.QuizManager;
import com.codsoft.quiz.util.AppConstants;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * QuizPanel
 * 
 * Interactive quiz screen displaying one question at a time,
 * four multiple-choice options within a ButtonGroup,
 * a real-time countdown Swing Timer, and submission controls.
 */
public class QuizPanel extends JPanel {

    private final MainFrame mainFrame;
    private final QuizManager quizManager;

    // Timer components
    private Timer countdownTimer;
    private int secondsRemaining;

    // UI Components
    private JLabel titleLabel;
    private JLabel progressBadgeLabel;
    private JLabel topicBadgeLabel;
    private JLabel timerLabel;
    private JProgressBar timerProgressBar;

    private JLabel questionNumberLabel;
    private JTextArea questionTextArea;

    private JRadioButton[] optionRadios;
    private ButtonGroup optionButtonGroup;
    private JPanel[] optionPanels;

    private JLabel feedbackLabel;
    private JButton submitButton;

    public QuizPanel(MainFrame mainFrame, QuizManager quizManager) {
        this.mainFrame = mainFrame;
        this.quizManager = quizManager;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 16));
        setBackground(AppConstants.COLOR_BACKGROUND);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // 1. TOP HEADER PANEL
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // 2. CENTER QUESTION & OPTIONS CARD
        JPanel centerCard = createCenterCard();
        add(centerCard, BorderLayout.CENTER);

        // 3. BOTTOM CONTROL PANEL
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);

        // Title and Topic Row
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);

        titleLabel = new JLabel("JAVA QUIZ APPLICATION");
        titleLabel.setFont(AppConstants.FONT_TITLE);
        titleLabel.setForeground(AppConstants.COLOR_SECONDARY);

        topicBadgeLabel = new JLabel("Topic: Java Fundamentals");
        topicBadgeLabel.setFont(AppConstants.FONT_BADGE);
        topicBadgeLabel.setForeground(AppConstants.COLOR_PRIMARY);
        topicBadgeLabel.setOpaque(true);
        topicBadgeLabel.setBackground(new Color(238, 242, 255)); // Indigo 50
        topicBadgeLabel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(199, 210, 254), 1, true),
            new EmptyBorder(4, 10, 4, 10)
        ));

        titleRow.add(titleLabel, BorderLayout.WEST);
        titleRow.add(topicBadgeLabel, BorderLayout.EAST);

        // Status & Timer Bar Row
        JPanel barRow = new JPanel(new BorderLayout(15, 0));
        barRow.setOpaque(false);

        progressBadgeLabel = new JLabel("Question 1 of 10");
        progressBadgeLabel.setFont(AppConstants.FONT_SECTION);
        progressBadgeLabel.setForeground(AppConstants.COLOR_TEXT_DARK);

        // Timer container
        JPanel timerContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        timerContainer.setOpaque(false);

        timerLabel = new JLabel("⏱ Time Remaining: " + AppConstants.QUESTION_TIME_LIMIT + "s");
        timerLabel.setFont(AppConstants.FONT_TIMER);
        timerLabel.setForeground(AppConstants.COLOR_TIMER_NORMAL);

        timerContainer.add(timerLabel);

        barRow.add(progressBadgeLabel, BorderLayout.WEST);
        barRow.add(timerContainer, BorderLayout.EAST);

        // Timer Progress Bar
        timerProgressBar = new JProgressBar(0, AppConstants.QUESTION_TIME_LIMIT);
        timerProgressBar.setValue(AppConstants.QUESTION_TIME_LIMIT);
        timerProgressBar.setForeground(AppConstants.COLOR_PRIMARY);
        timerProgressBar.setBackground(new Color(226, 232, 240));
        timerProgressBar.setBorderPainted(false);
        timerProgressBar.setPreferredSize(new Dimension(panel.getWidth(), 6));

        // Group into top panel
        JPanel headerStack = new JPanel();
        headerStack.setLayout(new BoxLayout(headerStack, BoxLayout.Y_AXIS));
        headerStack.setOpaque(false);
        headerStack.add(titleRow);
        headerStack.add(Box.createRigidArea(new Dimension(0, 10)));
        headerStack.add(barRow);
        headerStack.add(Box.createRigidArea(new Dimension(0, 8)));
        headerStack.add(timerProgressBar);

        panel.add(headerStack, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCenterCard() {
        JPanel card = new JPanel(new BorderLayout(0, 16));
        card.setBackground(AppConstants.COLOR_CARD_BG);
        card.setBorder(new CompoundBorder(
            new LineBorder(AppConstants.COLOR_CARD_BORDER, 1, true),
            new EmptyBorder(24, 28, 24, 28)
        ));

        // Question header & text
        JPanel questionHeaderPanel = new JPanel(new BorderLayout(0, 8));
        questionHeaderPanel.setOpaque(false);

        questionNumberLabel = new JLabel("QUESTION 1");
        questionNumberLabel.setFont(AppConstants.FONT_BADGE);
        questionNumberLabel.setForeground(AppConstants.COLOR_PRIMARY);

        questionTextArea = new JTextArea();
        questionTextArea.setFont(AppConstants.FONT_QUESTION);
        questionTextArea.setForeground(AppConstants.COLOR_TEXT_DARK);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setEditable(false);
        questionTextArea.setOpaque(false);
        questionTextArea.setFocusable(false);
        questionTextArea.setBorder(null);

        questionHeaderPanel.add(questionNumberLabel, BorderLayout.NORTH);
        questionHeaderPanel.add(questionTextArea, BorderLayout.CENTER);
        card.add(questionHeaderPanel, BorderLayout.NORTH);

        // Options Container
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(4, 1, 0, 10));
        optionsPanel.setOpaque(false);

        optionRadios = new JRadioButton[4];
        optionPanels = new JPanel[4];
        optionButtonGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            final int index = i;
            JPanel optPanel = new JPanel(new BorderLayout(10, 0));
            optPanel.setBackground(new Color(248, 250, 252));
            optPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(10, 14, 10, 14)
            ));
            optPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JRadioButton radio = new JRadioButton();
            radio.setOpaque(false);
            radio.setFont(AppConstants.FONT_BODY);
            radio.setForeground(AppConstants.COLOR_TEXT_DARK);
            radio.setFocusPainted(false);

            // Clicking the panel selects the radio button
            optPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    radio.setSelected(true);
                    updateOptionHighlight(index);
                }
            });

            radio.addActionListener(e -> updateOptionHighlight(index));

            optionButtonGroup.add(radio);
            optPanel.add(radio, BorderLayout.CENTER);

            optionRadios[i] = radio;
            optionPanels[i] = optPanel;
            optionsPanel.add(optPanel);
        }

        card.add(optionsPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setOpaque(false);

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(AppConstants.FONT_BODY_BOLD);
        feedbackLabel.setForeground(AppConstants.COLOR_DANGER);

        submitButton = new JButton("SUBMIT ANSWER");
        submitButton.setFont(AppConstants.FONT_BUTTON);
        submitButton.setForeground(AppConstants.COLOR_TEXT_LIGHT);
        submitButton.setBackground(AppConstants.COLOR_PRIMARY);
        submitButton.setOpaque(true);
        submitButton.setBorderPainted(false);
        submitButton.setFocusPainted(false);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.setPreferredSize(new Dimension(200, 44));

        submitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (submitButton.isEnabled()) {
                    submitButton.setBackground(AppConstants.COLOR_PRIMARY_HOVER);
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (submitButton.isEnabled()) {
                    submitButton.setBackground(AppConstants.COLOR_PRIMARY);
                }
            }
        });

        submitButton.addActionListener(e -> handleSubmitClicked());

        bottomPanel.add(feedbackLabel, BorderLayout.WEST);
        bottomPanel.add(submitButton, BorderLayout.EAST);

        return bottomPanel;
    }

    /**
     * Updates visual border and background when an option is selected.
     */
    private void updateOptionHighlight(int selectedIdx) {
        feedbackLabel.setText(" ");
        for (int i = 0; i < 4; i++) {
            if (i == selectedIdx) {
                optionPanels[i].setBackground(new Color(238, 242, 255)); // Indigo 50
                optionPanels[i].setBorder(new CompoundBorder(
                    new LineBorder(AppConstants.COLOR_PRIMARY, 2, true),
                    new EmptyBorder(9, 13, 9, 13)
                ));
            } else {
                optionPanels[i].setBackground(new Color(248, 250, 252));
                optionPanels[i].setBorder(new CompoundBorder(
                    new LineBorder(new Color(226, 232, 240), 1, true),
                    new EmptyBorder(10, 14, 10, 14)
                ));
            }
        }
    }

    /**
     * Resets option styles and clears selection for a new question.
     */
    private void resetOptionHighlights() {
        optionButtonGroup.clearSelection();
        for (int i = 0; i < 4; i++) {
            optionPanels[i].setBackground(new Color(248, 250, 252));
            optionPanels[i].setBorder(new CompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(10, 14, 10, 14)
            ));
        }
    }

    /**
     * Loads the current question from QuizManager and initializes the countdown timer.
     */
    public void displayCurrentQuestion() {
        Question current = quizManager.getCurrentQuestion();
        if (current == null) {
            return;
        }

        int qNum = quizManager.getCurrentQuestionNumber();
        int total = quizManager.getTotalQuestions();

        // Update labels
        progressBadgeLabel.setText("Question " + qNum + " of " + total);
        topicBadgeLabel.setText("Topic: " + current.getTopic());
        questionNumberLabel.setText("QUESTION " + qNum + " OF " + total);
        questionTextArea.setText(current.getQuestionText());

        // Update options
        resetOptionHighlights();
        for (int i = 0; i < 4; i++) {
            String letter = Question.getOptionLetter(i);
            optionRadios[i].setText("   " + letter + ")  " + current.getOption(i));
        }

        feedbackLabel.setText(" ");
        submitButton.setEnabled(true);

        // Start countdown timer for this question
        startQuestionTimer();
    }

    /**
     * Initializes and starts the Java Swing countdown Timer.
     * Guaranteed to stop any existing timer to prevent thread leaks or dual execution.
     */
    private void startQuestionTimer() {
        stopTimer(); // Ensure previous timer is halted

        secondsRemaining = AppConstants.QUESTION_TIME_LIMIT;
        updateTimerDisplay();

        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsRemaining--;
                updateTimerDisplay();

                if (secondsRemaining <= 0) {
                    stopTimer();
                    handleTimeout();
                }
            }
        });
        countdownTimer.setRepeats(true);
        countdownTimer.start();
    }

    /**
     * Updates the countdown display text and progress bar color.
     */
    private void updateTimerDisplay() {
        timerLabel.setText("⏱ Time Remaining: " + secondsRemaining + "s");
        timerProgressBar.setValue(secondsRemaining);

        if (secondsRemaining <= 5) {
            timerLabel.setForeground(AppConstants.COLOR_TIMER_URGENT);
            timerProgressBar.setForeground(AppConstants.COLOR_TIMER_URGENT);
        } else {
            timerLabel.setForeground(AppConstants.COLOR_TIMER_NORMAL);
            timerProgressBar.setForeground(AppConstants.COLOR_PRIMARY);
        }
    }

    /**
     * Stops and nullifies the active Swing Timer safely.
     */
    public void stopTimer() {
        if (countdownTimer != null) {
            countdownTimer.stop();
            countdownTimer = null;
        }
    }

    /**
     * Handles manual answer submission by user.
     */
    private void handleSubmitClicked() {
        int selectedIndex = getSelectedOptionIndex();

        if (selectedIndex == -1) {
            feedbackLabel.setText("⚠️ Please select an option before submitting!");
            return;
        }

        // Valid answer chosen - stop timer and record
        stopTimer();
        feedbackLabel.setText(" ");
        quizManager.submitAnswer(selectedIndex);
        moveToNextOrResult();
    }

    /**
     * Handles automatic timeout when countdown hits 0 seconds.
     */
    private void handleTimeout() {
        stopTimer();
        feedbackLabel.setText("⏰ Time expired! Moving to next question...");
        quizManager.recordTimeout();

        // Brief delay for visual feedback, then proceed
        Timer transitionDelay = new Timer(500, e -> {
            ((Timer) e.getSource()).stop();
            moveToNextOrResult();
        });
        transitionDelay.setRepeats(false);
        transitionDelay.start();
    }

    /**
     * Checks if quiz finished; transitions to Result Screen or loads next question.
     */
    private void moveToNextOrResult() {
        if (quizManager.isQuizFinished()) {
            mainFrame.showResultScreen(quizManager.calculateResult());
        } else {
            displayCurrentQuestion();
        }
    }

    /**
     * Returns 0-based index of selected radio button, or -1 if none is selected.
     */
    private int getSelectedOptionIndex() {
        for (int i = 0; i < 4; i++) {
            if (optionRadios[i].isSelected()) {
                return i;
            }
        }
        return -1;
    }
}

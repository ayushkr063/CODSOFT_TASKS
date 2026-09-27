package com.codsoft.quiz.util;

import java.awt.Color;
import java.awt.Font;

/**
 * AppConstants
 * 
 * Centralized configuration constants for the Quiz Application.
 * Allows easy modification of application-wide parameters such as timer duration,
 * fonts, colors, and layout dimensions.
 */
public final class AppConstants {

    // Private constructor to prevent instantiation (Utility Class pattern)
    private AppConstants() {
        throw novelUnsupportedOperationException("AppConstants cannot be instantiated.");
    }

    private static UnsupportedOperationException novelUnsupportedOperationException(String msg) {
        return new UnsupportedOperationException(msg);
    }

    // =========================================================================
    // APPLICATION SETTINGS
    // =========================================================================
    public static final String APP_TITLE = "CodSoft Quiz Application - Java Mastery";
    public static final int WINDOW_WIDTH = 860;
    public static final int WINDOW_HEIGHT = 680;

    /**
     * CONFIGURABLE TIMER DURATION
     * Change this constant to modify the countdown time (in seconds) per question.
     * Default: 20 seconds.
     */
    public static final int QUESTION_TIME_LIMIT = 20;

    /**
     * Number of questions to be included in a single quiz session.
     */
    public static final int QUIZ_QUESTION_COUNT = 10;

    // =========================================================================
    // UI COLOR PALETTE (Modern Slate & Indigo Theme)
    // =========================================================================
    public static final Color COLOR_PRIMARY = new Color(79, 70, 229);        // Indigo 600
    public static final Color COLOR_PRIMARY_HOVER = new Color(67, 56, 202);  // Indigo 700
    public static final Color COLOR_SECONDARY = new Color(30, 41, 59);       // Slate 800
    public static final Color COLOR_BACKGROUND = new Color(248, 250, 252);   // Slate 50
    public static final Color COLOR_CARD_BG = new Color(255, 255, 255);      // Pure White
    public static final Color COLOR_CARD_BORDER = new Color(226, 232, 240);  // Slate 200

    public static final Color COLOR_TEXT_DARK = new Color(15, 23, 42);       // Slate 900
    public static final Color COLOR_TEXT_MUTED = new Color(100, 116, 139);   // Slate 500
    public static final Color COLOR_TEXT_LIGHT = new Color(255, 255, 255);   // White

    public static final Color COLOR_SUCCESS = new Color(22, 163, 74);        // Emerald 600
    public static final Color COLOR_SUCCESS_BG = new Color(240, 253, 244);   // Emerald 50
    public static final Color COLOR_DANGER = new Color(220, 38, 38);         // Red 600
    public static final Color COLOR_DANGER_BG = new Color(254, 242, 242);    // Red 50
    public static final Color COLOR_WARNING = new Color(217, 119, 6);        // Amber 600
    public static final Color COLOR_WARNING_BG = new Color(254, 243, 199);   // Amber 50
    public static final Color COLOR_TIMER_NORMAL = new Color(14, 165, 233);   // Sky 500
    public static final Color COLOR_TIMER_URGENT = new Color(225, 29, 72);   // Rose 600

    // =========================================================================
    // TYPOGRAPHY
    // =========================================================================
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_QUESTION = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_TIMER = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_BADGE = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);
}

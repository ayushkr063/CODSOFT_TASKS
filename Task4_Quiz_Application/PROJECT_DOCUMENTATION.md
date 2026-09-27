# PROJECT DOCUMENTATION
## CodSoft Java Development Internship — Task 4
### Quiz Application with Timer

---

### 1. Project Title
**Desktop Quiz Application with Automated Countdown Timer**  
*(CodSoft Java Development Internship - Task 4)*

---

### 2. Problem Statement
Traditional computer-based test systems often lack an intuitive desktop user interface, real-time feedback mechanisms, and time-bound question enforcement. Students and learners need a focused, distraction-free desktop application to assess their core technical concepts under realistic time constraints, followed by instant score calculation and an educational review of their mistakes.

---

### 3. Objective
The primary objective of this project is to develop a robust, clean, and interactive desktop Quiz Application in Java using Java Swing. The system:
- Presents multiple-choice questions one at a time.
- Limits the answering time for each question with an active countdown timer.
- Evaluates submitted answers accurately and handles timeouts gracefully.
- Displays an aggregated performance report with a question-by-question breakdown and conceptual explanations.
- Demonstrates sound Object-Oriented Programming (OOP) principles, modular architecture, and thread-safe GUI development.

---

### 4. Target Users
- **Students & Learners**: Preparing for technical interviews and university examinations on Core Java and OOP.
- **Instructors & Evaluators**: Assessing basic programming competencies in a timed environment.
- **Internship Evaluators**: Reviewing clean code practices, GUI design, event handling, and OOP implementation in Java.

---

### 5. Features
1. **Interactive Graphical User Interface (GUI)**:
   - Polished Slate & Indigo color palette.
   - Clean typography using Segoe UI.
   - CardLayout for seamless transition between screens without opening multiple windows.
2. **Dynamic Question Presentation**:
   - Single-question display with clear numbering (`Question X of 10`).
   - Four mutually exclusive choices managed via `ButtonGroup` and `JRadioButton`.
   - Visual card highlighting when an option is selected.
3. **Automated Countdown Timer**:
   - 20-second per-question limit using `javax.swing.Timer`.
   - Real-time countdown clock and visual progress bar.
   - Color shifts to urgent crimson when remaining time is 5 seconds or less.
   - Automated submission and timeout handling when timer hits zero.
4. **Input Validation**:
   - Protects against accidental blank submissions with polite warning messages.
5. **Detailed Result & Analytics**:
   - Total questions, correct count, incorrect count, unanswered count, score, percentage, and grade.
   - Itemized scrollable question review detailing chosen answers, correct answers, status badges, and explanatory notes.
6. **Quiz Reset & Restart**:
   - Full state reset capability for repeated testing.

---

### 6. Functional Requirements
- **FR1 (Question Repository)**: Store at least 10 original Java MCQs with options and designated correct answers.
- **FR2 (Question Navigation)**: Display exactly one question at a time in sequence.
- **FR3 (Timer Management)**: Start a 20-second countdown upon displaying a question, decreasing by 1 second every 1000ms.
- **FR4 (Answer Recording)**: Allow user selection of a single option and record their answer upon clicking Submit.
- **FR5 (Timeout Enforcement)**: Automatically advance to the next question and record the question as *Unanswered* if the countdown expires.
- **FR6 (Score Aggregation)**: Compute total score (+1 per correct answer, 0 for incorrect or unanswered).
- **FR7 (Result Display)**: Transition to a comprehensive result screen upon completion of the final question.
- **FR8 (Session Restart)**: Enable restarting the quiz with fresh questions and zero residual state.

---

### 7. Non-Functional Requirements
- **NFR1 (Performance & Responsiveness)**: Instantaneous transitions without freezing; all Swing operations execute on the Event Dispatch Thread (EDT).
- **NFR2 (Reliability & Robustness)**: Complete absence of unhandled exceptions or `NullPointerException` risks.
- **NFR3 (Maintainability)**: Decoupled architecture separating business logic (`QuizManager`) from presentation views (`QuizPanel`, `ResultPanel`).
- **NFR4 (Configurability)**: Centralized configuration constants for timer limit, question count, fonts, and colors.
- **NFR5 (Portability)**: Runs out-of-the-box on any system with Java 17+ (Windows, macOS, Linux) without third-party libraries.

---

### 8. Technology Stack
- **Language**: Java Standard Edition (SE) 17 / 21
- **UI Toolkit**: Java Swing & AWT (`JFrame`, `JPanel`, `CardLayout`, `JRadioButton`, `ButtonGroup`, `JButton`, `JProgressBar`, `JScrollPane`, `Timer`)
- **Build Tools**: Standard `javac` compiler and `java` runtime
- **Version Control**: Git

---

### 9. System Architecture

The application adopts a **Model-Service-View** architectural pattern:

```
[ Data Source ]  ---> QuestionBank (Curated Java Questions)
                              |
                              v
[ Model Layer ]  ---> Question, UserAnswer, QuizResult
                              |
                              v
[ Service Layer ] ---> QuizManager (State, Answers, Calculations)
                              ^
                              |
[ Presentation ] ---> MainFrame (CardLayout Coordinator)
                        ├── WelcomePanel (Instructions)
                        ├── QuizPanel (Questions, ButtonGroup, Swing Timer)
                        └── ResultPanel (Scorecard, JScrollPane Review)
```

---

### 10. Module Description

| Module / Package | Purpose |
| :--- | :--- |
| `com.codsoft.quiz` | Application entry point (`Main.java`). Initializes Look & Feel and launches GUI on EDT. |
| `com.codsoft.quiz.model` | Entity classes (`Question`, `UserAnswer`, `QuizResult`) encapsulating quiz data. |
| `com.codsoft.quiz.service` | Business logic service (`QuizManager`) handling session progression and scoring. |
| `com.codsoft.quiz.ui` | Graphical Swing panels (`MainFrame`, `WelcomePanel`, `QuizPanel`, `ResultPanel`). |
| `com.codsoft.quiz.util` | Global constants (`AppConstants`) and question data source (`QuestionBank`). |

---

### 11. Class Description

1. **`AppConstants`**:
   - Defines application title, dimensions, `QUESTION_TIME_LIMIT = 20`, color tokens, and font styles.
   - Private constructor prevents instantiation.
2. **`Question`**:
   - Represents an MCQ with `id`, `questionText`, immutable `List<String> options`, `correctAnswerIndex`, `topic`, and `explanation`.
   - Provides validation in constructor and helper methods like `isCorrect(int index)` and `getOptionLetter(int index)`.
3. **`UserAnswer`**:
   - Stores user response for a question, tracking selected option index, timeout flag, and resulting `Status` (`CORRECT`, `INCORRECT`, `UNANSWERED`).
4. **`QuizResult`**:
   - Aggregates user answers to compute `score`, `correctCount`, `incorrectCount`, `unansweredCount`, `percentage`, and qualitative performance feedback.
5. **`QuestionBank`**:
   - Static factory providing 12 original Java MCQs covering OOP, Collections, JVM, and Exception Handling.
6. **`QuizManager`**:
   - State manager tracking active question index, answers list, and quiz completion status.
7. **`MainFrame`**:
   - Top-level `JFrame` utilizing `CardLayout` to switch between screens cleanly.
8. **`WelcomePanel`**:
   - Displays instructions, quiz rules, and the "Start Quiz" trigger button.
9. **`QuizPanel`**:
   - Contains the live question card, radio button group, countdown timer, progress bar, and submit button.
10. **`ResultPanel`**:
    - Renders the final scorecard and a scrollable list of question reviews with explanations.

---

### 12. Application Workflow

1. **Initialization**: `Main.java` applies system Look & Feel and invokes `SwingUtilities.invokeLater()` to display `MainFrame`.
2. **Welcome Screen**: User reads the rules and clicks "Start Quiz Now".
3. **Session Start**: `MainFrame` requests `QuizManager.startNewQuiz()`, resetting counters and loading questions.
4. **Question Loop**:
   - Question details and choices are bound to UI components.
   - Timer starts counting down from 20 seconds.
   - **Case A (User Submits Answer)**: Timer stops immediately, user's selection is validated, and `QuizManager.submitAnswer()` is called.
   - **Case B (Timer Hits 0)**: Timer stops, `QuizManager.recordTimeout()` marks the question as unanswered, and system advances automatically.
5. **Completion**: When the final question is handled, `QuizManager.calculateResult()` aggregates statistics and `MainFrame` switches to `ResultPanel`.
6. **Review & Restart**: User inspects their score and question reviews, with options to **Restart Quiz** or **Exit Application**.

---

### 13. Timer Logic

The countdown mechanism is driven by `javax.swing.Timer`:
```java
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
```

**Key Safety Measures**:
- **Zero Race Conditions**: Submitting an answer invokes `stopTimer()` synchronously before processing, preventing delayed tick events from interfering.
- **Resource Management**: Any screen transition or restart triggers `stopTimer()`, nullifying references to prevent timer leaks.
- **EDT Compatibility**: Because `javax.swing.Timer` invokes its listener on the EDT, all UI updates (progress bar, label text, color shifts) are inherently thread-safe.

---

### 14. OOP Concepts

- **Encapsulation**: Strict private member variables with public accessors and defensive copying of collections.
- **Single Responsibility Principle (SRP)**: Each class is dedicated to one single concern (e.g., `QuizManager` handles logic; `QuizPanel` handles graphics).
- **Composition**: `MainFrame` is composed of `WelcomePanel`, `QuizPanel`, and `ResultPanel`.
- **Inheritance**: Custom panels extend `JPanel`; `MainFrame` extends `JFrame`.
- **Polymorphism**: Standard Swing event listener interfaces (`ActionListener`, `MouseAdapter`) dispatched polymorphically.

---

### 15. Testing
The application underwent automated and manual testing:
- **Unit & Logic Verification**: `QuizAppTest` executed 49 assertions covering model validation, scoring calculations, timeout handling, and restart mechanisms with a 100% pass rate.
- **GUI Smoke Test**: `GuiSmokeTest` verified safe component instantiation and lifecycle navigation on the Swing EDT.
- Detailed test cases and verification logs are documented in `TESTING.md`.

---

### 16. Limitations
- Quiz questions are bundled statically in memory; question additions require editing `QuestionBank.java`.
- Graphical desktop execution requires an environment with an active display server (standard desktop JDK).
- User progress is not saved to persistent disk storage across application restarts.

---

### 17. Future Scope
- **File / Database Storage**: Loading dynamic question pools from JSON files or an embedded SQLite database.
- **Leaderboard & History**: Persisting user high scores and historical quiz attempts.
- **Audio Effects**: Adding subtle audio cues for countdown ticks and answer submissions.
- **Category Selection**: Allowing users to choose specific categories (e.g., Only OOP, Only Collections).

---

### 18. Conclusion
The **CodSoft Quiz Application with Timer** successfully fulfills all assigned internship objectives. It showcases modern Java Swing GUI design, clean architecture, automated countdown timers, and rigorous object-oriented design. The project serves as an exemplary academic and practical submission for the CodSoft Java Development Internship.

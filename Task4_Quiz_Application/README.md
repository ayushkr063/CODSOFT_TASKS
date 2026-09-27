# Quiz Application with Timer

A clean, modern, and interactive desktop Quiz Application built with **Java** and **Java Swing**, featuring an automated countdown timer per question, dynamic score calculation, and a comprehensive question-by-question review screen.

Developed as part of the **CodSoft Java Development Internship (Task 4)**.

---

## Project Overview

The **Quiz Application with Timer** is a desktop application developed to evaluate and reinforce core Java programming concepts through timed multiple-choice questions. It provides an intuitive graphical interface where questions are presented one at a time with four choices. Each question is governed by an automated countdown timer. The user can submit their answers or allow the question to automatically submit upon timeout.

At the conclusion of the quiz, users receive an itemized performance breakdown with exact scores, percentages, performance grades, and an educational review section displaying their chosen answers alongside the correct answers and conceptual explanations.

---

## CodSoft Internship Task 4

This project completely satisfies all functional and non-functional requirements specified for **CodSoft Java Development Internship – Task 4**:

1. **Quiz Questions and Options**: Stored in encapsulated `Question` models with 4 distinct choices and validated correct answers.
2. **Timer**: Integrated Java `javax.swing.Timer` counting down for each question (default 20 seconds, fully configurable).
3. **Question Display**: Clean presentation of one question at a time with modern radio button styling.
4. **Answer Submission**: Seamless option selection with validation preventing blank submissions, moving automatically to subsequent questions.
5. **Score Calculation**: Accurate tally of correct answers, incorrect answers, and timeouts.
6. **Result Screen**: Final score, percentage, statistics grid, and complete question-by-question review with explanatory notes.

---

## Features

- **Professional Swing GUI**: Designed using a modern Slate & Indigo color palette, card layouts, clean typography, and responsive borders.
- **CardLayout Navigation**: Smooth transitions between the **Welcome Screen**, **Active Quiz**, and **Result Screen** without closing or reopening windows.
- **Real-Time Countdown Timer**:
  - Dynamically updates remaining seconds.
  - Interactive visual progress bar.
  - Visual urgency indicator (changes to urgent red when 5 seconds or fewer remain).
  - Automatically submits and marks questions as *Unanswered / Timed Out* when the countdown hits zero.
- **Selection Safety**: Alerts the user if they click Submit without selecting an answer.
- **Comprehensive Scorecard**:
  - Total Questions, Correct, Incorrect, and Unanswered counts.
  - Percentage calculation with letter grade evaluation (e.g., *Very Good (A+)*).
- **Educational Review**: Every question is reviewed at the end, displaying what the user answered, what the correct answer was, and a helpful concept explanation.
- **State Reset & Restart**: Users can restart the quiz at any time with fresh questions and zero memory leaks.

---

## Technologies Used

- **Language**: Java (JDK 17 or higher recommended; verified on Java 21)
- **GUI Toolkit**: Java Swing (`JFrame`, `JPanel`, `CardLayout`, `JRadioButton`, `ButtonGroup`, `JButton`, `JProgressBar`, `JScrollPane`, `Timer`)
- **Design Pattern**: Model-View-Service (decoupled business logic from Swing presentation layer)
- **Architecture**: Pure Object-Oriented Programming (OOP) using standard Java SE libraries (no external frameworks or heavy dependencies required).

---

## Project Structure

```
QuizApplication/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── codsoft/
│   │               └── quiz/
│   │                   ├── Main.java              # Application entry point with EDT safety
│   │                   ├── model/
│   │                   │   ├── Question.java        # Immutable Question entity
│   │                   │   ├── UserAnswer.java      # Captures user selection & timeout status
│   │                   │   └── QuizResult.java      # Score aggregation & statistics
│   │                   ├── service/
│   │                   │   └── QuizManager.java     # Business logic & quiz session lifecycle
│   │                   ├── ui/
│   │                   │   ├── MainFrame.java       # Top-level window with CardLayout
│   │                   │   ├── WelcomePanel.java    # Welcome & instructions screen
│   │                   │   ├── QuizPanel.java       # Interactive question & timer screen
│   │                   │   └── ResultPanel.java     # Final score & itemized review screen
│   │                   └── util/
│   │                       ├── AppConstants.java    # Global config (time limit, colors, fonts)
│   │                       └── QuestionBank.java    # Curated repository of Java MCQs
│   └── test/
│       └── java/
│           └── com/
│               └── codsoft/
│                   └── quiz/
│                       ├── QuizAppTest.java         # Automated verification test suite
│                       └── GuiSmokeTest.java        # Swing EDT smoke test
├── .gitignore                                       # Git ignore rules for Java projects
├── README.md                                        # Project overview and quick start guide
├── PROJECT_DOCUMENTATION.md                         # Detailed 18-section technical documentation
├── TESTING.md                                       # Comprehensive test scenarios and results
├── VIVA_QUESTIONS.md                                # 15+ Viva voce questions and explanations
└── run.sh                                           # Quick compilation & launch script
```

---

## How the Application Works

```
                     +----------------------+
                     |    Application Start |
                     +----------+-----------+
                                |
                                v
                     +----------------------+
                     |    Welcome Screen    |
                     |  (Rules & Overview)  |
                     +----------+-----------+
                                |  [Start Quiz]
                                v
                +--------------->+----------------------+
                |                |    Display Question  |
                |                |  (Start Swing Timer) |
                |                +----------+-----------+
                |                           |
                |             +-------------+-------------+
                |             |                           |
                |             v (User Submits)            v (Timer = 0s)
                |    +------------------+        +------------------+
                |    | Validate Option  |        |  Record Timeout  |
                |    |  & Record Answer |        |  (Unanswered)    |
                |    +--------+---------+        +--------+---------+
                |             |                           |
                |             +-------------+-------------+
                |                           |
                |                           v
                |                 [Has Next Question?]
                |                     /          \
                |             Yes    /            \  No
                +-------------------+              +----------------+
                                                                    |
                                                                    v
                                                         +--------------------+
                                                         |   Result Screen    |
                                                         | Score, Stats Grid, |
                                                         |   & Detailed Review|
                                                         +---------+----------+
                                                                   |
                                                  +----------------+----------------+
                                                  |                                 |
                                                  v [Restart Quiz]                  v [Exit]
                                            (Resets State)                    (Terminates App)
```

1. **Start**: Application launches on the Swing Event Dispatch Thread (EDT) displaying the `WelcomePanel`.
2. **Quiz Session**: Clicking "Start Quiz Now" initializes `QuizManager` with 10 questions.
3. **Question & Countdown**: For each question, the timer is set to 20 seconds.
4. **Answer or Timeout**:
   - If the user selects an option and clicks **Submit**, the answer is logged and the next question loads immediately.
   - If the user does not select within 20 seconds, a timeout is recorded as *Unanswered* and the system automatically advances.
5. **Completion**: Once all 10 questions are answered, `QuizResult` calculates the scores and displays the `ResultPanel`.
6. **Actions**: The user can review explanations, click **Restart Quiz** to take a fresh quiz, or click **Exit Application**.

---

## Timer Functionality

The countdown timer is implemented using standard `javax.swing.Timer`:

- **Event Dispatch Thread Safety**: The timer fires `ActionEvent` on the Swing Event Dispatch Thread (EDT), ensuring thread-safe GUI updates without synchronization bottlenecks.
- **Accurate 1-Second Ticks**: Ticks every `1000ms`, decrementing the remaining seconds and updating both the numeric label and the `JProgressBar`.
- **Memory & Resource Leak Prevention**: Before loading any new question or transitioning to another screen, `stopTimer()` is invoked to halt and nullify active timers, ensuring no zombie timers run concurrently.
- **Dynamic Styling**: When `secondsRemaining <= 5`, the timer display turns urgent crimson (`#E11D48`).

---

## OOP Concepts Used

1. **Encapsulation**:
   - `Question` maintains private fields (`id`, `questionText`, `options`, `correctAnswerIndex`, `explanation`).
   - Options are exposed through immutable defensive copies (`Collections.unmodifiableList`).
2. **Abstraction**:
   - `QuizManager` exposes high-level domain operations (`startNewQuiz()`, `submitAnswer()`, `recordTimeout()`) while abstracting internal index manipulations and array handling.
3. **Single Responsibility Principle (SRP)**:
   - `Question`: Holds question data.
   - `UserAnswer`: Encapsulates user response for one question.
   - `QuizResult`: Calculates statistics and grades.
   - `QuizManager`: Oversees quiz workflow logic.
   - `MainFrame`, `WelcomePanel`, `QuizPanel`, `ResultPanel`: Handle distinct presentation views.
4. **Polymorphism & Event-Driven Architecture**:
   - Java Swing event listeners (`ActionListener`, `MouseAdapter`) handle button clicks and timer ticks polymorphically.

---

## How to Run

### Prerequisites
- Java Development Kit (JDK 17 or higher) installed.
- Verify installation in your terminal:
  ```bash
  java -version
  javac -version
  ```

### Option 1: Using the Provided Shell Script (macOS / Linux)
In the project root directory:
```bash
chmod +x run.sh
./run.sh
```

### Option 2: Manual Terminal Commands

1. **Compile all Java files**:
   ```bash
   mkdir -p bin
   javac -d bin $(find src/main/java -name "*.java")
   ```

2. **Run the Application**:
   ```bash
   java -cp bin com.codsoft.quiz.Main
   ```

3. **Run the Automated Test Suite**:
   ```bash
   javac -d bin -sourcepath src/main/java:src/test/java $(find src -name "*.java")
   java -cp bin com.codsoft.quiz.QuizAppTest
   ```

---

## How to Change Timer Duration

The timer duration is centrally managed in **`AppConstants.java`**.

To modify the time limit per question:

1. Open `src/main/java/com/codsoft/quiz/util/AppConstants.java`.
2. Locate the constant `QUESTION_TIME_LIMIT`:
   ```java
   /**
    * CONFIGURABLE TIMER DURATION
    * Change this constant to modify the countdown time (in seconds) per question.
    * Default: 20 seconds.
    */
   public static final int QUESTION_TIME_LIMIT = 20; // <-- Change to 15, 30, etc.
   ```
3. Recompile and run the application.

---

## Sample Quiz Topics

The application includes 12 comprehensive questions covering:
- **JVM & Bytecode Architecture**
- **Encapsulation & Access Modifiers**
- **Class Inheritance & `super` keyword**
- **Polymorphism & Method Overriding**
- **Interfaces & Java 8 Default Methods**
- **Exception Handling & `finally` execution**
- **Java Collections Framework (`Set` vs `List`)**
- **Java Keywords (`final`, `static`)**
- **Constructors & Object Initialization**
- **Heap Memory & Garbage Collection**
- **Primitive vs Reference Data Types**

---

## Future Improvements

- Support for loading questions dynamically from an external JSON or CSV file.
- Audio sound effects for timer ticks and correct/incorrect answer submissions.
- High score persistence using local SQLite or file storage.
- Difficulty levels (Easy, Medium, Hard) with variable time limits.

---

## Author

Author: [Your Name]  
CodSoft Java Development Internship  
Task 4 – Quiz Application with Timer

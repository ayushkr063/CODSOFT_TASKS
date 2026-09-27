# VIVA VOCE PREPARATION QUESTIONS & ANSWERS
## CodSoft Java Development Internship — Task 4
### Quiz Application with Timer

---

### Q1: Why did you use Java Swing for this project?
**Answer:**
Java Swing is the standard graphical user interface (GUI) toolkit provided with the Java Standard Edition. It is completely platform-independent, lightweight, and requires no external third-party frameworks. It provides rich built-in components like `JFrame`, `JPanel`, `JButton`, and `JRadioButton`, making it ideal for creating standalone desktop applications that run reliably on any operating system with a JDK.

---

### Q2: What is a `JFrame`?
**Answer:**
`JFrame` is a top-level container window in Java Swing that provides a titled border, minimize, maximize, and close buttons. It acts as the main desktop window holding the entire user interface and containing all child panels and components. In our project, `MainFrame` extends `JFrame`.

---

### Q3: What is a `JPanel`?
**Answer:**
`JPanel` is a generic lightweight container used to group and organize components together within a window. Panels allow us to divide the interface into logical regions (such as headers, option cards, and action bars) and assign custom background colors, borders, and layout managers.

---

### Q4: What is a `JRadioButton` and why was it chosen for options?
**Answer:**
`JRadioButton` is a graphical toggle button designed for multiple-choice scenarios where only one option can be chosen from a set. In our quiz, each question displays four `JRadioButton` items representing choices A, B, C, and D.

---

### Q5: Why do we use a `ButtonGroup` with `JRadioButton`?
**Answer:**
By default, individual `JRadioButton` components are independent and can all be checked simultaneously. A `ButtonGroup` creates a mutual exclusion group: when the user selects one radio button inside the group, all other radio buttons in that same group are automatically deselected.

---

### Q6: How does Java `javax.swing.Timer` work?
**Answer:**
`javax.swing.Timer` is a timer utility specifically designed for Swing applications. Unlike `java.util.Timer`, it fires an `ActionEvent` at regular intervals (such as every 1000 milliseconds) directly on the **Event Dispatch Thread (EDT)**. This ensures that GUI updates—such as updating label text and progress bars—occur safely without multi-threading race conditions.

---

### Q7: Why do we use `SwingUtilities.invokeLater()` in `Main.java`?
**Answer:**
Swing components are **not thread-safe**. All graphical components must be created, modified, and rendered exclusively on the dedicated **Event Dispatch Thread (EDT)**. `SwingUtilities.invokeLater()` places the application startup task onto the AWT event queue to ensure thread-safe initialization and avoid UI flickering or threading deadlocks.

---

### Q8: What is Encapsulation and how did you implement it?
**Answer:**
Encapsulation is the OOP principle of bundling data and methods that manipulate that data into a single unit while restricting direct external access to internal fields. In our `Question` class, variables (`questionText`, `options`, `correctAnswerIndex`) are marked `private final`. Access is provided through public getter methods, and defensive copies (`Collections.unmodifiableList`) are returned so the internal state cannot be modified from the outside.

---

### Q9: What is Inheritance and where is it used in the project?
**Answer:**
Inheritance is an OOP mechanism where a new class derives properties and behaviors from an existing superclass using the `extends` keyword. In our application:
- `MainFrame extends JFrame`
- `WelcomePanel extends JPanel`
- `QuizPanel extends JPanel`
- `ResultPanel extends JPanel`  
This promotes code reuse and allows our custom panels to utilize all native Swing container capabilities.

---

### Q10: What is Polymorphism and where is it demonstrated?
**Answer:**
Polymorphism allows objects to be treated as instances of their parent class or interface, executing different behaviors at runtime. In our project, polymorphism is evident in event handling:
- `ActionListener` and `MouseListener` interfaces are implemented with custom behaviors for button clicks and timer ticks.
- Dynamic method dispatch ensures that the appropriate callback logic executes when events fire.

---

### Q11: How are the quiz questions stored and structured?
**Answer:**
Questions are modeled using an encapsulated `Question` class and stored in memory using `java.util.List<Question>`. The `QuestionBank` utility class initializes and supplies a curated list of questions. When a quiz begins, `QuestionBank.getQuizQuestions()` can shuffle the pool and return the requested number of questions.

---

### Q12: How is the final score calculated?
**Answer:**
When each question is answered or times out, a `UserAnswer` record is created. Upon finishing the quiz, `QuizManager.calculateResult()` constructs a `QuizResult` object. It iterates over the recorded answers:
- If `status == Status.CORRECT`, the score increments by 1.
- If `status == Status.INCORRECT` or `Status.UNANSWERED`, score remains unchanged.
- Percentage is computed as:  
  $$\text{Percentage} = \left(\frac{\text{Correct Answers}}{\text{Total Questions}}\right) \times 100$$

---

### Q13: How is a timeout handled when the timer reaches zero?
**Answer:**
When `secondsRemaining` reaches zero:
1. The `countdownTimer` is immediately stopped (`timer.stop()`).
2. `QuizManager.recordTimeout()` is invoked, creating a `UserAnswer` with option index `-1` and `timedOut = true`.
3. The question status is set to `UNANSWERED`.
4. The system displays a brief expiration notice and advances automatically to the next question or the result screen.

---

### Q14: How does the application transition between questions and screens?
**Answer:**
The top-level `MainFrame` employs a **`CardLayout`** container manager. Screens (`WelcomePanel`, `QuizPanel`, `ResultPanel`) are registered as cards identified by unique keys (`"WELCOME"`, `"QUIZ"`, `"RESULT"`). To navigate, `cardLayout.show(cardsPanel, key)` is invoked. Between questions within the `QuizPanel`, the UI simply repopulates its labels and radio buttons with data from `quizManager.getCurrentQuestion()`.

---

### Q15: How would you integrate a database into this application in the future?
**Answer:**
To add persistent database support:
1. Create a `QuizDAO` (Data Access Object) interface.
2. Use JDBC with SQLite or MySQL to connect to the database.
3. Replace the static `QuestionBank` with SQL queries (`SELECT * FROM questions ORDER BY RAND() LIMIT 10`) to retrieve questions dynamically.
4. Save historical results to a `user_results` table containing user name, date, score, and percentage for a leaderboard feature.

---

### Q16: How do you prevent multiple timers or memory leaks from running simultaneously?
**Answer:**
Before starting any new timer in `QuizPanel`, the `stopTimer()` method is explicitly called. This stops the active `countdownTimer` instance and sets its reference to `null`. Furthermore, whenever the user leaves the quiz screen (such as navigating to the result screen or restarting), `stopTimer()` is invoked to guarantee that background timer threads do not linger in memory.

---

### Q17: What happens if a user clicks Submit without selecting an answer?
**Answer:**
The application validates that an option has been selected before proceeding. If no radio button is selected (`getSelectedOptionIndex() == -1`), the application does not advance; instead, it displays a clear warning message: *"⚠️ Please select an option before submitting!"* while allowing the timer to continue running.

---

### Q18: What is the benefit of separating `QuizManager` from `QuizPanel`?
**Answer:**
This follows the **Single Responsibility Principle (SRP)** and the **Model-View-Controller (MVC)** design pattern. `QuizManager` handles only business logic (state, scoring, questions list), while `QuizPanel` handles only graphical presentation and user input. This separation makes the code modular, easier to maintain, and enables headless unit testing without requiring a GUI.

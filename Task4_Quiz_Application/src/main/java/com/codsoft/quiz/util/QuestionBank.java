package com.codsoft.quiz.util;

import com.codsoft.quiz.model.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * QuestionBank
 * 
 * Provides a curated repository of original multiple-choice questions
 * covering core Java topics: Basics, OOP, Collections, Exceptions, Keywords, etc.
 */
public final class QuestionBank {

    private QuestionBank() {
        // Prevent instantiation
    }

    /**
     * Returns the master list of all available questions in the question bank.
     */
    public static List<Question> getAllQuestions() {
        List<Question> list = new ArrayList<>();

        list.add(new Question(
            1,
            "What is the primary role of the Java Virtual Machine (JVM)?",
            Arrays.asList(
                "To compile Java source code (.java) into bytecode (.class)",
                "To execute Java bytecode by translating it into platform-specific machine code",
                "To design graphical user interfaces for desktop apps",
                "To manage Git version control repositories"
            ),
            1,
            "Java Basics & Architecture",
            "The javac compiler compiles Java code into bytecode, while the JVM executes that bytecode on the underlying operating system."
        ));

        list.add(new Question(
            2,
            "Which technique is used to achieve Encapsulation in Java?",
            Arrays.asList(
                "Declaring all variables public and accessing them directly",
                "Declaring variables as private and providing public getter and setter methods",
                "Using the 'extends' keyword to inherit properties from another class",
                "Overloading methods with different parameter counts"
            ),
            1,
            "Object-Oriented Programming",
            "Encapsulation protects object state by hiding internal data with private access modifiers and providing controlled access via methods."
        ));

        list.add(new Question(
            3,
            "Which keyword is used by a Java subclass to inherit from a superclass?",
            Arrays.asList(
                "implements",
                "inherits",
                "extends",
                "super"
            ),
            2,
            "Inheritance",
            "In Java, class inheritance is declared using the 'extends' keyword. The 'implements' keyword is used for interfaces."
        ));

        list.add(new Question(
            4,
            "Method Overriding in Java is a classic example of which concept?",
            Arrays.asList(
                "Compile-time polymorphism",
                "Runtime (Dynamic) polymorphism",
                "Multithreading concurrency",
                "Memory serialization"
            ),
            1,
            "Polymorphism",
            "Method overriding allows a subclass to provide a specific implementation of a method defined in its superclass, resolved dynamically at runtime."
        ));

        list.add(new Question(
            5,
            "Starting from Java 8, which types of methods can contain a body inside an interface?",
            Arrays.asList(
                "Only private abstract methods",
                "Default and static methods",
                "Final and synchronized methods",
                "Interfaces can never have method bodies"
            ),
            1,
            "Interfaces & Abstraction",
            "Java 8 introduced default and static methods with implementation bodies in interfaces to allow API evolution without breaking backward compatibility."
        ));

        list.add(new Question(
            6,
            "Which block in Java is guaranteed to execute regardless of whether an exception occurs or is caught?",
            Arrays.asList(
                "try",
                "catch",
                "throws",
                "finally"
            ),
            3,
            "Exception Handling",
            "The 'finally' block is executed after try-catch blocks finish, making it ideal for cleanup operations such as closing file streams or network sockets."
        ));

        list.add(new Question(
            7,
            "Which interface in the Java Collections Framework does NOT allow duplicate elements?",
            Arrays.asList(
                "List",
                "Set",
                "Queue",
                "Vector"
            ),
            1,
            "Collections Framework",
            "The Set interface (implemented by HashSet, TreeSet, LinkedHashSet) models a mathematical set and strictly disallows duplicate elements."
        ));

        list.add(new Question(
            8,
            "What is the effect of declaring a class with the 'final' keyword in Java?",
            Arrays.asList(
                "The class cannot be instantiated using the 'new' operator",
                "All methods in the class automatically become abstract",
                "The class cannot be subclassed (inherited) by any other class",
                "The class can only contain static variables"
            ),
            2,
            "Java Keywords",
            "A final class cannot be extended. A prominent example in the standard library is java.lang.String."
        ));

        list.add(new Question(
            9,
            "What is the return type of a constructor in Java?",
            Arrays.asList(
                "void",
                "int",
                "Object",
                "Constructors do not have any return type"
            ),
            3,
            "Constructors",
            "Constructors initialize new objects of a class and do not declare any return type (not even void)."
        ));

        list.add(new Question(
            10,
            "Where are object instances dynamically allocated in memory at runtime in Java?",
            Arrays.asList(
                "Stack Memory",
                "Heap Memory",
                "Instruction Register",
                "Permanent Read-Only ROM"
            ),
            1,
            "Memory Management",
            "All object instances and their member fields are allocated on the Heap memory, which is automatically reclaimed by the Garbage Collector."
        ));

        list.add(new Question(
            11,
            "Which of the following is NOT a primitive data type in Java?",
            Arrays.asList(
                "int",
                "boolean",
                "String",
                "double"
            ),
            2,
            "Java Basics",
            "String is a reference type (a Class provided in java.lang), whereas int, boolean, and double are built-in primitive data types."
        ));

        list.add(new Question(
            12,
            "What does the 'static' keyword signify when applied to a method in Java?",
            Arrays.asList(
                "The method belongs to the class itself rather than to individual instances",
                "The method can be executed only once during the application lifecycle",
                "The method executes automatically in a background daemon thread",
                "The method is strictly immutable and cannot return any value"
            ),
            0,
            "Java Keywords",
            "Static methods belong to the class level and can be invoked directly using the ClassName.methodName() syntax without creating an object instance."
        ));

        return list;
    }

    /**
     * Returns a specified number of questions for a quiz session.
     * Optionally shuffles the questions to ensure unique quiz attempts.
     *
     * @param count   Number of questions requested
     * @param shuffle Whether to randomize question order
     * @return List of Question objects
     */
    public static List<Question> getQuizQuestions(int count, boolean shuffle) {
        List<Question> all = getAllQuestions();
        if (shuffle) {
            Collections.shuffle(all);
        }
        int limit = Math.min(count, all.size());
        return new ArrayList<>(all.subList(0, limit));
    }
}

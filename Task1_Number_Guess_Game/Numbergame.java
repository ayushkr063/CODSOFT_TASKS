
import java.util.Random; // it is use to generate random numbers
import java.util.Scanner; //used to generate Scanner class, which is used for user interaction for with the computer

public class Numbergame {

    public static void playGame(Scanner sc) {
        Random rand = new Random();
        int num = rand.nextInt(100) + 1; //random number rane from(1 to 100)
        int attempts = 10; //how many attempts left
        int attend = 0; //start

        System.out.println("Welcome to the Number Guessing Game!");
        System.out.println("Guess a number between 1 to 100.");
        System.out.println("You have " + attempts + " attempts to Win the game.");

        while (attend < attempts) { //if the number of times we have attempted is less than the number of attempts
            System.out.print("Enter your guessed number: ");

            if (sc.hasNextInt()) { // checking a valid input, to ensure that the user has put an integer
                int guess = sc.nextInt();
                attend++; //incrementing the chances by one

                if (guess < num) {
                    System.out.println("Number is too low! Try again.");
                } else if (guess > num) {
                    System.out.println("Number is too high! Try again.");
                } else {
                    System.out.println("Congratulations! you have guessed the number correctly.");
                    return; //exits the code after guessing the correct output
                }
                System.out.println("Attempts left:" + (attempts - attend)); //updating how many attempts left after each attmpt
            } else {
                System.out.println("Invalid input! Please enter a valid number."); //if number is outside the range invalid
                sc.next(); //if didn't find a valid input, it'll turn into infinite loop
            }
        }
        System.out.println("Oops! You've used all your " + attempts + " attempts. The correct number was: " + num); //If all attempts have been used
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); //Creating scanner object
        do {
            playGame(sc);
            System.out.println("Do you want to play again? (yes/no): ");
            sc.nextLine();
        } while (sc.nextLine().trim().equalsIgnoreCase("Yes")); // reads the user input, remove extra spaces and ignore case sensitivity, like YES and yes, both are allowed, If no then close the loop

        System.out.println("Thank you for playing the Number Guessing Game!");
        sc.close();
    }
}

package canelo;

import java.util.Scanner;

/**
 * Handles user interaction, including printing messages and getting user input.
 * This class manages all input/output operations for the program.
 */
public class Ui {
    private final Scanner scanner;
    static final String LINE = "____________________________________________________________";

    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Prints a start message when the program begins.
     */
    public void printStartMessage() {
        printLine();
        System.out.println("""
                 Hello! I'm Canelo :)
                 What can I do for you?""");
        printLine();
    }

    /**
     * Retrieves user input from the console.
     *
     * @return The command entered by the user.
     */
    public String getUserInput() {
        return scanner.nextLine();
    }

    /**
     * Displays an error message if the tasks fail to load from storage.
     */
    public void showLoadingError() {
        System.out.println("Something went wrong when loading tasks.");
    }

    /**
     * Prints a message indicating that the program is exiting.
     */
    public void printEndMessage() {
        printLine();
        System.out.println("Bye. Hope to see you again soon!");
        printLine();
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    /**
     * Prints a line separator for better readability.
     */
    public void printLine() {
        System.out.println(LINE);
    }

    public void printTaskAdded(Task task, int numTasks) {
        System.out.println("Got it. I've added this task:");
        System.out.println("    [" + task.getTypeIcon() + "][" + task.getStatusIcon()+"] " + task.getDescription());
        System.out.println("Now you have " + numTasks + " tasks in the list.");
    }

    public void printTaskDeleted(Task task, int numTasks) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("    [" + task.getTypeIcon() + "]["+task.getStatusIcon() + "] "+task.getDescription());
        System.out.println("Now you have " + numTasks + " tasks in the list.");
    }
}
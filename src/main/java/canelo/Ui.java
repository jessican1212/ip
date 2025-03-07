package canelo;

import java.util.Scanner;

public class Ui {
    private final Scanner scanner;
    static final String LINE = "____________________________________________________________";

    public Ui() {
        scanner = new Scanner(System.in);
    }

    public void printStartMessage() {
        printLine();
        System.out.println("""
                 Hello! I'm Canelo :)
                 What can I do for you?""");
        printLine();
    }

    public String getUserInput() {
        return scanner.nextLine();
    }

    public void showLoadingError() {
        System.out.println("Something went wrong when loading tasks.");
    }

    public void printEndMessage() {
        printLine();
        System.out.println("Bye. Hope to see you again soon!");
        printLine();
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

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
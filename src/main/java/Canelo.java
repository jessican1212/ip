import java.util.Scanner;

public class Canelo {
    static Scanner input = new Scanner(System.in);
    static int numTasks = 0;
    static Task[] list = new Task[100];

    static final String BY = "/by";
    static final String FROM = "/from";
    static final String TO = "/to";

    static final String LINE = "____________________________________________________________";

    private static void printStartMessage() {
        System.out.println(LINE);
        System.out.println("""
                 Hello! I'm Canelo :)
                 What can I do for you?""");
        System.out.println(LINE);
    }

    private static void printEndMessage() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    private static boolean isValidUserInput(String userInput) {
        return !userInput.isEmpty() && !userInput.equals("bye");
    }

    private static String determineTaskType(String userInput) {
        String[] splitKeyboardInput = userInput.split(" ");
        return switch (splitKeyboardInput[0]) {
            case "list" -> "list";
            case "mark" -> "mark";
            case "unmark" -> "unmark";
            case "todo" -> "todo";
            case "deadline" -> "deadline";
            case "event" -> "event";
            default -> "unknown";
        };
    }

    private static void handleList() {
        for (int i = 0; i < numTasks; i++) {
            int taskNumber = i + 1;
            String typeIcon = list[i].getTypeIcon();
            String statusIcon = list[i].getStatusIcon();
            String description = list[i].getDescription();
            System.out.println(taskNumber + ".[" + typeIcon + "][" + statusIcon + "] " + description);
        }
    }

    private static void handleMark(String userInput) {
        try {
            int taskNumber = Integer.parseInt(userInput.substring(5));
            if (isValidTaskNumber(taskNumber)) {
                Task task = list[taskNumber - 1];
                task.markDone();
                System.out.println("Nice! I've marked this task as done:\n[X] " + task.getDescription());
            } else {
                System.out.println("Invalid task number.");
            }
        } catch (Exception e) {
            System.out.println("Add a valid task number to mark.");
        }
    }

    private static void handleUnmark(String userInput) {
        try {
            int taskNumber = Integer.parseInt(userInput.substring(7));
            if (isValidTaskNumber(taskNumber)) {
                Task task = list[taskNumber - 1];
                task.markNotDone();
                System.out.println("OK, I've marked this task as not done yet:\n[ ] " + task.getDescription());
            } else {
                System.out.println("Invalid task number.");
            }
        } catch (Exception e) {
            System.out.println("Add a task number to unmark.");
        }
    }

    private static void handleDeadline(String userInput) {
        int indexOfBy = userInput.indexOf(BY);
        if (indexOfBy > 9) {
            String deadlineName = userInput.substring(9, indexOfBy - 1);
            String deadlineBy = userInput.substring(indexOfBy + 4);
            Deadline deadline = new Deadline(deadlineName + " (by: " + deadlineBy + ")", deadlineBy);
            addTask(deadline);
            printTaskAdded(deadline);
        } else {
            System.out.println("Submit a Deadline using `deadline taskName /by taskBy` format.");
        }
    }

    private static void handleEvent(String userInput) {
        int indexOfFrom = userInput.indexOf(FROM);
        int indexOfTo = userInput.indexOf(TO);
        if (indexOfFrom > 6 && indexOfTo > indexOfFrom + 6) {
            String eventName = userInput.substring(6, indexOfFrom - 1);
            String eventFrom = userInput.substring(indexOfFrom + 6, indexOfTo - 1);
            String eventTo = userInput.substring(indexOfTo + 4);
            Event event = new Event(eventName + " (from: " + eventFrom + " to: " + eventTo + ")", eventFrom, eventTo);
            addTask(event);
            printTaskAdded(event);
        } else {
            System.out.println("Submit an Event using /from and /to format.");
        }
    }

    private static void handleTodo(String userInput) {
        if (userInput.length() > 5) {
            Task task = new Task(userInput.substring(5));
            addTask(task);
            printTaskAdded(task);
        } else {
            System.out.println("Add a task name to todo.");
        }
    }

    private static void printTaskAdded(Task task) {
        System.out.println("Got it. I've added this task:");
        System.out.println("    [" + task.getTypeIcon() + "][" + task.getStatusIcon()+"] " + task.getDescription());
        System.out.println("Now you have " + numTasks + " tasks in the list.");
    }

    private static void addTask(Task task) {
        list[numTasks] = task;
        numTasks += 1;
    }

    private static boolean isValidTaskNumber(int taskNumber) {
        return taskNumber > 0 && taskNumber <= numTasks;
    }

    public static void main(String[] args) {
        printStartMessage();
        String userInput = input.nextLine();
        while (isValidUserInput(userInput)) {
            System.out.println(LINE);
            String taskType = determineTaskType(userInput);

            switch (taskType) {
            case "list":
                handleList();
                break;
            case "mark":
                handleMark(userInput);
                break;
            case "unmark":
                handleUnmark(userInput);
                break;
            case "todo":
                handleTodo(userInput);
                break;
            case "deadline":
                handleDeadline(userInput);
                break;
            case "event":
                handleEvent(userInput);
                break;
            default:
                System.out.println("Invalid task type.");
            }

            System.out.println(LINE);
            userInput = input.nextLine();
        }
        printEndMessage();
    }
}

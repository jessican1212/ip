package canelo;

import java.util.Scanner;
import java.util.ArrayList;

public class Canelo {
    static Scanner input = new Scanner(System.in);
    static int numTasks = 0;
    static ArrayList<Task> tasks = new ArrayList<>();

    static final String BY = "/by";
    static final String FROM = "/from";
    static final String TO = "/to";

    static final int MINIMUM_TODO_LENGTH = 5;
    static final int MINIMUM_MARK_LENGTH = 5;
    static final int MINIMUM_UNMARK_LENGTH = 7;
    static final int MINIMUM_DEADLINE_LENGTH = 9;
    static final int DEADLINE_BY_LENGTH = 4;
    static final int MINIMUM_EVENT_LENGTH = 6;
    static final int EVENT_TO_LENGTH = 4;
    static final int MINIMUM_DELETE_LENGTH = 7;

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
        return !userInput.equals("bye");
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
            case "delete" -> "delete";
            default -> "unknown";
        };
    }

    private static void handleList() {
        for (int i = 0; i < numTasks; i++) {
            int taskNumber = i + 1;
            String typeIcon = tasks.get(i).getTypeIcon();
            String statusIcon = tasks.get(i).getStatusIcon();
            String description = tasks.get(i).getDescription();
            System.out.println(taskNumber + ".[" + typeIcon + "][" + statusIcon + "] " + description);
        }
    }

    private static void handleMark(String userInput) throws CaneloException {
        if (userInput.length() <= MINIMUM_MARK_LENGTH) {
            throw new CaneloException("Please add a task number to mark.");
        }
        int taskNumber = Integer.parseInt(userInput.substring(MINIMUM_MARK_LENGTH));
        if (isInvalidTaskNumber(taskNumber)) {
            throw new CaneloException("Please input a valid task number between 1 and " + numTasks + ".");
        }
        Task task = tasks.get(taskNumber - 1);
        task.markDone();
        System.out.println("Nice! I've marked this task as done:\n[" + task.getTypeIcon() + "][X] " + task.getDescription());
    }

    private static void handleUnmark(String userInput) throws CaneloException {
        if (userInput.length() <= MINIMUM_UNMARK_LENGTH) {
            throw new CaneloException("Please add a task number to unmark.");
        }
        int taskNumber = Integer.parseInt(userInput.substring(MINIMUM_UNMARK_LENGTH));
        if (isInvalidTaskNumber(taskNumber)) {
            throw new CaneloException("Please input a valid task number.");
        }
        Task task = tasks.get(taskNumber - 1);
        task.markNotDone();
        System.out.println("OK, I've marked this task as not done yet:\n[" + task.getTypeIcon() + "][ ] " + task.getDescription());
    }

    private static void handleDeadline(String userInput) throws CaneloException {
        int indexOfBy = userInput.indexOf(BY);
        if (indexOfBy <= MINIMUM_DEADLINE_LENGTH) {
            throw new CaneloException("Submit a Deadline using `taskName /by taskBy` format.");
        }
        String deadlineName = userInput.substring(MINIMUM_DEADLINE_LENGTH, indexOfBy - 1);
        String deadlineBy = userInput.substring(indexOfBy + DEADLINE_BY_LENGTH);
        Deadline deadline = new Deadline(deadlineName + " (by: " + deadlineBy + ")", deadlineBy);
        addTask(deadline);
        printTaskAdded(deadline);
    }

    private static void handleEvent(String userInput) throws CaneloException {
        int indexOfFrom = userInput.indexOf(FROM);
        int indexOfTo = userInput.indexOf(TO);
        if (indexOfFrom <= MINIMUM_EVENT_LENGTH || indexOfTo <= MINIMUM_EVENT_LENGTH) {
            throw new CaneloException("Submit an Event using /from and /to format.");
        }
        String eventName = userInput.substring(MINIMUM_EVENT_LENGTH, indexOfFrom - 1);
        String eventFrom = userInput.substring(indexOfFrom + MINIMUM_EVENT_LENGTH, indexOfTo - 1);
        String eventTo = userInput.substring(indexOfTo + EVENT_TO_LENGTH);
        Event event = new Event(eventName + " (from: " + eventFrom + " to: " + eventTo + ")", eventFrom, eventTo);
        addTask(event);
        printTaskAdded(event);
    }

    private static void handleTodo(String userInput) throws CaneloException {
        if (userInput.length() <= MINIMUM_TODO_LENGTH) {
            throw new CaneloException("Please input a task name for todo.");
        }
        Task task = new Task(userInput.substring(MINIMUM_TODO_LENGTH));
        addTask(task);
        printTaskAdded(task);
    }

    private static void handleDelete(String userInput) throws CaneloException {
        if (userInput.length() <= MINIMUM_DELETE_LENGTH) {
            throw new CaneloException("Please add a task number to delete.");
        }
        int taskNumber = Integer.parseInt(userInput.substring(MINIMUM_DELETE_LENGTH));
        if (isInvalidTaskNumber(taskNumber)) {
            throw new CaneloException("Please input a valid task number between 1 and " + numTasks + ".");
        }
        Task taskToDelete = tasks.get(taskNumber - 1);
        System.out.println("Noted. I've removed this task:");
        System.out.println("    [" + taskToDelete.getTypeIcon() + "]["+taskToDelete.getStatusIcon() + "] "+taskToDelete.getDescription());
        tasks.remove(taskToDelete);
        numTasks -= 1;
        System.out.println("Now you have " + numTasks + " tasks in the list.");
    }

    private static void printTaskAdded(Task task) {
        System.out.println("Got it. I've added this task:");
        System.out.println("    [" + task.getTypeIcon() + "][" + task.getStatusIcon()+"] " + task.getDescription());
        System.out.println("Now you have " + numTasks + " tasks in the list.");
    }

    private static void addTask(Task task) {
        tasks.add(task);
        numTasks += 1;
    }

    private static boolean isInvalidTaskNumber(int taskNumber) {
        return taskNumber <= 0 || taskNumber > numTasks;
    }

    public static void main(String[] args) {
        printStartMessage();
        String userInput = input.nextLine();
        while (isValidUserInput(userInput)) {
            System.out.println(LINE);
            String taskType = determineTaskType(userInput);

            try {
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
                case "delete":
                    handleDelete(userInput);
                    break;
                default:
                    throw new CaneloException("Unknown task type.");
                }
            } catch (CaneloException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Add a valid task number: " + userInput);
            }

            System.out.println(LINE);
            userInput = input.nextLine();
        }
        printEndMessage();
    }
}

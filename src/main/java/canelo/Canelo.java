package canelo;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

public class Canelo {
    static Scanner input = new Scanner(System.in);
    static int numTasks = 0;
    static ArrayList<Task> tasks = new ArrayList<>();

    private static final String FILE_PATH = "caneloData.txt";

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
        return !userInput.equals("bye") && !userInput.equals("q");
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
        try {
            writeToCaneloFile();
        } catch (IOException e) {
            System.out.println("Something went wrong when writing to Canelo File: " + e.getMessage());
        }
        System.out.println("Nice! I've marked this task as done:\n[X] " + task.getDescription());
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
        try {
            writeToCaneloFile();
        } catch (IOException e) {
            System.out.println("Something went wrong when writing to Canelo File: " + e.getMessage());
        }
        System.out.println("OK, I've marked this task as not done yet:\n[ ] " + task.getDescription());
    }

    private static void handleDeadline(String userInput) throws CaneloException {
        int indexOfBy = userInput.indexOf(BY);
        if (indexOfBy <= MINIMUM_DEADLINE_LENGTH) {
            throw new CaneloException("Submit a Deadline using `taskName /by taskBy` format.");
        }
        String deadlineName = userInput.substring(MINIMUM_DEADLINE_LENGTH, indexOfBy - 1);
        String deadlineBy = userInput.substring(indexOfBy + DEADLINE_BY_LENGTH);
        Deadline deadline = new Deadline(deadlineName + " (by: " + deadlineBy + ")", deadlineBy, false);
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
        Event event = new Event(eventName + " (from: " + eventFrom + " to: " + eventTo + ")", eventFrom, eventTo, false);
        addTask(event);
        printTaskAdded(event);
    }

    private static void handleTodo(String userInput) throws CaneloException {
        if (userInput.length() <= MINIMUM_TODO_LENGTH) {
            throw new CaneloException("Please input a task name for todo.");
        }
        Task task = new Task(userInput.substring(MINIMUM_TODO_LENGTH), false);
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
        try {
            writeToCaneloFile();
        } catch (IOException e) {
            System.out.println("Something went wrong when writing to Canelo File: " + e.getMessage());
        }
    }

    private static void writeToCaneloFile() throws IOException {
        FileWriter fw = new FileWriter(FILE_PATH, false);
        for (int i = 0; i < numTasks; i++) {
            Task task = list[i];
            fw.write(task.toSaveFormat() + System.lineSeparator());
        }
        fw.close();
    }

    private static void loadTasks() throws FileNotFoundException {
        File f = new File(FILE_PATH);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating Canelo file: " + e.getMessage());
            }
        }
        Scanner s = new Scanner(f);
        while (s.hasNext()) {
            String line = s.nextLine();
            switch (line.charAt(0)) {
            case 'T':
                loadTask(line);
                break;
            case 'D':
                loadDeadline(line);
                break;
            case 'E':
                loadEvent(line);
                break;
            }
        }
    }

    private static void loadTask(String savedTask) {
        String[] splitSavedTask = savedTask.split(",,,");
        Task newTask;
        if (splitSavedTask[1].equals("X")) {
            newTask = new Task(splitSavedTask[2], true);
        } else {
            newTask = new Task(splitSavedTask[2], false);
        }
        list[numTasks] = newTask;
        numTasks += 1;
    }

    private static void loadDeadline(String savedDeadline) {
        String[] splitSavedDeadline = savedDeadline.split(",,,");
        Task newDeadline;
        if (splitSavedDeadline[1].equals("X")) {
            newDeadline = new Deadline(splitSavedDeadline[2], splitSavedDeadline[3], true);
        } else {
            newDeadline = new Deadline(splitSavedDeadline[2], splitSavedDeadline[3], false);
        }
        list[numTasks] = newDeadline;
        numTasks += 1;
    }

    private static void loadEvent(String savedEvent) {
        String[] splitSavedEvent = savedEvent.split(",,,");
        Task newEvent;
        if (splitSavedEvent[1].equals("X")) {
            newEvent = new Event(splitSavedEvent[2], splitSavedEvent[3], splitSavedEvent[4],true);
        } else {
            newEvent = new Event(splitSavedEvent[2], splitSavedEvent[3], splitSavedEvent[4],false);
        }
        list[numTasks] = newEvent;
        numTasks += 1;
    }

    private static boolean isInvalidTaskNumber(int taskNumber) {
        return taskNumber <= 0 || taskNumber > numTasks;
    }

    public static void main(String[] args) {
        try {
            loadTasks();
        } catch (FileNotFoundException e) {
            System.out.println("Canelo File not found.");
        }
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

package canelo;

import java.io.IOException;

/**
 * Handles parsing of user commands and delegates the execution of tasks.
 * This class is responsible for interpreting user input and calling
 * appropriate methods in other classes to execute commands.
 */
public class Parser {

    private TaskList tasks;
    private Ui ui;
    private Storage storage;

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
    static final int MINIMUM_FIND_LENGTH = 5;

    public Parser(TaskList taskList, Ui ui, Storage storage) {
        this.tasks = taskList;
        this.ui = ui;
        this.storage = storage;
    }

    /**
     * Checks whether the given user input is valid for continuing the program.
     * The input is considered invalid if it equals "bye", which is the
     * command to exit the program.
     *
     * @param userInput A string entered by the user.
     * @return {@code true} if the input is not an exit command, {@code false} otherwise.
     */
    public boolean isValidUserInput(String userInput) {
        return !userInput.equals("bye");
    }

    /**
     * Parses the given input and executes instructions for a given task type that
     * was entered by the user.
     *
     * @param userInput A string entered by the user.
     */
    public void handleCommand(String userInput) {
        String[] splitKeyboardInput = userInput.split(" ");
        String taskType = splitKeyboardInput[0];

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
            case "find":
                handleFind(userInput);
                break;
            default:
                throw new CaneloException("Unknown task type.");
            }
        } catch (CaneloException | IOException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Add a valid task number: " + userInput);
        }
    }

    /**
     * Prints all tasks in the TaskList in the form of [typeIcon][statusIcon] taskDescription.
     */
    private void handleList() {
        tasks.listTasks();
    }

    /**
     * Marks a specified task as done based on user input.
     * The method extracts the task number from the input, validates it,
     * and updates the task's status if valid. It then saves the updated
     * task list to storage.
     *
     * @param userInput The input string containing the mark command and task number.
     * @throws CaneloException If the input does not include a valid task number
     * or if the task number is out of range.
     */
    private void handleMark(String userInput) throws CaneloException {
        if (userInput.length() <= MINIMUM_MARK_LENGTH) {
            throw new CaneloException("Please add a task number to mark.");
        }
        int taskNumber = Integer.parseInt(userInput.substring(MINIMUM_MARK_LENGTH));
        if (tasks.isInvalidTaskNumber(taskNumber)) {
            throw new CaneloException("Please input a valid task number between 1 and " + tasks.getNumTasks() + ".");
        }
        Task task = tasks.getTask(taskNumber - 1);
        task.markDone();
        ui.printMessage("Nice! I've marked this task as done:\n[" + task.getTypeIcon() + "][X] " + task.getDescription());
        try {
            storage.writeToCaneloFile(tasks);
        } catch (IOException e) {
            System.out.println("Something went wrong when writing to Canelo File: " + e.getMessage());
        }
    }

    /**
     * Marks a specified task as not done based on user input.
     * The method extracts the task number from the input, validates it,
     * and updates the task's status if valid. It then saves the updated
     * task list to storage.
     *
     * @param userInput The input string containing the mark command and task number.
     * @throws CaneloException If the input does not include a valid task number
     * or if the task number is out of range.
     */
    private void handleUnmark(String userInput) throws CaneloException {
        if (userInput.length() <= MINIMUM_UNMARK_LENGTH) {
            throw new CaneloException("Please add a task number to unmark.");
        }
        int taskNumber = Integer.parseInt(userInput.substring(MINIMUM_UNMARK_LENGTH));
        if (tasks.isInvalidTaskNumber(taskNumber)) {
            throw new CaneloException("Please input a valid task number.");
        }
        Task task = tasks.getTask(taskNumber - 1);
        task.markNotDone();
        ui.printMessage("OK, I've marked this task as not done yet:\n[" + task.getTypeIcon() + "][ ] " + task.getDescription());
        try {
            storage.writeToCaneloFile(tasks);
        } catch (IOException e) {
            System.out.println("Something went wrong when writing to Canelo File: " + e.getMessage());
        }
    }

    /**
     * Handles the addition of a new todo task.
     * Extracts the task description from user input, creates a new {@link Task}
     * object, and adds it to the task list.
     *
     * @param userInput The input string containing the todo command and task description.
     * @throws CaneloException If the task description is missing.
     * @throws IOException If an error occurs while saving the updated task list to storage.
     */
    private void handleTodo(String userInput) throws CaneloException, IOException {
        if (userInput.length() <= MINIMUM_TODO_LENGTH) {
            throw new CaneloException("Please input a task name for todo.");
        }
        Task task = new Task(userInput.substring(MINIMUM_TODO_LENGTH), false);
        handleAddTask(task);
    }

    /**
     * Handles the addition of a new deadline task.
     * Extracts the deadline description and by from user input, creates a new
     * {@link Deadline} object, and adds it to the task list.
     *
     * @param userInput The input string containing the deadline command, description, and by.
     * @throws CaneloException If the deadline description and by are missing.
     * @throws IOException If an error occurs while saving the updated task list to storage.
     */
    private void handleDeadline(String userInput) throws CaneloException, IOException {
        int indexOfBy = userInput.indexOf(BY);
        if (indexOfBy <= MINIMUM_DEADLINE_LENGTH) {
            throw new CaneloException("Submit a Deadline using `taskName /by taskBy` format.");
        }
        String deadlineName = userInput.substring(MINIMUM_DEADLINE_LENGTH, indexOfBy - 1);
        String deadlineBy = userInput.substring(indexOfBy + DEADLINE_BY_LENGTH);
        Deadline deadline = new Deadline(deadlineName + " (by: " + deadlineBy + ")", deadlineBy, false);
        handleAddTask(deadline);
    }

    /**
     * Handles the addition of a new event task.
     * Extracts the event description, from, and to from user input, creates a new
     * {@link Event} object, and adds it to the task list.
     *
     * @param userInput The input string containing the event command, description, from, and to.
     * @throws CaneloException If the event description, from, and to are missing.
     * @throws IOException If an error occurs while saving the updated task list to storage.
     */
    private void handleEvent(String userInput) throws CaneloException, IOException {
        int indexOfFrom = userInput.indexOf(FROM);
        int indexOfTo = userInput.indexOf(TO);
        if (indexOfFrom <= MINIMUM_EVENT_LENGTH || indexOfTo <= MINIMUM_EVENT_LENGTH) {
            throw new CaneloException("Submit an Event using /from and /to format.");
        }
        String eventName = userInput.substring(MINIMUM_EVENT_LENGTH, indexOfFrom - 1);
        String eventFrom = userInput.substring(indexOfFrom + MINIMUM_EVENT_LENGTH, indexOfTo - 1);
        String eventTo = userInput.substring(indexOfTo + EVENT_TO_LENGTH);
        Event event = new Event(eventName + " (from: " + eventFrom + " to: " + eventTo + ")", eventFrom, eventTo, false);
        handleAddTask(event);
    }

    /**
     * Adds a new task to the task list, displays a confirmation message,
     * and saves the updated task list to storage.
     *
     * @param task The {@link Task} object to be added.
     * @throws IOException If an error occurs while saving the updated task list to storage.
     */
    private void handleAddTask(Task task) throws IOException {
        tasks.addTask(task);
        ui.printTaskAdded(task, tasks.getNumTasks());
        storage.writeToCaneloFile(tasks);
    }

    /**
     * Deletes a specified task number from the task list, displays a confirmation
     * message, and saves the updated task list to storage.
     *
     * @param userInput The input string containing the delete command and task number to delete.
     * @throws CaneloException If the task number is missing from the input.
     * @throws IOException If an error occurs while saving the updated task list to storage.
     */
    private void handleDelete(String userInput) throws CaneloException, IOException {
        if (userInput.length() <= MINIMUM_DELETE_LENGTH) {
            throw new CaneloException("Please add a task number to delete.");
        }
        int taskNumber = Integer.parseInt(userInput.substring(MINIMUM_DELETE_LENGTH));
        if (tasks.isInvalidTaskNumber(taskNumber)) {
            throw new CaneloException("Please input a valid task number between 1 and " + tasks.getNumTasks() + ".");
        }
        Task taskToDelete = tasks.getTask(taskNumber - 1);
        tasks.deleteTask(taskToDelete);
        ui.printTaskDeleted(taskToDelete, tasks.getNumTasks());
        storage.writeToCaneloFile(tasks);
    }

    /**
     * Prints all tasks from task list that contain a specified search word from the user input.
     *
     * @param userInput The input string containing the find command and a search word.
     * @throws CaneloException If the search word is missing from the input.
     */
    private void handleFind(String userInput) throws CaneloException {
        if (userInput.length() <= MINIMUM_FIND_LENGTH) {
            throw new CaneloException("Please add a search word to find.");
        }
        String searchWord = userInput.substring(MINIMUM_FIND_LENGTH);
        ui.printMessage("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.getNumTasks(); i++) {
            Task task = tasks.getTask(i);
            if (task.getDescription().contains(searchWord)) {
                ui.printMessage("   [" + task.getTypeIcon() + "]["+task.getStatusIcon()+"] " + task.getDescription());
            }
        }
    }
}


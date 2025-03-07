package canelo;

import java.io.IOException;

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

    public boolean isValidUserInput(String userInput) {
        return !userInput.equals("bye") && !userInput.equals("q");
    }

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

    private void handleList() {
        tasks.listTasks();
    }

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

    private void handleTodo(String userInput) throws CaneloException, IOException {
        if (userInput.length() <= MINIMUM_TODO_LENGTH) {
            throw new CaneloException("Please input a task name for todo.");
        }
        Task task = new Task(userInput.substring(MINIMUM_TODO_LENGTH), false);
        handleAddTask(task);
    }

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

    private void handleAddTask(Task task) throws IOException {
        tasks.addTask(task);
        ui.printTaskAdded(task, tasks.getNumTasks());
        storage.writeToCaneloFile(tasks);
    }

    public void handleDelete(String userInput) throws CaneloException, IOException {
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

    public void handleFind(String userInput) throws CaneloException {
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


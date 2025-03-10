package canelo;

import java.util.ArrayList;

/**
 * This class manages a list of tasks and provides methods to add, remove,
 * and retrieve tasks in the list.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public Task getTask(int taskNumber) {
        return tasks.get(taskNumber);
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
    }

    public int getNumTasks() {
        return tasks.size();
    }

    /**
     * Prints all tasks in the TaskList in the form of [typeIcon][statusIcon] taskDescription.
     */
    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks in list.");
        } else {
            int taskNum = 1;
            for (Task task : tasks) {
                String typeIcon = task.getTypeIcon();
                String statusIcon = task.getStatusIcon();
                String description = task.getDescription();
                System.out.println(taskNum + ".[" + typeIcon + "][" + statusIcon + "] " + description);
                taskNum++;
            }
        }
    }

    /**
     * Checks whether an integer is a valid task number.
     *
     * @return {@code true} if the integer is invalid, {@code false} otherwise.
     */
    public boolean isInvalidTaskNumber(int taskNumber) {
        return taskNumber <= 0 || taskNumber > tasks.size();
    }
}


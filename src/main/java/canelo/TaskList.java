package canelo;

import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
    }

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

    public int getNumTasks() {
        return tasks.size();
    }

    public boolean isInvalidTaskNumber(int taskNumber) {
        return taskNumber <= 0 || taskNumber > tasks.size();
    }

    public Task getTask(int taskNumber) {
        return tasks.get(taskNumber);
    }
}


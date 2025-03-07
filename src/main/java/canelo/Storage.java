package canelo;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from a file specified by the file path into an ArrayList.
     * The method reads the file and parses its contents, creating tasks based on the file's content.
     * If the file does not exist, a new file is created, and an empty task list is returned.
     *
     * @return An {@link ArrayList} of {@link Task} objects representing the tasks loaded from the file.
     * @throws CaneloException If there is an error related to the file format or loading tasks.
     * @throws FileNotFoundException If the file path does not point to an existing file when scanning.
     */
    public ArrayList<Task> load() throws CaneloException, FileNotFoundException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating Canelo file: " + e.getMessage());
            }
            return tasks;
        }

        Scanner s = new Scanner(file);
        while (s.hasNext()) {
            String line = s.nextLine();
            switch (line.charAt(0)) {
            case 'T':
                loadTask(line, tasks);
                break;
            case 'D':
                loadDeadline(line, tasks);
                break;
            case 'E':
                loadEvent(line, tasks);
                break;
            }
        }
        return tasks;
    }

    /**
     * Parses a todo task string and adds the corresponding task to the task list.
     * The method extracts the task description and status from the
     * saved format and creates a new {@link Task} object.
     *
     * @param savedTask The string representation of a todo task.
     * @param tasks The list of tasks to which the loaded task will be added.
     */
    private void loadTask(String savedTask, ArrayList<Task> tasks) {
        String[] splitSavedTask = savedTask.split(",,,");
        Task newTask;
        if (splitSavedTask[1].equals("X")) {
            newTask = new Task(splitSavedTask[2], true);
        } else {
            newTask = new Task(splitSavedTask[2], false);
        }
        tasks.add(newTask);
    }

    /**
     * Parses a deadline task string and adds the corresponding task to the task list.
     * The method extracts the deadline description, by, and status from the
     * saved format and creates a new {@link Deadline} object.
     *
     * @param savedDeadline The string representation of a deadline task.
     * @param tasks The list of tasks to which the loaded task will be added.
     */
    private void loadDeadline(String savedDeadline, ArrayList<Task> tasks) {
        String[] splitSavedDeadline = savedDeadline.split(",,,");
        Task newDeadline;
        if (splitSavedDeadline[1].equals("X")) {
            newDeadline = new Deadline(splitSavedDeadline[2], splitSavedDeadline[3], true);
        } else {
            newDeadline = new Deadline(splitSavedDeadline[2], splitSavedDeadline[3], false);
        }
        tasks.add(newDeadline);
    }

    /**
     * Parses an event task string and adds the corresponding task to the task list.
     * The method extracts the event description, from, to, and status from the
     * saved format and creates a new {@link Event} object.
     *
     * @param savedEvent The string representation of an event task.
     * @param tasks The list of tasks to which the loaded task will be added.
     */
    private void loadEvent(String savedEvent, ArrayList<Task> tasks) {
        String[] splitSavedEvent = savedEvent.split(",,,");
        Task newEvent;
        if (splitSavedEvent[1].equals("X")) {
            newEvent = new Event(splitSavedEvent[2], splitSavedEvent[3], splitSavedEvent[4],true);
        } else {
            newEvent = new Event(splitSavedEvent[2], splitSavedEvent[3], splitSavedEvent[4],false);
        }
        tasks.add(newEvent);
    }

    /**
     * Writes the current task list to the storage file. Each task is
     * converted into a string format using and written to the file,
     * with each task on a new line.
     *
     * @param tasks The {@link TaskList} containing tasks to be saved.
     * @throws IOException If an error occurs while writing to the file.
     */
    public void writeToCaneloFile(TaskList tasks) throws IOException {
        FileWriter fw = new FileWriter(filePath, false);
        for (int i = 0; i < tasks.getNumTasks(); i++) {
            Task task = tasks.getTask(i);
            fw.write(task.toSaveFormat() + System.lineSeparator());
        }
        fw.close();
    }
}

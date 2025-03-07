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

    public void writeToCaneloFile(TaskList tasks) throws IOException {
        FileWriter fw = new FileWriter(filePath, false);
        for (int i = 0; i < tasks.getNumTasks(); i++) {
            Task task = tasks.getTask(i);
            fw.write(task.toSaveFormat() + System.lineSeparator());
        }
        fw.close();
    }
}

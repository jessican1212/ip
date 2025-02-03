import java.util.Scanner;

public class Canelo {
    public static void main(String[] args) {
        System.out.println("""
                ____________________________________________________________
                 Hello! I'm Canelo :)
                 What can I do for you?
                 ____________________________________________________________""");
        Task[] list = new Task[100];
        int numTasks = 0;
        String line;
        Scanner in = new Scanner(System.in);
        line = in.nextLine();
        while (!line.equals("bye")) {
            System.out.println("____________________________________________________________");
            String[] splitKeyboardInput = line.split(" ");
            if (splitKeyboardInput.length == 0) {
                continue;
            } else if (splitKeyboardInput[0].equals("list") && splitKeyboardInput.length == 1) {
                for (int i = 0; i < numTasks; i++) {
                    int taskNumber = i + 1;
                    System.out.println(taskNumber + ".[" + list[i].getTypeIcon() + "][" + list[i].getStatusIcon() + "] " + list[i].getDescription());
                }
            } else if (splitKeyboardInput[0].equals("mark") && splitKeyboardInput.length == 2&& isValidTaskNumber(Integer.parseInt(splitKeyboardInput[1]), numTasks)) {
                Task task = list[Integer.parseInt(splitKeyboardInput[1]) - 1];
                task.markDone();
                System.out.println("Nice! I've marked this task as done:\n[X] " + task.getDescription());
            } else if (splitKeyboardInput[0].equals("unmark") && splitKeyboardInput.length == 2 && isValidTaskNumber(Integer.parseInt(splitKeyboardInput[1]), numTasks)) {
                Task task = list[Integer.parseInt(splitKeyboardInput[1]) - 1];
                task.markNotDone();
                System.out.println("OK, I've marked this task as not done yet:\n[ ] " + task.getDescription());
            } else if (splitKeyboardInput[0].equals("todo") && splitKeyboardInput.length >= 2) {
                Task task = new Task(line.substring(5));
                numTasks = addTask(task, list, numTasks);
                printTaskAdded(task, numTasks);
            } else if (splitKeyboardInput[0].equals("deadline")) {
                int indexOfBy = line.indexOf("/by");
                if (indexOfBy != -1) {
                    String deadlineName = line.substring(9, indexOfBy -1);
                    String deadlineBy = line.substring(indexOfBy +4);
                    Deadline deadline = new Deadline(deadlineName + " (by: " + deadlineBy + ")", deadlineBy);
                    numTasks = addTask(deadline, list, numTasks);
                    printTaskAdded(deadline, numTasks);
                }
            } else if (splitKeyboardInput[0].equals("event")) {
                int indexOfFrom = line.indexOf("/from");
                int indexOfTo = line.indexOf("/to");
                if (indexOfFrom != -1 && indexOfTo != -1) {
                    String eventName = line.substring(6, indexOfFrom -1);
                    String eventFrom = line.substring(indexOfFrom +6, indexOfTo -1);
                    String eventTo = line.substring(indexOfTo +4);
                    Event event = new Event(eventName + " (from: " + eventFrom + " to: " + eventTo + ")", eventFrom, eventTo);
                    numTasks = addTask(event, list, numTasks);
                    printTaskAdded(event, numTasks);
                }
            }
            System.out.println("____________________________________________________________");
            line = in.nextLine();
        }
        System.out.println("""
                ____________________________________________________________
                 Bye. Hope to see you again soon!
                ____________________________________________________________""");
    }

    private static void printTaskAdded(Task task, int numTasks) {
        System.out.println("Got it. I've added this task:");
        System.out.println("    [" + task.getTypeIcon() + "][" + task.getStatusIcon()+"] " + task.getDescription());
        System.out.println("Now you have " + numTasks + " tasks in the list.");
    }

    private static int addTask(Task task, Task[] list, int numTasks) {
        list[numTasks] = task;
        return numTasks+1;
    }

    private static boolean isValidTaskNumber(int taskNumber, int numTasks) {
        return taskNumber > 0 && taskNumber <= numTasks;
    }
}

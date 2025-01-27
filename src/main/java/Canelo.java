import java.util.Scanner;

public class Canelo {
    public static void main(String[] args) {
        System.out.println("""
                ____________________________________________________________
                 Hello! I'm Canelo :)
                 What can I do for you?
                 ____________________________________________________________""");
        Task[] list = new Task[100];
        int numInList = 0;
        String line;
        Scanner in = new Scanner(System.in);
        line = in.nextLine();
        while (!line.equals("bye")) {
            System.out.println("____________________________________________________________");
            String[] keyboardInput = line.split(" ");
            if (keyboardInput.length == 1 && keyboardInput[0].equals("list")) {
                for (int i = 0; i < numInList; i++) {
                    int iPlusOne = i + 1;
                    System.out.println(iPlusOne + ":[" + list[i].getStatusIcon() + "] " + list[i].getDescription());
                }
            } else if (keyboardInput.length == 2 && keyboardInput[0].equals("mark")) {
                Task task = list[Integer.parseInt(keyboardInput[1]) - 1];
                task.markDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("[X] " + task.getDescription());
            } else if (keyboardInput.length == 2 && keyboardInput[0].equals("unmark")) {
                Task task = list[Integer.parseInt(keyboardInput[1]) - 1];
                task.markNotDone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("[ ] " + task.getDescription());
            } else if (keyboardInput.length > 0) {
                Task task = new Task(line);
                list[numInList] = task;
                numInList++;
                System.out.println("added: " + line);
            }
            System.out.println("____________________________________________________________");
            line = in.nextLine();
        }
        System.out.println("""
                ____________________________________________________________
                 Bye. Hope to see you again soon!
                ____________________________________________________________""");
    }
}

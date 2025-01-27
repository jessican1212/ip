import java.util.Scanner;

public class Canelo {
    public static void main(String[] args) {
        System.out.println("""
                ____________________________________________________________
                 Hello! I'm Canelo :)
                 What can I do for you?
                 ____________________________________________________________""");
        String[] list = new String[100];
        int numInList = 0;
        String keyboardInput;
        Scanner in = new Scanner(System.in);
        keyboardInput = in.nextLine();
        while (!keyboardInput.equals("bye")) {
            System.out.println("____________________________________________________________");
            if (keyboardInput.equals("list")) {
                for (int i = 0; i < numInList; i++) {
                    int iPlusOne = i + 1;
                    System.out.println(iPlusOne + ": " + list[i]);
                }
            } else {
                list[numInList] = keyboardInput;
                numInList++;
                System.out.println("added: " + keyboardInput);
            }
            System.out.println("____________________________________________________________");
            keyboardInput = in.nextLine();
        }
        System.out.println("""
                ____________________________________________________________
                 Bye. Hope to see you again soon!
                ____________________________________________________________""");
    }
}

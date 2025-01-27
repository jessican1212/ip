import java.util.Scanner;

public class Canelo {
    public static void main(String[] args) {
        System.out.println("""
                ____________________________________________________________
                 Hello! I'm Canelo :)
                 What can I do for you?
                 ____________________________________________________________""");
        String keyboardInput;
        Scanner in = new Scanner(System.in);
        keyboardInput = in.nextLine();
        while (!keyboardInput.equals("bye")) {
            System.out.println("____________________________________________________________");
            System.out.println(keyboardInput);
            System.out.println("____________________________________________________________");
            keyboardInput = in.nextLine();
        }
        System.out.println("""
                ____________________________________________________________
                 Bye. Hope to see you again soon!
                ____________________________________________________________""");
    }
}

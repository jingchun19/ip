import java.util.Objects;
import java.util.Scanner;

public class JacobMalon {

    public static void input() {
        boolean isEnd = false;
        Scanner scn = new Scanner(System.in);
        while (!isEnd) {
            String inputFromUser = scn.nextLine();
            isEnd = Objects.equals(inputFromUser, "bye");
            if (isEnd) {
                break;
            }
            System.out.println(inputFromUser);
        }
    }
    public static void main(String[] args) {
        String greet = "Hello! I'm JacobMalon \n" +
                "What can I do for you? \n";
        String exit = "Bye. Hope to see you again!";
        System.out.println(greet);
        input();
        System.out.println(exit);
    }
}

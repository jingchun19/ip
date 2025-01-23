import java.util.Objects;
import java.util.Scanner;

public class JacobMalon {

    public static void displayList(String[] itemList, int count) {
        String itemString = "";
        for (int i = 0; i < count; i++) {
            int listNumber = i + 1;
            itemString += listNumber + ". " + itemList[i] + "\n";
        }
        System.out.println(itemString);
    }
    public static void input() {
        boolean isEnd = false;
        boolean isList = false;
        Scanner scn = new Scanner(System.in);
        String[] itemArray = new String[100];
        int counter = 0;
        while (!isEnd) {
            String inputFromUser = scn.nextLine();
            isEnd = Objects.equals(inputFromUser, "bye");
            isList = Objects.equals(inputFromUser, "list");
            if (isEnd) {
                break;
            } else if (isList) {
                displayList(itemArray, counter);
                continue;
            }
            itemArray[counter] = inputFromUser;
            System.out.println("added: " + inputFromUser);
            counter++;
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

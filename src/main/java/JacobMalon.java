import java.util.Objects;
import java.util.Scanner;

public class JacobMalon {
    private static final String CHATBOT_NAME = "Aiden";
    private final TaskManager taskManager;
    public JacobMalon() {
        this.taskManager = new TaskManager();
    }
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello! I'm " + CHATBOT_NAME);
        System.out.println("What can I do for you?");

        while (true) {
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            } else if (command.equals("list")) {
                taskManager.listTasks();
            } else if (command.startsWith("mark ")) {
                taskManager.markTask(Integer.parseInt(command.split(" ")[1]));
            } else if (command.startsWith("unmark ")) {
                taskManager.unmarkTask(Integer.parseInt(command.split(" ")[1]));
            } else if (command.startsWith("todo ")) {
                taskManager.addTask(new ToDo(command.substring(5)));
            } else if (command.startsWith("deadline ")) {
                String[] parts = command.substring(9).split(" /by ", 2);
                taskManager.addTask(new Deadline(parts[0], parts[1]));
            } else if (command.startsWith("event ")) {
                String[] parts = command.substring(6).split(" /from | /to ", 3);
                taskManager.addTask(new Event(parts[0], parts[1], parts[2]));
            } else {
                System.out.println("I'm sorry, but I don't understand that command.");
            }
        }
        scanner.close();
    }

    public static void main(String[] args) {
        new JacobMalon().run();
    }
}

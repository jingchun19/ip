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
            try {
                if (command.equals("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    break;
                } else if (command.equals("list")) {
                    taskManager.listTasks();
                } else if (command.startsWith("mark ")) {
                    String[] parts = command.split(" ");
                    if (parts.length < 2) {
                        throw new IllegalArgumentException("mark command must be followed by a task number.");
                    }
                    taskManager.markTask(Integer.parseInt(parts[1]));
                } else if (command.startsWith("unmark ")) {
                    taskManager.unmarkTask(Integer.parseInt(command.split(" ")[1]));
                } else if (command.startsWith("delete ")) {
                    taskManager.deleteTask(Integer.parseInt(command.split(" ")[1]));
                } else if (command.startsWith("todo ")) {
                    String taskDescription = command.substring(5).trim();
                    if (taskDescription.isEmpty()) {
                        throw new IllegalArgumentException("The description of a todo cannot be empty.");
                    }
                    taskManager.addTask(new ToDo(taskDescription));
                } else if (command.startsWith("deadline ")) {
                    String[] parts = command.substring(9).split(" /by ", 2);
                    taskManager.addTask(new Deadline(parts[0], parts[1]));
                } else if (command.startsWith("event ")) {
                    String[] parts = command.substring(6).split(" /from | /to ", 3);
                    taskManager.addTask(new Event(parts[0], parts[1], parts[2]));
                } else {
                    throw new IllegalArgumentException("I'm sorry, but I don't understand that command.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    public static void main(String[] args) {
        new JacobMalon().run();
    }
}

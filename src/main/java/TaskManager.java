import java.util.ArrayList;
import java.util.Scanner;

public class TaskManager {
    private final ArrayList<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
        System.out.println("Got it. I've added this task:");
        System.out.println(" " + task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks in your list.");
            return;
        }
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    public void markTask(int index) {
        if (index < 1 || index > tasks.size()) {
            System.out.println("Invalid task number!");
            return;
        }
        tasks.get(index - 1).markAsDone();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + tasks.get(index - 1));
    }

    public void unmarkTask(int index) {
        if (index < 1 || index > tasks.size()) {
            System.out.println("Invalid task number!");
            return;
        }
        tasks.get(index - 1).markAsNotDone();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + tasks.get(index - 1));
    }
}

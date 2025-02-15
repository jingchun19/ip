package taskscommand;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages a collection of tasks, providing operations to add, delete, mark, and list tasks.
 */
public class TaskManager {
    private static final String STORAGE_FILE = "list.txt";
    private static final String TASK_ADDED_MESSAGE = "Got it. I've added this task:";
    private static final String TASK_DELETED_MESSAGE = "Noted. I've removed this task:";
    private static final String TASK_MARKED_MESSAGE = "Nice! I've marked this task as done:";
    private static final String TASK_UNMARKED_MESSAGE = "Okay! I've marked this task as not done:";
    private static final String DUPLICATE_TASK_MESSAGE = "This task already exists in your list!";
    private static final String DUPLICATE_TASK_ADDED_ANYWAY = "Adding it anyway as requested.";
    
    private final ArrayList<Task> tasks;
    private final DateTimeFormatter formatter;

    public TaskManager() {
        tasks = new ArrayList<>();
        formatter = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");
        loadFromFile();
    }

    /**
     * Checks if a task is a duplicate of an existing task.
     * A task is considered a duplicate if it has the same description
     * and, in case of Deadline/Event tasks, the same timing.
     */
    private boolean isDuplicate(Task newTask) {
        return tasks.stream().anyMatch(existingTask -> {
            if (!existingTask.getDescription().equals(newTask.getDescription())) {
                return false;
            }
            
            // For Deadline tasks, check the deadline timing
            if (existingTask instanceof Deadline && newTask instanceof Deadline) {
                return ((Deadline) existingTask).getDeadline()
                    .equals(((Deadline) newTask).getDeadline());
            }
            
            // For Event tasks, check both start and end timing
            if (existingTask instanceof Event && newTask instanceof Event) {
                Event existing = (Event) existingTask;
                Event newEvent = (Event) newTask;
                return existing.getFrom().equals(newEvent.getFrom()) 
                    && existing.getTo().equals(newEvent.getTo());
            }
            
            // For Todo tasks, description equality is sufficient
            return true;
        });
    }

    /**
     * Adds a new task to the list and saves to storage.
     * If the task is a duplicate, warns the user but adds it anyway.
     */
    public void addTask(Task task) {
        assert task != null : "Task cannot be null";
        
        if (isDuplicate(task)) {
            System.out.println(DUPLICATE_TASK_MESSAGE);
            System.out.println(DUPLICATE_TASK_ADDED_ANYWAY);
        }
        
        tasks.add(task);
        System.out.println(TASK_ADDED_MESSAGE);
        System.out.println(task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        saveToFile();
    }

    /**
     * Deletes a task at the specified index.
     */
    public void deleteTask(int index) {
        validateTaskIndex(index);
        
        Task deletedTask = tasks.remove(index - 1);
        System.out.println(TASK_DELETED_MESSAGE);
        System.out.println(deletedTask);
        saveToFile();
    }

    /**
     * Marks a task as done at the specified index.
     */
    public void markTask(int index) {
        validateTaskIndex(index);
        
        Task task = tasks.get(index - 1);
        task.markAsDone();
        System.out.println(TASK_MARKED_MESSAGE);
        System.out.println(task);
        saveToFile();
    }

    /**
     * Marks a task as not done at the specified index.
     */
    public void unmarkTask(int index) {
        validateTaskIndex(index);
        
        Task task = tasks.get(index - 1);
        task.markAsNotDone();
        System.out.println(TASK_UNMARKED_MESSAGE);
        System.out.println(task);
        saveToFile();
    }

    /**
     * Lists all tasks in the list.
     */
    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks in the list.");
            return;
        }

        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Lists tasks with the specified date.
     */
    public void listTasksByDate(String dateStr) {
        List<Task> matchingTasks = findTasksByDate(dateStr);
        
        if (matchingTasks.isEmpty()) {
            System.out.println("No tasks found for the specified date.");
            return;
        }

        System.out.println("Here are the tasks for " + dateStr + ":");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println((i + 1) + "." + matchingTasks.get(i));
        }
    }

    /**
     * Finds tasks containing the specified keyword.
     */
    public void findTasks(String keyword) {
        assert keyword != null && !keyword.trim().isEmpty() : "Search keyword cannot be empty";
        
        List<Task> matchingTasks = tasks.stream()
            .filter(task -> task.getDescription().toLowerCase()
                .contains(keyword.toLowerCase()))
            .collect(Collectors.toList());

        displaySearchResults(matchingTasks, keyword);
    }

    // Private helper methods
    private void validateTaskIndex(int index) {
        if (index < 1 || index > tasks.size()) {
            throw new IllegalArgumentException(
                "Invalid task number. Please provide a number between 1 and " + tasks.size());
        }
    }

    private List<Task> findTasksByDate(String dateStr) {
        LocalDateTime searchDate = LocalDateTime.parse(dateStr, formatter);
        return tasks.stream()
            .filter(task -> task instanceof Deadline 
                && ((Deadline) task).getDeadline().equals(searchDate))
            .collect(Collectors.toList());
    }

    private void displaySearchResults(List<Task> matchingTasks, String keyword) {
        if (matchingTasks.isEmpty()) {
            System.out.println("No matching tasks found for: " + keyword);
            return;
        }

        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println((i + 1) + "." + matchingTasks.get(i));
        }
    }

    // File operations
    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(STORAGE_FILE)) {
            for (Task task : tasks) {
                writer.println(task.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(STORAGE_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                parseAndAddTask(line);
            }
        } catch (IOException e) {
            System.out.println("Error loading from file: " + e.getMessage());
        }
    }

    private void parseAndAddTask(String line) {
        // Implementation of task parsing logic
        // This would need to be implemented based on your task format
    }
}

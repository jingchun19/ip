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
    public String addTask(Task task) {
        assert task != null : "Task cannot be null";
        
        StringBuilder response = new StringBuilder();
        if (isDuplicate(task)) {
            response.append(DUPLICATE_TASK_MESSAGE).append("\n");
            response.append(DUPLICATE_TASK_ADDED_ANYWAY).append("\n");
        }
        
        tasks.add(task);
        response.append(TASK_ADDED_MESSAGE).append("\n");
        response.append(task).append("\n");
        response.append("Now you have " + tasks.size() + " tasks in the list.");
        saveToFile();
        return response.toString();
    }

    /**
     * Deletes a task at the specified index.
     */
    public String deleteTask(int index) {
        validateTaskIndex(index);
        
        Task deletedTask = tasks.remove(index - 1);
        StringBuilder response = new StringBuilder();
        response.append(TASK_DELETED_MESSAGE).append("\n");
        response.append(deletedTask);
        saveToFile();
        return response.toString();
    }

    /**
     * Marks a task as done at the specified index.
     */
    public String markTask(int index) {
        validateTaskIndex(index);
        
        Task task = tasks.get(index - 1);
        task.markAsDone();
        StringBuilder response = new StringBuilder();
        response.append(TASK_MARKED_MESSAGE).append("\n");
        response.append(task);
        saveToFile();
        return response.toString();
    }

    /**
     * Marks a task as not done at the specified index.
     */
    public String unmarkTask(int index) {
        validateTaskIndex(index);
        
        Task task = tasks.get(index - 1);
        task.markAsNotDone();
        StringBuilder response = new StringBuilder();
        response.append(TASK_UNMARKED_MESSAGE).append("\n");
        response.append(task);
        saveToFile();
        return response.toString();
    }

    /**
     * Lists all tasks in the list.
     */
    public String listTasks() {
        if (tasks.isEmpty()) {
            return "No tasks in the list.";
        }

        StringBuilder response = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            response.append(String.format("%d.%s\n", i + 1, tasks.get(i)));
        }
        return response.toString().trim();
    }

    /**
     * Lists tasks with the specified date.
     */
    public String listTasksByDate(String dateStr) {
        List<Task> matchingTasks = findTasksByDate(dateStr);
        
        if (matchingTasks.isEmpty()) {
            return "No tasks found for the specified date.";
        }

        StringBuilder response = new StringBuilder("Here are the tasks for " + dateStr + ":\n");
        for (int i = 0; i < matchingTasks.size(); i++) {
            response.append(String.format("%d.%s\n", i + 1, matchingTasks.get(i)));
        }
        return response.toString().trim();
    }

    /**
     * Finds tasks containing the specified keyword.
     */
    public String findTasks(String keyword) {
        assert keyword != null && !keyword.trim().isEmpty() : "Search keyword cannot be empty";
        
        List<Task> matchingTasks = tasks.stream()
            .filter(task -> task.getDescription().toLowerCase()
                .contains(keyword.toLowerCase()))
            .collect(Collectors.toList());

        return displaySearchResults(matchingTasks, keyword);
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

    private String displaySearchResults(List<Task> matchingTasks, String keyword) {
        if (matchingTasks.isEmpty()) {
            return "No matching tasks found for: " + keyword;
        }

        StringBuilder response = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matchingTasks.size(); i++) {
            response.append(String.format("%d.%s\n", i + 1, matchingTasks.get(i)));
        }
        return response.toString().trim();
    }

    // File operations
    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(STORAGE_FILE)) {
            for (Task task : tasks) {
                if (task instanceof ToDo) {
                    writer.println("todo " + task.getDescription());
                } else if (task instanceof Deadline) {
                    Deadline deadline = (Deadline) task;
                    writer.println("deadline " + deadline.getDescription() + " /by " + deadline.getDeadline());
                } else if (task instanceof Event) {
                    Event event = (Event) task;
                    writer.println("event " + event.getDescription() + " /from " + event.getFrom() + " /to " + event.getTo());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving to file: " + e.getMessage());
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
                if (!line.trim().isEmpty()) {
                    if (line.startsWith("todo ")) {
                        tasks.add(new ToDo(line.substring(5)));
                    } else if (line.startsWith("deadline ")) {
                        String[] parts = line.substring(9).split(" /by ", 2);
                        String description = parts[0];
                        String date = parts[1];
                        tasks.add(new Deadline(description, date));
                    } else if (line.startsWith("event ")) {
                        String[] parts = line.substring(6).split(" /from | /to ", 3);
                        String description = parts[0];
                        String fromDate = parts[1];
                        String toDate = parts[2];
                        tasks.add(new Event(description, fromDate, toDate));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading from file: " + e.getMessage());
        }
    }
}

package taskscommand;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {
    private ArrayList<Task> tasks;
    private static final String FILE_PATH = "data/list.TXT";
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy, h:mma");


    public TaskManager() {
        tasks = new ArrayList<>();
        loadFromFile(); // Load existing tasks when TaskManager is created
    }

    // Add this method to load tasks from file
    private void loadFromFile() {
        try {
            File directory = new File("data");
            if (!directory.exists()) {
                directory.mkdir();
            }

            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.createNewFile();
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    if (line.startsWith("[T]")) {
                        tasks.add(new ToDo(line.substring(7)));
                    } else if (line.startsWith("[D]")) {
                        int descEndIndex = line.indexOf(" (by: ");
                        if (descEndIndex == -1) {
                            throw new IllegalArgumentException("Invalid deadline format: " + line);
                        }
                        String description = line.substring(7, descEndIndex);
                        String dateStr = line.substring(descEndIndex + 6, line.length() - 1); // Remove ")"

                        // Adjust date parsing based on stored format
                        LocalDateTime parsedDate = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma"));
                        tasks.add(new Deadline(description, parsedDate.format(DateTimeFormatter.ofPattern("d/M/yyyy HHmm"))));
                    } else if (line.startsWith("[E]")) {
                        String[] parts = line.split(" \\(from: | to: ");
                        tasks.add(new Event(parts[0].substring(7), parts[1], parts[2].substring(0, parts[2].length() - 1)));
                    }
                } catch (Exception e) {
                    System.out.println("Error parsing task: " + line + " - " + e.getMessage());
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading from file: " + e.getMessage());
        }
    }


    // Add this method to save tasks to file
    private void saveToFile() {
        try {
            FileWriter writer = new FileWriter(FILE_PATH);
            for (Task task : tasks) {
                writer.write(task.toString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    // Modify your existing methods to call saveToFile()
    public void addTask(Task task) {
        tasks.add(task);
        System.out.println("Got it. I've added this task:");
        System.out.println(" " + task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        saveToFile(); // Save after adding
    }

    public void deleteTask(int index) {
        Task task = tasks.remove(index - 1);
        System.out.println("Noted. I've removed this task:");
        System.out.println(" " + task);
        saveToFile(); // Save after deleting
    }

    public void markTask(int index) {
        tasks.get(index - 1).markAsDone();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(" " + tasks.get(index - 1));
        saveToFile(); // Save after marking
    }

    public void unmarkTask(int index) {
        tasks.get(index - 1).markAsNotDone();
        System.out.println("Noted. I've unmarked this task:");
        System.out.println(" " + tasks.get(index - 1));
        saveToFile(); // Save after unmarking
    }

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

    // Add new method to list tasks by date
    public void listTasksByDate(String date) {
        try {
            LocalDate queryDate = LocalDate.parse(date, INPUT_FORMATTER);
            List<Task> tasksOnDate = tasks.stream()
                .filter(task -> task instanceof Deadline)
                .map(task -> (Deadline) task)
                .filter(deadline -> deadline.getDeadline().equals(queryDate))
                .collect(Collectors.toList());
            if (tasksOnDate.isEmpty()) {
                System.out.println("No tasks due on " + queryDate.format(OUTPUT_FORMATTER));
                return;
            }
            System.out.println("Here are the tasks due on " + queryDate.format(OUTPUT_FORMATTER) + ":");
            for (int i = 0; i < tasksOnDate.size(); i++) {
                System.out.println((i + 1) + "." + tasksOnDate.get(i));
            }
        } catch (DateTimeParseException e) {
            System.out.println("Please enter date in the format: yyyy-MM-dd (e.g., 2019-10-15)");
        }
    }

    /**
     * Finds and lists tasks that contain the specified keyword.
     * @param keyword The keyword to search for in task descriptions.
     */
    public void findTasks(String keyword) {
        List<Task> matchingTasks = tasks.stream()
            .filter(task -> task.getDescription().contains(keyword))
            .collect(Collectors.toList());

        if (matchingTasks.isEmpty()) {
            System.out.println("No matching tasks found.");
        } else {
            System.out.println("Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println((i + 1) + "." + matchingTasks.get(i));
            }
        }
    }
}

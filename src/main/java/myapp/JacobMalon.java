package myapp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import taskscommand.Deadline;
import taskscommand.Event;
import taskscommand.TaskManager;
import taskscommand.ToDo;

/**
 * The main class for the JacobMalon chatbot application.
 */
public class JacobMalon {
    private static final String CHATBOT_NAME = "Jacob";
    private final TaskManager taskManager;
    public boolean isExit = false;
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");

    /**
     * Constructs a new JacobMalon instance.
     */
    public JacobMalon() {
        this.taskManager = new TaskManager();
    }

    /**
     * Processes a single input command and returns the response.
     * @param input The input command.
     * @return The response from the chatbot.
     */
    public String getResponse(String input) {
        StringBuilder response = new StringBuilder();
        try {
            if (input.equals("bye")) {
                response.append("Bye. Hope to see you again soon!");
                System.exit(0);
            } else if (input.equals("list")) {
                // CHANGED: Now using taskManager.listTasks() that returns a string instead of printing to the terminal.
                response.append(taskManager.listTasks());
            } else if (input.startsWith("mark ")) {
                String[] parts = input.split(" ");
                if (parts.length < 2) {
                    throw new IllegalArgumentException("mark command must be followed by a task number.");
                }
                // Append the result from taskManager.markTask instead of printing.
                response.append(taskManager.markTask(Integer.parseInt(parts[1])));
            } else if (input.startsWith("unmark ")) {
                // Append the result from taskManager.unmarkTask.
                response.append(taskManager.unmarkTask(Integer.parseInt(input.split(" ")[1])));
            } else if (input.startsWith("delete ")) {
                // Append the result from taskManager.deleteTask.
                response.append(taskManager.deleteTask(Integer.parseInt(input.split(" ")[1])));
            } else if (input.startsWith("todo ")) {
                String taskDescription = input.substring(5).trim();
                if (taskDescription.isEmpty()) {
                    throw new IllegalArgumentException("The description of a todo cannot be empty.");
                }
                // Append the result from taskManager.addTask for a new ToDo.
                response.append(taskManager.addTask(new ToDo(taskDescription)));
            } else if (input.startsWith("deadline ")) {
                String[] parts = input.substring(9).split(" /by ", 2);
                if (parts.length < 2) {
                    throw new IllegalArgumentException(
                        "deadline command must be followed by description and date (e.g., deadline return book /by 2/12/2023 1800)");
                }
                // Validate date format by trying to parse it
                try {
                    LocalDateTime.parse(parts[1], INPUT_FORMATTER);
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException(
                        "date must be in d/M/yyyy HHmm format (e.g., 2/12/2023 1800)");
                }
                response.append(taskManager.addTask(new Deadline(parts[0], parts[1])));
            } else if (input.startsWith("list date ")) {
                String date = input.substring(10).trim();
                // Append the result from taskManager.listTasksByDate.
                response.append(taskManager.listTasksByDate(date));
            } else if (input.startsWith("event ")) {
                String[] parts = input.substring(6).split(" /from | /to ", 3);
                if (parts.length < 3) {
                    throw new IllegalArgumentException(
                        "event command must be followed by description and timing (e.g., event team meeting /from 2/12/2023 1400 /to 2/12/2023 1600)");
                }
                // Validate both dates
                try {
                    LocalDateTime from = LocalDateTime.parse(parts[1], INPUT_FORMATTER);
                    LocalDateTime to = LocalDateTime.parse(parts[2], INPUT_FORMATTER);
                    if (!from.isBefore(to)) {
                        throw new IllegalArgumentException("Event start time must be before end time");
                    }
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException(
                        "dates must be in d/M/yyyy HHmm format (e.g., 2/12/2023 1400)");
                }
                response.append(taskManager.addTask(new Event(parts[0], parts[1], parts[2])));
            } else if (input.startsWith("find ")) {
                String keyword = input.substring(5).trim();
                // CHANGED: Append the result from taskManager.findTasks.
                response.append(taskManager.findTasks(keyword));
            } else {
                throw new IllegalArgumentException("I'm sorry, but I don't understand that command.");
            }
        } catch (Exception e) {
            response.append("Error: ").append(e.getMessage());
        }
        return response.toString();
    }

    public static void main(String[] args) {
        JacobMalon chatbot = new JacobMalon();
        System.out.println("Hello! I'm " + CHATBOT_NAME);
        System.out.println("What can I do for you?");
    }
}

# JacobMalon Task Manager

A desktop task management application optimized for users who prefer typing over mouse interactions. If you can type fast, JacobMalon can help you manage tasks more efficiently than traditional GUI apps.

## Quick Start

1. Ensure you have Java 17 or above installed.
2. Download the latest `JacobMalon.jar` from [here](releases/latest).
3. Run the command: `java -jar JacobMalon.jar`

## Features

### Notes about Command Format
* Words in UPPER_CASE are parameters you need to provide
* Parameters can be in any order
* Dates should be in the format: `d/M/yyyy HHmm` (e.g., `2/12/2023 1800`)

### 1. Adding Tasks
You can add three types of tasks:

a) **Todo Task**
```
todo DESCRIPTION
todo read book
```

b) **Deadline Task**
```
deadline DESCRIPTION /by DATE_TIME
deadline return book /by 2/12/2023 1800
```

c) **Event Task**
```
event DESCRIPTION /from START_DATE_TIME /to END_DATE_TIME
event team meeting /from 2/12/2023 1400 /to 2/12/2023 1600
```

### 2. Viewing Tasks

a) **List all tasks**
```
list
```

b) **Find tasks by keyword**
```
find KEYWORD
find book
```

### 3. Managing Tasks

a) **Mark task as done**
```
mark INDEX
mark 1
```

b) **Mark task as not done**
```
unmark INDEX
unmark 1
```

c) **Delete task**
```
delete INDEX
delete 1
```

### 4. Exiting the Program
```
bye
```

## Duplicate Task Detection

The application will warn you when you try to add a duplicate task:
* For todos: Same description
* For deadlines: Same description and deadline
* For events: Same description, start time, and end time

## Data Storage

* Tasks are automatically saved after each command
* Data is stored in `list.txt` in the same directory as the application
* The file is automatically loaded when you start the application

## FAQ

**Q: How do I transfer my tasks to another computer?**
A: Copy the `list.txt` file to the same directory as the JacobMalon.jar on the new computer.

**Q: What happens if I enter an invalid date format?**
A: The application will show an error message. Remember to use the format: d/M/yyyy HHmm

## Command Summary

| Command | Format | Example |
|---------|---------|---------|
| Add todo | `todo DESCRIPTION` | `todo read book` |
| Add deadline | `deadline DESCRIPTION /by DATE_TIME` | `deadline return book /by 2/12/2023 1800` |
| Add event | `event DESCRIPTION /from START /to END` | `event meeting /from 2/12/2023 1400 /to 2/12/2023 1600` |
| List | `list` | `list` |
| Find | `find KEYWORD` | `find book` |
| Mark done | `mark INDEX` | `mark 1` |
| Mark not done | `unmark INDEX` | `unmark 1` |
| Delete | `delete INDEX` | `delete 1` |
| Exit | `bye` | `bye` |

## Development

### Project Structure

```
src
├── main
│   ├── java
│   │   └── myapp
│   │       ├── JacobMalon.java
│   │       ├── Main.java
│   │       └── taskscommand
│   │           ├── Task.java
│   │           ├── ToDo.java
│   │           ├── Deadline.java
│   │           └── Event.java
│   └── resources
│       └── view
│           └── MainWindow.fxml
└── test
    └── java
        └── taskscommand
            ├── TaskTest.java
            └── DeadlineTest.java
```

### Running Tests

```bash
./gradlew test
```

### Building JAR

```bash
./gradlew shadowJar
```

The JAR file will be created in `build/libs/JacobMalon.jar`

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
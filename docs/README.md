# JacobMalon Task Manager

A Java desktop application for managing tasks, built with JavaFX.

## Features

- **Task Management**: Create, update, and delete tasks
- **Multiple Task Types**:
    - Todo tasks
    - Deadline tasks (with date and time)
    - Event tasks (with start and end times)
- **Task Status**: Mark tasks as done/undone
- **Search**: Find tasks by keywords
- **Persistence**: Tasks are automatically saved to disk

## Getting Started

### Prerequisites

- JDK 17 or later
- Gradle (included in wrapper)
- JavaFX (included in dependencies)

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/JacobMalon.git
   ```

2. Navigate to the project directory:
   ```bash
   cd JacobMalon
   ```

3. Run the application:
   ```bash
   ./gradlew run
   ```

## Usage

The application supports the following commands:

| Command | Format | Example |
|---------|---------|---------|
| Add todo | `todo DESCRIPTION` | `todo read book` |
| Add deadline | `deadline DESCRIPTION /by DATE TIME` | `deadline return book /by 2/12/2023 1800` |
| Add event | `event DESCRIPTION /from START /to END` | `event meeting /from 2pm /to 4pm` |
| List tasks | `list` | `list` |
| Mark as done | `mark INDEX` | `mark 1` |
| Mark as not done | `unmark INDEX` | `unmark 1` |
| Delete task | `delete INDEX` | `delete 1` |
| Find tasks | `find KEYWORD` | `find book` |
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
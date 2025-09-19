# Usagi User Guide

Usagi is a task management chatbot that helps you organize your tasks efficiently. It supports different types of tasks and provides a user-friendly graphical interface.

## Quick Start

1. Run the application using `./gradlew run`
2. Type your commands in the input field at the bottom
3. Click on any message bubble to copy its content to your clipboard

## Task Types

### Todo Tasks
Simple tasks without specific deadlines.

**Command:** `todo <description>`

**Example:** `todo Buy groceries`

**Output:** `Got it. I've added this task: [T][ ] Buy groceries`

### Deadline Tasks
Tasks with a specific due date and time.

**Command:** `deadline <description> /by <date> <time>`

**Example:** `deadline Submit report /by 2024-12-25 1400`

**Output:** `Got it. I've added this task: [D][ ] Submit report (by: Dec 25 2024 2:00 PM)`

### Event Tasks
Tasks with a specific start and end time.

**Command:** `event <description> /from <date> <time> /to <date> <time>`

**Example:** `event Team meeting /from 2024-12-20 1000 /to 2024-12-20 1100`

**Output:** `Got it. I've added this task: [E][ ] Team meeting (from: Dec 20 2024 10:00 AM to: Dec 20 2024 11:00 AM)`

### Recurring Tasks
Tasks that repeat at regular intervals.

**Command:** `recurring <description> /from <date> <time> /to <date> <time> /every <pattern> [interval]`

**Example:** `recurring Weekly standup /from 2024-12-20 0900 /to 2024-12-20 0930 /every weekly`

**Output:** `Got it. I've added this recurring task: [R][ ] Weekly standup (from: Dec 20 2024 9:00 AM to: Dec 20 2024 9:30 AM) - every 1 week`

**Recurrence Patterns:**
- `daily` - repeats every day
- `weekly` - repeats every week
- `monthly` - repeats every month
- `yearly` - repeats every year

**Optional interval:** Add a number before the pattern (e.g., `2 weekly` for every 2 weeks)

## Task Management Commands

### List All Tasks
**Command:** `list`

**Output:** Shows all tasks with their status, type, and details.

### Mark Task as Done
**Command:** `mark <task_number>`

**Example:** `mark 1`

**Output:** `Nice! I've marked this task as done: [T][X] Buy groceries`

### Mark Task as Not Done
**Command:** `unmark <task_number>`

**Example:** `unmark 1`

**Output:** `OK, I've marked this task as not done yet: [T][ ] Buy groceries`

### Delete Task
**Command:** `delete <task_number>`

**Example:** `delete 1`

**Output:** `Noted. I've removed this task: [T][ ] Buy groceries`

### Find Tasks
**Command:** `find <keyword>`

**Example:** `find meeting`

**Output:** Shows all tasks containing the keyword.

### Show Upcoming Recurring Tasks
**Command:** `upcoming [days]`

**Example:** `upcoming 7`

**Output:** Shows recurring tasks due within the specified number of days (default: 7 days).

## Date and Time Format

- **Date format:** `YYYY-MM-DD` (e.g., `2024-12-25`)
- **Time format:** `HHMM` in 24-hour format (e.g., `1400` for 2:00 PM)

## Exit Application

**Command:** `bye`

**Output:** `Bye. Hope to see you again soon!`

## Features

- **Rich Text Display:** Task types, status, and titles are displayed in bold for better readability
- **Click to Copy:** Click any message bubble to copy its content to your clipboard
- **Error Handling:** Invalid commands show error messages with red background
- **Persistent Storage:** Tasks are automatically saved and restored between sessions
- **Welcome Message:** Get started with helpful instructions when the app launches

document.addEventListener('DOMContentLoaded', () => {
    const form = document.querySelector("#new-task-form");
    const input = document.querySelector("#new-task-input");
    const priority = document.querySelector("#new-task-priority");
    const due = document.querySelector("#new-task-due");
    const tasksContainer = document.querySelector("#tasks");
    const modal = document.querySelector("#edit-task-modal");
    const closeButton = document.querySelector(".close-button");
    const editForm = document.querySelector("#edit-task-form");
    const editInput = document.querySelector("#edit-task-input");
    const editPriority = document.querySelector("#edit-task-priority");
    const editDue = document.querySelector("#edit-task-due");
    const themeToggleButton = document.querySelector("#theme-toggle");
    const searchInput = document.querySelector("#search-input");
    const searchButton = document.querySelector("#search-button");
    const exportButton = document.querySelector("#export-button");
    const importButton = document.querySelector("#import-button");
    const importFileInput = document.querySelector("#import-file");
    const apiUrl = 'http://localhost:8082/To-Do-App/todos';

    let currentTaskElement = null;

    // Validation functions
    const validateTaskInputs = (taskText, taskPriority, taskDue) => {
        let isValid = true;
        const errors = [];
        const now = new Date();

        if (!taskText) {
            errors.push("Task description cannot be empty.");
            isValid = false;
        }

        const validPriorities = ['low', 'medium', 'high'];
        if (!validPriorities.includes(taskPriority.toLowerCase())) {
            errors.push("Invalid priority selected.");
            isValid = false;
        }

        const dueDate = new Date(taskDue);
        if (!taskDue || isNaN(dueDate.getTime()) || dueDate < now) {
            errors.push("Invalid due date.");
            isValid = false;
        }

        if (!isValid) {
            alert(errors.join("\n"));
        }

        return isValid;
    };

    const validateEditTaskInputs = (taskName, taskPriority, taskDue) => {
        let isValid = true;
        const errors = [];
        const now = new Date();

        if (!taskName) {
            errors.push("Task description cannot be empty.");
            isValid = false;
        }

        const validPriorities = ['low', 'medium', 'high'];
        if (!validPriorities.includes(taskPriority.toLowerCase())) {
            errors.push("Invalid priority selected.");
            isValid = false;
        }

        const dueDate = new Date(taskDue);
        if (!taskDue || isNaN(dueDate.getTime()) || dueDate < now) {
            errors.push("Invalid due date.");
            isValid = false;
        }

        if (!isValid) {
            alert(errors.join("\n"));
        }

        return isValid;
    };

    // Function to add a new task
    const addTask = async (taskTitle, taskPriority, taskDue) => {
        if (!taskTitle) {
            console.error("Task title is required");
            return;
        }

        taskPriority = taskPriority || 'medium';
        taskDue = taskDue || 'No due date';

        const taskDiv = document.createElement('div');
        taskDiv.classList.add('task');
        taskDiv.dataset.priority = taskPriority;
        taskDiv.dataset.due = taskDue;
        taskDiv.draggable = true;

        const taskContentDiv = document.createElement('div');
        taskContentDiv.classList.add('content');
        taskDiv.appendChild(taskContentDiv);

        const taskInput = document.createElement('input');
        taskInput.classList.add('text');
        taskInput.type = 'text';

        const formattedPriority = taskPriority.charAt(0).toUpperCase() + taskPriority.slice(1).toLowerCase();
        taskInput.value = `${taskTitle} - [${formattedPriority} Priority] - Due: ${taskDue}`;
        taskInput.readOnly = true;
        taskContentDiv.appendChild(taskInput);

        const taskActionsDiv = document.createElement('div');
        taskActionsDiv.classList.add('actions');

        const checkbox = document.createElement('input');
        checkbox.type = 'checkbox';
        checkbox.classList.add('complete-checkbox');
        checkbox.addEventListener('change', () => handleCheckboxChange(taskDiv));
        taskActionsDiv.appendChild(checkbox);

        const editButton = document.createElement('button');
        editButton.classList.add('edit');
        editButton.innerText = 'Edit';
        editButton.addEventListener('click', () => {
            modal.style.display = "block";
            currentTaskElement = taskDiv;

            const [taskName, , taskDueDate] = taskInput.value.split(" - ");
            editInput.value = taskName.trim();
            editPriority.value = taskPriority.toLowerCase();
            editDue.value = taskDueDate.replace("Due:", "").trim();
        });
        taskActionsDiv.appendChild(editButton);

        const deleteButton = document.createElement('button');
        deleteButton.classList.add('delete');
        deleteButton.innerText = 'Delete';
        deleteButton.addEventListener('click', () => {
            deleteTaskFromDatabase(taskDiv);
        });
        taskActionsDiv.appendChild(deleteButton);

        taskDiv.appendChild(taskActionsDiv);
        tasksContainer.appendChild(taskDiv);

        input.value = '';
        priority.value = '';
        due.value = '';

        taskDiv.addEventListener('dragstart', () => taskDiv.classList.add('dragging'));
        taskDiv.addEventListener('dragend', () => taskDiv.classList.remove('dragging'));

        tasksContainer.addEventListener('dragover', (event) => {
            event.preventDefault();
            const draggingTask = document.querySelector('.dragging');
            const afterElement = getDragAfterElement(tasksContainer, event.clientY);
            if (afterElement == null) {
                tasksContainer.appendChild(draggingTask);
            } else {
                tasksContainer.insertBefore(draggingTask, afterElement);
            }
        });

        tasksContainer.addEventListener('drop', () => {
            const draggingTask = document.querySelector('.dragging');
            if (draggingTask) {
                draggingTask.classList.remove('dragging');
            }
        });

        sortTasks();

        // Call to the API to save the task
        try {
            const response = await fetch(`${apiUrl}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    title: taskTitle,
                    priority: taskPriority,
                    dueDate: taskDue,
                    isDone: checkbox.checked
                })
            });

            if (!response.ok) throw new Error('Failed to create task');
        } catch (error) {
            console.error('Error creating task:', error);
        }
    };

    function getDragAfterElement(container, y) {
        const draggableElements = [...container.querySelectorAll('.task:not(.dragging)')];
        return draggableElements.reduce((closest, child) => {
            const box = child.getBoundingClientRect();
            const offset = y - box.top - box.height / 2;
            if (offset < 0 && offset > closest.offset) {
                return { offset: offset, element: child };
            } else {
                return closest;
            }
        }, { offset: Number.NEGATIVE_INFINITY }).element;
    }

    const sortOptions = document.querySelector("#sort-options");

    const sortTasks = (criteria) => {
        const tasks = Array.from(tasksContainer.querySelectorAll('.task'));

        if (criteria === 'priority') {
            tasks.sort((a, b) => {
                const priorityOrder = ['low', 'medium', 'high'];
                const priorityA = priorityOrder.indexOf(a.dataset.priority);
                const priorityB = priorityOrder.indexOf(b.dataset.priority);
                return priorityB - priorityA;
            });
        } else if (criteria === 'due-date') {
            tasks.sort((a, b) => {
                const dueA = new Date(a.dataset.due);
                const dueB = new Date(b.dataset.due);
                return dueA - dueB;
            });
        }

        tasks.forEach(task => tasksContainer.appendChild(task));
    };

    sortOptions.addEventListener('change', (event) => {
        const selectedOption = event.target.value;
        sortTasks(selectedOption);
    });

    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        const taskTitle = input.value.trim();
        const taskPriority = priority.value.trim();
        const taskDue = due.value.trim();

        if (validateTaskInputs(taskTitle, taskPriority, taskDue)) {
            addTask(taskTitle, taskPriority, taskDue);
            await postTask(taskTitle, taskPriority, taskDue, false); // Assuming the task is not done initially
        }
    });


    editForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const editedTaskName = editInput.value.trim();
        const editedPriority = editPriority.value.trim();
        const editedDue = editDue.value.trim();

        if (validateEditTaskInputs(editedTaskName, editedPriority, editedDue)) {
            const updatedTaskText = `${editedTaskName} - [${editedPriority.charAt(0).toUpperCase() + editedPriority.slice(1)} Priority] - Due: ${editedDue}`;
            const taskInput = currentTaskElement.querySelector('.text');
            taskInput.value = updatedTaskText;
            currentTaskElement.dataset.priority = editedPriority;
            currentTaskElement.dataset.due = editedDue;
            modal.style.display = "none";

            sortTasks();

            try {
                const taskId = currentTaskElement.dataset.id;
                const response = await fetch(`${apiUrl}?id=${taskId}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        title: editedTaskName,
                        priority: editedPriority,
                        due: editedDue,
                        isDone: currentTaskElement.querySelector('.complete-checkbox').checked
                    })
                });

                if (!response.ok) throw new Error('Failed to update task');
            } catch (error) {
                console.error('Error updating task:', error);
            }
        }
    });

    const deleteTaskFromDatabase = async (taskDiv) => {
        const taskId = taskDiv.dataset.id;
        try {
            const response = await fetch(`${apiUrl}?id=${taskId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) throw new Error('Failed to delete task');

            tasksContainer.removeChild(taskDiv);
        } catch (error) {
            console.error('Error deleting task:', error);
        }
    };


    closeButton.addEventListener('click', () => {
        modal.style.display = "none";
    });

    document.addEventListener('click', (event) => {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    });

    searchButton.addEventListener('click', () => {
        const searchTerm = searchInput.value.toLowerCase();
        const tasks = tasksContainer.querySelectorAll('.task');

        tasks.forEach(task => {
            const taskText = task.querySelector('.text').textContent.toLowerCase();
            task.style.display = taskText.includes(searchTerm) ? '' : 'none';
        });
    });

    exportButton.addEventListener('click', async () => {
        try {
            const response = await fetch(`${apiUrl}`);
            if (!response.ok) throw new Error('Failed to fetch tasks');
            const tasks = await response.json();
            const blob = new Blob([JSON.stringify(tasks, null, 2)], { type: 'application/json' });
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'tasks.json';
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            URL.revokeObjectURL(url);
        } catch (error) {
            console.error('Error exporting tasks:', error);
        }
    });

    importButton.addEventListener('click', () => {
        importFileInput.click();
    });

    importFileInput.addEventListener('change', async (event) => {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = async (e) => {
                try {
                    const tasks = JSON.parse(e.target.result);
                    for (const task of tasks) {
                        await addTask(task.title, task.priority, task.due);
                    }
                } catch (error) {
                    console.error('Error parsing JSON:', error);
                }
            };
            reader.readAsText(file);
        }
    });

    themeToggleButton.addEventListener('click', () => {
        document.body.classList.toggle("light-theme");
    });

    const handleCheckboxChange = async (taskDiv) => {
        const isChecked = taskDiv.querySelector('.complete-checkbox').checked;

        if (isChecked) {
            document.querySelector('#completed-tasks').appendChild(taskDiv);
        } else {
            tasksContainer.appendChild(taskDiv);
        }

        try {
            const taskId = taskDiv.dataset.id;
            const response = await fetch(`${apiUrl}?id=${taskId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    isDone: isChecked
                })
            });

            if (!response.ok) throw new Error('Failed to update task status');
        } catch (error) {
            console.error('Error updating task status:', error);
        }
    };


    const fetchTodos = async () => {
        console.log("fetch");
        try {
            const response = await fetch(`${apiUrl}`);
            if (!response.ok) throw new Error('Network response was not ok');
            const tasks = await response.json();
            console.log(tasks);
            tasks.forEach(task => {
                addTask(task.title, task.priority, task.dueDate).then(() => {
                    const taskDiv = tasksContainer.querySelector('.task:last-child');
                    const checkbox = taskDiv.querySelector('.complete-checkbox');
                    checkbox.checked = task.isDone;

                    if (task.isDone) {
                        document.querySelector('#completed-tasks').appendChild(taskDiv);
                    }
                });
            });
            console.log(tasks);
        } catch (error) {
            console.error('Error fetching todos:', error);
        }
    };
    fetchTodos();

    const showDueDateNotifications = () => {
        const tasks = tasksContainer.querySelectorAll('.task');
        const now = new Date();

        tasks.forEach(task => {
            const dueDate = new Date(task.dataset.due);
            const timeDiff = dueDate - now;

            if (timeDiff > 0 && timeDiff <= 24 * 60 * 60 * 1000) {
                const notification = {
                    title: 'Task Due Soon!',
                    body: `Your task "${task.querySelector('.text').value.split(" - ")[0]}" is due soon.`,
                    icon: 'icon.png'
                };

                if (Notification.permission === 'granted') {
                    new Notification(notification.title, notification);
                } else if (Notification.permission !== 'denied') {
                    Notification.requestPermission().then(permission => {
                        if (permission === 'granted') {
                            new Notification(notification.title, notification);
                        }
                    });
                }
            }
        });
    };

    Notification.requestPermission().then(permission => {
        if (permission === 'granted') {
            showDueDateNotifications();
        } else {
            alert("Please enable notifications for due date alerts.");
        }
    });
});

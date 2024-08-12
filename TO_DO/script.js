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

    let currentTaskElement = null;

     // *Validation of task
     const validateTaskInputs = (taskText, taskPriority, taskDue) => {
        let isValid = true;
        const errors = [];
        const now = new Date();

        // Validate task text
        if (!taskText) {
            errors.push("Task description cannot be empty.");
            isValid = false;
        }

        // Validate priority
        const validPriorities = ['low', 'medium', 'high'];
        if (!validPriorities.includes(taskPriority.toLowerCase())) {
            errors.push("Invalid priority selected.");
            isValid = false;
        }

        // Validate due date
        const dueDate = new Date(taskDue);
        if (!taskDue || isNaN(dueDate.getTime()) || dueDate < now) {
            errors.push("Invalid due date.");
            isValid = false;
        }

        // Display errors if any
        if (!isValid) {
            alert(errors.join("\n"));
        }

        return isValid;
    };

    // Validate edited task
    const validateEditTaskInputs = (taskName, taskPriority, taskDue) => {
        let isValid = true;
        const errors = [];
        const now = new Date();

        // Validate task name
        if (!taskName) {
            errors.push("Task description cannot be empty.");
            isValid = false;
        }

        // Validate priority
        const validPriorities = ['low', 'medium', 'high'];
        if (!validPriorities.includes(taskPriority.toLowerCase())) {
            errors.push("Invalid priority selected.");
            isValid = false;
        }

        // Validate due date
        const dueDate = new Date(taskDue);
        if (!taskDue || isNaN(dueDate.getTime()) || dueDate < now) {
            errors.push("Invalid due date.");
            isValid = false;
        }

        // Display errors if any
        if (!isValid) {
            alert(errors.join("\n"));
        }

        return isValid;
    };


    // Function to add a new task
    const addTask = (taskText, taskPriority, taskDue) => {
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
        taskInput.value = `${taskText} - [${taskPriority.charAt(0).toUpperCase() + taskPriority.slice(1)} Priority] - Due: ${taskDue}`;
        taskInput.readOnly = true;
        taskContentDiv.appendChild(taskInput);

        const taskActionsDiv = document.createElement('div');
        taskActionsDiv.classList.add('actions');

        const editButton = document.createElement('button');
        editButton.classList.add('edit');
        editButton.innerText = 'Edit';

        const deleteButton = document.createElement('button');
        deleteButton.classList.add('delete');
        deleteButton.innerText = 'Delete';
        
        taskActionsDiv.appendChild(editButton);
        taskActionsDiv.appendChild(deleteButton);

        taskDiv.appendChild(taskActionsDiv);
        tasksContainer.appendChild(taskDiv);

        input.value = '';
        priority.value = '';
        due.value = '';

        // Add event listeners for edit and delete buttons
        editButton.addEventListener('click', () => {
                        modal.style.display = "block";
                        currentTaskElement = taskDiv;

                        const [taskName, , taskDue] = taskInput.value.split(" - ");
                        editInput.value = taskName.trim();
                        editPriority.value = taskPriority.toLowerCase();
                        editDue.value = taskDue.replace("Due:", "").trim();
                    });

                    deleteButton.addEventListener('click', () => {
                        tasksContainer.removeChild(taskDiv);
                    });


        // Add drag event listeners
        taskDiv.addEventListener('dragstart', (event) => {
            event.dataTransfer.setData('text/plain', '');
            taskDiv.classList.add('dragging');
        });

        taskDiv.addEventListener('dragend', () => {
            taskDiv.classList.remove('dragging');
        });

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
        closeButton.addEventListener('click', () => {
            modal.style.display = "none";
           
        });
        tasksContainer.addEventListener('drop', () => {
            const draggingTask = document.querySelector('.dragging');
            if (draggingTask) {
                draggingTask.classList.remove('dragging');
            }
        });

        // Sort tasks after adding a new one
        sortTasks();
    };

    // Function to get the element after which the dragged element should be inserted
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

    // Function to sort tasks based on priority and due date
    const sortOptions = document.querySelector("#sort-options");

    // Function to sort tasks
    const sortTasks = (criteria) => {
        const tasks = Array.from(tasksContainer.querySelectorAll('.task'));
        
        if (criteria === 'priority') {
            tasks.sort((a, b) => {
                const priorityOrder = ['low', 'medium', 'high'];
                const priorityA = priorityOrder.indexOf(a.dataset.priority);
                const priorityB = priorityOrder.indexOf(b.dataset.priority);
                return priorityB - priorityA; // High priority first
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

    // Event listener for sort options change
    sortOptions.addEventListener('change', (event) => {
        const selectedOption = event.target.value;
        sortTasks(selectedOption);
    });

    // Function to handle form submission
    form.addEventListener('submit', (event) => {
        event.preventDefault();

        const taskText = input.value.trim();
        const taskPriority = priority.value.trim();
        const taskDue = due.value.trim();

        if (validateTaskInputs (taskText, taskPriority, taskDue)) {
            addTask(taskText, taskPriority, taskDue);
            saveTasksToLocalStorage(); 
        }
    });

    // Function to handle editing task
    editForm.addEventListener('submit', (event) => {
        event.preventDefault();
        const editedTaskName = editInput.value.trim();
        const editedPriority = editPriority.value.trim();
        const editedDue = editDue.value.trim();

        if (validateEditTaskInputs(editedTaskName, editedPriority, editedDue)){
        const updatedTaskText = `${editedTaskName} - [${editedPriority.charAt(0).toUpperCase() + editedPriority.slice(1)} Priority] - Due: ${editedDue}`;
        const taskInput = currentTaskElement.querySelector('.text');
        taskInput.value = updatedTaskText;
        currentTaskElement.dataset.priority = editedPriority;
        currentTaskElement.dataset.due = editedDue;
        modal.style.display = "none";

        saveTasksToLocalStorage(); 
        sortTasks();
        }
    });

    closeButton.addEventListener('click', () => {
        modal.style.display = "none";
       
    });

    document.addEventListener('click', (event) => {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    });

    // Search functionality
    searchButton.addEventListener('click', () => {
        const searchTerm = searchInput.value.toLowerCase();
        const tasks = tasksContainer.querySelectorAll('.task');

        tasks.forEach(task => {
            const taskText = task.querySelector('.text').value.toLowerCase();
            if (taskText.includes(searchTerm)) {
                task.style.display = '';
            } else {
                task.style.display = 'none';
            }
        });
    });

    // Export functionality
    exportButton.addEventListener('click', () => {
        const tasks = Array.from(tasksContainer.querySelectorAll('.task')).map(task => ({
            text: task.querySelector('.text').value,
            priority: task.dataset.priority,
            due: task.dataset.due
        }));
        const blob = new Blob([JSON.stringify(tasks, null, 2)], { type: 'application/json' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'tasks.json';
        document.body.appendChild(a); 
        a.click();
        document.body.removeChild(a); 
        URL.revokeObjectURL(url);
    });

    // Import functionality
    importButton.addEventListener('click', () => {
        importFileInput.click();
    });

    importFileInput.addEventListener('change', (event) => {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (e) => {
                try {
                    const tasks = JSON.parse(e.target.result);
                    tasks.forEach(task => addTask(task.text, task.priority, task.due));
                } catch (error) {
                    console.error('Error parsing JSON:', error);
                }
            };
            reader.readAsText(file);
        }
    });

    // Theme toggle functionality
    themeToggleButton.addEventListener('click', () => {
        document.body.classList.toggle("light-theme");
    });

    // Function to save tasks to local storage
    const saveTasksToLocalStorage = () => {
        const tasks = Array.from(tasksContainer.querySelectorAll('.task')).map(task => ({
            text: task.querySelector('.text').value,
            priority: task.dataset.priority,
            due: task.dataset.due
        }));
        localStorage.setItem('tasks', JSON.stringify(tasks));
    };

    // Function to load tasks from local storage
    const loadTasksFromLocalStorage = () => {
        const tasks = JSON.parse(localStorage.getItem('tasks')) || [];
        tasks.forEach(task => addTask(task.text, task.priority, task.due));
    };

    // Load tasks from local storage on page load
    loadTasksFromLocalStorage();

    // Function to show notifications based on due date
    const showDueDateNotifications = () => {

        const tasks = tasksContainer.querySelectorAll('.task');
        // const now = new Date()
        tasks.forEach(task => {
            const dueDate = new Date(task.dataset.due);
            const now = new Date();
            const timeDiff = dueDate - now;

            if (timeDiff > 0 && timeDiff <= 24 * 60 * 60 * 1000) { 
               
                const notification = {
                    title: 'Task Due Soon!',
                    body: `Your task "${task.querySelector('.text').value}" is due soon.`,
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

    // Call showDueDateNotifications function on page load
        
            Notification.requestPermission().then(permission => {
                if (permission === 'granted') {
                    showDueDateNotifications();
                }
                else{
                    prompt("give permission");
                }
            });
        
});

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.querySelector("#loginForm");
    const emailInput = document.querySelector("#email");
    const passwordInput = document.querySelector("#password");
    const errorMessage = document.querySelector("#errorMessage");

    // Login form submission
    if (loginForm) {
        loginForm.addEventListener("submit", (event) => {
            event.preventDefault();

            const email = emailInput.value.trim();
            const password = passwordInput.value.trim();

            if (validateLogin(email, password)) {
                // Send login request to the server
                fetch('/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ email, password })
                })
                    .then(response => {
                        if (response.ok) {
                            // Redirect to index.html if login is successful
                            window.location.href = "index.html";
                        } else {
                            // Display error message if login fails
                            return response.text().then(text => {
                                errorMessage.textContent = text; // Display error message from server
                                errorMessage.style.display = "block";
                            });
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        errorMessage.textContent = "An unexpected error occurred.";
                        errorMessage.style.display = "block";
                    });
            } else {
                errorMessage.textContent = "Please enter valid email and password.";
                errorMessage.style.display = "block";
            }
        });
    }
});

// Basic validation function (expand as needed)
function validateLogin(email, password) {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const isEmailValid = emailPattern.test(email);
    const isPasswordValid = password.length > 0;

    return isEmailValid && isPasswordValid;
}

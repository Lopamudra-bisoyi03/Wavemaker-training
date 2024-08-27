document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.querySelector("#loginForm");
    const emailInput = document.querySelector("#email");
    const passwordInput = document.querySelector("#password");
    const errorMessage = document.querySelector("#errorMessage");

    // Login validation function
    //const validateLogin = (email, password) => {
        //return email === "bisoyilopamudra@gmail.com" && password === "lopa@03";
   // };

    // Login form submission
    if (loginForm) {
        loginForm.addEventListener("submit", (event) => {
            event.preventDefault();

            const email = emailInput.value.trim();
            const password = passwordInput.value.trim();

            if (validateLogin(email, password)) {
                localStorage.setItem("logged in successfully", "true");
                window.location.href = "index.html";
            } else {
                errorMessage.style.display = "block";
            }
        });
    }
});
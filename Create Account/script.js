document.addEventListener('DOMContentLoaded', () => {
    getUserData();

    document.getElementById('CreateAccount').addEventListener('submit', function(event) {
        event.preventDefault();
        if (validateForm() && validatePasswords()) {
            const userData = collectFormData();
            postData(userData);
        }
    });
});

function getUserData() {
    fetch('https://reqres.in/api/users/2')
        .then(response => response.json())
        .then(data => {
            const user = data.data;
            document.getElementById('first-name').value = user.first_name;
            document.getElementById('last-name').value = user.last_name; 
            document.getElementById('email').value = user.email;
            document.getElementById('age').value = user.dob?.age || '';
            document.getElementById('bio').value = user.bio || ''; 

            // Select gender
            if (user.gender === 'male') {
                document.getElementById('male').checked = true;
            } else {
                document.getElementById('female').checked = true;
            }
        })
        .catch(error => console.error('Error fetching the user data:', error));
}

function collectFormData() {
    return {
        firstName: document.getElementById('first-name').value,
        lastName: document.getElementById('last-name').value,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value,
        confirmPassword: document.getElementById('confirm-password').value,
        gender: document.querySelector('input[name="gender"]:checked').value,
        age: document.getElementById('age').value,
        hobbies: Array.from(document.querySelectorAll('input[name="hobbies"]:checked')).map(el => el.value),
        sourceIncome: document.getElementById('source-income').value,
        income: document.getElementById('income').value,
        bio: document.getElementById('bio').value
    };
}

function postData(userData) {
    fetch('https://reqres.in/api/users/2', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
    })
    .then(response => response.json())
    .then(data => {
        console.log('Success:', data);
        alert('Data has been submitted successfully!');
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error submitting data.');
    });
}

function updateIncomeValue() {
    const incomeRange = document.getElementById('income');
    const incomeValue = document.getElementById('income-value');
    incomeValue.textContent = `${incomeRange.value}K`;
}

function validateForm() {
    const requiredFields = document.querySelectorAll('input[required], textarea[required], select[required]');
    let allFilled = true;

    requiredFields.forEach(field => {
        if (!field.value.trim()) {
            allFilled = false;
            field.classList.add('error');
        } else {
            field.classList.remove('error');
        }
    });

    if (!allFilled) {
        alert('Please fill all required fields.');
    }

    return allFilled;
}

function validatePasswords() {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;

    if (password !== confirmPassword) {
        alert('Passwords do not match!');
        return false;
    }
    return true;
}

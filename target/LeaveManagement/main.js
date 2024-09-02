document.addEventListener('DOMContentLoaded', () => {
    const profileIcon = document.getElementById('profile-icon');
    const leaveRequestsContainer = document.getElementById('leave-requests');
    const teamLeavesSection = document.getElementById('team-leaves-section');
    const teamLeavesMessage = document.getElementById('team-leaves-message');

    // Function to create leave request item
    function createLeaveRequestItem(leave) {
        return `
            <div class="leave-request-item">
                <div class="leave-info">
                    <h3>${leave.leaveType}</h3>
                    <p><strong>From:</strong> ${leave.fromDate}</p>
                    <p><strong>To:</strong> ${leave.toDate}</p>
                    <p><strong>Status:</strong> <span class="status status-${leave.status.toLowerCase()}">${leave.status}</span></p>
                </div>
                <button class="btn-details">Details</button>
            </div>
        `;
    }

    // Function to fetch and display leave requests
    async function fetchLeaveRequests() {
        console.log("Fetching leave requests...");
        try {
            const response = await fetch('fetchLeaveRequests', {
                method: 'GET'
            });

            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            const data = await response.json();
            if (leaveRequestsContainer) {
                leaveRequestsContainer.innerHTML = data.length > 0
                    ? data.map(createLeaveRequestItem).join('')
                    : '<p>No leave requests available.</p>';
            }
        } catch (error) {
            console.error('Error fetching leave requests:', error);
            if (leaveRequestsContainer) {
                leaveRequestsContainer.innerHTML = '<p>Error loading leave requests. Please try again later.</p>';
            }
        }
    }

    // Function to fetch and display team leaves
    const managerId = '1';
    async function fetchTeamLeaves() {
        try {
            const response = await fetch(`http://localhost:8088/LeaveManagement/fetchTeamLeaves?managerId=${managerId}`, {
                method: 'GET'
            });

            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            const data = await response.json();
            if (teamLeavesMessage) {
                teamLeavesMessage.innerHTML = data.length > 0
                    ? data.map(createLeaveRequestItem).join('') : 'No team leave information available.';
            }
        } catch (error) {
            console.error('Error fetching team leaves:', error);
            if (teamLeavesMessage) {
                teamLeavesMessage.innerHTML = 'Error loading team leaves. Please try again later.';
            }
        }
    }

    // Function to handle user login
    async function handleLogin(email, password) {
        try {
            const response = await fetch('/loginCredentials', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            });

            if (!response.ok) {
                throw new Error('Invalid credentials');
            }

            const {employeeName, isManager } = await response.json();

            if (profileIcon) {
                profileIcon.innerText = employeeName.charAt(0).toUpperCase();
            }

            document.getElementById('login-section').style.display = 'none';
            document.getElementById('dashboard').style.display = 'block';

            await fetchLeaveRequests();

            if (isManager) {
                if (teamLeavesSection) {
                    teamLeavesSection.style.display = 'block';
                }
                await fetchTeamLeaves();
            }
        } catch (error) {
            console.error('Login failed:', error);
            const loginError = document.getElementById('login-error');
            if (loginError) {
                loginError.innerText = error.message;
            }
        }
    }

    // Handle login form submission
    const loginForm = document.querySelector('form[action="http://localhost:8088/loginCredentials"]');
    if (loginForm) {
        loginForm.addEventListener('submit', async (event) => {
            event.preventDefault();

            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            await handleLogin(email, password);
        });
    } else {
        console.error('Login form element not found');
    }

    // Handle logout
    document.querySelectorAll('.dropdown a[href="logout.html"]').forEach(link => {
        link.addEventListener('click', () => {
            document.getElementById('dashboard').style.display = 'none';
            document.getElementById('login-section').style.display = 'block';
        });
    });

    // Add event listeners for fetch buttons
    const fetchLeaveRequestsBtn = document.getElementById('fetchLeaveRequestsBtn');
    console.log(fetchLeaveRequestsBtn);
    const fetchTeamLeavesBtn = document.getElementById('fetchTeamLeavesBtn');

    if (fetchLeaveRequestsBtn) {
        fetchLeaveRequestsBtn.addEventListener('click', () => {
            fetchLeaveRequests();
        });
    }

    if (fetchTeamLeavesBtn) {
        fetchTeamLeavesBtn.addEventListener('click', () => {
            fetchTeamLeaves();
        });
    }

    // Call the function to fetch leave requests when the page loads
    fetchLeaveRequests().catch(error => {
        console.error('Error initializing leave requests:', error);
    });
});

Overview
Budget Buddy is a web application designed for managing personal and group finances. Users can track their income and expenses, set personal or group financial goals, and collaborate in groups to achieve shared objectives. This project was developed as a semester assignment for the ОРИС course.
The application emphasizes user authentication, financial tracking, and collaborative goal-setting without real money transactions—contributions are tracked as virtual pledges toward goals.

Features

User Authentication: Secure registration, login, and logout with hashed passwords and session management.
Personal Finance Management: Track income and expenses with CRUD operations. View a dashboard with balance overview (income minus expenses) and recent transactions.
Personal Goals: Create, manage, and contribute to individual financial goals with progress tracking.
Group Management: Create groups, add members, and manage group ownership. Ownership transfers to the oldest member upon the creator's exit; groups are deleted if empty.
Group Goals: Each group has one active goal where members can contribute. Contributions are summed toward the target amount.
Visualization: Progress bars for goal tracking.
Security: Protection against SQL injection, form validation, and prevention of duplicate submissions.
Dashboard: Personalized overview of finances, goals, and groups.

Note: Contributions to goals are independent of personal income/expenses and do not affect the user's balance—they serve as a tracking mechanism only.
Technologies Used

Backend: Java Servlets, JDBC (with PreparedStatement for database interactions)
Frontend: JSP (with JSTL for templating), HTML5, CSS3, Bootstrap (for responsive design and grid system), JavaScript (for form validation and dynamic charts with Chart.js)
Database: PostgreSQL
Architecture: MVC pattern with SOLID principles; code organized into packages (repositories, services, servlets, etc.)
Security & Utilities: SHA-256 for password hashing with random salt, WebFilter (AuthFilter for authentication, UserFilter for access control), WebListener (for service initialization)
Build Tool: Maven 
Other: No ORM frameworks; custom Repositories for data access.
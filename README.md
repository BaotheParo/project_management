# Project Management Application

This is a Spring Boot application for managing projects and tasks, built with Java, Spring Data JPA, and PostgreSQL. It provides REST APIs to perform CRUD operations on projects and tasks, with role-based access control.

## Prerequisites

To run this project, ensure you have the following installed:
- **Java 21** or later
- **Maven 3.8.x** or later
- **PostgreSQL 15** or later
- **IntelliJ IDEA** (optional, for development)
- **Postman** (optional, for testing APIs)

## Setup Instructions

### 1. Clone the Repository
Clone the project from GitHub:
```bash
git clone https://github.com/BaotheParo/project_management.git
cd project_management
```

### 2. Configure the Database
1. **Install PostgreSQL**:
   - Download and install PostgreSQL from [https://www.postgresql.org/download/](https://www.postgresql.org/download/).
   - Ensure the PostgreSQL server is running.

2. **Create a Database**:
   - Open pgAdmin 4 or use the PostgreSQL command line.
   - Create a new database named `project_management`:
     ```sql
     CREATE DATABASE project_management;
     ```

3. **Set Up Database Schema**:
   - Run the following SQL script in pgAdmin 4 (Query Tool) to create tables and excute project_management.sql:
     
### 3. Configure Application Properties
1. Create a file named `application.properties` in `src/main/resources/` with the following content:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/project_management
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```
   - Replace `your_username` and `your_password` with your PostgreSQL credentials.
   - **Note**: This file is not included in the repository (due to `.gitignore`). Obtain it from the project owner or configure it manually.

### 4. Build and Run the Application
1. **Install Dependencies**:
   - Open the project in IntelliJ IDEA (`File` > `Open` > select the `project_management` folder).
   - Sync the project with Maven by clicking the "Reload All Maven Projects" icon in the Maven tab (or run):
     ```bash
     mvn clean install
     ```

2. **Run the Application**:
   - Locate `ProjectManagementApplication.java` in `src/main/java/com/example/project_management`.
   - Right-click and select `Run 'ProjectManagementApplication'`.

3. **Verify the Application**:
   - The application runs on `http://localhost:8080`.
   - Use Postman to test the APIs (see below).

## API Usage

The application provides REST APIs for managing projects and tasks. Use Postman or a similar tool to test them. Below are the key endpoints:

### Project Endpoints
- **Get All Projects**:
  ```
  GET http://localhost:8080/api/projects
  ```
  - Returns a list of all projects.

- **Get Project by ID**:
  ```
  GET http://localhost:8080/api/projects/{id}
  ```
  - Example: `GET http://localhost:8080/api/projects/1`

- **Create a Project**:
  ```
  POST http://localhost:8080/api/projects
  ```
  - Body (JSON):
    ```json
    {
        "name": "Project C",
        "description": "Dự án kiểm thử hệ thống",
        "owner": { "userId": 1 },
        "startDate": "2025-09-01",
        "endDate": "2025-12-01",
        "status": "planned"
    }
    ```

- **Update a Project**:
  ```
  PUT http://localhost:8080/api/projects/{id}
  ```
  - Example: `PUT http://localhost:8080/api/projects/1`
  - Body (JSON):
    ```json
    {
        "name": "Project A Updated",
        "description": "Dự án phát triển ứng dụng web (đã cập nhật)",
        "startDate": "2025-07-10",
        "endDate": "2026-01-31",
        "status": "active"
    }
    ```

- **Delete a Project**:
  ```
  DELETE http://localhost:8080/api/projects/{id}
  ```
  - Example: `DELETE http://localhost:8080/api/projects/1`

### Task Endpoints
- **Get Tasks by Project ID**:
  ```
  GET http://localhost:8080/api/tasks/project/{projectId}
  ```
  - Example: `GET http://localhost:8080/api/tasks/project/1`

- **Create a Task**:
  ```
  POST http://localhost:8080/api/tasks
  ```
  - Body (JSON):
    ```json
    {
        "project": { "projectId": 1 },
        "title": "Kiểm thử API",
        "description": "Kiểm thử các endpoint",
        "assignedUser": { "userId": 2 },
        "priority": "medium",
        "status": "pending",
        "dueDate": "2025-07-30"
    }
    ```

- **Update a Task**:
  ```
  PUT http://localhost:8080/api/tasks/{id}
  ```
  - Example: `PUT http://localhost:8080/api/tasks/1`
  - Body (JSON):
    ```json
    {
        "title": "Thiết kế giao diện (Cập nhật)",
        "description": "Thiết kế UI cho trang chủ và trang đăng nhập",
        "assignedUser": { "userId": 1 },
        "priority": "critical",
        "status": "in_progress",
        "dueDate": "2025-07-22"
    }
    ```

- **Delete a Task**:
  ```
  DELETE http://localhost:8080/api/tasks/{id}
  ```
  - Example: `DELETE http://localhost:8080/api/tasks/1`

## Notes
- **Security**: Authentication is currently disabled for `/api/projects/**` and `/api/tasks/**`. In the future, JWT-based authentication will be implemented via `/api/auth/login`.
- **Database Credentials**: Contact the project owner (BaotheParo) to obtain the `application.properties` file or configure it with your own PostgreSQL credentials.
- **Collaboration**: Create a new branch for your changes (e.g., `git checkout -b feature/my-feature`), commit, and push to GitHub. Use pull requests to merge into `main`.

## Troubleshooting
- **Database Connection Issues**: Ensure PostgreSQL is running and the credentials in `application.properties` are correct.
- **API Errors**: Check the console logs in IntelliJ or enable `spring.jpa.show-sql=true` in `application.properties` to debug SQL queries.
- **Maven Issues**: Run `mvn clean install` to resolve dependency problems.

## Contact
For issues or questions, contact the project owner via GitHub issues or email.

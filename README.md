# Project Management Application

This is a Spring Boot application for managing projects and tasks, built with Java, Spring Data JPA, and PostgreSQL. It provides REST APIs to perform CRUD operations on projects, tasks, and user-project assignments, with JWT-based authentication and role-based access control.

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
    - Run the following SQL script in pgAdmin 4 (Query Tool) to create tables and sample data:
      ```sql
      -- Create ENUM types
      CREATE TYPE project_status AS ENUM ('planned', 'active', 'archived', 'completed');
      CREATE TYPE task_status AS ENUM ('pending', 'in_progress', 'completed', 'blocked', 'deferred');
      CREATE TYPE task_priority AS ENUM ('low', 'medium', 'high', 'critical');
 
      -- Create tables
      CREATE TABLE roles (
          role_id SERIAL PRIMARY KEY,
          role_name VARCHAR(50) NOT NULL UNIQUE,
          description VARCHAR(255)
      );
 
      CREATE TABLE users (
          user_id SERIAL PRIMARY KEY,
          username VARCHAR(50) NOT NULL UNIQUE,
          email VARCHAR(100) NOT NULL UNIQUE,
          password_hash VARCHAR(255) NOT NULL,
          full_name VARCHAR(100),
          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
      );
 
      CREATE TABLE user_roles (
          user_id INT NOT NULL,
          role_id INT NOT NULL,
          assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          PRIMARY KEY (user_id, role_id),
          FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
          FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
      );
 
      CREATE TABLE projects (
          project_id SERIAL PRIMARY KEY,
          name VARCHAR(150) NOT NULL,
          description TEXT,
          owner_user_id INT NOT NULL,
          start_date DATE,
          end_date DATE,
          status project_status DEFAULT 'planned',
          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          FOREIGN KEY (owner_user_id) REFERENCES users(user_id) ON DELETE SET NULL
      );
 
      CREATE TABLE tasks (
          task_id SERIAL PRIMARY KEY,
          project_id INT NOT NULL,
          title VARCHAR(150) NOT NULL,
          description TEXT,
          assigned_user_id INT,
          priority task_priority DEFAULT 'medium',
          status task_status DEFAULT 'pending',
          due_date DATE,
          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE,
          FOREIGN KEY (assigned_user_id) REFERENCES users(user_id) ON DELETE SET NULL
      );
 
      CREATE TABLE user_projects (
          user_id INT NOT NULL,
          project_id INT NOT NULL,
          role_id INT NOT NULL,
          assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          PRIMARY KEY (user_id, project_id),
          FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
          FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE,
          FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
      );
 
      -- Create indexes
      CREATE INDEX idx_projects_owner ON projects(owner_user_id);
      CREATE INDEX idx_tasks_project ON tasks(project_id);
      CREATE INDEX idx_tasks_assigned_user ON tasks(assigned_user_id);
      CREATE INDEX idx_tasks_status ON tasks(status);
      CREATE INDEX idx_tasks_due_date ON tasks(due_date);
      CREATE INDEX idx_projects_status ON projects(status);
      CREATE INDEX idx_projects_dates ON projects(start_date, end_date);
 
      -- Insert sample data
      INSERT INTO roles (role_name, description) 
      VALUES 
          ('ADMIN', 'Quản trị viên hệ thống'),
          ('MANAGER', 'Quản lý dự án'),
          ('MEMBER', 'Thành viên dự án');
 
      INSERT INTO users (username, email, password_hash, full_name) 
      VALUES 
          ('admin', 'admin@example.com', '$2a$10$your_hashed_password_here', 'Quản Trị Viên'),
          ('manager', 'manager@example.com', '$2a$10$your_hashed_password_here', 'Quản Lý');
 
      INSERT INTO user_roles (user_id, role_id) 
      VALUES 
          (1, 1), -- admin có vai trò ADMIN
          (2, 2); -- manager có vai trò MANAGER
 
      INSERT INTO projects (name, description, owner_user_id, start_date, end_date, status)
      VALUES 
          ('Project A', 'Dự án phát triển ứng dụng web', 1, '2025-07-10', '2025-12-31', 'planned'),
          ('Project B', 'Dự án phân tích dữ liệu', 2, '2025-08-01', '2026-01-31', 'active');
 
      INSERT INTO tasks (project_id, title, description, assigned_user_id, priority, status, due_date)
      VALUES 
          (1, 'Thiết kế giao diện', 'Thiết kế UI cho trang chủ', 2, 'high', 'pending', '2025-07-20'),
          (1, 'Cài đặt backend', 'Triển khai API đăng nhập', 1, 'medium', 'in_progress', '2025-07-25'),
          (2, 'Thu thập dữ liệu', 'Thu thập dữ liệu từ nguồn công khai', 2, 'low', 'pending', '2025-08-15'),
          (2, 'Phân tích dữ liệu', 'Phân tích dữ liệu bằng Python', NULL, 'critical', 'pending', '2025-08-20');
 
      INSERT INTO user_projects (user_id, project_id, role_id)
      VALUES 
          (1, 1, 1), -- admin là ADMIN của Project A
          (2, 1, 2), -- manager là MANAGER của Project A
          (2, 2, 2), -- manager là MANAGER của Project B
          (1, 2, 3); -- admin là MEMBER của Project B
      ```

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

The application provides REST APIs for managing projects, tasks, and user-project assignments. Most endpoints require JWT authentication.

### Authentication
- **Login**:
  ```
  POST http://localhost:8080/api/auth/login
  Content-Type: application/json
  ```
    - Body (JSON):
      ```json
      {
          "username": "admin",
          "password": "password123"
      }
      ```
    - Response:
      ```json
      {
          "token": "eyJhbGciOiJIUzUxMiJ9...",
          "username": "admin"
      }
      ```
    - Use the `token` in the `Authorization` header for subsequent requests:
      ```
      Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
      ```

### Project Endpoints
- **Permissions**:
    - Accessible by users with roles `ADMIN`, `MANAGER`, or `MEMBER` assigned to the project via `user_projects`.
    - Only `ADMIN` or `MANAGER` can create/update projects.
    - Only `ADMIN` can delete projects.

- **Get All Projects**:
  ```
  GET http://localhost:8080/api/projects
  Authorization: Bearer <token>
  ```
    - Returns a list of all projects the authenticated user is authorized to view.

- **Get Project by ID**:
  ```
  GET http://localhost:8080/api/projects/{id}
  Authorization: Bearer <token>
  ```
    - Example: `GET http://localhost:8080/api/projects/1`

- **Create a Project**:
  ```
  POST http://localhost:8080/api/projects
  Authorization: Bearer <token>
  Content-Type: application/json
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
  Authorization: Bearer <token>
  Content-Type: application/json
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
  Authorization: Bearer <token>
  ```
    - Example: `DELETE http://localhost:8080/api/projects/1`

### Task Endpoints
- **Permissions**:
    - Accessible by users with roles `ADMIN`, `MANAGER`, or `MEMBER` assigned to the project via `user_projects`.
    - Only `ADMIN` or `MANAGER` can create/update tasks.
    - Only `ADMIN` can delete tasks.

- **Get Tasks by Project ID**:
  ```
  GET http://localhost:8080/api/tasks/project/{projectId}
  Authorization: Bearer <token>
  ```
    - Example: `GET http://localhost:8080/api/tasks/project/1`

- **Create a Task**:
  ```
  POST http://localhost:8080/api/tasks
  Authorization: Bearer <token>
  Content-Type: application/json
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
  Authorization: Bearer <token>
  Content-Type: application/json
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
  Authorization: Bearer <token>
  ```
    - Example: `DELETE http://localhost:8080/api/tasks/1`

### User-Project Assignment Endpoints
- **Permissions**:
    - Only accessible by users with the `ADMIN` role.

- **Assign User to Project**:
  ```
  POST http://localhost:8080/api/user-projects
  Authorization: Bearer <token>
  Content-Type: application/json
  ```
    - Body (JSON):
      ```json
      {
          "user": { "userId": 1 },
          "project": { "projectId": 1 },
          "role": { "roleId": 2 }
      }
      ```

- **Remove User from Project**:
  ```
  DELETE http://localhost:8080/api/user-projects/{userId}/{projectId}
  Authorization: Bearer <token>
  ```
    - Example: `DELETE http://localhost:8080/api/user-projects/1/1`

- **Get Users by Project**:
  ```
  GET http://localhost:8080/api/user-projects/project/{projectId}
  Authorization: Bearer <token>
  ```
    - Example: `GET http://localhost:8080/api/user-projects/project/1`

- **Get Projects by User**:
  ```
  GET http://localhost:8080/api/user-projects/user/{userId}
  Authorization: Bearer <token>
  ```
    - Example: `GET http://localhost:8080/api/user-projects/user/1`

## Notes
- **Security**: Most endpoints require a JWT token in the `Authorization` header. Obtain the token via `/api/auth/login`.
- **Roles**:
    - `ADMIN`: Can perform all operations (create, update, delete projects/tasks; manage user-project assignments).
    - `MANAGER`: Can create/update projects and tasks in projects they are assigned to.
    - `MEMBER`: Can view projects and tasks they are assigned to.
- **Database Credentials**: Contact the project owner (BaotheParo) to obtain the `application.properties` file or configure it with your own PostgreSQL credentials.
- **Collaboration**: Create a new branch for your changes (e.g., `git checkout -b feature/my-feature`), commit, and push to GitHub. Use pull requests to merge into `main`.

## Troubleshooting
- **Authentication Errors**: Ensure the `Authorization` header includes a valid JWT token (`Bearer <token>`).
- **Database Connection Issues**: Verify PostgreSQL is running and credentials in `application.properties` are correct.
- **API Errors**: Check console logs in IntelliJ or enable `spring.jpa.show-sql=true` in `application.properties` to debug SQL queries.
- **Maven Issues**: Run `mvn clean install` to resolve dependency problems.

## Contact
For issues or questions, contact the project owner via GitHub issues or email.
-- Tạo kiểu ENUM trong PostgreSQL
CREATE TYPE project_status AS ENUM ('planned', 'active', 'archived', 'completed');
CREATE TYPE task_status AS ENUM ('pending', 'in_progress', 'completed', 'blocked', 'deferred');
CREATE TYPE task_priority AS ENUM ('low', 'medium', 'high', 'critical');

-- Bảng Roles
CREATE TABLE roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- Bảng Users
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng UserRoles (phân quyền toàn cục)
CREATE TABLE user_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
);

-- Bảng Projects
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

-- Bảng Tasks
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

-- Bảng UserProjects (phân quyền trong từng dự án)
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

-- Chỉ mục (Indexes)
CREATE INDEX idx_projects_owner ON projects(owner_user_id);
CREATE INDEX idx_tasks_project ON tasks(project_id);
CREATE INDEX idx_tasks_assigned_user ON tasks(assigned_user_id);
CREATE INDEX idx_tasks_status ON tasks(status);
CREATE INDEX idx_tasks_due_date ON tasks(due_date);
CREATE INDEX idx_projects_status ON projects(status);
CREATE INDEX idx_projects_dates ON projects(start_date, end_date);



-- Thêm roles
INSERT INTO roles (role_name, description) 
VALUES 
    ('ADMIN', 'Quản trị viên hệ thống'),
    ('MANAGER', 'Quản lý dự án'),
    ('MEMBER', 'Thành viên dự án');

-- Thêm user mẫu
INSERT INTO users (username, email, password_hash, full_name) 
VALUES 
    ('admin', 'admin@example.com', '$2a$10$examplehashedpassword', 'Quản Trị Viên'),
    ('manager', 'manager@example.com', '$2a$10$examplehashedpassword', 'Quản Lý');

-- Thêm user_roles
INSERT INTO user_roles (user_id, role_id) 
VALUES 
    (1, 1), -- admin có vai trò ADMIN
    (2, 2); -- manager có vai trò MANAGER

-- Thêm dữ liệu vào bảng projects
INSERT INTO projects (name, description, owner_user_id, start_date, end_date, status)
VALUES 
    ('Project A', 'Dự án phát triển ứng dụng web', 1, '2025-07-10', '2025-12-31', 'planned'),
    ('Project B', 'Dự án phân tích dữ liệu', 2, '2025-08-01', '2026-01-31', 'active');

-- Thêm dữ liệu vào bảng tasks
INSERT INTO tasks (project_id, title, description, assigned_user_id, priority, status, due_date)
VALUES 
    (1, 'Thiết kế giao diện', 'Thiết kế UI cho trang chủ', 2, 'high', 'pending', '2025-07-20'),
    (1, 'Cài đặt backend', 'Triển khai API đăng nhập', 1, 'medium', 'in_progress', '2025-07-25'),
    (2, 'Thu thập dữ liệu', 'Thu thập dữ liệu từ nguồn công khai', 2, 'low', 'pending', '2025-08-15'),
    (2, 'Phân tích dữ liệu', 'Phân tích dữ liệu bằng Python', NULL, 'critical', 'pending', '2025-08-20');

-- Thêm dữ liệu vào bảng user_projects
INSERT INTO user_projects (user_id, project_id, role_id)
VALUES 
    (1, 1, 1), -- admin là ADMIN của Project A
    (2, 1, 2), -- manager là MANAGER của Project A
    (2, 2, 2), -- manager là MANAGER của Project B
    (1, 2, 3); -- admin là MEMBER của Project B

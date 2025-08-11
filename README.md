# URL Shortener Application

## 📌 Project Overview
This is a **Spring Boot-based URL Shortener** application that allows users to shorten long URLs into easily shareable links.  
Users can create either **public** links without authentication or **private** links that are tied to their account.  
An integrated **admin panel** enables administrators to manage users and their shortened URLs.

The application includes **session-based authentication** using Spring Security and is fully containerized with **Docker Compose**, making it cloud-ready and easy to deploy.

---

## 🚀 Features
- **Public & Private URL Shortening**  
  - Public links can be created without authentication.  
  - Private links require a registered account.  

- **User Management**  
  - User registration and login with session-based authentication.  
  - Password encryption for secure storage.  

- **Admin Panel**  
  - Manage users and their created links.  
  - Modify, disable, or delete URLs.

- **Database Migrations**  
  - Managed with **Flyway** for consistent schema updates.

- **Docker Integration**  
  - Automatic container management with `spring-boot-docker-compose`.  
  - Easy setup for development and production environments.

---

## 🛠 Tech Stack
- **Backend:** Spring Boot 3, Spring Data JPA, Spring Security (Session-based Auth)
- **Frontend:** Thymeleaf, Bootstrap 5
- **Database:** PostgreSQL
- **Migrations:** Flyway
- **Containerization:** Docker Compose
- **Validation:** Spring Boot Starter Validation
- **Template Engine Enhancements:** Thymeleaf Layout Dialect
- **Testing:** Spring Boot Test, Spring Security Test

---

## 📡 API Endpoints Overview

| Method | Endpoint             | Description |
|--------|----------------------|-------------|
| GET    | `/`                  | Home page with URL shortening form |
| POST   | `/shorten`           | Create a new shortened URL (public or private) |
| GET    | `/{shortCode}`       | Redirect to the original URL |
| GET    | `/login`             | User login page |
| POST   | `/login`             | Process login |
| GET    | `/register`          | User registration page |
| POST   | `/register`          | Create a new user |
| GET    | `/admin/users`       | Admin dashboard for managing users |
| GET    | `/admin/urls`        | Admin dashboard for managing URLs |

---

## 🏗 Architecture Notes
- **Authentication:** Implemented with **Spring Security session-based authentication**, ensuring secure user sessions without relying on JWT.  
- **Database Layer:** Uses **Spring Data JPA** for repository management and **PostgreSQL** for data storage.  
- **Migrations:** **Flyway** handles schema migrations automatically during startup.  
- **Containerization:** The application is **Docker Compose-ready**, automatically starting the required PostgreSQL service if not running.  
- **Frontend:** Thymeleaf templates are styled with Bootstrap for a responsive UI.

---

## 🔧 Installation & Run
```bash
# Clone repository
git clone https://github.com/your-username/url-shortener.git
cd url-shortener

# Start with Docker Compose
docker-compose up

# Or run without Docker
mvn spring-boot:run

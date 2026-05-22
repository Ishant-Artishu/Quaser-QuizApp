# Quaser - Quiz App

A high-performance, containerized REST API developed for a dynamic quiz platform. Built with Spring Boot and PostgreSQL, it focuses on scalability, secure authentication, and a smooth developer experience through Docker.

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Docker](#docker)
- [Contributing](#contributing)
- [License](#license)

## ✨ Features

- **High-Performance REST API** - Optimized for speed and reliability
- **Secure Authentication** - Robust authentication mechanisms for user security
- **Scalable Architecture** - Designed to handle growing user bases
- **Docker Support** - Containerized deployment for easy management
- **PostgreSQL Database** - Reliable and powerful data persistence
- **Spring Boot Framework** - Modern Java framework for rapid development

## 🛠️ Tech Stack

- **Backend**: Java with Spring Boot
- **Database**: PostgreSQL
- **Containerization**: Docker
- **Build Tool**: Maven/Gradle (implied by Spring Boot)

### Language Composition
- **Java**: 98.6%
- **Dockerfile**: 1.4%

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- Java 11 or higher
- PostgreSQL 12 or higher
- Docker (optional, but recommended)
- Maven or Gradle
- Git

## 🚀 Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Ishant-Artishu/Quaser-QuizApp.git
   cd Quaser-QuizApp
   ```

2. **Build the project**
   ```bash
   mvn clean install
   # or with Gradle
   gradle build
   ```

## ⚙️ Configuration

1. **Set up PostgreSQL Database**
   ```bash
   createdb quaser_db
   ```

2. **Configure application properties**
   
   Create or update `application.properties` or `application.yml` in `src/main/resources/`:
   
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/quaser_db
   spring.datasource.username=postgres
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=false
   ```

3. **Update other configurations as needed** for authentication, JWT tokens, etc.

## ▶️ Running the Application

### Local Development

```bash
mvn spring-boot:run
# or with Gradle
gradle bootRun
```

The application will start on `http://localhost:8080` by default.

### With Docker

1. **Build the Docker image**
   ```bash
   docker build -t quaser-quiz-app .
   ```

2. **Run with Docker Compose** (if docker-compose.yml exists)
   ```bash
   docker-compose up -d
   ```

3. **Or run the container directly**
   ```bash
   docker run -p 8080:8080 \
     -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/quaser_db \
     -e SPRING_DATASOURCE_USERNAME=postgres \
     -e SPRING_DATASOURCE_PASSWORD=your_password \
     quaser-quiz-app
   ```

## 📚 API Documentation

API documentation will be available at:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html` (if Springfox/Springdoc is configured)
- **API Docs**: `http://localhost:8080/v3/api-docs`

### Example Endpoints

- `GET /api/quizzes` - Retrieve all quizzes
- `POST /api/quizzes` - Create a new quiz
- `GET /api/quizzes/{id}` - Get a specific quiz
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

*(Adjust based on your actual API endpoints)*

## 🐳 Docker

### Dockerfile

The project includes a Dockerfile for containerized deployment:

```dockerfile
# Build and run with Docker
docker build -t quaser-quiz-app:latest .
docker run -p 8080:8080 quaser-quiz-app:latest
```

### Environment Variables

Configure the following environment variables when running with Docker:

- `SPRING_DATASOURCE_URL` - PostgreSQL connection URL
- `SPRING_DATASOURCE_USERNAME` - Database username
- `SPRING_DATASOURCE_PASSWORD` - Database password
- `SERVER_PORT` - Application port (default: 8080)

## 🤝 Contributing

Contributions are welcome! To get started:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

**Author**: [Ishant-Artishu](https://github.com/Ishant-Artishu)

**Repository**: [Quaser-QuizApp](https://github.com/Ishant-Artishu/Quaser-QuizApp)

For questions or issues, please open an issue on the GitHub repository.

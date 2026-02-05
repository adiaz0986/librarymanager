# 📚 Library Manager

Sistema de gestión de biblioteca desarrollado con Spring Boot que permite administrar libros y préstamos de manera eficiente.

## 🚀 Características

- ✅ Gestión completa de libros (CRUD)
- ✅ Sistema de préstamos con fechas de vencimiento
- ✅ Control de disponibilidad de libros
- ✅ Búsqueda y filtrado de libros
- ✅ Estadísticas del sistema
- ✅ Autenticación JWT
- ✅ API REST documentada con Swagger
- ✅ Dockerizado con Docker Compose

## 📋 Requisitos Previos

- **Java 21** o superior
- **PostgreSQL 16**
- **Maven 3.8+** o **Gradle 8+**
- **Docker & Docker Compose** (opcional, para despliegue containerizado)

## 🛠️ Tecnologías Utilizadas

- **Spring Boot 3.4.1**
- **Spring Security** con JWT
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **Swagger/OpenAPI 3**
- **Docker**

## ⚙️ Configuración del Proyecto

### 1. Clonar el repositorio
```bash
git clone <repository-url>
cd library-manager
```

### 2. Configuración de Base de Datos

#### Opción A: PostgreSQL Local

Crear la base de datos:
```sql
CREATE DATABASE librarydb;
CREATE USER libraryuser WITH PASSWORD 'librarypass';
GRANT ALL PRIVILEGES ON DATABASE librarydb TO libraryuser;
```

Configurar en `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/librarydb
spring.datasource.username=libraryuser
spring.datasource.password=librarypass
```

#### Opción B: Usar Docker Compose (Recomendado)
```bash
docker-compose up -d
```

Esto levantará:
- PostgreSQL en `localhost:5432`
- API en `localhost:8080`

### 3. Ejecutar la Aplicación

#### Con Maven:
```bash
./mvnw spring-boot:run
```

#### Con Gradle:
```bash
./gradlew bootRun
```

#### Con Docker Compose:
```bash
docker-compose up --build
```

### 4. Acceder a la Aplicación

- **Frontend**: [http://localhost:8080](http://localhost:8080)
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **API Docs**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## 🔐 Autenticación

El sistema requiere autenticación JWT. Primero debes hacer login:

### Login
```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin"
}
```

Usar el token en las siguientes peticiones:
```bash
Authorization: Bearer <token>
```

## 📡 API Endpoints

### 🔑 Autenticación

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/login` | Iniciar sesión |
| POST | `/api/auth/register` | Registrar usuario |

### 📖 Libros

| Método | Endpoint | Descripción | Requiere Auth |
|--------|----------|-------------|---------------|
| GET | `/api/books` | Listar todos los libros | ✅ |
| GET | `/api/books/search?q={keyword}` | Buscar libros por título/autor | ✅ |
| GET | `/api/books/stats` | Obtener estadísticas | ✅ |
| POST | `/api/books` | Crear un nuevo libro | ✅ |
| PUT | `/api/books/{id}` | Actualizar libro | ✅ |
| DELETE | `/api/books/{id}` | Eliminar libro | ✅ |

### 📚 Préstamos

| Método | Endpoint | Descripción | Requiere Auth |
|--------|----------|-------------|---------------|
| GET | `/api/loans` | Listar todos los préstamos | ✅ |
| GET | `/api/loans/active` | Listar préstamos activos | ✅ |
| GET | `/api/loans/overdue` | Listar préstamos vencidos | ✅ |
| GET | `/api/loans/borrower/{email}` | Buscar por email del solicitante | ✅ |
| GET | `/api/loans/{id}` | Obtener préstamo por ID | ✅ |
| POST | `/api/loans` | Crear nuevo préstamo | ✅ |
| PUT | `/api/loans/{id}/return` | Devolver libro | ✅ |

## 📝 Ejemplos de Uso

### Crear un Libro
```bash
POST /api/books
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "9780132350884",
  "publicationYear": 2008
}
```

### Crear un Préstamo
```bash
POST /api/loans
Authorization: Bearer <token>
Content-Type: application/json

{
  "bookId": 1,
  "borrowerName": "Juan Pérez",
  "borrowerEmail": "juan@example.com"
}
```

### Devolver un Libro
```bash
PUT /api/loans/1/return
Authorization: Bearer <token>
```

### Buscar Libros
```bash
GET /api/books/search?q=clean
Authorization: Bearer <token>
```


## 🧪 Testing

Ejecutar los tests:
```bash
# Maven
./mvnw test

# Gradle
./gradlew test
```

El proyecto incluye:
- ✅ Tests unitarios de servicios
- ✅ Tests de integración de controladores
- ✅ Cobertura de casos críticos

## 🐳 Docker

### Construir imagen
```bash
docker build -t library-manager .
```

### Ejecutar con Docker Compose
```bash
# Levantar servicios
docker-compose up -d

# Ver logs
docker-compose logs -f api

# Detener servicios
docker-compose down

# Detener y eliminar volúmenes
docker-compose down -v
```


## 🔧 Configuración Avanzada

### Variables de Entorno
```bash
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/librarydb
SPRING_DATASOURCE_USERNAME=libraryuser
SPRING_DATASOURCE_PASSWORD=librarypass
SERVER_PORT=8080
JWT_SECRET=your-secret-key
JWT_EXPIRATION=86400000
```

### Profiles

- `dev`: Desarrollo con H2 (en memoria)
- `prod`: Producción con PostgreSQL

## 👥 Autor

**Tu Nombre** - [GitHub](https://github.com/tu-usuario)


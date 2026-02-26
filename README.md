# MS Books Catalogue - Microservicio Buscador--

![](https://img.shields.io/github/stars/pandao/editor.md.svg) ![](https://img.shields.io/github/forks/pandao/editor.md.svg) ![](https://img.shields.io/github/tag/pandao/editor.md.svg) ![](https://img.shields.io/github/release/pandao/editor.md.svg) ![](https://img.shields.io/github/issues/pandao/editor.md.svg) ![](https://img.shields.io/bower/v/editor.md.svg)

Microservicio de catálogo de libros para el proyecto transversal **"Relatos de Papel"**.
Implementa la API REST del buscador con operaciones CRUD (Create, Read, Update, Delete), búsquedas filtradas avanzadas y soft delete, arquitectura de microservicios con Spring Boot. **Grupo 27**

## 📋 Descripción

Este microservicio gestiona el catálogo de libros de la aplicación, proporcionando:

- **CRUD completo** de libros con validaciones
- **Búsquedas dinámicas** por múltiples criterios (autor, categoría, rating, fechas, etc.)
- **Soft Delete** (eliminación lógica) - los libros cambian a estado HIDDEN

-  Swagger/OpenAPI
- **Persistencia** con PostgreSQL (Docker) o H2 (desarrollo)
- **Preparado para Eureka** (Service Discovery)

## 🛠️ Tecnologías

- **Java 17/21/25** (OpenJDK)
- **Spring Boot 4.0.2**
- **Spring Data JPA** (Hibernate 7.x)
- **PostgreSQL 16** / H2 Database
- **Maven 3.6+**
- **Docker & Docker Compose**
- **SpringDoc OpenAPI** (Swagger UI)
- **Lombok**
- **Spring Boot DevTools** (hot reload)

## 📦 Requisitos Previos

- **Java 17+** JDK instalado
- **Maven 3.6+**
- **Docker Desktop** (para PostgreSQL)
- **Postman** o similar (para pruebas)

##  ⚙️Cómo ejecutar la aplicación

### PostgreSQL

1. **Levantar la base de datos:**

`$ docker-compose up -d`

## 🚀 Compilar y ejecutar:

Dentro de la ubicación del proyecto (en una Terminal) ejecutar:

`$ ./mvnw clean install`

`$ ./mvnw spring-boot:run`

en desarrollo (con H2):

`./mvnw spring-boot:run`

## 📄Verificar instalación

- API Base: http://localhost:8081/api/v1/books
- Swagger UI: http://localhost:8081/swagger-ui.html
- H2 Console (si aplica): http://localhost:8081/h2-console

## Variables de Entorno
| Variable                | Descripción                  | Default                                          |
| ----------------------- | ---------------------------- | ------------------------------------------------ |
| `SERVER_PORT`           | Puerto del microservicio     | `8081`                                           |
| `DB_URL`                | URL JDBC de PostgreSQL       | `jdbc:postgresql://localhost:5432/bookcatalogue` |
| `DB_USERNAME`           | Usuario PostgreSQL           | `<Define un usuario>`                                       |
| `DB_PASSWORD`           | Contraseña PostgreSQL        | `<Defina una contraseña>`                                    |
| `EUREKA_CLIENT_ENABLED` | Habilitar registro en Eureka | `false` (para desarrollo)                        |
## 📁 Estructura del Proyecto
ms-books-catalogue/
├── src/main/java/com/master/ms_books_catalogue/
│   ├── MsBooksCatalogueApplication.java
│   ├── config/
│   │   └── SwaggerConfig.java          # Configuración OpenAPI
│   ├── controller/
│   │   └── BookController.java         # API REST endpoints
│   ├── entity/
│   │   └── Book.java                   # Entidad JPA
│   ├── repository/
│   │   └── BookRepository.java         # Spring Data JPA
│   ├── service/
│   │   └── BookService.java            # Lógica de negocio
│   └── specification/
│       └── BookSpecification.java      # Consultas dinámicas
├── src/main/resources/
│   └── application.yml
├── docker-compose.yml
├── pom.xml
└── README.md

## 🔗 Endpoints

URL Base: http://localhost:8081/api/v1/books

EndPoints disponibles:

| Método     | Endpoint                          | Descripción                              | Query Params                                                                                                     |
| ---------- | --------------------------------- | ---------------------------------------- | ---------------------------------------------------------------------------------------------------------------- |
| **POST**   | `/api/v1/books`                   | Crear nuevo libro                        | Body: JSON Book                                                                                                  |
| **GET**    | `/api/v1/books`                   | Listar libros (con filtros)              | `title`, `author`, `category`, `status`, `isbn`, `minRating`, `maxRating`, `startDate`, `endDate`, `onlyVisible` |
| **GET**    | `/api/v1/books/{id}`              | Obtener libro por ID                     | -                                                                                                                |
| **PUT**    | `/api/v1/books/{id}`              | Actualizar libro completo                | Body: JSON Book                                                                                                  |
| **PATCH**  | `/api/v1/books/{id}`              | Actualización parcial                    | Body: JSON partial Book                                                                                          |
| **DELETE** | `/api/v1/books/{id}`              | Soft delete (ocultar libro)              | -                                                                                                                |
| **GET**    | `/api/v1/books/{id}/availability` | Verificar disponibilidad (para Operador) | -                                                                                                                |

## Ejemplos de Uso

Crear libro (POST)

`curl -X POST http://localhost:8081/api/v1/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Drácula",
    "description": "El clásico de vampiros",
    "price": 49.99,
    "category": "Terror",
    "isbn": "REF04-2323-45",
    "stockQuantity": 8,
    "coverImageUrl": "https://covers.openlibrary.org/b/id/7984916-L.jpg",
    "status": "AVAILABLE",
    "author": "Bram Stoker",
    "publicationDate": "1897-05-26",
    "rating": 4
  }'`

#Búsqueda avanzada (GET)

#### Buscar por categoría y rating mínimo
`GET http://localhost:8081/api/v1/books?category=Terror&minRating=4&onlyVisible=true`

### Buscar por autor (parcial)
`GET http://localhost:8081/api/v1/books?author=King`

### Ver TODOS los libros incluyendo ocultos (eliminados)
`GET http://localhost:8081/api/v1/books?onlyVisible=false`

### Método DELETE

Esto NO borra el registro, solo cambia status a HIDDEN

`DELETE http://localhost:8081/api/v1/books/1`

Colección JSON (para probar con Postman) 👉🏼 [MS-Books-Catalogue.postman_collection.json](https://github.com/Relatos-de-Papel-Grupo-27/ms-books-catalogue/blob/main/MS-Books-Catalogue.postman_collection.json "MS-Books-Catalogue.postman_collection.json")

#📚 Documentación API

Acceder a Swagger UI para probar endpoints interactivamente:

- URL: http://localhost:8081/swagger-ui.html
- OpenAPI JSON: http://localhost:8081/v3/api-docs

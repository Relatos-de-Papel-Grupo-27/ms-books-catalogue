package com.master.ms_books_catalogue.controller;

import com.master.ms_books_catalogue.entity.Book;
import com.master.ms_books_catalogue.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")  // Ruta base: http://localhost:8081/api/v1/books
@CrossOrigin(origins = "*")    // Permitir llamadas desde cualquier frontend (por ahora)
@Tag(name = "Catálogo de Libros", description = "Operaciones CRUD y búsquedas para la gestión de libros")
public class BookController {

    @Autowired
    private BookService bookService;

    // CREATE - POST /api/v1/books
    @Operation(summary = "Crear un nuevo libro",
            description = "Registra un libro en el catálogo con todos sus atributos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Libro creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o ISBN duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book newBook = bookService.createBook(book);
        return new ResponseEntity<>(newBook, HttpStatus.CREATED);
    }

    // READ ALL (con filtros opcionales) - GET /api/v1/books
    @Operation(summary = "Listar libros",
            description = "Obtiene todos los libros con opción de filtrar por múltiples criterios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de libros obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(
            @Parameter(description = "Título del libro (búsqueda parcial)")
            @RequestParam(required = false) String title,

            @Parameter(description = "Autor del libro (búsqueda parcial)")
            @RequestParam(required = false) String author,

            @Parameter(description = "Categoría exacta")
            @RequestParam(required = false) String category,

            @Parameter(description = "Estado del libro (AVAILABLE, OUT_OF_STOCK, HIDDEN)")
            @RequestParam(required = false) Book.BookStatus status,

            @Parameter(description = "ISBN exacto")
            @RequestParam(required = false) String isbn,

            @Parameter(description = "Valoración mínima (1-5)")
            @RequestParam(required = false) Integer minRating,

            @Parameter(description = "Valoración máxima (1-5)")
            @RequestParam(required = false) Integer maxRating,

            @Parameter(description = "Fecha de publicación inicial (YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "Fecha de publicación final (YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @Parameter(description = "true: solo visibles (no HIDDEN), false: todos incluyendo eliminados")
            @RequestParam(required = false, defaultValue = "true")
            Boolean onlyVisible
    )
        {
            List<Book> books = bookService.findBooks(title, author, category, status,
                    isbn, minRating, maxRating,
                    startDate, endDate, onlyVisible);
            return ResponseEntity.ok(books);
        }

    // OBTENER por id - GET /api/v1/books/{id}
    @Operation(summary = "Obtener libro por ID", description = "Busca un libro específico por su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro encontrado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(
            @Parameter(description = "ID del libro", required = true)
            @PathVariable Long id) {
        Book book = bookService.findById(id);
        return ResponseEntity.ok(book);
    }

    // UPDATE completo - PUT /api/v1/books/{id}
    @Operation(summary = "Actualizar libro completo", description = "Reemplaza todos los campos del libro existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro actualizado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @Parameter(description = "ID del libro a actualizar") @PathVariable Long id,
            @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    // UPDATE parcial - PATCH /api/books/{id}
    @Operation(summary = "Actualización parcial", description = "Modifica solo los campos proporcionados del libro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro actualizado parcialmente"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Book> patchBook(
            @Parameter(description = "ID del libro") @PathVariable Long id,
            @RequestBody Book book) {
        Book updatedBook = bookService.patchBook(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    // DELETE - DELETE /api/books/{id}
    @Operation(summary = "Eliminar libro (Soft Delete)",
            description = "Cambia el estado del libro a HIDDEN. No elimina físicamente de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Libro marcado como oculto exitosamente"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "ID del libro a ocultar") @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint especial para el Operador - Verificar si libro existe y está disponible

    @Operation(summary = "Verificar disponibilidad",
            description = "Endpoint para el microservicio Operador. Valida si el libro existe, no está oculto y tiene stock.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disponibilidad verificada (true/false)")
    })
    @GetMapping("/{id}/availability")
    public ResponseEntity<Boolean> checkAvailability(
            @Parameter(description = "ID del libro") @PathVariable Long id) {
        boolean available = bookService.isBookAvailable(id);
        return ResponseEntity.ok(available);
    }
}
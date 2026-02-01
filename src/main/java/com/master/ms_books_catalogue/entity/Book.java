package com.master.ms_books_catalogue.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "books")
@Data              // Lombok: genera getters, setters, toString, etc.
@NoArgsConstructor // Constructor vacío obligatorio para JPA
@AllArgsConstructor // Constructor con todos los campos
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;  // nombre

    @Column(length = 2000)
    private String description;  // descripcion

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;  // precio

    @Column(nullable = false, length = 100)
    private String category;  // categoria

    @Column(unique = true, length = 20)
    private String isbn;  // isbn

    @Column(nullable = false)
    private Integer stockQuantity;  // cantidad

    @Column(length = 500)
    private String coverImageUrl;   // fotos

    @Enumerated(EnumType.STRING)  // Guarda el nombre del enum como texto
    @Column(nullable = false)
    private BookStatus status;      // estado (AVAILABLE, OUT_OF_STOCK, HIDDEN)

    @Column(nullable = false, length = 100)
    private String author;          // autor

    @Column(name = "publication_date")
    private LocalDate publicationDate;  // fech_publicacion

    @Column
    private Integer rating;         // valoracion (1-5)

    // Enum interno para el estado
    public enum BookStatus {
        AVAILABLE,      // Disponible
        OUT_OF_STOCK,   // Agotado (cantidad = 0)
        HIDDEN          // Oculto (no visible para operador ni frontend)
    }
}
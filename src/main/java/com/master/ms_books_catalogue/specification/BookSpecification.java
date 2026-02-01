package com.master.ms_books_catalogue.specification;

import com.master.ms_books_catalogue.entity.Book;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookSpecification {

    public static Specification<Book> filterBooks(
            String title,
            String author,
            String category,
            Book.BookStatus status,
            String isbn,
            Integer minRating,
            Integer maxRating,
            LocalDate startDate,
            LocalDate endDate,
            Boolean onlyVisible) {  // true = solo AVAILABLE/OUT_OF_STOCK, false = todos

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Búsqueda por título (contiene, ignorando mayúsculas)
            if (title != null && !title.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + title.toLowerCase() + "%"
                ));
            }

            // Búsqueda por autor (contiene)
            if (author != null && !author.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("author")),
                        "%" + author.toLowerCase() + "%"
                ));
            }

            // Búsqueda exacta por categoría
            if (category != null && !category.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("category"), category));
            }

            // Búsqueda por estado específico
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            // Búsqueda por ISBN (exacto)
            if (isbn != null && !isbn.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("isbn"), isbn));
            }

            // Rango de valoración
            if (minRating != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), minRating));
            }
            if (maxRating != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("rating"), maxRating));
            }

            // Rango de fechas
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("publicationDate"), startDate));
            }
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("publicationDate"), endDate));
            }

            // Solo visibles (no ocultos) - IMPORTANTE para el operador
            if (onlyVisible != null && onlyVisible) {
                predicates.add(criteriaBuilder.notEqual(root.get("status"), Book.BookStatus.HIDDEN));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
package com.master.ms_books_catalogue.repository;

import com.master.ms_books_catalogue.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    // JpaRepository te da: save, findById, findAll, deleteById...
    // JpaSpecificationExecutor te permite hacer búsquedas dinámicas complejas
    boolean existsByIsbn(String isbn);
}
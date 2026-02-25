package com.master.ms_books_catalogue.service;

import com.master.ms_books_catalogue.entity.Book;
import com.master.ms_books_catalogue.repository.BookRepository;
import com.master.ms_books_catalogue.specification.BookSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.master.ms_books_catalogue.search.document.BookDocument;
import com.master.ms_books_catalogue.search.repository.BookSearchRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookSearchRepository bookSearchRepository;

    private void indexBook(Book book) {

        // Si está oculto, lo eliminamos del índice
        if (book.getStatus() == Book.BookStatus.HIDDEN) {
            bookSearchRepository.deleteById(book.getId().toString());
            return;
        }

        BookDocument doc = new BookDocument();
        doc.setId(book.getId().toString());
        doc.setTitle(book.getTitle());
        doc.setDescription(book.getDescription());
        doc.setCategory(book.getCategory());
        doc.setAuthor(book.getAuthor());
        doc.setPrice(book.getPrice().doubleValue());
        doc.setRating(book.getRating());
        doc.setStatus(book.getStatus().name());
        doc.setStockQuantity(book.getStockQuantity());
        doc.setPublicationDate(book.getPublicationDate());

        bookSearchRepository.save(doc);
    }


    // CREATE
    public Book createBook(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new RuntimeException("Ya existe un libro con ese ISBN");
        }

        // Lógica de negocio: si cantidad = 0, estado = OUT_OF_STOCK
        if (book.getStockQuantity() == 0) {
            book.setStatus(Book.BookStatus.OUT_OF_STOCK);
        } else if (book.getStatus() == null) {
            book.setStatus(Book.BookStatus.AVAILABLE);
        }

        Book saved = bookRepository.save(book);
        indexBook(saved);
        return saved;
    }

    // READ - Obtener todos con filtros opcionales
    public List<Book> findBooks(String title, String author, String category,
                                Book.BookStatus status, String isbn,
                                Integer minRating, Integer maxRating,
                                LocalDate startDate, LocalDate endDate,
                                Boolean onlyVisible) {

        return bookRepository.findAll(BookSpecification.filterBooks(
                title, author, category, status, isbn,
                minRating, maxRating, startDate, endDate, onlyVisible
        ));
    }

    // READ - Por ID
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + id));
    }

    // UPDATE completo (PUT)
    public Book updateBook(Long id, Book bookDetails) {
        Book book = findById(id);

        book.setTitle(bookDetails.getTitle());
        book.setDescription(bookDetails.getDescription());
        book.setPrice(bookDetails.getPrice());
        book.setCategory(bookDetails.getCategory());
        book.setIsbn(bookDetails.getIsbn());
        book.setStockQuantity(bookDetails.getStockQuantity());
        book.setCoverImageUrl(bookDetails.getCoverImageUrl());
        book.setStatus(bookDetails.getStatus());
        book.setAuthor(bookDetails.getAuthor());
        book.setPublicationDate(bookDetails.getPublicationDate());
        book.setRating(bookDetails.getRating());

        // Recalcular estado si cambió stock
        if (book.getStockQuantity() == 0) {
            book.setStatus(Book.BookStatus.OUT_OF_STOCK);
        }

        Book updated = bookRepository.save(book);
        indexBook(updated);
        return updated;
    }

    // UPDATE parcial (PATCH) - Solo campos no nulos
    public Book patchBook(Long id, Book bookDetails) {
        Book book = findById(id);

        if (bookDetails.getTitle() != null) book.setTitle(bookDetails.getTitle());
        if (bookDetails.getDescription() != null) book.setDescription(bookDetails.getDescription());
        if (bookDetails.getPrice() != null) book.setPrice(bookDetails.getPrice());
        if (bookDetails.getCategory() != null) book.setCategory(bookDetails.getCategory());
        if (bookDetails.getIsbn() != null) book.setIsbn(bookDetails.getIsbn());
        if (bookDetails.getStockQuantity() != null) {
            book.setStockQuantity(bookDetails.getStockQuantity());
            if (book.getStockQuantity() == 0) {
                book.setStatus(Book.BookStatus.OUT_OF_STOCK);
            }
        }
        if (bookDetails.getCoverImageUrl() != null) book.setCoverImageUrl(bookDetails.getCoverImageUrl());
        if (bookDetails.getStatus() != null) book.setStatus(bookDetails.getStatus());
        if (bookDetails.getAuthor() != null) book.setAuthor(bookDetails.getAuthor());
        if (bookDetails.getPublicationDate() != null) book.setPublicationDate(bookDetails.getPublicationDate());
        if (bookDetails.getRating() != null) book.setRating(bookDetails.getRating());

        Book updated = bookRepository.save(book);
        indexBook(updated);
        return updated;
    }

    // DELETE
    public void deleteBook(Long id) {
        Book book = findById(id);
        book.setStatus(Book.BookStatus.HIDDEN);  // Eliminación lógica
        Book updated = bookRepository.save(book);

        // Eliminar del índice
        bookSearchRepository.deleteById(updated.getId().toString());
    }

    // Método especial para el Operador: verificar disponibilidad
    public boolean isBookAvailable(Long id) {
        try {
            Book book = findById(id);
            return book.getStatus() != Book.BookStatus.HIDDEN &&
                    book.getStockQuantity() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    //Metodo de busqueda masivo
    public List<BookDocument> searchBooks(String text) {
        return bookSearchRepository.findByTitleContaining(text);
    }

    public void reindexAllBooks() {

        // Limpia el índice antes de reindexar (opcional pero recomendado)
        bookSearchRepository.deleteAll();

        var books = bookRepository.findAll();

        var documents = books.stream()
                .map(this::convertToDocument)
                .toList();

        bookSearchRepository.saveAll(documents);

        System.out.println("Total libros indexados: " + documents.size());
    }

    private BookDocument convertToDocument(Book book) {

        BookDocument doc = new BookDocument();

        doc.setId(book.getId().toString());
        doc.setTitle(book.getTitle());
        doc.setDescription(book.getDescription());
        doc.setCategory(book.getCategory());
        doc.setAuthor(book.getAuthor());
        doc.setPrice(book.getPrice().doubleValue());
        doc.setRating(book.getRating());
        doc.setStatus(book.getStatus().name());
        doc.setStockQuantity(book.getStockQuantity());
        doc.setPublicationDate(book.getPublicationDate());

        return doc;
    }

    public List<BookDocument> searchByCategory(String category) {
        return bookSearchRepository.findByCategory(category);
    }
}
package com.master.ms_books_catalogue.config;

import com.master.ms_books_catalogue.entity.Book;
import com.master.ms_books_catalogue.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(BookRepository bookRepository) {
        return args -> {
            // Solo insertar si NO hay libros existentes
            if (bookRepository.count() == 0) {
                System.out.println("🌱 Insertando datos iniciales...");

                // Libro 1: Drácula
                Book dracula = new Book();
                dracula.setTitle("Drácula");
                dracula.setDescription("El clásico de vampiros más influyente de la literatura.");
                dracula.setPrice(new BigDecimal("49.99"));
                dracula.setCategory("Terror");
                dracula.setIsbn("REF04-2323-45");
                dracula.setStockQuantity(8);
                dracula.setCoverImageUrl("https://covers.openlibrary.org/b/id/7984916-L.jpg");
                dracula.setStatus(Book.BookStatus.AVAILABLE);
                dracula.setAuthor("Bram Stoker");
                dracula.setPublicationDate(LocalDate.of(1897, 5, 26));
                dracula.setRating(4);

                // Libro 2: Dune
                Book dune = new Book();
                dune.setTitle("Dune");
                dune.setDescription("La épica historia de Paul Atreides en el desierto de Arrakis.");
                dune.setPrice(new BigDecimal("59.99"));
                dune.setCategory("Ciencia Ficción");
                dune.setIsbn("REF01-1111-11");
                dune.setStockQuantity(12);
                dune.setCoverImageUrl("https://covers.openlibrary.org/b/id/8114145-L.jpg");
                dune.setStatus(Book.BookStatus.AVAILABLE);
                dune.setAuthor("Frank Herbert");
                dune.setPublicationDate(LocalDate.of(1965, 8, 1));
                dune.setRating(5);

                // Libro 3: It (sin stock - OUT_OF_STOCK)
                Book it = new Book();
                it.setTitle("It");
                it.setDescription("Un grupo de niños enfrenta a un payaso demoníaco en Derry.");
                it.setPrice(new BigDecimal("45.50"));
                it.setCategory("Terror");
                it.setIsbn("REF03-3333-33");
                it.setStockQuantity(0);
                it.setCoverImageUrl("https://covers.openlibrary.org/b/id/8259441-L.jpg");
                it.setStatus(Book.BookStatus.OUT_OF_STOCK);
                it.setAuthor("Stephen King");
                it.setPublicationDate(LocalDate.of(1986, 9, 15));
                it.setRating(4);

                // Libro 4: El Código Da Vinci
                Book codigo = new Book();
                codigo.setTitle("El Código Da Vinci");
                codigo.setDescription("Un thriller de misterio que mezcla arte, historia y religión.");
                codigo.setPrice(new BigDecimal("39.90"));
                codigo.setCategory("Misterio");
                codigo.setIsbn("REF05-5555-55");
                codigo.setStockQuantity(15);
                codigo.setCoverImageUrl("https://covers.openlibrary.org/b/id/8314156-L.jpg");
                codigo.setStatus(Book.BookStatus.AVAILABLE);
                codigo.setAuthor("Dan Brown");
                codigo.setPublicationDate(LocalDate.of(2003, 3, 18));
                codigo.setRating(3);

                // Libro 5: Cien años de soledad
                Book cienAnos = new Book();
                cienAnos.setTitle("Cien años de soledad");
                cienAnos.setDescription("La historia de la familia Buendía en Macondo.");
                cienAnos.setPrice(new BigDecimal("55.00"));
                cienAnos.setCategory("Realismo Mágico");
                cienAnos.setIsbn("REF07-7777-77");
                cienAnos.setStockQuantity(20);
                cienAnos.setCoverImageUrl("https://covers.openlibrary.org/b/id/8259442-L.jpg");
                cienAnos.setStatus(Book.BookStatus.AVAILABLE);
                cienAnos.setAuthor("Gabriel García Márquez");
                cienAnos.setPublicationDate(LocalDate.of(1967, 5, 30));
                cienAnos.setRating(5);

                // Guardar todos
                bookRepository.save(dracula);
                bookRepository.save(dune);
                bookRepository.save(it);
                bookRepository.save(codigo);
                bookRepository.save(cienAnos);

                System.out.println("✅ Datos iniciales insertados: 5 libros");
            } else {
                System.out.println("📚 La base de datos ya contiene datos, seeder omitido.");
            }
        };
    }
}
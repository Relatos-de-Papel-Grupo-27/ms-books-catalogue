package com.master.ms_books_catalogue.config;

import com.master.ms_books_catalogue.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    @Autowired
    private BookService bookService;

    @EventListener(ApplicationReadyEvent.class)
    public void loadData() {

        try {
            System.out.println("Iniciando reindexación automática...");

            bookService.reindexAllBooks();

            System.out.println("Reindexación completada correctamente.");

        } catch (Exception e) {
            System.err.println("Error durante la reindexación automática:");
            e.printStackTrace();
        }
    }
}
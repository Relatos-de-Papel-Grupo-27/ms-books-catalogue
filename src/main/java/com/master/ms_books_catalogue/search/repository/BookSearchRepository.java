package com.master.ms_books_catalogue.search.repository;

import com.master.ms_books_catalogue.search.document.BookDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BookSearchRepository
        extends ElasticsearchRepository<BookDocument, String> {

    List<BookDocument> findByTitleContaining(String title);

    List<BookDocument> findByAuthorContaining(String author);

    List<BookDocument> findByCategory(String category);

}
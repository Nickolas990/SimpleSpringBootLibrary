package org.melnikov.simplespringboot.repositories;

import org.melnikov.simplespringboot.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BooksRepository extends JpaRepository<Book, Integer> {

    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}

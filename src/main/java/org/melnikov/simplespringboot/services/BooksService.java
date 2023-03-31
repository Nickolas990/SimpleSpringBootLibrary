package org.melnikov.simplespringboot.services;


import org.melnikov.simplespringboot.models.Book;
import org.melnikov.simplespringboot.models.Person;
import org.melnikov.simplespringboot.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BooksService {
    private BooksRepository booksRepository;


    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Book> findAll(Pageable pageable) {
       return booksRepository.findAll(pageable);
    }

    public Page<Book> findByTitleContainingIgnoringCase(String keyword, Pageable pageable) {
        return booksRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Book> findById(Integer id) {
        return booksRepository.findById(id);
    }

    @Transactional
    public void update(Integer id,  Book updatedBook) {
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void deleteById(Integer id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void delete(Book book) {
        booksRepository.delete(book);
    }

    @Transactional(readOnly = true)
    public Optional<Person> getOwner(Integer id) {
        Optional<Book> searchedBook = booksRepository.findById(id);
        Person owner = null;
        if (searchedBook.isPresent()) {
            Book book = searchedBook.get();
            owner = book.getClient();
        }
        return Optional.ofNullable(owner);
    }

    @Transactional
    public void assign(int id, Person newOwner) {
        Optional<Book> operatedBook = booksRepository.findById(id);
        if (operatedBook.isPresent()){
            Book book = operatedBook.get();
            newOwner.getBooks().add(book);
            book.setClient(newOwner);
        }
    }

    @Transactional
    public void release(int id) {
        Optional <Book> operatedBook = booksRepository.findById(id);
        operatedBook.ifPresent(book -> {
            Person person = book.getClient();
            person.getBooks().remove(book);
            book.setClient(null);
        });
    }
}

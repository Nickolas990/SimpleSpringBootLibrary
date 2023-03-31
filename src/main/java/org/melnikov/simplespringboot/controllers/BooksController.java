package org.melnikov.simplespringboot.controllers;

import jakarta.validation.Valid;
import org.melnikov.simplespringboot.models.Book;
import org.melnikov.simplespringboot.models.Person;
import org.melnikov.simplespringboot.services.BooksService;
import org.melnikov.simplespringboot.services.PeopleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * @author Nikolay Melnikov
 */
@Controller
@RequestMapping("/books")
public class BooksController {


    private final PeopleService peopleService;
    private final BooksService booksService;

    public BooksController(PeopleService peopleService, BooksService booksService) {
        this.peopleService = peopleService;
        this.booksService = booksService;
    }

//    @GetMapping()
//    public String index(Model model) {
//        model.addAttribute("books", booksService.findAll());
//        return "books/index";
//    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "/books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }

        booksService.save(book);
        return "redirect:/books";
    }

//    @GetMapping("/{id}")
//    public String show(@PathVariable("id") int id, Model model) {
//        Optional<Book> bookToShow = booksService.findById(id);
//        bookToShow.ifPresent(book -> model.addAttribute("book", book));
//        Optional <Person> bookOwner = booksService.getOwner(id);
//
//        if (bookOwner.isPresent()) {
//            model.addAttribute("bookOwner", bookOwner.get());
//        } else {
//            model.addAttribute("people", peopleService.findAll());
//        }
//        model.addAttribute("person", new Person());
//
//        return "books/show";
//    }

    @GetMapping()
    public String show(Model model,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "id,asc") Optional<String>[] sort) {
        try {
            List<Book> books;
            String sortField = sort[0].orElse("id");
            String sortDirection = sort[1].orElse("asc");

            Direction direction = sortDirection.equals("asc") ? Direction.ASC : Direction.DESC;
            Order order = new Order(direction, sortField);

            Pageable pageable = PageRequest.of(page-1, size, Sort.by(order));
            Page<Book> pageBooks;

            if (keyword == null) {
                pageBooks = booksService.findAll(pageable);
            } else {
                pageBooks = booksService.findByTitleContainingIgnoringCase(keyword, pageable);
                model.addAttribute("keyword", keyword);
            }

            books = pageBooks.getContent();

            model.addAttribute("books", books);
            model.addAttribute("currentPage", pageBooks.getNumber() + 1);
            model.addAttribute("totalPages", pageBooks.getTotalPages());
            model.addAttribute("totalItems", pageBooks.getTotalElements());
            model.addAttribute("pageSize", size);
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDirection", sortDirection);
            model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
            model.addAttribute("pageSizes", List.of(3, 9, 12));
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "books/listOfBooks";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        Optional<Book> bookToEdit = booksService.findById(id);
        bookToEdit.ifPresent(book -> model.addAttribute("book", book));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        booksService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            booksService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Book id " + id + "deleted");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        booksService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        Optional <Person> newOwner = peopleService.findById(selectedPerson.getId());
        newOwner.ifPresent(person -> booksService.assign(id, person));
        return "redirect:/books/" + id;
    }

}

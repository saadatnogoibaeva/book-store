package com.LibraryManagement.controller;


import com.LibraryManagement.entity.Book;
import com.LibraryManagement.entity.Borrower;
import com.LibraryManagement.repository.BookRepository;
import com.LibraryManagement.repository.BorrowerRepository;
import com.LibraryManagement.service.BookService;
import com.LibraryManagement.service.BorrowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class BorrowerController {

    private final
    BorrowerRepository borrowerRepository;
    private final
    BookRepository bookRepository;

    @Autowired
    BookService bookService;

    @Autowired
    BorrowerService service;

    public BorrowerController(BorrowerRepository borrowerRepository, BookRepository bookRepository) {
        this.borrowerRepository = borrowerRepository;
        this.bookRepository = bookRepository;
    }


    @RequestMapping(path = {"/search"})
    public String home(Book shop, Model model, String keyword) {
        System.out.println(keyword + "\n\n\n");
        if(keyword!=null) {
            List<Book> list = bookService.getByKeyword(keyword);
            model.addAttribute("book", list);
        }else {
            List<Book> list = bookService.getAllBooks();
            model.addAttribute("book", list);}
        return "bookList";
    }


    @RequestMapping(path = {"/borrowers/search"})
    public String hosme(Book shop, Model model, String keyword) {
        if(keyword!=null) {
            List<Borrower> list = service.getByKeyword(keyword);
            model.addAttribute("users", list);
        }else {
            List<Borrower> list = service.getAllBooks();
            model.addAttribute("users", list);}
        return "borrowersPages/borrowersPage";
    }

    @GetMapping("/borrowers")
    private String BorrowersPage(Model model){
        model.addAttribute("users", borrowerRepository.findAll());
        return "borrowersPages/borrowersPage";
    }

    @GetMapping("/borrowers/book_register")
    public String bookRegister(Model model){
        model.addAttribute("allBooks", bookRepository.findAll());
        return "/borrowersPages/borrowersBookRegister";
    }


    @PostMapping("/borrowers/save")
    public String addBook(@ModelAttribute Borrower b){
        service.save(b);
        return "redirect:/borrowers";
    }



    @RequestMapping("/borrowers/editBook/{id}")
    public String editeBook(@PathVariable Integer id, Model model){
        Borrower b=service.getBookById(id);
        model.addAttribute("book",b);
        model.addAttribute("allBooks",bookRepository.findAll());
        return "borrowersPages/borrowersBookEdit";
    }
    @RequestMapping("/borrowers/deleteBook/{id}")
    public String deleteeBook(@PathVariable Integer id){
        service.deleteById(id);
        return "redirect:/borrowers";
    }
}

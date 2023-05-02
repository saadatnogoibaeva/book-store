package com.LibraryManagement.controller;
import com.LibraryManagement.entity.Book;
import com.LibraryManagement.entity.MyBookList;
import com.LibraryManagement.repository.MyBookRepository;
import com.LibraryManagement.service.BookService;
import com.LibraryManagement.service.MyBookListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;


@Controller
public class BookController {
    @Autowired
    public BookService service;

    @Autowired
    private MyBookListService myBookListService;




    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/book_register")
        public String bookRegister(){
        return "bookRegister";
    }

    @GetMapping("/available_books")
    public ModelAndView getAllBook() {
        List<Book> list = service.getAllBook();

        return new ModelAndView("bookList","book",list);
    }
    @PostMapping("/save")
    public String addBook(@ModelAttribute Book b){
        service.save(b);
        return "redirect:/available_books";
    }
    @GetMapping("/my_books")
    public String getMyBooks(Model model){
        List<MyBookList>list=myBookListService.getAllMyBooks();
        model.addAttribute("book",list);
        return "MyBooks";
    }
    @RequestMapping("/mylist/{id}")
    public String getMyList(@PathVariable("id")int id){
        Book b= service.getBookById(id);
        MyBookList mb=new MyBookList(b.getId(), b.getName(), b.getAuthor(),b.getPrice());
        myBookListService.saveMyBook(mb);
        service.deleteById(b.getId());
        return "redirect:/my_books";
    }

    @RequestMapping("/editBook/{id}")
    public String editBook(@PathVariable("id")int id, Model model){
        Book b=service.getBookById(id);
        model.addAttribute("book",b);
        return"bookEdit";
    }
    @RequestMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable("id")int id){
        service.deleteById(id);
        return "redirect:/available_books";
    }
}

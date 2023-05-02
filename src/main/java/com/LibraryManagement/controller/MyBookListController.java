package com.LibraryManagement.controller;


import com.LibraryManagement.entity.Book;
import com.LibraryManagement.service.BookService;
import com.LibraryManagement.service.MyBookListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class MyBookListController {

    @Autowired
    private MyBookListService service;

    @Autowired
    private BookService bookService;


    @RequestMapping("/deleteMyList/{id}")
    public String deleteMyList(@PathVariable("id")int id){
        bookService.save(new Book(id, service.getById(id).getName(), service.getById(id).getAuthor(),
                service.getById(id).getPrice()) );
        service.deleteById(id);
        return"redirect:/my_books";
    }
}

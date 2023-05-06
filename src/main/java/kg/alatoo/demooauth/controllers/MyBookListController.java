package kg.alatoo.demooauth.controllers;



import kg.alatoo.demooauth.entity.Book;
import kg.alatoo.demooauth.service.BookService;
import kg.alatoo.demooauth.service.MyBookListService;
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

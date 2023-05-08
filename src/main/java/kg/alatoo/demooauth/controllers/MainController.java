package kg.alatoo.demooauth.controllers;

import kg.alatoo.demooauth.Sender.EmailSenderService;
import kg.alatoo.demooauth.dto.ProductDTO;
import kg.alatoo.demooauth.entity.*;
import kg.alatoo.demooauth.repository.RoleRepository;
import kg.alatoo.demooauth.repository.UserRepository;
import kg.alatoo.demooauth.service.BookService;
import kg.alatoo.demooauth.service.MyBookListService;
import kg.alatoo.demooauth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
@Slf4j
public class MainController {

    public  static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";

    @Autowired
    private EmailSenderService senderService;
    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/") public String homePage(){

        if (userService.CreateFirstAdmin()){

        }
        else {
            System.out.println("Not working");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getAuthorities()+"   ---   "+auth.getName());

        if (auth.getAuthorities().toString().contains("ADMIN")) {
            return "home";
        }
        if (auth.getAuthorities().toString().contains("USER")) {
            return "homeForUsers";
        }

        return "homeL";

    }
    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/login")
    public String loginPage(){

        return "login";
    }
    @GetMapping("/register") public String registerPage(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.isAuthenticated()) {
            return "register";
        }

        return "registerL";
    }
    @PostMapping("/register")
    public String postRegister(@ModelAttribute User user){
        userService.createUser(user);
        return "login";
    }

    @Autowired
    public BookService service;

    @Autowired
    PasswordEncoder encoder;


    @Autowired
    private MyBookListService myBookListService;



    @GetMapping("/book_register")
    public String bookRegister(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("productDTO", new ProductDTO());

        if (auth.getAuthorities().toString().contains("ADMIN")) {
            return "bookRegister";
        }
        return "homeForUsers";
    }
    @PostMapping("/book_register")
    public String productAddPost(@ModelAttribute("productDTO") ProductDTO productDTO,
                                 @RequestParam("productImage") MultipartFile file,
                                 @RequestParam("imgName") String imgName) throws IOException {

        Book product = new Book();
        product.setAuthor(productDTO.getAuthor());
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());


        String imageUUID;

        if (!file.isEmpty()) {
            imageUUID = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
            Files.write(fileNameAndPath, file.getBytes());

        } else {
            imageUUID = imgName;
        }

        product.setImageName(imageUUID);
        service.save(product);

        return "redirect:/available_books";
    }
    List<String> listSort = new ArrayList<>();




    @RequestMapping(path = {"/available_books"})
    public String getAllBook(Model model, String keyword) {
        listSort.clear();
        listSort.add("By Author Asc");listSort.add("By Author Desc");listSort.add("By Name Asc");listSort.add("By Name Desc");
        listSort.add("By Prize Asc");listSort.add("By Prize Desk");listSort.add("By Id Desc");
        model.addAttribute("listS", listSort);

        if(keyword!=null) {
            switch (keyword){
                case "By Author Asc":
                    model.addAttribute("list", service.bRepo.findByOrderByAuthorAsc());
                    break;
                case "By Author Desc":
                    model.addAttribute("list", service.bRepo.findByOrderByAuthorDesc());
                    break;
                case "By Name Asc":
                    model.addAttribute("list", service.bRepo.findByOrderByNameAsc());
                    break;
                case "By Name Desc":
                    model.addAttribute("list", service.bRepo.findByOrderByNameDesc());
                    break;
                case "By Prize Asc":
                    model.addAttribute("list", service.bRepo.findByOrderByPriceAsc());
                    break;
                case "By Prize Desc":
                    model.addAttribute("list", service.bRepo.findByOrderByPriceDesc());
                    break;
            }


        }else {

            model.addAttribute("list", service.getAllBooks());
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().toString().contains("ADMIN")) {
            return "bookList";
        }

        return "bookListForUsers";
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().toString().contains("ADMIN")) {
            return "MyBooks";
        }

        return "MyBooksForUsers";
    }
    @GetMapping("/order/{id}")
    public String orderBookById(@PathVariable("id")int id, Model model){
        model.addAttribute("product", myBookListService.getById(id));
        return "sender";
    }
    @PostMapping("/order/{id}")
    public String postOrderById (@ModelAttribute Adress adress, Model model){

        senderService.sendEmail(adress.getEmail(), adress.getAdress(), adress.getPhone(), adress.getBuying_book_id());


        return "check";
    }
    @RequestMapping("/mylist/{id}")
    public String getMyList(@PathVariable("id")int id){
        Book b= service.getBookById(id);
        MyBookList mb=new MyBookList(b.getId(), b.getName(), b.getAuthor(),b.getPrice(), b.getImageName());
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
        Book b = service.getBookById(id);

        service.deleteById(id);
        return "redirect:/available_books";
    }

}

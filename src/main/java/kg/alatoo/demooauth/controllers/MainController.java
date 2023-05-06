package kg.alatoo.demooauth.controllers;

import kg.alatoo.demooauth.entity.Book;
import kg.alatoo.demooauth.entity.MyBookList;
import kg.alatoo.demooauth.entity.Role;
import kg.alatoo.demooauth.entity.User;
import kg.alatoo.demooauth.repository.RoleRepository;
import kg.alatoo.demooauth.repository.UserRepository;
import kg.alatoo.demooauth.service.BookService;
import kg.alatoo.demooauth.service.MyBookListService;
import kg.alatoo.demooauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

  /*  @GetMapping(value = "/",headers = {})
    public String index() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println(principal.getClass().getName());
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) principal;
        System.out.println(oAuth2User.toString());
        return "home";
    }*/
    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;
    @GetMapping("/") public String homePage(){
        if(!userRepository.findByUsername("admin01").isPresent()){
            User user = new User();
            user.setUsername("admin01");
            user.setFirstname("user1");
            user.setLastname("user1");
            user.setEmail("user1@gmail.com");
            user.setPassword("123");
       /* Role role = new Role("ADMIN");
        user.setRoles((Set<Role>) role);*/
            userService.createAdmin(user);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return "home";
        }
        System.out.println(auth.getAuthorities());
        System.out.println(auth.getAuthorities().stream().map(s -> s.getAuthority()));
        System.out.println(auth.getAuthorities().stream().findAny());

        System.out.println(auth.getName());
        System.out.println(auth.toString());
        System.out.println(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")));

        return "homeForUsers";

    }
    @Autowired
    RoleRepository roleRepository;

    protected Role getOrCreateRole(String roleName) {
        Optional<Role> optionalRole = roleRepository.findById(roleName);
        if (optionalRole.isPresent()) {
            return optionalRole.get();
        }
        Role role = new Role(roleName);
        return roleRepository.save(role);
    }

    @GetMapping("/login") public String loginPage(){return "login";}
    @GetMapping("/register") public String registerPage(){return "register";}
    @PostMapping("/register")
    public String postRegister(@ModelAttribute User user){
        userService.createUser(user);
        return "login";
    }
    @PostMapping("/login")
    public String postLogin(@ModelAttribute User user){
        System.out.println("AJLDAJKL");
        return "homeForUsers";
    }

    @Autowired
    public BookService service;

    @Autowired
    private MyBookListService myBookListService;



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

package com.LibraryManagement.service;


import com.LibraryManagement.entity.MyBookList;
import com.LibraryManagement.repository.MyBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyBookListService {
    @Autowired
    private MyBookRepository mybook;

    public void saveMyBook(MyBookList book){
        mybook.save(book);
    }
    public List<MyBookList> getAllMyBooks(){
        return mybook.findAll();
    }
    public void deleteById(int id) {
        mybook.deleteById(id);
    }
    public MyBookList getById(int id){
        return mybook.getById(id);
    }
}

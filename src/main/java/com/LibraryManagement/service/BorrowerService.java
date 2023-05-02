package com.LibraryManagement.service;


import com.LibraryManagement.entity.Book;
import com.LibraryManagement.entity.Borrower;
import com.LibraryManagement.repository.BorrowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowerService {

    @Autowired
    public BorrowerRepository borrowerRepository;
    public void save(Borrower b){
        borrowerRepository.save(b);
    }
    public List<Borrower> getAllBook(){
        return borrowerRepository.findAll();
    }


    public Borrower getBookById(Integer id){
        return borrowerRepository.findById(id).get();
    }
    public void deleteById(Integer id){
        borrowerRepository.deleteById(id);
    }

    public List<Borrower> getByKeyword(String keyword){
        return borrowerRepository.findByKeyword(keyword);
    }
    public List<Borrower> getAllBooks(){
        List<Borrower> list = (List<Borrower>) borrowerRepository.findAll();
        return list;
    }
}

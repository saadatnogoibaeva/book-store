package com.LibraryManagement.repository;

import com.LibraryManagement.entity.MyBookList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyBookRepository extends JpaRepository<MyBookList, Integer> {

    MyBookList getById(int id);
}

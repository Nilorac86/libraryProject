package com.carolin.libraryproject.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface BookRepository extends JpaRepository<Book, Long> {



     Book searchBookByTitleIgnoreCase(String title);


}

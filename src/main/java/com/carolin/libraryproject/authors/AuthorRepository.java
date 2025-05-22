package com.carolin.libraryproject.authors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

     List<Author> findAuthorsByLastname(String lastname);
}

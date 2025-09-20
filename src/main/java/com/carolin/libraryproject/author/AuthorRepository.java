package com.carolin.libraryproject.author;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

     List<Author> findAuthorsByLastnameIgnoreCase(String lastname);

     Optional<Author> findAuthorByFirstnameAndLastnameIgnoreCase(String firstname, String lastname);
}

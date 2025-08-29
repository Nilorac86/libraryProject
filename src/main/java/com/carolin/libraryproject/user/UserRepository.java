package com.carolin.libraryproject.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // JPQL query. En query som inte bryr sig om vilken dialekt databasen har. Men genererar en sql query.
    @Query("SELECT u FROM User u WHERE u.email = :email") //??? ta bort vid implementering av security ???
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}

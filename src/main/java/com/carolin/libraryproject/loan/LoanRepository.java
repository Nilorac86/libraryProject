package com.carolin.libraryproject.loan;

import com.carolin.libraryproject.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {


    List<Loan> findByUserId(Long userId);

    boolean existsByUserAndReturnedDateIsNull(User user);
}

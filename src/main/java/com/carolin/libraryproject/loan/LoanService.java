package com.carolin.libraryproject.loan;
import com.carolin.libraryproject.book.Book;
import com.carolin.libraryproject.book.BookRepository;
import com.carolin.libraryproject.exceptionHandler.LoanExpiredException;
import com.carolin.libraryproject.exceptionHandler.NoAvailableCopiesException;
import com.carolin.libraryproject.exceptionHandler.NoLoanFoundException;
import com.carolin.libraryproject.loan.loanDto.LoanDto;
import com.carolin.libraryproject.user.User;
import com.carolin.libraryproject.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;



@Service
public class LoanService {

    private final LoanMapper loanMapper;
    private LoanRepository loanRepository;

    private BookRepository bookRepository;

    private UserRepository userRepository;



    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository,  LoanMapper loanMapper) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.loanMapper = loanMapper;
    }


    // Skapar ett lån, genom användares id och bokens id. Uppdaterar antal tillgängliga kopior av boken.
    @Transactional
    public Loan createLoan(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));


        // Kontroll om boken finns tillgänglig innan lån utförs.
        if (book.getAvailableCopies() <= 0 ){
            throw new NoAvailableCopiesException("No copies available of the book: " + book.getTitle());
        }

        Loan loan = new Loan();
        loan.setUser(user);

        loan.setBook(book);


        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        return loanRepository.save(loan);

    }

    // Hämtar en användares alla lån med användar id
    public List<LoanDto> findUserLoans (Long userId) {
        List<Loan> loans = loanRepository.findByUserId(userId);

        if (loans.isEmpty()) {
            throw new NoLoanFoundException("No loan found for user: " + userId);
        }

        return loanMapper.toDtoList(loans);
    }



    // Återlämning av bok via lånets id. Lägger till dagens datum för retur av lånet
    // samt uppdaterar antalet tillgängliga kopior.

    public void returnBook(Long loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getReturnedDate() != null) {
            throw new IllegalStateException ("Book already returned");
        }

        loan.setReturnedDate(LocalDateTime.now());

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() +1);

        bookRepository.save(book);
        loanRepository.save(loan);

    }



    // Förlänga returdatum på en lånad bok
    public void extendBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found with id " + loanId));

        if (loan.getDueDate().isBefore(LocalDateTime.now())) {
            throw new LoanExpiredException("Loan with id: " + loanId + " duedate has passed. Loan cannot be extended");
        }

        if (loan.getReturnedDate() != null) {
            throw new IllegalStateException("Book already returned and cannot be extended");
        }

        LocalDateTime newReturnDate = loan.getDueDate().plusDays(14);
        loan.setDueDate(newReturnDate);
        loanRepository.save(loan);
    }

}

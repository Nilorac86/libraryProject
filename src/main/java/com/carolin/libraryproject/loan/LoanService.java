package com.carolin.libraryproject.loan;
import com.carolin.libraryproject.book.Book;
import com.carolin.libraryproject.book.BookRepository;
import com.carolin.libraryproject.exceptionHandler.LoanExpiredException;
import com.carolin.libraryproject.exceptionHandler.NoAvailableCopiesException;
import com.carolin.libraryproject.exceptionHandler.NoLoanFoundException;
import com.carolin.libraryproject.loan.loanDto.LoanDto;
import com.carolin.libraryproject.security.CustomUserDetails;
import com.carolin.libraryproject.user.User;
import com.carolin.libraryproject.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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


    // Skapar den inloggade användarens lån tillsammans med bokens id. Uppdaterar antal tillgängliga kopior av boken.
    @Transactional
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Loan createLoan(Authentication authentication, Long bookId, Long targetUserId) {
        CustomUserDetails loggedUser = (CustomUserDetails) authentication.getPrincipal();

        Loan loan = new Loan();

        if (targetUserId != null && loggedUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))){
            User targetUser = userRepository.findById(targetUserId)
                     .orElseThrow(() -> new EntityNotFoundException("Target user not found"));
           loan.setUser(targetUser);
        } else {
            loan.setUser(loggedUser.getUser());
        }
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));


        // Kontroll om boken finns tillgänglig innan lån utförs.
        if (book.getAvailableCopies() <= 0 ){
            throw new NoAvailableCopiesException("No copies available");
        }

        loan.setBook(book);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }


    // Hämtar den inloggade användarens alla lån.
    @PreAuthorize("hasAnyRole ('USER', 'ADMIN')")
    public List<LoanDto> findUserLoans (Authentication authentication, Long userId) {

        CustomUserDetails loggedInUser = (CustomUserDetails) authentication.getPrincipal();


        boolean isAdmin = loggedInUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if (userId == null) {
            userId = loggedInUser.getUser().getId();
        }

        if (!userId.equals(loggedInUser.getUser().getId()) && !isAdmin) {
            throw new SecurityException("You do not have permission to view these loans");
        }


        List<Loan> loans = loanRepository.findByUserId(userId);

        if (loans.isEmpty()) {
            throw new NoLoanFoundException("No loans found");
        }

        return loanMapper.toDtoList(loans);
    }



    // Återlämning av bok den inloggade användaren. Lägger till dagens datum för retur av lånet
    // samt uppdaterar antalet tillgängliga kopior.

    @PreAuthorize("hasAnyRole ('ADMIN','USER')")
    public void returnBook(Long loanId, Authentication authentication) {
        CustomUserDetails loggedInUser = (CustomUserDetails) authentication.getPrincipal();

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        boolean isAdmin = loggedInUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if (!loan.getUser().getId().equals(loggedInUser.getUser().getId()) && !isAdmin) {
            throw new SecurityException("You do not have permission to return this loan");
        }

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
    @PreAuthorize("hasAnyRole ('ADMIN','USER')")
    public void extendBook(Long loanId, Authentication authentication) {
        CustomUserDetails loggedInUser = (CustomUserDetails) authentication.getPrincipal();

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found"));

        boolean isAdmin = loggedInUser.getAuthorities().stream()
                .anyMatch(authority -> authority
                        .getAuthority().equals("ROLE_ADMIN"));

        if (!loan.getUser().getId().equals(loggedInUser.getUser().getId()) && !isAdmin) {
            throw new SecurityException("You do not have permission to return this loan");
        }

        if (loan.getDueDate().isBefore(LocalDateTime.now())) {
            throw new LoanExpiredException("Loan duedate has passed. Loan cannot be extended");
        }

        if (loan.getReturnedDate() != null) {
            throw new IllegalStateException("Book already returned and cannot be extended");
        }

        LocalDateTime newReturnDate = loan.getDueDate().plusDays(14);
        loan.setDueDate(newReturnDate);
        loanRepository.save(loan);
    }



   // ########################### ADMIN ONLY ###################################

    @PreAuthorize("hasRole ('ADMIN')")
    public List<LoanDto> findAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        return loanMapper.toDtoList(loans);
    }



    @PreAuthorize("hasRole ('ADMIN')")
    public void deleteLoan(Long loanId) {
        if (!loanRepository.existsById(loanId)) {
            throw new EntityNotFoundException("Loan not found");
        }
        loanRepository.deleteById(loanId);
    }

}

package com.carolin.libraryproject.loan.loanDto;
import com.carolin.libraryproject.book.bookDto.BookDto;
import com.carolin.libraryproject.user.userDto.UserDto;

import java.time.LocalDateTime;

public class LoanDto {


    private UserDto user;
    private Long loanId;
    private BookDto book;
    private LocalDateTime borrowedDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnedDate;

    public LoanDto() {
    }

    public LoanDto(UserDto user, Long loanId, BookDto book, LocalDateTime borrowedDate, LocalDateTime dueDate, LocalDateTime returnedDate) {


        this.user = user;
        this.loanId = loanId;
        this.book = book;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        this.returnedDate = returnedDate;
    }



    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }


    public BookDto getBook() {
        return book;
    }

    public void setBook(BookDto book) {
        this.book = book;
    }

    public LocalDateTime getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(LocalDateTime borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDateTime returnedDate) {
        this.returnedDate = returnedDate;
    }
}

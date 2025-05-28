package com.carolin.libraryproject.loan;

import com.carolin.libraryproject.book.BookMapper;
import com.carolin.libraryproject.loan.loanDto.LoanDto;
import com.carolin.libraryproject.user.UserMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoanMapper {

    private BookMapper bookMapper;
    private UserMapper userMapper;

    public LoanMapper(BookMapper bookMapper, UserMapper userMapper) {
        this.bookMapper = bookMapper;
        this.userMapper = userMapper;
    }

    public LoanDto toDto(Loan loan) {
        if (loan == null) {
            return null;
        }

        return new LoanDto(
                userMapper.toUserDto(loan.getUser()),
                loan.getId(),
                bookMapper.toDto(loan.getBook()),
                loan.getBorrowedDate(),
                loan.getDueDate(),
                loan.getReturnedDate());
    }


    public List<LoanDto> toDtoList(List<Loan> loans) {
        if (loans == null) {
            return Collections.emptyList();
        }

        return loans.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

    }
}

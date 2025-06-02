package com.carolin.libraryproject.user;

import com.carolin.libraryproject.loan.Loan;
import com.carolin.libraryproject.loan.LoanService;
import com.carolin.libraryproject.loan.loanDto.LoanDto;
import com.carolin.libraryproject.user.userDto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final LoanService loanService;
    private final UserMapper userMapper;

    public UserController(UserService userService, LoanService loanService, UserMapper userMapper) {
        this.userService = userService;
        this.loanService = loanService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {

        List<UserDto> users = userService.findAll();

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> findUserByEmail(@PathVariable String email) {
        UserDto user = userService.findUserByEmail(email);


        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody User user) {

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        User savedUser = userService.addUser(user);
        UserDto userDto = userMapper.toUserDto(savedUser);
        URI location = URI.create("/users/" + savedUser.getId());

        return ResponseEntity.created(location).body(userDto);
    }

    @GetMapping("/{userId}/loans")
    public ResponseEntity<List<LoanDto>> getLoans(@PathVariable Long userId) {
        List<LoanDto> loan = loanService.findUserLoans(userId);

        if (loan.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(loan);
    }

}

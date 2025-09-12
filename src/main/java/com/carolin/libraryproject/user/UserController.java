package com.carolin.libraryproject.user;


import com.carolin.libraryproject.loan.LoanService;
import com.carolin.libraryproject.loan.loanDto.LoanDto;
import com.carolin.libraryproject.security.CustomUserDetails;
import com.carolin.libraryproject.user.userDto.UserDto;
import com.carolin.libraryproject.user.userDto.UserRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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


    @GetMapping("/me")
    public ResponseEntity<UserDto> getLoggedInUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser == null) {

            return ResponseEntity.status(401).build();
        }

        UserDto userDto = userService.findUserByEmail(currentUser.getUsername());

        return ResponseEntity.ok(userDto);

    }


    // Hämtar en lista av alla användare returnerar en userDto
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {

        List<UserDto> users = userService.findAll();

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }



    // Hämtar användare baserat på email som variabel i url sökningen
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> findUserByEmail(@PathVariable String email) {
        UserDto user = userService.findUserByEmail(email);


        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }


    // Lägger till användare genom input i body.
    @PostMapping("/register")
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserRequestDto userRequestDto) {


        User user = userMapper.toUserEntity(userRequestDto);
        User savedUser = userService.addUser(user);
        UserDto userDto = userMapper.toUserDto(savedUser);

        URI location = URI.create("/users/" + savedUser.getId());
        return ResponseEntity.created(location).body(userDto);
    }


    // Hämtar användares lån
    @GetMapping("/{userId}/loans")
    public ResponseEntity<List<LoanDto>> getLoans(@AuthenticationPrincipal CustomUserDetails loggedInUser) {
        List<LoanDto> loan = loanService.findUserLoans(loggedInUser.getUser());

        if (loan.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(loan);
    }


}

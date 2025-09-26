package com.carolin.libraryproject.user;


import com.carolin.libraryproject.loan.LoanService;
import com.carolin.libraryproject.loan.loanDto.LoanDto;
import com.carolin.libraryproject.security.CustomUserDetails;
import com.carolin.libraryproject.user.userDto.UserDto;
import com.carolin.libraryproject.user.userDto.UserRequestDto;
import com.carolin.libraryproject.utils.HtmlSanitizer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;

@Validated
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


    // Alla inloggade kan hämta sin info
    @PreAuthorize("hasAnyRole ('ADMIN', 'USER')")
    @GetMapping("/me")
    public ResponseEntity<UserDto> getLoggedInUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser == null) {

            return ResponseEntity.status(401).build();
        }
        UserDto userDto = userService.findUserByEmail(currentUser.getUsername());
        return ResponseEntity.ok(userDto);

    }

    // Endast admin
    // Hämtar en lista av alla användare returnerar en userDto
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {

        List<UserDto> users = userService.findAll();

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }


    // Endast admin
    // Hämtar användare baserat på email.
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/email")
    public ResponseEntity<UserDto> findUserByEmail(@RequestParam String email) {
        UserDto user = userService.findUserByEmail(email);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }


    // Public
    // Lägger till användare genom input i body.
    @PostMapping("/register")
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserRequestDto userRequestDto, HttpServletRequest request) {

        // Sparar användares ipAdress
        String clientIp = request.getRemoteAddr();

        // Skydd mot XSS attack
        userRequestDto.setFirstName(HtmlSanitizer.cleanAll(userRequestDto.getFirstName()));
        userRequestDto.setLastName(HtmlSanitizer.cleanAll(userRequestDto.getLastName()));
        userRequestDto.setEmail(HtmlSanitizer.cleanAll(userRequestDto.getEmail()));
        userRequestDto.setPassword(HtmlSanitizer.cleanAll(userRequestDto.getPassword()));

        User user = userMapper.toUserEntity(userRequestDto);
        User savedUser = userService.addUser(user, clientIp);
        UserDto userDto = userMapper.toUserDto(savedUser);

        URI location = URI.create("/users/" + savedUser.getId());
        return ResponseEntity.created(location).body(userDto);
    }


    // Objektorienterad rollkontroll användare kan se alla sina egna lån, admin kan se en användares lån via id.
    // Se alla sina lån
    @PreAuthorize("hasAnyRole ('ADMIN','USER')")
    @GetMapping("/loans")
    public ResponseEntity<List<LoanDto>> getLoans(Authentication authentication, Long userId) {

        List<LoanDto> loans = loanService.findUserLoans(authentication, userId);
        return ResponseEntity.ok(loans);

    }



    // Admin kan radera användare
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestParam String email) throws IllegalAccessException {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }

}

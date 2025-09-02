package com.carolin.libraryproject.user;

import com.carolin.libraryproject.loan.Loan;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "INTEGER")
    private Long id;

    @NotBlank
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;


    @Column(name = "role", nullable = false)
    private String role;

    // kontrollerar om kontot är aktivt
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Loan> loans = new ArrayList<>();

    //Lägger automatiskt till datum och tid vid registrering
    @PrePersist
    protected void onCreate() {
        this.registrationDate = LocalDateTime.now();
        if (role == null) {
            this.role = "ROLE_USER";
        }
    }





    // Tom konstruktor
    public User() {

    }

    public User( String email, String password, String role ) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = true;
    }

    // Getters och setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}




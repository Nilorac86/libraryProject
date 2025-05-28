package com.carolin.libraryproject.user;

import com.carolin.libraryproject.user.userDto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDto> findAll() {

        return userMapper.toDtoList(userRepository.findAll());
    }

    public UserDto findUserByEmail(String email) {

       User user = userRepository.findByEmail(email);

       return userMapper.toUserDto(user);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

}

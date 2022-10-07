package raf.projekat_back.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import raf.projekat_back.dto.*;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {

    List<UserDto> getAllUsers();
    UserDto getUser(Integer id);
    UserDto getLoggedUser(String email);

    UserDto createUser(CreateUserDto createUserDto);
    UserDto updateUser(Integer id, UpdateUserDto updateUserDto);
    UserDto deleteUser(Integer id);

}

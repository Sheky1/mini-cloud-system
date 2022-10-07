package raf.projekat_back.services;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import raf.projekat_back.dto.*;
import raf.projekat_back.exceptions.CustomException;
import raf.projekat_back.exceptions.ForbiddenException;
import raf.projekat_back.exceptions.NotAcceptableException;
import raf.projekat_back.exceptions.NotFoundException;
import raf.projekat_back.model.Permission;
import raf.projekat_back.model.User;
import raf.projekat_back.repositories.UserRepository;
import raf.projekat_back.utility.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDto> getAllUsers() {
        checkEverything("can_read_users", "read users");
//        String email = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
//        User loggedUser = userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(String.format("You are not logged in")));
//        int flag = 0;
//        for(Permission permission: loggedUser.getPermissions()) if(permission.getValue().equals("can_read_users")) flag = 1;
//        if(flag == 0) throw new ForbiddenException("You don't have the permission to read users.");
        List<UserDto> allUsersDto = new ArrayList<>();
        for(User user: userRepository.findAll()) {
            allUsersDto.add(userMapper.userToUserDto(user));
        }
        return allUsersDto;
    }

    @Override
    public UserDto getUser(Integer id) {
        String email = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User loggedUser = userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(String.format("You are not logged in")));
        int flag = 0;
        for(Permission permission: loggedUser.getPermissions()) if(permission.getValue().equals("can_read_users")) flag = 1;
        if(flag == 0) throw new ForbiddenException("You don't have the permission to read users.");
        User user = userRepository.findUserById(id).orElseThrow(() -> new NotFoundException(String.format("No user with id: %s found.", id)));
        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto createUser(CreateUserDto createUserDto) {
        String email = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User loggedUser = userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(String.format("You are not logged in")));
        int flag = 0;
        for(Permission permission: loggedUser.getPermissions()) if(permission.getValue().equals("can_create_users")) flag = 1;
        if(flag == 0) throw new ForbiddenException("You don't have the permission to create users.");
        User newUser = userMapper.createUserToUser(createUserDto);
        try {
            userRepository.save(newUser);
        }catch (Exception e) {
            throw new NotAcceptableException("Email already in usage.");
        }
        return userMapper.userToUserDto(newUser);
    }

    @Override
    public UserDto updateUser(Integer id, UpdateUserDto updateUserDto) {
        String email = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User loggedUser = userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(String.format("You are not logged in")));
        int flag = 0;
        for(Permission permission: loggedUser.getPermissions()) if(permission.getValue().equals("can_update_users")) flag = 1;
        if(flag == 0) throw new ForbiddenException("You don't have the permission to update users.");
        User user = userRepository.findUserById(id).orElseThrow(() -> new NotFoundException(String.format("No user with id: %s found.", id)));
        user = userMapper.updateUser(user, updateUserDto);
        try {
            userRepository.save(user);
        }catch (Exception e) {
            throw new NotAcceptableException("Email already in usage.");
        }
        return userMapper.userToUserDto(user);
    }

    public UserDto deleteUser(Integer id) {
        String email = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User loggedUser = userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(String.format("You are not logged in")));
        int flag = 0;
        for(Permission permission: loggedUser.getPermissions()) if(permission.getValue().equals("can_delete_users")) flag = 1;
        if(flag == 0) throw new ForbiddenException("You don't have the permission to delete users.");
        User user = userRepository.findUserById(id).orElseThrow(() -> new NotFoundException(String.format("No user with id: %s found.", id)));
        UserDto userDto = userMapper.userToUserDto(user);
        userRepository.delete(user);
        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(String.format("No user with email: %s found.", email)));
        if(user == null) {
            throw new UsernameNotFoundException("User with email "+email+" not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    @Override
    public UserDto getLoggedUser(String email) {
        User user = this.userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(String.format("No user with email: %s found.", email)));
        return userMapper.userToUserDto(user);
    }

    private void checkEverything(String action, String errorMessage) throws CustomException {
        String email = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User loggedUser = userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(String.format("You are not logged in")));
        int flag = 0;
        for(Permission permission: loggedUser.getPermissions()) if(permission.getValue().equals(action)) flag = 1;
        if(flag == 0) throw new ForbiddenException("You don't have the permission to " + errorMessage + ".");
    }
}

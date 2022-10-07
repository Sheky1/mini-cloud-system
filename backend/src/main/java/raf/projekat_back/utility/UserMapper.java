package raf.projekat_back.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import raf.projekat_back.dto.CreateUserDto;
import raf.projekat_back.dto.PermissionDto;
import raf.projekat_back.dto.UpdateUserDto;
import raf.projekat_back.dto.UserDto;
import raf.projekat_back.exceptions.NotFoundException;
import raf.projekat_back.model.Permission;
import raf.projekat_back.model.User;
import raf.projekat_back.repositories.PermissionRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    PermissionRepository permissionRepository;
    private PasswordEncoder passwordEncoder;

    public UserMapper(PermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUserToUser(CreateUserDto createUserDto) {
        User newUser = new User();
        newUser.setFirstName(createUserDto.getFirstName());
        newUser.setLastName(createUserDto.getLastName());
        newUser.setEmail(createUserDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        List<Permission> permissions = new ArrayList<Permission>();
        for(String permissionString: createUserDto.getPermissions()) {
            Permission permission = permissionRepository.findPermissionByValue(permissionString).orElseThrow(() -> new NotFoundException(String.format("%s is not a valid permission.", permissionString)));
            permissions.add(permission);
        }
        newUser.setPermissions(permissions);
        return newUser;
    }

    public UserDto userToUserDto(User user) {
        UserDto newUserDto = new UserDto();
        newUserDto.setId(user.getId());
        newUserDto.setFirstName(user.getFirstName());
        newUserDto.setLastName(user.getLastName());
        newUserDto.setEmail(user.getEmail());
        List<String> permissions = new ArrayList<>();
        for(Permission permission: user.getPermissions()) {
            permissions.add(permission.getValue());
        }
        newUserDto.setPermissions(permissions);
        return newUserDto;
    }

    public User updateUser(User user, UpdateUserDto updateUserDto) {
        if(!updateUserDto.getFirstName().equals("")) user.setFirstName(updateUserDto.getFirstName());
        if(!updateUserDto.getLastName().equals("")) user.setLastName(updateUserDto.getLastName());
        if(!updateUserDto.getEmail().equals("")) user.setEmail(updateUserDto.getEmail());
        if(!updateUserDto.getPassword().equals("")) user.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
        List<Permission> permissions = new ArrayList<Permission>();
        for(String permissionString: updateUserDto.getPermissions()) {
            Permission permission = permissionRepository.findPermissionByValue(permissionString).orElseThrow(() -> new NotFoundException(String.format("%s is not a valid permission.", permissionString)));
            permissions.add(permission);
        }
        user.setPermissions(permissions);
        return user;
    }

}

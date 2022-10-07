package raf.projekat_back.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.projekat_back.dto.*;
import raf.projekat_back.exceptions.CustomException;
import raf.projekat_back.exceptions.NotAcceptableException;
import raf.projekat_back.services.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Qualifier(value = "userServiceImpl")
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try{
            return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") Integer id) {
        try{
            return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserDto createUserDto) {
        try{
            return new ResponseEntity<>(userService.createUser(createUserDto), HttpStatus.CREATED);
        } catch (CustomException e) {
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateUser(@PathVariable("id") Integer id, @RequestBody @Valid UpdateUserDto updateUserDto) {
        try{
            return new ResponseEntity<>(userService.updateUser(id, updateUserDto), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) {
        try {
            return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

}

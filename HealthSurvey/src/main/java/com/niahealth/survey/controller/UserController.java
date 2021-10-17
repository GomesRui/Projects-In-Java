package com.niahealth.survey.controller;

import java.util.List;

import com.niahealth.survey.model.User;
import com.niahealth.survey.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    
    @Autowired
    private UserService service;

    //Get all available Users from database
    @GetMapping("/users/all")
    List<User> findAll()
    {
        return service.findAll();
    }

    //Get User by ID
    @GetMapping("/users/{id}")
    User findById(@PathVariable Long id)
    {
        return service.findById(id);
    }  

    //Create new User and add to database
    @PostMapping("/users")
    User addNewUser(@RequestBody User user)
    {
        return service.insertUser(user);           
    }  
    
    //Update the User
    @PutMapping("/users/{id}")
    User updateUser(@RequestBody User user, @PathVariable Long id)
    {
        // CustomException ex = new CustomException(CustomException.Code.HTTP_METHOD_NOT_AVAILABLE, id.toString());
        // ex.throwCustomException();
        throw new ApplicationMessage.HTTP_METHOD_NOT_AVAILABLE(id.toString());
    }

    //Clean users
    @DeleteMapping("/users/all")
    ResponseEntity<String> clean()
    {
        List<User> usersDB = service.findAll();        
       
        service.deleteAll();
        return new ResponseEntity<String>("All " + usersDB.size() + " were deleted!",HttpStatus.OK);
    }

    //Delete a Survey by ID
    @DeleteMapping("/users/{id}")
    ResponseEntity<String> deleteUser(@PathVariable Long id)
    {
        User userDB = service.findById(id);

        service.deleteById(id);

        return new ResponseEntity<String>("The user " + userDB.getFirstName() + " " + userDB.getLastName() + " was deleted!",HttpStatus.OK);
    }
    //Delete a Survey by name
    @DeleteMapping("/users")
    ResponseEntity<String> deleteUser(@RequestParam(value = "firstName") String firstName,@RequestParam(value = "lastName") String lastName)
    {
        User userDB = service.findUserByName(firstName, lastName);

        service.deleteById(userDB.getId());

        return new ResponseEntity<String>("The user " + userDB.getFirstName() + " " + userDB.getLastName() + " was deleted!",HttpStatus.OK);
    }
}



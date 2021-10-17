package com.niahealth.survey.service;

import java.util.List;

import com.niahealth.survey.controller.ApplicationMessage;
import com.niahealth.survey.model.User;
import com.niahealth.survey.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService
{

    @Autowired
    private UserRepository repository;

    public User save(User user)
    {
        return this.repository.save(user);
    }

    public List<User> findAll()
    {
        List<User> users = this.repository.findAll();

        if(users != null && users.size() > 0)
        {
            return users;
        } else {
            throw new ApplicationMessage.ENTITY_NOT_FOUND("[]");
        }
    }

    public User findById(Long id)
    {
        return this.repository.findById(id).orElseThrow(() -> new ApplicationMessage.ENTITY_NOT_FOUND(id.toString()));
    }

    public User findUserByName(String firstName, String lastName)
    {
        return repository.findAll().stream()
        .filter(u -> (u.getFirstName().equals(firstName) && u.getLastName().equals(lastName)))
        .findFirst()
        .orElseThrow(() -> new ApplicationMessage.ENTITY_NOT_FOUND(firstName + " " + lastName));
    }

    public void deleteById(Long id)
    {
        this.repository.deleteById(id);
    }
    public void deleteAll()
    {
        this.repository.deleteAll();
    }

    //Interface for insert(User) and logic to insert a user
    public User insertUser(User user)
    {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        User userDB = null;

        try 
        {
            userDB = findUserByName(firstName, lastName);
            
        } catch (ApplicationMessage.ENTITY_NOT_FOUND ex)
        {
            return repository.save(user);
        }

        throw new ApplicationMessage.ENTITY_ALREADY_EXISTS(userDB.getFirstName() + " " + userDB.getLastName());
    }
}

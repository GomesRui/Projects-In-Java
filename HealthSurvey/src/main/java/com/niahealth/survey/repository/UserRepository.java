package com.niahealth.survey.repository; 

import java.util.Optional;

import com.niahealth.survey.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findByFirstNameAndLastName(String firstName, String lastName);
}
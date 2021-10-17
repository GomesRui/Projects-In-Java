package com.niahealth.survey.repository;

import java.util.Optional;

import com.niahealth.survey.model.Survey;
import com.niahealth.survey.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long>
{
    Optional<Survey> findByUser(User user);
    // @Query(value = "SELECT * FROM SURVEYS s INNER JOIN USERS u ON s.USER_ID = u.ID INNER JOIN REL_SURVEY_QUESTION sq ON sq.SURVEY_ID = s.ID INNER JOIN DOMAIN_QUESTIONS q ON q.ID = sq.QUESTION_ID WHERE s.ID = :id", 
    // nativeQuery = true)
    // Optional<Survey> findById(@Param("id") Long id);
    @Query("SELECT s from Survey s INNER JOIN User u ON u.id = s.user WHERE u.firstName = :firstName AND u.lastName = :lastName")
    Optional<Survey> findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);
    Optional<Survey> findByName(String name);
}
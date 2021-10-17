package com.niahealth.survey.repository;

import java.util.Optional;

import com.niahealth.survey.model.Question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository  extends JpaRepository<Question, Long> 
{
    Optional<Question> findByQuestionText(String questionText);
}
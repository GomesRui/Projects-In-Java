package com.niahealth.survey.repository;

import java.util.Optional;

import com.niahealth.survey.model.Surveys_Questions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Surveys_QuestionsRepository  extends JpaRepository<Surveys_Questions, Long> 
{
    Optional<Surveys_Questions> findById(Long id);
    Optional<Surveys_Questions> deleteBySurvey_Id(Long surveyId);
    Optional<Surveys_Questions> deleteByQuestion_Id(Long questionId);
}
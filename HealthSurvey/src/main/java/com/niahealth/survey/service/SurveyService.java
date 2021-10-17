package com.niahealth.survey.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.niahealth.survey.controller.ApplicationMessage;
import com.niahealth.survey.model.Question;
import com.niahealth.survey.model.Survey;
import com.niahealth.survey.model.Surveys_Questions;
import com.niahealth.survey.model.User;
import com.niahealth.survey.repository.QuestionRepository;
import com.niahealth.survey.repository.SurveyRepository;
import com.niahealth.survey.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SurveyService
{

    @Autowired
    private SurveyRepository repository;
    @Autowired
    private QuestionRepository questionRepo;
    @Autowired
    private UserRepository userRepo;

    public Survey save(Survey survey)
    {
        return this.repository.save(survey);
    }

    public List<Survey> findAll()
    {
        List<Survey> surveys = this.repository.findAll();

        if(surveys != null && surveys.size() > 0)
        {
            return surveys;
        } else {
            throw new ApplicationMessage.ENTITY_NOT_FOUND("[]");
        }
    }

    public Survey findById(Long id)
    {
        return this.repository.findById(id).orElseThrow(() -> new ApplicationMessage.ENTITY_NOT_FOUND(id.toString()));
    }

    public Survey findSurveyByUser(User user)
    {
        return repository.findAll().stream()
        .filter(s -> (s.getUser().getFirstName().equals(user.getFirstName()) && s.getUser().getLastName().equals(user.getLastName())))
        .findFirst()
        .orElseThrow(() -> new ApplicationMessage.ENTITY_NOT_FOUND(user.getFirstName() + " " + user.getLastName()));
    }

    public Survey findSurveyByUser(String firstName, String lastName)
    {
        return repository.findAll().stream()
        .filter(s -> (s.getUser().getFirstName().equals(firstName) && s.getUser().getLastName().equals(lastName)))
        .findFirst()
        .orElseThrow(() -> new ApplicationMessage.ENTITY_NOT_FOUND(firstName + " " + lastName));
    }
    public Survey findSurveyByName(String name)
    {
        return repository.findByName(name).orElseThrow(() -> new ApplicationMessage.ENTITY_NOT_FOUND(name));
    }
    public void deleteById(Long id)
    {
        this.repository.deleteById(id);
    }
    public void deleteAll()
    {
        this.repository.deleteAll();
    }

    // public Survey updateSurvey(Survey survey, Long id)
    // {
    //     return repository.findById(id).map(surveyDB -> 
    //     {
    //         surveyDB.setModified(new Timestamp(new Date().getTime()));
    //         surveyDB.setQuestions(survey.getQuestions());
    //         return repository.save(surveyDB);
    //     }).orElseThrow(() -> new ApplicationMessage.ENTITY_NOT_FOUND(id.toString()));
    // }
    
    //Interface for update(String, Integer, Long)
    public Survey updateSurveyQuestion(String questionText, Integer answerRating, Long id)
    {
        Survey surveyDB = repository.findById(id).orElseThrow(() -> new ApplicationMessage.ENTITY_NOT_FOUND(id.toString()));
        return updateSurveyQuestion(questionText, answerRating, surveyDB);  
    }

    //Interface for update(Survey)
    public Survey updateSurveyQuestion(Survey newSurvey)
    {
        Survey surveyDB = repository.findByFirstNameAndLastName(newSurvey.getUser().getFirstName(),newSurvey.getUser().getLastName()).orElseThrow(() -> new ApplicationMessage.ENTITY_NOT_FOUND(newSurvey.getUser().getFirstName() + " " + newSurvey.getUser().getLastName()));
        if(newSurvey.getQuestions() != null && newSurvey.getQuestions().size() > 0)
        {
            for(Surveys_Questions surveyQuestion : newSurvey.getQuestions())
                updateSurveyQuestion(surveyQuestion.getQuestion().getQuestionText(), surveyQuestion.getAnswerRating(), surveyDB);
        }
        else
        {
            throw new ApplicationMessage.ENTITY_NOT_FOUND("questions");
        }
        
        return newSurvey;  
    }

    //Interface for update(String, Integer, String, String)
    public Survey updateSurveyQuestion(String questionText, Integer answerRating, String firstName, String lastName)
    {
        Survey surveyDB = repository.findByFirstNameAndLastName(firstName, lastName).orElseThrow(() -> new ApplicationMessage.ENTITY_NOT_FOUND(firstName + " " + lastName));
        return updateSurveyQuestion(questionText, answerRating, surveyDB);  
    }

    //private logic to update a survey
    protected Survey updateSurveyQuestion(String questionText, Integer answerRating, Survey surveyDB)
    {
        if(answerRating >= 0 && answerRating <= 10)
        {
            boolean isUpdated = false;
            for(Surveys_Questions surveyQuestions : surveyDB.getQuestions())
            {
                if(surveyQuestions.getQuestion().getQuestionText().equals(questionText))
                {
                    surveyQuestions.setAnswerRating(answerRating);
                    surveyDB.setModified(new Timestamp(new Date().getTime()));
                    isUpdated = true;
                    break;
                }
            }

            if(!isUpdated)
                throw new ApplicationMessage.ENTITY_NOT_FOUND("Question", questionText);
        }
        else
        {
            throw new ApplicationMessage.CHECK_VALUE_FAILED("Answer", answerRating);
        }

        return repository.save(surveyDB);
    }
    
    //Interface for insert(String, String) empty ratings for all questions
    // public Survey insertSurvey(String firstName, String lastName)
    // { 
    //     User user = userRepo.findByFirstNameAndLastName(firstName, lastName).orElseThrow(() -> new ApplicationMessage.ENTITY_NOT_FOUND("User", firstName + " " + lastName));
    //     Survey newSurvey = null;
        
    //     for(Question question : questionRepo.findAll())
    //         newSurvey = insertSurvey(question.getQuestionText(), null, user);

    //     return newSurvey;
    // }
    // public Survey insertSurvey(List<Surveys_Questions> surveysQuestions, String firstName, String lastName)
    // {
    //     User user = userRepo.findByFirstNameAndLastName(firstName, lastName).orElseThrow(() -> new ApplicationMessage.ENTITY_NOT_FOUND("User", firstName + " " + lastName));
    //     Survey newSurvey = null;
        
    //     for(Surveys_Questions question : surveysQuestions)
    //         newSurvey = insertSurvey(question.getQuestionText(), null, user);
    // }

    //Interface for insert(Survey)
    public Survey insertSurvey(Survey newSurvey)
    {    
        String firstName = newSurvey.getUser().getFirstName();
        String lastName = newSurvey.getUser().getLastName();
        List<Question> allQuestions = questionRepo.findAll();

        User user = userRepo.findByFirstNameAndLastName(firstName, lastName).orElseThrow(() -> new ApplicationMessage.ENTITY_NOT_FOUND("User", firstName + " " + lastName));

        if(allQuestions != null && allQuestions.size() > 0)
        {
            if(newSurvey.getQuestions() != null && newSurvey.getQuestions().size() > 0)
            {
                List<String> questionsInSurvey = newSurvey.getQuestions().stream().map(sq -> sq.getQuestion().getQuestionText()).toList();
                List<Question> questionsMissing = allQuestions.stream().filter(q -> !questionsInSurvey.contains(q.getQuestionText())).toList();

                for(Question questionMissing : questionsMissing)
                {
                    insertSurvey(questionMissing.getQuestionText(), null, user); 
                }
                for(Surveys_Questions questionAdded : newSurvey.getQuestions())
                {
                    newSurvey = insertSurvey(questionAdded.getQuestion().getQuestionText(), questionAdded.getAnswerRating(), user); 
                }
            } 
            else
            {
                for(Question surveyQuestion : allQuestions)
                newSurvey = insertSurvey(surveyQuestion.getQuestionText(), null, user);
            }       
        }
        else
        {
            throw new ApplicationMessage.CHECK_VALUE_FAILED("Question", "No questions are available to build a survey!");
        }

        return newSurvey;
    }
    
    //Interface for insert(String, Integer, String, String)
    public Survey insertSurvey(String questionText, Integer answerRating, String firstName, String lastName)
    {        
        User user = userRepo.findByFirstNameAndLastName(firstName, lastName).orElseThrow(() -> new ApplicationMessage.ENTITY_NOT_FOUND("User", firstName + " " + lastName));
        return insertSurvey(questionText, answerRating, user);
    }

    //private logic to insert a survey
    protected Survey insertSurvey(String questionText, Integer answerRating, User user)
    {   
        Survey newSurvey = null;

        if(answerRating == null || (answerRating >= 0 && answerRating <= 10))
        {
            Question questionDB = questionRepo.findByQuestionText(questionText).orElseThrow(() -> new ApplicationMessage.ENTITY_NOT_FOUND("Question", questionText));
            // User user = userRepo.findByFirstNameAndLastName(firstName, lastName).orElseGet(() -> 
            // {
            //     return userRepo.save(new User(firstName, lastName));
            // });

            newSurvey = repository.findByUser(user).orElseGet(() -> 
            {
                Survey survey = new Survey(user);
                this.repository.save(survey);
                return survey;
            });
            
            Surveys_Questions newSurveysQuestions = new Surveys_Questions(newSurvey, questionDB, answerRating);

            if(!newSurvey.getQuestions().contains(newSurveysQuestions))
                newSurvey.getQuestions().add(newSurveysQuestions);
        }
        else
        {
            throw new ApplicationMessage.CHECK_VALUE_FAILED("Answer", answerRating);
        }

        return repository.save(newSurvey);
    }

}
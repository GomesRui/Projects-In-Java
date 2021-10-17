package com.niahealth.survey.controller;

import java.util.List;
import java.util.Set;

import com.niahealth.survey.model.Survey;
import com.niahealth.survey.model.Surveys_Questions;
import com.niahealth.survey.service.SurveyService;

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
public class SurveyController {
    
    @Autowired
    private SurveyService service;

    // @Autowired
    // private UserService userSer;

    //Get all available surveys from database
    @GetMapping("/surveys/all")
    public List<Survey> findAll()
    {
        return service.findAll();
    }

    //Get Survey by ID
    @GetMapping("/surveys/{id}")
    Survey findById(@PathVariable Long id)
    {
        return service.findById(id);
    }  
    //Get Survey by username
    @GetMapping("/surveys")
    Survey findByUser(@RequestParam(value = "firstName") String firstName, @RequestParam(value = "lastName") String lastName)
    {
        return service.findSurveyByUser(firstName,lastName);
    }  
    
    @PutMapping("/surveys")
    ResponseEntity<String> updateSurvey(@RequestBody Survey newSurvey)
    {
        Survey survey = null;
        String firstName = newSurvey.getUser().getFirstName();
        String lastName = newSurvey.getUser().getLastName();
        Set<Surveys_Questions> newSurveysQuestions = newSurvey.getQuestions();
        
        if(newSurveysQuestions != null && newSurveysQuestions.size() > 0)
        {
            try
            {
                service.findSurveyByUser(firstName,lastName).getId();
            }
            catch(ApplicationMessage.ENTITY_NOT_FOUND ex)
            {
                return new ResponseEntity<String>("Survey does not exist for the user: " + firstName + " " + lastName + "! Create a new user first...", HttpStatus.CONFLICT);
            }
            
            for(Surveys_Questions newSurveyQuestion : newSurveysQuestions)
                survey = service.updateSurveyQuestion(newSurveyQuestion.getQuestion().getQuestionText(), newSurveyQuestion.getAnswerRating(), firstName,lastName);
            
                return new ResponseEntity<String>("Survey was updated: " + survey.getId() + " - " + survey.getName(), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("No questions were attached to the survey: " + newSurvey.getQuestions(), HttpStatus.BAD_REQUEST);
        }

    }

    //Clean surveys
    @DeleteMapping("/surveys/all")
    ResponseEntity<String> clean()
    {
        List<Survey> surveysDB = service.findAll();        
       
        service.deleteAll();
        return new ResponseEntity<String>("All " + surveysDB.size() + " surveys were deleted!",HttpStatus.OK);
    }

    //Delete a Survey by ID
    @DeleteMapping("/surveys/{id}")
    ResponseEntity<String> deleteSurvey(@PathVariable Long id)
    {
        Survey surveyDB = service.findById(id);

        service.deleteById(id);

        return new ResponseEntity<String>("The survey " + surveyDB.getName() + " was deleted!",HttpStatus.OK);
    }
    //Delete a Survey by name
    @DeleteMapping("/surveys/name")
    ResponseEntity<String> deleteSurvey(@RequestParam("name") String name)
    {
        Survey surveyDB = service.findSurveyByName(name);

        service.deleteById(surveyDB.getId());

        return new ResponseEntity<String>("The survey " + surveyDB.getName() + " was deleted!",HttpStatus.OK);
    }
    @DeleteMapping("/surveys/user")
    ResponseEntity<String> deleteSurvey(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName)
    {
        Survey surveyDB = service.findSurveyByUser(firstName, lastName);

        service.deleteById(surveyDB.getId());

        return new ResponseEntity<String>("The survey " + surveyDB.getName() + " was deleted!",HttpStatus.OK);
    }

    //Create Survey for a new User (Official channel)
    @PostMapping("/surveys")
    ResponseEntity<String> createSurvey(@RequestBody Survey newSurvey)
    {
        Survey survey = null;

        String firstName = "";
        String lastName = "";

        try
        {
            if(newSurvey != null)
            {
                if(newSurvey.getUser() != null)
                {
                    firstName = newSurvey.getUser().getFirstName() == null ? "" : newSurvey.getUser().getFirstName() ;
                    lastName = newSurvey.getUser().getLastName() == null ? "" : newSurvey.getUser().getLastName() ;

                    if(firstName.isEmpty() )
                    {
                        throw new ApplicationMessage.INPUT_VALUE_NULL("Survey.User.firstName");
                    }
                    if(lastName.isEmpty() )
                    {
                        throw new ApplicationMessage.INPUT_VALUE_NULL("Survey.User.lastName");
                    }
                } else{
                    throw new ApplicationMessage.INPUT_VALUE_NULL("Survey.User");
                }

                Long id = service.findSurveyByUser(firstName,lastName).getId();
                return new ResponseEntity<String>("Survey already exists: " + id + "! Use the PUT method instead...", HttpStatus.CONFLICT);
            } else{
                throw new ApplicationMessage.INPUT_VALUE_NULL("Survey");
            }
        }
        catch(ApplicationMessage.ENTITY_NOT_FOUND ex)
        {

            survey = service.insertSurvey(newSurvey);
        }

        String questions = "";
        for(Surveys_Questions surveyQuestion : survey.getQuestions())
        {
            questions += "\r\n" + "- questionText: " + surveyQuestion.getQuestion().getQuestionText();
            questions += "\r\n" + "- answerRating: " + surveyQuestion.getAnswerRating();
        }
        return new ResponseEntity<String>("Survey was created: " + survey.getId() + " - " + survey.getName() + ": " + questions, HttpStatus.OK);

    }

    @PostMapping("/surveysTest")
    public Survey test(@RequestBody Survey t)
    {

        return t;

    }
    
}

package com.niahealth.survey.controller;

import java.util.List;

import com.niahealth.survey.model.Question;
import com.niahealth.survey.service.QuestionService;

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
public class QuestionController {
    
    @Autowired
    private QuestionService service;

    //Get all available questions from database
    @GetMapping("/questions/all")
    public List<Question> findAll()
    {
        return service.findAll();
    }

    //Get Question by ID
    @GetMapping("/questions/{id}")
    Question findById(@PathVariable Long id)
    {
        return service.findById(id);
    }  

    //Create new question and add to database
    @PostMapping("/questions")
    Question addNewQuestion(@RequestBody Question newQuestion)
    {
        return service.insertQuestion(newQuestion);
    }  
    
    //Update the questions on a question
    @PutMapping("/questions/{id}")
    Question updateQuestion(@RequestBody Question question, @PathVariable Long id)
    {
        throw new ApplicationMessage.HTTP_METHOD_NOT_AVAILABLE("PUT");
    }

    //Clean questions
    @DeleteMapping(value = "/questions/all")
    ResponseEntity<String> clean()
    {
        List<Question> questionsDB = service.findAll();        
       
        service.deleteAll();
        return new ResponseEntity<String>("All " + questionsDB.size() + " questions were deleted!",HttpStatus.OK);
    }

    //Delete a Question by ID
    @DeleteMapping(value = "/questions/{id}")
    ResponseEntity<String> deleteQuestion(@PathVariable Long id)
    {
        Question questionDB = service.findById(id);

        service.deleteById(id);

        return new ResponseEntity<String>("The question " + questionDB.getQuestionText() + " was deleted!",HttpStatus.OK);
    }
    //Delete a Question by Text
    @DeleteMapping("/questions")
    ResponseEntity<String> deleteQuestion(@RequestParam(value = "questionText") String questionText)
    {
        Question questionDB = service.findQuestionByText(questionText);

        service.deleteById(questionDB.getId());

        return new ResponseEntity<String>("The question " + questionDB.getQuestionText() + " was deleted!",HttpStatus.OK);
    }
}

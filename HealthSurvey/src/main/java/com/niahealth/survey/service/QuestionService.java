package com.niahealth.survey.service;

import java.util.ArrayList;
import java.util.List;

import com.niahealth.survey.controller.ApplicationMessage;
import com.niahealth.survey.model.Question;
import com.niahealth.survey.model.Survey;
import com.niahealth.survey.model.Surveys_Questions;
import com.niahealth.survey.repository.QuestionRepository;
import com.niahealth.survey.repository.SurveyRepository;
import com.niahealth.survey.repository.Surveys_QuestionsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService
{

    @Autowired
    private QuestionRepository repository;
    @Autowired
    private SurveyRepository surveyRepo;
    @Autowired
    private Surveys_QuestionsRepository surveysQuestionsRepo;

    protected Question save(Question question)
    {
        return this.repository.save(question);
    }

    public List<Question> findAll()
    {
        List<Question> questions = this.repository.findAll();

        if(questions != null && questions.size() > 0)
        {
            return questions;
        } else {
            throw new ApplicationMessage.ENTITY_NOT_FOUND("[]");
        }
    }

    public Question findById(Long id)
    {
        return this.repository.findById(id).orElseThrow(() -> new ApplicationMessage.ENTITY_NOT_FOUND(id.toString()));
    }

    public Question findQuestionByText(String text)
    {
        return repository.findAll().stream()
        .filter(q -> q.getQuestionText().equals(text))
        .findFirst()
        .orElseThrow(() -> new ApplicationMessage.ENTITY_NOT_FOUND(text));
    }
    public void deleteById(Long id)
    {
        this.repository.deleteById(id);
    }
    public void deleteAll()
    {
        this.repository.deleteAll();
    }

    //Interface for insert(Question)
    public Question insertQuestion(Question question)
    {
        return insertQuestion(question.getQuestionText());
    }

    //private logic to insert a question
    protected Question insertQuestion(String questionText)
    {
        Question questionDB = null;

        try 
        {
            questionDB = findQuestionByText(questionText);
            
        } catch (ApplicationMessage.ENTITY_NOT_FOUND ex)
        {
            Question newQuestion = new Question(questionText);
            repository.save(newQuestion);

            List<Surveys_Questions> newSurveysQuestions = new ArrayList<Surveys_Questions>();
            for(Survey surveyDB : surveyRepo.findAll())
            {
                //Updating each survey with new question question
                Surveys_Questions newSurveys_Questions = new Surveys_Questions(surveyDB,newQuestion);
                surveysQuestionsRepo.save(newSurveys_Questions);
                
                surveyDB.getQuestions().add(newSurveys_Questions);
                surveyRepo.save(surveyDB);

                newSurveysQuestions.add(newSurveys_Questions);
            }

            //Linking the question to< all the surveys
            newQuestion.getSurveys().addAll(newSurveysQuestions);
            return repository.save(newQuestion);
            
        }

        throw new ApplicationMessage.ENTITY_ALREADY_EXISTS(questionDB.getQuestionText());
    }
}
package com.niahealth.survey;

import com.niahealth.survey.repository.SurveyRepository;
import com.niahealth.survey.repository.UserRepository;
import com.niahealth.survey.service.QuestionService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SurveyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SurveyApplication.class, args);
    }
                                  

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepo, SurveyRepository surveyRepo, QuestionService questionRepo)
    {
        return args -> {    

        };
    }
}



package com.niahealth.survey.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity(name = "Surveys_Questions")
@Table(name = "REL_SURVEY_QUESTION")
public class Surveys_Questions implements Serializable{
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "surveysQuestionsSequence")
    @SequenceGenerator(name = "surveysQuestionsSequence", sequenceName = "SURVEYS_QUESTIONS_SEQUENCE", allocationSize = 1)
    @Column(name = "ID", columnDefinition = "NUMBER", updatable = false)
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @OnDelete(action=OnDeleteAction.CASCADE)
    @JsonBackReference(value = "surveyToQuestion-movement")
    @JsonProperty(value = "survey",access = JsonProperty.Access.READ_WRITE)
    @JoinColumn(name = "SURVEY_ID", columnDefinition = "NUMBER")
    private Survey survey;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}, optional = false)
    @JsonBackReference(value = "questionToSurvey-movement")
    @JoinColumn(name = "QUESTION_ID", columnDefinition = "NUMBER")
    @JsonProperty(value = "question",access = JsonProperty.Access.READ_WRITE)
    private Question question;

    @Column(name = "ANSWER", columnDefinition = "NUMBER CHECK (ANSWER BETWEEN 0 AND 10)", updatable = true, nullable = true)
    private Integer answerRating;
    
    protected Surveys_Questions() 
    {

    }

    @JsonCreator
    public Surveys_Questions(Survey survey, Question question, Integer answerRating)
    {
        this.question = question;
        this.survey = survey;
        this.answerRating = answerRating;
    }
    
    public Surveys_Questions(Survey survey, Question question)
    {
        this.survey = survey;
        this.question = question;
        this.answerRating = null;
    }

    public Long getId() {
        return id;
    }

    public Survey getSurvey() {
        return survey;
    }

    public Question getQuestion() {
        return question;
    }

    public Integer getAnswerRating() {
        return answerRating;
    }

    public void setAnswerRating(Integer answerRating) {
        this.answerRating = answerRating;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }
    public void setQuestion(Question question) {
        this.question = question;
    }
    
  
    @Override
    public String toString() {
        return "Surveys_Questions [answerRating=" + answerRating + ", id=" + (id==null?"":id) + ", question=" + (question==null?"":question) + ", survey="
                + (survey==null?"":survey) + "]";
    }
    @Override
    public boolean equals(Object o) {
  
      if (this == o)
        return true;
      if (!(o instanceof Surveys_Questions))
        return false;
        Surveys_Questions surveysQuestions = (Surveys_Questions) o;
      return Objects.equals(this.id, surveysQuestions.id) && Objects.equals(this.answerRating, surveysQuestions.answerRating)
          && Objects.equals(this.question, surveysQuestions.question) && Objects.equals(this.survey, surveysQuestions.survey);
    }
    
}
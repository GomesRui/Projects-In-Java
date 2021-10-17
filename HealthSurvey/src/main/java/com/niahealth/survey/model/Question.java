package com.niahealth.survey.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(name = "Question")
@Table(name = "DOMAIN_QUESTIONS")
// @SecondaryTable(name = "REL_SURVEY_QUESTION", pkJoinColumns = @PrimaryKeyJoinColumn(name = "QUESTION_ID"))
public class Question implements Serializable{
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "questionSequence")
    @SequenceGenerator(name = "questionSequence", sequenceName = "QUESTION_SEQUENCE", allocationSize = 1, initialValue = 3)
    @Column(name = "ID", columnDefinition = "NUMBER", updatable = false)
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "QUESTION", columnDefinition = "VARCHAR(255)", updatable = true, nullable = false)
    @JsonProperty(value = "questionText", access = JsonProperty.Access.READ_WRITE)
    private String questionText;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference(value = "questionToSurvey-movement")
    @JsonProperty(value = "surveys", access = JsonProperty.Access.READ_WRITE)
    private Set<Surveys_Questions> surveys = new HashSet<Surveys_Questions>();


    protected Question() {

    }


    public Question(String questionText) {
        this.questionText = questionText;
        this.surveys = new HashSet<Surveys_Questions>();
    }
    public Question(String questionText, Survey survey, Integer answerRating) {
        this.questionText = questionText;
        this.surveys.add(new Surveys_Questions(survey,this,answerRating));
    }
    public Question(String questionText, Surveys_Questions surveysQuestions) {
      this.questionText = questionText;
      this.surveys.add(surveysQuestions);
    }
    @JsonCreator
    public Question(String questionText, Set<Surveys_Questions> surveys) {
      this.questionText = questionText;
      this.surveys = surveys;
    } 
    
    public Long getId() {
        return id;
    }
    public String getQuestionText() {
        return questionText;
    }

    public Set<Surveys_Questions> getSurveys() {
        return surveys;
    }
    public void setSurveys(Set<Surveys_Questions> surveys) {
        this.surveys = surveys;
    }
    public void setQuestionText(String questionText)
    {
      this.questionText = questionText;
    }


  @Override
    public String toString() {
      return "Question [id=" + (id==null?"":id) + ", questionText=" + questionText + "]";
    }
  @Override
  public boolean equals(Object o) {

    if (this == o)
      return true;
    if (!(o instanceof Question))
      return false;
      Question question = (Question) o;
    return Objects.equals(this.id, question.id) && Objects.equals(this.questionText, question.questionText);
  }

}

package com.niahealth.survey.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.CreationTimestamp;

@Entity(name = "Survey")
@Table(name = "SURVEYS")
public class Survey implements Serializable{

  @Id
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "surveySequence")
  @SequenceGenerator(name = "surveySequence", sequenceName = "SURVEY_SEQUENCE", allocationSize = 1)
  @Column(name = "ID", columnDefinition = "NUMBER", updatable = false)
  @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY) 
  private Long id;

  @Column(name = "CREATED", nullable = false, columnDefinition = "TIMESTAMP", updatable = false)
  @CreationTimestamp
  @JsonProperty(value = "created", access = JsonProperty.Access.READ_ONLY)
  private Timestamp created;

  @Column(name = "MODIFIED", nullable = true, columnDefinition = "TIMESTAMP", updatable = true)
  @JsonProperty(value = "modified", access = JsonProperty.Access.READ_ONLY)
  private Timestamp modified;

  @JsonProperty(value = "name", access = JsonProperty.Access.READ_ONLY)
  @Column(name = "SURVEY_NAME", nullable = false, columnDefinition = "VARCHAR(255)", updatable = false)
  private String name;

  @OneToOne(orphanRemoval = false,cascade = CascadeType.ALL)
  @JsonProperty(value = "user", access = JsonProperty.Access.READ_WRITE)
  @JsonManagedReference(value = "surveyToUser-movement")
  @JoinColumn(name = "USER_ID", nullable = false, columnDefinition = "NUMBER", unique = true ,referencedColumnName = "ID", updatable = false)
  private User user;

  @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JsonManagedReference(value = "surveyToQuestion-movement")
  @JsonProperty(value = "questions", access = JsonProperty.Access.READ_WRITE)
  private Set<Surveys_Questions> questions = new HashSet<Surveys_Questions>();

  public Survey(User user, Timestamp created) {
    this.name = "SURVEY_"+user.getFirstName()+"_"+user.getLastName();
    this.user = user;
    this.created = created;
    this.modified = null;
    this.questions = new HashSet<Surveys_Questions>();
  }

  public Survey( User user) {
    this.name = "SURVEY_"+user.getFirstName()+"_"+user.getLastName();
    this.user = user;
    this.created = null;
    this.modified = null;
    this.questions = new HashSet<Surveys_Questions>();
  }

  @JsonCreator
  public Survey( User user, Set<Surveys_Questions> questions) {
    this.name = "SURVEY_"+user.getFirstName()+"_"+user.getLastName();
    this.user = user;
    this.created = null;
    this.modified = null;
    this.questions = questions;
  }

  public Survey(User user, Map<String,Integer> questions) {
    this.name = "SURVEY_"+user.getFirstName()+"_"+user.getLastName();
    this.user = user;
    this.created = null;
    this.modified = null;
    this.questions = new HashSet<Surveys_Questions>();

    for(Map.Entry<String,Integer> question : questions.entrySet())
    {
      Surveys_Questions surveyQuestion = new Surveys_Questions(this, new Question(question.getKey()), question.getValue());
      this.questions.add(surveyQuestion);
    }
    
  }

  public Survey() {
   
  }

  
  public Long getId()
  {
    return id;
  }
 
  
  public Timestamp getCreated() {
    return created;
  }

  
  public Timestamp getModified() {
    return modified;
  }

  public void setModified(Timestamp modified) {
    this.modified = modified;
  }

  public void setName() {
    this.name = "SURVEY_"+user.getFirstName()+"_"+user.getLastName();
  }

  public String getName() {
    return name;
  }

  public User getUser() {
    return user;
  }

  public Set<Surveys_Questions> getQuestions() {
    return questions;
  }

  public void setQuestions(Set<Surveys_Questions> questions) {
    this.questions = questions;
  }


  @Override
  public String toString() {
    return "Survey [created=" + (created==null?"":created) + ", id=" + (id==null?"":id) + ", modified=" + (modified==null?"":modified)
        + ", Name=" + name + ", user=" + (user==null?"":user) + "]";
  }
  @Override
  public boolean equals(Object o) {

    if (this == o)
      return true;
    if (!(o instanceof Survey))
      return false;
    Survey survey = (Survey) o;
    return Objects.equals(this.id, survey.id) && Objects.equals(this.created, survey.created)
        && Objects.equals(this.modified, survey.modified) && Objects.equals(this.user.getId(), survey.getUser().getId());
  }

}

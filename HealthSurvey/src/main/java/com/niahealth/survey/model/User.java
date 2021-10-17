package com.niahealth.survey.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(name = "User")
@Table(name = "USERS", uniqueConstraints = { @UniqueConstraint(name = "NAME_CHECK", columnNames = {"FIRST_NAME","LAST_NAME"})})
public class User implements Serializable{
    
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "userSequence")
    @SequenceGenerator(name = "userSequence", sequenceName = "USER_SEQUENCE", allocationSize = 1)
    @Column(name = "ID", columnDefinition = "NUMBER", updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "FIRST_NAME", columnDefinition = "VARCHAR(255)", nullable = false, updatable = false)
    @JsonProperty(value = "firstName", access = JsonProperty.Access.READ_WRITE)
    private String firstName;

    @Column(name = "LAST_NAME", columnDefinition = "VARCHAR(255)", nullable = false, updatable = false)
    @JsonProperty(value = "lastName", access = JsonProperty.Access.READ_WRITE)
    private String lastName;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference(value = "surveyToUser-movement")
    @JsonProperty(value = "survey", access = JsonProperty.Access.READ_WRITE)
    private Survey survey;

    protected User() {

    }

    @JsonCreator
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public User(String firstName, String lastName, Survey survey) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.survey = survey;
    }
    
    public Long getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    
    public Survey getSurvey() {
        return survey;
    }

  
    @Override
    public String toString() {
      return "User [firstName=" + firstName + ", id=" + (id==null?"":id) + ", lastName=" + lastName + ", survey=" + (survey==null?"":survey) + "]";
    }

    @Override
  public boolean equals(Object o) {

    if (this == o)
      return true;
    if (!(o instanceof User))
      return false;
      User user = (User) o;
    return Objects.equals(this.id, user.id) && Objects.equals(this.firstName, user.firstName)
        && Objects.equals(this.lastName, user.lastName) && this.survey.getName().equals(user.getSurvey().getName());
  }

}

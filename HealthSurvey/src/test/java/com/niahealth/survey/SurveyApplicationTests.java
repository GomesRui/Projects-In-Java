package com.niahealth.survey;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.niahealth.survey.controller.ApplicationMessage;
import com.niahealth.survey.model.Question;
import com.niahealth.survey.model.Survey;
import com.niahealth.survey.model.Surveys_Questions;
import com.niahealth.survey.model.User;
import com.niahealth.survey.service.QuestionService;
import com.niahealth.survey.service.SurveyService;
import com.niahealth.survey.service.UserService;
import com.niahealth.survey.utility.TestUtil;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;



@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class SurveyApplicationTests {

	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserService userService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private SurveyService surveyService;

	@Nested
    @DisplayName("User Tests")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	class UserControllerTests
	{
	
		@Test
		@Order(1)
		public void whenGetUserById_thenReturnJsonObject() throws Exception
		{
			User preLoadedUser = userService.insertUser(new User("Rui", "Gomes"));

			mockMvc.perform(get("/users/"+preLoadedUser.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName", is(preLoadedUser.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(preLoadedUser.getLastName())));
		}
		@Test
		@Order(2)
		public void whenGetUsers_thenReturnJsonArrayOfSize2() throws Exception
		{
			User preLoadedUser1 = userService.insertUser(new User("Rui", "Gomes"));
			User preLoadedUser2 = userService.insertUser(new User("Nia", "Health"));

			mockMvc.perform(get("/users/all")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)));

		}
		@Test
		@Order(3)
		public void whenPostUser_thenReturnJsonObject() throws Exception
		{
			User bot1 = new User("Bot","1");
	
			mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(bot1)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName", is(bot1.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(bot1.getLastName())));
		}
		@Test
		@Order(4)
		public void whenDeleteUser_thenReturnSucess() throws Exception
		{
			User preloadedUser = userService.insertUser(new User("Rui", "Gomes"));
	
			mockMvc.perform(delete("/users/"+preloadedUser.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string("The user " + preloadedUser.getFirstName() + " " + preloadedUser.getLastName() + " was deleted!"));
		}

		@Test
		@Order(5)
		public void whenDeleteUserByName_thenReturnSucess() throws Exception
		{
			User preloadedUser = userService.insertUser(new User("Rui", "Gomes"));
	
			mockMvc.perform(delete("/users?"+"firstName="+preloadedUser.getFirstName()+"&lastName="+preloadedUser.getLastName())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string("The user " + preloadedUser.getFirstName() + " " + preloadedUser.getLastName() + " was deleted!"));
		}

		@AfterEach
		public void cleanup()
		{
			userService.deleteAll();
		}

	}

	@Nested
    @DisplayName("Survey Tests")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	class SurveyControllerTests
	{
		@Order(1)
		@Test
		public void whenGetSurveyById_thenReturnJsonObject() throws Exception
		{
			Survey preLoadedSurvey = surveyService.save(new Survey(new User("Rui", "Gomes")));
			
			mockMvc.perform(get("/surveys/"+preLoadedSurvey.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(preLoadedSurvey.getName())))
				.andExpect(jsonPath("$.user.firstName", is(preLoadedSurvey.getUser().getFirstName())))
				.andExpect(jsonPath("$.user.lastName", is(preLoadedSurvey.getUser().getLastName())));
		}
		@Order(2)
		@Test
		public void whenGetSurveyByUserName_thenReturnJsonObject() throws Exception
		{
			Survey preLoadedSurvey = surveyService.save(new Survey(new User("Rui", "Gomes")));
			
			mockMvc.perform(get("/surveys?"+"firstName="+preLoadedSurvey.getUser().getFirstName()+"&lastName="+preLoadedSurvey.getUser().getLastName())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(preLoadedSurvey.getName())))
				.andExpect(jsonPath("$.user.firstName", is(preLoadedSurvey.getUser().getFirstName())))
				.andExpect(jsonPath("$.user.lastName", is(preLoadedSurvey.getUser().getLastName())));
		}
		@Order(3)
		@Test
		public void whenGetSurveys_thenReturnJsonArrayOfSize2() throws Exception
		{
			Survey preLoadedSurvey1 = surveyService.save(new Survey(new User("Rui", "Gomes")));
			Survey preLoadedSurvey2 = surveyService.save(new Survey(new User("Nia", "Health")));

			mockMvc.perform(get("/surveys/all")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)));
		}
		@Order(4)
		@Test
		public void whenPostSurveysWithoutAnswers_thenReturnJsonArray() throws Exception
		{
			User user = userService.save(new User("Rui","Gomes"));
			Survey preloadedSurvey = new Survey(user);

			String questions = "";
			List<Question> questionSet = questionService.findAll();
			for(Question question : questionSet)
        	{
            	questions += "\r\n" + "- questionText: " + question.getQuestionText();
            	questions += "\r\n" + "- answerRating: " + "null";
			}

			mockMvc.perform(post("/surveys")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(preloadedSurvey)))
				.andExpect(status().isOk())
				.andExpect(content().string("Survey was created: " + 1 + " - " + preloadedSurvey.getName() + ": " + questions));
		}
		@Test
		@Order(5)
		public void whenPostSurveysWithAllAnswers_thenReturnJsonArray() throws Exception
		{

			User user = userService.save(new User("Rui","Gomes"));
			Survey preloadedSurvey = new Survey(user);

			Question question1 = new Question("How was your sleep last night?");
			Question question2 = new Question("How good is your skin condition?");


			Surveys_Questions surveyQuestion1 = new Surveys_Questions(preloadedSurvey, question1,10);
			question1.setSurveys(new HashSet<Surveys_Questions>(Arrays.asList(new Surveys_Questions[]{surveyQuestion1})));

			Surveys_Questions surveyQuestion2 = new Surveys_Questions(preloadedSurvey, question2, 8);
			question2.setSurveys(new HashSet<Surveys_Questions>(Arrays.asList(new Surveys_Questions[]{surveyQuestion2})));

			preloadedSurvey.setQuestions(new HashSet<Surveys_Questions>()
			{
				{
					add(surveyQuestion1);
					add(surveyQuestion2);
				}
			});


			String questions = "";
			List<Question> questionSet = questionService.findAll();
            	questions += "\r\n" + "- questionText: " + questionSet.get(0).getQuestionText();
            	questions += "\r\n" + "- answerRating: " + "10";
            	questions += "\r\n" + "- questionText: " + questionSet.get(1).getQuestionText();
            	questions += "\r\n" + "- answerRating: " + "8";

			mockMvc.perform(post("/surveys")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(preloadedSurvey)))
				.andExpect(status().isOk())
				.andExpect(content().string("Survey was created: " + 1 + " - " + preloadedSurvey.getName() + ": " + questions));
			
		}
		@Order(6)
		public void whenPostSurveysWithSomeAnswers_thenReturnJsonArray() throws Exception
		{

			User user = userService.save(new User("Rui","Gomes"));
			Survey preloadedSurvey = new Survey(user);
			preloadedSurvey.setQuestions(new HashSet<Surveys_Questions>()
			{
				{
				}
			});

			
			mockMvc.perform(post("/surveys")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(preloadedSurvey)))
				.andExpect(status().isOk())
				.andExpect(content().string("Survey was created: " + 1 + " - " + preloadedSurvey.getName()));
		}
		@Test
		@Order(7)
		public void whenDeleteSurvey_thenReturnSucess() throws Exception
		{
			Survey preloadedSurvey = surveyService.save(new Survey(new User("Rui", "Gomes")));
	
			mockMvc.perform(delete("/surveys/"+preloadedSurvey.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string("The survey " + preloadedSurvey.getName() + " was deleted!"));
		}
		@Test
		@Order(8)
		public void whenPutSurvey_thenReturnSucess() throws Exception
		{
			Survey preloadedSurvey = surveyService.save(new Survey(new User("Rui", "Gomes"), new HashMap<String,Integer>()
			{
				{
					put("How was your sleep last night?",10);
					put("How good is your skin condition?",8);
				}
			}));
	
			mockMvc.perform(put("/surveys")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(preloadedSurvey)))
				.andExpect(status().isOk())
				.andExpect(content().string("Survey was updated: " + 1 + " - " + preloadedSurvey.getName()));
		}

		@Test
		@Order(9)
		public void whenDeleteSurveyByUsername_thenReturnSucess() throws Exception
		{
			Survey preLoadedSurvey = surveyService.save(new Survey(new User("Rui", "Gomes")));
	
			mockMvc.perform(delete("/surveys/user?"+"firstName="+preLoadedSurvey.getUser().getFirstName()+"&lastName="+preLoadedSurvey.getUser().getLastName())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string("The survey " + preLoadedSurvey.getName() + " was deleted!"));
		}

		@AfterEach
		public void cleanup()
		{
			surveyService.deleteAll();
		}

	}

	@Nested
    @DisplayName("Question Tests")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	class QuestionControllerTests
	{
		@Test
		@Order(1)
		public void whenGetQuestionById_thenReturnJsonObject() throws Exception
		{
			Question preLoadedQuestion = questionService.findQuestionByText("How was your sleep last night?");
			mockMvc.perform(get("/questions/"+preLoadedQuestion.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.questionText", is(preLoadedQuestion.getQuestionText())));
		}
		@Test
		@Order(2)
		public void whenGetQuestions_thenReturnJsonArrayOfSize2() throws Exception
		{
			List<Question> preLoadedQuestions = questionService.findAll();

			mockMvc.perform(get("/questions/all")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(preLoadedQuestions.size())));

		}
		@Test
		@Order(3)
		public void whenPostQuestion_thenReturnJsonObject() throws Exception
		{
			Question newQuestion = new Question("How dry is your skin?");
			mockMvc.perform(post("/questions")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(newQuestion)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.questionText", is(newQuestion.getQuestionText())));
		}
		@Test
		@Order(4)
		public void whenDeleteQuestion_thenReturnSucess() throws Exception
		{
			Question question = questionService.findQuestionByText("How was your sleep last night?");
			mockMvc.perform(delete("/questions/"+question.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string("The question " + question.getQuestionText() + " was deleted!"));
		}

		@Test
		@Order(5)
		public void whenDeleteQuestionByName_thenReturnSucess() throws Exception
		{
	
			Question question = new Question("How was your sleep last night?");
			mockMvc.perform(delete("/questions?"+"questionText="+question.getQuestionText())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string("The question " + question.getQuestionText() + " was deleted!"));
		}

		@AfterEach
		public void cleanup()
		{
			Question question1 = new Question("How was your sleep last night?");
			Question question2 = new Question("How good is your skin condition?");

			List<Question> questions = new ArrayList<Question>(Arrays.asList(new Question[]{question1,question2}));
			for(Question question : questions)
			{
				try
				{
					questionService.findQuestionByText(question.getQuestionText());
				} catch(ApplicationMessage.ENTITY_NOT_FOUND ex)
				{
					questionService.insertQuestion(question);
				}
			}

		}


	}
	
	@Nested
    @DisplayName("Create Swagger")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	public class GenerateSwagger {
		
		private final String outputDir = "C:/Users/UnikeViber/Desktop/Learning/Spring/survey/src/main/resources/static";
		
		@Test
    	public void createSpringfoxSwaggerJson() throws Exception 
		{
			
		}
		// private final String outputDir = "C:/Users/UnikeViber/Desktop/Learning/Spring/survey/src/main/resources/static";
		
		// @Test
    	// public void createSpringfoxSwaggerJson() throws Exception {
		// 	MvcResult mvcResult = mockMvc.perform(get("/swagger-ui/index.html#/")
		// 			.accept(MediaType.APPLICATION_JSON))
		// 			.andExpect(status().isOk())
		// 			.andReturn();

		// 	MockHttpServletResponse response = mvcResult.getResponse();
		// 	String swaggerJson = response.getContentAsString();
		// 	Files.createDirectories(Paths.get(outputDir));
		// 	try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputDir, "swagger.json"), StandardCharsets.UTF_8))
		// 	{
		// 		writer.write(swaggerJson);
		// 	}
		// }
	}

}

package controllers;

// imports emil
import java.util.Map;
import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import play.db.jpa.*;
import play.data.*;
import models.UserModel;
//



import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import models.GameState;
import play.*;
import play.mvc.*;
import at.ac.tuwien.big.we15.lab2.api.*;
import at.ac.tuwien.big.we15.lab2.api.impl.PlayJeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.impl.SimpleJeopardyGame;
import at.ac.tuwien.big.we15.lab2.api.impl.SimpleUser;
import views.html.*;
import play.data.*;

public class Application extends Controller {
	
	//wenn framework aufgerufen wird soll login-seite erscheinen
	@play.db.jpa.Transactional
	public static Result index(){
		
		return redirect(routes.Application.authentication());
	}
	
	//logout von Jeopardy
	public static Result logout(){
		session().remove("username");
		session().remove("avatar");
		return redirect(routes.Application.authentication());
	}
	
	//loads the Jeopardy site
	@Security.Authenticated(Secured.class)
	public static Result jeopardy(){
		JeopardyFactory jeopardyFactory;
		if(lang().code().equals("de")){
			jeopardyFactory = new PlayJeopardyFactory("data.de.json");
		}
		else {
			jeopardyFactory = new PlayJeopardyFactory("data.en.json");
		}
		User user = new SimpleUser();
		user.setName(session().get("username"));
		user.setAvatar(Avatar.getAvatar(session().get("avatar")));
		JeopardyGame jeopardyGame = new SimpleJeopardyGame(jeopardyFactory, user);
		GameState.getGameStateMap().put(session().get("username"), jeopardyGame);
		return ok(jeopardy.render(GameState.getGameStateMap().get(session().get("username")), session().get("timestamp")));
	}
	
	//loads the Winner site
	public static Result showWinner(){
		return ok(winner.render(GameState.getGameStateMap().get(session().get("username"))));
	}
	
	//loads the question site
	public static Result showQuestion(){
		DynamicForm form = Form.form().bindFromRequest();
		if(form.data().get("question_selection") == null){
			return ok(jeopardy.render(GameState.getGameStateMap().get(session().get("username")), session().get("timestamp")));
		}
		Integer questionId = Integer.parseInt(form.data().get("question_selection"));

		JeopardyGame jeopardyGame = GameState.getGameStateMap().get(session().get("username"));

		jeopardyGame.chooseHumanQuestion(questionId);
		return ok(question.render(jeopardyGame, jeopardyGame.getHumanPlayer().getChosenQuestion()));
	}
	
	public static Result newGame(){
		JeopardyFactory jeopardyFactory;
		if(lang().code().equals("de")){
			jeopardyFactory = new PlayJeopardyFactory("data.de.json");
		}
		else {
			jeopardyFactory = new PlayJeopardyFactory("data.en.json");
		}
		User user = new SimpleUser();
		user.setName(session().get("username"));
		System.out.println("NEW GAME AVATAR : " + session().get("avatar"));
		System.out.println("AVATAR == NULL ? " + Avatar.getAvatar(session().get("avatar")) == null);
		user.setAvatar(Avatar.getAvatar(session().get("avatar")));
		GameState.getGameStateMap().replace(session().get("username"), new SimpleJeopardyGame(jeopardyFactory, user));
		return ok(jeopardy.render(GameState.getGameStateMap().get(session().get("username")), session().get("timestamp")));
	}

	public static Result answerQuestion(){
		try{
		DynamicForm form = Form.form().bindFromRequest();
		JeopardyGame jeopardyGame = GameState.getGameStateMap().get(session().get("username"));
		//TODO fill with answers
		String[] choice = request().body().asFormUrlEncoded().get("answers[]");
		List<Integer> answers = new ArrayList<Integer>();
		
		Question currentQuestion = jeopardyGame.getHumanPlayer().getChosenQuestion();
		List<Answer> allAnswers = currentQuestion.getAllAnswers();

			if(choice != null){
				for(String now : choice){
					for (Answer answer : allAnswers){
						if(answer.getId() == Integer.parseInt(now)) {
							answers.add(answer.getId());
						}
					}
				}
			}
		
		
		jeopardyGame.answerHumanQuestion(answers);
		
		if(jeopardyGame.isGameOver()){
			System.out.println(getTimeStamp());
			session().put("timestamp", getTimeStamp().toString());
			return ok(winner.render(jeopardyGame));
		}
		else {
			return ok(jeopardy.render(jeopardyGame, session().get("timestamp")));
		}

		}catch(NullPointerException e){
			return badRequest();
		}
	}
	
		public static Result registration() {
        return ok(registration.render());
    }
    
	//Inserted by Emil begin
    @Transactional
    public static Result registrationSubmit() {
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        Map<String, String> formData = dynamicForm.data();
        
        UserModel user = JPA.em().find(UserModel.class, formData.get("username"));
        boolean usernameValidationError = false;
        
        // Username is already taken
        if (user != null) {
            
            usernameValidationError = true;
        }
        
        UserModel newUser = new UserModel();
        newUser.setName(formData.get("username"));
        newUser.setAvatar(Avatar.getAvatar(formData.get("avatar")));
        newUser.setPassword(formData.get("password"));
        newUser.setFirstName(formData.get("firstname"));
        newUser.setLastName(formData.get("lastname"));
        
        if (formData.get("gender").equals("m")) {
            newUser.setSex(true);
        } else {
            newUser.setSex(false);
        }
        
        String dateString = formData.get("birthdate");
        boolean dateValidationError = false;
        Date date = null;
        
        // date validation
        if (!dateString.equals("")) {
            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            
            try {
            
                date = df.parse(dateString);
            
            } catch (ParseException e) {
            
                dateValidationError = true;
            }
        
        }
        
        if (dateValidationError || usernameValidationError) {
            
            return redirect(routes.Application.registration());
            
        } else {
            
            //if birthdate and username both are valid
            if (date != null) {
                newUser.setBirthdate(new java.sql.Date(date.getTime()));
            }
            JPA.em().persist(newUser);
            return redirect(routes.Application.index());
        }
    }
    
    public static Result authentication() {
        
        return ok(authentication.render());
    }
    
    @Transactional
    public static Result authenticationSubmit() {
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        Map<String, String> formData = dynamicForm.data();
        
        String username = formData.get("username");
        String password = formData.get("password");
        
        UserModel user = JPA.em().find(UserModel.class, username);
        
        if (user != null) {
            
            if (user.getPassword().equals(password)) {
                
                session().remove("username");
                session().remove("avatar");
                session("username", username);
                session("avatar", user.getAvatar().getId());
                return redirect(routes.Application.jeopardy());
            }
        }
        
        return redirect(routes.Application.index());
    }
	//inserted by emil end
	
	public static class Login{
		public String username;
		public String password;
	}
	
	
	public static Timestamp getTimeStamp() {
		Date date = new Date();
		return new Timestamp(date.getTime());
    }
	
}

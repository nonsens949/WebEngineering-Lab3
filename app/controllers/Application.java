package controllers;

// imports emil
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import play.db.jpa.*;
import play.data.*;
import models.UserModel;
//

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import models.GameState;
import play.*;
import play.mvc.*;
import at.ac.tuwien.big.we15.lab2.api.*;
import at.ac.tuwien.big.we15.lab2.api.impl.PlayJeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.impl.SimpleJeopardyGame;
import views.html.*;
import play.data.*;

public class Application extends Controller {
	
	
	public static HashMap<String, Object> objects = new HashMap<String, Object>();
	
	//wenn framework aufgerufen wird soll login-seite erscheinen
	@play.db.jpa.Transactional
	public static Result index(){
		
		return redirect(routes.Application.authentication());
	}
	
	/*
	//login-seite
	@play.db.jpa.Transactional
    public static Result authenticate() {
        return ok(authentication.render(Form.form(Login.class)));
    }
	
	//von login seite werden daten weitergeleitet
	@play.db.jpa.Transactional
    public static Result registration() {
		return ok(registration.render());
    }
	*/
	
	//logout von Jeopardy
	public static Result logout(){
		session().clear();
		return redirect(routes.Application.authentication());
	}
	
	//loads the Jeopardy site
	@Security.Authenticated(Secured.class)
	public static Result jeopardy(){
		//Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
		//session("username", loginForm.get().username);
		JeopardyFactory jeopardyFactory;
		if(lang().code().equals("de")){
			jeopardyFactory = new PlayJeopardyFactory("data.de.json");
		}
		else {
			jeopardyFactory = new PlayJeopardyFactory("data.en.json");
		}
		JeopardyGame jeopardyGame = new SimpleJeopardyGame(jeopardyFactory, session().get("username") );
		GameState.getGameStateMap().put(session().get("username"), jeopardyGame);
		return ok(jeopardy.render(GameState.getGameStateMap().get(session().get("username"))));
	}
	
	//loads the Winner site
	public static Result showWinner(){
		return ok(winner.render(GameState.getGameStateMap().get(session().get("username"))));
	}
	
	//loads the question site
	public static Result showQuestion(){
		DynamicForm form = Form.form().bindFromRequest();
		Integer questionId = Integer.parseInt(form.data().get("question_selection"));
//		System.out.println("empty? " + form.data().keySet().isEmpty());
//		for(String s : form.data().keySet()){
//			System.out.println("s = " + s);
//			System.out.println("" + s  + " = " + form.data().get(s));
//		}
//		System.out.println(form.data().get("questionId"));
		JeopardyGame jeopardyGame = GameState.getGameStateMap().get(session().get("username"));
		List<Category> categories = jeopardyGame.getCategories();
		Question chosenQuestion = null;
		for(Category c : categories){
			List<Question> questionList = c.getQuestions();
			for(Question q: questionList){
				if(q.getId() == questionId){
					chosenQuestion = q;
				}
			}
		}
		jeopardyGame.chooseHumanQuestion(questionId);
		return ok(question.render(jeopardyGame, chosenQuestion));
	}
	
	public static Result newGame(){
		return ok(jeopardy.render(GameState.getGameStateMap().get(session().get("username"))));
	}

	public static Result answerQuestion(){
		DynamicForm form = Form.form().bindFromRequest();
		JeopardyGame jeopardyGame = GameState.getGameStateMap().get(session().get("username"));
		//TODO fill with answers
		List<Integer> answers = new ArrayList<Integer>();
		jeopardyGame.answerHumanQuestion(answers);
		
		
//		System.out.println("empty? " + form.data().keySet().isEmpty());
//		for(String s : form.data().keySet()){
//			System.out.println("s = " + s);
//			System.out.println("" + s  + " = " + form.data().get(s));
//		}
//		System.out.println(form.data().values());
//		System.out.println(form.data().containsValue("2"));
		
		return ok(jeopardy.render(jeopardyGame));
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
                
                session().clear();
                session("username", username);
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
	
	
}

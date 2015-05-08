package controllers;

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
		
		return redirect(routes.Application.authenticate());
	}
	
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
	
	//logout von Jeopardy
	public static Result logout(){
		session().clear();
		return redirect(routes.Application.authenticate());
	}
	
	//loads the Jeopardy site
	public static Result jeopardy(){
		Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
		session("username", loginForm.get().username);
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
	
	
	public static class Login{
		public String username;
		public String password;
	}
	
	
}

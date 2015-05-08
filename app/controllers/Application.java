package controllers;

import java.io.InputStream;
import java.util.HashMap;

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
        return TODO;
    }
	
	//logout von Jeopardy
	public static Result logout(){
		return redirect(routes.Application.authenticate());
	}
	
	//loads the Jeopardy site
	public static Result jeopardy(){
		Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
		session("username", loginForm.get().username);
		JeopardyFactory jeopardyFactory = new PlayJeopardyFactory("data.de.json");
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
		JeopardyGame jeopardyGame = GameState.getGameStateMap().get(session().get("username"));
		return ok(question.render(jeopardyGame));
	}
	
	public static Result newGame(){
		return ok(jeopardy.render(GameState.getGameStateMap().get(session().get("username"))));
	}

	
	public static class Login{
		public String username;
		public String password;
	}
	
}

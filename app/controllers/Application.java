package controllers;

import play.*;
import play.mvc.*;
import at.ac.tuwien.big.we15.lab2.api.*;
import at.ac.tuwien.big.we15.lab2.api.impl.SimplePlayer;
import views.html.*;
import controllers.Application.*;

public class Application extends Controller {
	
	//wenn framework aufgerufen wird soll login-seite erscheinen
	@play.db.jpa.Transactional
	public static Result index(){
		return redirect(routes.Application.login());
	}
	
	//login-seite
	@play.db.jpa.Transactional
    public static Result login() {
		//TODO player ersetzen
		session().clear();
		session("email", "bla");
		Player human = new SimplePlayer();
		//Cache.set("human", human);
        return ok(authentication.render());
    }
	
	//von login seite werden daten weitergeleitet
	@play.db.jpa.Transactional
    public static Result registration() {
        return TODO;
    }
	
	public static Result mainGame(){
		return TODO;
	}
	
	public static Result newGame(){
		Player human = new SimplePlayer();
		return ok(jeopardy.render(Avatar.ALDRICH_KILLIAN, human));
	}
	Player human = new SimplePlayer();
	public static Result showWinner(){
		Player human = new SimplePlayer();
		return ok(winner.render(human));
	}
	
	public static Result chooseQuestion(){
		return ok(question.render());
	}
	
}

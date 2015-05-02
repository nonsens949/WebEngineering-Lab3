package controllers;

import play.*;
import play.mvc.*;
import at.ac.tuwien.big.we15.lab2.api.*;
import views.html.*;

public class Application extends Controller {
	
	//wenn framework aufgerufen wird soll login-seite erscheinen
	@play.db.jpa.Transactional
    public static Result login() {
        return ok(authentication.render());
    }
	
	//von login seite werden daten weitergeleitet
	@play.db.jpa.Transactional
    public static Result authentication() {
        return ok(index.render("Your new application is ready."));
    }
	
	
}

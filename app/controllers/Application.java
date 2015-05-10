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


import controllers.MainGame;

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
                return redirect(routes.MainGame.jeopardy());
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

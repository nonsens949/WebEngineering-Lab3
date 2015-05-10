package controllers;

import java.util.ArrayList;
import java.util.List;

import models.GameState;
import at.ac.tuwien.big.we15.lab2.api.Answer;
import at.ac.tuwien.big.we15.lab2.api.Avatar;
import at.ac.tuwien.big.we15.lab2.api.JeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.JeopardyGame;
import at.ac.tuwien.big.we15.lab2.api.Question;
import at.ac.tuwien.big.we15.lab2.api.User;
import at.ac.tuwien.big.we15.lab2.api.impl.PlayJeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.impl.SimpleJeopardyGame;
import at.ac.tuwien.big.we15.lab2.api.impl.SimpleUser;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.jeopardy;
import views.html.question;
import views.html.winner;

public class MainGame extends Controller {
	
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
		session().put("timestamp", Application.getTimeStamp().toString());
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
		user.setAvatar(Avatar.getAvatar(session().get("avatar")));
		GameState.getGameStateMap().replace(session().get("username"), new SimpleJeopardyGame(jeopardyFactory, user));
		return ok(jeopardy.render(GameState.getGameStateMap().get(session().get("username")), session().get("timestamp")));
	}

	public static Result answerQuestion(){
	
			DynamicForm form = Form.form().bindFromRequest();
			JeopardyGame jeopardyGame = GameState.getGameStateMap().get(session().get("username"));
			
			String[] choice = request().body().asFormUrlEncoded().get("answers");
			List<Integer> answers = new ArrayList<Integer>();
			
			if(choice != null){
				for(String c : choice){
					answers.add(Integer.parseInt(c.substring("answers".length())));
				}
			}
			
			jeopardyGame.answerHumanQuestion(answers);
			
			if(jeopardyGame.isGameOver()){
				return redirect(routes.MainGame.showWinner());
			}
			else {
				return ok(jeopardy.render(jeopardyGame, session().get("timestamp")));
			}

	}
	
	
}
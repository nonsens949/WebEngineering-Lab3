package controllers;

import play.*;
import play.mvc.*;
import jeopardy.at.ac.tuwien.big.we15.lab2.api.*;
import views.html.*;

public class Application extends Controller {
	
	//wenn framework aufgerufen wird soll login-seite erscheinen
	@play.db.jpa.Transactional
    public static Result login() {
        return ok(index.render("Your new application is ready."));
    }
	
	//von login seite werden daten weitergeleitet
	@play.db.jpa.Transactional
    public static Result authentication() {
        return ok(index.render("Your new application is ready."));
    }
	
	JeopardyFactory factory = new PlayQuizFactory(jsonFilePat);
	JeopardyGame game = factory.createGame(user);

	Player human = game.getHumanPlayer(); // get human player
	human.getProfit(); // get current profit of player
	human.getLatestProfitChange(); // get last profit change (for display)

	game.chooseHumanQuestion(questionId); // choose next human question
	game.answerHumanQuestion(answerIds); // answer current human questions
	game.isRoundStart(); // check if we are at the beginning of a new round
	game.isAnswerPending(); // check if the current question needs to be answered
	game.isGameOver(); // check if game is over
	game.getWinner(); // winner of round or null if no winner exists yet

}

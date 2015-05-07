package models;

import java.util.Collections;
import java.util.List;
import at.ac.tuwien.big.we15.lab2.api.*;

public class Opponent {

//	public Question nextQuestion(QuestionCatalog catalog, GameStatus status) {
//		List<Question> unselectedQuestions = catalog.getUnselectedQuestions();
//		Collections.shuffle(unselectedQuestions);
//		return unselectedQuestions.get(0);
//	}

	public boolean answerQuestion(Question question) {
		float result = 1f/question.getValue();
		double random = Math.random();
		return result <= random;
	}
	
}

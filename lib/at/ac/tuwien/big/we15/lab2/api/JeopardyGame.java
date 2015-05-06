/**
 * <copyright>
 *
 * Copyright (c) 2014 http://www.big.tuwien.ac.at All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * </copyright>
 */
package at.ac.tuwien.big.we15.lab2.api;

import java.util.List;


/**
 * Represents a quiz game
 */
public interface JeopardyGame {

	User getHuman();
	
	User getMarvin();
	
	Player getHumanPlayer();

	Player getMarvinPlayer();
	
	Player getLeader();
	
	Player getSecond();

	Player getWinner();
	
	Player getLoser();
	
	List<Category> getCategories();

	boolean isGameOver();
	
	boolean isRoundStart();

	boolean isAnswerPending();

	void chooseHumanQuestion(int questionId);

	void answerHumanQuestion(List<Integer> answerIds);

	boolean hasBeenChosen(Question question);

	int getMaxQuestions();

	boolean isHumanPlayer(User user);

	boolean isComputerPlayer(User user);	
}

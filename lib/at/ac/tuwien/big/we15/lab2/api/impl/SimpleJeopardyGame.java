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
package at.ac.tuwien.big.we15.lab2.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import at.ac.tuwien.big.we15.lab2.api.Answer;
import at.ac.tuwien.big.we15.lab2.api.Avatar;
import at.ac.tuwien.big.we15.lab2.api.Category;
import at.ac.tuwien.big.we15.lab2.api.JeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.JeopardyGame;
import at.ac.tuwien.big.we15.lab2.api.Player;
import at.ac.tuwien.big.we15.lab2.api.Question;
import at.ac.tuwien.big.we15.lab2.api.QuestionDataProvider;
import at.ac.tuwien.big.we15.lab2.api.User;

public class SimpleJeopardyGame implements JeopardyGame {

	public static JeopardyFactory DEFAULT_FACTORY = null;
	
	private static final int NUM_QUESTIONS = 5;

	private Random random = new Random();
	
	private JeopardyFactory factory;
	private List<Category> categories;
	private Player human, marvin; // marvin == computer opponent	
	
	private Map<Integer, Question> idToQuestion = new TreeMap<>();
	private List<Question> openQuestions = new ArrayList<>();
	private List<Question> chosenQuestions = new ArrayList<>();
		

	public SimpleJeopardyGame(JeopardyFactory factory, User human) {
		this.factory = factory;
		this.human = createHumanPlayer(human);
		this.marvin = createComputerPlayer();
		initializeQuestions();
	}
	
	public SimpleJeopardyGame(JeopardyFactory factory, String humanName) {
		this.factory = factory;
		this.human = createHumanPlayer(createHumanUser(humanName));
		this.marvin = createComputerPlayer();
		initializeQuestions();
	}

	public SimpleJeopardyGame(JeopardyFactory factory) {
		this(factory, "Spieler 1");
	}
	
	public SimpleJeopardyGame() {
		this(DEFAULT_FACTORY);
	}
	
	private Player createHumanPlayer(User user) {
		return new SimplePlayer(user);
	}

	private User createHumanUser(String name) {
		User user = factory.createUser();
		user.setName(name);
		user.setAvatar(Avatar.getRandomAvatar());
		return user;
	}

	private Player createComputerPlayer() {
		User user = factory.createUser();
		user.setAvatar(Avatar.getOpponent(getHumanPlayer().getUser().getAvatar()));
		user.setName(user.getAvatar().getName());	
		return new SimplePlayer(user);
	}
	
	protected void initializeQuestions() {
		QuestionDataProvider provider = factory.createQuestionDataProvider();
		categories = provider.getCategoryData();
		for(Category category : categories) 
			for(Question question : category.getQuestions()) {
				idToQuestion.put(question.getId(), question);
				openQuestions.add(question);
			}
	}
	
	public JeopardyFactory getFactory() {
		return factory;
	}
	
	public List<Category> getCategories() {
		return categories;
	}
	
	protected Random getRandom() {
		return random;
	}
	
	@Override
	public Player getHumanPlayer() {
		return human;
	}
	
	@Override
	public Player getMarvinPlayer() {
		return marvin;
	}
	
	@Override
	public User getHuman() {
		return getHumanPlayer().getUser();
	}
	
	@Override
	public User getMarvin() {
		return getMarvinPlayer().getUser();
	}
	
	@Override
	public boolean isHumanPlayer(User user) {
		return getHumanPlayer().getUser() == user;
	}
	
	@Override
	public boolean isComputerPlayer(User user) {
		return getMarvinPlayer().getUser() == user;
	}
		
	@Override
	public void chooseHumanQuestion(int questionId) {
		Question question = idToQuestion.get(questionId);
		if(question == null || chosenQuestions.contains(question)) 
			throw new IllegalArgumentException("Question can not be chosen.");
		
		chooseQuestion(getHumanPlayer(), question);
		
		// if marvin has not chosen a new question yet, he will do so now
		if(getMarvinPlayer().hasAnsweredQuestion())
			autoChooseQuestionMarvin();
	}
	
	@Override
	public void answerHumanQuestion(List<Integer> answers) {
		List<Answer> givenAnswers = new ArrayList<>();
		for(Answer answer : getHumanPlayer().getChosenQuestion().getAllAnswers()) {
			if(answers.contains(answer.getId()))
				givenAnswers.add(answer);
		}
		getHumanPlayer().answerQuestion(givenAnswers);
		
		autoAnswerQuestionMarvin();
		
		// marvin chooses first if he has less profit than human opponent
		if(getMarvinPlayer().getProfit() < getHumanPlayer().getProfit())
			autoChooseQuestionMarvin();
	}
	
	@Override
	public int getMaxQuestions() {
		return NUM_QUESTIONS;
	}
	
	protected void chooseQuestion(Player player, Question question) {
		player.chooseQuestion(question);
		chosenQuestions.add(question);
		openQuestions.remove(question);
	}
	
	protected void autoChooseQuestionMarvin() {
		Question question = openQuestions.get(
				getRandom().nextInt(openQuestions.size()));
		chooseQuestion(getMarvinPlayer(), question);	
	}

	private void autoAnswerQuestionMarvin() {
		if(getRandom().nextDouble() < 0.5)
			getMarvinPlayer().answerQuestion( 
				getMarvinPlayer().getChosenQuestion().getCorrectAnswers());
		else
			getMarvinPlayer().answerQuestion( 
					getMarvinPlayer().getChosenQuestion().getAllAnswers());
	}

	@Override
	public boolean isGameOver() {
		return getHumanPlayer().getAnsweredQuestions().size() == NUM_QUESTIONS &&
			   getMarvinPlayer().getAnsweredQuestions().size() == NUM_QUESTIONS;
	}

	@Override
	public boolean isRoundStart() {
		return !isGameOver() &&
				getHumanPlayer().hasAnsweredQuestion();
	}
	
	@Override
	public boolean isAnswerPending() {
		return !getHumanPlayer().hasAnsweredQuestion();
	}
	
	@Override
	public Player getLeader() {
		if(human.getProfit() >= marvin.getProfit())
			return human; // advantage human
		return marvin;
	}
	
	@Override
	public Player getSecond() {
		if(human.getProfit() >= marvin.getProfit())
			return marvin;	
		return human;
	}

	@Override
	public Player getWinner() {
		if(!isGameOver()) 
			return null;
		
		if(human.getProfit() > marvin.getProfit())
			return human;	
		return marvin; // advantage marvin
	}
	
	@Override
	public Player getLoser() {
		if(!isGameOver()) 
			return null;
		
		if(human.getProfit() > marvin.getProfit())
			return marvin;	
		return human;
	}

	@Override
	public boolean hasBeenChosen(Question question) {
		return chosenQuestions.contains(question);
	}
	
}

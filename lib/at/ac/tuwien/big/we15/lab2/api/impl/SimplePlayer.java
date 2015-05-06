package at.ac.tuwien.big.we15.lab2.api.impl;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.big.we15.lab2.api.Answer;
import at.ac.tuwien.big.we15.lab2.api.Player;
import at.ac.tuwien.big.we15.lab2.api.Question;
import at.ac.tuwien.big.we15.lab2.api.User;

public class SimplePlayer implements Player {
	private User user;
	private int profit = 0;
	private Integer latestProfitChange;
	private Question chosenQuestion;
	private List<Question> answeredQuestions = new ArrayList<>();
	
	public SimplePlayer() { }
	
	public SimplePlayer(User user) {
		this.user = user;
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.big.we15.lab2.api.impl.Playe#getUser()
	 */
	@Override
	public User getUser() {
		return user;
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.big.we15.lab2.api.impl.Playe#getProfit()
	 */
	@Override
	public int getProfit() {
		return profit;
	}
	
	protected void addProfit(int profit) {
		latestProfitChange = profit;
		this.profit += profit;
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.big.we15.lab2.api.impl.Playe#getLatestProfitChange()
	 */
	@Override
	public Integer getLatestProfitChange() {
		return this.latestProfitChange;
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.big.we15.lab2.api.impl.Playe#chooseQuestion(at.ac.tuwien.big.we15.lab2.api.Question)
	 */
	@Override
	public void chooseQuestion(Question question) {
		this.chosenQuestion = question;
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.big.we15.lab2.api.impl.Playe#getChosenQuestion()
	 */
	@Override
	public Question getChosenQuestion() {
		return this.chosenQuestion;
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.big.we15.lab2.api.impl.Playe#hasAnsweredQuestion()
	 */
	@Override
	public boolean hasAnsweredQuestion() {
		return getChosenQuestion() == null;
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.big.we15.lab2.api.impl.Playe#answerQuestion(java.util.List)
	 */
	@Override
	public void answerQuestion(List<Answer> answers) {
		if(answers.size() == chosenQuestion.getCorrectAnswers().size() &&
			answers.containsAll(chosenQuestion.getCorrectAnswers())) 
			addProfit(chosenQuestion.getValue());
		else
			addProfit(-chosenQuestion.getValue());
		answeredQuestions.add(chosenQuestion);
		chosenQuestion = null;
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.big.we15.lab2.api.impl.Playe#getAnsweredQuestions()
	 */
	@Override
	public List<Question> getAnsweredQuestions() {
		return answeredQuestions;
	}
}

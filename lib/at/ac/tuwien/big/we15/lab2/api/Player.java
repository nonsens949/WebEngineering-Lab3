package at.ac.tuwien.big.we15.lab2.api;

import java.util.List;

public interface Player {

	public User getUser();

	public int getProfit();

	public Integer getLatestProfitChange();

	public void chooseQuestion(Question question);

	public Question getChosenQuestion();

	public boolean hasAnsweredQuestion();

	public void answerQuestion(List<Answer> answers);

	public List<Question> getAnsweredQuestions();

}
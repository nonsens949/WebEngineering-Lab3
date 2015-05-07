package models;

public class GameState {

	private int player1Score;
	private int player2Score;
	private int round;
	private String player1Category;
	private String player2Category;
	private boolean player1Answer;
	private boolean player2Answer;
	private int player1Value;
	private int player2Value;

	public GameState() {
		player1Score = 0;
		player2Score = 0;
		round = 1;
		player1Category = "noch nichts";
		player2Category = "noch nichts";
		player1Answer = true;
		player2Answer = true;
		player1Value = 0;
		player2Value = 0;
	}
	
	public int getPlayer1Score() {
		return player1Score;
	}

	
	public int getPlayer2Score() {
		return player2Score;
	}

	
	public void setPlayer1Score(int score) {
		player1Score = score;
	}

	
	public void setPlayer2Score(int score) {
		player2Score = score;
	}

	
	public int getRound() {
		return round;
	}

	
	public void setRound(int round) {
		this.round = round;
	}
	
	
	public void incrementRound() {
		round++;
	}

	
	public String getPlayer1Category() {
		return player1Category;
	}

	
	public String getPlayer2Category() {
		return player2Category;
	}

	
	public boolean getPlayer1Answer() {
		return player1Answer;
	}

	
	public boolean getPlayer2Answer() {
		return player2Answer;
	}

	
	public int getPlayer1Value() {
		return player1Value;
	}

	
	public int getPlayer2Value() {
		return player2Value;
	}

	
	public void setPlayer1Category(String category) {
		player1Category = category;
	}

	
	public void setPlayer2Category(String category) {
		player2Category = category;
	}

	
	public void setPlayer1Answer(boolean answerCorrect) {
		player1Answer = answerCorrect;
	}

	
	public void setPlayer2Answer(boolean answerCorrect) {
		player2Answer = answerCorrect;
	}

	
	public void setPlayer1Value(int value) {
		player1Value = value;
	}

	
	public void setPlayer2Value(int value) {
		player2Value = value;
	}
	
}

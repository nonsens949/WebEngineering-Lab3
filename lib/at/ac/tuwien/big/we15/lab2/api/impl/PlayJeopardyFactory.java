package at.ac.tuwien.big.we15.lab2.api.impl;

import java.io.InputStream;

import play.Play;
import at.ac.tuwien.big.we15.lab2.api.JeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.QuestionDataProvider;
import at.ac.tuwien.big.we15.lab2.api.impl.JSONQuestionDataProvider;
import at.ac.tuwien.big.we15.lab2.api.impl.SimpleJeopardyFactory;

public class PlayJeopardyFactory extends SimpleJeopardyFactory implements
		JeopardyFactory {

	private String questionDataFilePath;
	
	/**
	 * Creates a new PlayQuizFactory with the specified data input file for the
	 * questions (e.g., "data.de.json" or 
	 * Play.application().configuration().getString("<specified-key-in-conf>")
	 * if the application configuration file is used) and the human user of the
	 * game. 
	 * @param questionDataFilePath file path to question data
	 */
	public PlayJeopardyFactory(String questionDataFilePath) {
		this.questionDataFilePath = questionDataFilePath;
	}

	@Override
	public QuestionDataProvider createQuestionDataProvider() {
		InputStream is = Play.application().resourceAsStream(questionDataFilePath);
		
		return new JSONQuestionDataProvider(is, this);
	}

}

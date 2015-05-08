package models;
import java.util.HashMap;

import at.ac.tuwien.big.we15.lab2.api.*;

public class GameState {
	
	private static HashMap<String, JeopardyGame> gameState;
	
	public static HashMap<String, JeopardyGame> getGameStateMap(){
		if(gameState == null){
			gameState = new HashMap<String, JeopardyGame>();
		}
		return gameState;
	}

}

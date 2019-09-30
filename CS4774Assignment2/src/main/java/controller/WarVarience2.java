package controller;

import model.*;

public class WarVarience2 extends WarVarience1{
	protected int scoreOne, scoreTwo;
	
	public WarVarience2(Player one, Player two, Deck deck) throws Exception {
		super(one, two, deck);
		scoreOne = scoreTwo = 0;
	}
	
	/* scorePoints changed from adding cards back to the winner deck
	 * to actually keeping points earned per each player. */
	protected void scorePoints(Player winner) {
		if (winner.getName() == playerOne.getName()) {
			scoreOne += winnings.size();
		} else if (winner.getName() == playerTwo.getName()) {
			scoreTwo += winnings.size();
		} else {
			logger.error("Player " + winner + " is neither player of the game.");
		}
		winnings.clear();
		printScore();
	}
	
	/* Printing score, method meant to be overridden by subclasses */
	protected void printScore() {
		logger.info("Score is " + playerOne + " " + scoreOne + ", " + 
				playerTwo + " " + scoreTwo);
	}
	
	/* changed to determine winner by points */
	protected void endGame() {
		if (scoreOne > scoreTwo) {
			logger.info(playerOne.getName() + " wins!");
		} else if (scoreOne < scoreTwo) {
			logger.info(playerTwo.getName() + " wins!");
		} else if (scoreOne == scoreTwo) {
			logger.info("Tie game!");
		} else {
			logger.error("Impossible scoring occured while ending game.");
		}
	}
}

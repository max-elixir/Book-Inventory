package controller;

import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.*;

/* This variance of war stores won cards on
 * the bottom of the player's deck, 
 * a maximum number of iterations will 
 * allow us to stop the game. Winner
 * is the player with most cards in the deck.*/
public class WarVarience1 {
	protected Player playerOne, playerTwo;
	protected Deck deckOne;
	protected final int MAX_TURNS = 100;
	protected int counter;
	protected Stack<Card> winnings;
	protected static Logger logger = LogManager.getLogger();
	
	public WarVarience1 (Player one, Player two, Deck deck) throws WarException{
		if( one == null || two == null ) {
			throw new WarException("Not enough players.");
		}
		
		if (deck == null) {
			throw new WarException("Deck must be initialized, cards not needed.");
		}
		
		playerOne = one;
		playerTwo = two;
		if(playerOne.getName() == playerTwo.getName()) {
			throw new WarException("Players can't share the same name: " + playerOne
					+ " and " + playerTwo);
		}
		
		winnings = new Stack<Card>();
		deckOne = deck;
		assignDecks();	
	}
	
	protected void assignDecks() throws WarException {
		Deck deckTwo = deckOne.splitDeck(deckOne.getDeckSize() / 2);
		playerOne.setDeck(deckOne);
		playerTwo.setDeck(deckTwo);
	}

	public void start() throws WarException {
		for(counter = 0; counter < MAX_TURNS; counter++) {
			//System.out.println("* * * Round: " + (counter+1)); // delete, counter doesnt work right with short games
			warRound();
		}
		endGame();
	}
	
	protected void warRound() throws WarException {
		Card oneDraw = null;
		Card twoDraw = null;
		try {
			oneDraw = playerOne.playCard();
			twoDraw = playerTwo.playCard();
		} catch (Exception e) {
			counter = MAX_TURNS;
			return;
		}
		
		winnings.push(oneDraw);
		winnings.push(twoDraw);
		Player winner = getWinner(playerOne, oneDraw, playerTwo, twoDraw);
		if (winner != null)	{
			logger.info(winner + " wins the round.");
			scorePoints(winner);
		} else {
			//throw new WarException("Winner can't be determined");
			//logger.error("No winner for this round.");
			return;
		}
	}
	
	protected Player getWinner(Player first, Card firstCard, Player second, Card secondCard) throws WarException {
		
		try {
			if(firstCard.getRank().compareTo(secondCard.getRank()) > 0 ) {
				return first;
			} else if (firstCard.getRank().compareTo(secondCard.getRank()) < 0){
				return second;
			} else if (firstCard.getRank().compareTo(secondCard.getRank()) == 0) {
				logger.info("War!");
				warRound();
			}
		} catch (NullPointerException e) {
			throw new WarException("Invalid cards cannot be compared.");
		}
		
		return null;
	}
	
	protected void scorePoints(Player winner) {
		winner.takeCards(winnings);
	}
	
	protected void endGame() {
		if (playerOne.getDeckSize() > playerTwo.getDeckSize()) {
			logger.info(playerOne.getName() + " wins!");
		} else if (playerOne.getDeckSize() < playerTwo.getDeckSize()) {
			logger.info(playerTwo.getName() + " wins!");
		} else if (playerOne.getDeckSize() == playerTwo.getDeckSize()) {
			logger.info("Tie game!");
		} else {
			logger.info("Not sure if this can happen");
		}
	}

	public int getCounter() {
		return counter;
		
	}
	
}

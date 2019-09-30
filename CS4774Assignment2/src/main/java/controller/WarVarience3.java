package controller;

import model.*;

/**
 * Three player game of war. Like the other variations implemented,
 * there are no "prize cards" also drawn with the card that is played.
 * 
 * Implementations for this variation is basically the same implementation
 * as WarVarience2 but just including an extra step to for the third player.
 * 
 * I considered making the other variations a little more modular and procedural
 * when handling steps for each player, but that would in-turn make WarVariation1
 * and WarVariation2 their own version of WarVarience3 that just never uses 
 * more than 2 players.
 * 
 * Decided to just leave the other variations as is and just copy-paste old code with 
 * 1 extra to come up with this variation.
 * 
 * @author maxim
 *
 */
public class WarVarience3 extends WarVarience2 {
	protected Player playerThree;
	protected Deck deckThree;
	protected int scoreThree;
	
	public WarVarience3(Player one, Player two, Player three, Deck deck) throws Exception  {
		super(one, two, deck);
		if (three == null) {
			throw new WarException("Not enough players.");
		}
		
		playerThree = three;
		if( (playerThree.getName() == playerTwo.getName()) || (playerThree.getName() == playerOne.getName())) {
			throw new WarException("Some of the players share the same name");
		}
		
		scoreThree = 0;
		playerThree.setDeck(playerTwo.getDeck().splitDeck(playerTwo.getDeckSize() / 2));
	}

	protected void assignDecks() throws WarException {
		int currentSize = deckOne.getDeckSize();
		Deck deckTwo = deckOne.splitDeck(currentSize / 3);
		currentSize = deckTwo.getDeckSize();
		
		playerOne.setDeck(deckOne);
		playerTwo.setDeck(deckTwo);
	}
	
	protected void warRound() throws WarException {
		Card oneDraw = null;
		Card twoDraw = null;
		Card threeDraw = null;
		
		try {
			oneDraw = playerOne.playCard();
			twoDraw = playerTwo.playCard();
			threeDraw = playerThree.playCard();
		} catch (Exception e) {
			counter = MAX_TURNS;
			return;
		}
		
		winnings.push(oneDraw);
		winnings.push(twoDraw);
		winnings.push(threeDraw);
		Player roundWinner = getWinner(playerOne, oneDraw, playerTwo, twoDraw, playerThree, threeDraw);
		
		if (roundWinner != null)	{
			logger.info(roundWinner + " wins the round.");
			scorePoints(roundWinner);
		} else {
			return;
		}
	}
	
	protected Player getWinner(Player first, Card firstCard, Player second, Card secondCard, Player third, Card thirdCard) throws WarException {
		Player winner = null;
		
		if(firstCard.getRank().compareTo(secondCard.getRank()) > 0 ) {
			winner = getWinner(first, firstCard, third, thirdCard);
		} else if (firstCard.getRank().compareTo(secondCard.getRank()) < 0){
			winner = getWinner(second, secondCard, third, thirdCard);
		} else if (firstCard.getRank().compareTo(secondCard.getRank()) == 0) {
			logger.info("War!");
			warRound();
		} else {
			logger.error("Invalid cards cannot be compared.");
		}
		
		return winner;
	}
	
	protected void scorePoints(Player winner) {
		if (winner.getName() == playerOne.getName()) {
			scoreOne += winnings.size();
		} else if (winner.getName() == playerTwo.getName()) {
			scoreTwo += winnings.size();
		} else if (winner.getName() == playerThree.getName()) {
			scoreThree += winnings.size();
		} else {
			logger.error("Player " + winner + " is neither player of the game.");
		}
		winnings.clear();
		printScore();
	}
	
	protected void printScore() {
		logger.info("Score is " + playerOne + " " + scoreOne + ", " + 
				playerTwo + " " + scoreTwo + ", " +
				playerThree + " " + scoreThree + ".");
	}
	
	protected void endGame() {
		if (scoreOne > scoreTwo) {
			if (scoreOne > scoreThree) {
				logger.info(playerOne.getName() + " wins!");
			} else if (scoreOne < scoreThree){
				logger.info(playerThree.getName() + " wins!");
			} else if (scoreOne == scoreThree) {
				logger.info(playerOne.getName() + " ties with " + playerThree.getName() + "!");
			}
		} else if (scoreOne < scoreTwo) {
			if (scoreTwo > scoreThree) {
				logger.info(playerTwo.getName() + " wins!");
			} else if (scoreTwo < scoreThree){
				logger.info(playerThree.getName() + " wins!");
			} else if (scoreTwo == scoreThree) {
				logger.info(playerTwo.getName() + " ties with " + playerThree.getName() + "!");
			}
		} else if (scoreOne == scoreTwo) {
			if (scoreOne > scoreThree) {
				logger.info(playerOne.getName() + " ties with " + playerTwo.getName() + "!");
			} else if (scoreOne < scoreThree) {
				logger.info(playerThree.getName() + " wins!");
			} else if (scoreOne == scoreThree){
				logger.info("THREE-WAY TIE!");
			}
		} else {
			logger.error("Impossible scoring occured while ending game.");
		}
	}
	
}

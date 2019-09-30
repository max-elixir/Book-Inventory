package model;

import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Player {
	private String name;
	private Deck deck;
	private static Logger logger = LogManager.getLogger();
	
	public Player(String name) {
		setName(name);
	}

	public Card playCard() throws WarException {
		if (!deck.isEmpty()) {
			Card play = deck.drawCard();
			logger.info(getName() + " drew " + play);
			return play;
		} else {
			throw new WarException(name + " can't draw any more cards");
		}
		
	}
	
	public void takeCards(Stack<Card> winnings) {
		int currentSize = winnings.size();
		for (int i = 0; i < currentSize; i++)
			deck.scryCard(winnings.pop());
	}
	
	public String getName() {
		return name;
	}
	
	private void setName(String name) {
		this.name = name;
	}

	public Deck getDeck() {
		return deck;
	}

	public void setDeck(Deck splitDeck) {
		deck = splitDeck;
	}
	
	public int getDeckSize() {
		return deck.getDeckSize();
	}
	
	public Stack<Card> getCards(){
		return deck.getDeck();
	}
	
	public String toString() {
		return name;
	}

	
	
}

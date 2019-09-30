package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Deck {
	private Stack<Card> deck;
	private int deckSize;
	private static Logger logger = LogManager.getLogger();
	
	/* Create a new deck of 52 cards */
	public Deck() {		
		try {
			deck = new Stack<Card>();
		} catch (Exception e){
			logger.error(e.getMessage());
		}
		
		for (CardSuit suit: CardSuit.values()) {
			for (CardRank rank: CardRank.values()) {
				deck.push(new Card(rank, suit));
			}
		}
		
		setDeckSize(deck.size());
		shuffleDeck();
	}
	
	/* Take in a stack of cards to make the deck */
	public Deck(Stack<Card> deck) {
		this.deck = deck;
		setDeckSize(deck.size());
	}
	
	/* Returns the top card of the stack */
	public Card drawCard() throws WarException{
		Card drawnCard;
		if (!isEmpty()) {
			drawnCard = deck.pop();
			setDeckSize(deck.size());
		} else {
			throw new WarException("Can't draw a card");
		}
		return drawnCard;
	}
	
	/* Put each card on top of the deck */
	public void placeCard(ArrayList<Card> newCards) {
		for(Card newCard: newCards) {
			deck.push(newCard);
		}
		setDeckSize(deck.size());
	}
	
	/* Put a single card on the bottom of the deck */
	public void scryCard(Card won) {
		deck.add(0, won);
		setDeckSize(deck.size());
	}
	
	/* Deck will be cut down to size, based on the number of cards passed in.
	 * This deck will be that number of cards requested, starting from the bottom
	 * Returns the remaining cards on the top. */
	public Deck splitDeck(int newDeckSize) throws WarException {
		if (newDeckSize > getDeckSize()) {
			throw new WarException("Requested too many cards, "
					+ "currently only have "+getDeckSize());
		}
		
		Stack<Card> topHalf = new Stack<Card>();
		topHalf.addAll(deck.subList(0, newDeckSize));
		
		Stack<Card> bottomHalf = new Stack<Card>();
		bottomHalf.addAll(deck.subList(newDeckSize, deckSize));
		
		deck = topHalf;
		setDeckSize(deck.size());
		
		return new Deck(bottomHalf);
	}
	
	public void shuffleDeck() {
		Collections.shuffle(deck);
	}
	
	public boolean isEmpty() {
		if(getDeckSize() == 0) {
			return true;
		}
		return false;
	}
	
	public Stack<Card> getDeck() {
		return deck;
	}
	
	public int getDeckSize() {
		return deckSize;
	}

	private void setDeckSize(int deckSize) {
		this.deckSize = deckSize;
	}
	
	public String toString() {
		return deck.toString();
	}
}

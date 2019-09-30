package controller;

import static org.junit.Assert.assertEquals;

import java.util.Stack;

import org.junit.BeforeClass;
import org.junit.Test;

import model.*;

public class TestWar {
	static Player testerOne, testerTwo, copy1, copy2;
	
	static WarVarience1 testGame1, testGame2, testGame3;
	
	@BeforeClass
	public static void setUp() throws Exception {
		testerOne = new Player("Urza");
		testerTwo = new Player("Mishra");
		copy1 = new Player("Changeling");
		copy2 = new Player("Changeling");
	}
	
	/**
	 * A chain of wars with no winner doesn't cause
	 * errors, simply a tie if no points are scored.
	 * 
	 * This occurs because once either player (or in this case, both)
	 * is unable to draw, then endGame() is called to find the winner.
	 * */
	@Test 
	public void warTillEnd() throws Exception {
		System.out.println("*** Test One ***");
		
		Stack<Card> firstLooses = new Stack<Card>();
		firstLooses.push(new Card(CardRank.FOUR, CardSuit.CLUBS));
		firstLooses.push(new Card(CardRank.FOUR, CardSuit.CLUBS));
		firstLooses.push(new Card(CardRank.FOUR, CardSuit.CLUBS));
		firstLooses.push(new Card(CardRank.FOUR, CardSuit.DIAMONDS));
		firstLooses.push(new Card(CardRank.FOUR, CardSuit.DIAMONDS));
		firstLooses.push(new Card(CardRank.FOUR, CardSuit.DIAMONDS));
		Deck deck = new Deck(firstLooses);
		
		testGame1 = new WarVarience1(testerOne, testerTwo, deck);
		testGame1.start();
		System.out.println(testGame1.getCounter());
	}

	/**
	 * Conflict occurs with determining winner if names
	 * are exactly the same or the same player is passed in.
	 * @throws Exception
	 */
	@Test (expected = WarException.class)
	public void samePlayer() throws Exception {
		System.out.println("*** Test Two ***");
		
		Deck deck2 = new Deck();
		testGame2 = new WarVarience2(copy1, copy2, deck2);
		testGame2.start();
	}
	
	/**
	 * Throw error if trying to run without a third player 
	 * for WarVarience3
	 * @throws Exception
	 */
	@Test (expected = WarException.class)
	public void notEnoughPlayers() throws Exception {
		System.out.println("*** Test Three ***");
		
		Deck deck3 = new Deck();
		testGame3 = new WarVarience3(testerOne, testerTwo, null, deck3);
	}
	
	/**
	 * Total points scored up among players in a three player game
	 * is always 51, meaning a single card isn't used as a point.
	 * 
	 * Proof the card resides in someone's deck and isn't just missing.
	 */
	@Test 
	public void oneCardLeft() throws Exception {
		System.out.println("*** Test Four ***");
		
		Deck deck4 = new Deck();
		testGame1 = new WarVarience3(testerOne, testerTwo, copy1, deck4);
		testGame1.start();
		assertEquals(1,copy1.getDeckSize());
	}
	
	/**
	 * Card can be unnecessarily initiated with a null rank
	 * and suit. Using card later on in the game will make an error occur
	 * and end the game. 
	 * 
	 * @throws WarException 
	 */
	@Test (expected = WarException.class)
	public void badCards() throws WarException {
		System.out.println("*** Test Five ***");
		
		Card newCard = new Card(null, null);
		Card newCard2 = new Card(null, null);
		Stack<Card> badCards = new Stack<Card>();
		badCards.push(newCard);
		badCards.push(newCard2);
		Deck badDeck = new Deck(badCards);
		
		WarVarience1 badGame = new WarVarience1(testerOne, testerTwo, badDeck);
		badGame.start();
	}
	
	/**
	 * Making players use a deck with one card in it results
	 * in a tie in both versions of the game. 
	 * 
	 * Previously, Varience1 led to the second player winning
	 * by simply having the card in the deck and not having to draw.
	 * 
	 * Error properly thrown to tie game when no one scores or decks are 
	 * evenly decked out.
	 * 
	 * @throws Exception
	 */
	@Test
	public void oneCardDeck( ) throws Exception {
		System.out.println("*** Test Six ***");
		
		Stack<Card> smallStack = new Stack<Card>();
		smallStack.push(new Card(CardRank.ACE, CardSuit.DIAMONDS));
		Deck deck4 = new Deck(smallStack);
		Deck deck5 = deck4;
		
		WarVarience1 shortGame = new WarVarience1(testerOne, testerTwo, deck4);
		WarVarience1 shortGame2 = new WarVarience2(testerOne, testerTwo, deck5);
		shortGame.start();
		shortGame2.start();
		
	}
	
	/**
	 * Ensuring that passing in an empty deck
	 * doesn't cause issues.
	 * 
	 * @throws Exception
	 */
	@Test
	public void noCardDeck() throws Exception {
		System.out.println("*** Test Seven ***");
		
		Stack<Card> smallestStack = new Stack<Card>();
		Deck shortestDeck1 = new Deck(smallestStack);
		Deck shortestDeck2 = shortestDeck1;
		
		WarVarience1 shortestGame1 = new WarVarience1(testerOne, testerTwo, shortestDeck1);
		WarVarience1 shortestGame2 = new WarVarience2(testerOne, testerTwo, shortestDeck2);
		
		shortestGame1.start();
		shortestGame2.start();
	}
	
	/**
	 * Confirmation that WarVarience2 will end in tie if
	 * constant wars occur till the end of the game.
	 * 
	 * No points are scored and attempting to draw a card that isn't there
	 * won't cause issues.
	 * @throws Exception
	 */
	@Test 
	public void warTillEnd2() throws Exception {
		System.out.println("*** Test Eight ***");
		
		Stack<Card> firstLooses = new Stack<Card>();
		firstLooses.push(new Card(CardRank.FOUR, CardSuit.CLUBS));
		firstLooses.push(new Card(CardRank.FOUR, CardSuit.CLUBS));
		firstLooses.push(new Card(CardRank.FOUR, CardSuit.CLUBS));
		firstLooses.push(new Card(CardRank.FOUR, CardSuit.DIAMONDS));
		firstLooses.push(new Card(CardRank.FOUR, CardSuit.DIAMONDS));
		firstLooses.push(new Card(CardRank.FOUR, CardSuit.DIAMONDS));
		Deck deck = new Deck(firstLooses);
		
		testGame1 = new WarVarience2(testerOne, testerTwo, deck);
		testGame1.start();
	}
	
	
	/*
	 * Tossing in a two card deck to three players results in
	 * the game immediately ending, since the first player gets no cards.
	 * 
	 * Warvarience3 extends of warvarience2, testing to make sure the last player
	 * not having a card properly end the game.*/
	@Test 
	public void notEnoughCardsThreePlayer() throws Exception {
		System.out.println("*** Test Nine ***");
		
		Stack<Card> shortStack = new Stack<Card>();
		shortStack.push(new Card(CardRank.FOUR, CardSuit.CLUBS));
		shortStack.push(new Card(CardRank.FOUR, CardSuit.DIAMONDS));
		shortStack.push(new Card(CardRank.FIVE, CardSuit.DIAMONDS));
		Deck deck = new Deck(shortStack);
		
		testGame3 = new WarVarience3(testerOne, testerTwo, copy1, deck);
		copy1.playCard();
		System.out.println("The above card was drawn in test, not in"
				+ "the main game.");
		testGame3.start();
	}
	
	
	/**
	 * Compilation of various tests on deck functionality.
	 * 
	 * Deck can be initialized with an empty Stack but not null.
	 * Attempting to draw without cards gives error, meant to be
	 * handled in WarVarience1
	 * 
	 * Putting cards on the bottom of the deck uses a simple
	 * Stack method to put it in its place.
	 * 
	 * Attempting to "split" deck by its full size results in original
	 * Deck with all its cards and new Deck being empty.
	 * 
	 * @throws Exception
	 */
	@Test 
	public void testingDeck() throws Exception {
		System.out.println("*** Test Ten ***");
		
		Stack<Card> emptyStack = new Stack<Card>();
		Deck testDeck = new Deck(emptyStack);
		try {
			System.out.println("Attempt to draw a card from empty stack: " + testDeck.drawCard());
		} catch (WarException e) {
			System.out.println("Catch block in test 9, error message:");
			System.out.println(e.getMessage());
		}
		
		Card scryCard = new Card(CardRank.FOUR, CardSuit.DIAMONDS);
		testDeck.scryCard(scryCard);
		System.out.println("Card passed in: " + scryCard);
		System.out.println("Attempt to put card on bottom of empty deck: " + testDeck);
		
		System.out.println("Attempt to split deck by its full size: ");
		Card scryCard2 = new Card(CardRank.FIVE, CardSuit.DIAMONDS);
		testDeck.scryCard(scryCard2);
		System.out.println("Original deck before split: "+ testDeck);
		Deck testDeck2 =  testDeck.splitDeck(testDeck.getDeckSize());
		System.out.println("Original deck: "+ testDeck);
		System.out.println("New deck: " + testDeck2);
	}
}

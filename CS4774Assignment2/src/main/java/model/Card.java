package model;

public class Card {
	private CardSuit suit;
	private CardRank rank;

	public Card(CardRank rank, CardSuit suit)
	{
		setSuit(suit);
		setRank(rank);
	}

	@Override
	public String toString() {
		return rank + " of " + suit;
	}
	
	public CardSuit getSuit() {
		return suit;
	}

	private void setSuit(CardSuit suit) {
		this.suit = suit;
	}

	public CardRank getRank() {
		return rank;
	}

	private void setRank(CardRank rank) {
		this.rank = rank;
	}
}

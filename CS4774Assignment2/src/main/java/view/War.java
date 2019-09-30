package view;

import controller.*;
import model.*;

/*
 * "Driver" class to run three different variations of 
 * the card game "War"
 */
public class War {

	public static void main(String[] args) throws Exception {
		Player one = new Player("Liliana");
		Player two = new Player("Bolas");
		Player three = new Player("Nahiri");
		Player four = new Player("Sorrin");
		Player five = new Player("Dovin");
		Player six = new Player("Chandra");
		Player seven = new Player("Lazav");
		
		Deck deck1 = new Deck();
		Deck deck2 = new Deck();
		Deck deck3 = new Deck();
		
		WarVarience1 game1 = new WarVarience1(one, two, deck1);
		WarVarience2 game2 = new WarVarience2(three, four, deck2);
		WarVarience3 game3 = new WarVarience3(five, six, seven, deck3);
		
		game1.start();
		game2.start();
		game3.start();
		
	}

}

package solitaire;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Deck {

	private final int DECK_SIZE = 52;
	private Card[] cards = new Card[DECK_SIZE];
	private Vector<Integer> order = new Vector<Integer>(DECK_SIZE);
	private int current = 0;

	private void initialize_deck(final String file_name) {
		try {
			Scanner in = new Scanner(new File(file_name));
			// Read in cards from deck file
			for (int i = 0; i < DECK_SIZE; i++) {
				Rank rank_in = parse_rank(in.next());
				Suit suit_in = parse_suit(in.next());
				cards[i] = new Card(rank_in, suit_in);
				order.addElement(i);
			}
			in.close();
		} catch (FileNotFoundException ex) {
			System.out.printf("Unable to open file %s.txt\n", file_name);
			System.exit(1);
		} catch (NoSuchElementException ex) {
			System.out.printf("Not enough cards in %s.txt\n", file_name);
			System.exit(1);
		}
	}

	private Suit parse_suit(final String suit_in) {
		switch (suit_in) {
		case "Spades":
			return Suit.SPADES;
		case "Hearts":
			return Suit.HEARTS;
		case "Diamonds":
			return Suit.DIAMONDS;
		case "Clubs":
			return Suit.CLUBS;
		default: {
			System.err.printf("Error: Invalid Suit '%s'. EXIT 1", suit_in);
			System.exit(1);
		}
		}
		return null;
	}

	private Rank parse_rank(final String rank_in) {
		switch (rank_in) {
		case "Ace":
			return Rank.ACE;
		case "Two":
			return Rank.TWO;
		case "Three":
			return Rank.THREE;
		case "Four":
			return Rank.FOUR;
		case "Five":
			return Rank.FIVE;
		case "Six":
			return Rank.SIX;
		case "Seven":
			return Rank.SEVEN;
		case "Eight":
			return Rank.EIGHT;
		case "Nine":
			return Rank.NINE;
		case "Ten":
			return Rank.TEN;
		case "Jack":
			return Rank.JACK;
		case "Queen":
			return Rank.QUEEN;
		case "King":
			return Rank.KING;
		default:
			System.err.printf("Error: Invalid Rank '%s'. EXIT 1", rank_in);
			System.exit(1);
		}
		return null;
	}

	public Deck(final String file_name) {
		initialize_deck(file_name);
		shuffle();
	}

	public void shuffle() {
		Collections.shuffle(order);
	}

	public Card top() {
		return cards[order.get(current)];
	}

	public Card deal_one() {
		Card top = this.top();
		++current;
		return top;
	}

}

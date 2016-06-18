/*
 * Created June 13, 2016
 * Author: Alexander Zhu
 * Coauthor: Jason Zhu
 */

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Deck {

	public static final String DECK_NAME = "deck";
	private final int DECK_SIZE = 52;
	private Card[] cards = new Card[DECK_SIZE];
	private Vector<Integer> order = new Vector<Integer>(DECK_SIZE);
	private Vector<Boolean> used = new Vector<Boolean>(DECK_SIZE);
	private int current = 0; // 0 is top of deck. Current should never be a used card
	
	// TODO: update used vector for all actions. or remove card from deck instead

	// MODIFIES this
	// EFFECTS populates cards and order
	private void initialize_deck(final String file_name) {
		try {
			Scanner in = new Scanner(new File(file_name));
			// Read in cards from deck file
			for (int i = 0; i < DECK_SIZE; i++) {
				Rank rank_in = parse_rank(in.next());
				Suit suit_in = parse_suit(in.next());
				cards[i] = new Card(rank_in, suit_in);
				order.addElement(i);
				used.addElement(false);
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

	// EFFECTS returns Suit based on suit_in
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

	// EFFECTS returns Rank based on rank_in
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

	// MODIFIES this
	// EFFECTS Load specific deck and shuffle
	public Deck(final String file_name) {
		this(file_name, true);
	}
	
	// MODIFIES this
	// EFFECTS Load specific deck, shuffle=false for debugging
	public Deck(final String file_name, boolean shuffle) {
		initialize_deck(file_name);
		// No shuffle option for debugging purposes
		if (shuffle) {
			shuffle();
		}
	}

	// MODIFIES order
	// EFFECTS shuffles deck by shuffling order
	public void shuffle() {
		Collections.shuffle(order);
	}

	// REQUIRES this is not empty
	// EFFECTS returns top card
	public Card top() {
		assert(!empty());
		return cards[order.get(current)];
	}

	// REQUIRES this is not empty
	// EFFECTS deals top card
	public Card deal_one() {
		if (empty()) {
			System.out.println("deal_one(): Deck is empty");
		}
		Card top = this.top();
		// Keep iterating current until reach unused card
		while(++current < DECK_SIZE && used.get(order.get(current))) {}
		if (current == DECK_SIZE) {
			System.out.println("deal_one(): Reached end of deck"); // Temporary
			// FIXME: What to do when reached end of deck?
			//reset();
		}
		return top;
	}

	// EFFECTS returns true if deck is empty
	public boolean empty() {
		return current >= DECK_SIZE;
	}
	
	public void set_used(Card card) {
		used.set(card.get_deck_index(), true);
	}
	
	// MODIFIES this
	// EFFECTS Resets deck. Maintains order and used
	public void reset() {
		current = 0;
	}

}
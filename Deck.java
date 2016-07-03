/*
 * Created June 13, 2016
 * Author: Alexander Zhu
 * Coauthor: Jason Zhu
 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Deck {

	public static final String DECK_NAME = "deck";
	private final int DECK_SIZE = 52;
	// Top of deck is cards.size() - 1
	private List<Card> cards = new ArrayList<Card>(DECK_SIZE);
	// Holds cards that have been dealt but not used
	protected List<Card> dealt = new ArrayList<Card>(DECK_SIZE);

	// MODIFIES this
	// EFFECTS populates cards and order
	private void initialize_deck(final String file_name) {
		try {
			Scanner in = new Scanner(new File(file_name));
			// Read in cards from deck file
			for (int i = 0; i < DECK_SIZE; i++) {
				try {
					cards.add(new Card(in.next()));
				} catch (InvalidCardException e) {
					System.err.println("Error: " + e.getMessage());
				}
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
		Collections.shuffle(cards);
	}

	// REQUIRES deck is not empty
	// EFFECTS Returns top card
	public Card top() {
		return cards.get(cards.size() - 1);
	}
	
	public void add_to_dealt(ArrayDeque<Card> card_queue) {
		card_queue.addAll(dealt);
		dealt.clear();
		dealt.addAll(card_queue);
	}

	// REQUIRES deck is not empty
	// EFFECTS Deals top card
	public Card deal_one() {
		if (cards.isEmpty()) {
			//TODO: Make this and retrieve_one an exception instead?
			System.out.println("deal_one(): Deck is empty");
		}
		Card top_card = top();
		cards.remove(cards.size() - 1);
		return top_card;
	}
	
	public Card retrieve_one() {
		if (dealt.isEmpty()) {
			System.out.println("retrieve_one(); Dealt is empty");
		}
		Card last = dealt.remove(0);
		return last;
	}
	
	// EFFECTS Returns True if deck is empty
	public boolean cardsIsEmpty() {
		return cards.isEmpty();
	}
	
	// EFFECTS Returns True if dealt is empty
	public boolean dealtIsEmpty() {
		return dealt.isEmpty();
	}
	
	// MODIFIES this
	// EFFECTS Resets deck. Maintains order and used
	public void reset() {
		cards.addAll(dealt);
		dealt.clear();
	}

	// EFFECTS Returns number of cards remaining in deck
	public int remaining() {
		return cards.size();
	}

}
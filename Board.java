/*
 * Created June 13, 2016
 * Author: Alexander Zhu
 * Coauthor: Jason Zhu
 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.ArrayList;

public class Board implements Use_cases {
	// TODO: change assert statements to throw exceptions instead. Driver catches.
	private final int NUM_COLS = 7;
	private final int NUM_FOUNDATIONS = 4;
	private final int NUM_IN_QUEUE = 3;

	// A column in the game board
	private class Column {
		public Column() {
			column = new ArrayDeque<Card>();
			pile = new Stack<Card>();
		}
		
		public Stack<Card> pile;
		public ArrayDeque<Card> column;
		public String top_color = null;
	}

	private ArrayList<Column> tableau; // .column = face up, .pile = face down
	private ArrayList<Stack<Card>> foundations; // 4 stacks, starting with Ace
	protected Deck deck; // Protected for debug purposes. The face down deck
	protected ArrayDeque<Card> card_queue; // addLast and removeFirst. 3 cards facing up

	// REQUIRES: col is [0, NUM_COLS - 1]
	// EFFECTS: Checks whether can place card in col
	private boolean valid_col_move(final int col, final Card card) {
		// Assert REQUIRES statement
		assert (col >= 0 && col < NUM_COLS);

		Column this_column = tableau.get(col);
		// Column is empty
		if (this_column.column.isEmpty())
			return true;
		// If top_color is "Red"
		boolean correct_color = card.get_color() == Card.RED;
		if (this_column.top_color == Card.RED) {
			correct_color = !correct_color;
		}
		// True if card is correct color and rank
		boolean correct_rank = card.get_rank_num() ==
				peek_col_card(col).get_rank_num() - 1;
		return correct_color && correct_rank;
	}
	
	// REQUIRES: pile_num is [0, NUM_FOUNDATIONS - 1]
	// EFFECTS: Checks whether can place card in foundations
	private boolean valid_foundation_move(final int pile_num, final Card card) {
		Stack<Card> this_pile = foundations.get(pile_num);
		if (this_pile.isEmpty()) {
			// True if card is an Ace
			return card.get_rank() == Rank.ACE;
		}
		else {
			// True if card is same suit and one rank higher
			boolean correct_suit = card.get_suit() == peek_foundation_card(pile_num).get_suit();
			boolean correct_rank = card.get_rank_num() == this_pile.peek().get_rank_num() + 1;
			return correct_suit && correct_rank;
		}
	}

	// REQUIRES col is [0, NUM_COLS - 1]
	// MODIFIES tableau
	// EFFECTS Adds card to selected column
	private void col_push_card(final int col, final Card card) {
		Column this_col = tableau.get(col);
		this_col.column.push(card);
		this_col.top_color = card.get_color();
	}
	
	// REQUIRES foundation_num is [0, NUM_FOUNDATIONS - 1]
	// MODIFIES foundations
	// EFFECTS Adds card to selected foundation
	private void foundation_push_card(final int foundation_num, final Card card) {
		Stack<Card> this_foundation = foundations.get(foundation_num);
		this_foundation.push(card);
	}
	
	// REQUIRES pile_num is [0, NUM_FOUNDATIONS - 1]
	// MODIFIES tableau
	// EFFECTS Adds card to selected pile. Use for dealing.
	private void pile_push_card(final int pile_num, final Card card) {
		Stack<Card> this_pile = tableau.get(pile_num).pile;
		this_pile.push(card);
	}

	// REQUIRES col is [0, NUM_COLS - 1].
	//			col is not empty
	// MODIFIES tableau
	// EFFECTS Removes card from selected column. Updates color.
	private void col_pop_card(final int col) {
		Column this_col = tableau.get(col);
		this_col.column.pop();
		if (!this_col.column.isEmpty())
			this_col.top_color = this_col.column.peek().get_color();
		else
			this_col.top_color = null;
	}

	// MODIFIES tableau, deck
	// EFFECTS Deals cards from deck to tableau
	private void deal_new_game() {
		// Face up for current column, then face down for rest of columns
		// Column 0 should have 1 card, column 6 should have 7 cards
		for (int i = 0; i < NUM_COLS; ++i) {
			col_push_card(i, deck.deal_one());
			for (int j = i + 1; j < NUM_COLS; ++j) {
				pile_push_card(j, deck.deal_one());
			}
		}
	}
	
	// MODIFIES: this
	// EFFECTS: Initializes board, shuffle=empty=false for debug
	public Board(boolean shuffle, boolean deal) {
		tableau = new ArrayList<Column>();
		foundations = new ArrayList<Stack<Card>>();
		deck = new Deck(Deck.DECK_NAME, shuffle);
		card_queue = new ArrayDeque<Card>();
		
		// Add empty Columns
		for (int i = 0; i < NUM_COLS; ++i) {
			tableau.add(new Column());
		}
		// Add empty Piles
		for (int j = 0; j < NUM_FOUNDATIONS; ++j) {
			foundations.add(new Stack<Card>());
		}
		if (deal)
			deal_new_game();
	}
	
	// MODIFIES: this
	// EFFECTS: Initialize generic board
	public Board() {
		this(true, true);
	}
	
	// REQUIRES: col is [0, NUM_COLS - 1].
	// EFFECTS: Returns bottom card in selected column
	//			Returns null if empty
	public Card peek_col_card(final int col) {
		if (tableau.get(col).column.isEmpty())
			return null;
		return tableau.get(col).column.peek();
	}
	
	// REQUIRES: foundation is [0, NUM_FOUNDATIONS - 1].
	// EFFECTS: Returns card on top of selected foundation
	//			Returns null if empty
	public Card peek_foundation_card(final int foundation_num) {
		if (foundations.get(foundation_num).isEmpty())
			return null;
		return foundations.get(foundation_num).peek();
	}

	// EFFECTS: Returns card at front of card_queue
	//			Returns null if empty
	public Card peek_queue_card() {
		if (card_queue.isEmpty())
			return null;
		return card_queue.getLast();
	}
	
	// REQUIRES: col is [0, NUM_COLS - 1], queue is not empty
	// MODIFIES: columns, card_queue
	// EFFECTS: Places card in column if valid
	//			Returns false if invalid
	public boolean deck_to_col(final int col, final Card card) {
		// Assert REQUIRES statement
		assert (col >= 0 && col < NUM_COLS);
		assert (!card_queue.isEmpty());

		if (valid_col_move(col, card)) {
			col_push_card(col, card); // Add card to column
			card_queue.removeLast(); // Remove card from queue
			return true;
		}
		return false; // Invalid move
	}
	
	// REQUIRES: foundation_num is [0, NUM_FOUNDATIONS - 1], queue is not empty
	// MODIFIES: foundations, card_queue
	// EFFECTS: Places card on foundations if valid
	//			Returns false if invalid
	public boolean deck_to_foundation(final int foundation_num, final Card card) {
		// Assert REQUIRES statement
		assert (foundation_num >= 0 && foundation_num < NUM_FOUNDATIONS);
		assert (!card_queue.isEmpty());

		if (valid_foundation_move(foundation_num, card)) {
			foundation_push_card(foundation_num, card); // Add card to foundations
			card_queue.removeLast(); // Remove card from queue
			return true;
		}
		return false; // Invalid move
	}

	// REQUIRES: old_col and new_col are [0, NUM_COLS - 1].
	//			 old_col is not empty
	// MODIFIES: columns
	// EFFECTS: Moves card from one column to the other if valid
	//			Returns false if invalid move
	public boolean col_to_col(final int old_col, final int new_col) {
		// Assert REQUIRES statement
		assert (old_col >= 0 && old_col < NUM_COLS);
		assert (new_col >= 0 && new_col < NUM_COLS);
		assert (!tableau.get(old_col).column.isEmpty());
		
		// Remove card from old_col
		Card top_card = peek_col_card(old_col);
		col_pop_card(old_col);
		
		// Attempt to add to new_col
		if (valid_col_move(new_col, top_card)) {
			col_push_card(new_col, top_card);
			//col_pop_card(old_col);
			return true;
		}
		// Add card back to old_col if rejected
		col_push_card(old_col, top_card);
		return false; // Invalid move
	}

	// REQUIRES: col is [0, NUM_COLS - 1]
	//			 foundation_num is [0, NUM_FOUNDATIONS - 1]
	//			 col is not empty
	// MODIFIES: columns, foundations
	// EFFECTS: Moves card from column to foundations if valid
	//			Returns false if invalid move
	public boolean col_to_foundation(final int col, final int foundation_num) {
		assert (col >= 0 && col < NUM_COLS);
		assert (foundation_num >= 0 && foundation_num < NUM_FOUNDATIONS);
		assert (!tableau.get(col).column.isEmpty());
		
		Card top_card = peek_col_card(col);
		if (valid_foundation_move(foundation_num, top_card)) {
			col_pop_card(col);
			foundation_push_card(foundation_num, top_card);
			return true;
		}
		return false;
	}
	
	// REQUIRES: col is [0, NUM_COLS - 1]
	//			 flip_pile[col] is not empty
	//			 col is empty
	// MODIFIES: tableau
	public boolean flip_to_col(final int col) {
		assert(col >= 0 && col < NUM_COLS);
		Column this_col = tableau.get(col);
		assert(!this_col.pile.isEmpty());
		assert(this_col.column.isEmpty());
		Card top_card = this_col.pile.peek();
		if (valid_col_move(col, top_card)) {
			this_col.pile.pop();
			col_push_card(col, top_card);
			return true;
		}
		return false;
	}
	
	// MODIFIES: deck, card_queue
	// EFFECTS: Adds next three cards to next_cards
	public void get_next_three_cards() {
		deck.add_to_dealt(card_queue);
		card_queue.clear();
		while (!deck.empty() && card_queue.size() != NUM_IN_QUEUE) {
			card_queue.addFirst(deck.deal_one());
		}
	}
	
	// MODIFIES: deck, card_queue
	// EFFECTS: Resets deck when player reaches bottom of deck
	public void reset_deck() {
		deck.add_to_dealt(card_queue);
		card_queue.clear();
		deck.reset();
	}
	
	// EFFECTS: Prints out contents of card_queue
	public void print_card_queue() {
		for(Iterator<Card> itr = card_queue.iterator(); itr.hasNext();) {
			System.out.print(itr.next() + " ");
		}
		System.out.println();
	}
	
	// EFFECTS: Prints out cards in piles
	public void print_piles() {
		for (int i = 0; i < NUM_COLS; ++i) {
			System.out.print(" " + tableau.get(i).pile.size() + "| ");
		}
		System.out.println();
	}
	
	// EFFECTS: Prints out contents of columns
	public void print_columns() {
		// Find len of longest column
		int max = 0;
		for (int i = 0; i < NUM_COLS; ++i) {
			int len = tableau.get(i).column.size();
			if (len > max) {
				max = len;
			}
		}
		
		//create ArrayList of Iterators to reference in double for loop
		ArrayList<Iterator<Card>> itr = new ArrayList<Iterator<Card>>();
		for (int j = 0; j < NUM_COLS; ++j) {
			itr.add(tableau.get(j).column.iterator());
		}
		
		// Print cards in columns, skipping if null
		for (int k = 0; k < max;  ++k) { // Iterate vertically
			for (int l = 0; l < NUM_COLS; ++l) { // Iterate horizontally
				if (itr.get(l).hasNext()) {
					Card current = itr.get(l).next();
					//String ranksym = current.get_rank_symbol();
					//String suitletter = current.suitLetter();
					//System.out.printf(" %s%s ", ranksym, suitletter);
					System.out.print(current);
				}
				else {
					continue;
				}
			}	
		}
		System.out.println();
	}
	
	// EFFECTS: Prints out top of each foundation
	public void print_foundations() {
		for(int i = 0; i < NUM_FOUNDATIONS; ++i) {
			Card current = peek_foundation_card(i);
			//String ranksym = current.get_rank_symbol();
			//String suitletter = current.suitLetter();
			//System.out.printf(" %s%s ", ranksym, suitletter);
			if (current == null)
				System.out.print(" -- ");
			else
				System.out.print(current);
		}
		System.out.println();
	}
	
	// EFFECTS: Prints out number of cards left in deck
	public void print_deck_size() {
		int size = deck.remaining();
		System.out.println(size);
	}
	
	// EFFECTS: Prints board layout
	public void print_board() {
		System.out.println("Initializing print_board");
		print_deck_size();
		System.out.print("Card queue: ");
		print_card_queue();
		System.out.print("Foundations: ");
		print_foundations();
		System.out.println("Tableau: ");
		print_piles();
		print_columns();
	}
}
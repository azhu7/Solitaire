/*
 * Created June 13, 2016
 * Author: Alexander Zhu
 * Coauthor: Jason Zhu
 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class Board {
	// TODO: change assert statements to throw exceptions instead. Driver catches.
	// TODO: implement flip_piles...oops
	private final int NUM_COLS = 7;
	private final int NUM_PILES = 4;
	private final int NUM_CARDS_IN_QUEUE = 3;

	// A column in the game board
	private class Column {
		public Column() {
			column = new ArrayDeque<Card>();
		}

		public ArrayDeque<Card> column;
		public String top_color = null;
	}

	// A pile in the game board
	private class Pile {
		public Pile() {
			pile = new Stack<Card>();
		}

		public Stack<Card> pile;
	}

	private ArrayList<Column> columns;
	private ArrayList<Pile> finish_piles;
	private Stack<Card> flip_piles;
	protected Deck deck; // Protected for debug purposes
	protected ArrayDeque<Card> card_queue; // addLast and removeFirst

	// REQUIRES: col is [0, NUM_COLS - 1]
	// EFFECTS: Checks whether can place card in col
	private boolean valid_col_move(final int col, final Card card) {
		// Assert REQUIRES statement
		assert (col >= 0 && col < NUM_COLS);

		Column this_column = columns.get(col);
		// Column is empty
		if (this_column.column.isEmpty())
			return true;
		// If top_color is "Red"
		boolean correct_color = card.get_suit() == Suit.SPADES ||
				card.get_suit() == Suit.CLUBS;
		if (this_column.top_color == "Black") {
			correct_color = !correct_color;
		}
		// True if card is correct color and rank
		boolean correct_rank = card.get_rank_num() ==
				peek_col_card(col).get_rank_num() - 1;
		return correct_color && correct_rank;
	}
	
	// REQUIRES: pile_num is [0, NUM_PILES - 1]
	// EFFECTS: Checks whether can place card in finish_piles
	private boolean valid_pile_move(final int pile_num, final Card card) {
		Stack<Card> this_pile = finish_piles.get(pile_num).pile;
		if (this_pile.isEmpty()) {
			// True if card is an Ace
			return card.get_rank() == Rank.ACE;
		}
		else {
			// True if card is same suit and one rank higher
			boolean correct_suit = card.get_suit() == peek_finish_pile_card(pile_num).get_suit();
			boolean correct_rank = card.get_rank_num() == this_pile.peek().get_rank_num() + 1;
			return correct_suit && correct_rank;
		}
	}

	// REQUIRES col is [0, NUM_COLS - 1]
	// MODIFIES columns
	// EFFECTS adds card to selected column
	private void col_push_card(final int col, final Card card) {
		Column this_col = columns.get(col);
		this_col.column.push(card);
		this_col.top_color = card.get_color();
	}
	
	// REQUIRES pile_num is [0, NUM_PILES - 1]
	// MODIFIES finish_piles
	// EFFECTS adds card to selected finish_pile
	private void pile_push_card(final int pile_num, final Card card) {
		Pile this_pile = finish_piles.get(pile_num);
		this_pile.pile.push(card);
	}

	// REQUIRES col is [0, NUM_COLS - 1].
	//			col is not empty
	// MODIFIES columns
	// EFFECTS removes card from selected column
	private void pop_card(final int col) {
		Column this_col = columns.get(col);
		this_col.column.pop();
		if (!this_col.column.isEmpty())
			this_col.top_color = this_col.column.peek().get_color();
		else
			this_col.top_color = null;
	}

	// MODIFIES: this
	// EFFECTS: Initializes board, shuffle=false for debug
	public Board(boolean shuffle) {
		columns = new ArrayList<Column>();
		finish_piles = new ArrayList<Pile>();
		// Add empty Columns
		for (int i = 0; i < NUM_COLS; ++i) {
			columns.add(new Column());
		}
		// Add empty Piles
		for (int j = 0; j < NUM_PILES; ++j) {
			finish_piles.add(new Pile());
		}
		deck = new Deck(Deck.deck_name, shuffle);
		card_queue = new ArrayDeque<Card>();
		// TODO: Add flip Piles
	}
	
	// MODIFIES: this
	// EFFECTS: Initialize generic board
	public Board() {
		this(false);
	}
	
	// REQUIRES: col is [0, NUM_COLS - 1].
	//			 col is not empty
	// EFFECTS: returns bottom card in selected column
	public Card peek_col_card(final int col) {
		return columns.get(col).column.peek();
	}
	
	// REQUIRES: pile is [0, NUM_PILES - 1].
	//			 finish_piles is not empty
	// EFFECTS: returns card on top of selected pile
	public Card peek_finish_pile_card(final int pile_num) {
		return finish_piles.get(pile_num).pile.peek();
	}

	// REQUIRES: card_queue is not empty
	// EFFECTS: returns card at front of card_queue
	public Card peek_queue_card() {
		return card_queue.peek();
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
			card_queue.removeFirst(); // Remove card from queue
			deck.set_used(card); // Set card as used
			return true;
		}
		return false; // Invalid move
	}
	
	// REQUIRES: pile_num is [0, NUM_PILES - 1], queue is not empty
	// MODIFIES: finish_piles, card_queue
	// EFFECTS: Places card on finish_piles if valid
	//			Returns false if invalid
	public boolean deck_to_pile(final int pile_num, final Card card) {
		// Assert REQUIRES statement
		assert (pile_num >= 0 && pile_num < NUM_PILES);
		assert (!card_queue.isEmpty());

		if (valid_pile_move(pile_num, card)) {
			pile_push_card(pile_num, card); // Add card to finish_piles
			card_queue.removeFirst(); // Remove card from queue
			deck.set_used(card); // Set card as used
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
		
		Card top_card = peek_col_card(old_col);
		if (valid_col_move(new_col, top_card)) {
			col_push_card(new_col, top_card);
			pop_card(old_col);
			return true;
		}
		return false; // Invalid move
	}

	// REQUIRES: col is [0, NUM_COLS - 1]
	//			 pile_num is [0, NUM_PILES - 1]
	// MODIFIES: columns, piles
	// EFFECTS: Moves card from column to finish_piles if valid
	//			Returns false if invalid move
	public boolean col_to_pile(final int col, final int pile_num) {
		assert (col >= 0 && col < NUM_COLS);
		assert (pile_num >= 0 && pile_num < NUM_PILES);
		
		Card top_card = peek_col_card(col);
		if (valid_pile_move(pile_num, top_card)) {
			pop_card(col);
			pile_push_card(pile_num, top_card);
			return true;
		}
		return false;
	}
	
	// MODIFIES: deck, card_queue
	// EFFECTS: Adds next three cards to next_cards
	public void get_next_three_cards() {
		card_queue.clear();
		while (!deck.empty() && card_queue.size() != 3) {
			card_queue.addLast(deck.deal_one());
		}
	}
	
	// MODIFIES: deck, card_queue
	// EFFECTS: Resets deck when player reaches bottom of deck
	public void reset_deck() {
		card_queue.clear();
		deck.reset();
	}
	
	// EFFECTS: Prints out contents of card_queue
	public void print_card_queue() {
		String out = "";
		for(Iterator itr = card_queue.iterator(); itr.hasNext();) {
			out = itr.next() + " " + out; // Print in reverse order
		}
		System.out.println(out);
	}
	
	// EFFECTS: Prints out contents of columns
	public void print_columns() {
		
	}
	
	// EFFECTS: Prints out top of each pile
	public void print_piles() {
		
	}
	
	// EFFECTS: Prints board layout
	public void print_board() {
		// TODO
	}

}
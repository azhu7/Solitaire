/*
 * Created June 13, 2016
 * Author: Alexander Zhu
 * Coauthor: Jason Zhu
 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;

public class Board {
	// TODO: Change assert statements to throw exceptions instead. Driver catches.
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
	private ArrayList<Pile> piles;
	private Deck deck;
	private ArrayDeque<Card> card_queue;

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
	// EFFECTS: Checks whether can place card in pile
	private boolean valid_pile_move(final int pile_num, final Card card) {
		Stack<Card> this_pile = piles.get(pile_num).pile;
		if (this_pile.isEmpty()) {
			// True if card is an Ace
			return card.get_rank() == Rank.ACE;
		}
		else {
			// True if card is same suit and one rank higher
			boolean correct_suit = card.get_suit() == peek_pile_card(pile_num).get_suit();
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
	// MODIFIES piles
	// EFFECTS adds card to selected column
	private void pile_push_card(final int pile_num, final Card card) {
		Pile this_pile = piles.get(pile_num);
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
	// EFFECTS: Initializes board
	public Board() {
		columns = new ArrayList<Column>();
		piles = new ArrayList<Pile>();
		// Add empty Columns
		for (int i = 0; i < NUM_COLS; ++i) {
			columns.add(new Column());
		}
		// Add empty Piles
		for (int j = 0; j < NUM_PILES; ++j) {
			piles.add(new Pile());
		}
		deck = new Deck(Deck.deck_name);
		card_queue = new ArrayDeque<Card>();
	}
	
	// REQUIRES: col is [0, NUM_COLS - 1].
	//			 col is not empty
	// EFFECTS: returns bottom card in selected column
	public Card peek_col_card(final int col) {
		return columns.get(col).column.peek();
	}
	
	// REQUIRES: pile is [0, NUM_PILES - 1].
	//			 pile is not empty
	// EFFECTS: returns card on top of selected pile
	public Card peek_pile_card(final int pile_num) {
		return piles.get(pile_num).pile.peek();
	}

	// REQUIRES: col is [0, NUM_COLS - 1]
	// MODIFIES: columns, next_cards
	// EFFECTS: Places card in column if valid
	//			Returns false if invalid
	public boolean deck_to_col(final int col, final Card card) {
		// Assert REQUIRES statement
		assert (col >= 0 && col < NUM_COLS);

		if (valid_col_move(col, card)) {
			col_push_card(col, card);
			// TODO: Update deck as well
			return true;
		}
		return false; // Invalid move
	}
	
	// REQUIRES: pile_num is [0, NUM_PILES - 1]
	// MODIFIES: piles, next_cards
	// EFFECTS: Places card on pile if valid
	//			Returns false if invalid
	public boolean deck_to_pile(final int pile_num, final Card card) {
		// Assert REQUIRES statement
		assert (pile_num >= 0 && pile_num < NUM_PILES);

		if (valid_pile_move(pile_num, card)) {
			pile_push_card(pile_num, card);
			// TODO: Update deck as well
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
	// EFFECTS: Moves card from column to pile if valid
	//			Returns false if invalid move
	public boolean col_to_pile(final int col, final int pile_num) {
		assert (col >= 0 && col < NUM_COLS);
		assert (pile_num >= 0 && pile_num < NUM_PILES);
		
		Card top_card = peek_col_card(col);
		if (valid_pile_move(pile_num, top_card)) {
			pop_card(col);
			pile_push_card(pile_num, top_card);
		}
		return false;
	}
	
	// MODIFIES: deck, next_three
	// EFFECTS: Adds next three cards to next_cards
	public void next_three_cards() {
		card_queue.clear();
		while (!deck.empty() && card_queue.size() != 3) {
			card_queue.add(deck.deal_one());
		}
	}
	
	public void reset_deck() {
		card_queue.clear();
		deck.reset();
	}
	
	// EFFECTS: Prints board layout
	public void print_board() {
		// TODO
	}
}
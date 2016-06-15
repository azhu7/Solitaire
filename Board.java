package solitaire;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;

public class Board {
	// TODO: change assert statements to throw exceptions instead
	private final int NUM_COLS = 7;
	private final int NUM_PILES = 4;

	// A column in the game board
	private class Column {
		public Column() {
			column = new ArrayDeque<Card>();
		}

		public ArrayDeque<Card> column;
		public String top_color;
	}

	// A pile in the game board
	private class Pile {
		public Pile() {
			pile = new Stack<Card>();
		}

		public Stack<Card> pile;
		public Suit suit;
	}

	private ArrayList<Column> board;
	private ArrayList<Pile> piles;
	private Deck deck;
	private ArrayDeque<Card> next_cards;

	// REQUIRES: col is [0, NUM_COLS - 1]
	// EFFECTS: Checks whether move is valid
	private boolean valid_col_move(final int col, final Card card) {
		// Assert REQUIRES statement
		assert (col > 0 && col < NUM_COLS - 1);

		Column this_column = board.get(col);
		// Column is empty
		if (this_column.column.isEmpty())
			return true;
		// If top_color is "Red"
		boolean correct_color = card.get_suit() == Suit.SPADES ||
				card.get_suit() == Suit.CLUBS;
		if (this_column.top_color == "Black") {
			correct_color = !correct_color;
		}
		// Card is correct color and rank
		return (correct_color && card.get_rank_num() ==
				this_column.column.peek().get_rank_num() - 1);
	}
	
	private boolean valid_pile_move(final int pile_num, final Card card) {
		// TODO
		// If empty, check if Ace
		// Else, check top card of selected pile
		return false;
	}

	// REQUIRES col is [0, NUM_COLS - 1]
	// MODIFIES this
	// EFFECTS adds card to selected column
	private void col_push_card(final int col, final Card card) {
		Column this_col = board.get(col);
		this_col.column.push(card);
		this_col.top_color = card.get_color();
	}
	
	// REQUIRES pile_num is [0, NUM_PILES - 1]
	// MODIFIES this
	// EFFECTS adds card to selected column
	private void pile_push_card(final int pile_num, final Card card) {
		Pile this_pile = piles.get(pile_num);
		if (this_pile.pile.empty()) {
			this_pile.suit = card.get_suit();
		}
		this_pile.pile.push(card);
	}

	// REQUIRES col is [0, NUM_COLS - 1]
	// MODIFIES this
	// EFFECTS removes card from selected column
	private void pop_card(final int col) {
		Column this_col = board.get(col);
		this_col.column.pop();
		this_col.top_color = this_col.column.peek().get_color();
	}

	// MODIFIES: this
	// EFFECTS: Initializes board
	public Board() {
		board = new ArrayList<Column>();
		pile = new ArrayList<Pile>();
		// Add empty Columns
		for (int i = 0; i < NUM_COLS; ++i) {
			board.add(new Column());
		}
		// Add empty Piles
		for (int j = 0; j < NUM_PILES; ++j) {
			pile.add(new Pile());
		}
		deck = new Deck(Deck.deck_name);
		next_cards = new ArrayDeque<Card>();
	}
	
	// REQUIRES: this
	// EFFECTS: returns bottom card in selected card
	public Card peek_card(final int col) {
		return board.get(col).column.peek();
	}

	// REQUIRES: col is [0, NUM_COLS - 1]
	// MODIFIES: this
	// EFFECTS: Places card on game board if valid
	public boolean place_card_from_deck(final int col, final Card card) {
		// Assert REQUIRES statement
		assert (col > 0 && col < NUM_COLS - 1);

		if (valid_col_move(col, card)) {
			col_push_card(col, card);
			// TODO: Update deck as well
			return true;
		}
		return false; // Invalid move
	}

	// REQUIRES: old_col and new_col are [0, NUM_COLS - 1]
	// MODIFIES: this
	// EFFECTS: Moves card from one column to the other if valid
	public boolean move_card_btwn_cols(final int old_col, final int new_col) {
		// Assert REQUIRES statement
		assert (old_col > 0 && old_col < NUM_COLS - 1);
		assert (new_col > 0 && new_col < NUM_COLS - 1);
		
		Card top_card = peek_card(old_col);
		if (valid_col_move(new_col, top_card)) {
			col_push_card(new_col, top_card);
			pop_card(old_col);
			return true;
		}
		return false; // Invalid move
	}

	public boolean move_card_to_pile(final int col, final int pile_num) {
		assert (col > 0 && col < NUM_COLS - 1);
		assert (pile_num > 0 && pile_num < NUM_PILES - 1);
		
		Card top_card = peek_card(col);
		if (valid_pile_move(pile_num, top_card)) {
			pop_card(col);
			pile_push_card(pile_num, top_card);
		}
		return false;
	}
	
	// EFFECTS: Prints board layout
	public void print_board() {
		// TODO
	}
}
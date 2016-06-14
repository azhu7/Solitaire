package solitaire;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;

public class Board {

	private final String deck_name = "deck";
	private final int NUM_COLS = 7;
	private final int NUM_PILES = 4;

	// A column in the game board
	private class Column {
		public Column() {
			column = new Stack<Card>();
		}

		public Stack<Card> column;
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
	private ArrayList<Pile> pile;
	private Deck deck;
	private ArrayDeque<Card> next_cards;

	// EFFECTS: Checks whether move is valid
	private boolean valid_move(final int col, final Card card) {
		// Column out of bounds
		if (col < 1 || col > NUM_COLS)
			return false;
		int actual_col = col - 1;
		Column this_column = board.get(actual_col);
		// Column is empty
		if (this_column.column.empty())
			return true;
		// If top_color is "Red"
		boolean correct_color = card.get_suit() == Suit.SPADES || card.get_suit() == Suit.CLUBS;
		if (this_column.top_color == "Black") {
			correct_color = !correct_color;
		}
		// Card is correct color and rank
		return (correct_color && card.get_rank_num() == this_column.column.peek().get_rank_num() - 1);
	}

	// MODIFIES: this
	// EFFECTS: Initializes board
	public Board() {
		// Add empty Columns
		for (int i = 0; i < NUM_COLS; ++i) {
			board.add(new Column());
		}
		// Add empty Piles
		for (int j = 0; j < NUM_PILES; ++j) {
			pile.add(new Pile());
		}
		deck = new Deck(deck_name);
		next_cards = new ArrayDeque<Card>();
	}

	// EFFECTS: Places card on game board if valid
	public boolean place_card(final int col, final Card card) {
		if (valid_move(col, card)) {
			board.get(col).column.push(card);
			board.get(col).top_color = card.get_color();
			// TODO: Update deck as well
			return true;
		}
		return false; // Invalid move
	}
}

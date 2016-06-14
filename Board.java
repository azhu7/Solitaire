package solitaire;

import java.util.ArrayList;
import java.util.Stack;

public class Board {
	
	private final int NUM_COLS = 7;
	private final int NUM_PILES = 4;
	private class Column {
		public Column() {
			column = new Stack<Card>();
		}
		public String top_color;
		public Stack<Card> column;
	}
	private ArrayList<Column> board;
	private ArrayList<Stack<Card>> pile;

	private void initialize_board() {
		for (int i = 0; i < NUM_COLS; ++i) {
			board.add(new Column());
		}
		for (int j = 0; j < NUM_PILES; ++j) {
			pile.add(new Stack<Card>());
		}
	}

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
		boolean correct_color = card.get_suit() == Suit.SPADES || 
				card.get_suit() == Suit.CLUBS;
		if (this_column.top_color == "Black") {
			correct_color = !correct_color;
		}
		// Card is correct color and rank
		return (correct_color && card.get_rank_num() == 
					this_column.column.peek().get_rank_num() - 1);
	}

	public Board() {
		initialize_board();
	}

	public boolean place_card(final int col, final Card card) {
		if (valid_move(col, card)) {
			board.get(col).column.push(card);
			board.get(col).top_color = card.get_color();
			return true;
		}
		return false; // Invalid move
	}
}

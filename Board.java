
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
	private final int NUM_COLS = 7;
	private final int NUM_FOUNDATIONS = 4;
	private final int NUM_IN_QUEUE = 3;

	// A column in the game board
	private class Column {
		public Column() {
			column = new ArrayDeque<Card>();
			pile = new Stack<Card>();
		}

		public Stack<Card> pile; // Face down
		public ArrayDeque<Card> column; // Face up
		public String top_color = null;
	}

	private ArrayList<Column> tableau;
	private ArrayList<Stack<Card>> foundations;
	protected Deck deck; // Protected for debug purposes.
	protected ArrayDeque<Card> card_queue; // addLast and removeFirst.

	// REQUIRES: col is [0, NUM_COLS - 1]
	// EFFECTS: Checks whether can place card in col
	private boolean valid_col_move(final int col, final Card card) {
		// Assert REQUIRES statement
		assert (col >= 0 && col < NUM_COLS);

		Column this_column = tableau.get(col);
		// Column is empty and card is King
		if (this_column.column.isEmpty() && card.get_rank() == Rank.KING)
			return true;
		// If top_color is "Red"
		boolean correct_color = card.get_color() == Card.RED;
		if (this_column.top_color == Card.RED) {
			correct_color = !correct_color;
		}
		// True if card is correct color and rank
		boolean correct_rank = card.get_rank_num() == peek_col_card(col).get_rank_num() - 1;
		return correct_color && correct_rank;
	}

	// REQUIRES: pile_num is [0, NUM_FOUNDATIONS - 1]
	// EFFECTS: Checks whether can place card in foundations
	private boolean valid_foundation_move(final int pile_num, final Card card) {
		Stack<Card> this_pile = foundations.get(pile_num);
		if (this_pile.isEmpty()) {
			// True if card is an Ace
			return card.get_rank() == Rank.ACE;
		} else {
			// True if card is same suit and one rank higher
			boolean correct_suit = card.get_suit() == peek_foundation_card(pile_num).get_suit();
			boolean correct_rank = card.get_rank_num() == this_pile.peek().get_rank_num() + 1;
			return correct_suit && correct_rank;
		}
	}

	// REQUIRES col is [0, NUM_COLS - 1]
	// MODIFIES tableau
	// EFFECTS pushes card to end of column deque. Updates color.
	private void col_push_card(final int col, final Card card) {
		Column this_col = tableau.get(col);
		this_col.column.addLast(card);
		this_col.top_color = card.get_color();
	}

	// REQUIRES col is [0, NUM_COLS - 1]
	//			col is not empty
	// MODIFIES tableau
	// EFFECTS pushes sequence of cards to end of column deque. Updates color.
	private void col_push_cards(final int col, final ArrayList<Card> sequence) {
		Column this_col = tableau.get(col);
		this_col.column.addAll(sequence);
		this_col.top_color = this_col.column.getLast().get_color();
	}
	
	// REQUIRES col is [0, NUM_COLS - 1]
	// 			col is not empty
	// MODIFIES tableau
	// EFFECTS pops card from end of column deque. Updates color.
	private void col_pop_card(final int col) {
		Column this_col = tableau.get(col);
		this_col.column.removeLast();
		if (!this_col.column.isEmpty())
			this_col.top_color = this_col.column.getLast().get_color();
		else
			this_col.top_color = null;
	}
	
	// REQUIRES col is [0, NUM_COLS - 1]
	//			col has at least pop_num elements
	// MODIFIES tableau
	// EFFECTS pops pop_num cards from end of column deque. Updates color.
	private void col_pop_cards(final int col, final int pop_num) {
		assert(tableau.get(col).column.size() >= pop_num);
		for (int i = 0; i < pop_num; i++) {
			col_pop_card(col);
		}
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
	public Board(final boolean shuffle, final boolean deal) {
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
	// Returns null if empty
	public Card peek_col_card(final int col) {
		if (tableau.get(col).column.isEmpty())
			return null;
		return tableau.get(col).column.getLast();
	}

	// REQUIRES: foundation is [0, NUM_FOUNDATIONS - 1].
	// EFFECTS: Returns card on top of selected foundation
	// Returns null if empty
	public Card peek_foundation_card(final int foundation_num) {
		if (foundations.get(foundation_num).isEmpty())
			return null;
		return foundations.get(foundation_num).peek();
	}

	// EFFECTS: Returns card at front of card_queue
	// Returns null if empty
	public Card peek_queue_card() {
		if (card_queue.isEmpty())
			return null;
		return card_queue.getLast();
	}

	// REQUIRES: col is [0, NUM_COLS - 1], queue is not empty
	// MODIFIES: columns, card_queue
	// EFFECTS: Places card in column if valid
	//			Throws exception if invalid
	public void deck_to_col(final int col, final Card card) throws InvalidMoveException {
		// Assert REQUIRES statement
		if (col < 0 || col >= NUM_COLS) {
			throw new InvalidMoveException(
					String.format("Invalid column index %d", col));
		} else if (card_queue.isEmpty()) {
			throw new InvalidMoveException("No cards in queue");
		}
		if (!valid_col_move(col, card)) {
			throw new InvalidMoveException("Invalid move!"); // Invalid move
		}
		// If valid move
		col_push_card(col, card); // Add card to column
		card_queue.removeLast(); // Remove card from queue
	}

	// REQUIRES: foundation_num is [0, NUM_FOUNDATIONS - 1], queue is not empty
	// MODIFIES: foundations, card_queue
	// EFFECTS: Places card on foundations if valid
	// 			Throws exception if invalid
	public void deck_to_foundation(final int foundation_num, final Card card) throws InvalidMoveException {
		// Assert REQUIRES statement
		if (foundation_num < 0 || foundation_num >= NUM_FOUNDATIONS) {
			throw new InvalidMoveException(
					String.format("Invalid foundation index %d", foundation_num));
		} else if (card_queue.isEmpty()) {
			throw new InvalidMoveException("No cards in queue");
		}
		if (!valid_foundation_move(foundation_num, card)) {
			throw new InvalidMoveException("Invalid move!"); // Invalid move
		}
		// If valid move
		foundation_push_card(foundation_num, card); // Add card to foundations
		card_queue.removeLast(); // Remove card from queue
	}

	// REQUIRES: col is [0, NUM_COLS - 1]
	// 			 flip_pile[col] is not empty
	// 			 col is empty
	// MODIFIES: tableau
	// EFFECTS: flips card from top of pile when col is empty
	private void flip_to_col(final int col) throws InvalidMoveException {
		Column this_col = tableau.get(col);
		assert (this_col.column.isEmpty());
		assert (col >= 0 && col < NUM_COLS);

		if (this_col.pile.isEmpty()) {
			throw new InvalidMoveException("No cards to flip");
		}

		Card top_card = this_col.pile.peek();
		this_col.pile.pop();
		col_push_card(col, top_card);
	}

	// REQUIRES: col is [0, NUM_COLS - 1]
	// EFFECTS: returns all cards in col starting from specified card
	private ArrayList<Card> get_card_sequence(final int col, final Card card) throws InvalidCardException {
		ArrayList<Card> sequence = new ArrayList<Card>();
		for (Iterator<Card> itr = tableau.get(col).column.iterator(); itr.hasNext();) {
			if (itr.next().equals(card)) {
				sequence.add(card);
				while (itr.hasNext()) {
					sequence.add(itr.next());
				}
				return sequence;
			}
		}
		// No such card in column
		throw new InvalidCardException(
				String.format("Column %d does not contain Card %s", col, card));
	}
	
	// REQUIRES: old_col and new_col are [0, NUM_COLS - 1].
	// 			 old_col is not empty
	// MODIFIES: columns
	// EFFECTS: Moves cards from one column to the other if valid
	// 			Throws exception if invalid move
	public void col_to_col(final int old_col, final Card card, final int new_col) 
			throws InvalidMoveException, InvalidCardException {
		// Assert REQUIRES statement
		if (old_col < 0 || old_col >= NUM_COLS) {
			throw new InvalidMoveException(
					String.format("Invalid first column index %d", old_col));
		} else if (new_col < 0 || new_col >= NUM_COLS) {
			throw new InvalidMoveException(
					String.format("Invalid second column index %d", new_col));
		} else if (tableau.get(old_col).column.isEmpty()) {
			throw new InvalidMoveException("No cards to move");
		} else if (old_col == new_col) {
			return; // Move to its own column
		}

		// Get sequence of cards from old_col
		ArrayList<Card> sequence = get_card_sequence(old_col, card);

		// Make sure top card in sequence is card being moved
		assert (sequence.get(0).equals(card));
		
		// Attempt to add to new_col
		if (!valid_col_move(new_col, card)) {
			throw new InvalidMoveException("Invalid move!"); // Invalid move
		}
		// If valid move
		col_pop_cards(old_col, sequence.size());
		col_push_cards(new_col, sequence);

		// If column is now empty, flip over next card
		if (tableau.get(old_col).column.isEmpty()) {
			try {
				flip_to_col(old_col);
			} catch (InvalidMoveException e) {}
		}
	}

	// REQUIRES: col is [0, NUM_COLS - 1]
	//			 foundation_num is [0, NUM_FOUNDATIONS - 1]
	//			 col is not empty
	// MODIFIES: columns, foundations
	// EFFECTS: Moves card from column to foundations if valid
	//			Throws exception if invalid move
	public void col_to_foundation(final int col, final int foundation_num) throws InvalidMoveException {
		if (col < 0 || col >= NUM_COLS) {
			throw new InvalidMoveException("Invalid column index");
		} else if (foundation_num < 0 || foundation_num >= NUM_FOUNDATIONS) {
			throw new InvalidMoveException("Invalid foundations index");
		} else if (tableau.get(col).column.isEmpty()) {
			throw new InvalidMoveException("No cards to move");
		}

		Card top_card = peek_col_card(col);
		if (!valid_foundation_move(foundation_num, top_card)) {
			throw new InvalidMoveException("Invalid move!"); // Invalid move
		}
		// If valid move
		col_pop_card(col);
		foundation_push_card(foundation_num, top_card);

		// If column is now empty, flip over next card
		if (tableau.get(col).column.isEmpty()) {
			try {
				flip_to_col(col);
			} catch (InvalidMoveException e) {}
		}
	}

	// MODIFIES: deck, card_queue
	// EFFECTS: Adds next three cards to next_cards
	//			Throws exception if invalid move
	public void get_next_three_cards() throws InvalidMoveException {
		if (deck.empty()) {
			throw new InvalidMoveException("Deck is empty");
		}
		deck.add_to_dealt(card_queue);
		card_queue.clear();
		while (!deck.empty() && card_queue.size() != NUM_IN_QUEUE) {
			card_queue.addFirst(deck.deal_one());
		}
	}

	// MODIFIES: deck, card_queue
	// EFFECTS: Resets deck when player reaches bottom of deck
	//			Throws exception if invalid move
	public void reset_deck() throws InvalidMoveException {
		if (!deck.empty()) {
			throw new InvalidMoveException("Deck is not empty");
		}
		deck.add_to_dealt(card_queue);
		card_queue.clear();
		deck.reset();
	}

	// EFFECTS: Prints out contents of card_queue
	public void print_card_queue() {
		for (Iterator<Card> itr = card_queue.iterator(); itr.hasNext();) {
			System.out.print(itr.next() + " ");
		}
		System.out.println("-->"); // Indicate queue direction
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

		// create ArrayList of Iterators to reference in double for loop
		ArrayList<Iterator<Card>> itr = new ArrayList<Iterator<Card>>();
		for (int j = 0; j < NUM_COLS; ++j) {
			itr.add(tableau.get(j).column.iterator());
		}

		// Print cards in columns, skipping if null
		for (int k = 0; k < max; ++k) { // Iterate vertically
			for (int l = 0; l < NUM_COLS; ++l) { // Iterate horizontally
				if (itr.get(l).hasNext()) {
					Card current = itr.get(l).next();
					System.out.print(current);
				} else {
					// Empty spot
					System.out.print("    ");
					continue;
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	// EFFECTS: Prints out top of each foundation
	public void print_foundations() {
		for (int i = 0; i < NUM_FOUNDATIONS; ++i) {
			Card current = peek_foundation_card(i);
			if (current == null)
				// Empty pile
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
		System.out.println("~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-");
		System.out.print("Deck size: ");
		print_deck_size();
		System.out.print("Card queue: ");
		print_card_queue();
		System.out.print("Foundations: ");
		print_foundations();
		System.out.println("Tableau: ");
		print_piles();
		print_columns();
	}

	public boolean emptyPile(int pile_num) {
		assert (pile_num >= 0 && pile_num < NUM_COLS);
		return tableau.get(pile_num).pile.empty();
	}
}
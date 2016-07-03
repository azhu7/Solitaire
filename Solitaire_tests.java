/*
 * Created June 14, 2016
 * Author: Alexander Zhu
 * Coauthor: Jason Zhu
 */

public class Solitaire_tests {
	
	public static void main(String[] args) {
		System.out.println("Initializing Solitaire tests");
		
		//Call tests here
		test_Card();
		test_Deck();
		test_Board();
		test_Queue();
		test_Print();
		
		System.out.println("All tests passed!");
	}
	
	public static void test_Card() {
		System.out.println("Begin test_Card");
		// Create an Ace of Clubs
		try {
			Card card1 = new Card("AC");
			assert(card1.get_color() == Card.BLACK);
			assert(card1.get_rank() == Rank.ACE);
			assert(card1.get_suit() == Suit.CLUBS);
			assert(card1.get_rank_num() == 1);
			assert(card1.get_rank_print() == "A");
		} catch (InvalidCardException e) {
			System.err.println("InvalidCardException: " + e.getMessage());
		}
		System.out.println("test_Card passed");
	}

	public static void test_Deck() {
		System.out.println("Begin test_Deck");
		// Load default deck, no shuffle
		Deck deck = new Deck(Deck.DECK_NAME, false);
		Card top = deck.top(); // Should be King of Diamonds
		assert(top.get_color() == Card.RED);
		assert(top.get_rank() == Rank.KING);
		assert(top.get_suit() == Suit.DIAMONDS);
		assert(top.get_rank_num() == 13);
		deck.deal_one();
		top = deck.top(); // Should be Queen of Diamonds
		assert(top.get_color() == Card.RED);
		assert(top.get_rank() == Rank.QUEEN);
		assert(top.get_suit() == Suit.DIAMONDS);
		assert(top.get_rank_num() == 12);
		System.out.println("test_Deck passed");
	}
	
	public static void test_Board() {
		System.out.println("Begin test_Board");
		
		Board board = new Board(false, false);
		try {
			board.get_next_three_cards();
		} catch (InvalidMoveException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		board.print_card_queue();
		try {
			Card king_diamonds = new Card("KD");
			Card nine_diamonds = new Card("9D");
			Card two_spades = new Card("2S");
			Card ace_hearts = new Card("AH");
			Card two_hearts = new Card("2H");
			// Place king_diamonds in column 0
			board.deck_to_col(0, board.peek_queue_card());
			board.print_card_queue();
			assert(board.card_queue.size() == 2);
			assert(board.peek_col_card(0).equals(king_diamonds));
			// Place ace_hearts in column 1
			board.deck_to_col(1, ace_hearts);
			assert(board.peek_col_card(1).equals(ace_hearts));
			// Place two_spades in column 2
			board.deck_to_col(2, two_spades);
			assert(board.peek_col_card(2).equals(two_spades));
			// Move ace_hearts from column 1 to 2
			try {
				board.col_to_col(1, ace_hearts, 2);
			} catch (InvalidCardException e) {
				System.err.println("InvalidCardException: " + e.getMessage());
			}
			assert(board.peek_col_card(2).equals(ace_hearts));
			
			// Invalid move
			try {
				board.col_to_col(0, king_diamonds, 2);
			} catch (InvalidMoveException e) {
				System.err.println("InvalidMoveException: " + e.getMessage());
				System.out.println("Invalid move test 1 passed");
			} catch (InvalidCardException e) {
				System.err.println("InvalidCardException: " + e.getMessage());
			}
			
			// Make sure card did not move
			assert(board.peek_col_card(0).equals(king_diamonds));
			// Status: ace_spades in col 0; two_spades, ace_hearts in col 2
	
			// Move ace_hearts to foundation 0
			board.col_to_foundation(2, 0);
			assert(board.peek_foundation_card(0).equals(ace_hearts));
			
			// Invalid move
			try {
				board.col_to_foundation(0, 0);
			} catch (InvalidMoveException e) {
				System.err.println("InvalidMoveException: " + e.getMessage());
				System.out.println("Invalid move test 2 passed");
			}
			
			// Card queue should be empty now
			assert(board.card_queue.isEmpty());
			board.get_next_three_cards();
			board.print_card_queue();
			// Move two_hearts to foundation 0
			board.deck_to_foundation(0, two_hearts);
			assert(board.peek_foundation_card(0).equals(two_hearts));
			// Move king_diamonds to its current column
			try {
				board.col_to_col(0, king_diamonds, 0);
			} catch (InvalidCardException e) {
				System.err.println("InvalidCardException: " + e.getMessage());
			}
			board.reset_deck();
			assert(board.deck.top().equals(nine_diamonds));
		} catch (InvalidMoveException e) {
			System.err.println("InvalidMoveException(s): " + e.getMessage());
			System.err.println("-----DID NOT PASS-----");
		} catch (InvalidCardException e) {
			System.err.println("InvalidCardException: " + e.getMessage());
		}
		System.out.println("test_Board passed");
	}
	
	public static void test_Queue() {
		System.out.println("Begin test_Queue()");
		Board board = new Board(false, false);
		// Print through end of deck
		for (int i = 0; i < 18; ++i) {
			try {
				board.get_next_three_cards();
			} catch (InvalidMoveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			board.print_card_queue();
		}
		assert(board.deck.cardsIsEmpty());
		// TODO: finish this test case
		System.out.println("test_Queue() passed");
	}
	
	public static void test_Print() {
		Board board = new Board(false, true);
		try{
			board.print_board();
			board.get_next_three_cards();
			board.print_board();
			board.get_next_three_cards();
			board.print_board();
			board.reset_deck();
			board.print_board();
			board.get_next_three_cards();
			board.print_board();
		} catch (InvalidMoveException e) {
			e.printStackTrace();
		}
	}

}

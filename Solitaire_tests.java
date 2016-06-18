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
		
		System.out.println("All tests passed!");
	}
	
	public static void test_Card() {
		System.out.println("Begin test_Card");
		// Create an Ace of Clubs
		Card card1 = new Card(Rank.ACE, Suit.CLUBS);
		assert(card1.get_color() == Card.BLACK);
		assert(card1.get_rank() == Rank.ACE);
		assert(card1.get_suit() == Suit.CLUBS);
		assert(card1.get_rank_num() == 1);
		System.out.println(card1.get_deck_index());
		
		System.out.println("test_Card passed");
	}

	public static void test_Deck() {
		System.out.println("Begin test_Deck");
		// Load default deck, no shuffle
		Deck deck = new Deck(Deck.DECK_NAME, false);
		Card top = deck.top();
		assert(top.get_color() == Card.BLACK);
		assert(top.get_rank() == Rank.ACE);
		assert(top.get_suit() == Suit.SPADES);
		assert(top.get_rank_num() == 1);
		deck.deal_one();
		top = deck.top();
		assert(top.get_color() == Card.BLACK);
		assert(top.get_rank() == Rank.TWO);
		assert(top.get_suit() == Suit.SPADES);
		assert(top.get_rank_num() == 2);
		System.out.println("test_Deck passed");
	}
	
	public static void test_Board() {
		System.out.println("Begin test_Board");
		
		Board board = new Board(false, false);
		board.get_next_three_cards();
		board.print_card_queue();
		Card ace_spades = new Card(Rank.ACE, Suit.SPADES);
		Card two_spades = new Card(Rank.TWO, Suit.SPADES);
		Card ace_hearts = new Card(Rank.ACE, Suit.HEARTS);
		Card two_hearts = new Card(Rank.TWO, Suit.HEARTS);
		// Place ace_spades in column 0
		assert(board.deck_to_col(0, board.peek_queue_card()));
		board.print_card_queue();
		assert(board.card_queue.size() == 2);
		assert(board.peek_col_card(0).equals(ace_spades));
		// Place ace_hearts in column 1
		assert(board.deck_to_col(1, ace_hearts));
		assert(board.peek_col_card(1).equals(ace_hearts));
		// Place two_spades in column 2
		assert(board.deck_to_col(2, two_spades));
		assert(board.peek_col_card(2).equals(two_spades));
		// Move ace_hearts from column 1 to 2
		assert(board.col_to_col(1, 2));
		assert(board.peek_col_card(2).equals(ace_hearts));
		// Invalid move
		assert(!board.col_to_col(0, 2));
		// Make sure card did not move
		assert(board.peek_col_card(0).equals(ace_spades));
		// Status: ace_spades in col 0; two_spades, ace_hearts in col 2

		// Move ace_hearts to pile 0
		assert(board.col_to_foundation(2, 0));
		assert(board.peek_foundation_card(0).equals(ace_hearts));
		// Invalid move
		assert(!board.col_to_foundation(0, 0));
		// Card queue should be empty now
		assert(board.card_queue.isEmpty());
		board.get_next_three_cards();
		board.print_card_queue();
		// Move two_hearts to pile 0
		assert(board.deck_to_foundation(0, two_hearts));
		assert(board.peek_foundation_card(0) == two_hearts);
		
		//TODO: test moving col to same col
		
		System.out.println("test_Board passed");
	}
	
	public static void test_Queue() {
		System.out.println("Begin test_Queue()");
		Board board = new Board(false, false);
		// Print through end of deck
		for (int i = 0; i < 18; ++i) {
			board.get_next_three_cards();
			board.print_card_queue();
		}
		assert(board.deck.empty());
		// TODO: finish this test case
		System.out.println("test_Queue() passed");
	}

}

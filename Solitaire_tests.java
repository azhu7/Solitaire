public class Solitaire_tests {
	
	public static void main(String[] args) {
		System.out.println("Initializing Solitaire tests");
		
		//Call tests here
		test_Card();
		//test_Deck();
		//test_Board();
		
		System.out.println("All tests passed!");
	}
	
	public static void test_Card() {
		System.out.println("Begin test_Card");
		// Create an Ace of Clubs
		Card card1 = new Card(Rank.ACE, Suit.CLUBS);
		assert(card1.get_color() == "Black");
		assert(card1.get_rank() == Rank.ACE);
		assert(card1.get_suit() == Suit.CLUBS);
		assert(card1.get_rank_num() == 1);
		
		System.out.println("test_Card passed");
	}

	public static void test_Deck() {
		System.out.println("Begin test_Deck");
		Deck deck = new Deck(Deck.deck_name, false);
		Card top = deck.top();
		assert(top.get_color() == "Black");
		assert(top.get_rank() == Rank.TWO);
		assert(top.get_suit() == Suit.SPADES);
		assert(top.get_rank_num() == 2);
		deck.deal_one();
		top = deck.top();
		assert(top.get_color() == "Black");
		assert(top.get_rank() == Rank.THREE);
		assert(top.get_suit() == Suit.SPADES);
		assert(top.get_rank_num() == 3);
		System.out.println("test_Deck passed");
	}
	
	public static void test_Board() {
		System.out.println("Begin test_Board");
		
		Board board = new Board();
		Card two_clubs = new Card(Rank.TWO, Suit.CLUBS);
		Card ace_hearts = new Card(Rank.ACE, Suit.HEARTS);
		// Place two_clubs
		board.place_card_from_deck(0, two_clubs);
		assert(board.peek_card(0) == two_clubs);
		// Place ace_hearts
		board.place_card_from_deck(0, ace_hearts);
		assert(board.peek_card(0) == ace_hearts);
		// Move ace_hearts
		board.move_card_btwn_cols(0, 1);
		assert(board.peek_card(0) == two_clubs);
		assert(board.peek_card(1) == ace_hearts);
		// Invalid move
		assert(board.move_card_btwn_cols(0, 1));
		// Make sure card did not move
		assert(board.peek_card(0) == two_clubs);
		
		// TODO: Check pile actions
		
		System.out.println("test_Board passed");
	}
}

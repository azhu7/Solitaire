package solitaire;

public class Solitaire_tests {
	
	public static void main(String[] args) {
		System.out.println("Initializing Solitaire tests");
		
		//Call tests here
		test_Card();
		
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
		
		Deck deck = new Deck();
		
		System.out.println("test_Deck passed");
	}
}

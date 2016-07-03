/*
 * Created June 13, 2016
 * Author: Alexander Zhu
 * Coauthor: Jason Zhu
 */

public class Card {
	public static final String RED = "Red";
	public static final String BLACK = "Black";
	
	public static final Suit_BidiMap suits = new Suit_BidiMap();
	public static final Rank_BidiMap ranks = new Rank_BidiMap();
	
	private Rank rank;
	private Suit suit;
	private String color; // Consider removing this
	
	// EFFECTS: Returns string of card color
	private String suit_color(Suit suit_in) {
		if (suit_in == Suit.SPADES || suit_in == Suit.CLUBS) {
			return "Black";
		}
		else {
			return "Red";
		}
	}

	public Card(String card_code) throws InvalidCardException {
		String rank_in = null;
		String suit_in = null;
		// Use first letters of card_code
		rank_in = card_code.substring(0, card_code.length() - 1);
		// Use last letter in card_code
		suit_in = String.valueOf(card_code.charAt(card_code.length() - 1));
		
		// Set card values
		this.rank = ranks.get_rank(rank_in);
		this.suit = suits.get_suit(suit_in);
		this.color = suit_color(this.suit);
		
		// Invalid rank or suit
		if (this.rank == null || this.suit == null) {
			throw new InvalidCardException(
					String.format("Invalid card %s", card_code));
		}
	}
	
	public Rank get_rank() {
		return this.rank;
	}
	
	public int get_rank_num() {
		return ranks.get_rank_num(this.rank);
	}
	
	// Converts rank to print format
	public String get_rank_print() {
		int symbol = this.get_rank_num();
		if (symbol == 1) {
			return "A";
		}
		else if (symbol == 10) {
			return "10";
		}
		else if (symbol == 11) {
			return "J";
		}
		else if (symbol == 12) {
			return "Q";
		}
		else if (symbol == 13) {
			return "K";
		}
		return Integer.toString(symbol);
	}
	
	public Suit get_suit() {
		return this.suit;
	}
	
	public String get_color() {
		return this.color;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((rank == null) ? 0 : rank.hashCode());
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (rank != other.rank)
			return false;
		if (suit != other.suit)
			return false;
		return true;
	}
	
	// For printing purposes
	public String toString() {
		if (this.get_rank_print() == "10") {
			return this.get_rank_print() + suits.get_suit_char(this.suit) + " ";
		}
		return " " + this.get_rank_print() + suits.get_suit_char(this.suit) + " ";
	}

}

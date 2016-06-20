/*
 * Created June 13, 2016
 * Author: Alexander Zhu
 * Coauthor: Jason Zhu
 */

public class Card {
	public static final String RED = "Red";
	public static final String BLACK = "Black";
	
	private Rank rank;
	private int rank_num; // Consider removing this
	private Suit suit;
	private String color; // Consider removing this

	// EFFECTS: Returns numerical value of card rank
	private int rank_to_num(Rank rank_in) {
		switch (rank_in) {
		case ACE:
			return 1;
		case TWO:
			return 2;
		case THREE:
			return 3;
		case FOUR:
			return 4;
		case FIVE:
			return 5;
		case SIX:
			return 6;
		case SEVEN:
			return 7;
		case EIGHT:
			return 8;
		case NINE:
			return 9;
		case TEN:
			return 10;
		case JACK:
			return 11;
		case QUEEN:
			return 12;
		case KING:
			return 13;
		default: {
			System.err.printf("Error: Invalid Rank '%s'. EXIT 1", rank_in);
			System.exit(1);
		}
		}
		return -1; // To suppress compiler warnings
	}
	
	// EFFECTS: Returns string of card color
	private String suit_to_string(Suit suit_in) {
		if (suit_in == Suit.SPADES || suit_in == Suit.CLUBS) {
			return "Black";
		}
		else {
			return "Red";
		}
	}
	
	public Card(Rank rank_in, Suit suit_in) {
		this.suit = suit_in;
		this.rank = rank_in;
		this.rank_num = rank_to_num(rank_in);
		this.color = suit_to_string(suit_in);
	}

	public Rank get_rank() {
		return this.rank;
	}
	
	public int get_rank_num() {
		return this.rank_num;
	}
	
	public String get_rank_symbol() {
		String symbol = String.valueOf(this.rank_num);
		if (symbol == "1") {
			return "A";
		}
		else if (symbol == "11") {
			return "J";
		}
		else if (symbol == "12") {
			return "Q";
		}
		else if (symbol == "13") {
			return "K";
		}
		return symbol;
	}
	
	public Suit get_suit() {
		return this.suit;
	}
	
	public String get_color() {
		return this.color;
	}

	// REQUIRES using default deck
	// EFFECTS Returns index in deck. Used in Deck methods
	public int get_deck_index() {
		int index = get_rank_num() - 1;
		// Continually adds 13 based on suit
		switch (get_suit()) {
		case DIAMONDS:
			index += 13;
		case CLUBS:
			index += 13;
		case HEARTS:
			index += 13;
		default: {
			return index;
		}
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((rank == null) ? 0 : rank.hashCode());
		result = prime * result + rank_num;
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
		if (rank_num != other.rank_num)
			return false;
		if (suit != other.suit)
			return false;
		return true;
	}
	
	public String suitLetter() {
		return (suit.toString()).substring(0,1);
	}
	
	public String toString() {
		//return "/" + rank + " of " + suit + '\\';
		return " " + this.get_rank_symbol() + this.suitLetter() + " ";
	}

}

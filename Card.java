package solitaire;

public class Card {
	private Rank rank;
	private int rank_num; // Consider removing this
	private Suit suit;
	private String color; // Consider removing this

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
	
	public Suit get_suit() {
		return this.suit;
	}
	
	public String get_color() {
		return this.color;
	}

	public String toString() {
		return rank + " of " + suit;
	}
}

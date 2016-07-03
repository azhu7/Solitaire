/*
 * Created July 2, 2016
 * Author: Alexander Zhu
 * Coauthor: Jason Zhu
 */

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;

public class Suit_BidiMap {
	
	private BidiMap<Suit, String> suits;
	
	public Suit_BidiMap() {
		suits = new TreeBidiMap<Suit, String>();
		suits.put(Suit.SPADES, "S");
		suits.put(Suit.HEARTS, "H");
		suits.put(Suit.CLUBS, "C");
		suits.put(Suit.DIAMONDS, "D");
	}
	
	public String get_suit_char(Suit key) {
		return suits.get(key);
	}
	
	public Suit get_suit(String value) {
		return suits.getKey(value);
	}
}

public interface Use_cases {

	void deck_to_col(final int col, final Card card) throws InvalidMoveException ;
	
	void deck_to_foundation(final int foundation_num, final Card card) throws InvalidMoveException ;
	
	void col_to_col(final int old_col, final Card card, final int new_col)
			throws InvalidMoveException, InvalidCardException ;
	
	void col_to_foundation(final int col, final int foundation_num) throws InvalidMoveException ;
	
	void get_next_three_cards() throws InvalidMoveException;
	
	void reset_deck() throws InvalidMoveException;
}

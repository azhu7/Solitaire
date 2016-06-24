
public interface Use_cases {

	void deck_to_col(final int col, final Card card) throws InvalidMoveException ;
	
	void deck_to_foundation(final int foundation_num, final Card card) throws InvalidMoveException ;
	
	void col_to_col(final int old_col, final int new_col) throws InvalidMoveException ;
	
	void col_to_foundation(final int col, final int foundation_num) throws InvalidMoveException ;
	
	void flip_to_col(final int col) throws InvalidMoveException ;
	
	void get_next_three_cards();
	
	void reset_deck();
}

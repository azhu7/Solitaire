
public interface Use_cases {

	boolean deck_to_col(final int col, final Card card);
	
	boolean deck_to_foundation(final int foundation_num, final Card card);
	
	boolean col_to_col(final int old_col, final int new_col);
	
	boolean col_to_foundation(final int col, final int foundation_num);
	
	boolean flip_to_col(final int col);
	
	void get_next_three_cards();
	
	void reset_deck();
}

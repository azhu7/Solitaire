import java.util.Scanner;

/*
 * Created June 13, 2016
 * Author: Alexander Zhu
 * Coauthor: Jason Zhu
 */

public class Driver {

	public static void main(String[] args) {
		// TODO: Init output stuff

		Board board = new Board(false, true);
		board.print_board();
		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.println("Choose an action:");
			System.out.println("[1 - Deck to Col] [2 - Deck to Foundation]");
			System.out.println("[3 - Flip Pile Card] [4 - Col to Col]");
			System.out.println("[5 - Col to Foundation] [6 - Next 3 cards]");
			System.out.println("[7 - Reset Deck] [8 - Quit Game]");
			boolean valid_choice = false;
			while (!valid_choice) {
				int choice = in.nextInt();
				switch (choice) {
					// Deck to Col
					case 1: {
						if (board.card_queue.isEmpty()) {
							System.err.println("Error: No cards in queue");
							break;
						}
						Card top_card = board.peek_queue_card();
						System.out.print("Move to which column? ");
						int col = in.nextInt();
						boolean valid_move = false;
						while (!valid_move) {
							try {
								board.deck_to_col(col, top_card);
								valid_move = true;
							} catch (InvalidMoveException e) {
								System.err.println("Error: " + e.getMessage());
							} finally {
								col = in.nextInt();
							}
						}
						valid_choice = true;
						break;
					}
					// Deck to Foundation
					case 2: {
						if (board.card_queue.isEmpty()) {
							System.err.println("Error: No cards in queue");
							break;
						}
						Card top_card = board.peek_queue_card();
						System.out.print("Move to which foundation? ");
						int col = in.nextInt();
						boolean valid_move = false;
						while (!valid_move) {
							try {
								board.deck_to_foundation(col, top_card);
								valid_move = true;
							} catch (InvalidMoveException e) {
								System.err.println("Error: " + e.getMessage());
							} finally {
								col = in.nextInt();
							}
						}
						valid_choice = true;
						break;
					}
					// Flip pile card
					// TODO: Remove this case and call flip_to_col automatically when a column empties
					case 3: {
						System.out.print("Which pile to flip? ");
						int pile_num = in.nextInt();
						boolean valid_move = false;
						while (!valid_move) {
							// TODO: Add exceptions.
							// TODO: Make sure there are piles to flip, or provide option to return to choice selection
							try {
								board.flip_to_col(pile_num);
								valid_move = true;
							} catch (InvalidMoveException e) {
								System.err.println("Error: " + e.getMessage());
							} finally {
								pile_num = in.nextInt();
							}
						}
						valid_choice = true;
						break;
					}
					// Col to Col
					case 4: {
						System.out.print("From which column to which column (# #)? ");
						int old_col = in.nextInt();
						int new_col = in.nextInt();
						boolean valid_move = false;
						while (!valid_move) {
							try {
								board.col_to_col(old_col, new_col);
								valid_move = true;
							} catch (InvalidMoveException e) {
								System.err.println("Error: " + e.getMessage());
							// TODO: Catch more exceptions? ..probably ok with messages
							} finally {
								old_col = in.nextInt();
								new_col = in.nextInt();
							}
						}
						valid_choice = true;
						break;
					}
					// Col to Foundation
					case 5: {
						System.out.print("From which column to which foundation (# #)? ");
						int old_col = in.nextInt();
						int foundation_num = in.nextInt();
						boolean valid_move = false;
						while (!valid_move) {
							try {
								board.col_to_foundation(old_col, foundation_num);
								valid_move = true;
							} catch (InvalidMoveException e) {
								System.err.println("Error: " + e.getMessage());
							} finally {
								old_col = in.nextInt();
								foundation_num = in.nextInt();
							}
						}
						valid_choice = true;
						break;
					}
					// Next 3 cards
					case 6: {
						// TODO: Add try catch block for empty deck exception
						board.get_next_three_cards();
						valid_choice = true;
						break;
					}
					// Reset Deck
					case 7: {
						// TODO: Add try catch block for non-empty deck exception
						board.reset_deck();
						valid_choice = true;
						break;
					}
					case 8: {
						System.out.println("Thanks for playing!");
						System.exit(0);
					}
					default: {
						System.out.println("Error: invalid choice. Select a different choice.");
					}
				} // switch (choice)
				if (valid_choice)
					board.print_board();
			} // while (!valid_choice)
		} // while (true)
	} // main
}

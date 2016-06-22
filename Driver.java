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
			System.out.println("[3 - Flip pile card] [4 - Col to Col]");
			System.out.println("[5 - Col to Foundation] [6 - Next 3 cards]");
			System.out.println("[7 - Reset deck]");
			boolean valid_choice = false;
			while (!valid_choice) {
				int choice = in.nextInt();
				switch (choice) {
				case 1: {
					if (board.card_queue.isEmpty()) {
						System.out.println("Error: card queue is empty. Select a different option.");
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
						} catch (IndexOutOfBoundsException ex) {
							System.out.println("Error: invalid index. Select a different index.");
							col = in.nextInt();
						}
					}
					valid_choice = true;
					break;
				}
				case 2: {
					if (board.card_queue.isEmpty()) {
						System.out.println("Error: card queue is empty. Select a different option.");
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
						} catch (IndexOutOfBoundsException ex) {
							System.out.println("Error: invalid index. Select a different index.");
						} finally {
							col = in.nextInt();
						}
					}
					valid_choice = true;
					break;
				}
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
						} catch (IndexOutOfBoundsException ex) {
							System.out.println("Error: invalid index. Select a different index.");
						} /*catch (EmptyPileException ex) {
							System.out.println("Error: pile is empty. Select a different pile");
						} catch (InvalidMoveException ex) {
							System.out.println("Error: column is not empty. Select a different pile");
						} */finally {
							pile_num = in.nextInt();
						}
					}
					valid_choice = true;
					break;
				}
				case 4: {
					System.out.print("From which column to which column (# #)? ");
					int old_col = in.nextInt();
					int new_col = in.nextInt();
					boolean valid_move = false;
					while (!valid_move) {
						try {
							board.col_to_col(old_col, new_col);
							valid_move = true;
						} catch (IndexOutOfBoundsException ex) {
							System.out.println("Error: invalid index. Select a different index.");
						} // TODO: Catch more exceptions
						finally {
							old_col = in.nextInt();
							new_col = in.nextInt();
						}
					}
					valid_choice = true;
					break;
				}
				case 5: {
					System.out.print("From which column to which foundation (# #)? ");
					int old_col = in.nextInt();
					int foundation_num = in.nextInt();
					boolean valid_move = false;
					while (!valid_move) {
						try {
							board.col_to_foundation(old_col, foundation_num);
							valid_move = true;
						} catch (IndexOutOfBoundsException ex) {
							System.out.println("Error: invalid index. Select a different index.");
						} finally {
							old_col = in.nextInt();
							foundation_num = in.nextInt();
						}
					}
					valid_choice = true;
					break;
				}
				case 6: {
					// TODO: Add try catch block for empty deck exception
					board.get_next_three_cards();
					valid_choice = true;
					break;
				}
				case 7: {
					// TODO: Add try catch block for non-empty deck exception
					board.reset_deck();
					valid_choice = true;
					break;
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

import java.util.Scanner;

/*
 * Created June 13, 2016
 * Author: Alexander Zhu
 * Coauthor: Jason Zhu
 */

// TODO: Get prev card if queue empty

public class Driver {

	public static void main(String[] args) {
		// TODO: Init output stuff

		Board board = new Board(true, true); // No shuffle for debugging
		board.print_board();
		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.println("Choose an action:");
			System.out.println("[1 - Next 3 cards]\n[2 - Deck to Col]");
			System.out.println("[3 - Deck to Foundation]\n[4 - Col to Col]");
			System.out.println("[5 - Col to Foundation]\n[6 - Reset Deck]");
			System.out.println("[7 - Quit Game]");
			boolean valid_choice = false;
			while (!valid_choice) {
				int choice = in.nextInt();
				switch (choice) {
				// Next 3 cards
				case 1: {
					try {
						board.get_next_three_cards();
						valid_choice = true;
					} catch (InvalidMoveException e) {
						System.err.println("Error: " + e.getMessage());
					}
					break;
				}
				// Deck to Col
				case 2: {
					if (board.card_queue.isEmpty()) {
						System.err.println("Error: No cards in queue");
						break;
					}
					Card top_card = board.peek_queue_card();
					boolean valid_move = false;
					while (!valid_move) {
						try {
							System.out.print("Move to which column (-1 to go back)? ");
							int col = in.nextInt();
							if (col == -1) {
								valid_choice = true;
								valid_move = true;
								break;
							}
							board.deck_to_col(col, top_card);
							valid_move = true;
						} catch (InvalidMoveException e) {
							System.err.println("Error: " + e.getMessage());
						}
					}
					valid_choice = true;
					break;
				}
				// Deck to Foundation
				case 3: {
					if (board.card_queue.isEmpty()) {
						System.err.println("Error: No cards in queue");
						break;
					}
					Card top_card = board.peek_queue_card();
					boolean valid_move = false;
					while (!valid_move) {
						try {
							System.out.print("Move to which foundation (-1 to go back)? ");
							int foundation = in.nextInt();
							if (foundation == -1) {
								valid_choice = true;
								valid_move = true;
								break;
							}
							board.deck_to_foundation(foundation, top_card);
							valid_move = true;
						} catch (InvalidMoveException e) {
							System.err.println("Error: " + e.getMessage());
						}
					}
					valid_choice = true;
					break;
				}
				// Col to Col
				case 4: {
					boolean valid_move = false;
					while (!valid_move) {
						try {
							System.out.print("From which column (-1 to go back)? ");
							int old_col = in.nextInt();
							System.out.print("To which column? ");
							int new_col = in.nextInt();
							System.out.print("Move which card? ");
							String card_code = in.next();
							if (old_col == -1 || new_col == -1 || card_code == "-1") {
								valid_choice = true;
								break;
							}
							board.col_to_col(old_col, new Card(card_code), new_col);
							valid_move = true;
						} catch (InvalidMoveException e) {
							System.err.println("Error: " + e.getMessage());
						} catch (InvalidCardException e) {
							System.err.println("Error: " + e.getMessage());
						}
					}
					valid_choice = true;
					break;
				}
				// Col to Foundation
				case 5: {
					boolean valid_move = false;
					while (!valid_move) {
						try {
							System.out.print("From which column (-1 to go back) ");
							int old_col = in.nextInt();
							System.out.print("To which foundation? ");
							int foundation_num = in.nextInt();
							if (old_col == -1 || foundation_num == -1) {
								valid_choice = true;
								valid_move = true;
								break;
							}
							board.col_to_foundation(old_col, foundation_num);
							valid_move = true;
						} catch (InvalidMoveException e) {
							System.err.println("Error: " + e.getMessage());
						}
					}
					valid_choice = true;
					break;
				}
				// Reset Deck
				case 6: {
					try {
						board.reset_deck();
						valid_choice = true;
					} catch (InvalidMoveException e) {
						System.err.println("Error: " + e.getMessage());
					}
					break;
				}
				case 7: {
					System.out.print("Are you sure you want to quit (y/n)? ");
					String quit = in.next();
					if (quit.equals("n")) {
						valid_choice = true;
						break;
					}
					else {
						System.out.println("Thanks for playing!");
						in.close();
						System.exit(0);
					}
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

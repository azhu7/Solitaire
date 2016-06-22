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
			System.out.println("[1 - Deck to Column] [2 - Deck to Foundation] [3 - Flip pile card]");
			System.out.println("[4 - Column to Column] [5 - Column to Foundation]");
			int choice = in.nextInt();
			switch (choice) {
				case 1: {
				
				}
				case 2: {
				
				}
				case 3:{
				
				}
				case 4:{
				
				}
				case 5: {
				
				}
				default: {
				
				}
			}
		}
	}
}

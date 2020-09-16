import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class TuringMachine {
	Map<String, String> states = new HashMap<>(); // Map containing transition states
	Map<String, String> dir = new HashMap<>(); // Map containing direction
	List<String> finals = new ArrayList<>(); // List of final states
	int state = 0; // State the TM is currently in
	boolean acceptByFinal = false;
	
	// Add a state transition to the "states" map
	// The key is the "from state" and "read letter",
	// The value is the "to state" and "write letter"
	// Examples:	"0 ", "1 "
	//				"2a", "2c"
	void newState(String s1, String s2) {
		states.put(s1, s2);
	}

	void addDirection(String c, String d) {
		dir.put(c, d);
	}
	
	// Returns a string in turnstile notation showing
	// where the head is on the tape display during
	// computation
	String display(char c[], int pos, boolean halted) {
		String turnStile = "";
		if (halted) {
			turnStile += "|-* ";
		} else {
			turnStile = "|- ";
		}
		int flag = 0; // Flag to catch edge case
		for (int i = 0; i < c.length; i++) {
			if (i == pos + 1) {
				turnStile += " q" + state + " ";
				flag = 1;
			}
			turnStile += c[i];
		}

		if (flag == 0) {
			turnStile += " q" + state + " ";
		}
		return turnStile;
	}

	// Main function to drive the Turing Machine
	String runMachine(String input) {
		char inputAsChar[] = input.toCharArray();
		int headPosition = 0;
		System.out.println("Turnstile Notation of Machine's Progress:");
		while (true) {
			if (headPosition < 0) {
				return "Abnormal Termination";
			}
			String key = String.valueOf(state) + String.valueOf(inputAsChar[headPosition]);
			if (!states.containsKey(key)) {
				String toReturn = "\nTuring Machine Halted With: " + String.valueOf(inputAsChar) + " at: " + display(inputAsChar, headPosition, true);
				if (!acceptByFinal) {
					return toReturn;
				}
				if (finals.contains(String.valueOf(state))) {
					toReturn += "\nThe Turing Machine halted in a final state.";
				} else {
					toReturn += "\nThe Turing Machine did not halt in a final state.";
				}
				return toReturn;
			}
			inputAsChar[headPosition] = states.get(key).charAt(1);
			String s = states.get(key).charAt(0) + "";
			state = Integer.valueOf(s);

			System.out.println(display(inputAsChar, headPosition, false));

			if (dir.get(key + states.get(key)).equals("R")) {
				headPosition++;
			} else {
				headPosition--;
			}
		}
	}

	// Function to ask for final states, if TM is to accept by final state
	// Final states are given by a single number separated by spaces
	void finalStates() {
		System.out.println("Please enter a list of final states (ex: 1 2 5):");
		Scanner sc = new Scanner(System.in);
		String line = sc.nextLine();
		sc.close();

		String lineArr[] = line.split(" ");
		for (String s : lineArr) {
			finals.add(s);
		}
		acceptByFinal = true;
	}
}

public class TM {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		TuringMachine machine = new TuringMachine();
		while (true) {
			String line = sc.nextLine();
			if (line.equals("-1")) {
				break;
			}

			char c[] = line.toCharArray();
			String s1 = String.valueOf(c[0]) + c[1] + "";
			String s2 = String.valueOf(c[2]) + c[3] + "";

			machine.newState(s1, s2);
			String direction = c[4] + "";
			machine.addDirection(s1 + s2, direction);
		}

		String input = sc.nextLine();
		String choice;
		do {
			System.out.println("Would you like to accept by final state? (yes/no)");
			choice = sc.nextLine();
			if (choice.equals("yes")) {
				machine.finalStates();
			}
		} while (!choice.equals("yes") && !choice.equals("no"));

		System.out.println(machine.runMachine(input));

		sc.close();
	}
}

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Vinícius Schütz Piva
 */
public class FSAv2 {

	final private List<Character>                    alphabet;
	final private Map<Pair<State, Character>, State> transitions;
	      private State currentState = null;

	private StringBuilder word       = new StringBuilder();
	private List<String>  foundWords = new ArrayList<>();

	protected FSAv2(List<Character> alphabet, Map<Pair<State, Character>, State> transitions) {

		this.alphabet    = alphabet;
		this.transitions = transitions;
	}

	/*
	public State getCurrentState() {
		return currentState;
	}
	 */

	private State nextState(char symbol) {
		return this.currentState = transitions.get(new Pair<>(this.currentState, symbol));
	}

	public void process(String data) {
		for (int i = 0; i < data.length(); i++) {
			char symbol = data.charAt(i);
			if (nextState(symbol) == null) {
				throw new InputMismatchException("No transition: " + this.currentState.toString() + "(" + symbol + ")");
			}
		}
		if (currentState.isEndState() ) {
			System.out.println("[FSAv2]: Word did not match: " + this.word.toString());
			this.word.setLength(0);
		}
	}
}

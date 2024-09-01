
class State {
	final private int id;
	final boolean endState;

	State(int id, boolean endState) {
		this.id = id;
		this.endState = endState;
	}

	@Override
	public String toString() {
		return String.format("q%d", id);
	}

	public boolean isEndState() {
		return this.endState;
	}
}

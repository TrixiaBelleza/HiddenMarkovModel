public class State {
	private String stateSymbol;
	private int stateNum;
	private double probability;
	public State(String stateSymbol, int stateNum){

		this.stateSymbol = stateSymbol;
		this.stateNum = stateNum;
		this.probability = -1;
	}

	//getters
	public String getStateSymbol() {
		return this.stateSymbol;
	}
	public int getStateNum(){
		return this.stateNum;
	}
	
	//setter
	public void setProbability(double probability) {
		this.probability = probability;
	}
}
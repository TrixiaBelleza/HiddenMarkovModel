public class ProbabilityAGivenB {
	private State A;
	private State B;
	private double probability;
	public ProbabilityAGivenB(State A, State B){
		this.A = A;
		this.B = B;
		this.probability = 0;
	}

	//getters
	public State getA(){
		return A;
	}

	public State getB(){
		return B;
	}

	public double getProbability(){
		return probability;
	}

	//setters
	public void setProbability(double probability) {
		this.probability = probability;
	}
}
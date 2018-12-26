import java.util.*;
import java.io.*;

public class Main {
	public static void main (String [] args) throws FileNotFoundException {

		System.out.print("Enter filename: ");
        Scanner sc = new Scanner(System.in);
        String filename = sc.next();
	
        File inputFile = new File(filename);
        Scanner scannedInput = new Scanner(inputFile);

        //iterate scanned input
        String line;
        String[] tokens;
        String[] sequence; //for example: STSST, sequence[] = [S, T, S, S, T]
        String[] stateSplit;
        LinkedList<String> finalSequence = new LinkedList<String>();
        
        //skip first two lines
        scannedInput.nextLine();
        scannedInput.nextLine();

        //loop the lines of a file
        //starts at line 3 (1 indexing sa file)
        int lineCount = 3;
        double probEgivenS = 0;
        double probFgivenS = 0;
        double probEgivenT = 0;
        double probFgivenT = 0;
        int numOfCases = 0;
   
        ArrayList<ProbabilityAGivenB> toFindAgivenB = new ArrayList<ProbabilityAGivenB>();  //if to find is P(A|B)
        ArrayList<State> toFindStates = new ArrayList<State>();                             //if to find is P(A) or P(B), etc
        
        State[] computeStates = new State[2];
        while(scannedInput.hasNext()) {
        	line = scannedInput.nextLine();
        	tokens = line.split(" ");
        	
        	/* Fetches for P(E|S) & P(F|S) */
        	if(lineCount == 3) {
        		for(int i = 0; i < tokens.length; i++) {
        			if(i == 0) probEgivenS = Double.parseDouble(tokens[i]);
        			if(i == 1) probFgivenS = Double.parseDouble(tokens[i]);
        		}
        	}

        	/* Fetches for P(E|S) & P(F|S) */
        	else if(lineCount == 4) {
        		for(int i = 0; i < tokens.length; i++) {
        			if(i==0) probEgivenT = Double.parseDouble(tokens[i]);
        			if(i==1) probFgivenT = Double.parseDouble(tokens[i]);
        		}
        	}

        	/* Fetches sequence used to derive transition probability values */
        	else if(lineCount == 5){
        		sequence = line.split(""); //split without delimiter
        		for(int i = 0; i < sequence.length; i++){
        			finalSequence.add(sequence[i]);
        		}
        	}

            /* Fetches number of cases */
        	else if(lineCount == 6){
        		numOfCases = Integer.parseInt(line); 
        	}

            /* Get states na dapat hanapan ng probabilities */
        	else if(lineCount >= 7) {
        		int count = 0;
        		//get each token separated by space
        		for(int i = 0; i<tokens.length; i++) {
                    //get states 
                    if(i == 0 || i == 2){
        				stateSplit = tokens[i].split(""); //split mo yung token
        				                                 //example tokens[i] = S1 stateSplit[0] = S, stateSplit[1] = 1

        				State newState = new State(stateSplit[0], Integer.parseInt(stateSplit[1]));
        				
        				computeStates[count] = newState;
        				count++;
        			}
        		}
                if(tokens.length <= 1) {
                    toFindStates.add(computeStates[0]);
                }
                else {
                    ProbabilityAGivenB newProb = new ProbabilityAGivenB(computeStates[0], computeStates[1]);
                    toFindAgivenB.add(newProb);
                }
        	}

        	lineCount++;
        }

        //Given Probabilities

        ArrayList<ProbabilityAGivenB> givenProb = new ArrayList<ProbabilityAGivenB>();
        System.out.println("Read from file: ");
        System.out.println("P(E|S): " + probEgivenS);
        ProbabilityAGivenB eGivenS = new ProbabilityAGivenB(new State("E", -1), new State("S", -1));
        eGivenS.setProbability(probEgivenS);
        givenProb.add(eGivenS);           //-1 yung num kasi pwede siya any number.
        
        System.out.println("P(F|S): " + probFgivenS);
        ProbabilityAGivenB fGivenS = new ProbabilityAGivenB(new State("F", -1), new State("S", -1));
        fGivenS.setProbability(probFgivenS);
        givenProb.add(fGivenS);    

        System.out.println("P(E|T): " + probEgivenT);
        ProbabilityAGivenB eGivenT = new ProbabilityAGivenB(new State("E", -1), new State("T", -1));
        eGivenT.setProbability(probEgivenT);
        givenProb.add(eGivenT);    

        System.out.println("P(F|T): " + probFgivenT);
        ProbabilityAGivenB fGivenT = new ProbabilityAGivenB(new State("F", -1), new State("T", -1));
        fGivenT.setProbability(probFgivenT);
        givenProb.add(fGivenT);    

        
        //Sequence
        for(int i = 0; i < finalSequence.size(); i++) {
        	System.out.print(finalSequence.get(i));
        }
        System.out.println();

        double pS0 = 0;
        double pT0 = 0;
    
        /* COMPUTE FOR P(S0) */
        if(finalSequence.get(0).equals("S")) {
        	pS0 = (double)1;
        	pT0 = (double)0;
        }
        else {
        	pS0 = (double)0;
        	pT0 = (double)1;
        }


        /*************************************

                Compute Probabilities

        *************************************/
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
            writer.write("P(S0): " + String.valueOf(pS0) + "\n");
            System.out.println("P(S0): " + pS0);
            writer.write("P(S0): " + String.valueOf(pT0) + "\n");
            System.out.println("P(T0): " + pT0);
            writer.write("P(S|S): " + String.valueOf(getSgivenS(finalSequence)) + "\n");
            System.out.println("P(S|S): " + String.valueOf(getSgivenS(finalSequence)));
            writer.write("P(T|S): " + String.valueOf(getTgivenS(finalSequence)));
            System.out.println("P(T|S): " + String.valueOf(getTgivenS(finalSequence)));
            writer.write("P(S|T):  " + String.valueOf(getSgivenT(finalSequence)));
            System.out.println("P(S|T): " + String.valueOf(getSgivenT(finalSequence)));
            writer.write("P(T|T): " + String.valueOf(getTgivenT(finalSequence)));
            System.out.println("P(T|T): " + String.valueOf(getTgivenT(finalSequence)));

            //iterate toFindAgivenB arraylist
            for(int i=0; i<toFindAgivenB.size(); i++) {
                writer.write("P(" + toFindAgivenB.get(i).getA().getStateSymbol() + toFindAgivenB.get(i).getA().getStateNum() +  "|" + toFindAgivenB.get(i).getB().getStateSymbol() + toFindAgivenB.get(i).getB().getStateNum() + "): ");
                System.out.print("P(" + toFindAgivenB.get(i).getA().getStateSymbol() + toFindAgivenB.get(i).getA().getStateNum() +  "|" + toFindAgivenB.get(i).getB().getStateSymbol() + toFindAgivenB.get(i).getB().getStateNum() + "): ");
                writer.write(String.valueOf(getValue_A_given_B(toFindAgivenB.get(i).getA(), toFindAgivenB.get(i).getB(), givenProb, pS0, finalSequence)) + "\n");
                System.out.println(getValue_A_given_B(toFindAgivenB.get(i).getA(), toFindAgivenB.get(i).getB(), givenProb, pS0, finalSequence));
            }

            //iterate toFindStates arrayList
            State oppositeState;
            double result=0;
            for(int j=0; j<toFindStates.size(); j++) {
                writer.write("P(" + toFindStates.get(j).getStateSymbol() + toFindStates.get(j).getStateNum() + "): ");
                System.out.print("P(" + toFindStates.get(j).getStateSymbol() + toFindStates.get(j).getStateNum() + "): ");

                switch(toFindStates.get(j).getStateSymbol()){
                    case "S":
                        result = getProbState(toFindStates.get(j), pS0, finalSequence);
                        break;
                    case "T":
                        oppositeState = new State("S", toFindStates.get(j).getStateNum());
                        result = 1 - getProbState(oppositeState, pS0, finalSequence);
                        break;
                    case "E":
                        result = getTotalProb(givenProb, pS0, finalSequence, toFindStates.get(j));
                        break;
                    case "F":
                        result = getTotalProb(givenProb, pS0, finalSequence, toFindStates.get(j));
                        break;
                }
                System.out.println(result);
                writer.write(String.valueOf(result) + "\n");
            }
            writer.close();
        }catch(FileNotFoundException e){
            System.out.println("File not found");
        } catch(Exception e){
            System.out.println(e.getMessage());
        }       
    }


    //get prob of a state using total probability. Example find P(S3)
    public static double getProbState(State state, double pS0, LinkedList<String> finalSequence) {
        LinkedList<Double> results = new LinkedList<Double>();
        results.add(pS0);
        int count = 1;
        double result;

        while(count <= state.getStateNum()) {

            result = (getSgivenS(finalSequence) * results.get(count-1)) + (getSgivenT(finalSequence) * (1 - results.get(count-1)));
            results.add(result);
            count++;
        }

        return results.get(count-1);
    }

    //get total probability
    public static double getTotalProb(ArrayList<ProbabilityAGivenB> givenProb, double pS0, LinkedList<String> finalSequence, State state) {
        double probability = 0;
        State marginal;
        for(int i = 0; i < givenProb.size(); i++) {
            if(givenProb.get(i).getA().getStateSymbol().equals(state.getStateSymbol())) {

                marginal = new State(givenProb.get(i).getB().getStateSymbol(), state.getStateNum()); 

                //if P(E|S)
                if(givenProb.get(i).getB().getStateSymbol().equals("S"))                    
                    probability += givenProb.get(i).getProbability() * getProbState(marginal, pS0, finalSequence);

                //else if P(E|T)
                else 
                    probability += givenProb.get(i).getProbability() * (1-getProbState(marginal, pS0, finalSequence));
               
            }
        }
        return probability;
    }

    //get probability a given b
    public static double getValue_A_given_B(State A, State B, ArrayList<ProbabilityAGivenB> givenProb, double pS0, LinkedList<String> finalSequence) {
        double probability = 0;
        for(int i=0; i<givenProb.size(); i++) {
            if(givenProb.get(i).getB().getStateSymbol().equals(A.getStateSymbol())) {

                if(givenProb.get(i).getA().getStateSymbol().equals(B.getStateSymbol())) {
                    // System.out.println("P(toFindAgivenB state A): " + getProbState(A, pS0, finalSequence));
                    // System.out.println("P(toFindAgivenB state B): " + getTotalProb(givenProb, pS0, finalSequence, B));
                    probability = (givenProb.get(i).getProbability() * getProbState(A, pS0, finalSequence)) / getTotalProb(givenProb, pS0, finalSequence, B);
                }
            }
        }
        return probability;
    }

    //how many times a state occurred in a sequence
	public static int getStateCount(LinkedList<String> finalSequence, String stateSymbol){
		int sCount = 0;
		for(int i = 0; i < finalSequence.size()-1; i++) {
			if(finalSequence.get(i).equals(stateSymbol))
				sCount++;
		}
		return sCount;
	} 


	public static double getSgivenS(LinkedList<String> finalSequence){
		int sCount = getStateCount(finalSequence, "S");
		int s_given_s_count = 0;
		for(int i = 0; i < finalSequence.size()-1; i++) {
			if(finalSequence.get(i).equals("S") && finalSequence.get(i+1).equals("S"))
				s_given_s_count++;
		}
		return (double)s_given_s_count/(double)sCount;
	}


	public static double getTgivenS(LinkedList<String> finalSequence){
		int sCount = getStateCount(finalSequence, "S");
		int t_given_s_count = 0;
		for(int i = 0; i < finalSequence.size()-1; i++) {
			if(finalSequence.get(i).equals("S") && finalSequence.get(i+1).equals("T"))
				t_given_s_count++;
		}
		return (double)t_given_s_count/(double)sCount;
	}

	public static double getTgivenT(LinkedList<String> finalSequence){
		int tCount = getStateCount(finalSequence, "T");
		int t_given_t_count = 0;
		for(int i = 0; i < finalSequence.size()-1; i++) {
			if(finalSequence.get(i).equals("T") && finalSequence.get(i+1).equals("T"))
				t_given_t_count++;
		}
		return (double)t_given_t_count/(double)tCount;
	}

	public static double getSgivenT(LinkedList<String> finalSequence){
		int tCount = getStateCount(finalSequence, "T");
		int s_given_t_count = 0;
		for(int i = 0; i < finalSequence.size()-1; i++) {
			if(finalSequence.get(i).equals("T") && finalSequence.get(i+1).equals("S"))
				s_given_t_count++;
		}
		return (double)s_given_t_count/(double)tCount;
	}


}
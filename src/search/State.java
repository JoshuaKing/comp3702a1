package search;

/**
 * @author Josh King (42344825)
 * @author Coen McClelland (42363901)
 */

/**
 * The State interface.
 * Implement this to search for goal states.
 */
public interface State {
    
    /**
     * Determines whether the state is a goal state or not
     * @return true if this is a goal state, false otherwise
     */
    public boolean goal();
    
    /**
     * Generates all possible successor actions and respective states that result from this state. 
     * @return an array of successor <action, state> pairs
     */
    public ActionStatePair[] successor();
    
    /**
     * Determines the cost for taking the specified action when in this state. 
     * @param action the action that takes us from this state, must be a legal action
     * @return the cost (non-negative for all legal moves, negative for illegal moves) 
     */
    public double pathcost(Action action);
    
    /**
     * This method calculates the number of misplaced tiles as its heuristic value.
     * @return Heuristic 1 value of the state
     */
    public int getH1();

    /**
     * This method calculates the sum of the distances of tiles from its goal
     * location as its heuristic value.
     * @return Heuristic 2 value of the state
     */
    public int getH2();
    
    /**
     * This method calculates the number of swaps the 'space'
     * tile makes with other tiles to reach the goal state as
     * its heuristic value.
     * @return Heuristic 3 value of the state
     */
	public int getH3();

	/**
     * This method calculates the number of tiles not in the correct column
     * plus the number of tiles not in the correct row as its heuristic value.
     * @return Heuristic 4 value of the state
     */
	public int getH4();
}

package search.npuzzle;

import java.util.ArrayList;
import java.lang.Math;

import search.Action;
import search.Node;

/**
 * @author Josh King (42344825)
 * @author Coen McClelland (42363901)
 */

public class NPuzzleSearchApp {

    /**
     * Test program for search procedures
     * @param args none interpreted as yet
     */
    public static void main(String[] args) {
        // create an initial fifteen puzzle state by first generating the goal config
        //PuzzleState myState=new PuzzleState();

    	// a size of 4 gives the 15 puzzle
    	int sizeOfPuzzle = 6;
    	
        // Create a random puzzle and memorise the puzzle state.
        NPuzzleState myState= randomPuzzle(sizeOfPuzzle, 10);
        
        int solutionDepth = 8;
        
        Action[] actionstest = solveH3A(new NPuzzleState(myState));
        while (actionstest.length != solutionDepth) {
        	myState= randomPuzzle(sizeOfPuzzle, solutionDepth);
        	actionstest = solveH3A(new NPuzzleState(myState));
        }
        System.out.println("----------------------------------------");
        

        // or "shuffle" the tiles around manually a little bit...
        /*myState=new NPuzzleState(myState, NPuzzleState.MOVE_UP);
        myState=new NPuzzleState(myState, NPuzzleState.MOVE_LEFT);
        myState=new NPuzzleState(myState, NPuzzleState.MOVE_UP);
        myState=new NPuzzleState(myState, NPuzzleState.MOVE_LEFT);
        myState=new NPuzzleState(myState, NPuzzleState.MOVE_DOWN);
        myState=new NPuzzleState(myState, NPuzzleState.MOVE_DOWN);
        myState=new NPuzzleState(myState, NPuzzleState.MOVE_RIGHT);
        myState=new NPuzzleState(myState, NPuzzleState.MOVE_UP);
        */
        NPuzzleState myState2 = new NPuzzleState(myState);

        // now perform the search from the "shuffled" initial state (fringe is empty), and
        // pull out the actions that were used to generate this goal state from the initial state
        Action[] actions1A = solveH1A(new NPuzzleState(myState));
        Action[] actions1G = solveH1G(new NPuzzleState(myState));
        Action[] actions2A = solveH2A(new NPuzzleState(myState));
        Action[] actions2G = solveH2G(new NPuzzleState(myState));
        Action[] actions3A = solveH3A(new NPuzzleState(myState));
        Action[] actions3G = solveH3G(new NPuzzleState(myState));
        Action[] actions4A = solveH4A(new NPuzzleState(myState));
        Action[] actions4G = solveH4G(new NPuzzleState(myState));

        // List the initial state and results of actions performed.
        /*System.out.println("Initial state:");
        System.out.println(myState2.toString());

        System.out.println("Solution via H1 with Greedy:-------------");
        for (int i=0; i<actions1G.length; i++) {
            System.out.println((i+1)+": "+actions1G[actions1G.length-1-i]);
            NPuzzleState.performAction(myState2,actions1G[actions1G.length-1-i]);
            System.out.println(myState2.toString());
        }

        System.out.println("Solution via H1 with A*:-------------");
        myState2 = new NPuzzleState(myState);
        for (int i=0; i<actions1A.length; i++) {
            System.out.println((i+1)+": "+actions1A[actions1A.length-1-i]);
            NPuzzleState.performAction(myState2,actions1A[actions1A.length-1-i]);
            System.out.println(myState2.toString());
        }

        System.out.println("Solution via H2 with Greedy:-------------");
        myState2 = new NPuzzleState(myState);
        for (int i=0; i<actions2G.length; i++) {
            System.out.println((i+1)+": "+actions2G[actions2G.length-1-i]);
            NPuzzleState.performAction(myState2,actions2G[actions2G.length-1-i]);
            System.out.println(myState2.toString());
        }

        System.out.println("Solution via H2 with A*:-------------");
        myState2 = new NPuzzleState(myState);
        for (int i=0; i<actions2A.length; i++) {
            System.out.println((i+1)+": "+actions2A[actions2A.length-1-i]);
            NPuzzleState.performAction(myState2,actions2A[actions2A.length-1-i]);
            System.out.println(myState2.toString());
        }
        
        System.out.println("Solution via H3 with Greedy:-------------");
        myState2 = new NPuzzleState(myState);
        for (int i=0; i<actions3G.length; i++) {
            System.out.println((i+1)+": "+actions3G[actions3G.length-1-i]);
            NPuzzleState.performAction(myState2,actions3G[actions3G.length-1-i]);
            System.out.println(myState2.toString());
        }
        
        System.out.println("Solution via H3 with A*:-------------");
        myState2 = new NPuzzleState(myState);
        for (int i=0; i<actions3A.length; i++) {
            System.out.println((i+1)+": "+actions3A[actions3A.length-1-i]);
            NPuzzleState.performAction(myState2,actions3A[actions3A.length-1-i]);
            System.out.println(myState2.toString());
        }
        
        System.out.println("Solution via H4 with Greedy:-------------");
        myState2 = new NPuzzleState(myState);
        for (int i=0; i<actions4G.length; i++) {
            System.out.println((i+1)+": "+actions4G[actions4G.length-1-i]);
            NPuzzleState.performAction(myState2,actions4G[actions4G.length-1-i]);
            System.out.println(myState2.toString());
        }
        
        System.out.println("Solution via H4 with A*:-------------");
        myState2 = new NPuzzleState(myState);
        for (int i=0; i<actions4A.length; i++) {
            System.out.println((i+1)+": "+actions4A[actions4A.length-1-i]);
            NPuzzleState.performAction(myState2,actions4A[actions4A.length-1-i]);
            System.out.println(myState2.toString());
        }*/
        
    }

	/**
     * Example solve
     * @param state initial puzzle state
     */
    public static Action[] solveTree(NPuzzleState state){
        // now perform the search from the "shuffled" initial state (fringe is empty)
        Node goal=Node.breadthFirstSearch(state, new ArrayList());
        Action[] actions=goal.getActions();
        
        return actions;
    }

    /**
     * Example solve
     * You must change this function to solve the problem with your own 
     * Greedy implementation with heuristic function 1.
     * @param state initial puzzle state
     */
    public static Action[] solveH1G(NPuzzleState state){
        // now perform the search from the "shuffled" initial state (fringe is empty)
        Node goal = Node.myHG(state, 1);
        //Node goal=Node.breadthFirstSearch(state, new ArrayList());
        Action[] actions=goal.getActions();
        
        return actions;
    }

    /**
     * Example solve
     * You must change this function to solve the problem with your own 
     * A* implementation with heuristic function 1.
     * @param state initial puzzle state
     */
    public static Action[] solveH1A(NPuzzleState state){
        // now perform the search from the "shuffled" initial state (fringe is empty)
        Node goal = Node.myHA(state, 1);
        //Node goal=Node.breadthFirstSearch(state, new ArrayList());
        Action[] actions=goal.getActions();
        
        return actions;
    }

     /**
     * Example solve
     * You must change this function to solve the problem with your own 
     * Greedy implementation with heuristic function 2.
     * @param state initial puzzle state
     */
    public static Action[] solveH2G(NPuzzleState state){
        // now perform the search from the "shuffled" initial state (fringe is empty)
        Node goal = Node.myHG(state, 2);
        //Node goal=Node.breadthFirstSearch(state, new ArrayList());
        Action[] actions=goal.getActions();
        
        return actions;
    }

     /**
     * Example solve
     * You must change this function to solve the problem with your own 
     * A* implementation with heuristic function 2.
     * @param state initial puzzle state
     */
    public static Action[] solveH2A(NPuzzleState state){
        // now perform the search from the "shuffled" initial state (fringe is empty)
        Node goal = Node.myHA(state, 2);
        //Node goal=Node.breadthFirstSearch(state, new ArrayList());
        Action[] actions=goal.getActions();
        
        return actions;
    }
    
    /**
    * Example solve
    * You must change this function to solve the problem with your own 
    * Greedy implementation with heuristic function 3.
    * @param state initial puzzle state
    */
   public static Action[] solveH3G(NPuzzleState state){
       // now perform the search from the "shuffled" initial state (fringe is empty)
       Node goal = Node.myHG(state, 3);
       //Node goal=Node.breadthFirstSearch(state, new ArrayList());
       Action[] actions=goal.getActions();
       
       return actions;
   }
   
   /**
    * Example solve
    * You must change this function to solve the problem with your own 
    * A* implementation with heuristic function 3.
    * @param state initial puzzle state
    */
   public static Action[] solveH3A(NPuzzleState state){
       // now perform the search from the "shuffled" initial state (fringe is empty)
       Node goal = Node.myHA(state, 3);
       //Node goal=Node.breadthFirstSearch(state, new ArrayList());
       Action[] actions=goal.getActions();
       
       return actions;
   }
   
   /**
    * Example solve
    * You must change this function to solve the problem with your own 
    * Greedy implementation with heuristic function 4.
    * @param state initial puzzle state
    */
   public static Action[] solveH4G(NPuzzleState state){
       // now perform the search from the "shuffled" initial state (fringe is empty)
       Node goal = Node.myHG(state, 4);
       //Node goal=Node.breadthFirstSearch(state, new ArrayList());
       Action[] actions=goal.getActions();
       
       return actions;
   }
   
   /**
    * Example solve
    * You must change this function to solve the problem with your own 
    * A* implementation with heuristic function 4.
    * @param state initial puzzle state
    */
   public static Action[] solveH4A(NPuzzleState state){
       // now perform the search from the "shuffled" initial state (fringe is empty)
       Node goal = Node.myHA(state, 4);
       //Node goal=Node.breadthFirstSearch(state, new ArrayList());
       Action[] actions=goal.getActions();
       
       return actions;
   }

    /**
     * Generate a solvable random puzzle.
     * @param maxShuffles the number of shuffles to be performed
     */
    public static NPuzzleState randomPuzzle(int sizeOfSquare, int maxShuffles) {
        NPuzzleState myState=new NPuzzleState(sizeOfSquare);
        int totalMoves = 0;
        while(totalMoves < maxShuffles){
            double r = Math.random();
            try {
                if(r < 0.25){
                    NPuzzleState.performAction(myState, NPuzzleState.MOVE_LEFT);
                    //System.out.println("left");
                } else if (r < 0.5) {
                    NPuzzleState.performAction(myState, NPuzzleState.MOVE_RIGHT);
                    //System.out.println("right");
                } else if (r < 0.75) {
                    NPuzzleState.performAction(myState, NPuzzleState.MOVE_UP);
                    //System.out.println("up");
                } else {
                    NPuzzleState.performAction(myState, NPuzzleState.MOVE_DOWN);
                    //System.out.println("down");
                }
                totalMoves++;
            }
            catch (RuntimeException e){
                ; // illegal move
            }
        }
        return myState;
    }

    /**
     * Check if the actions solve the given puzzle.
     * @param myState a problem
     * @param actions an array of Action
     */
    public static boolean checkActions(NPuzzleState myState, Action[] actions) {
        // create an initial fifteen puzzle state by first generating the goal config
        for (int i=0; i<actions.length; i++) {
            try {
                NPuzzleState.performAction(myState, actions[i]);
            }
            catch (RuntimeException e){
                return false; // illegal move
            }
        }

        // check whether myState is the goal state.
        return myState.goal();
    }
}

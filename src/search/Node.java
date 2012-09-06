package search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
//import java.util.ArrayList;

import search.npuzzle.NPuzzleState;

//import search.fifteen.PuzzleState;


/**
 * The Node is the main class for the search tree.
 * To use ordering, use the Comparator class. 
 */
public class Node {
    private final State  state;
    private final Node   parent;
    private final Action action;
    private final double cost;
    private final int    depth;

    protected Node() {
        this(null, null, null, 0);
    }
    /**
     * Create a search node with no parent node, i.e. a root node.
     * @param state the state which this node refers to, usually the initial state
     */
    public Node(State state) {
        this(state, null, null, 0);
    }
    
    /**
     * Initialise a search node.
     * @param state the state it contains
     * @param parent the parent node it derives from
     * @param action the action that we took to get here
     * @param cost the cost that has been accumulated getting here
     */
    public Node(State state, Node parent, Action action, double cost) {
        this.state=state;
        this.parent=parent;
        this.action=action;
        this.cost=cost;
        if (parent!=null)
            this.depth=parent.getDepth()+1;
        else
            this.depth=0;
    }
    
    /**
     * Expand the node into sub-nodes.
     * It uses the successor-function of the state to determine all Action/State pairs, and generates
     * a node for each.
     * @return all nodes that can be reached from this node
     */
    public Node[] expand() {
        ActionStatePair[] successors=state.successor();
        Node[] descendants=new Node[successors.length];
        for (int a=0; a<successors.length; a++) {
            Action action=successors[a].getAction();
            State child=successors[a].getState();
            descendants[a]=new Node(child, this, action, this.getCost()+state.pathcost(action));
        }
        return descendants;
    }

    /**
     * Determine all actions that were used to get to this node.
     * @return all actions that took us to this node
     */
    public Action[] getActions() {
        Action[] steps=new Action[depth];
        Node node=this;
        for (int i=0; i<depth; i++) {
            steps[i]=node.getAction();
            node=node.getParent();
        }
        return steps;
    }
    
    /**
     * @return the (last) action that took us here (from parent)
     */
    public Action getAction() {
        return action;
    }

    /**
     * @return the accumulated cost of getting here
     */
    public double getCost() {
        return cost;
    }

    /**
     * @return the tree depth of this node
     */
    public int getDepth() {
        return depth;
    }

    /**
     * @return the parent node of this node
     */
    public Node getParent() {
        return parent;
    }

    /**
     * @return the state this node contains
     */
    public State getState() {
        return state;
    }

    /**
     * Calculate the effective branching factor for a tree search.
     * Adapted from ATILLA DEMIRAY's ANSI C CODE CALCULATING EFFECTIVE BRANCHING FACTOR
     * http://utopia.poly.edu/~ademir02/soft/sources/aisx/ebfx.htm (was available 30 Aug 2005)
     * @param nNodes number of nodes
     * @param depth depth where solution was found
     * @return the estimated effective branching factor
     */
    public static double effectiveBranchingFactor(int nNodes, int depth) {
        double maxError=0.01; // the maximum error we accept from a solution 
        double delta=0.01;    // how much we change our tested ebf each iteration   
        int signOfError=0;    // the sign of the difference between N+1 and 1+b+b^2+......+b^d
        double b=0;           // search for the optimum b will start from minimum possible b
        while (true) {        // search for b starts here
            // compute the expression 1+b+b^2+......+b^d
            double sum=1;
            for (int i=1; i<depth+1; i++) {
                sum+=Math.pow(b, (double)i);
            }
            // now the tricky bit, we compute the difference between 
            // N+1 and 1+b+b^2+......+b^d, remember that we should have N+1=1+b+b^2+......+b^d
            double error=sum-(1+(double)nNodes);
            // save previous sign of error
            int signOfPreviousError=signOfError;
            // determine the new sign of error
            if (error<0.0) // negative
                signOfError=-1;
            else // positive
                signOfError=+1;
            /* if the error calculated above is greater than the maximum acceptable error, then check if sign of error
               was changed, if so that means loop missed the root that we are looking for, then decrease b by delta and 
               decrease delta to catch root in next search if sign of error wasnt change then increase 'b' by delta
               otherwise if error is smaller than the limit return the effective branch factor */
            if (Math.abs(error)>maxError) { // error is big
                if (signOfPreviousError==signOfError || signOfPreviousError==0) {  
                    b+=delta;   // taking another step towards solution
                } else {        // change of sign which means that we jumped over the minima
                    b-=delta;   // go back
                    delta/=2;   // take smaller steps
                    signOfError=signOfPreviousError;  // undo change of sign
                }
            } else // error is small, let's return current estimate
                return(b);
        }
    }
    
    /**
     * Executes a breadth-first search from an initial state and with a queue/open list/fringe. 
     * @param initial the initial, starting state
     * @param fringe the list of all nodes that should be expanded, 
     * usually empty.
     * @return the solution node if one is found, null otherwise
     */
    public static Node breadthFirstSearch(State initial, List<Node> fringe) {
    //public static Node breadthFirstSearch(State initial, List fringe) {
        // add the initial state to the fringe
        fringe.add(new Node(initial)); 
        // loop through all nodes in the fringe
        while (!fringe.isEmpty()) { // test if fringe is empty, if yes "failure"
            // poll the first node in the list
            Node head=(Node)fringe.get(0);
            fringe.remove(0);
            // pull out the state in the node
            State state=head.getState();
            // examine it to see if it is a goal state
            if (state.goal()) {
                return head; // if goal state then return this node
            }
            // consider using a "closed list" for visited states (avoiding repeated states)
            
            // expand the node, and add all new states to the fringe
            fringe.addAll(Arrays.asList(head.expand())); // a list adds new nodes to the end of the queue
        }
        return null;
    }
    
    /**
     * myH1G searches the tree with the Greedy algorithm using heuristic 1 (# misplaced
     * tiles).
     * @param initial initial state of the puzzle
     * @return solution if one is found, null otherwise
     */
	public static Node myH1G(State initial) {
		List<State> repeated = new ArrayList<State>();
		State s = initial;
		Node node = new Node(s);
		MapQueue queue = new MapQueue();
		
		// Check if state is the goal state //
		int n = 0;
		while (!s.goal()) {			
			// If not, expand children //
			Node[] children = node.expand();
			for( Node c : children) {
				State cs = c.getState();
				int h1 = cs.getH1();
				
				// Add children to queue //
				queue.put(h1, c);
				n++;
			}
			
			// Pick next closest node to goal + check for repeated states //
			boolean found = true;
			while (found) {
				found = false;
				node = queue.getNext();
				s = node.getState();
				for (int i = 0; i < repeated.size(); i++) {
					if (repeated.get(i).equals(s)) {
						found = true;
						break;
					}
				}
			}
			repeated.add(s);
		}
		// Return goal state node //
		//System.out.println("H1G nodes checked: " + n);
		System.out.println("H1G\t" + effectiveBranchingFactor(n, node.getDepth()));
		return node;
	}
	
	/**
     * myH1A searches the tree with the A* algorithm using heuristic 1 (# misplaced
     * tiles).
     * @param initial initial state of the puzzle
     * @return solution if one is found, null otherwise
     */
	public static Node myH1A(State initial) {
		State s = initial;
		Node node = new Node(s);
		MapQueue queue = new MapQueue();
		
		// Check if state is the goal state //
		int n = 0;
		while (!s.goal()) {			
			// If not, expand children //
			Node[] children = node.expand();
			for( Node c : children) {
				State cs = c.getState();
				int h1 = cs.getH1();
				double cost = c.getCost() + s.pathcost(c.getAction());
				
				// Add children to queue //
				queue.put((int) (h1 + cost), c);
				n++;
			}
			
			// Pick next closest node to goal //
			node = queue.getNext();
			s = node.getState();
		}
		// Return goal state node //
		//System.out.println("H1G nodes checked: " + n);
		System.out.println("H1A\t" + effectiveBranchingFactor(n, node.getDepth()));
		return node;
	}
	
	public static Node myH2G(State initial) {
		List<State> repeated = new ArrayList<State>();
		State s = initial;
		Node node = new Node(s);
		MapQueue queue = new MapQueue();
		
		// Check if state is the goal state //
		int n = 0;
		while (!s.goal()) {			
			// If not, expand children //
			Node[] children = node.expand();
			for( Node c : children) {
				State cs = c.getState();
				int h2 = cs.getH2();

				// Add children to queue //
				queue.put(h2, c);
				n++;
			}
			
			// Pick next closest node to goal + check for repeated states //
			boolean found = true;
			while (found) {
				found = false;
				node = queue.getNext();
				s = node.getState();
				for (int i = 0; i < repeated.size(); i++) {
					if (repeated.get(i).equals(s)) {
						found = true;
						break;
					}
				}
			}
			repeated.add(s);
		}
		// Return goal state node //
		//System.out.println("H2G nodes checked: " + n);
		System.out.println("H2G\t" + effectiveBranchingFactor(n, node.getDepth()));
		return node;
	}

	public static Node myH2A(State initial) {
		State s = initial;
		Node node = new Node(s);
		MapQueue queue = new MapQueue();
		
		// Check if state is the goal state //
		int n = 0;
		while (!s.goal()) {			
			// If not, expand children //
			Node[] children = node.expand();
			for( Node c : children) {
				State cs = c.getState();
				int h2 = cs.getH2();
				double cost = c.getCost() + s.pathcost(c.getAction());

				// Add children to queue //
				queue.put((int) (h2 + cost), c);
				n++;
			}
			
			// Pick next closest node to goal //
			node = queue.getNext();
			s = node.getState();
		}
		// Return goal state node //
		//System.out.println("H2A nodes checked: " + n);
		System.out.println("H2A\t" + effectiveBranchingFactor(n, node.getDepth()));
		return node;
	}
	
	public static Node myH3G(State initial) {
		List<State> repeated = new ArrayList<State>();
		State s = initial;
		Node node = new Node(s);
		MapQueue queue = new MapQueue();
		
		// Check if state is the goal state //
		int n = 0;
		while (!s.goal()) {			
			// If not, expand children //
			Node[] children = node.expand();
			for( Node c : children) {
				State cs = c.getState();
				int h3 = cs.getH3();

				// Add children to queue //
				queue.put(h3, c);
				n++;
			}
			
			// Pick next closest node to goal + check for repeated states //
			boolean found = true;
			while (found) {
				found = false;
				node = queue.getNext();
				s = node.getState();
				for (int i = 0; i < repeated.size(); i++) {
					if (repeated.get(i).equals(s)) {
						found = true;
						break;
					}
				}
			}
			repeated.add(s);
		}
		// Return goal state node //
		//System.out.println("H3G nodes checked: " + n);
		System.out.println("H3G\t" + effectiveBranchingFactor(n, node.getDepth()));
		return node;
	}
	
	public static Node myH3A(State initial) {
		State s = initial;
		Node node = new Node(s);
		MapQueue queue = new MapQueue();
		
		// Check if state is the goal state //
		int n = 0;
		while (!s.goal()) {			
			// If not, expand children //
			Node[] children = node.expand();
			for( Node c : children) {
				State cs = c.getState();
				int h3 = cs.getH3();
				double cost = c.getCost() + s.pathcost(c.getAction());

				// Add children to queue //
				queue.put((int) (h3 + cost), c);
				n++;
			}
			
			// Pick next closest node to goal //
			node = queue.getNext();
			s = node.getState();
		}
		// Return goal state node //
		//System.out.println("H3A nodes checked: " + n);
		System.out.println("H3A\t" + effectiveBranchingFactor(n, node.getDepth()));
		return node;
	}
	
	public static Node myH4G(State initial) {
		List<State> repeated = new ArrayList<State>();
		State s = initial;
		Node node = new Node(s);
		MapQueue queue = new MapQueue();
		
		// Check if state is the goal state //
		int n = 0;
		while (!s.goal()) {			
			// If not, expand children //
			Node[] children = node.expand();
			for( Node c : children) {
				State cs = c.getState();
				int h4 = cs.getH4();

				// Add children to queue //
				queue.put(h4, c);
				n++;
			}
			
			// Pick next closest node to goal + check for repeated states //
			boolean found = true;
			while (found) {
				found = false;
				node = queue.getNext();
				s = node.getState();
				for (int i = 0; i < repeated.size(); i++) {
					if (repeated.get(i).equals(s)) {
						found = true;
						break;
					}
				}
			}
			repeated.add(s);
		}
		// Return goal state node //
		//System.out.println("H4G nodes checked: " + n);
		System.out.println("H4G\t" + effectiveBranchingFactor(n, node.getDepth()));
		return node;
	}
	
	public static Node myH4A(State initial) {
		State s = initial;
		Node node = new Node(s);
		MapQueue queue = new MapQueue();
		
		// Check if state is the goal state //
		int n = 0;
		while (!s.goal()) {			
			// If not, expand children //
			Node[] children = node.expand();
			for( Node c : children) {
				State cs = c.getState();
				int h4 = cs.getH4();
				double cost = c.getCost() + s.pathcost(c.getAction());

				// Add children to queue //
				queue.put((int) (h4 + cost), c);
				n++;
			}
			
			// Pick next closest node to goal //
			node = queue.getNext();
			s = node.getState();
		}
		// Return goal state node //
		//System.out.println("H4A nodes checked: " + n);
		System.out.println("H4A\t" + effectiveBranchingFactor(n, node.getDepth()));
		return node;
	}

}

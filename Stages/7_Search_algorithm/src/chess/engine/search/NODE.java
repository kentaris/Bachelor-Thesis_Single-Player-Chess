package chess.engine.search;

import java.util.Comparator;

public class NODE implements Comparable<NODE> {
    public NODE PARENT; //the parent node from which the current node was expanded (predecessor).
    //public String ACTION; //the action that was used on the parent’s state to generate the current node (the knight movement in our running example).
    public STATE STATE; //the state the node is representing (in our case bitmaps)
    public int PATH_COST; //the total cost from the initial state up to the current node (also called the g-value)
    public int HEURISTIC_VALUE;

    public NODE(NODE parent, long[] state, long difference, int path_cost, boolean wTurn, int heuristic) {
        this.PARENT = parent;
        this.STATE = new STATE(state, difference, wTurn);
        this.PATH_COST = path_cost;
        this.HEURISTIC_VALUE = heuristic;
    }

    public NODE() {
    }

    public int compareTo(NODE o) {
        if (this.HEURISTIC_VALUE < o.HEURISTIC_VALUE) {
            return -1;
        } else if (this.HEURISTIC_VALUE > o.HEURISTIC_VALUE) {
            return 1;
        } else return 0;
    }
}
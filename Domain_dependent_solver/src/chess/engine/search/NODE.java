package chess.engine.search;

public class NODE implements Comparable<NODE> {
    public NODE PARENT; //the parent node from which the current node was expanded (predecessor).
    //public String ACTION; //the action that was used on the parent’s state to generate the current node (the knight movement in our running example).
    public STATE STATE; //the state the node is representing (in our case bitmaps)
    public Integer[] MovedCapturedCastling; //only used to create the path at the end (saving if piece was captured etc.)

    public NODE(NODE parent, long[] state, long difference, int path_cost, boolean wTurn, int heuristic) {
        this.PARENT = parent;
        this.STATE = new STATE(state, difference, wTurn, path_cost, heuristic);
    }

    public NODE() {
    }

    public int compareTo(NODE o) {
        //System.out.println("\n:"+this.HEURISTIC_VALUE);
        //System.out.println(":"+o.HEURISTIC_VALUE);
        //System.exit(6);
        if (this.STATE.heuristic_value < o.STATE.heuristic_value) {
            return  -1;
        } else if (this.STATE.heuristic_value > o.STATE.heuristic_value) {
            return 1;
        } else return 0;
    }
}
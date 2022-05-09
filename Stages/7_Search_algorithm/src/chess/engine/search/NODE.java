package chess.engine.search;

public class NODE{
    public NODE PARENT; //the parent node from which the current node was expanded (predecessor).
    //public String ACTION; //the action that was used on the parent’s state to generate the current node (the knight movement in our running example).
    public STATE STATE; //the state the node is representing (in our case bitmaps)
    public int PATH_COST; //the total cost from the initial state up to the current node (also called the g-value)

    public NODE(NODE parent, long[] state, long difference, int path_cost, boolean wTurn) {
        this.PARENT = parent;
        this.STATE = new STATE(state, difference, wTurn);
        this.PATH_COST = path_cost;
    }
}
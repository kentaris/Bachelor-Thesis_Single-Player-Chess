package chess.engine.search;

public class STATE {
    public long[] state;
    public boolean wTurn;
    public long difference;
    public int path_cost;
    public int heuristic_value;

    public STATE(long[] state, long difference, boolean wTurn, int path_cost, int heuristic_value) {
        this.state = state;
        this.difference = difference;
        this.wTurn = wTurn;
        this.path_cost = path_cost;
        this.heuristic_value = heuristic_value;
    }
}
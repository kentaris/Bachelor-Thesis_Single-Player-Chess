package chess.engine.search;

import java.util.Arrays;

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

    @Override
    public boolean equals(Object o) {
        //two_bitmaps_to_chessboard((long[])o,this.state);
        //System.out.println(this.state.hashCode()+" "+o);
        if (this.state == o) {
            //System.out.println("true");
            return true;
        }else if (o == null || getClass() != o.getClass()) {
            //System.out.println("false");
            return false;
        }else {
            //if(Arrays.equals(this.state, ((STATE) o).state)) System.out.println("True");
            //else System.out.println("False");
            return Arrays.equals(this.state, ((STATE) o).state);
        }
    }

    @Override
    public int hashCode() {
        //System.out.println(this.state.hashCode());
        return this.state.hashCode();
    }
}
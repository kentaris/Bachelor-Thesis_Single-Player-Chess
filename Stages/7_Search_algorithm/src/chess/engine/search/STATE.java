package chess.engine.search;

public class STATE {
    public long[] state;
    public boolean wTurn;
    public long difference;

    public STATE(long[] state, long difference, boolean wTurn) {
        this.state = state;
        this.difference = difference;
        this.wTurn = wTurn;
    }
}
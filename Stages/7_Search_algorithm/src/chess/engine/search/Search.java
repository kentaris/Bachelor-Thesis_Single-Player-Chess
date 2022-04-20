package chess.engine.search;

import chess.engine.board.Bitboards;

import static chess.engine.board.Bitboards.*;
import static chess.engine.figures.Moves.initiate_moves;


public class Search {
    public static final int board_size = 8;

    public static void main(String[] args) {
        long t1 = System.currentTimeMillis();
        String FEN = "r1pp4/1Pp5/1R6/4n3/3K4/2Bbn1k1/4QP2/4R1B1";
        initiate_boards(FEN);
        initiate_moves();
        long t2 = System.currentTimeMillis();
        System.out.println(String.format("\n\u001B[33m[%sms execution time]\u001B[0m", t2 - t1));
    }
}

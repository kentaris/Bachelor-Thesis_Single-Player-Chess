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
        /*System.out.println(long_to_bitstring(WHITEPIECES));
        long bit = setBit(13);
        System.out.println(long_to_bitstring(bit));
        System.out.println(((WHITEPIECES >> 2) & 1));
        System.out.println(long_to_bitstring(WHITEPIECES>>12));
        if (((WHITEPIECES>>12) & 1) != 0){ //collision with piece //TODO
            System.out.println("test");
        }*/
        long t2 = System.currentTimeMillis();
        System.out.println(String.format("\n\u001B[33m[%sms execution time]\u001B[0m", t2 - t1));
    }
}

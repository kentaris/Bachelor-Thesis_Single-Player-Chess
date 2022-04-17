package chess.engine.search;

import chess.engine.board.Bitboards;


public class Search {
    public static final int board_size = 8;

    public static void main(String[] args) {
        long t1 = System.currentTimeMillis();
        String FEN = "p7/p7/p7/4n3/3K4/2BbN1k1/4Q3/4R2p";
        System.out.println(FEN);
        initiate_boards(FEN);
        long t2 = System.currentTimeMillis();
        System.out.println(String.format("\n\u001B[33m[%sms execution time]\u001B[0m",t2-t1));
    }

    public static void initiate_boards(String FEN) {
        Bitboards.map(FEN);
        Bitboards.files();
        Bitboards.ranks();
        Bitboards.KQ_side();
        Bitboards.colors();
        Bitboards.empty();
    }
}

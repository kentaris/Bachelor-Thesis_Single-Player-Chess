package chess.engine.search;

import static chess.engine.fen.Decoder.FEN_decodeTo_64String;
import static chess.engine.board.Bitboards.generate_bitboards;
import static chess.engine.figures.Figures.get_idx;

public class Search {
    public static int board_size = 8;

    public static void main(String[] args) {
        String[] board = FEN_decodeTo_64String("8/8/p7/4n3/3K4/2BbN1k1/4Q3/4R3", board_size);
        long[] bitmaps = generate_bitboards(board);
        for (long map: bitmaps){
            System.out.println(map);
        }

    }
}

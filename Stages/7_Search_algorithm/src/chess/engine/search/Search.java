package chess.engine.search;

import java.util.Arrays;

import static chess.engine.board.Bitboards.generate_bitboards;
import static chess.engine.fen.Decoder.FEN_decodeTo_64String;
import static chess.engine.fen.Decoder.FEN_decodeTo_Board;


public class Search {
    public static final int board_size = 8;

    public static void main(String[] args) {
        String FEN = "8/8/p7/4n3/3K4/2BbN1k1/4Q3/4R3";
        System.out.println(FEN);
        String[] board = FEN_decodeTo_64String(FEN, board_size);
        String[][] board2 = FEN_decodeTo_Board(FEN, board_size);
        for (int i = 0; i < board_size; i++) {
            System.out.println(Arrays.toString(board2[i]));
        }
        System.out.println();
        long[] bitmaps = generate_bitboards(board);

        /*for (long map: bitmaps){
            System.out.println(map);
        }*/
    }
}

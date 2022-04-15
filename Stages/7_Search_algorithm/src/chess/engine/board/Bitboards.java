package chess.engine.board;

import java.util.Arrays;

import static chess.engine.figures.Figures.get_key;
import static chess.engine.search.Search.board_size;

public class Bitboards {
    public static long[] generate_bitboards(String[] stringMaps) {
        long[] bitmaps = new long[12]; //12 maps for 2*6 chess figures (black and white) -->long so board has 64bits available
        for (int i = 0; i < bitmaps.length; i++) {
            bitmaps[i] = Long.parseLong(stringMaps[i], 2);
        }
        return bitmaps;
    }

    public static void bitmaps_to_chessboard(long[] bitmaps) {

        String[][] board = new String[board_size][board_size];
        for (int pos = 0; pos < (board_size * board_size); pos++) {
            board[pos / board_size][pos % board_size] = " "; //initialize empty board with placeholder
        }
        for (int fig = 0; fig < 12; fig++) {
            //System.out.println(fig+" "+get_key(fig).toString()+ " "+Long.toBinaryString((bitmaps[fig])));
            for (int i = 0; i < (board_size * board_size); i++) {
                if (((bitmaps[fig] >> i) & 1) == 1) { //wherever we find 1's in the binary code of the current bitmap...
                    board[i / board_size][i % board_size] = get_key(fig).toString(); // ...place the character of the current bitmap to the board
                }
            }
        }
        for (int i = 0; i < board_size; i++) {
            System.out.println(Arrays.toString(board[i]));
        }
    }
}

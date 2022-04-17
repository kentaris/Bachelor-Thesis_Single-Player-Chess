package chess.engine.board;

import java.util.Arrays;

import static chess.engine.fen.Decoder.FEN_decodeTo_64String;
import static chess.engine.figures.Figures.gtfig;
import static chess.engine.search.Search.board_size;
import static java.lang.Long.parseUnsignedLong;

public class Bitboards {
    static long[] bitmaps = new long[12]; //12 maps for 2*6 chess figures (black and white) -->long so board has 64bits available
    static long[] FILES = new long[board_size];
    static long[] RANKS = new long[board_size];
    static long KINGSIDE;
    static long QUEENSIDE;
    static long WHITEPIECES; //remove king to avoid legal move?
    static long BLACKPIECES; //remove king to avoid legal move?
    static long EMPTY;

    public static long[] generate_bitboards(String[] stringMaps) {
        for (int i = 0; i < bitmaps.length; i++) {
            //System.out.println(stringMaps[i]);
            bitmaps[i] = Long.parseUnsignedLong(stringMaps[i], 2);
        }
        return bitmaps;
    }

    public static void map(String FEN) {
        String[] board = FEN_decodeTo_64String(FEN, board_size);
        long[] bitmaps = generate_bitboards(board);
        bitmaps_to_chessboard(bitmaps);
        /*for (long map: bitmaps){
            System.out.println(map);
        }*/
        //System.out.println(long_to_bitstring(bitmaps[gtidx('p')]));
    }

    public static void bitmaps_to_chessboard(long[] bitmaps) {
        String[][] board = new String[board_size][board_size];
        for (int pos = 0; pos < (board_size * board_size); pos++) {
            board[pos / board_size][pos % board_size] = " "; //initialize empty board with placeholder
        }
        for (int fig = 0; fig < bitmaps.length; fig++) {
            //System.out.println(fig+" "+get_key(fig).toString()+ " "+Long.toBinaryString((bitmaps[fig])));
            for (int i = 0; i < (board_size * board_size); i++) {
                //System.out.println(bitmaps[fig]);
                if (((bitmaps[fig] >> i) & 1) == 1) { //wherever we find 1's in the binary code of the current bitmap...
                    board[i / board_size][i % board_size] = gtfig(fig).toString(); // ...place the character of the current bitmap to the board
                }
            }
        }
        for (int i = 0; i < board_size; i++) {
            System.out.println(Arrays.toString(board[i]));
        }
    }

    public static void bitmap_to_chessboard(long bitmap) {
        String[][] board = new String[board_size][board_size];
        for (int pos = 0; pos < (board_size * board_size); pos++) {
            board[pos / board_size][pos % board_size] = " "; //initialize empty board with placeholder
        }
        //System.out.println(fig+" "+get_key(fig).toString()+ " "+Long.toBinaryString((bitmaps[fig])));
        for (int i = 0; i < (board_size * board_size); i++) {
            //System.out.println(bitmaps[fig]);
            if (((bitmap >> i) & 1) == 1) { //wherever we find 1's in the binary code of the current bitmap...
                board[i / board_size][i % board_size] = "1"; // ...place the character of the current bitmap to the board
            }
        }
        for (int i = 0; i < board_size; i++) {
            System.out.println(Arrays.toString(board[i]));
        }
    }

    public static String long_to_bitstring(long l) {
        StringBuilder builder = new StringBuilder(board_size * board_size);
        String str = Long.toBinaryString((long) l);
        String mask = "0".repeat(board_size * board_size);
        return (mask.substring(0, mask.length() - str.length()) + str);
    }
    public static void empty(){
        EMPTY=~BLACKPIECES+~WHITEPIECES;
        //bitmap_to_chessboard(EMPTY);
    }

    public static void colors() {
        for (int fig = 0; fig < 6; fig++) { //black figures
            BLACKPIECES += bitmaps[fig];
        }
        System.out.println();
        for (int fig = 6; fig < 12; fig++) { //white figures
            WHITEPIECES += bitmaps[fig];
        }
        //bitmap_to_chessboard(WHITEPIECES);
    }

    public static void KQ_side() {
        StringBuilder builder = new StringBuilder(board_size * board_size);
        for (int row = 0; row < board_size; row++) {
            builder.append("0".repeat(board_size / 2));
            builder.append("1".repeat(board_size - (board_size / 2)));
        }
        KINGSIDE = parseUnsignedLong(builder.reverse().toString(), 2); //the .reverse() function changes the builder variable permanently even though we don't set it here...
        QUEENSIDE = parseUnsignedLong(builder.reverse().toString(), 2); //...so we need to apply it again to get the actual reverse (otherwise it'll be the same). That's why this works.
        //bitmap_to_chessboard(QUEENSIDE);
    }

    public static void files() {
        for (int i = 0; i < board_size; i++) { //bitmap for every file
            StringBuilder builder = new StringBuilder(board_size * board_size);
            for (int row = 0; row < board_size; row++) { //fill rows of the board...
                //...with according 8 long bit string (to build 64 bit long string)
                for (int space = 0; space < i; space++) { //spaces BEFORE we mark the place in the row to represent the file
                    builder.append("0");
                }
                builder.append("1");
                for (int space = i + 1; space < board_size; space++) { // spaces AFTER we mark the place in the row to represent the file
                    builder.append("0");
                }
            }
            FILES[i] = parseUnsignedLong(builder.reverse().toString(), 2);
            //bitmap_to_chessboard(FILES[i]);
        }
    }

    public static void ranks() {
        for (int i = 0; i < board_size; i++) { //bitmap for every rank
            StringBuilder builder = new StringBuilder(board_size * board_size);
            for (int file = 0; file < board_size; file++) {
                if (i == file) {
                    builder.append("1".repeat(board_size)); //create row with 1's
                } else {
                    builder.append("0".repeat(board_size)); //create row with 1's
                }
            }
            RANKS[i] = parseUnsignedLong(builder.toString(), 2);
            //bitmap_to_chessboard(RANKS[i]);
        }
    }
}

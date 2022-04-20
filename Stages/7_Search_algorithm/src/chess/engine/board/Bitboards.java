package chess.engine.board;

import java.util.Arrays;

import static chess.engine.fen.Decoder.FEN_decodeTo_64String;
import static chess.engine.figures.Figures.gtfig;
import static chess.engine.figures.Figures.gtidx;
import static chess.engine.search.Search.board_size;
import static java.lang.Long.parseUnsignedLong;

public class Bitboards {
    public static long[] bitmaps = new long[12]; //12 maps for 2*6 chess figures (black and white) -->long so board has 64bits available
    public static long[] FILES = new long[board_size];
    public static long[] RANKS = new long[board_size];
    public static long KINGSIDE;
    public static long QUEENSIDE;
    public static long WHITEPIECES; //remove king to avoid legal move?
    public static long BLACKPIECES; //remove king to avoid legal move?
    public static long EMPTY;

    public static long[] generate_bitboards(String[] stringMaps) {
        for (int i = 0; i < bitmaps.length; i++) {
            //System.out.println(stringMaps[i]);
            bitmaps[i] = Long.parseUnsignedLong(stringMaps[i], 2);
        }
        return bitmaps;
    }

    public static void initiate_FEN_to_chessboard(String FEN) {
        System.out.println(FEN);
        String[] board = FEN_decodeTo_64String(FEN, board_size);
        long[] bitmaps = generate_bitboards(board);
        bitmaps_to_chessboard(bitmaps);
        //System.out.println(long_to_bitstring(bitmaps[gtidx('p')]));
    }

    public static void initiate_custom_chessBoard() {
        Character board[][] = {
                {' ', ' ', ' ', ' ', 'p', ' ', ' ', ' '}, //left: square 0 & 7,0
                {' ', ' ', ' ', ' ', 'P', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', 'b', ' ', ' ', 'r', 'P', 'R', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', 'P', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}}; //right: square 63 & 0,7
        arrayToBitboards(board);
        bitmaps_to_chessboard(bitmaps);
    }

    public static void initiate_boards(String FEN) {
        //initiate_FEN_to_chessboard(FEN);
        initiate_custom_chessBoard();
        files();
        ranks();
        KQ_side();
        colors();
        empty();
    }

    public static void arrayToBitboards(Character[][] board) {
        for (int i = 0; i < 64; i++) {
            if (board[i / 8][i % 8] != ' ') {
                String bit = "0".repeat((board_size * board_size) - (i + 1)) + "1" + "0".repeat(i); //build bit string where 1 bit is at pos i and rest is 0... but we need to reverse it like we did in the FEN decoder.
                bitmaps[gtidx(board[i / 8][i % 8])] += Long.parseUnsignedLong((bit), 2);
            }
        }
    }

    public static void bitmaps_to_chessboard(long[] bitmaps) {
        String[][] board = new String[board_size][board_size];
        for (int pos = 0; pos < (board_size * board_size); pos++) {
            board[pos / board_size][pos % board_size] = " "; //initialize empty board with placeholder
        }
        /*for (int i = 0; i < 64; i++) {
            if (i % board_size==0 && i / board_size!=0){ //new rank
                System.out.println();
            }
            System.out.print(" ("+i / board_size+","+i % board_size+") || ");
        }*/
        System.out.println();
        for (int fig = 0; fig < bitmaps.length; fig++) {
            //System.out.println(fig+" "+get_key(fig).toString()+ " "+Long.toBinaryString((bitmaps[fig])));
            for (int i = 0; i < (board_size * board_size); i++) {
                //System.out.println(bitmaps[fig]);
                if (((bitmaps[fig] >> i) & 1) == 1) { //wherever we find 1's in the binary code of the current bitmap...
                    board[i / board_size][i % board_size] = gtfig(fig).toString(); // ...place the character of the current bitmap to the board
                    /*System.out.println("rank: "+i / board_size+", file: "+i % board_size);
                    for(int j = 0; j<8;j++){
                        for (int m=0;m<8;m++) {
                            System.out.print(board[j][m]);
                            System.out.print("("+j+","+m+") || ");
                        }
                        System.out.print('|');
                        System.out.println();
                    }*/
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
        System.out.println();
        for (int i = 0; i < board_size; i++) {
            System.out.println(Arrays.toString(board[i]));
        }
    }

    public static long[] get_single_figure_boards(long bitboard) {
        /*returns single boards for the given figure. every board has exactly one figure on it, so we can select them easily.*/
        //long figures = bitmaps[gtidx(fig)];
        long figures = bitboard;
        int n = Long.bitCount(figures);
        long[] boards = new long[n];
        for (int i = 0; i < n; i++) {
            long highestBit = Long.highestOneBit(figures);
            figures -= highestBit;
            //bitmap_to_chessboard(highestBit);
            boards[i] = highestBit;
        }
        return boards;
    }

    public static String long_to_bitstring(long l) {
        String str = Long.toBinaryString(l);
        String mask = "0".repeat(board_size * board_size);
        String s = mask.substring(0, mask.length() - str.length()) + str;
        //System.out.println(s);
        return s;
    }

    public static void empty() {
        EMPTY = ~(BLACKPIECES + WHITEPIECES);
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
                builder.append("0".repeat(i)); //spaces BEFORE we mark the place in the row to represent the file
                builder.append("1");
                builder.append("0".repeat(board_size - (i + 1))); // spaces AFTER we mark the place in the row to represent the file
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

    public static Integer get_squareIndex_of_figure(long bitmap) {
        /*returns a number from 0 to 63 representing the square the given bitmap bit (with one figure on it) is on. The top left square is 0, the bottom right square is 63.*/
        int index = 0;
        while (bitmap != 1) { //shift until bit is == 000....001 (which is the 64'th square)
            bitmap >>>= 1; // shift the bit to the right (up the board and left)
            index++; // so we are now looking at the next index.
        }
        return index;
    }

    public static Integer[] idx_to_fileRank(Integer idx) {
        /*assigning rows and files.*/
        Integer[] file_row = {((idx / board_size)), ((idx % board_size))}; //this corresponds to the java matrix[x][y]-coordinates not the chessboard-square-coordinates.
        return file_row;
    }

    public static long hor_ver_bitboard(long bitboard) {
        /*creates a bitmap mask which marks the row and file up and down to mark the spots where a rook (or queen) can go to possibly.*/
        long hor_ver = 0;
        long[] figures = get_single_figure_boards(bitboard);
        for (int i = 0; i < figures.length; i++) { //loop over single figures
            Integer idx = get_squareIndex_of_figure(figures[i]);
            Integer[] file_row = idx_to_fileRank(idx);
            Integer file = file_row[1];
            Integer rank = file_row[0];
            for (int k = 1; k < file + 1; k++) {//left
                if (k==0){ //collision with piece //TODO
                    break;
                }
                hor_ver |= figures[i] >>> k;
            }
            for (int k = 1; k < board_size - file; k++) {//right
                hor_ver |= figures[i] << k;
            }
            for (int k = 1; k < rank + 1; k++) {//up
                hor_ver |= figures[i] >>> k * board_size;
            }
            for (int k = 1; k < board_size - rank; k++) {//down
                hor_ver |= figures[i] << k * board_size;
            }
        }
        //bitmap_to_chessboard(hor_ver);
        return hor_ver;
    }

    public static long diag_bitboard(long bitboard) {
        long diag = 0;
        long[] figures = get_single_figure_boards(bitboard); //separate figures into separate boards
        for (int i = 0; i < figures.length; i++) { //loop over single figures
            Integer idx = get_squareIndex_of_figure(figures[i]);
            Integer[] file_row = idx_to_fileRank(idx);
            Integer file = file_row[1];
            Integer rank = file_row[0];
            for (int k = 1; k < file + 1; k++) {//left down
                diag |= figures[i] << k * 7;
            }
            for (int k = 1; k < board_size - file; k++) {//right up
                diag |= figures[i] >>> k * 7;
            }
            for (int k = 1; (k < rank + 1) & (k < file + 1); k++) {//left up
                diag |= figures[i] >>> k * (board_size + 1);
            }
            for (int k = 1; k < (board_size - rank) & (k < board_size - file); k++) {//right down
                diag |= figures[i] << k * (board_size + 1);
            }
        }
        //bitmap_to_chessboard(diag);
        return diag;
    }
    public static long collisions(long bitboard){

        return -1;
    }
}

package chess.engine.board;

import chess.engine.figures.Moves;

import java.util.Arrays;

import static chess.engine.fen.Decoder.FEN_decodeTo_64String;
import static chess.engine.figures.Figures.gtfig;
import static chess.engine.figures.Figures.gtidx;
import static chess.engine.search.Problem.board_size;
import static java.lang.Long.parseUnsignedLong;
import static java.util.Objects.isNull;

public class Bitboards {
    public static long[] bitmaps = new long[12]; //12 maps for 2*6 chess figures (black and white) -->long so board has 64bits available
    public static long[] FILES = new long[board_size];
    public static long[] RANKS = new long[board_size];
    //static long[] DIAGS = {1L, 102L, 10204L, 1020408L, 102040810L, 10204081020L, 1020408102040L, 102040810204080L, 204081020408000L, 408102040800000L, 810204080000000L, 1020408000000000L, 2040800000000000L, 4080000000000000L, 8000000000000000L};
    //static long[] ADIAGS = {80L, 8040L, 804020L, 80402010L, 8040201008L, 804020100804L, 80402010080402L, 8040201008040201L, 4020100804020100L, 2010080402010000L, 1008040201000000L, 804020100000000L, 402010000000000L, 201000000000000L, 100000000000000L};
    public static long KINGSIDE;
    public static long QUEENSIDE;
    public static long WHITEPIECES; //remove king to avoid legal move?
    public static long BLACKPIECES; //remove king to avoid legal move?
    public static long EMPTY;

    public static long[] generate_bitboards(String[] stringMaps) {
        /*Part of the fen-code translation process. it takes the strings and makes longs out of it.*/
        for (int i = 0; i < bitmaps.length; i++) {
            //System.out.println(stringMaps[i]);
            bitmaps[i] = Long.parseUnsignedLong(stringMaps[i], 2);
        }
        return bitmaps;
    }

    public static long[] return_bitboards(String[] stringMaps) {
        /*Part of the fen-code translation process. it takes the strings and makes longs out of it.*/
        long[] Bitmaps = new long[12];
        for (int i = 0; i < Bitmaps.length; i++) {
            Bitmaps[i] = Long.parseUnsignedLong(stringMaps[i], 2);
        }
        return Bitmaps;
    }

    public static void set_bitboards(long[] bitboards){
        bitmaps=bitboards;
    }

    public static void initiate_FEN_to_chessboard(String FEN) {
        /*translates the fen code into the 12 bitmaps*/
        System.out.println(FEN);
        String[] board = FEN_decodeTo_64String(FEN, board_size);
        long[] bitmaps = generate_bitboards(board);
        bitmaps_to_chessboard(bitmaps);
        //System.out.println(long_to_bitstring(bitmaps[gtidx('p')]));
    }

    public static long[] FEN_to_chessboard(String FEN) {
        /*translates the fen code into the 12 bitmaps*/
        String[] board = FEN_decodeTo_64String(FEN, board_size);
        long[] Bitmaps = return_bitboards(board);
        return Bitmaps;
    }

    public static void initiate_custom_chessBoard(Character[][] board) {
        /*creates a bitboard from a given visual representaion*/
        /*This method is here for convenience only since it is somethimes timeconsuming and error-prone to come up with the exact FEN code for a given chess position.*/
        /*{'r','n','b','q','k','b','n','r'},
        {'p','p','p','p','p','p','p',' '},
        {' ',' ',' ',' ',' ',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ','Q'},
        {' ',' ',' ',' ','P',' ',' ',' '},
        {' ',' ',' ',' ',' ',' ',' ',' '},
        {'P','P','P','P',' ','P','P','P'},
        {'R','N','B',' ','K','B','N','R'}*//*
        Character[][] board = {
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ','p',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' '}
        }; //right: square index 63  &  0,7 (file,row)*/
        arrayToBitboards(board);
        //bitmaps_to_chessboard(bitmaps);
    }

    public static void arrayToBitboards(Character[][] board) {
        /*used for translating the given board in the method 'initiate_custom_chessBoard()' to the 12 bitmaps.*/
        for (int i = 0; i < 64; i++) {
            if (board[i / 8][i % 8] != ' ') {
                String bit = "0".repeat((board_size * board_size) - (i + 1)) + "1" + "0".repeat(i); //build bit string where 1 bit is at pos i and rest is 0... but we need to reverse it like we did in the FEN decoder.
                bitmaps[gtidx(board[i / 8][i % 8])] |= Long.parseUnsignedLong((bit), 2); //add it to the correct bitmap
            }
        }
    }

    public static long[] return_arrayToBitboards(Character[][] board) {
        /*used for translating the given board in the method 'initiate_custom_chessBoard()' to the 12 bitmaps.*/
        long[] chess_bitboards = new long[12];
        for (int i = 0; i < 64; i++) {
            if (board[i / 8][i % 8] != ' ') {
                String bit = "0".repeat((board_size * board_size) - (i + 1)) + "1" + "0".repeat(i); //build bit string where 1 bit is at pos i and rest is 0... but we need to reverse it like we did in the FEN decoder.
                chess_bitboards[gtidx(board[i / 8][i % 8])] |= Long.parseUnsignedLong((bit), 2); //add it to the correct bitmap
            }
        }
        return chess_bitboards;
    }

    public static void initiate_boards() {
        /*initiates the board variables*/
        if (Long.bitCount(bitmaps[gtidx('k')]) > 1 | Long.bitCount(bitmaps[gtidx('K')]) > 1) {
            System.out.println("\u001B[31mThere are multiple kings of the same color present on the board. this is an illegal chess position!\u001B[0m");
            System.exit(2);
        }/* //TODO: uncomment this at the end
        else if (Long.bitCount(bitmaps[gtidx('k')]) < 1 | Long.bitCount(bitmaps[gtidx('K')]) < 1) {
            System.out.println("\u001B[31mIllegal chess position given: one or more king is missing!\u001B[0m");
            System.exit(0);
        }*/
        files();
        ranks();
        KQ_side();
        bitmaps_to_chessboard(bitmaps);
    }
    public static void two_bitmaps_to_chessboard(long[] bitmaps1, long[] bitmaps2) {
        /*prints 2 given bitboards next to each other so I can compare them better for debugging purposes*/
        String[][] board = new String[board_size][board_size];
        for (int pos = 0; pos < (board_size * board_size); pos++) {
            board[pos / board_size][pos % board_size] = " "; //initialize empty board with placeholder
        }
        String[][] board2 = new String[board_size][board_size];
        for (int pos = 0; pos < (board_size * board_size); pos++) {
            board2[pos / board_size][pos % board_size] = " "; //initialize empty board with placeholder
        }
        for (int fig = 0; fig < bitmaps1.length; fig++) {
            for (int i = 0; i < (board_size * board_size); i++) {
                if (((bitmaps1[fig] >> i) & 1) == 1) { //wherever we find 1's in the binary code of the current bitmap...
                    board[i / board_size][i % board_size] = gtfig(fig).toString(); // ...place the character of the current bitmap to the board
                }
            }
        }
        for (int fig = 0; fig < bitmaps2.length; fig++) {
            for (int i = 0; i < (board_size * board_size); i++) {
                if (((bitmaps2[fig] >> i) & 1) == 1) { //wherever we find 1's in the binary code of the current bitmap...
                    board2[i / board_size][i % board_size] = gtfig(fig).toString(); // ...place the character of the current bitmap to the board
                }
            }
        }
        for (int i = 0; i < board_size; i++) {
            String s = "        ";
            if (i==4){
                s="   -->̣  ";
            }
            System.out.println(Arrays.toString(board[i])+String.format("%s",s)+Arrays.toString(board2[i]));
        }
    }

    public static void bitmaps_to_chessboard(long[] bitmaps) {
        /*prints the n given bitmaps by overlaying them on top of each other and printing the result to the terminal.*/
        if(isNull(bitmaps)){
            System.out.println("\u001B[35mBitmap is Null\u001B[0m");
            return;
        }
        String[][] board = new String[board_size][board_size];
        for (int pos = 0; pos < (board_size * board_size); pos++) {
            board[pos / board_size][pos % board_size] = " "; //initialize empty board with placeholder
        }
        System.out.println();
        for (int fig = 0; fig < bitmaps.length; fig++) {
            for (int i = 0; i < (board_size * board_size); i++) {
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
        /*prints the given 64bit as a chessboard to the terminal*/
        if(isNull(bitmap)){
            System.out.println("\u001B[35mBitmap is Null\u001B[0m");
            return;
        }
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
        /*For debugging purposes: returns the given bit as a readable string*/
        String str = Long.toBinaryString(l);
        String mask = "0".repeat(board_size * board_size);
        return mask.substring(0, mask.length() - str.length()) + str;
    }

    public static void empty() {
        /*sets the empty squares*/
        EMPTY = ~(BLACKPIECES + WHITEPIECES);
        //bitmap_to_chessboard(EMPTY);
    }

    public static void colors() {
        /*sets the occupied squares of each color*/
        BLACKPIECES = 0L;
        WHITEPIECES = 0L;
        for (int fig = 0; fig < 6; fig++) { //black figures
            BLACKPIECES |= bitmaps[fig];
        }
        for (int fig = 6; fig < 12; fig++) { //white figures
            WHITEPIECES |= bitmaps[fig];
        }
    }

    public static void KQ_side() {
        /*defines the kingside and queenside (for castling)*/
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
        /*initialies the 8 files*/
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
        /*initializes the 8 ranks*/
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
        return new Integer[]{((idx / board_size)), ((idx % board_size))};
    }

    public static long setBit(int n) {
        /*sets a bit at the given position. n=0 results in 000...0, n=1 in 000...1 and so on.*/
        long bitmap = 1L;
        for (int i = 0; i < n - 1; i++) {
            bitmap <<= 1;
        }
        if (n == 0) {
            bitmap = 0L;
        }
        return bitmap;
    }

    public static boolean isWhite(long bitboard) {
        /*returns true if the given bitboard contains white pieces. It is thought to be given a bitboard with only one piece on it, but it works for multiple also.*/
        /*For this method to work, WHITEPIECES & BLACKPIECES need to be initialized correctly.*/
        return (bitboard & WHITEPIECES) != 0L;
    }

    public static boolean isKing(long bitboard) {
        /*returns true if the given bitboard contains a king of some color*/
        return (bitboard & (bitmaps[gtidx('k')] | bitmaps[gtidx('K')])) != 0L;
    }
}
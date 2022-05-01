package chess.engine.figures;

import static chess.engine.board.Bitboards.*;
import static chess.engine.figures.Figures.gtidx;
import static chess.engine.figures.Moves_Helper.*;
import static java.util.Objects.isNull;

public class Moves {
    public static boolean whitesTurn;
    public static long[] movemaps = new long[12]; //TODO: needed only for the red zone, can we change that to make it faster?
    public static long[][] movemapsIndividual = new long[12][];
    public static long[][] posmapsIndividual = new long[12][];
    public static long pinnedMovement = 0L;
    public static long REDZONEB;
    public static long REDZONEW;
    static long pPOSM; //black pawn possibilities for moving (not capturing)
    static long PPOSM;
    public static long BPROTECTED; //white pieces protected by other white pieces
    public static long WPROTECTED; //black pieces protected by other black pieces
    public static long BATTACKED; //attacked black pieces
    public static long WATTACKED;
    public static int nrOfbAttackers; //number of attackers which check the black king
    public static int nrOfwAttackers;

    public static long pinnedB; //pinned black pieces
    public static long pinnedW;
    public static long locOfbAttackers; //location of attackers which check the black king
    public static long locOfwAttackers;
    public static long blockLocationsB; //locations which can be moved to, to block a check by white
    public static long blockLocationsW;
    /*public static boolean BINCHECK; //if black king is in check
    public static boolean WINCHECK;*/
    public static int currAmount;


    public static void black_pawns() {
        int idx = gtidx('p');
        movemaps[idx] = 0L;
        long[] Figures = get_single_figure_boards(bitmaps[idx]);
        movemapsIndividual[idx] = new long[Figures.length];
        posmapsIndividual[idx] = new long[Figures.length];
        for (int i = 0; i < Figures.length; i++) {
            long curr = Figures[i];
            long pawn_to_moves = 0L; //empty board
            long pawn_to_captures = 0L;
            if (curr != 0L) {
                pawn_to_moves |= (curr << 8) & EMPTY & (~RANKS[0]); //move one forward
                pawn_to_moves |= (curr << 16) & EMPTY & (EMPTY << 8) & (RANKS[6] << 16); //move two forwards
                pawn_to_captures |= (curr << 7) & (~EMPTY) & WHITEPIECES;//capture left
                pawn_to_captures |= (curr << 9) & (~EMPTY) & WHITEPIECES;//capture right
                pawn_to_moves |= (curr << 8) & EMPTY & (RANKS[0]); //pawn promotion by move; //TODO: replace pawn with new figure
                pawn_to_captures |= (curr << 7) & (~EMPTY) & WHITEPIECES & (RANKS[0]); //pawn promotion by capture left; //TODO: replace pawn with new figure
                pawn_to_captures |= (curr << 9) & (~EMPTY) & WHITEPIECES & (RANKS[0]); //pawn promotion by capture right; //TODO: replace pawn with new figure
                //pawn_to_moves |= (curr << 7);//en-passant capture left // TODO: en-passant : piece must have moved in last turn
                //pawn_to_moves |= (curr << 9);//en-passant capture right // TODO: en-passant : piece must have moved in last turn
                //bitmap_to_chessboard(pawn_to_moves);
                pawn_to_captures = clearOverflow(curr, pawn_to_captures);
                if ((pawn_to_captures & bitmaps[gtidx('k')]) != 0L) { //if a black king is attacked
                    nrOfbAttackers++;
                    locOfbAttackers |= curr;
                }
                long protecting = ((((curr >>> 9)) & BLACKPIECES) | (((curr >>> 7)) & BLACKPIECES));
                if (protecting != 0L) { //if a opposite colored piece is reachable it is protected
                    BPROTECTED |= protecting;
                    REDZONEW |= protecting;
                }
            }
            pPOSM = pawn_to_moves;
            movemaps[idx] |= pawn_to_moves | pawn_to_captures;
            movemapsIndividual[idx][i] = pawn_to_moves | pawn_to_captures;
            posmapsIndividual[idx][i] = Figures[i];
        }
    }

    public static void white_pawns() { //TODO: white pawn can capture white pawn and also it does so in the wrong direction...
        int idx = gtidx('P');
        movemaps[idx] = 0L;
        long[] Figures = get_single_figure_boards(bitmaps[idx]);
        movemapsIndividual[idx] = new long[Figures.length];
        posmapsIndividual[idx] = new long[Figures.length];
        for (int i = 0; i < Figures.length; i++) {
            long curr = Figures[i];
            long pawn_to_moves = 0L; //empty board
            long pawn_to_captures = 0L;
            if (curr != 0L) {
                pawn_to_moves |= (curr >>> 8) & EMPTY & (~RANKS[7]); //move one forward
                pawn_to_moves |= (curr >>> 16) & EMPTY & (EMPTY >> 8) & (RANKS[1] >> 16); //move two forwards
                pawn_to_captures |= (curr >>> 9) & (~EMPTY) & BLACKPIECES;//capture left //TODO: remove captured piece
                pawn_to_captures |= (curr >>> 7) & (~EMPTY) & BLACKPIECES;//capture right //TODO: remove captured piece
                pawn_to_moves |= (curr >>> 8) & EMPTY & (RANKS[7]); //pawn promotion by move; //TODO: replace pawn with new figure
                pawn_to_captures |= (curr >>> 9) & (~EMPTY) & BLACKPIECES & (RANKS[7]); //pawn promotion by capture left; //TODO: replace pawn with new figure
                pawn_to_captures |= (curr >>> 7) & (~EMPTY) & BLACKPIECES & (RANKS[7]); //pawn promotion by capture right; //TODO: replace pawn with new figure
                //pawn_to_moves |= (curr >>> 9);//en-passant capture left // TODO: en-passant : piece must have moved in last turn
                //pawn_to_moves |= (curr >>> 7);//en-passant capture right // TODO: en-passant : piece must have moved in last turn
                //TODO: for the en-passant I give the search call a "history" of the last move that has been played. I can give it a bitmap of the NEW position of the pawn that moved IFF it did a dobble move. otherwise the history is empty.
                //bitmap_to_chessboard(pawn_to_moves);
                pawn_to_captures = clearOverflow(curr, pawn_to_captures);
                if ((pawn_to_captures & bitmaps[gtidx('k')]) != 0L) { //if a black king is attacked
                    nrOfwAttackers++;
                    locOfwAttackers |= curr;
                }
                long protecting = ((((curr >>> 9)) & WHITEPIECES) | (((curr >>> 7)) & WHITEPIECES));
                if (protecting != 0L) { //if a opposite colored piece is reachable it is protected
                    WPROTECTED |= protecting;
                    REDZONEB |= protecting;
                }
            }
            PPOSM = pawn_to_moves;
            movemaps[gtidx('P')] |= pawn_to_moves | pawn_to_captures;
            movemapsIndividual[idx][i] = pawn_to_moves | pawn_to_captures;
            posmapsIndividual[idx][i] = Figures[i];
        }
    }

    public static void black_knights() {
        int idx = gtidx('n');
        movemaps[idx] = 0L;
        long[] Figures = get_single_figure_boards(bitmaps[idx]);
        movemapsIndividual[idx] = new long[Figures.length];
        posmapsIndividual[idx] = new long[Figures.length];
        for (int i = 0; i < Figures.length; i++) {
            long curr = Figures[i];
            long knight_to_moves = 0L; //empty board
            if (curr != 0L) {
                knight_to_moves |= ((curr >>> 6) | (curr >>> 10) | (curr >>> 15) | (curr >>> 17) | (curr << 6) | (curr << 10) | (curr << 15) | (curr << 17));
                knight_to_moves = clearOverflow(curr, knight_to_moves);
                if ((knight_to_moves & bitmaps[gtidx('K')]) != 0L) { //if a white king is attacked
                    nrOfbAttackers++;
                    locOfbAttackers |= curr;
                }
                long protecting = knight_to_moves & BLACKPIECES;
                if (protecting != 0L) { //if a opposite colored piece is reachable it is protected
                    BPROTECTED |= protecting;
                    REDZONEW |= protecting;
                }
                knight_to_moves &= ~BLACKPIECES;
                movemaps[idx] |= knight_to_moves;
                movemapsIndividual[idx][i] = knight_to_moves;
                posmapsIndividual[idx][i] = Figures[i];
            }
        }
    }

    public static void white_knights() {
        int idx = gtidx('N');
        movemaps[idx] = 0L;
        long[] Figures = get_single_figure_boards(bitmaps[idx]);
        movemapsIndividual[idx] = new long[Figures.length];
        posmapsIndividual[idx] = new long[Figures.length];
        for (int i = 0; i < Figures.length; i++) {
            long curr = Figures[i];
            long knight_to_moves = 0L; //empty board
            if (curr != 0L) {
                knight_to_moves |= ((curr >>> 6) | (curr >>> 10) | (curr >>> 15) | (curr >>> 17) | (curr << 6) | (curr << 10) | (curr << 15) | (curr << 17));
                knight_to_moves = clearOverflow(curr, knight_to_moves);
                if ((knight_to_moves & bitmaps[gtidx('k')]) != 0L) { //if a white king is attacked
                    nrOfwAttackers++;
                    locOfwAttackers |= curr;
                }
                long protecting = knight_to_moves & WHITEPIECES;
                if (protecting != 0L) { //if a opposite colored piece is reachable it is protected
                    WPROTECTED |= protecting;
                    REDZONEB |= protecting;
                }
                knight_to_moves &= ~WHITEPIECES;
                movemaps[idx] |= knight_to_moves;
                movemapsIndividual[idx][i] = knight_to_moves;
                posmapsIndividual[idx][i] = Figures[i];
            }
        }
    }

    public static void black_bishops() {
        int idx = gtidx('b');
        movemaps[idx] = 0L;
        //long bishop_to_moves = 0L; //empty board
        long curr = bitmaps[idx];
        if (curr != 0L) {
            movemapsIndividual[idx] = diag_bitboard(curr, WHITEPIECES, BLACKPIECES, idx);
            for (long f : movemapsIndividual[idx]) {
                movemaps[idx] |= f;
            }
        }
    }

    public static void white_bishops() {
        int idx = gtidx('B');
        movemaps[idx] = 0L;
        //long bishop_to_moves = 0L; //empty board
        long curr = bitmaps[idx];
        if (curr != 0L) {
            movemapsIndividual[idx] = diag_bitboard(curr, BLACKPIECES, WHITEPIECES, idx);
            for (long f : movemapsIndividual[idx]) {
                movemaps[idx] |= f;
            }
        }
        bitmaps_to_chessboard(movemapsIndividual[idx]);
        System.exit(4);
    }

    public static void black_rooks() {
        int idx = gtidx('r');
        movemaps[idx] = 0L;
        //long rook_to_moves = 0L; //empty board
        long curr = bitmaps[idx];
        if (curr != 0L) {
            movemapsIndividual[idx] = hor_ver_bitboard(curr, WHITEPIECES, BLACKPIECES, idx);
            for (long f : movemapsIndividual[idx]) {
                movemaps[idx] |= f;
            }
        }
    }

    public static void white_rooks() {
        int idx = gtidx('R');
        movemaps[idx] = 0L;
        //long rook_to_moves = 0L; //empty board
        long curr = bitmaps[idx];
        if (curr != 0L) {
            movemapsIndividual[idx] = hor_ver_bitboard(curr, BLACKPIECES, WHITEPIECES, idx);
            for (long f : movemapsIndividual[idx]) {
                movemaps[idx] |= f;
            }
        }
        //bitmap_to_chessboard(movemaps[idx]);
        //System.out.println(idx);
    }

    public static void black_queens() {
        int idx = gtidx('q');
        movemaps[idx] = 0L;
        //long queen_to_moves = 0L; //empty board
        long curr = bitmaps[idx];
        if (curr != 0L) {
            movemapsIndividual[idx] = hor_ver_bitboard(curr, WHITEPIECES, BLACKPIECES, idx);
            long[] diag = diag_bitboard(curr, WHITEPIECES, BLACKPIECES, idx);
            for (int m = 0; m < currAmount; m++) {
                movemapsIndividual[idx][m] |= diag[m];
            }
            for (long f : movemapsIndividual[idx]) {
                movemaps[idx] |= f;
            }
        }
    }

    public static void white_queens() {
        int idx = gtidx('Q');
        movemaps[idx] = 0L;
        //long queen_to_moves = 0L; //empty board
        long curr = bitmaps[idx];
        if (curr != 0L) {
            movemapsIndividual[idx] = hor_ver_bitboard(curr, BLACKPIECES, WHITEPIECES, idx);
            long[] diag = diag_bitboard(curr, BLACKPIECES, WHITEPIECES, idx);
            for (int m = 0; m < currAmount; m++) { //how many queens we have
                movemapsIndividual[idx][m] |= diag[m];
            }
            for (long f : movemapsIndividual[idx]) {
                movemaps[idx] |= f;
            }
        }
    }

    public static void black_king() {
        int idx = gtidx('k');
        //TODO: castling
        long king_to_moves = 0L; //empty board
        long curr = bitmaps[idx];
        movemapsIndividual[idx] = new long[1]; //there is only one king!
        if (curr != 0L) {
            king_to_moves |= (curr >>> 8); //move one up
            king_to_moves |= (curr >>> 9); //move one left up
            king_to_moves |= (curr >>> 7); //move one right up
            king_to_moves |= (curr >>> 1); //move one left
            king_to_moves |= (curr << 8); //move one down
            king_to_moves |= (curr << 9); //move one right down
            king_to_moves |= (curr << 7); //move one left down
            king_to_moves |= (curr << 1); //move one right
            //bitmap_to_chessboard(king_to_moves);
            king_to_moves = clearOverflow(curr, king_to_moves);
            long protecting = king_to_moves & BLACKPIECES;
            if (protecting != 0L) { //if a opposite colored piece is reachable it is protected
                BPROTECTED |= protecting;
                REDZONEW |= protecting;
            }
        }
        king_to_moves &= ~BLACKPIECES;
        movemaps[idx] |= king_to_moves;
        movemapsIndividual[idx][0] |= king_to_moves; //there is only one king!
    }

    public static void white_king() {
        int idx = gtidx('K');
        //TODO: castling
        long king_to_moves = 0L; //empty board
        long curr = bitmaps[idx];
        movemapsIndividual[idx] = new long[1]; //there is only one king!
        if (curr != 0L) {
            king_to_moves |= (curr >>> 8); //move one up
            king_to_moves |= (curr >>> 9); //move one left up
            king_to_moves |= (curr >>> 7); //move one right up
            king_to_moves |= (curr >>> 1); //move one left
            king_to_moves |= (curr << 8); //move one down
            king_to_moves |= (curr << 9); //move one right down
            king_to_moves |= (curr << 7); //move one left down
            king_to_moves |= (curr << 1); //move one right
            king_to_moves = clearOverflow(curr, king_to_moves);
            long protecting = king_to_moves & WHITEPIECES;
            if (protecting != 0L) { //if a opposite colored piece is reachable it is protected
                WPROTECTED |= protecting;
                REDZONEB |= protecting;
            }
        }
        king_to_moves &= ~WHITEPIECES;
        movemaps[idx] |= king_to_moves;
        movemapsIndividual[idx][0] |= king_to_moves;
    }

    public static void initiate_next_black_movements() {
        colors();
        empty();
        black_bishops();
        black_rooks();
        black_queens();
        black_pawns();
        black_knights();
        black_king();
    }

    public static void initiate_next_white_movements() {
        colors();
        empty();
        white_bishops();
        white_rooks();
        white_queens();
        white_pawns();
        white_knights();
        white_king();
    }

    public static void initiate_next_moves(long[] parent, long[] history, boolean turn) { //initiates all moves
        //Reset everything:
        blockLocationsB = 0L;
        blockLocationsW = 0L;
        movemaps = new long[12];
        movemapsIndividual = new long[12][];
        pinnedMovement = 0L;
        nrOfbAttackers = 0;
        nrOfwAttackers = 0;
        WATTACKED = 0L;
        BATTACKED = 0L;
        BPROTECTED = 0L;
        WPROTECTED = 0L;
        pinnedB = 0L;
        pinnedW = 0L;
        locOfbAttackers = 0L;
        locOfwAttackers = 0L;
        if (!isNull(history)) {
            //TODO: use history
        }
        whitesTurn = turn;
        if (!isNull(parent)) {
            set_bitboards(parent);
        }
        initiate_next_black_movements();
        initiate_next_white_movements();

        initiate_redzone();
        //initiate_inCheck();
        valid_moves();
    }
}

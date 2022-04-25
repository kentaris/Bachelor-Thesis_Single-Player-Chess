package chess.engine.figures;

import static chess.engine.board.Bitboards.*;
import static chess.engine.figures.Figures.gtidx;
import static chess.engine.figures.Moves_Helper.*;

public class Moves {
    public static long[] movemaps = new long[12];
    public static long[] movemapsp; //indicidual movement sets of the pieces
    public static long[] movemapsP;
    public static long[] movemapsn;
    public static long[] movemapsN;
    public static long[] movemapsb;
    public static long[] movemapsB;
    public static long[] movemapsr;
    public static long[] movemapsR;
    public static long[] movemapsq;
    public static long[] movemapsQ;
    public static long[] movemapsk;
    public static long[] movemapsK;
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
    public static boolean BINCHECK; //if black king is in check
    public static boolean WINCHECK;


    public static void black_pawns() {
        long[] Figures = get_single_figure_boards(bitmaps[gtidx('p')]);
        movemapsp = new long[Figures.length];
        for (int i = 0; i < Figures.length; i++) {
            long curr = Figures[i];
            long pawn_to_moves = 0L; //empty board
            long pawn_to_captures = 0L;
            if (curr != 0) {
                pawn_to_moves |= (curr << 8) & EMPTY & (~RANKS[0]); //move one forward
                pawn_to_moves |= (curr << 16) & EMPTY & (EMPTY << 8) & (RANKS[6] << 16); //move two forwards
                pawn_to_captures |= (curr << 7) & (~EMPTY) & WHITEPIECES;//capture left //TODO: remove captured piece
                pawn_to_captures |= (curr << 9) & (~EMPTY) & WHITEPIECES;//capture right //TODO: remove captured piece
                pawn_to_moves |= (curr << 8) & EMPTY & (RANKS[0]); //pawn promotion by move; //TODO: replace pawn with new figure
                pawn_to_captures |= (curr << 7) & (~EMPTY) & WHITEPIECES & (RANKS[0]); //pawn promotion by capture left; //TODO: replace pawn with new figure
                pawn_to_captures |= (curr << 9) & (~EMPTY) & WHITEPIECES & (RANKS[0]); //pawn promotion by capture right; //TODO: replace pawn with new figure
                //pawn_to_moves |= (curr << 7);//en-passant capture left // TODO: en-passant : piece must have moved in last turn
                //pawn_to_moves |= (curr << 9);//en-passant capture right // TODO: en-passant : piece must have moved in last turn
                //bitmap_to_chessboard(pawn_to_moves);
                pawn_to_captures = clearOverflow(curr, pawn_to_captures);
                if ((pawn_to_captures & bitmaps[gtidx('K')]) != 0L) { //if a black king is attacked
                    nrOfbAttackers++;
                    locOfbAttackers |= (((bitmaps[gtidx('K')] >>> 7) & curr) | ((bitmaps[gtidx('K')] >>> 9) & curr)); //we shift the king position back to the desination where a pawn could have captured the king (both directions: >>>7 and >>>9) and then overlap it with the positions where pawns are located. The result is the pawn which is attacking the king. Ideally there is only one pawn attacking the king but this implementation allows for multiple pawns which attack the king if the start position is defining it so (we can't arrive there with legal moves).
                }
            }
            pPOSM = pawn_to_moves;
            movemaps[gtidx('p')] |= pawn_to_moves | pawn_to_captures; //TODO: needed?
            movemapsp[i] = pawn_to_moves | pawn_to_captures;
        }
    }

    public static void white_pawns() {
        long[] Figures = get_single_figure_boards(bitmaps[gtidx('P')]);
        movemapsP = new long[Figures.length];
        for (int i = 0; i < Figures.length; i++) {
            long curr = Figures[i];
            long pawn_to_moves = 0L; //empty board
            long pawn_to_captures = 0L;
            if (curr != 0) {
                pawn_to_moves |= (curr >>> 8) & EMPTY & (~RANKS[7]); //move one forward
                pawn_to_moves |= (curr >>> 16) & EMPTY & (EMPTY >> 8) & (RANKS[1] >> 16); //move two forwards
                pawn_to_captures |= (curr >>> 9) & (~EMPTY) & BLACKPIECES;//capture left //TODO: remove captured piece
                pawn_to_captures |= (curr >>> 7) & (~EMPTY) & BLACKPIECES;//capture right //TODO: remove captured piece
                pawn_to_moves |= (curr >>> 8) & EMPTY & (RANKS[7]); //pawn promotion by move; //TODO: replace pawn with new figure
                pawn_to_captures |= (curr >>> 9) & (~EMPTY) & BLACKPIECES & (RANKS[7]); //pawn promotion by capture left; //TODO: replace pawn with new figure
                pawn_to_captures |= (curr >>> 7) & (~EMPTY) & BLACKPIECES & (RANKS[7]); //pawn promotion by capture right; //TODO: replace pawn with new figure
                //pawn_to_moves |= (curr >>> 9);//en-passant capture left // TODO: en-passant : piece must have moved in last turn
                //pawn_to_moves |= (curr >>> 7);//en-passant capture right // TODO: en-passant : piece must have moved in last turn
                //bitmap_to_chessboard(pawn_to_moves);
                pawn_to_captures = clearOverflow(curr, pawn_to_captures);
                if ((pawn_to_captures & bitmaps[gtidx('k')]) != 0L) { //if a black king is attacked
                    nrOfwAttackers++;
                    locOfwAttackers |= (((bitmaps[gtidx('k')] << 7) & curr) | ((bitmaps[gtidx('k')] << 9) & curr));
                }
            }
            PPOSM = pawn_to_moves;
            movemaps[gtidx('P')] |= pawn_to_moves | pawn_to_captures; //TODO: needed?
            movemapsP[i] = pawn_to_moves | pawn_to_captures;
        }
    }

    public static void black_knights() {
        long[] Figures = get_single_figure_boards(bitmaps[gtidx('n')]);
        movemapsn = new long[Figures.length];
        for (int i = 0; i < Figures.length; i++) {
            long curr = Figures[i];
            long knight_to_moves = 0L; //empty board
            if (curr != 0) {
                knight_to_moves |= ((curr >>> 6) | (curr >>> 10) | (curr >>> 15) | (curr >>> 17) | (curr << 6) | (curr << 10) | (curr << 15) | (curr << 17)) & (WHITEPIECES | EMPTY);
                clearOverflow(curr, knight_to_moves);
                int idx = gtidx('K');
                if ((knight_to_moves & bitmaps[idx]) != 0L) { //if a white king is attacked
                    nrOfbAttackers++;
                    locOfbAttackers |= (((bitmaps[idx] << 6) & curr) |
                            ((bitmaps[idx] << 10) & curr) |
                            ((bitmaps[idx] << 15) & curr) |
                            ((bitmaps[idx] << 17) & curr) |
                            ((bitmaps[idx] >>> 6) & curr) |
                            ((bitmaps[idx] >>> 10) & curr) |
                            ((bitmaps[idx] >>> 15) & curr) |
                            ((bitmaps[idx] >>> 17) & curr)
                    );
                }
                movemaps[gtidx('n')] |= knight_to_moves;
                movemapsn[i] = knight_to_moves;
            }
        }
    }

    public static void white_knights() {
        long[] Figures = get_single_figure_boards(bitmaps[gtidx('N')]);
        movemapsN = new long[Figures.length];
        for (int i = 0; i < Figures.length; i++) {
            long curr = Figures[i];
            long knight_to_moves = 0L; //empty board
            if (curr != 0) {
                knight_to_moves |= ((curr >>> 6) | (curr >>> 10) | (curr >>> 15) | (curr >>> 17) | (curr << 6) | (curr << 10) | (curr << 15) | (curr << 17)) & (BLACKPIECES | EMPTY);
                knight_to_moves = clearOverflow(curr, knight_to_moves);
                int idx = gtidx('k');
                if ((knight_to_moves & bitmaps[idx]) != 0L) { //if a white king is attacked
                    nrOfwAttackers++;
                    locOfwAttackers |= (((bitmaps[idx] << 6) & curr) |
                            ((bitmaps[idx] << 10) & curr) |
                            ((bitmaps[idx] << 15) & curr) |
                            ((bitmaps[idx] << 17) & curr) |
                            ((bitmaps[idx] >>> 6) & curr) |
                            ((bitmaps[idx] >>> 10) & curr) |
                            ((bitmaps[idx] >>> 15) & curr) |
                            ((bitmaps[idx] >>> 17) & curr)
                    );
                }
            }
            movemaps[gtidx('N')] |= knight_to_moves;
            movemapsN[i] = knight_to_moves;
        }
    }

    public static void black_bishops() {
        //long bishop_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('b')];
        if (curr != 0) {
            movemapsb = diag_bitboard(curr, WHITEPIECES, BLACKPIECES);
            for (long f: movemapsb) {
                movemaps[gtidx('b')] |= f;
            }
        }
    }

    public static void white_bishops() {
        //long bishop_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('B')];
        if (curr != 0) {
            movemapsB = diag_bitboard(curr, BLACKPIECES, WHITEPIECES);
            for (long f: movemapsB) {
                movemaps[gtidx('B')] |= f;
            }
        }
    }

    public static void black_rooks() {
        //long rook_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('r')];
        if (curr != 0) {
            movemapsr = hor_ver_bitboard(curr, WHITEPIECES, BLACKPIECES);
            for (long f: movemapsr) {
                movemaps[gtidx('r')] |= f;
            }
        }
    }

    public static void white_rooks() {
        //long rook_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('R')];
        if (curr != 0) {
            movemapsR = hor_ver_bitboard(curr, BLACKPIECES, WHITEPIECES);
            for (long f: movemapsR) {
                movemaps[gtidx('R')] |= f;
            }
        }
    }

    public static void black_queens() {
        //long queen_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('q')];
        if (curr != 0) {
            //movemapsq |= hor_ver_bitboard(curr, WHITEPIECES, BLACKPIECES);
            //movemapsq |= diag_bitboard(curr, WHITEPIECES, BLACKPIECES);
            /*for (long f: movemapsq) {
                movemaps[gtidx('q')] |= f;
            }*/
        }
    }

    public static void white_queens() {
        //long queen_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('Q')];
        if (curr != 0) {
            //movemapsQ |= hor_ver_bitboard(curr, BLACKPIECES, WHITEPIECES);
            //movemapsQ |= diag_bitboard(curr, BLACKPIECES, WHITEPIECES);
            /*for (long f: movemapsQ) {
                movemaps[gtidx('Q')] |= f;
            }*/
        }
    }

    public static void black_king() {
        long king_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('k')];
        movemapsk = new long[1]; //there is only one king!
        if (curr != 0) {
            //TODO: opposite colored king cannot reach position & not red zone
            king_to_moves |= (curr >>> 8) & (EMPTY | WHITEPIECES); //move one up
            king_to_moves |= (curr >>> 9) & (EMPTY | WHITEPIECES); //move one left up
            king_to_moves |= (curr >>> 7) & (EMPTY | WHITEPIECES); //move one right up
            king_to_moves |= (curr >>> 1) & (EMPTY | WHITEPIECES); //move one left
            king_to_moves |= (curr << 8) & (EMPTY | WHITEPIECES); //move one down
            king_to_moves |= (curr << 9) & (EMPTY | WHITEPIECES); //move one right down
            king_to_moves |= (curr << 7) & (EMPTY | WHITEPIECES); //move one left down
            king_to_moves |= (curr << 1) & (EMPTY | WHITEPIECES); //move one right
            //bitmap_to_chessboard(king_to_moves);
            king_to_moves = clearOverflow(curr, king_to_moves);
        }
        movemaps[gtidx('k')] |= king_to_moves;
        movemapsk[0] |= king_to_moves; //there is only one king!
    }

    public static void white_king() {
        long king_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('K')];
        movemapsK = new long[1]; //there is only one king!
        if (curr != 0) {
            //TODO: opposite colored king cannot reach position & not red zone
            king_to_moves |= (curr >>> 8) & (EMPTY | BLACKPIECES); //move one up
            king_to_moves |= (curr >>> 9) & (EMPTY | BLACKPIECES); //move one left up
            king_to_moves |= (curr >>> 7) & (EMPTY | BLACKPIECES); //move one right up
            king_to_moves |= (curr >>> 1) & (EMPTY | BLACKPIECES); //move one left
            king_to_moves |= (curr << 8) & (EMPTY | BLACKPIECES); //move one down
            king_to_moves |= (curr << 9) & (EMPTY | BLACKPIECES); //move one right down
            king_to_moves |= (curr << 7) & (EMPTY | BLACKPIECES); //move one left down
            king_to_moves |= (curr << 1) & (EMPTY | BLACKPIECES); //move one right
            //bitmap_to_chessboard(king_to_moves);
            king_to_moves = clearOverflow(curr, king_to_moves);
        }
        movemaps[gtidx('K')] |= king_to_moves;
        movemapsK[0] |= king_to_moves;
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

    public static void initiate_next_moves() { //initiates all moves
        initiate_next_black_movements();
        initiate_next_white_movements();
    }
}

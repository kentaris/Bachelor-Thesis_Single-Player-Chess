package chess.engine.figures;

import static chess.engine.board.Bitboards.*;
import static chess.engine.figures.Figures.gtidx;
import static chess.engine.figures.Moves_Helper.diag_bitboard;
import static chess.engine.figures.Moves_Helper.hor_ver_bitboard;

public class Moves {
    static long pPOS; //black pawn possibilities
    static long nPOS;
    static long bPOS;
    static long rPOS;
    static long qPOS;
    static long kPOS;
    static long PPOS; //white pawn possibilities
    static long NPOS;
    static long BPOS;
    static long RPOS;
    static long QPOS;
    static long KPOS;
    static long REDZONEB;
    static long REDZONEW;
    static long pPOSM; //black pawn possibilities for moving (not capturing)
    static long PPOSM;
    public static long BPROTECTED; //white pieces protected by other white pieces
    public static long WPROTECTED; //black pieces protected by other black pieces
    public static long BATTACKED; //attacked black pieces
    public static long WATTACKED; //attacked white pieces
    public static int nrOfbAttackers; //number of attackers which check the black king
    public static int nrOfwAttackers; //number of attackers which check the white king

    public static void black_pawns() {
        long pawn_to_moves = 0L; //empty board
        long pawn_to_captures = 0L;
        long curr = bitmaps[gtidx('p')];
        if (curr != 0) {
            //TODO: add myKingInCheck bit
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
        }
        pPOSM = pawn_to_moves;
        pPOS = pawn_to_moves | pawn_to_captures;
    }

    public static void white_pawns() {
        long pawn_to_moves = 0L; //empty board
        long pawn_to_captures = 0L;
        long curr = bitmaps[gtidx('P')];
        if (curr != 0) {
            //TODO: add myKingInCheck bit
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
        }
        PPOSM = pawn_to_moves;
        PPOS = pawn_to_moves | pawn_to_captures;
    }

    public static void black_knights() {
        long knight_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('n')];
        if (curr != 0) {
            //TODO: add myKingInCheck bit
            knight_to_moves |= ((curr >>> 6) | (curr >>> 10) | (curr >>> 15) | (curr >>> 17) | (curr << 6) | (curr << 10) | (curr << 15) | (curr << 17)) & (WHITEPIECES | EMPTY);
            //bitmap_to_chessboard(knight_to_moves);
        }
        nPOS = knight_to_moves;
    }

    public static void white_knights() {
        long knight_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('N')];
        if (curr != 0) {
            //TODO: add myKingInCheck bit
            knight_to_moves |= ((curr >>> 6) | (curr >>> 10) | (curr >>> 15) | (curr >>> 17) | (curr << 6) | (curr << 10) | (curr << 15) | (curr << 17)) & (BLACKPIECES | EMPTY);
        }
        NPOS = knight_to_moves;
    }

    public static void black_bishops() { //TODO
        long bishop_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('b')];
        if (curr != 0) {
            bishop_to_moves = diag_bitboard(curr, WHITEPIECES, BLACKPIECES);
        }
        bPOS = bishop_to_moves;
    }

    public static void white_bishops() { //TODO
        long bishop_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('B')];
        if (curr != 0) {
            bishop_to_moves = diag_bitboard(curr, BLACKPIECES, WHITEPIECES);
        }
        BPOS = bishop_to_moves;
    }

    public static void black_rooks() {
        long rook_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('r')];
        if (curr != 0) {
            rook_to_moves = hor_ver_bitboard(curr, WHITEPIECES, BLACKPIECES);
            //TODO: add myKingInCheck bit
        }
        rPOS = rook_to_moves;
    }

    public static void white_rooks() {
        long rook_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('R')];
        if (curr != 0) {
            rook_to_moves = hor_ver_bitboard(curr, BLACKPIECES, WHITEPIECES);
            //TODO: add myKingInCheck bit
        }
        RPOS = rook_to_moves;
    }

    public static void black_queens() {
        long queen_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('q')];
        if (curr != 0) {
            queen_to_moves |= hor_ver_bitboard(curr, WHITEPIECES, BLACKPIECES);
            queen_to_moves |= diag_bitboard(curr, WHITEPIECES, BLACKPIECES);
            //TODO: add myKingInCheck bit
        }
        qPOS = queen_to_moves;
    }

    public static void white_queens() {
        long queen_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('Q')];
        if (curr != 0) {
            queen_to_moves |= hor_ver_bitboard(curr, BLACKPIECES, WHITEPIECES);
            queen_to_moves |= diag_bitboard(curr, BLACKPIECES, WHITEPIECES);
            //TODO: add myKingInCheck bit
        }
        QPOS = queen_to_moves;
    }

    public static void black_king() {
        long king_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('k')];
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
        }
        kPOS = king_to_moves;
    }

    public static void white_king() {
        long king_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('K')];
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
        }
        KPOS = king_to_moves;
    }

    public static void initiate_next_black_movements() {
        colors();
        empty();
        black_pawns();
        black_knights();
        black_bishops();
        black_rooks();
        black_queens();
        black_king();
    }

    public static void initiate_next_white_movements() {
        colors();
        empty();
        white_pawns();
        white_knights();
        white_bishops();
        white_rooks();
        white_queens();
        white_king();
    }

    public static void initiate_next_moves() { //initiates all moves
        initiate_next_black_movements();
        initiate_next_white_movements();
    }

    public static void initiate_red_zone_white() {
        /*this method does not initialize the moves. This method should only be called after the moves have been initialized otherwise we get the red-zone of the previous round. */
        REDZONEW = pPOS | nPOS | bPOS | rPOS | qPOS | kPOS;
        REDZONEW-=pPOSM; //remove regular pawn movements as they are not attacking //TODO: consider en-passant move. How to deal with them? they're only dangerous to pawns. for now they are just ignored
        //System.out.println("white redzone:");
        //bitmap_to_chessboard(REDZONEW); //TODO: test with start pos fen
    }

    public static void initiate_red_zone_black() {
        /*this method does not initialize the moves. This method should only be called after the moves have been initialized otherwise we get the red-zone of the previous round. */
        REDZONEB = PPOS | NPOS | BPOS | RPOS | QPOS | KPOS;
        REDZONEB-= PPOSM; //remove regular pawn movements as they are not attacking
        //System.out.println("black redzone:");
        //bitmap_to_chessboard(REDZONEB); //TODO: test with start pos fen
    }
}

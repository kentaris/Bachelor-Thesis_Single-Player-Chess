package chess.engine.figures;

import static chess.engine.board.Bitboards.*;
import static chess.engine.figures.Figures.gtidx;
import static chess.engine.search.Search.board_size;

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
    static long redZone;

    public static void black_pawns() {
        long pawn_to_moves = 0; //empty board
        long curr = bitmaps[gtidx('p')];
        //TODO: add myKingInCheck bit
        pawn_to_moves |= (curr << 8) & EMPTY & (~RANKS[0]); //move one forward
        pawn_to_moves |= (curr << 16) & EMPTY & (EMPTY << 8) & (RANKS[6] << 16); //move two forwards
        pawn_to_moves |= (curr << 7) & (~EMPTY) & WHITEPIECES;//capture left //TODO: remove captured piece
        pawn_to_moves |= (curr << 9) & (~EMPTY) & WHITEPIECES;//capture right //TODO: remove captured piece
        pawn_to_moves |= (curr << 8) & EMPTY & (RANKS[0]); //pawn promotion by move; //TODO: replace pawn with new figure
        pawn_to_moves |= (curr << 7) & (~EMPTY) & WHITEPIECES & (RANKS[0]); //pawn promotion by capture left; //TODO: replace pawn with new figure
        pawn_to_moves |= (curr << 9) & (~EMPTY) & WHITEPIECES & (RANKS[0]); //pawn promotion by capture right; //TODO: replace pawn with new figure
        //pawn_to_moves |= (curr << 7);//en-passant capture left // TODO: en-passant : piece must have moved in last turn
        //pawn_to_moves |= (curr << 9);//en-passant capture right // TODO: en-passant : piece must have moved in last turn
        //bitmap_to_chessboard(pawn_to_moves);
        pPOS = pawn_to_moves;
    }

    public static void white_pawns() {
        long pawn_to_moves = 0; //empty board
        long curr = bitmaps[gtidx('P')];
        //TODO: add myKingInCheck bit
        pawn_to_moves |= (curr >>> 8) & EMPTY & (~RANKS[7]); //move one forward
        pawn_to_moves |= (curr >>> 16) & EMPTY & (EMPTY >> 8) & (RANKS[1] >> 16); //move two forwards
        pawn_to_moves |= (curr >>> 9) & (~EMPTY) & BLACKPIECES;//capture left //TODO: remove captured piece
        pawn_to_moves |= (curr >>> 7) & (~EMPTY) & BLACKPIECES;//capture right //TODO: remove captured piece
        pawn_to_moves |= (curr >>> 8) & EMPTY & (RANKS[7]); //pawn promotion by move; //TODO: replace pawn with new figure
        pawn_to_moves |= (curr >>> 9) & (~EMPTY) & BLACKPIECES & (RANKS[7]); //pawn promotion by capture left; //TODO: replace pawn with new figure
        pawn_to_moves |= (curr >>> 7) & (~EMPTY) & BLACKPIECES & (RANKS[7]); //pawn promotion by capture right; //TODO: replace pawn with new figure
        //pawn_to_moves |= (curr >>> 9);//en-passant capture left // TODO: en-passant : piece must have moved in last turn
        //pawn_to_moves |= (curr >>> 7);//en-passant capture right // TODO: en-passant : piece must have moved in last turn
        //bitmap_to_chessboard(pawn_to_moves);
        PPOS = pawn_to_moves;
    }

    public static void black_knights() {
        long knight_to_moves = 0; //empty board
        long curr = bitmaps[gtidx('n')];
        //TODO: add myKingInCheck bit
        knight_to_moves |= ((curr >>> 6) | (curr >>> 10) | (curr >>> 15) | (curr >>> 17) | (curr << 6) | (curr << 10) | (curr << 15) | (curr << 17)) & (WHITEPIECES | EMPTY);
        //bitmap_to_chessboard(knight_to_moves);
        nPOS = knight_to_moves;
    }

    public static void white_knights() {
        long knight_to_moves = 0; //empty board
        long curr = bitmaps[gtidx('N')];
        //TODO: add myKingInCheck bit
        knight_to_moves |= ((curr >>> 6) | (curr >>> 10) | (curr >>> 15) | (curr >>> 17) | (curr << 6) | (curr << 10) | (curr << 15) | (curr << 17)) & (BLACKPIECES | EMPTY);
        //bitmap_to_chessboard(knight_to_moves);
        NPOS = knight_to_moves;
    }

    public static void black_bishops() {
        
    }

    public static void white_bishops() {

    }

    public static void black_rooks() {
        long rook_to_moves = 0; //empty board
        long curr = bitmaps[gtidx('r')];
        //TODO: add myKingInCheck bit
        for (int i = 0; i<board_size;i++){
            rook_to_moves |= 1;
        }
        //bitmap_to_chessboard(rook_to_moves);
        nPOS = rook_to_moves;
    }

    public static void white_rooks() {

    }

    public static void black_queens() {

    }

    public static void white_queens() {

    }

    public static void black_king() {
        long king_to_moves = 0; //empty board
        long curr = bitmaps[gtidx('k')];
        //TODO: opposite colored king cannot reach position & not red zone
        king_to_moves |= (curr >>> 8) & (EMPTY|WHITEPIECES); //move one up
        king_to_moves |= (curr >>> 9) & (EMPTY|WHITEPIECES); //move one left up
        king_to_moves |= (curr >>> 7) & (EMPTY|WHITEPIECES); //move one right up
        king_to_moves |= (curr >>> 1) & (EMPTY|WHITEPIECES); //move one left
        king_to_moves |= (curr << 8) & (EMPTY|WHITEPIECES); //move one down
        king_to_moves |= (curr << 9) & (EMPTY|WHITEPIECES); //move one right down
        king_to_moves |= (curr << 7) & (EMPTY|WHITEPIECES); //move one left down
        king_to_moves |= (curr << 1) & (EMPTY|WHITEPIECES); //move one right
        bitmap_to_chessboard(king_to_moves);
        kPOS = king_to_moves;
    }

    public static void white_king() {
        long king_to_moves = 0; //empty board
        long curr = bitmaps[gtidx('K')];
        //TODO: opposite colored king cannot reach position & not red zone
        king_to_moves |= (curr >>> 8) & (EMPTY|BLACKPIECES); //move one up
        king_to_moves |= (curr >>> 9) & (EMPTY|BLACKPIECES); //move one left up
        king_to_moves |= (curr >>> 7) & (EMPTY|BLACKPIECES); //move one right up
        king_to_moves |= (curr >>> 1) & (EMPTY|BLACKPIECES); //move one left
        king_to_moves |= (curr << 8) & (EMPTY|BLACKPIECES); //move one down
        king_to_moves |= (curr << 9) & (EMPTY|BLACKPIECES); //move one right down
        king_to_moves |= (curr << 7) & (EMPTY|BLACKPIECES); //move one left down
        king_to_moves |= (curr << 1) & (EMPTY|BLACKPIECES); //move one right
        bitmap_to_chessboard(king_to_moves);
        KPOS = king_to_moves;
    }

    public static void black_pieces() {
        black_pawns();
        black_knights();
        black_bishops();
        black_rooks();
        black_queens();
        black_king();
    }

    public static void white_pieces() {
        white_pawns();
        white_knights();
        white_bishops();
        white_rooks();
        white_queens();
        white_king();
    }

    public static void initiate_moves() { //initiates all moves
        black_pieces();
        white_pieces();
    }
}

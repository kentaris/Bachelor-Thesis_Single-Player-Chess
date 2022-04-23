package chess.engine.figures;

import static chess.engine.board.Bitboards.*;
import static chess.engine.figures.Figures.gtidx;
import static chess.engine.figures.Moves_Helper.diag_bitboard;
import static chess.engine.figures.Moves_Helper.hor_ver_bitboard;

public class Moves {
    public static long[] movemaps = new long[12]; //movement maps of all pieces
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
    public static long locOfbAttackers; //location of attackers which check the black king
    public static long locOfwAttackers;
    public static boolean BINCHECK; //if black king is in check
    public static boolean WINCHECK;


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
            pawn_to_captures = clearOverflow(curr, pawn_to_captures);
            if ((pawn_to_captures & bitmaps[gtidx('K')]) != 0L) { //if a black king is attacked
                nrOfbAttackers++;
                locOfbAttackers|=(((bitmaps[gtidx('K')]>>>7)&curr)|((bitmaps[gtidx('K')]>>>9)&curr)); //we shift the king position back to the desination where a pawn could have captured the king (both directions: >>>7 and >>>9) and then overlap it with the positions where pawns are located. The result is the pawn which is attacking the king. Ideally there is only one pawn attacking the king but this implementation allows for multiple pawns which attack the king if the start position is defining it so (we can't arrive there with legal moves).
            }
        }
        pPOSM = pawn_to_moves;
        movemaps[gtidx('p')] = pawn_to_moves | pawn_to_captures;
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
            pawn_to_captures = clearOverflow(curr, pawn_to_captures);
            if ((pawn_to_captures & bitmaps[gtidx('k')]) != 0L) { //if a black king is attacked
                nrOfwAttackers++;
                locOfwAttackers|=(((bitmaps[gtidx('k')]<<7)&curr)|((bitmaps[gtidx('k')]<<9)&curr));
            }
        }
        PPOSM = pawn_to_moves;
        movemaps[gtidx('P')] = pawn_to_moves | pawn_to_captures;
    }

    public static void black_knights() {
        long knight_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('n')];
        if (curr != 0) {
            //TODO: add myKingInCheck bit
            knight_to_moves |= ((curr >>> 6) | (curr >>> 10) | (curr >>> 15) | (curr >>> 17) | (curr << 6) | (curr << 10) | (curr << 15) | (curr << 17)) & (WHITEPIECES | EMPTY);
            clearOverflow(curr, knight_to_moves);
            int idx = gtidx('K');
            if ((knight_to_moves & bitmaps[idx]) != 0L) { //if a white king is attacked
                nrOfbAttackers++;
                locOfbAttackers|=(((bitmaps[idx]<<6  )&curr)|
                                  ((bitmaps[idx]<<10 )&curr)|
                                  ((bitmaps[idx]<<15 )&curr)|
                                  ((bitmaps[idx]<<17 )&curr)|
                                  ((bitmaps[idx]>>>6 )&curr)|
                                  ((bitmaps[idx]>>>10)&curr)|
                                  ((bitmaps[idx]>>>15)&curr)|
                                  ((bitmaps[idx]>>>17)&curr)
                                 );
            }
        }
        movemaps[gtidx('n')] = knight_to_moves;
    }

    public static void white_knights() {
        long knight_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('N')];
        if (curr != 0) {
            //TODO: add myKingInCheck bit
            knight_to_moves |= ((curr >>> 6) | (curr >>> 10) | (curr >>> 15) | (curr >>> 17) | (curr << 6) | (curr << 10) | (curr << 15) | (curr << 17)) & (BLACKPIECES | EMPTY);
            knight_to_moves = clearOverflow(curr, knight_to_moves);
            int idx = gtidx('k');
            if ((knight_to_moves & bitmaps[idx]) != 0L) { //if a white king is attacked
                nrOfwAttackers++;
                locOfwAttackers|=(((bitmaps[idx]<<6  )&curr)|
                                  ((bitmaps[idx]<<10 )&curr)|
                                  ((bitmaps[idx]<<15 )&curr)|
                                  ((bitmaps[idx]<<17 )&curr)|
                                  ((bitmaps[idx]>>>6 )&curr)|
                                  ((bitmaps[idx]>>>10)&curr)|
                                  ((bitmaps[idx]>>>15)&curr)|
                                  ((bitmaps[idx]>>>17)&curr)
                                );
            }
        }
        movemaps[gtidx('N')] = knight_to_moves;
    }

    public static void black_bishops() { //TODO
        long bishop_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('b')];
        if (curr != 0) {
            bishop_to_moves = diag_bitboard(curr, WHITEPIECES, BLACKPIECES);
        }
        movemaps[gtidx('b')] = bishop_to_moves;
    }

    public static void white_bishops() { //TODO
        long bishop_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('B')];
        if (curr != 0) {
            bishop_to_moves = diag_bitboard(curr, BLACKPIECES, WHITEPIECES);
        }
        movemaps[gtidx('B')] = bishop_to_moves;
    }

    public static void black_rooks() {
        long rook_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('r')];
        if (curr != 0) {
            rook_to_moves = hor_ver_bitboard(curr, WHITEPIECES, BLACKPIECES);
            //TODO: add myKingInCheck bit
        }
        movemaps[gtidx('r')] = rook_to_moves;
    }

    public static void white_rooks() {
        long rook_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('R')];
        if (curr != 0) {
            rook_to_moves = hor_ver_bitboard(curr, BLACKPIECES, WHITEPIECES);
            //TODO: add myKingInCheck bit
        }
        movemaps[gtidx('R')] = rook_to_moves;
    }

    public static void black_queens() {
        long queen_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('q')];
        if (curr != 0) {
            queen_to_moves |= hor_ver_bitboard(curr, WHITEPIECES, BLACKPIECES);
            queen_to_moves |= diag_bitboard(curr, WHITEPIECES, BLACKPIECES);
            //TODO: add myKingInCheck bit
        }
        movemaps[gtidx('q')] = queen_to_moves;
    }

    public static void white_queens() {
        long queen_to_moves = 0L; //empty board
        long curr = bitmaps[gtidx('Q')];
        if (curr != 0) {
            queen_to_moves |= hor_ver_bitboard(curr, BLACKPIECES, WHITEPIECES);
            queen_to_moves |= diag_bitboard(curr, BLACKPIECES, WHITEPIECES);
            //TODO: add myKingInCheck bit
        }
        movemaps[gtidx('Q')] = queen_to_moves;
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
            king_to_moves = clearOverflow(curr, king_to_moves);
        }
        movemaps[gtidx('k')] = king_to_moves;
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
            king_to_moves = clearOverflow(curr, king_to_moves);
        }
        movemaps[gtidx('K')] = king_to_moves;
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

    public static void initiate_inCheck() {
        if ((REDZONEB & bitmaps[gtidx('k')]) != 0L) { //if the black king is on an attackable square
            BINCHECK = true;
        }
        if ((REDZONEW & bitmaps[gtidx('K')]) != 0L) {
            WINCHECK = true;
        }
    }

    public static void initiate_red_zone_white() {
        /*this method does not initialize the moves. This method should only be called after the moves have been initialized otherwise we get the red-zone of the previous round. */
        REDZONEW = movemaps[gtidx('p')] | movemaps[gtidx('n')] | movemaps[gtidx('b')] | movemaps[gtidx('r')] | movemaps[gtidx('q')] | movemaps[gtidx('k')];
        REDZONEW -= pPOSM; //remove regular pawn movements as they are not attacking //TODO: consider en-passant move. How to deal with them? they're only dangerous to pawns. for now they are just ignored
        //System.out.println("white redzone:");
        //bitmap_to_chessboard(REDZONEW); //TODO: test with start pos fen
    }

    public static void initiate_red_zone_black() {
        /*this method does not initialize the moves. This method should only be called after the moves have been initialized otherwise we get the red-zone of the previous round. */
        REDZONEB = movemaps[gtidx('P')] | movemaps[gtidx('N')] | movemaps[gtidx('B')] | movemaps[gtidx('R')] | movemaps[gtidx('Q')] | movemaps[gtidx('K')];
        REDZONEB -= PPOSM; //remove regular pawn movements as they are not attacking
        //System.out.println("black redzone:");
        bitmap_to_chessboard(REDZONEB); //TODO: test with start pos fen
    }

    public static long clearOverflow(long bitboard, long movesbitboard) {
        //We don't need to worry about overflow to the top or bottom as those bits just disappear into nowhere. we do however need to clear the attack squares which overlap the file boarders and move to the other side of the board:
        if ((bitboard & (FILES[0] | FILES[1])) != 0L) { //if we are too far to the left, we need to clear the right...
            movesbitboard = (movesbitboard | (FILES[6] | FILES[7])) & (movesbitboard & ~(FILES[6] | FILES[7])); //clear the files on the right
        } else if ((bitboard & (FILES[6] | FILES[7])) != 0L) { //if we are too far to the right, we need to clear the left...
            movesbitboard = (movesbitboard | (FILES[0] | FILES[1])) & (movesbitboard & ~(FILES[0] | FILES[1])); //clear the files on the left
        }
        return movesbitboard;
    }
}

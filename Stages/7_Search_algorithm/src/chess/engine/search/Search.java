package chess.engine.search;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import static chess.engine.board.Bitboards.*;
import static chess.engine.board.Bitboards.get_single_figure_boards;
import static chess.engine.figures.Figures.gtfig;
import static chess.engine.figures.Figures.gtidx;
import static chess.engine.figures.Moves.*;
import static chess.engine.figures.Moves.whitesTurn;
import static chess.engine.figures.Moves_Helper.*;
import static java.lang.Math.round;
import static java.util.Objects.isNull;

public class Search {
    public static final int board_size = 8;

    private class State { //Search node which contains the bitboards
        long[] bitboard;

    }

    public static void main(String[] args) {
        String FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";//"r1pp4/1Pp5/1R6/4n3/3K4/2Bbn1k1/4QP2/4R1B1";
        nrOfbAttackers = 0;
        nrOfbAttackers = 0;
        initiate_boards(FEN); //non-bit operations which happen only at the start
        //TODO: check if there are multiple kings on the board of each color/ no kings on the board
        long t1 = System.nanoTime();
        boolean whitesTurn = false;
        initiate_next_moves(whitesTurn);
        Stack<long[]> successors = generate_successors();


        //System.out.println("# white attackers: "+nrOfwAttackers);
        //bitmap_to_chessboard(REDZONEW); //TODO: now working!
        //System.out.println("red-zone black ^");
        //System.out.println(BINCHECK);

        /*int[][] test = getMoves();

        for (int i=0;i< test.length;i++){
            String s = (i+1)+": "+convertMove(test[i]);
        }*/
        /*long valid_moves = 0L;
        for (int i = 0; i < 6; i++) {
            if (!isNull(movemapsIndividual[i])) {
                for (long m : movemapsIndividual[i]) {
                    valid_moves |= m;
                }
            }
        }
        bitmap_to_chessboard(valid_moves);
        System.out.println("black valid moves ^");*/
        //TODO: if there are no more black movements available and it's black's turn, then generate no more children
        //bitmap_to_chessboard(valid_moves);
        //System.out.println("white ^");
        /*bitmap_to_chessboard(BATTACKED);
        System.out.println("black attacked pieces ^");
        bitmap_to_chessboard(WATTACKED);
        System.out.println("white attacked pieces ^");
        bitmap_to_chessboard(BPROTECTED);
        System.out.println("black PROTECTED pieces ^");
        bitmap_to_chessboard(WPROTECTED);
        System.out.println("white PROTECTED pieces ^");*/
        long t2 = System.nanoTime();
        long T = t2 - t1;
        long s = TimeUnit.SECONDS.convert(T, TimeUnit.NANOSECONDS);
        long s_int = s - (round(s / 1000) * 1000);
        long ms = TimeUnit.MILLISECONDS.convert(T, TimeUnit.NANOSECONDS);
        long ms_int = ms - (round(ms / 1000) * 1000);
        long µs = TimeUnit.MICROSECONDS.convert(T, TimeUnit.NANOSECONDS);
        long µs_int = µs - (round(µs / 1000) * 1000);
        long ns = TimeUnit.NANOSECONDS.convert(T, TimeUnit.NANOSECONDS);
        long ns_int = ns - (round(ns / 1000) * 1000);
        System.out.println(String.format("\n\u001B[33m[%ss %sms %sµs %sns execution time]\u001B[0m", s_int, ms_int, µs_int, ns_int));
    }
}

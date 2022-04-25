package chess.engine.search;

import static chess.engine.board.Bitboards.*;
import static chess.engine.figures.Moves.*;
import static chess.engine.figures.Moves_Helper.*;

public class Search {
    public static final int board_size = 8;

    public static void main(String[] args) {
        String FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";//"r1pp4/1Pp5/1R6/4n3/3K4/2Bbn1k1/4QP2/4R1B1";
        nrOfbAttackers=0;
        nrOfbAttackers=0;
        initiate_boards(FEN); //non-bit operations which happen only at the start
        //TODO: check if there are multiple kings on the board of each color/ no kings on the board
        long t1 = System.currentTimeMillis();
        initiate_next_moves();
        initiate_red_zone_black();
        initiate_red_zone_white();
        initiate_inCheck();
        unite_movements(); //update movemaps to reflect updated individual movements.
        System.out.println("# white attackers: "+nrOfwAttackers);
        bitmap_to_chessboard(REDZONEB);
        System.out.println("red-zone black ^");
        valid_black_moves(); //TODO: currently the valid moves are only printed, not returned and converted to valid moves
        long valid_moves=0L;
        for (int i = 0;i<6;i++){
            valid_moves|=movemaps[i];
        }
        bitmap_to_chessboard(valid_moves);
        valid_white_moves();
        /*for (int i = 6;i<12;i++){
            valid_moves|=movemaps[i];
        }
        bitmap_to_chessboard(valid_moves);*/
        /*bitmap_to_chessboard(BATTACKED);
        System.out.println("black attacked pieces ^");
        bitmap_to_chessboard(WATTACKED);
        System.out.println("white attacked pieces ^");
        bitmap_to_chessboard(BPROTECTED);
        System.out.println("black PROTECTED pieces ^");
        bitmap_to_chessboard(WPROTECTED);
        System.out.println("white PROTECTED pieces ^");*/
        long t2 = System.currentTimeMillis();
        System.out.println(String.format("\n\u001B[33m[%sms execution time]\u001B[0m", t2 - t1));
    }
}

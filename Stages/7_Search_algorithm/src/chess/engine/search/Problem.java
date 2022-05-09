package chess.engine.search;

import static chess.engine.board.Bitboards.*;
import static chess.engine.figures.Moves.*;
import static chess.engine.search.Search.convertMove;
import static chess.engine.search.Search.getMove;
import static java.lang.Math.round;
import static java.util.Objects.isNull;

public class Problem {
    public static final int board_size = 8;
    public static int n = 0;
    public static int current_depth = 1;
    public static int desired_depth;
    public static long[] goal_state;
    public static long[] root; //start_state
    public static NODE root_node;

    public static void initialize() {
        String start_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";//"r1pp4/1Pp5/1R6/4n3/3K4/2Bbn1k1/4QP2/4R1B1";
        String goal_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
        Character[][] start_board = {
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {'r', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {'P', 'P', 'P', 'P', 'P', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
        };
        Character[][] goal_board = {
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {'P', 'P', 'P', 'P', 'P', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', 'r'}
        };
        //toggle the following two lines to switch input method:
        goal_state = return_arrayToBitboards(goal_board);
        initiate_custom_chessBoard(start_board);
        //goal_state = FEN_to_chessboard(goal_FEN);
        //initiate_FEN_to_chessboard(start_FEN);

        whitesTurn = true;
        System.out.println("=======Start Board=======");
        initiate_boards(); //non-bit operations which happen only at the start
        System.out.println("=========================\n");

        //System.out.println(convertMove(getMove(bitmaps,goal_state)));
        //System.exit(9);
    }

    public NODE INITIAL() {
        if (isNull(root)) {
            initialize();
            root = bitmaps.clone(); //get initialized parent node

            root_node = new NODE(null, root, 0L, 0, whitesTurn);
        }
        Main.t1 = System.nanoTime(); //initialization is included here now...
        return root_node;
    }

    public static NODE makeNode(NODE parent, long[] state, long difference, int path_cost, boolean wTurn) {
        NODE node = new NODE(parent, state, difference, path_cost, wTurn);
        return node;
    }

    public static boolean IS_GOAL(STATE s) {
        /*Goal test which takes around 1-2µs to execute if true and less if false.*/
        for (int i = 0; i < 12; i++) {
            if (s.state[i] != goal_state[i]) { //if we find a bitboard that doesn't match with that goal bitboard, we return false
                return false;
            }
        }
        return true; //if all matched, we return true
    }
}

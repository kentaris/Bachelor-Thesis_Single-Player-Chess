package chess.engine.search;

import static chess.engine.board.Bitboards.*;
import static chess.engine.figures.Moves.*;
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
                {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'},
                {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
                {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}
        };
        Character[][] goal_board = {
                {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'},
                {'p', 'p', ' ', ' ', ' ', ' ', ' ', 'p'},
                {' ', ' ', 'p', 'p', 'p', 'p', 'p', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', 'P', 'P', 'N', ' ', ' '},
                {'P', 'P', 'P', ' ', 'B', 'P', 'P', 'P'},
                {'R', 'N', 'B', 'Q', ' ', 'R', ' ', 'K'}

                /*{'r', 'n', 'b', 'q', 'k', 'b', ' ', 'r'}, //out of memory
                {' ', 'p', ' ', ' ', ' ', 'p', 'p', 'p'},
                {'p', ' ', ' ', ' ', 'p', 'n', ' ', ' '},
                {' ', ' ', 'p', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', 'B', 'P', ' ', ' ', ' ', ' '},
                {' ', ' ', 'N', ' ', 'P', 'N', ' ', ' '},
                {'P', 'P', ' ', ' ', ' ', 'P', 'P', 'P'},
                {'R', ' ', 'B', 'Q', ' ', 'R', 'K', ' '}*/
        };
        //toggle the following two lines to switch input method:
        goal_state = return_arrayToBitboards(goal_board);
        initiate_custom_chessBoard(start_board);
        //goal_state = FEN_to_chessboard(goal_FEN);
        //initiate_FEN_to_chessboard(start_FEN);
        /*Heuristic h = new Heuristic();
        System.out.println(h.f(bitmaps,goal_state));
        System.exit(9);*/

        whitesTurn = true;
        System.out.print(" " + "\u2500".repeat(6) + "Init Board" + "\u2500".repeat(6));
        initiate_boards(); //non-bit operations which happen only at the start
        System.out.println(" " + "\u2500".repeat(22));

        //System.out.println(convertMove(getMove(bitmaps,goal_state)));
        //System.exit(9);
    }

    public NODE INITIAL() {
        if (isNull(root)) {
            initialize();
            root = bitmaps.clone(); //get initialized parent node
            root_node = new NODE(null, root, 0L, 0, whitesTurn, 0);
        }
        Main.t1 = System.nanoTime();
        return root_node;
    }

    public NODE INITIAL2(Problem problem, Heuristic heuristic) {
        if (isNull(root)) {
            initialize();
            root = bitmaps.clone(); //get initialized parent node
            root_node = new NODE(null, root, 0L, 0, whitesTurn, heuristic.f(problem, root, whitesTurn, 0));
        }
        //System.out.println(heuristic.f(problem,problem.goal_state,whitesTurn));
        //System.exit(9);
        Main.t1 = System.nanoTime();
        return root_node;
    }

    public static NODE makeNode(NODE parent, long[] state, long difference, int path_cost, boolean wTurn, int heuristic) {
        NODE node = new NODE(parent, state, difference, path_cost, wTurn, heuristic);
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

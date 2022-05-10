package chess.engine.search;

import java.util.Stack;

import static chess.engine.board.Bitboards.bitmap_to_chessboard;
import static chess.engine.board.Bitboards.bitmaps_to_chessboard;
import static chess.engine.search.Search.unite;

public class Heuristic {
    public int INFINITY = 2147483647; //maximal int value

    public int f(Problem problem, long[] current, boolean wTurn) {
        /*count number of out of place figures*/
        long[] goal = problem.goal_state;
        int n = 0;
        int[] missingFigures = new int[12];
        for (int i = 0; i < 12; i++) {
            n += Long.bitCount(current[i] & ~(current[i] & goal[i]));
            //outOfPlace[i] = Long.bitCount(current[i] & ~(current[i] & goal[i]));
            //if there are more pieces at the goal position (otherwise an empty board would always return 0 no matter the goal board position)
            int c = Long.bitCount(current[i]);
            int g = Long.bitCount(goal[i]);
            if (g > c) { //if goal state has more figures of type T than start state
                int diff = g - c;
                missingFigures[i] = diff;
                n += diff;
            }
        }
        if (UNSOLVABLE(problem, current, wTurn, missingFigures)) {
            return INFINITY;
        }
        return n*10;
    }

    private boolean UNSOLVABLE(Problem problem, long[] current, boolean wTurn, int[] missingFigures) {
        int start = 0; //blacks turn
        if (wTurn) {
            start = 6;
        }
        int end = 6; //blacks turn
        if (wTurn) {
            end = 12;
        }
        int nrOfPawns = Long.bitCount(current[start]);
        for (int i = start; i < end; i++) {
            //Unsolvable Cases:
            //System.out.println(nrOfPawns+" "+missingFigures[i]+i+wTurn);
            //System.exit(5);
            if (nrOfPawns < missingFigures[i]) {
                return true; //unsolvable
            }
        }
        return false; //solvable
    }
}

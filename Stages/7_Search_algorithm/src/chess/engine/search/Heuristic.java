package chess.engine.search;

import java.util.Stack;

import static chess.engine.board.Bitboards.*;
import static chess.engine.search.Search.unite;
import static java.util.Objects.isNull;

public class Heuristic {
    public int INFINITY = 2147483647; //maximal int value
    boolean A_Star = false;
    Double weight = null;

    public int f(Problem problem, long[] current, boolean wTurn, int path_cost) {
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
        if (A_Star) {
            if (isNull(weight)) {
                return n + path_cost; //A-Star
            } else {
                return ((int) (weight * n) + path_cost); //weighted A-Star
            }
        } else { //Greedy-Best-First Search
            return n;
        }
    }

    private boolean UNSOLVABLE(Problem problem, long[] current, boolean wTurn, int[] missingFigures) {
        //-----initializing variables:
        int start = 0; //blacks turn
        if (wTurn) {
            start = 6;
        }
        int end = 6; //blacks turn
        if (wTurn) {
            end = 12;
        }
        int totDiff = 0;
        int nrOfPawns = Long.bitCount(current[start]);
        for (int i = start; i < end; i++) {
            totDiff += missingFigures[i];
        }
        int[] lastPawnsGoal = problem.lastPawnsGoalB;
        int[] lastPawnCurr = get_last_pawns(current[start]);
        //----------------------------
        //Unsolvable Cases:
        if (nrOfPawns < totDiff) { //if number of pawns is smaller than missing pieces
            return true; //unsolvable
        }

        if (wTurn) { //if pawn in last row of color c is further ahead than the pawn in the last row of the goal state
            lastPawnsGoal = problem.lastPawnsGoalW;
            if (lastPawnCurr[1] < lastPawnsGoal[1]) {
                return true; //unsolvable
            }
        } else {
            if (lastPawnCurr[0] > lastPawnsGoal[0]) {
                return true; //unsolvable
            }
        }

        if (wTurn) { //number of pawns in goal state is bigger than in current state (since we cannot create new pawns)
            if (problem.nrPawnsB > nrOfPawns) {
                return true; //unsolvable
            }
        } else {
            if (problem.nrPawnsW > nrOfPawns) {
                return true; //unsolvable
            }
        }
        return false; //solvable
    }
}

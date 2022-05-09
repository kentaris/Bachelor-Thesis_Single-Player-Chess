package chess.engine.search;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import static chess.engine.board.Bitboards.*;
import static chess.engine.figures.Figures.gtfig;
import static chess.engine.figures.Moves.*;
import static chess.engine.figures.Moves_Helper.*;

import static chess.engine.search.Problem.board_size;
import static java.lang.Math.round;
import static java.util.Objects.isNull;

public class Search {
    public static int n = 0;
    public static int counter = 0;
    public static Queue<NODE> frontier = new LinkedList<>();
    public static Queue<STATE> reached = new LinkedList<>();
    //timing:


    /*public static Stack<long[]> get_children(long[] parent, long difference, boolean wTurn) {
        /*expands a given position and returns it's children*/
        /*if (current_depth == desired_depth) { //if we reached the desired depth, return no more children
            return null;
        }*//*
        initiate_next_moves(parent, difference, wTurn);
        Stack<long[]> children = generate_successors(difference);
        n += children.size();
        return children;
    }*/
    public NODE[] EXPAND(Problem problem, NODE node) {
        initiate_next_moves(node);
        Stack<long[]> children = generate_successors(node);
        int size = children.size();
        NODE[] nodes = new NODE[size];
        n += size;
        boolean wTurn = true; //we assume it is white's turn...
        if (node.STATE.wTurn) { //...but if input node (=new parent node) represents a state where it is white's Turn
            wTurn = false; //then the child state must be a state where it is black's turn
        }
        for (int i = 0; i < size; i++) {
            long[] child = children.pop();
            long difference = get_diff(node.STATE.state, child);
            nodes[i] = Problem.makeNode(node, child, difference, node.PATH_COST + 1, wTurn);
        }
        return nodes;
    }

    public static void print_Stack(Stack<long[]> children) {
        int k = children.size() - 1;
        System.out.println("expanding " + children.size() + " nodes:");
        Stack<long[]> copy = (Stack<long[]>) children.clone();
        while (!copy.isEmpty()) {
            String turn = "black";
            if (whitesTurn) {
                turn = "white";
            }
            System.out.println(String.format(k + " - \u001B[34m%s's turn\u001B[0m", turn));
            bitmaps_to_chessboard(copy.pop());
            System.out.println();
            k--;
        }
    }

    public static String[] EXTRACT_SOLUTION_ACTIONS(LinkedList<long[]> path, int size) {
        String[] actions = new String[size - 1];
        for (int i = 0; i < size - 1; i++) { //we go through all pairs of from-to boards forwards (we already reversed the path) until we reach the node before the solution node (since solution node is included in that pair)
            actions[i] = convertMove(getMove(path.get(i+1), path.get(i)));
        }
        return actions;
    }

    public static void print_children(long[] parent, Stack<long[]> children) {
        int k = children.size() - 1;
        System.out.println("expanding " + children.size() + " nodes:");
        Stack<long[]> copy = (Stack<long[]>) children.clone();
        while (!copy.isEmpty()) {
            String turn = "black";
            if (whitesTurn) {
                turn = "white";
            }
            System.out.println(String.format(k + " - \u001B[34m%s's turn\u001B[0m", turn));
            two_bitmaps_to_chessboard(parent, copy.pop());
            System.out.println();
            k--;
        }
    }

    /*
    public static Stack<long[]> gather_children(Stack<long[]> parents, Stack<long[]> children, boolean wTurn) {
        Stack<long[]> children_in_current_depth_level = new Stack<>();
        while (!parents.isEmpty()) { //for all parents
            long[] parent = parents.pop(); //individual parent
            while (!children.isEmpty()) { //for all children of the parents
                long[] child = children.pop(); //individual child
                Stack<long[]> new_children = get_children(child, parent, !wTurn); //we gather the children of one individual parent here
                while (!new_children.isEmpty()) { //not sure if there is an append funtion which can append this substack to the end of my stack...
                    children_in_current_depth_level.push(children.pop());
                }
            }
        }
        n += children.size();
        current_depth++; //increase the current depth by 1
        return children_in_current_depth_level;
    }

    public static Stack<long[]> generate_depth_n(Stack<long[]> children, Stack<long[]> parents, boolean whitesTurn) {
        if (children.isEmpty()) {
            return parents;
        } else {
            Stack<long[]> new_children = gather_children(children, parents, whitesTurn);
            n += children.size();
            generate_depth_n(new_children, children, !whitesTurn);
        }
        return null; //not able to generate successors
    }*/

    public static boolean invert(boolean turn) {
        if (turn) {
            //whitesTurn = false;
            return false;
        }
        //whitesTurn = true;
        return true;
    }

    public static int[] getMove(long[] parent, long[] child) {
        //long[] from = parent;
        //long[] to = child;
        //two_bitmaps_to_chessboard(from,to);
        long difference = get_diff(parent, child);
        long from_pos = unite(parent) & difference;
        long to_pos = unite(child) & difference;
        int from_idx = get_squareIndex_of_figure(from_pos);
        int to_idx = get_squareIndex_of_figure(to_pos);
        int from_file = from_idx % board_size;
        int from_rank = 8 - (from_idx / board_size);
        int to_file = to_idx % board_size;
        int to_rank = 8 - (to_idx / board_size);
        int[] coordinate = {from_file, from_rank, to_file, to_rank}; //a3h3
        //System.out.println(from_file + " " + from_rank + " " + to_file + " " + to_rank);
        return coordinate;
    }

    public static long unite(long[] state) {
        long s = 0L;
        for (long map : state) {
            s |= map;
        }
        return s;
    }

    public static String convertMove(int[] move) {
        StringBuilder builder = new StringBuilder();
        builder.append((char) (move[0] + 97));
        builder.append(move[1]);
        builder.append((char) (move[2] + 97));
        builder.append(move[3]);
        return builder.toString();
    }

    public static long get_diff(long[] old_parent, long[] parent) {
        long diff = 0L;
        for (int i = 0; i < 12; i++) {
            diff |= old_parent[i] ^ parent[i];
        }
        return diff;
    }

    public LinkedList<long[]> EXTRACT_PATH(NODE node) {
        LinkedList<long[]> path = new LinkedList<>();
        while (!isNull(node.PARENT)) {
            path.add(node.STATE.state);
            node = node.PARENT;
        }
        path.add(node.STATE.state); //add root node as well
        return path;
    }

    public NODE BFS(Problem problem) {
        //TODO: Exit if goal state can't be reached cause of insufficient material
        NODE root = problem.INITIAL();
        if (problem.IS_GOAL(root.STATE)) return root;
        frontier.add(root);
        reached.add(root.STATE);
        //long t1 = System.currentTimeMillis();
        //long t2 = System.currentTimeMillis();
        while (!frontier.isEmpty()){ //& t2-t1<1000) {
            //t2=System.currentTimeMillis();
            NODE node = frontier.poll(); //pop
            NODE[] EXPAND = EXPAND(problem, node);
            for (NODE child : EXPAND) {
                System.out.print("\rcurrently expanded nodes: " + counter + child.STATE.wTurn);
                counter += 1;
                STATE s = child.STATE;
                if (problem.IS_GOAL(s)) return child;
                if (!reached.contains(s)) {
                    reached.add(s);
                    frontier.add(child);
                }
            }
        }
        //Stack<long[]> children = EXPAND(parent, 0L, whitesTurn); //create possible children nodes
        return null; //failure
    }
}
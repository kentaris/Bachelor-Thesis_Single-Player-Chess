package chess.engine.search;

import java.util.*;

import static chess.engine.board.Bitboards.*;
import static chess.engine.figures.Moves.*;
import static chess.engine.figures.Moves_Helper.*;

import static chess.engine.search.Problem.board_size;

import java.util.PriorityQueue;

import static chess.engine.search.Problem.root_node;
import static java.util.Objects.isNull;

public class Search {
    public static int n = 0;
    public static int counter = 0;
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
        System.out.print("\rcurrently expanded nodes: " + n);
        boolean wTurn = true; //we assume it is white's turn...
        if (node.STATE.wTurn) { //...but if input node (=new parent node) represents a state where it is white's Turn
            wTurn = false; //then the child state must be a state where it is black's turn
        }
        for (int i = 0; i < size; i++) {
            long[] child = children.pop();
            long difference = get_diff(node.STATE.state, child);
            nodes[i] = Problem.makeNode(node, child, difference, node.PATH_COST + 1, wTurn, 0);
        }
        return nodes;
    }

    public NODE[] EXPAND2(Problem problem, Heuristic heuristic, NODE node) {
        initiate_next_moves(node);
        Stack<long[]> children = generate_successors(node);
        int size = children.size();
        NODE[] nodes = new NODE[size];
        n += size;
        System.out.print("\rcurrently expanded nodes: " + n);
        boolean wTurn = true; //we assume it is white's turn...
        if (node.STATE.wTurn) { //...but if input node (=new parent node) represents a state where it is white's Turn
            wTurn = false; //then the child state must be a state where it is black's turn
        }
        for (int i = 0; i < size; i++) {
            long[] child = children.pop();
            long difference = get_diff(node.STATE.state, child);
            nodes[i] = Problem.makeNode(node, child, difference, node.PATH_COST + 1, wTurn, heuristic.f(problem, child, wTurn));
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
        if (size > 1) {
            //System.out.println(size);
            for (int i = 0; i < size - 1; i++) { //we go through all pairs of from-to boards backwards until we reach the root node (since root node is included in that pair)
                //System.out.println(i);
                //bitmaps_to_chessboard(path.get(i));
                actions[(size - 2) - i] = convertMove(getMove(path.get(i + 1), path.get(i)));
            }
        } else actions[0] = "No Action needed!";
        //for (String s:actions) System.out.println(s);
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
        //TODO: castling:
        /*
        if (bitcount(bitmap)>1) then assume it's castling
        */
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
            path.add(node.STATE.state); //adds nodes from goal state to root state
            node = node.PARENT;
        }
        path.add(node.STATE.state); //add root node as well
        return path;
    }

    public NODE BreadthFirst_Search(Problem problem) {
        ArrayList<NODE> frontier = new ArrayList<>();
        HashMap<STATE, Integer> reached = new HashMap<>(); //we use a hash map because it is faster than array list
        NODE root = problem.INITIAL();
        if (problem.IS_GOAL(root.STATE)) return root;
        frontier.add(root);
        reached.put(root.STATE, 0);
        while (!frontier.isEmpty()) {
            NODE node = frontier.remove(0); //pop
            NODE[] EXPAND = EXPAND(problem, node);
            for (NODE child : EXPAND) {
                STATE s = child.STATE;
                if (problem.IS_GOAL(s)) return child;
                if (!reached.containsKey(s)) {
                    reached.put(s, 0);
                    frontier.add(child);
                }
            }
        }
        return null; //failure
    }

    public NODE BestFirst_Search(Problem problem, Heuristic heuristic) {
        PriorityQueue<NODE> frontier = new PriorityQueue<>();
        Map<STATE, Integer> reached = new HashMap<>(); //Lookup Table
        NODE root = problem.INITIAL2(problem, heuristic);
        NODE cheapestNode = root;
        if (root.HEURISTIC_VALUE == heuristic.INFINITY) {
            return null; //failure (unreachable goal state)
        }
        frontier.add(root);
        reached.put(root.STATE, root.HEURISTIC_VALUE);//a lookup table, with one entry with key problem.INITIAL and value node
        while (!frontier.isEmpty()) {
            if (n > 18000000) {
                System.out.println("Out of Memory");
                return null; //out of Memory
            }
            NODE node = frontier.poll(); //pop
            if (problem.IS_GOAL(node.STATE)) return node;
            NODE[] EXPAND = EXPAND2(problem, heuristic, node);
            for (NODE child : EXPAND) {
                STATE s = child.STATE;
                if (child.PATH_COST < cheapestNode.PATH_COST) {
                    cheapestNode = child;
                }
                if (child.HEURISTIC_VALUE > heuristic.INFINITY) {
                    continue; //skip
                }
                if (!reached.containsKey(s) | child.PATH_COST < cheapestNode.PATH_COST) {
                    reached.put(s, child.HEURISTIC_VALUE);
                    frontier.add(child);
                }
            }
        }
        return null; //failure (search finished without a solution)
    }
}
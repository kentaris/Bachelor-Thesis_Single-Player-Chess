package chess.engine.search;

import java.util.*;

import static chess.engine.board.Bitboards.*;
import static chess.engine.figures.Figures.gtfig;
import static chess.engine.figures.Moves.*;
import static chess.engine.figures.Moves_Helper.*;

import static chess.engine.search.Problem.board_size;

import java.util.PriorityQueue;

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
            nodes[i] = Problem.makeNode(node, child, difference, node.STATE.path_cost + 1, wTurn, 0);
        }
        return nodes;
    }

    public NODE[] EXPAND2(Problem problem, Heuristic heuristic, NODE node) {
        initiate_next_moves(node);
        Stack<long[]> children = generate_successors(node);
        int size = children.size();
        NODE[] nodes = new NODE[size];
        n += size;
        //System.out.print("\rcurrently expanded nodes: " + n);
        boolean wTurn = true; //we assume it is white's turn...
        if (node.STATE.wTurn) { //...but if input node (=new parent node) represents a state where it is white's Turn
            wTurn = false; //then the child state must be a state where it is black's turn
        }
        for (int i = 0; i < size; i++) {
            long[] child = children.pop();
            long difference = get_diff(node.STATE.state, child);
            int path_cost = node.STATE.path_cost + 1;
            nodes[i] = Problem.makeNode(node, child, difference, path_cost, wTurn, heuristic.f(problem, child, wTurn, path_cost));
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

    public static String[] EXTRACT_SOLUTION_ACTIONS(LinkedList<NODE> path, int size) {
        String[] actions = new String[size - 1];
        if (size > 1) {
            //System.out.println(size);
            for (int i = 0; i < size - 1; i++) { //we go through all pairs of from-to boards backwards until we reach the root node (since root node is included in that pair)
                //System.out.println(i);
                //bitmaps_to_chessboard(path.get(i));
                actions[(size - 2) - i] = convertMove(getMove(path.get(i + 1), path.get(i)), path.get(i));
            }
        } else {
            String[] a = {"No Action needed!"};
            return a;
        }
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

    public static Integer[] getMove(NODE parent, NODE child) {
        //long[] from = parent;
        //long[] to = child;
        //two_bitmaps_to_chessboard(from,to);
        //long difference = child.STATE.difference;
        Integer[] pos = set_AkrAtkdCastl(parent, child);
        //System.out.println(Arrays.toString(pos));
        int from_square = pos[0];
        int to_square = pos[1];
        int from_file = from_square % board_size;
        int from_rank = 8 - (from_square / board_size);
        int to_file = to_square % board_size;
        int to_rank = 8 - (to_square / board_size);
        Integer moved = parent.MovedCapturedCastling[0];
        Integer captured = parent.MovedCapturedCastling[1];
        Integer castling = parent.MovedCapturedCastling[2];
        Integer[] coordinate = {from_file, from_rank, to_file, to_rank, moved, captured, castling}; //a3h3
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

    public static String convertMove(Integer[] move, NODE node) {
        String captured = "";
        String figure = Character.toString(gtfig(move[4]));
        //0:from_file, 1:from_rank, 2:to_file, 3:to_rank, 4:mover, 5:captured, 6:castling
        if (!isNull(move[6])) { //castling
            if (move[6] == 1) return "O-O";//kingside Castling
            else return "O-O-O"; //queenside castling
        }
        if (!isNull(move[5])) { //captured
            //captured = Character.toString(gtfig(move[1]));
            //System.out.println(Arrays.toString(move));
            //System.exit(5);
            captured = "x";
        }
        /*if (move[4] == 3) {
            System.out.println(Arrays.toString(move));
            System.exit(6);
        }*/
        StringBuilder builder = new StringBuilder();
        builder.append(figure + " ");
        builder.append((char) (move[0] + 97));
        builder.append(move[1]);
        builder.append(captured);
        builder.append((char) (move[2] + 97));
        builder.append(move[3]);
        return builder.toString();
    }

    public static long get_diff(long[] parent, long[] child) {
        long diff = 0L;
        for (int i = 0; i < 12; i++) {
            diff |= parent[i] ^ child[i];
        }
        return diff;
    }

    public static Integer[] set_AkrAtkdCastl(NODE parent, NODE child) {
        int start = 0; //blacks turn
        if (parent.STATE.wTurn) {
            start = 6;
        }
        int end = 6; //blacks turn
        if (parent.STATE.wTurn) {
            end = 12;
        }
        Integer mover = null;
        Integer captured = null;
        Integer castling = null;
        Integer[] pos = {null, null}; //from_square ,to_square
        long MOVER_FROM = 0L;
        long MOVER_TO = 0L;
        for (int i = end - 1; i > start - 1; i--) { //find out who is moving: we go backwards, so we prioritize the king (for castling)
            long parent_i = parent.STATE.state[i];
            long child_i = child.STATE.state[i];
            long check = (child_i & ~(parent_i & child_i));
            if (check != 0L) { //if no figure moved then this will be = 0L - if at least one moved it won't be =0L
                mover = i;//we only set mover one time and do not override it after
                MOVER_FROM = parent_i & child.STATE.difference;
                pos[0] = get_squareIndex_of_figure(MOVER_FROM);//from_square
                MOVER_TO = check;
                pos[1] = get_squareIndex_of_figure(MOVER_TO); //to_square
                /*bitmap_to_chessboard(parent_i & child.STATE.difference);
                System.out.println(pos[0]);
                bitmap_to_chessboard(check);
                System.out.println(pos[1]);
                System.exit(2);*/
                break;
            }
        }
        for (int i = 0; i < 12; i++) { //find out who is captured (if someone is)
            long parent_i = parent.STATE.state[i];
            if ((parent_i & MOVER_TO) != 0L & i != mover) { //parent map had a piece at the position and now it's gone
                //bitmap_to_chessboard(child_i);
                //bitmap_to_chessboard(MOVER_TO);
                captured = i;
                break;
            }
        }
        if (mover == 5 | mover == 11) { //king is moving
            for (int i = 0; i < 12; i++) {
                //long parent_i = parent.STATE.state[i];
                long child_i = child.STATE.state[i];
                //if ((parent_i & MOVER_TO) != 0L & i != mover & i != captured) {
                if ((child_i & KINGSIDE) != 0L & i != mover) { //kingside =1 ,
                    if ((MOVER_FROM<<2)==MOVER_TO) {
                        castling = 1;
                        break;
                    }
                } else if ((child_i & QUEENSIDE) != 0L & i != mover){//queenside =2
                    if ((MOVER_FROM>>>2)==MOVER_TO) {
                        castling = 2;
                        break;
                    }
                }
            }
        }
        Integer[] moved = {mover, captured, castling};
        //System.out.println(Arrays.toString(moved));
        parent.MovedCapturedCastling = moved;
        //System.exit(6);
        return pos;
    }

    public LinkedList<NODE> EXTRACT_PATH(NODE node) {
        LinkedList<NODE> path = new LinkedList<>();
        while (!isNull(node.PARENT)) {
            path.add(node); //adds nodes from goal state to root state
            node = node.PARENT;
        }
        path.add(node); //add root node as well
        return path;
    }

    public NODE BreadthFirst_Search(Problem problem) {
        /*int d = 0; //<<<
        Search search = new Search(); //<<<*/
        ArrayList<NODE> frontier = new ArrayList<>();
        HashMap<STATE, Integer> reached = new HashMap<>(); //we use a hash map because it is faster than array list
        NODE root = problem.INITIAL(problem);
        if (problem.IS_GOAL(root.STATE)) return root;
        frontier.add(root);
        //int frontier_size = 0;
        reached.put(root.STATE, 0);
        while (!frontier.isEmpty()) {
            /*int c = 0; //<<<
            d += 1; //<<<
            System.out.println("===========" + d + "==========="); //<<<*/
            NODE node = frontier.remove(0); //pop
            //frontier_size -= 1;
            NODE[] EXPAND = EXPAND(problem, node);
            for (NODE child : EXPAND) {
                /*System.out.println("c: "+c); //<<<
                c += 1; //<<<
                two_bitmaps_to_chessboard(node.STATE.state, child.STATE.state); //<<<
                LinkedList<NODE> path = search.EXTRACT_PATH(child); //<<<
                int size = path.size(); //<<<
                String[] solution = EXTRACT_SOLUTION_ACTIONS(path, size); //<<<
                for (int i = 0; i < size - 1; i++) //<<<
                System.out.println("\t  \u2502" + (i + 1) + ": " + solution[i] + "\u2502"); //<<<*/
                /*if (child.STATE.path_cost > 2) { //<<<
                    System.exit(7); //<<<
                }*/ //<<<
                STATE s = child.STATE;
                if (problem.IS_GOAL(s)) {
                    //bitmaps_to_chessboard(s.state);
                    return child;
                }
                if (!reached.containsKey(s)) {
                    reached.put(s, 0);
                    frontier.add(child);
                    //frontier_size += 1;
                }
            }
        }
        return null; //failure
    }

    public NODE BestFirst_Search(Problem problem, Heuristic heuristic) {
        NODE root = problem.INITIAL2(problem, heuristic);
        PriorityQueue<NODE> frontier = new PriorityQueue<>();
        Map<STATE, NODE> reached = new HashMap<>(); //Lookup Table

        if (root.STATE.heuristic_value <= heuristic.INFINITY) { //Optimization
            frontier.add(root);
        }
        reached.put(root.STATE, root);//a lookup table, with one entry with key problem.INITIAL and value node
        while (!frontier.isEmpty()) {
            NODE node = frontier.poll(); //pop
            if (problem.IS_GOAL(node.STATE)) return node;
            NODE[] EXPAND = EXPAND2(problem, heuristic, node);
            for (NODE child : EXPAND) {
                STATE s = child.STATE;
                if (problem.IS_GOAL(s)) { //early goal detection
                    return child;
                }
                if (child.STATE.heuristic_value == heuristic.INFINITY ) { //optimization
                    continue; //skip
                }
                /*if(!reached.containsKey(root.STATE)){
                    System.exit(3);
                }*/
                if (!reached.containsKey(s)) {//"§ Each child node is added to the frontier if it has not been reached before, §"
                    reached.put(s, child);
                    frontier.add(child);
                }else if (child.STATE.path_cost < reached.get(s).STATE.path_cost) {//"§ OR: re-added if child node is now being reached with a path that has a lower path cost than any previous path. §"
                    reached.replace(s, child); //update value
                    frontier.add(child);
                    //System.out.println(s.heuristic_value+" "+s.path_cost);
                }
                //else System.out.println("test");
            }
        }
        return null; //failure (search finished without a solution)
    }
}



        /*//=====================DELETE============================
        NODE node = frontier.remove(0); //pop
        NODE[] e = EXPAND(problem, node);
        for (int i=0;i<e.length;i++) {
            System.out.println(i+":");
            two_bitmaps_to_chessboard(e[i].PARENT.STATE.state,e[i].STATE.state);
        }
        node = e[2];
        e = EXPAND(problem, node);
        for (int i=0;i<e.length;i++) {
            System.out.println(i+":");
            two_bitmaps_to_chessboard(e[i].PARENT.STATE.state,e[i].STATE.state);
        }
        System.exit(9);
        //=====================DELETE============================*/
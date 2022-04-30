package chess.engine.search;

import java.util.Stack;
import java.util.concurrent.TimeUnit;

import static chess.engine.board.Bitboards.*;
import static chess.engine.figures.Moves.*;
import static chess.engine.figures.Moves_Helper.*;
import static java.lang.Math.round;
import static java.util.Objects.isNull;

public class Search {
    public static final int board_size = 8;
    public static int n = 0;
    public static int current_depth = 1;
    public static int desired_depth;

    public static Stack<long[]> get_children(long[] child, long[] parent, boolean wTurn) {
        /*expands a given position and returns it's children*/
        nrOfbAttackers = 0;
        nrOfbAttackers = 0;
        /*if (current_depth == desired_depth) { //if we reached the desired depth, return no more children
            return null;
        }*/
        initiate_next_moves(child, parent, wTurn); //TODO: implement history - parent = history
        Stack<long[]> children = generate_successors();
        n += children.size();
        return children;
    }

    public static void print_children(long[] parent, Stack<long[]> children) {
        int k = 1;
        System.out.println("expanding " + children.size() + " nodes:");
        while (!children.isEmpty()) {
            String turn="black";
            if (whitesTurn){
                turn="white";
            }
            System.out.println(String.format(k+" - \u001B[34m%s's turn\u001B[0m",turn));
            two_bitmaps_to_chessboard(parent,children.pop());
            System.out.println();
            k++;
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
    public static boolean invert(boolean turn){
        if (turn){
            whitesTurn=false;
            return false;
        }
        whitesTurn=true;
        return true;
    }

    public static void main(String[] args) {
        String FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";//"r1pp4/1Pp5/1R6/4n3/3K4/2Bbn1k1/4QP2/4R1B1";

        whitesTurn = true;
        System.out.println("=======Start Board=======");
        initiate_boards(FEN); //non-bit operations which happen only at the start
        System.out.println("=========================\n");
        long t1 = System.nanoTime();

        //depth 1:
        long[] parent = bitmaps.clone();
        Stack<long[]> children = get_children(parent, null, whitesTurn);
        Stack<long[]> copy_children = (Stack<long[]>) children.clone();
        //print_children(parent.clone(), (Stack<long[]>) children.clone());
        System.out.println(n);

        System.out.println("==============2==============");

        //depth 2:
        parent = copy_children.elementAt(7);
        invert(whitesTurn);
        children = get_children(parent, null, invert(whitesTurn));
        //copy_children = (Stack<long[]>) children.clone();
        print_children(parent, (Stack<long[]>) children);
        System.out.println(n);

        /*
        System.out.println("==============3==============");

        //depth 3:
        parent = copy_children.elementAt(1);
        invert(whitesTurn);
        children = get_children(parent, null, invert(whitesTurn));
        copy_children = (Stack<long[]>) children.clone();
        print_children(parent.clone(), (Stack<long[]>) children.clone());
        System.out.println(n);

        System.out.println("===============4=============");

        //depth 4:
        parent = copy_children.elementAt(0);
        invert(whitesTurn);
        children = get_children(parent, null, invert(whitesTurn));
        print_children(parent.clone(), (Stack<long[]>) children.clone());
        System.out.println(n);


        System.out.println("=============5===============");

        //depth 5:
        parent = copy_children.elementAt(0);
        invert(whitesTurn);
        children = get_children(parent, null, invert(whitesTurn));
        print_children(parent.clone(), (Stack<long[]>) children.clone());
        System.out.println(n);*/


        /*Stack<long[]> root = new Stack<>();
        root.push(bitmaps.clone());
        desired_depth = 2;
        generate_depth_n(root, null, whitesTurn);*/


        //System.out.println("# white attackers: "+nrOfwAttackers);
        //bitmap_to_chessboard(REDZONEB);
        //System.out.println("red-zone black ^");
        //System.out.println(BINCHECK);
        /*long valid_moves = 0L;
        for (int i = 0; i < 6; i++) {
            if (!isNull(movemapsIndividual[i])) {
                for (long m : movemapsIndividual[i]) {
                    valid_moves |= m;
                }
            }
        }
        bitmap_to_chessboard(valid_moves);
        System.out.println("valid moves black ^");

        *//*System.out.println("# white attackers: "+nrOfbAttackers);
        bitmap_to_chessboard(REDZONEW);
        System.out.println("red-zone white ^");
        //System.out.println(WINCHECK);
        long valid_moves = 0L;
        for (int i = 6; i < 12; i++) {
            if (!isNull(movemapsIndividual[i])) {
                for (long m : movemapsIndividual[i]) {
                    valid_moves |= m;
                    bitmap_to_chessboard(m);
                    System.out.println(gtfig(i));
                }
            }
        }
        bitmap_to_chessboard(valid_moves);
        System.out.println("white valid moves ^");*/


        /*int[][] test = getMoves();

        for (int i=0;i< test.length;i++){
            String s = (i+1)+": "+convertMove(test[i]);
        }*/
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
        long mc = TimeUnit.MICROSECONDS.convert(T, TimeUnit.NANOSECONDS);
        long mc_int = mc - (round(mc / 1000) * 1000);
        long ns = TimeUnit.NANOSECONDS.convert(T, TimeUnit.NANOSECONDS);
        long ns_int = ns - (round(ns / 1000) * 1000);
        System.out.println(String.format("\n\u001B[33m[%ss %sms %sµs %sns execution time]\u001B[0m", s_int, ms_int, mc_int, ns_int));
    }
}

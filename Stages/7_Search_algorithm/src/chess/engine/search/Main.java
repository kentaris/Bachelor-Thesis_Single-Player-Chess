package chess.engine.search;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import static chess.engine.board.Bitboards.bitmaps_to_chessboard;
import static chess.engine.search.Search.EXTRACT_SOLUTION_ACTIONS;
import static java.lang.Math.round;
import static java.util.Objects.isNull;

public class Main {
    public static long t1;
    public static long t2;

    public static String translate(long t1, long t2){
        long T = t2 - t1;
        long s = TimeUnit.SECONDS.convert(T, TimeUnit.NANOSECONDS);
        long s_int = s - (round(s / 1000) * 1000);
        long ms = TimeUnit.MILLISECONDS.convert(T, TimeUnit.NANOSECONDS);
        long ms_int = ms - (round(ms / 1000) * 1000);
        long mc = TimeUnit.MICROSECONDS.convert(T, TimeUnit.NANOSECONDS);
        long mc_int = mc - (round(mc / 1000) * 1000);
        long ns = TimeUnit.NANOSECONDS.convert(T, TimeUnit.NANOSECONDS);
        long ns_int = ns - (round(ns / 1000) * 1000);
        return String.format("\n\u001B[33m[%ss %sms %sµs %sns execution time]\u001B[0m", s_int, ms_int, mc_int, ns_int);
    }

    public static void time_it() {
        t2 = System.nanoTime();
        String time = translate(t1,t2);
        System.out.println(time);
    }

    public static void main(String[] args) {
        Problem problem = new Problem();
        Search search = new Search();
        NODE node = search.BFS(problem);
        LinkedList<long[]> path = search.EXTRACT_PATH(node);
        //print solution:
        if (isNull(node)) System.out.println("\u001B[31mno solution found (" + Search.n + "nodes expanded)");
        else {
            int size = path.size();
            System.out.println("\u001B[32mSolution found:\n"+"\u2500".repeat(22));
            System.out.println("Solution length:\t" + size);
            System.out.println("Nodes expanded:\t\t" + Search.n);
            String[] solution = EXTRACT_SOLUTION_ACTIONS(path,size);
            System.out.println("\t\u2502"+"Found plan:\u2502\n\t\u251C"+"\u2500".repeat(11)+"\u2524");
            for (int i =0;i<size-1;i++) System.out.println("\t\u2502"+i+": "+solution[i]+"\t\u2502");
        }

        /*for (long[] state : path) {
            bitmaps_to_chessboard(state);
        }*/


        /*

        System.out.println("\u001B[32m\n\n==============1==============\n\n\u001B[0m");

        //depth 1:
        long[] parent = bitmaps.clone(); //get initialized parent node
        Stack<long[]> children = get_children(parent, 0L, whitesTurn); //create possible children nodes
        print_children(parent, children);
        System.out.println(n);

        System.out.println("\u001B[32m\n\n==============2==============\n\n\u001B[0m");
        //depth 2:
        long[] old_parent = parent.clone();
        parent = children.elementAt(1);
        long difference=get_diff(old_parent,parent);
        children = get_children(parent, difference, invert(whitesTurn));
        print_children(parent, children);
        System.out.println(n);

        System.out.println("\u001B[32m\n\n==============3==============\n\n\u001B[0m");
        */

        /*Stack<long[]> root = new Stack<>();
        root.push(bitmaps.clone());
        desired_depth = 2;
        generate_depth_n(root, null, whitesTurn);*/


        time_it();
    }
}


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
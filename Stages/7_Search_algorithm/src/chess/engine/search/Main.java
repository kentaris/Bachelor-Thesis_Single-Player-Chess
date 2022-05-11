package chess.engine.search;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

import static chess.engine.search.Search.EXTRACT_SOLUTION_ACTIONS;
import static java.lang.Math.round;
import static java.util.Objects.isNull;

public class Main {
    public static long t1;
    public static long t2;

    public static String translate(long t1, long t2) {
        long T = t2 - t1;
        long s = TimeUnit.SECONDS.convert(T, TimeUnit.NANOSECONDS);
        long s_int = s - (round(s / 1000) * 1000);
        long ms = TimeUnit.MILLISECONDS.convert(T, TimeUnit.NANOSECONDS);
        long ms_int = ms - (round(ms / 1000) * 1000);
        long mc = TimeUnit.MICROSECONDS.convert(T, TimeUnit.NANOSECONDS);
        long mc_int = mc - (round(mc / 1000) * 1000);
        long ns = TimeUnit.NANOSECONDS.convert(T, TimeUnit.NANOSECONDS);
        long ns_int = ns - (round(ns / 1000) * 1000);
        if (ns_int > 1000) return String.format("\n\u001B[33m[%ss %sms execution time]\u001B[0m", s_int, ms_int);
        else return String.format("\n\u001B[33m[%ss %sms %sµs %sns execution time]\u001B[0m", s_int, ms_int, mc_int, ns_int);
    }

    public static void time_it() {
        t2 = System.nanoTime();
        String time = translate(t1, t2);
        System.out.println(time);
    }

    public static void main(String[] args) {
        /*PriorityQueue<test> T = new PriorityQueue<test>();
        test t1 = new test(3,"erstes:3");
        test t2 = new test(2,"zweites:2");
        test t3 = new test(1,"drittes:1");
        test t4 = new test(4,"viertes:4");
        test t5 = new test(5,"fünftes:5");
        T.add(t1);
        T.add(t2);
        T.add(t4);
        T.add(t5);
        T.add(t3);
        for (int i =0;i<5;i++){
            System.out.println(T.poll().element);
        }
        System.exit(8);*/
        Problem problem = new Problem();
        Search search = new Search();
        NODE node = new NODE();
        if (args[0].toLowerCase().contains("best")){
            System.out.println("Greedy-Best-First Search selected");
            Heuristic heuristic = new Heuristic();
            node = search.BestFirst_Search(problem,heuristic);
        } else if (args[0].toLowerCase().contains("breadth")) {
            System.out.println("Breadth-First Search selected");
            node = search.BreadthFirst_Search(problem);
        }
        System.out.println();
        //print solution:
        if (isNull(node)) System.out.println("\u001B[31mno solution found (" + Search.n + " nodes expanded)");
        else {
            LinkedList<NODE> path = search.EXTRACT_PATH(node);
            int size = path.size();
            System.out.println("\u001B[32m"+"\u2500".repeat(4)+"Solution found" + "\u2500".repeat(4));
            System.out.println("Nodes expanded:\t\t" + Search.n);
            System.out.println("Solution length:\t" + size);
            System.out.println("Solution g-Value:\t" + node.PATH_COST);
            //System.out.println("\u2500".repeat(22));
            String[] solution = EXTRACT_SOLUTION_ACTIONS(path, size);
            System.out.println("\u001B[32m"+"\u2500".repeat(5)+"Action Plan" + "\u2500".repeat(5));
            //System.out.println("\t\u2502" + "Found plan:\u2502\n\t\u251C" + "\u2500".repeat(11) + "\u2524");
            for (int i = 0; i < size - 1; i++) System.out.println("\t  \u2502" + (i+1) + ": " + solution[i] + "\u2502");
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
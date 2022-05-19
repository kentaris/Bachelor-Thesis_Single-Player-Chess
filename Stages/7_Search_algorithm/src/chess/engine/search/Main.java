package chess.engine.search;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

import static chess.engine.board.Bitboards.bitmaps_to_chessboard;
import static chess.engine.board.Bitboards.two_bitmaps_to_chessboard;
import static chess.engine.search.Search.EXTRACT_SOLUTION_ACTIONS;
import static java.lang.Math.round;
import static java.util.Objects.isNull;

public class Main {
    public static double seconds;
    public static double nanos;
    public static String time;

    public static String translate(long t1, long t2) {
        long T = t2 - t1;
        nanos=TimeUnit.NANOSECONDS.convert(T, TimeUnit.NANOSECONDS);
        seconds = T / (1000.0 * 1000.0 * 1000.0);
        long s = TimeUnit.SECONDS.convert(T, TimeUnit.NANOSECONDS);
        long s_int = s - (round(s / 1000) * 1000);
        long ms = TimeUnit.MILLISECONDS.convert(T, TimeUnit.NANOSECONDS);
        long ms_int = ms - (round(ms / 1000) * 1000);
        long mc = TimeUnit.MICROSECONDS.convert(T, TimeUnit.NANOSECONDS);
        long mc_int = mc - (round(mc / 1000) * 1000);
        long ns = TimeUnit.NANOSECONDS.convert(T, TimeUnit.NANOSECONDS);
        long ns_int = ns - (round(ns / 1000) * 1000);
        if (ns_int > 1000) return String.format("\n\u001B[33m[%ss %sms execution time]\u001B[0m", s_int, ms_int);
        else
            return String.format("\n\u001B[33m[%ss %sms %sµs %sns execution time]\u001B[0m", s_int, ms_int, mc_int, ns_int);
    }

    public static void time_it(Problem problem) {
        problem.t2 = System.nanoTime();
        //t2=System.currentTimeMillis();
        time = translate( problem.t1,  problem.t2);
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
        boolean BFS = false;
        if (args[0].toLowerCase().contains("a*")) {
            if (args.length==1) {
                System.out.println("A-Star Search selected");
                Heuristic heuristic = new Heuristic();
                heuristic.A_Star = true;
                node = search.BestFirst_Search(problem, heuristic);
            } else if (args.length==2) {
                System.out.println("Weighted A-Star Search selected");
                Heuristic heuristic = new Heuristic();
                heuristic.A_Star = true;
                try {
                    heuristic.weight = Double.parseDouble(args[1]);
                }catch(Exception e){
                    System.out.println("\u001B[31m second argument of weighted A* must be a decimal weight");
                }
                node = search.BestFirst_Search(problem, heuristic);
            }
        } else if (args[0].toLowerCase().contains("best") | args[0].toLowerCase().contains("greedy")) {
            System.out.println("Greedy-Best-First Search selected");
            Heuristic heuristic = new Heuristic();
            node = search.BestFirst_Search(problem, heuristic);
        } else if (args[0].toLowerCase().contains("breadth")) {
            System.out.println("Breadth-First Search selected");
            BFS = true;
            node = search.BreadthFirst_Search(problem);
        }
        time_it(problem);
        System.out.println();
        //print solution:
        if (isNull(node)) System.out.println("\u001B[31mno solution found (" + Search.n + " nodes expanded)");
        else {
            LinkedList<NODE> path = search.EXTRACT_PATH(node);
            int size = path.size();
            String[] solution = EXTRACT_SOLUTION_ACTIONS(path, size);
            //uncomment for solution path visualization:
            /*System.out.print("\u001B[34m " + "\u2500".repeat(4) + "Solution Path:" + "\u2500".repeat(4));
            for (int i = path.size() - 1; i >= 0; i--) {
                bitmaps_to_chessboard(path.get(i).STATE.state);
                System.out.println(i + "^");
            }*/
            System.out.println(" " + "\u2500".repeat(22));
            System.out.print("\u001B[34m " + "\u2500".repeat(4) + "Solution State" + "\u2500".repeat(4));
            bitmaps_to_chessboard(node.STATE.state);
            System.out.println(" " + "\u2500".repeat(22));
            System.out.println("\u001B[32m" + "\u2500".repeat(3) + "Solution details" + "\u2500".repeat(3));
            System.out.println("Nodes expanded:\t\t" + Search.n);
            System.out.println("Nodes/s:       \t\t" + ((int)(Search.n / nanos *1000*1000*1000)));
            //System.out.println(Search.n+" "+nanos);
            //System.exit(0);
            if (!BFS) {
                System.out.println("heuristic from s_0:\t" + problem.root_node.STATE.heuristic_value);
                System.out.println("heuristic from s_*:\t" + node.STATE.heuristic_value);
            }
            System.out.println("Solution length:\t" + size);
            System.out.println("Solution g-Value:\t" + node.STATE.path_cost);
            //System.out.println("\u2500".repeat(22));
            System.out.println("\u001B[32m" + "\u2500".repeat(5) + "Action Plan" + "\u2500".repeat(5));
            //System.out.println("\t\u2502" + "Found plan:\u2502\n\t\u251C" + "\u2500".repeat(11) + "\u2524");
            for (int i = 0; i < size - 1; i++)
                System.out.println("    \u2502" + (i + 1) + ": " + solution[i]);
        }
        System.out.println(time);
    }
}
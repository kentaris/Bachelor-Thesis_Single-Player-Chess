package chess.engine.search;

public class test implements Comparable<test> {
    int key;
    String element;

    public test(int key, String element) {
        this.key = key;
        this.element = element;
    }
    public int compareTo(test o) {
        System.out.println(":"+this.key);
        System.out.println(":"+o.key);
        if (this.key<o.key){
            return -1;
        } else if (this.key>o.key) {
            return 1;
        }
        else return 0;
    }
}

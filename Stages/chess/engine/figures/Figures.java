package chess.engine.figures;

import java.util.*;

public class Figures {
    private static final Character[] cArray = {'p','n','b','r','q','k','P','N','B','R','Q','K'};
    private static final Map<Character, Integer> map = new HashMap<Character, Integer>() {{
        put('p',1);put('n',2);put('b',3);put('r',4);
        put('q',5);
        put('k',6);
        put('P',7);
        put('N',8);
        put('B',9);
        put('R',10);
        put('Q',11);
        put('K',12);
    }};
    public static final Character[] get_pieces(){
        return cArray;
    }
    public static final int get_idx(Character fig){
        return map.get(fig);
    }
}

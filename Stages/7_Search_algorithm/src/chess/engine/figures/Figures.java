package chess.engine.figures;

import java.util.LinkedHashMap;

public class Figures {
    private static final Character[] cArray = {'p', 'n', 'b', 'r', 'q', 'k', 'P', 'N', 'B', 'R', 'Q', 'K'};
    private static final LinkedHashMap<Character, Integer> map = new LinkedHashMap<Character, Integer>() {{
        put('p', 0);
        put('n', 1);
        put('b', 2);
        put('r', 3);
        put('q', 4);
        put('k', 5);
        put('P', 6);
        put('N', 7);
        put('B', 8);
        put('R', 9);
        put('Q', 10);
        put('K', 11);
    }};

    public static final Character[] get_pieces() {
        return cArray;
    }

    public static final Character gtfig(Integer idx) {
        return (Character) map.keySet().toArray()[idx];
    }

    public static final int gtidx(Character fig) {
        return map.get(fig);
    }
}

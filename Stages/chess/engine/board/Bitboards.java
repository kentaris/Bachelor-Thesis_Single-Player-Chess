package chess.engine.board;

import java.util.Arrays;

public class Bitboards {
    public static long[] generate_bitboards(String[] stringMaps) {
        long[] bitmaps = new long[12]; //12 maps for 2*6 chess figures (black and white) -->long so board has 64bits available
        Arrays.fill(bitmaps, 0L); //populate the bitmaps

        return bitmaps;
    }
}

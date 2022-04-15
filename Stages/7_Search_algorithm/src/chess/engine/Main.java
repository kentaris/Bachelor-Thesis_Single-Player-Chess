package chess.engine;

import static chess.engine.FEN.Decoder.FEN_decode;

public class Main {
    public static int board_size = 8;
    public static void main(String[] args){
        String[][] board=FEN_decode("8/8/p7/4n3/3K4/2BbN1k1/4Q3/4R3",board_size);
        for (int i=0;i<board_size;i++) {
            for (int j=0;j<board_size;j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }

    }
}

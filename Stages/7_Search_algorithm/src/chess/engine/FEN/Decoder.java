package chess.engine.FEN;

import java.util.Arrays;

public class Decoder {

    public static String[][] FEN_decode(String FEN, int board_size) {
        System.out.println(FEN);
        int length = FEN.split("/").length;
        if (length != board_size) {
            System.out.println("FEN code doesn\'t match board_size!");
            System.exit(1);
        }
        int rank = 0;
        int file = 0;
        String board[][] = new String[length][length];
        for (String[] row: board)
            Arrays.fill(row, "0"); //populate the board
        for (int pos = 0; pos < FEN.length(); pos++) {
            if (FEN.charAt(pos) == '/') {
                rank++;
                file = 0;
            }
            else{
                if (Character.isDigit(FEN.charAt(pos))){ //is digit
                    file+=Character.getNumericValue(FEN.charAt(pos));
                }
                else{ //is figure
                    board[rank][file] = String.valueOf(FEN.charAt(pos));
                    file++;
                }
                if ((pos%length)>length){
                    file=0;
                }
            }
        }
        return board;
    }
}

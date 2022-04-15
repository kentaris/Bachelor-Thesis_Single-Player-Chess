package chess.engine.fen;

import java.util.Arrays;

import static chess.engine.figures.Figures.get_idx;

public class Decoder {

    public static String[][] FEN_decodeTo_Board(String FEN, int board_size) {
        /* returns the board version of the given FEN code. */
        int length = FEN.split("/").length;
        if (length != board_size) {
            System.out.println("FEN code doesn\'t match board_size!");
            System.exit(1);
        }
        int rank = 0;
        int file = 0;
        String board[][] = new String[length][length];
        for (String[] row : board) {
            Arrays.fill(row, "0"); //populate the board
        }
        for (int pos = 0; pos < FEN.length(); pos++) {
            if (FEN.charAt(pos) == '/') {
                rank++;
                file = 0;
            } else {
                if (Character.isDigit(FEN.charAt(pos))) { //is digit
                    file += Character.getNumericValue(FEN.charAt(pos));
                } else { //is figure
                    board[rank][file] = String.valueOf(FEN.charAt(pos));
                    file++;
                }
                if ((pos % length) > length) {
                    file = 0;
                }
            }
        }
        return board;
    }

    public static String[] FEN_decodeTo_64String(String FEN, int board_size) {
        /* returns 12 (one for each figure) bitmaps à 64 position long string-Bit representations of the given FEN code. */
        int length = FEN.split("/").length;
        if (length != board_size) {
            System.out.println("FEN code doesn\'t match board_size!");
            System.exit(1);
        }
        String[] maps = new String[12]; //12 boards, one for every piece and color
        for (char fig : chess.engine.figures.Figures.get_pieces()) {
            StringBuilder builder = new StringBuilder(board_size * board_size);
            for (int pos = 0; pos < FEN.length(); pos++) {
                if (Character.isDigit(FEN.charAt(pos))) { //is digit
                    for (int i = 0; i < Character.getNumericValue(FEN.charAt(pos)); i++) { //for 8/.... we want 11111111/...
                        builder.append(0);
                    }
                } else if (FEN.charAt(pos) == fig) {
                    builder.append(1);
                } else if (FEN.charAt(pos) == '/') {
                    continue;
                } else { //symbol is not current figure but another one
                    builder.append(0);
                }

            }
            if (builder.length() != (board_size * board_size)) {
                System.out.println("Decoder: The String builder length is " + builder.length() + " but should be " + (board_size * board_size) + " (" + fig + '/' + builder + ")");
                System.exit(2);
            }
            maps[get_idx(fig) - 1] = builder.toString();
        }
        return maps;
    }
}

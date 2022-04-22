package chess.engine.figures;

import static chess.engine.board.Bitboards.*;
import static chess.engine.figures.Moves.*;
import static chess.engine.search.Search.board_size;

public class Moves_Helper {
    public static void addprotected(long figure, long current) {
        if (isWhite(figure)) { //if attacker is a white piece then the attacked piece can be categorized as a white piece being protected by a white piece
            WPROTECTED |= current; //currently looked at piece
        } else { //black attacker piece...
            BPROTECTED |= current;
        }
    }

    public static void addattacked(long figure, long next) {
        if (isWhite(figure)) {
            BATTACKED |= next; //add currently looked at piece
            //TODO: add squares behind king to red zone!
            if (isKing(next)) {
                nrOfwAttackers++; //we need to keep track of how many pieces attack the king since if it is only one piece it can be captured to eliminate the check but if there are more than one we can't eliminate the check by capturing but must move the king.
            }
        } else {
            WATTACKED |= next;
            if (isKing(next)) {
                nrOfbAttackers++;
            }
        }
    }

    public static long hor_ver_bitboard(long bitboard, long opposite_color, long same_color) {
        /*creates a bitmap mask which marks the row and file up and down to mark the spots where a rook (or queen) can possibly go to.*/
        /*the input is a bitmap of attacking pieces (rooks and or queens), opposite color bitmap where all opposite colored pieces are marked with a 1, and same colored bitmap. the bitmap is then separated such that every attacker piece is on its own isolated bitmap. We can then loop over those isolated pieces. The index of the current isolated attacker piece is calculated (0-63) by shifting the bit and counting how many times we had to shift to the right to get a 1.With that index we can now calculate what the represented row and columns would be on a chess board. To calculate the file we use the formula: index/board_size and cast it into an integer. To calculate the row we use the formula index%board_size. */
        /*Now that we know the file and rank of an attacker piece we can do some more math to move the piece into every direction until it reaches the end of the board. We do this with another (very shot) loop for every direction like so: /insert pseudo code/. This is very efficient since the loops will in total only execute 2*(board_size-1) times (possibilities in both directions minus the square on which the attacker piece is currently on). We can make it even more efficient by checking for a collision with the same colored pieces at current square or else checking for a collision with opposite colored piece at previous square. in both cases we can break the loop prematurely because if we know that the current square to be checked has the same color, then I don't want to set the bit. Similarly, if I know the previous checked location (either k-1 or k+1 depending on the direction we move to) was a opposite colored piece, then we already set the bit and now know we must stop since we can't continue the piece movement. Otherwise we can just continue shifting the attacker bitmap to the right and adding the resulting bitmap to the result bitmap (squares which can be attacked). This also saves us time since we don't need to do an additional removing of those unreachable squares.*/
        /*The math to move in all directions is as follows: to move the attacker piece to the left, we have the following loop: for (int k = 1; k < file + 1; k++). We start at the next square (k=1) and move until we reach the end of the file (k<file). On a physically equivalent chessboard this logic seems a little bit off. The logic is still correct tough because in our bitmap representation, the top left square has index 0 and therefore is rank 0 and file 0, meaning the ranks are mirrored but the files are correct. This is because of the nature of the bitmap. We have a single 64bit long which has a beginning (right side) and an end (left side) where the beginning corresponds to the left upper square (A8) and the ending corresponds to the right lower square (H1) on the physically equivalent chessboard. Maybe it is also worth mentioning that therefore a left shift (<<) corresponds to movements to the right on the  physically equivalent chessboard and a right shift (>>>) a movement to the left. A similar logic is true for all 4 directions (left, right, up and down) where the up and down movements also need to be multiplied with the board size to accomplish that shift by 8 upwards, downwards movement we saw at the beginning of this chapter.*/
        /*To check if there is a collision with a same colored piece at the current square we use a property of chess boards: no square can be occupied twice. To make use of this property we have the following line: if (((same_color >>> idx - k) & 1) == 1L). It shifts the same-color bitmap (where the pieces of the same color are marked with a 1) to the right by the absolute position of the capturing piece on the 64bit (idx) plus the current movement step (k). The result is a bit that is shifted to the right by idx+k meaning if there was a piece on the previous square we are checking, then the rightmost bit must be equal to 1 and else 0. By adding this result with '&1', we get a bit where all bits are set to 0 except the one on the rightmost position. Now we can simply check if the resulting 64bit is a 1 or a 0 to check if the square is occupied or not. A similar logic takes place if we check for a collision with an opposite colored piece at the previous square: we use k+1 or k-1 to achieve this (depending on the direction the attacker piece is moving.*/
        /*At this point we can go one step further. Since can differentiate between our own and the opponents pieces at this level, we can save the pieces that are attacked (checks) and the pieces that are protected (king can't capture). This saves us a lot of computation power later on.*/
        /*Here I also want to mention that state-of-the-art chess engines use a different approach. They also need the first loop where they go over every piece one by one, but they differ in the second step. They use a precomputed mask for the horizontals, verticals, diagonals and anti-diagonals (chess wiki, magic bitboards). They then overlay those masks to filter out all unrelevant information about other ranks and files that are not involved. They then continue and rotate the bitboard in such a way that all lines are horizontal (a anti-diagonal would be rotated by 45 degrees counterclockwise for example). Now they can make use of the following line of logic for the line attacks: (o-s)^(o'-2s')' where o is the occupied bitmap and s the bitmap where the slider piece (rook for example) is located. The ' is the notation for reversal of a line/flipping the bitboard. This is usefull because we can make all movements go in the same direction (left) by simply flipping the movements that go to the right. The multiplied by 2 accomplished a carrying over of bits so that all the bits between a slider piece and a occupied spot are marked with a 1 (which represents the movement of the piece). if we substract... (Sliding pieces(part1) - advanced java chess engine tutorial 8). I decided against this accepted approach because I wanted to come up with my own implementation to get a better feel of how the bitboards data structure works. It would be interesting to see how my implementation stacks up against this accepted approach but there is only so much that can be done in a bachelor's thesis.*/
        /*https://www.chessprogramming.org/Flipping_Mirroring_and_Rotating*/
        /*https://www.chessprogramming.org/Efficient_Generation_of_Sliding_Piece_Attacks*/
        /*https://core.ac.uk/download/pdf/33500946.pdf*/
        /*System.out.println("bitboard:");
        bitmap_to_chessboard(bitboard);
        System.out.println("opposite color:");
        bitmap_to_chessboard(opposite_color);
        System.out.println("same color:");
        bitmap_to_chessboard(same_color);
        //System.exit(0);*/
        long hor_ver = 0L;
        long[] figures = get_single_figure_boards(bitboard);
        for (int i = 0; i < figures.length; i++) { //loop over single figures
            Integer idx = get_squareIndex_of_figure(figures[i]);
            Integer[] file_row = idx_to_fileRank(idx);
            Integer file = file_row[1];
            Integer rank = file_row[0];
            for (int k = 0; k < file + 1; k++) { //move left (right shift on bitboard)
                long current = (figures[i] >>> k) & same_color;
                if ((current != 0L) & k != 0) { //collision with OWN piece at current square. //What we do here is we are shifting the attacker piece bitmap to the position which we are currently checking. the resulting bitmap is combined  with the same_color bitmap with an '&'. if the resulting map is not equal to 0 then we know we have a matchup of attacker piece and attacked piece which means it is at the current location. //k needs to be bigger than 0 so we can ignore the attacker piece and don't see it as blocking itself.
                    addprotected(figures[i], current); //TODO: exclude the king from protected pieces? I don't think it's neccesary.
                    break;
                }
                if (k > 0) { //we still need the if statement otherwise if we have an opposite color at the next square we also mark the origin spot.
                    hor_ver |= figures[i] >>> k;
                }
                long next = (figures[i] >>> (k + 1)) & opposite_color;
                if (next != 0L) { //collision with OPPOSITE colored piece at next square
                    addattacked(figures[i], next);
                    hor_ver |= figures[i] >>> (k + 1);
                    break; //TODO: add check here! ... if isKing(next)
                }
            }
            for (int k = 0; k < board_size - file; k++) { //move right (left shift on bitboard)
                long current = (figures[i] << k) & same_color;
                if ((current != 0L) & k != 0) {
                    addprotected(figures[i], current);
                    break;
                }
                if (k > 0) {
                    hor_ver |= figures[i] << k;
                }
                long next = (figures[i] << (k + 1)) & opposite_color;
                if (next != 0L) {
                    addattacked(figures[i], next);
                    hor_ver |= figures[i] << (k + 1);
                    break;
                }
            }
            for (int k = 0; k < rank + 1; k++) {//move up
                long current = (figures[i] >>> (k * board_size)) & same_color;
                if ((current != 0L) & k != 0) {
                    addprotected(figures[i], current);
                    break;
                }
                if (k > 0) {
                    hor_ver |= figures[i] >>> (k * board_size);
                }
                long next = (figures[i] >>> ((k + 1) * board_size)) & opposite_color;
                if (next != 0L) {
                    addattacked(figures[i], next);
                    hor_ver |= figures[i] >>> ((k + 1) * board_size);
                    break;
                }
            }
            for (int k = 0; k < board_size - rank; k++) {//move down
                long current = (figures[i] << (k * board_size)) & same_color;
                if ((current != 0L) & k != 0) {
                    addprotected(figures[i], current);
                    break;
                }
                if (k > 0) {
                    hor_ver |= figures[i] << (k * board_size);
                }
                long next = (figures[i] << ((k + 1) * board_size)) & opposite_color;
                if (next != 0L) {
                    addattacked(figures[i], next);
                    hor_ver |= figures[i] << ((k + 1) * board_size);
                    break;
                }
            }
        }
        //bitmap_to_chessboard(hor_ver);
        return hor_ver;
    }

    public static long diag_bitboard(long bitboard, long opposite_color, long same_color) {
        /*creates a bitmap mask which marks the diagonals and anti-diagonals to mark the spots where a bishop (or queen) can possibly go to.*/
        /*For the diagonals and anti-diagonals I decided to reuse the logic I came up with previously (horizontals & verticals). The diagonals are horizontal and vertical lines that are rotated 45 degrees counterclockwise. this is why for example the left down anti-diagonal has the opposite shift operator as the left moving vertical line.*/
        long diag = 0L;
        long[] figures = get_single_figure_boards(bitboard); //separate figures into separate boards
        for (int i = 0; i < figures.length; i++) { //loop over single figures
            Integer idx = get_squareIndex_of_figure(figures[i]);
            Integer[] file_row = idx_to_fileRank(idx);
            Integer file = file_row[1];
            Integer rank = file_row[0];
            for (int k = 0; k < file + 1; k++) {//move left down
                long current = (figures[i] << (k * (board_size - 1))) & same_color;
                if ((current != 0L) & k != 0) { //collision with OWN piece at current square
                    addprotected(figures[i], current);
                    break;
                }
                if (k > 0) {
                    diag |= figures[i] << (k * (board_size - 1));
                }
                long next = (figures[i] << ((k + 1) * (board_size - 1))) & opposite_color;
                if (next != 0L) { //collision with OPPOSITE colored piece at next square
                    addattacked(figures[i], next);
                    diag |= figures[i] << ((k + 1) * (board_size - 1));
                    break;
                }
            }
            for (int k = 0; k < board_size - file; k++) {//move right up
                long current = (figures[i] >>> (k * (board_size - 1))) & same_color;
                if ((current != 0L) & k != 0) {
                    addprotected(figures[i], current);
                    break;
                }
                if (k > 0) {
                    diag |= figures[i] >>> (k * (board_size - 1));
                }
                long next = (figures[i] >>> ((k + 1) * (board_size - 1))) & opposite_color;
                if (next != 0L) {
                    addattacked(figures[i], next);
                    diag |= figures[i] >>> ((k + 1) * (board_size - 1));
                    break;
                }
            }
            for (int k = 0; (k < rank + 1) & (k < file + 1); k++) {//move left up
                long current = (figures[i] >>> (k * (board_size + 1))) & same_color;
                if ((current != 0L) & k != 0) {
                    addprotected(figures[i], current);
                    break;
                }
                if (k > 0) {
                    diag |= figures[i] >>> (k * (board_size + 1));
                }
                long next = (figures[i] >>> ((k + 1) * (board_size + 1))) & opposite_color;
                if (next != 0L) {
                    addattacked(figures[i], next);
                    diag |= figures[i] >>> ((k + 1) * (board_size + 1));
                    break;
                }
            }
            for (int k = 0; k < (board_size - rank) & (k < board_size - file); k++) {//move right down
                long current = (figures[i] << (k * (board_size + 1))) & same_color;
                if ((current != 0L) & k != 0) {
                    addprotected(figures[i], current);
                    break;
                }
                if (k > 0) {
                    diag |= figures[i] << (k * (board_size + 1));
                }
                long next = (figures[i] << ((k + 1) * (board_size + 1))) & opposite_color;
                if (next != 0L) {
                    addattacked(figures[i], next);
                    diag |= figures[i] << ((k + 1) * (board_size + 1));
                    break;
                }
            }
        }
        //bitmap_to_chessboard(diag);
        return diag;
    }
}

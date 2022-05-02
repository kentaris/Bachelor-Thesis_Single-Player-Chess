package chess.engine.figures;

import java.util.Stack;

import static chess.engine.board.Bitboards.*;
import static chess.engine.figures.Figures.gtfig;
import static chess.engine.figures.Figures.gtidx;
import static chess.engine.figures.Moves.*;
import static chess.engine.search.Search.board_size;
import static java.util.Objects.isNull;

public class Moves_Helper {

    public static void addprotected(long figure, long current) {
        if (isWhite(figure)) { //if attacker is a white piece then the attacked piece can be categorized as a white piece being protected by a white piece
            WPROTECTED |= current; //currently looked at piece
            REDZONEB |= current;
        } else { //black attacker piece...
            BPROTECTED |= current;
            REDZONEW |= current;
        }
    }

    public static void addAttacked(long figure, long next) {
        if (isWhite(figure)) {
            BATTACKED |= next; //add currently looked at piece
            if (isKing(next)) {
                nrOfwAttackers++; //we need to keep track of how many pieces attack the king since if it is only one piece it can be captured to eliminate the check but if there are more than one we can't eliminate the check by capturing but must move the king.
                locOfwAttackers |= figure;
            }
        } else {
            WATTACKED |= next;
            if (isKing(next)) {
                nrOfbAttackers++;
                locOfbAttackers |= figure;
            }
        }
    }

    public static void addPinnedMovement(long attacker, long path) {
        pinnedMovement |= path + attacker;
    }

    public static void addPinned(long next) {
        if (isWhite(next)) {
            pinnedW |= next;
        } else {
            pinnedB |= next;
        }
    }

    public static void addRedZone(long attacker, long path) {
        if (isWhite(attacker)) {
            REDZONEB |= path;
        } else {
            REDZONEW |= path;
        }
    }

    public static void addToBeBlocked(long attacker, long block_it) {
        if (isWhite(attacker)) {
            blockLocationsB |= block_it;
        } else {
            blockLocationsW |= block_it;
        }
    }

    public static long[] hor_ver_bitboard(long bitboard, long opposite_color, long same_color, int fig_idx) {
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
        long[] figures = get_single_figure_boards(bitboard); //here I split the board's figures into single figure bitboards for every piece (if there are multiple)
        currAmount = figures.length;
        long[] movemap = new long[currAmount];
        posmapsIndividual[fig_idx] = new long[currAmount];
        for (int i = 0; i < currAmount; i++) { //loop over single figures
            posmapsIndividual[fig_idx][i] = figures[i];//we add the position of the current figure //TODO: remove that position again everywhere!!!
            movemap[i] = 0L;
            long path; //squares which can be blocked to get out of check will be saved here if there is a check
            boolean set = false;
            Integer idx = get_squareIndex_of_figure(figures[i]);
            Integer[] file_row = idx_to_fileRank(idx);
            Integer file = file_row[1];
            Integer rank = file_row[0];
            path = 0L;
            for (int k = 0; k < (file + 1); k++) { //move left (right shift on bitboard)
                long current = (figures[i] >>> k) & same_color;
                path |= figures[i] >>> (k + 1);
                if ((current != 0L) & k != 0) { //collision with OWN piece at current square.
                    addprotected(figures[i], current);
                    break;
                }
                if (k > 0) {
                    movemap[i] |= figures[i] >>> k;
                }
                long next = (figures[i] >>> (k + 1)) & opposite_color;
                //the following needs to stop at the end of the board. if we don't have the k<file+1 condition, the piece can check a piece on the opposite side of the board and also capture those pieces:
                if (next != 0L & k < (file + 1)) { //collision with OPPOSITE colored piece at next square
                    addAttacked(figures[i], next);
                    movemap[i] |= figures[i] >>> (k + 1);
                    set = false;
                    if (isKing(next)) {
                        addToBeBlocked(figures[i], path);
                        set = true;
                    }

                    long next_next;
                    for (int rem = k + 2; rem < file + 1; rem++) {
                        next_next = figures[i] >>> (rem);
                        path |= next_next;
                        if (set & rem == file) {
                            addRedZone(figures[i], path);
                        }
                        if (rem == file) {
                            addPinned(next); //TODO: piece is pinned even if there is no king behind it...
                            addPinnedMovement(figures[i], path);
                        }
                    }
                    break;
                }
                /*long current = (figures[i] >>> k) & same_color;
                if ((current != 0L) & k != 0) { //collision with OWN piece at current square. //What we do here is we are shifting the attacker piece bitmap to the position which we are currently checking. the resulting bitmap is combined  with the same_color bitmap with an '&'. if the resulting map is not equal to 0 then we know we have a matchup of attacker piece and attacked piece which means it is at the current location. //k needs to be bigger than 0 so we can ignore the attacker piece and don't see it as blocking itself.
                    addprotected(figures[i], current);
                    break;
                }
                if (k > 0) { //we still need the if statement otherwise if we have an opposite color at the next square we also mark the origin spot.
                    movemap[i] |= figures[i] >>> k;
                }
                long next = (figures[i] >>> (k + 1)) & opposite_color;
                if (next != 0L) { //collision with OPPOSITE colored piece at next square
                    addAttacked(figures[i], next);
                    movemap[i] |= figures[i] >>> (k + 1);
                    break;
                }*/
            }
            path = 0L;
            for (int k = 0; k < board_size - file; k++) { //move right (left shift on bitboard)
                long current = (figures[i] << k) & same_color;
                path |= figures[i] << (k + 1);
                if ((current != 0L) & k != 0) { //collision with OWN piece at current square.
                    addprotected(figures[i], current);
                    break;
                }
                if (k > 0) {
                    movemap[i] |= figures[i] << k;
                }
                long next = (figures[i] << (k + 1)) & opposite_color;
                if (next != 0L & k < (board_size - file)) { //collision with OPPOSITE colored piece at next square
                    addAttacked(figures[i], next);
                    movemap[i] |= figures[i] << (k + 1);
                    set = false;
                    if (isKing(next)) {
                        addToBeBlocked(figures[i], path);
                        set = true;
                    }

                    long next_next;
                    for (int rem = k + 2; rem < board_size - file; rem++) {
                        next_next = figures[i] << (rem);
                        path |= next_next;
                        if (set & rem == board_size - file - 1) {
                            addRedZone(figures[i], path);
                        }
                        if (rem == board_size - file - 1) {
                            addPinned(next);
                            addPinnedMovement(figures[i], path);
                        }
                    }

                    break;
                }
                /*long current = (figures[i] << k) & same_color;
                if ((current != 0L) & k != 0) {
                    addprotected(figures[i], current);
                    break;
                }
                if (k > 0) {
                    movemap[i] |= figures[i] << k;
                }
                long next = (figures[i] << (k + 1)) & opposite_color;
                if (next != 0L) {
                    addAttacked(figures[i], next);
                    movemap[i] |= figures[i] << (k + 1);
                    break;
                }*/
            }
            path = 0L;
            for (int k = 0; k < rank + 1; k++) {//move up
                long current = (figures[i] >>> (k * board_size)) & same_color; //current square which is targeted by figures[i]
                //TODO: down here I use (k+1) and in all other spots I only use "k"... which is correct? test! also in diagonal....
                path |= (figures[i] >>> ((k + 1) * board_size)); //we stop prematurely because of the break if we encounter an opposite king so the king spot itself will not be saved here.
                if ((current != 0L) & k != 0) { //collision with OWN piece at current square.
                    addprotected(figures[i], current); //protected piece which opposite king can't capture because he will be in check
                    break; //no further checking needed
                }
                if (k > 0) { //we don't want to mark ourselves
                    movemap[i] |= figures[i] >>> (k * board_size); //mark the current square which is targeted by figures[i]
                }
                long next = (figures[i] >>> ((k + 1) * board_size)) & opposite_color; //next square which is being attacked
                if (next != 0L & k < (rank + 1)) { //collision with OPPOSITE colored piece at next square
                    addAttacked(figures[i], next);
                    movemap[i] |= figures[i] >>> ((k + 1) * board_size);

                    //---------------------
                    set = false;
                    if (isKing(next)) { //if next square is an opposite colored king...
                        addToBeBlocked(figures[i], path);//...ad locations to block the check
                        set = true; //we only want to add the red zone if there was a king on the path. this is what we are marking here
                    }

                    long next_next;
                    for (int rem = k + 2; rem < rank + 1; rem++) { //check if there is a king behind the piece (meaning piece is pinned)
                        next_next = figures[i] >>> (rem * board_size);
                        path |= next_next;
                        /*if (isKing(next_next)) { //if next square is an opposite colored king...
                            set = true;
                        }*/
                        if (set & rem == rank) { //the opposite piece we collided with was a king
                            addRedZone(figures[i], path); //the whole path is a red zone
                        }
                        if (rem == rank) { //if the opposite piece we collided with was not a king (it is pinned)
                            addPinned(next); //piece is pinned
                            addPinnedMovement(figures[i], path);
                        }
                    }
                    //---------------------
                    break;
                }
            }
            path = 0L;
            for (int k = 0; k < board_size - rank; k++) {//move down
                long current = (figures[i] << (k * board_size)) & same_color;
                path |= (figures[i] << ((k + 1) * board_size));
                if ((current != 0L) & k != 0) { //collision with OWN piece at current square.
                    addprotected(figures[i], current);
                    break;
                }
                if (k > 0) {
                    movemap[i] |= figures[i] << (k * board_size);
                }
                long next = (figures[i] << ((k + 1) * board_size)) & opposite_color;
                if (next != 0L & k < (board_size - rank)) { //collision with OPPOSITE colored piece at next square
                    addAttacked(figures[i], next);
                    movemap[i] |= figures[i] << ((k + 1) * board_size);
                    set = false;
                    if (isKing(next)) {
                        addToBeBlocked(figures[i], path);
                        set = true;
                    }

                    long next_next;
                    for (int rem = k + 2; rem < board_size - rank; rem++) {
                        next_next = figures[i] << (rem * board_size);
                        path |= next_next;
                        if (set & rem == board_size - rank - 1) {
                            addRedZone(figures[i], path);
                        }
                        if (rem == board_size - rank - 1) {
                            addPinned(next);
                            addPinnedMovement(figures[i], path);
                        }
                    }

                    break;
                }
                /*long current = (figures[i] << (k * board_size)) & same_color;
                if ((current != 0L) & k != 0) {
                    addprotected(figures[i], current);
                    break;
                }
                if (k > 0) {
                    movemap[i] |= figures[i] << (k * board_size);
                }
                long next = (figures[i] << ((k + 1) * board_size)) & opposite_color;
                if (next != 0L) {
                    addAttacked(figures[i], next);
                    movemap[i] |= figures[i] << ((k + 1) * board_size);
                    break;
                }*/
            }
        }
        //bitmap_to_chessboard(hor_ver);
        return movemap;
    }

    public static long[] diag_bitboard(long bitboard, long opposite_color, long same_color, int fig_idx) {
        /*creates a bitmap mask which marks the diagonals and anti-diagonals to mark the spots where a bishop (or queen) can possibly go to.*/
        /*For the diagonals and anti-diagonals I decided to reuse the logic I came up with previously (horizontals & verticals). The diagonals are horizontal and vertical lines that are rotated 45 degrees counterclockwise. this is why for example the left down anti-diagonal has the opposite shift operator as the left moving vertical line.*/
        long[] figures = get_single_figure_boards(bitboard); //separate figures into separate boards
        currAmount = figures.length;
        long[] movemap = new long[currAmount];
        posmapsIndividual[fig_idx] = new long[currAmount];
        for (int i = 0; i < currAmount; i++) { //loop over single figures
            posmapsIndividual[fig_idx][i] = figures[i];//we add the position of the current figure //TODO: remove that position again everywhere!!!
            long path; //squares which can be blocked to get out of check will be saved here if there is a check
            boolean set = false;
            Integer idx = get_squareIndex_of_figure(figures[i]);
            Integer[] file_row = idx_to_fileRank(idx);
            Integer file = file_row[1];
            Integer rank = file_row[0];
            path = 0L;
            for (int k = 0; k < file + 1; k++) {//move left down
                long current = (figures[i] << (k * (board_size - 1))) & same_color;
                path |= figures[i] << ((k + 1) * (board_size - 1));
                if ((current != 0L) & k != 0) { //collision with OWN piece at current square.
                    addprotected(figures[i], current);
                    break;
                }
                if (k > 0) {
                    movemap[i] |= figures[i] << (k * (board_size - 1));
                }
                long next = (figures[i] << ((k + 1) * (board_size - 1))) & opposite_color;
                if (next != 0L & k < (file + 1)) { //collision with OPPOSITE colored piece at next square
                    addAttacked(figures[i], next);
                    movemap[i] |= figures[i] << ((k + 1) * (board_size - 1));
                    set = false;
                    if (isKing(next)) {
                        addToBeBlocked(figures[i], path);
                        set = true;
                    }

                    long next_next;
                    for (int rem = k + 2; rem < file + 1; rem++) {
                        next_next = figures[i] << (rem * (board_size - 1));
                        path |= next_next;
                        if (set & rem == file) {
                            addRedZone(figures[i], path);
                        }
                        if (rem == file) {
                            addPinned(next);
                            addPinnedMovement(figures[i], path);
                        }
                    }
                    break;
                }
                /*long current = (figures[i] << (k * (board_size - 1))) & same_color;
                if ((current != 0L) & k != 0) { //collision with OWN piece at current square
                    addprotected(figures[i], current);
                    break;
                }
                if (k > 0) {
                    movemap[i] |= figures[i] << (k * (board_size - 1));
                }
                long next = (figures[i] << ((k + 1) * (board_size - 1))) & opposite_color;
                if (next != 0L) { //collision with OPPOSITE colored piece at next square
                    addAttacked(figures[i], next);
                    movemap[i] |= figures[i] << ((k + 1) * (board_size - 1));
                    break;
                }*/
            }
            path = 0L;
            for (int k = 0; k < board_size - file; k++) {//move right up
                long current = (figures[i] >>> (k * (board_size - 1))) & same_color;
                path |= figures[i] >>> ((k + 1) * (board_size - 1));
                if ((current != 0L) & k != 0) { //collision with OWN piece at current square.
                    addprotected(figures[i], current);
                    break;
                }
                if (k > 0) {
                    movemap[i] |= figures[i] >>> (k * (board_size - 1));
                }
                long next = (figures[i] >>> ((k + 1) * (board_size - 1))) & opposite_color;
                if (next != 0L & k < (board_size - file)) { //collision with OPPOSITE colored piece at next square
                    addAttacked(figures[i], next);
                    movemap[i] |= figures[i] >>> ((k + 1) * (board_size - 1));
                    set = false;
                    if (isKing(next)) {
                        addToBeBlocked(figures[i], path);
                        set = true;
                    }

                    long next_next;
                    for (int rem = k + 2; rem < board_size - file; rem++) {
                        next_next = figures[i] >>> (rem * (board_size - 1));
                        path |= next_next;
                        if (set & rem == board_size - file - 1) {
                            addRedZone(figures[i], path);
                        }
                        if (rem == board_size - file - 1) {
                            addPinned(next);
                            addPinnedMovement(figures[i], path);
                        }
                    }
                    break;
                }
                /*long current = (figures[i] >>> (k * (board_size - 1))) & same_color;
                if ((current != 0L) & k != 0) {
                    addprotected(figures[i], current);
                    break;
                }
                if (k > 0) {
                    movemap[i] |= figures[i] >>> (k * (board_size - 1));
                }
                long next = (figures[i] >>> ((k + 1) * (board_size - 1))) & opposite_color;
                if (next != 0L) {
                    addAttacked(figures[i], next);
                    movemap[i] |= figures[i] >>> ((k + 1) * (board_size - 1));
                    break;
                }*/
            }
            path = 0L;
            for (int k = 0; (k <= rank) & (k <= file); k++) {//move left up
                long current = (figures[i] >>> (k * (board_size + 1))) & same_color;
                path |= figures[i] >>> ((k + 1) * (board_size + 1));
                if ((current != 0L) & k != 0) { //collision with OWN piece at current square.
                    addprotected(figures[i], current);
                    break;
                }
                if (k > 0) {
                    movemap[i] |= figures[i] >>> (k * (board_size + 1)); //TODO: should't I also add it to movemapsIndividual?!
                }
                long next = (figures[i] >>> ((k + 1) * (board_size + 1))) & opposite_color;
                if (next != 0L & k < rank & k < file) { //collision with OPPOSITE colored piece at next square
                    addAttacked(figures[i], next);
                    movemap[i] |= figures[i] >>> ((k + 1) * (board_size + 1));
                    set = false;
                    if (isKing(next)) {
                        addToBeBlocked(figures[i], path);
                        set = true;
                    }

                    long next_next;
                    for (int rem = k + 2; (rem < (rank + 1)) & (rem < (file + 1)); rem++) {
                        next_next = figures[i] >>> (rem * (board_size + 1));
                        path |= next_next;
                        //TODO:is the line below correct?
                        if (set & ((rem == rank) | (rem < (file)))) {
                            addRedZone(figures[i], path);
                        }
                        if ((rem == rank) | (rem < (file))) { //TODO: only pinned if king behind (set==true)
                            addPinned(next);
                            addPinnedMovement(figures[i], path);
                        }
                    }
                    break;
                }
                /*long current = (figures[i] >>> (k * (board_size + 1))) & same_color;
                if ((current != 0L) & k != 0) {
                    addprotected(figures[i], current);
                    break;
                }
                if (k > 0) {
                    movemap[i] |= figures[i] >>> (k * (board_size + 1));
                }
                long next = (figures[i] >>> ((k + 1) * (board_size + 1))) & opposite_color;
                if (next != 0L) {
                    addAttacked(figures[i], next);
                    movemap[i] |= figures[i] >>> ((k + 1) * (board_size + 1));
                    break;
                }*/
            }
            path = 0L;
            for (int k = 0; k < (board_size - rank) & (k < board_size - file); k++) {//move right down
                long current = (figures[i] << (k * (board_size + 1))) & same_color;
                path |= figures[i] >>> ((k + 1) * (board_size + 1));
                if ((current != 0L) & k != 0) { //collision with OWN piece at current square.
                    addprotected(figures[i], current);
                    break;
                }
                if (k > 0) {
                    movemap[i] |= figures[i] << (k * (board_size + 1));
                }
                long next = (figures[i] << ((k + 1) * (board_size + 1))) & opposite_color;
                if (next != 0L & k < (board_size - rank) & (k < board_size - file)) { //collision with OPPOSITE colored piece at next square
                    addAttacked(figures[i], next);
                    movemap[i] |= figures[i] << ((k + 1) * (board_size + 1));
                    set = false;
                    if (isKing(next)) {
                        addToBeBlocked(figures[i], path);
                        set = true;
                    }

                    long next_next;
                    for (int rem = k + 2; (rem < (board_size - rank)) & (rem < (board_size - file)); rem++) {
                        next_next = figures[i] << (rem * (board_size + 1));
                        path |= next_next;
                        //TODO:is the line below correct?
                        if (set & ((rem == (board_size - rank - 1)) | (rem < (board_size - file - 1)))) {
                            addRedZone(figures[i], path);
                        }
                        if ((rem == (board_size - rank - 1)) | (rem < (board_size - file - 1))) {
                            addPinned(next);
                            addPinnedMovement(figures[i], path);
                        }
                    }
                    break;
                }
                /*long current = (figures[i] << (k * (board_size + 1))) & same_color;
                if ((current != 0L) & k != 0) {
                    addprotected(figures[i], current);
                    break;
                }
                if (k > 0) {
                    movemap[i] |= figures[i] << (k * (board_size + 1));
                }
                long next = (figures[i] << ((k + 1) * (board_size + 1))) & opposite_color;
                if (next != 0L) {
                    addAttacked(figures[i], next);
                    movemap[i] |= figures[i] << ((k + 1) * (board_size + 1));
                    break;
                }*/
            }
        }
        //bitmap_to_chessboard(diag);
        return movemap;
    }

    public static int[][] getMoves() {
        //TODO:
        int start = 0;
        if (whitesTurn) {
            start = 6;
        }
        int end = 6;
        if (whitesTurn) {
            end = 12;
        }
        int n = 0;
        for (int i = start; i < end; i++) {
            if (!isNull(movemapsIndividual[i])) {
                for (int j = 0; j < movemapsIndividual[i].length; j++) {
                    n += Long.bitCount(movemapsIndividual[i][j]);
                }
            }
        }
        int[][] coordinate = new int[n][7]; //a3h3 movement for example -->̣ [3, 1,3,8,3, 0,0] the first number represents the piece (here a black rook). The second last number represents movemens (0) vs captures (1). The last number represents promotions (0-4), castling (5-6), double pawn moves (7).
        for (int i = 0; i < 12; i++) { //for all figures
            for (int j = 0; j < Long.bitCount(movemaps[i]); j++) { //for all possible movements for that figure
                if (!isNull(movemapsIndividual[i][j])) {
                    int idx = get_squareIndex_of_figure(movemapsIndividual[i][j]);
                    int file = idx / board_size;
                    int rank = idx % board_size;
                }
            }
        }
        return coordinate;
    }

    public static String convertMove(int[] move) {
        StringBuilder builder = new StringBuilder();
        builder.append(gtfig(move[0]) + " ");
        builder.append((char) (move[1] + 97));
        builder.append(move[2] + 1);
        builder.append((char) (move[3] + 97));
        builder.append(move[4] + 1);
        return builder.toString();
    }

    public static void valid_moves() {

        if (whitesTurn) { //TODO: not sure if I can do this. Is there a scenario where we need to initiate valid moves of the opposite color to
            valid_white_moves();
        } else {
            valid_black_moves();
        }
    }

    public static void valid_black_moves() { //black figure moves:
        movemaps[5] &= ~REDZONEB; //remove invalid king moves
        movemapsIndividual[5][0] &= ~REDZONEB; //get black king
        if (nrOfwAttackers == 1) { //if we don't have more than one piece attacking the king...
            // ...and we can capture the attacker piece...or block the attacker piece:
            for (int fig = 0; fig < 5; fig++) { //we exclude the king(5) from this calculation because he can't be pinned nor block his own check
                movemaps[fig] &= (locOfwAttackers | blockLocationsB);
                long[] figures = get_single_figure_boards(bitmaps[fig]);
                for (int i = 0; i < figures.length; i++) { //own figure captures attacker piece or blocks the path to the king
                    if ((movemapsIndividual[fig][i] | figures[i]) != 0L) { //TODO: double check logic
                        movemapsIndividual[fig][i] &= locOfwAttackers;
                        if ((movemapsIndividual[fig][i] & pinnedB) == 0L) { //if I'm not a pinned piece
                            movemapsIndividual[fig][i] &= blockLocationsB;
                        }
                    }
                }
            }
        } else if (nrOfwAttackers >= 2) { //we don't allow any movements except the king moving out of chess.
            //remove all possibilities that aren't king moves (only king moves possible):
            for (int fig = 0; fig < 5; fig++) {
                long[] figures = get_single_figure_boards(bitmaps[fig]);
                for (int i = 0; i < figures.length; i++) { //pawn captures attacker piece or blocks the path
                    if ((movemapsIndividual[fig][i] | figures[i]) != 0L) { //TODO: replace with posmapsIndividual[fig][i]
                        movemapsIndividual[fig][i] = 0L;
                    }
                }
            }
        } else { //if nrOfwAttackers==0
            if (pinnedB != 0L) { //if we have pinned pieces //TODO: pawn can still move forward if pinnned by a bishop...
                for (int fig = 0; fig < 5; fig++) { //pieces of type T...
                    if ((bitmaps[fig] & pinnedB) != 0) { //some figure of type T is among the pinned pieces... optimization, so we can opt out early
                        for (int i = 0; i < movemapsIndividual[fig].length; i++) { //...lets find out which one...
                            if (((movemapsIndividual[fig][i] | bitmaps[fig]) & pinnedB) != 0L) { //combine movemap with figure position to find out which movemap is the pinned one
                                /*Instead of limiting the pinned piece to 'movemapsq[i] = 0L;' where it can only stay at the same spot, we are finding the 'ray pins' between the king and a attacking piece which pins the piece in between. This can be quite an expensive calculation and the suggested approach here is that we calculate in all directions the moves from the opponent's sliding pieces, the sliding piece moves from the king in the opposite direction and the overlap of these two rays (https://peterellisjones.com/posts/generating-legal-chess-moves-efficiently/). I do this differently and less expensive. I already gathered all information I need and prepared it. I only need to access the right ray and substract my own pieces. Thats the movement the pinned piece can make. thats it.*/
                                //TODO: not sure if this works.... also: needs to be implemented for nrOfwAttackers==1 & nrOfwAttackers>=2...
                                //TODO: ...does not work... pawn can now move out of it's pinned position..
                                if (((movemapsIndividual[fig][i] | posmapsIndividual[fig][i]) & pinnedB) != 0L) {//(((movemapsIndividual[fig][i] & ~(bitmaps[fig])) & pinnedMovement) != 0L) {
                                    movemapsIndividual[fig][i] = 0L;//(movemapsIndividual[fig][i] & pinnedMovement); //here we combine the movement map of the individual piece with the pinned rays map of all pieces. The result of the &-operation of the 2 maps is the path on which the pinned piece is allowed to move. this works only because we have only one king on the board of each color. If there were multiple then this logic would fall apart as soon as both kings have pinned pieces and the pinned movement map of one piece extends the movement of the other piece.
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void valid_white_moves() {
        movemaps[11] &= ~REDZONEW; //remove invalid king moves
        movemapsIndividual[11][0] &= ~REDZONEW; //get black king
        if (nrOfbAttackers == 1) { //if we don't have more than one piece attacking the king...
            // ...and we can capture the attacker piece...or block the attacker piece:
            for (int fig = 6; fig < 10; fig++) { //we exclude the king from this calculation because he can't be pinned nor block his own check
                movemaps[fig] &= (locOfbAttackers | blockLocationsW);
                long[] figures = get_single_figure_boards(bitmaps[fig]);
                for (int i = 0; i < figures.length; i++) { //pawn captures attacker piece or blocks the path
                    if ((movemapsIndividual[fig][i] | figures[i]) != 0L) {
                        movemapsIndividual[fig][i] &= (locOfbAttackers | blockLocationsW);
                        if ((movemapsIndividual[fig][i] & pinnedW) == 0L) { //if I'm not a pinned piece
                            movemapsIndividual[fig][i] &= blockLocationsW;
                        }
                    }
                }
            }
        } else if (nrOfbAttackers >= 2) { //we don't allow any movements except the king moving out of chess.
            //remove all possibilities that aren't king moves (only king moves possible):
            for (int fig = 6; fig < 10; fig++) {
                long[] figures = get_single_figure_boards(bitmaps[fig]);
                for (int i = 0; i < figures.length; i++) { //pawn captures attacker piece or blocks the path
                    if ((movemapsIndividual[fig][i] | figures[i]) != 0L) {
                        movemapsIndividual[fig][i] = 0L;
                    }
                }
            }
        } else { //if nrOfbAttackers==0
            if (pinnedW != 0L) { //if we have pinned pieces //TODO: pawn can still move forward if pinnned by a bishop...
                for (int fig = 6; fig < 10; fig++) { //pieces of type T...
                    if ((bitmaps[fig] & pinnedW) != 0) { //some figure of type T is among the pinned pieces... optimization, so we can opt out early
                        for (int i = 0; i < movemapsIndividual[fig].length; i++) { //...lets find out which one...
                            if (((movemapsIndividual[fig][i] | bitmaps[fig]) & pinnedW) != 0L) { //combine movemap with figure position to find out which movemap is the pinned one
                                if (((movemapsIndividual[fig][i] | posmapsIndividual[fig][i]) & pinnedW) != 0L) {//(((movemapsIndividual[fig][i] & ~(bitmaps[fig])) & pinnedMovement) != 0L) {
                                    movemapsIndividual[fig][i] = 0L;//(movemapsIndividual[fig][i] & pinnedMovement); //here we combine the movement map of the individual piece with the pinned rays map of all pieces. The result of the &-operation of the 2 maps is the path on which the pinned piece is allowed to move. this works only because we have only one king on the board of each color. If there were multiple then this logic would fall apart as soon as both kings have pinned pieces and the pinned movement map of one piece extends the movement of the other piece.
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /*public static void initiate_inCheck() {
        if (whitesTurn) {
            if ((REDZONEW & bitmaps[11]) != 0L) { //if the white king is on an attackable square
                WINCHECK = true;
            }
        } else {
            if ((REDZONEB & bitmaps[5]) != 0L) {
                BINCHECK = true;
            }
        }
    }*/

    public static void initiate_red_zone_white() {
        /*this method does not initialize the moves. This method should only be called after the moves have been initialized otherwise we get the red-zone of the previous round. */
        //REDZONEW=0L;
        REDZONEW |= movemaps[0] | movemaps[1] | movemaps[2] | movemaps[3] | movemaps[4] | movemaps[5];
        REDZONEW &= ~pPOSM; //TODO: doesn't work!!!!   //remove regular pawn movements as they are not attacking //TODO: consider en-passant move. How to deal with them? they're only dangerous to pawns. for now they are just ignored
        //REDZONEW &= ~WPROTECTED;
        //System.out.println("white redzone:");
        //bitmap_to_chessboard(REDZONEW); //TODO: test with start pos fen
    }

    public static void initiate_redzone() {
        REDZONEB = 0L; //TODO: this may be a problem since data of the other king is lost? I don't think it is tough
        REDZONEW = 0L;
        if (whitesTurn) {
            initiate_red_zone_white();
        } else {
            initiate_red_zone_black();
        }
    }

    public static void initiate_red_zone_black() {
        /*this method does not initialize the moves. This method should only be called after the moves have been initialized otherwise we get the red-zone of the previous round. */
        //REDZONEB=0L;
        REDZONEB |= movemaps[6] | movemaps[7] | movemaps[8] | movemaps[9] | movemaps[10] | movemaps[11];
        REDZONEB &= ~PPOSM; //remove regular pawn movements as they are not attacking
        //REDZONEB &= ~BPROTECTED; //TODO: not working somehow
        //System.out.println("black redzone:");
        //bitmap_to_chessboard(REDZONEB); //TODO: test with start pos fen
    }

    public static long clearOverflow(long bitboard, long movesbitboard) {
        //We don't need to worry about overflow to the top or bottom as those bits just disappear into nowhere. we do however need to clear the attack squares which overlap the file boarders and move to the other side of the board:
        if ((bitboard & (FILES[0] | FILES[1])) != 0L) { //if we are too far to the left, we need to clear the right...
            movesbitboard = (movesbitboard | (FILES[6] | FILES[7])) & (movesbitboard & ~(FILES[6] | FILES[7])); //clear the files on the right
        } else if ((bitboard & (FILES[6] | FILES[7])) != 0L) { //if we are too far to the right, we need to clear the left...
            movesbitboard = (movesbitboard | (FILES[0] | FILES[1])) & (movesbitboard & ~(FILES[0] | FILES[1])); //clear the files on the left
        }
        return movesbitboard;
    }

    public static Stack<long[]> generate_successors(long history) {
        int start = 0;
        if (whitesTurn) {
            start = 6;
        }
        int end = 6;
        if (whitesTurn) {
            end = 12;
        }
        Stack<long[]> successors = new Stack<>();
        for (int type = start; type < end; type++) { //forall types (black pawns, white rooks, etc.
            long[] figures = get_single_figure_boards(bitmaps[type]); //get every figure
            for (int i = 0; i < figures.length; i++) { //for all pieces of the same type (w_pawn_1, w_pawn_2, etc.)
                //bitmap_to_chessboard(figures[i]);
                long[] movements = get_single_figure_boards(movemapsIndividual[type][i]); //every figure has multiple moves
                for (int m = 0; m < movements.length; m++) {//for all possible individual movements
                    long[] newState = bitmaps.clone();
                    if ((movements[m] & (BLACKPIECES | WHITEPIECES)) != 0L) { //we are capturing a piece
                        for (int k = 0; k < 12; k++) {
                            if ((movements[m] & newState[k]) != 0L) {
                                newState[k] &= ~(movements[m] & (BLACKPIECES | WHITEPIECES));
                            }
                        }
                    }
                    //pawn extra movements:
                    if (type == 0 | type == 6) { //it's a pawn
                        if ((movements[m] & (RANKS[0] | RANKS[7])) != 0L) { //pawn promotes
                            if ((figures[i] & BLACKPIECES) != 0L) { //we are dealing with a black piece
                                long[] copy;
                                for (int p = 0; p < 4; p++) {
                                    copy = newState.clone();
                                    copy[type] = (newState[type] & ~figures[i]); //remove original figure
                                    copy[p + 1] = movements[m];
                                    successors.push(copy);
                                }
                            } else if ((figures[i] & WHITEPIECES) != 0L) { //we are dealing with a white piece
                                long[] copy;
                                for (int p = 6; p < 10; p++) {
                                    copy = newState.clone();
                                    copy[type] = (newState[type] & ~figures[i]); //remove original figure
                                    copy[p + 1] = movements[m];
                                    successors.push(copy);
                                }
                            }
                        } else if (((figures[i] & (RANKS[3] | RANKS[4])) != 0L) & ((((history >>> 16) & history) != 0L)|(((history << 16) & history) != 0L))) {//en-passant capture position , if history shifted by 2 squares forwards or backwards does not overlap with itself, then the pawn did not do a double move but a single move
                            //TODO: check history: piece must have moved in last turn
                            long next_to_me = ((figures[i] >>> 1) & history | (figures[i] << 1) & history);
                            if (next_to_me != 0L) { //if the piece is next to me
                                long[] copi = newState.clone();
                                if (((figures[i] & BLACKPIECES) != 0L) & ((next_to_me & bitmaps[gtidx('P')]) != 0L)) { //we are dealing with a black pawn capturing a white pawn
                                    copi[type] = (copi[type] & ~figures[i]); //remove original figure
                                    copi[gtidx('P')] &= ~next_to_me; //remove captured pawn
                                    copi[type] |= next_to_me << 8; //position pawn behind captured pawn
                                    successors.push(copi);
                                } else if (((figures[i] & WHITEPIECES) != 0L) & ((next_to_me & bitmaps[gtidx('p')]) != 0L)) { //we are dealing with a white piece
                                    copi[type] = (copi[type] & ~figures[i]); //remove original figure
                                    copi[gtidx('p')] &= ~next_to_me; //remove captured pawn
                                    copi[type] |= next_to_me >>> 8; //position pawn behind captured pawn
                                    successors.push(copi);
                                }
                            }
                        }
                    } else { //normal move
                        newState[type] = movements.clone()[m] | (newState[type] & ~figures[i]); //remove original figure and add the to movement
                        successors.push(newState);
                    }
                    newState[type] = movements.clone()[m] | (newState[type] & ~figures[i]); //remove original figure and add the to movement
                    successors.push(newState);
                    //TODO: IFF en-passant has been made: remove enpassant captured piece & if king is in check after, don't add the move
                    //TODO: IFF pawn promotion: generate states here!
                    //TODO: return 0L map if no more moves possible to not run into null value problems

                    //bitmaps_to_chessboard(newState);
                    /*if (type==gtidx('Q')){
                        bitmaps_to_chessboard(newState);
                    }*/
                }
            }
        }
        /*for (int i = 0; i < successors.size(); i++) {
            bitmaps_to_chessboard(successors.elementAt(i));
        }
        System.exit(4);*/
        return successors;
    }
}

import chess
import FEN

def validate(start_FEN, goal_FEN, plan):
    original_size=start_FEN.count('/')+1
    start_board = FEN.FEN_to_Chess_board(FEN.expand_board(start_FEN,8))
    goal_board = FEN.FEN_to_Chess_board(FEN.expand_board(goal_FEN,8))
    for i in range(len(plan)-1):
        line=plan[i]
        print(line)
        board=chess.Board(FEN.board_to_FEN(start_board)+' w KQkq - 0 1') #Documentation: https://github.com/niklasf/python-chess
        board2=chess.Board(FEN.board_to_FEN(start_board)+' b KQkq - 0 1')
        [start_board,move]=FEN.next_pos(start_board,line,original_size)
        if chess.Move.from_uci(move) in board.legal_moves or chess.Move.from_uci(move) in board2.legal_moves:
            print('> not a legal move: {}'.format(move))
            return False
        else:
            pass
    return True


#plan=['(rook_move rook_w1 n4 n2 n5 n2)','(bishop_move bishop_b1 n5 n1 n1 n5)','(queen_move queen_b1 n5 n4 n4 n5)','; cost = 6 (unit cost)',''][:-2]
#print(validate('P2B1/4q/5/3R1/4b','b2q1/5/5/4R/5',plan))
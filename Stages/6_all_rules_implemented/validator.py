import chess
import FEN

def validate(start_FEN, goal_FEN, plan):
    s=FEN.add_coordinate_System(FEN.printable_board(FEN.FEN_to_Chess_board(start_FEN,start_FEN.count('/')+1),True,True))
    g=FEN.add_coordinate_System(FEN.printable_board(FEN.FEN_to_Chess_board(goal_FEN,start_FEN.count('/')+1),True,True))
    FEN.print_neighbor(s,g)
    print('\t> validating [\'{}\', \'{}\']'.format(start_FEN, goal_FEN))
    original_size=start_FEN.count('/')+1
    start_board = FEN.FEN_to_Chess_board(FEN.expand_board(start_FEN,8),8)
    goal_board = FEN.FEN_to_Chess_board(FEN.expand_board(goal_FEN,8),8)
    #start_FEN=FEN.expand_board(start_FEN,8)
    for i in range(len(plan)-1):
        line=plan[i]
        #print(line)
        board=chess.Board(FEN.board_to_FEN(start_board)+' w KQkq - 0 1') #Documentation: https://github.com/niklasf/python-chess
        board2=chess.Board(FEN.board_to_FEN(start_board)+' b KQkq - 0 1')
        print(board)
        #print(board)
        [start_board,move]=FEN.next_pos(start_board,line,original_size)
        #print(move)
        #print('...',chess.Move.from_uci(move))
        valid_moves=sorted(set([str(m) for m in list(board.legal_moves)]+[str(m) for m in list(board2.legal_moves)]))
        #print(valid_moves)
        if chess.Move.from_uci(move) not in board.legal_moves and chess.Move.from_uci(move) not in board2.legal_moves:
            print('> not a legal move: {}'.format(move))
            return False
        else:
            pass
    print('True')
    exit()
    return True
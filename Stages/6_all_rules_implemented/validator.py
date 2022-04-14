import chess
import FEN

def validate(start_FEN, goal_FEN, plan, color=None):
    original_size=start_FEN.count('/')+1
    #s=FEN.add_coordinate_System(FEN.printable_board(FEN.FEN_to_Chess_board(start_FEN,original_size),True,True))
    #g=FEN.add_coordinate_System(FEN.printable_board(FEN.FEN_to_Chess_board(goal_FEN,original_size),True,True))
    #FEN.print_neighbor(s,g)
    #print('\t> validating [\'{}\', \'{}\']'.format(start_FEN, goal_FEN))
    #print('\t > The Plan: ', plan)

    start_FEN=FEN.expand_board(start_FEN,8)
    goal_FEN=FEN.expand_board(goal_FEN,8)
    if color!=None:
        if color[0]=='w':
            start_FEN+=+' w KQkq - 0 1'
        if color[0]=='b':
            start_FEN+=+' b KQkq - 0 1'
    start_board = FEN.expand_board(start_FEN,8)
    print(start_board)
    goal_board = FEN.expand_board(goal_FEN,8)
    board=chess.Board(start_board) #Documentation: https://github.com/niklasf/python-chess
    #board2=chess.Board(FEN.board_to_FEN(start_board)+' b KQkq - 0 1')
    for i in range(len(plan)-1):
        line=plan[i]
        move=chess.Move.from_uci(FEN.next_move(line,original_size))
        print('in:')
        print(board)
        valid_moves=[str(m) for m in list(board.legal_moves)]
        if move not in board.legal_moves:
            print('  \033[93m   --> This is an INVALID plan! move nr.{} is illegal: {} {}\033[0m'.format(i,move,line))
            return False
        board.push(move) #TODO: test if this really updates the board or if I need to set it = also
        print('next:',move,line)
        print(board)
    return True

#validate('5/1pppp/1R1N1/PPP2/5', '5/Ppp2/RPpN1/4p/5',['(rook_move rook_w1 n2 n3 n1 n3)\n','(pawn_move_one pawn_w2 n2 n2 n2 n3)\n','(pawn_move_two pawn_b4 n5 n4 n5 n2)\n','(pawn_move_one pawn_w3 n3 n2 n3 n3)\n','(pawn_capture pawn_b3 n4 n4 n3 n3)\n','(rook_move rook_w1 n1 n3 n1 n5)\n','(rook_move rook_w1 n1 n5 n4 n5)\n','(pawn_move_two pawn_w1 n1 n2 n1 n4)\n','(knight_move knight_w1 n4 n3 n3 n1)\n','(rook_move rook_w1 n4 n5 n4 n2)\n','(rook_move rook_w1 n4 n2 n1 n2)\n','(rook_move rook_w1 n1 n2 n1 n3)\n','(knight_move knight_w1 n3 n1 n4 n3)\n','; cost = 13 (unit cost)\n'])
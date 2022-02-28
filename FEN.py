import valid_moves

def printable_board(board,color=False,symbols=False):
    """
    takes a FEN_to_Chess_board() board matrix and returns a string representing the given board that can be printed.
    optional parameters: 'color' determines if black is colored as green and the parameter 'symbols' encodes the figures as Unicode symbols to make it more appealing.
    """
    R=''
    for i in range(8):
        r=''
        if color:
            for j in range(8):
                symbol=board[i][j]
                letter=symbol
                if symbols and symbol.lower() in ['p','n','b','r','q','k']:
                    symbol=vars(figures_symbols)[symbol]

                if letter.islower(): #black pieces
                    r+='\033[1m\u001b[32m'+symbol+'\033[0m '
                elif letter.isupper(): #white pieces
                    r+='\033[1m'+symbol+'\033[0m '
                else: #empty space
                    if (i+j)%2==0:
                        r+='\033[38;5;237m'+symbol+'\033[0m '
                    else:
                        r+='\033[38;5;22m'+symbol+'\033[0m '
        else:
            for j in range(8):
                symbol=board[i][j]
                letter=symbol
                if symbols and symbol.lower() in ['p','n','b','r','q','k']:
                    symbol=vars(figures_symbols)[symbol]

                if letter.islower(): #black pieces
                    r+=symbol+' '
                elif letter.isupper(): #white pieces
                    r+=symbol+' '
                else: #empty space
                    r+=symbol+' '
        R+=r+'\n'
    return R

def true_board_squares(board,filler='⬚'):
    """takes a FEN_to_Chess_board() board as input and gives an array containing the positions of the pieces as tuples (Ex: ('A1', 'ROOK_w') )"""
    square_names=valid_moves.matrix_form_board(valid_moves.board())
    List=[]
    for rank in range(8):
        for file in range(8):
            if board[7-rank][file]!=filler: #TODO: Issue: Check if this is correct... 7-rank here and not in the last line of this if statement...
                name=FEN_names_to_pddl_names(board[7-rank][file],(7-rank,file))
                List.append((square_names[rank][file],name))
    return List

def printable_true_board_squares(List,prefix,indent=''):
    """convert true_board_squares() to string which can be either printed or inserted into a PDDL file"""
    L=''
    for e in List:
        L+=indent+'('+prefix+' '+str(e[1])+' '+str(e[0])+')\n'
    return L[:-1]

def FEN_names_to_pddl_names(figure,square): #TODO: this function is wrong somehow...
    """takes a FEN code figure name (p,n,b,...) as input and gives back the corresponding name defined in the figures() dictionary"""
    if figure.lower()!='b':
        return vars(figures)[figure]
    else: #there are two square colored bishops
        if (square[0]+square[1])%2==0: #black if even TODO: somehow this is reversed: black if uneven but I dont know why... it is NOT because it starts at 0 because then black would still be even
            #print('>>> ',square[0],square[1],'white')
            return vars(figures)[figure][0] #TODO: issue: returns ''__main__' as well... don't know if this may cause problems... : ['__main__', 'pawn', 'knight', 'w_bishop', 'b_bishop', 'rook', 'queen', 'king', 'PAWN', 'KNIGHT', 'W_BISHOP', 'B_BISHOP', 'ROOK', 'QUEEN', 'KING', <attribute '__dict__' of 'figures' objects>, <attribute '__weakref__' of 'figures' objects>, None]
        else:
            return vars(figures)[figure][1]

class figures:
    """how I want to name my pieces"""
    #black pieces:
    p='pawn_b'
    n='knight_b'
    b=['w_bishop_b','b_bishop_b']
    r='rook_b'
    q='queen_b'
    k='king_b'
    #white pieces:
    P='PAWN_w'
    N='KNIGHT_w'
    B=['W_BISHOP_w','B_BISHOP_w']
    R='ROOK_w'
    Q='QUEEN_w'
    K='KING_w'

class figures_symbols:
    """Unicode characters for the chess pieces"""
    #white pieces:
    P='\u265F'
    N='\u265E'
    B='\u265D'
    R='\u265C'
    Q='\u265B'
    K='\u265A'
    #black pieces:
    p='\u2659'
    n='\u2658'
    b='\u2657'
    r='\u2656'
    q='\u2655'
    k='\u2654'

def all_figures():
    """returns an array containing all figure names as defined in the dictionary"""
    fig=vars(figures)
    List=[]
    for f in fig:
        if isinstance(fig[f], list):
            for e in fig[f]:
                List.append(e)
        elif isinstance(fig[f], str) and fig[f]!='__main__' and fig[f]!='FEN':
            List.append(fig[f])
    return List

def printable_list_of_allFigures(figures,indent='',prefix=''):
    """takes an array and gives back a string which is indented by 'indent'"""
    List=indent
    for e in figures:
        #List+='('+prefix+' '+e+')\n'
        List+=e+' '
    return List[:-1]

def print_neighbor(board1,board2,indent='\t'):
    """prints two boards next to one another"""
    board1='\033[38;5;22m\033[4mInit Position:\033[0m   \n'+board1
    board1=board1.split('\n')
    board2='\033[38;5;22m\033[4mGoal Position:\033[0m   \n'+board2
    board2=board2.split('\n')
    for line in range(len(board1)):
        print(board1[line],indent,board2[line])

def FEN_to_Chess_board(FEN,filler='⬚'):
    """converts a FEN code to a 2d array representing the chess board"""
    #lowercase letters=black pieces
    FEN=FEN.split()[0]
    board=valid_moves.board()

    rank=0
    file=0
    for pos in range(len(FEN)):
        if FEN[pos]=='/':
            rank+=1
            file=0
        else:
            if not FEN[pos].isnumeric():
                board[rank][file]=FEN[pos]
                file+=1
            elif FEN[pos].isnumeric():
                for i in range(int(FEN[pos])):
                    board[rank][file+i]=filler
                if file==7:
                    file=0
                else:
                    file+=int(FEN[pos])
    return board

def add_coordinate_System(board):
    """adds a nice coordinate system around the given chess board"""
    board=board.split('\n')
    extended_board=''
    for row in range(8):
        extended_board+='\033[38;5;236m\033[3m'+str(8-(row))+'\033[0m '+board[row]+'\n'
    extended_board=extended_board[:-2]+'\n\033[38;5;236m\033[3m  a b c d e f g h\033[0m'
    return extended_board

def board_to_FEN(board):
    """converts a given board position to a FEN code. it is the reverse operation of FEN_to_Chess_board():
        board=FEN_to_Chess_board('rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR','`')
        code=board_to_FEN(board)
        where the variable 'code' has the same value as the given FEN code"""
    FEN=''
    spaces=0
    file=1
    for rank in board:
        for e in rank:
            if e.lower() not in ['p','n','b','r','q','k']:
                spaces+=1
                #print(spaces,file)
            elif e.lower() in ['p','n','b','r','q','k']:
                if spaces>0:
                    FEN+=str(spaces)
                    spaces=0
                FEN+=e
            if spaces==8 or (spaces>0 and file==8):
                FEN+=str(spaces)
                spaces=0
            if file==8:
                FEN+='/'
                file=0
            file+=1
    return FEN[:-1]
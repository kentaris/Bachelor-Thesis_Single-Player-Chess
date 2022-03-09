import FEN
import valid_moves
board_size=5

def FEN_pos_to_PDDL(fen):
    length=fen.count('/')+1
    R=''
    board=FEN.FEN_to_Chess_board(fen)
    for rank in range(length):
        for file in range(length):
            if board[rank][file] in vars(FEN.figures):
                idx=0
                while vars(figures)[board[rank][file]][idx] in R: #possible source of errors: if idx gets bigger than # elements. but should only happen if I rename something and forget about this.
                    idx+=1
                figure=vars(figures)[board[rank][file]][idx]
                R+='\t\t(at '+figure+' f'+str(rank+1)+' r'+str(file+1)+')\n'
    return R

class figures:
    #black pieces:
    p=['pawn_b1','pawn_b2','pawn_b3','pawn_b4','pawn_b5','pawn_b6','pawn_b7','pawn_b8']
    n=['knight_b1','knight_b2']
    b=['w_bishop_b','b_bishop_b']
    r=['rook_b1','rook_b2']
    q=['queen_b']
    k=['king_b']
    #white pieces:
    P=['pawn_w1','pawn_w2','pawn_w3','pawn_w4','pawn_w5','pawn_w6','pawn_w7','pawn_w8']
    N=['knight_w1','knight_w2']
    B=['w_bishop_w','b_bishop_w']
    R=['rook_w1','rook_w2']
    Q=['queen_w']
    K=['king_w']

'''knight_w1 knight_w2 - knight_w
        ;knight_b1 knight_b2 - knight_b
        pawn_w1 pawn_w2 pawn_w3 pawn_w4 pawn_w5 - pawn_w'''

def white_pawn_diffByOneTwo(): #ToDO
    R=''
    diffByOne_array=create_diffBy_list(1,'one_pawn_w',True)
    for t in diffByOne_array:
        for k in ['f','r']:
            for l in ['f','r']:
                prelude='diff_by_{}'.format(name)#_'+k+l
                diffBy+='\t\t({} {}{} {}{})\n'.format(prelude,k,t[0],l,t[1])
    diffByTwo_array=create_diffBy_list(2,'two_pawn_w',True)

def create_diffBy_list(diff,name,get=False):
    diffBy_array=[]
    for rank in range(board_size): #rank
        for file in range(board_size): #file
            if abs((rank+1)-(file+1))==diff:
                diffBy_array.append((rank+1,file+1))
    if get:
        return diffBy_array
    diffBy=''
    for t in diffBy_array:
        for k in ['f','r']:
            for l in ['f','r']:
                prelude='diff_by_{}'.format(name)#_'+k+l
                diffBy+='\t\t({} {}{} {}{})\n'.format(prelude,k,t[0],l,t[1])
    return diffBy

def diffByZeroOneTwo():
    R=''
    dbz=create_diffBy_list(0,'zero')
    dbo=create_diffBy_list(1,'one')
    dbt=create_diffBy_list(2,'two')
    R+='\t\t;Difference by zero:\n'+dbz+'\n\t\t;Difference by one:\n'+dbo+'\n\t\t;Difference by two:\n'+dbt
    return R

def board():
    R=''
    for rank in range(board_size):
        r=''
        for file in range(board_size):
            r+=chr(rank+1+64)+str(file+1)+' '
        R+='\t\t'+r+'\n'
    return R
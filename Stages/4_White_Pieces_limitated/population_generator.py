import FEN

board_size=5 #to change the board size

def add_FEN_pos_to_PDDL(fen):
    '''returns the PDDL line format of the occupied board positions of a given FEN string'''
    length=fen.count('/')+1
    R=''
    board=FEN.FEN_to_Chess_board(fen)
    for rank in range(length):
        for file in range(length):
            if board[rank][file] in vars(FEN.figures):
                idx=0 #ToDo: I don't know which pawn moved to which position but I number them from left to right atm...
                while vars(figures)[board[rank][file]][idx] in R: #possible source of errors: if idx gets bigger than # elements. but should only happen if I rename something and forget about this.
                    idx+=1
                figure=vars(figures)[board[rank][file]][idx]
                R+='\t\t(at '+figure+' n'+str((file+1))+' n'+str(board_size-(rank))+')\n'
    return R
    
class figures:
    #black pieces:
    p=['pawn_b1','pawn_b2','pawn_b3','pawn_b4','pawn_b5','pawn_b6','pawn_b7','pawn_b8']
    n=['knight_b1','knight_b2']
    b=['w_bishop_b1','b_bishop_b1']
    r=['rook_b1','rook_b2']
    q=['queen_b1']
    k=['king_b1']
    #white pieces:
    P=['pawn_w1','pawn_w2','pawn_w3','pawn_w4','pawn_w5','pawn_w6','pawn_w7','pawn_w8']
    N=['knight_w1','knight_w2']
    B=['w_bishop_w1','b_bishop_w1']
    R=['rook_w1','rook_w2']
    Q=['queen_w1']
    K=['king_w1']

def add_double_pawn_moves():
    R=''
    R+='\n\t\t;Pawn double moves for white:\n'
    R+=pawn_double('white')
    R+='\n\t\t;Pawn double moves for black:\n'
    R+=pawn_double('black')
    return R

def add_one_forward():
    R=''
    R+='\n\t\t;Pawn single moves for white:\n'
    R+=one_forward('white')
    R+='\n\t\t;Pawn single moves for black:\n'
    R+=one_forward('black')
    return R

def add_bishop_moves(n):
    R=''
    prelude=n*'\t'
    for i in range(board_size):
        R+='{}   (and (diff_by_{} ?from_file ?to_file) ;file +/-{}\n'.format(prelude,num2word(i+1),i+1)
        R+='{}        (diff_by_{} ?from_rank ?to_rank) ;rank +/-{}\n{}   )\n'.format(prelude,num2word(i+1),i+1,prelude)
    return R

def one_forward(type):
    '''returns the PDDL line format of type={'white', 'black'} for pawn single moves'''
    R=''
    for from_rank in range(board_size):
        for to_rank in range(board_size):
            if type=='white': #white
                if to_rank-from_rank==1:# and (from_rank+1)>1:
                    prelude='plusOne_{}'.format(type)
                    R+='\t\t({} {}{} {}{})\n'.format(prelude,'n',from_rank+1,'n',to_rank+1)
            else: #black
                if to_rank-from_rank==-1:# and (from_rank+1)<board_size:
                    prelude='plusOne_{}'.format(type)
                    R+='\t\t({} {}{} {}{})\n'.format(prelude,'n',from_rank+1,'n',to_rank+1)
    return R

def pawn_double(type):
    '''returns the PDDL line format of type={'white', 'black'} for pawn double moves'''
    R=''
    diffBy_array=[]
    to_rank=4
    from_rank=2
    if type=='black': to_rank=board_size-3; from_rank=board_size-1
    for file in range(board_size):
        diffBy_array.append((file+1,file+1,from_rank,to_rank))
    for t in diffBy_array:
        prelude='pawn_double_{}'.format(type)
        R+='\t\t({} {}{} {}{} {}{} {}{})\n'.format(prelude,'n',t[0],'n',t[1],'n',t[2],'n',t[3])
    return R

def create_diffBy_list(diff,name,get=False):
    '''returns the pddl line format of the given diff (=Difference) of two numbers. it returns all combinations of f and r. 'name' refers to the diff_by_[name] and 'get' is not used anywhere yet.'''
    diffBy_array=[]
    for rank in range(board_size): #rank
        for file in range(board_size): #file
            if abs((rank+1)-(file+1))==diff:
                diffBy_array.append((rank+1,file+1))
    if get:
        return diffBy_array
    diffBy=''
    for t in diffBy_array:
        prelude='diff_by_{}'.format(name)#_'+k+l
        diffBy+='\t\t({} {}{} {}{})\n'.format(prelude,'n',t[0],'n',t[1])
    return diffBy

def num2word(n):
    '''converts a numeric number to a letter word'''
    #credit: https://stackoverflow.com/a/19506803
    num2words= {1: 'One', 2: 'Two', 3: 'Three', 4: 'Four', 5: 'Five', 6: 'Six', 7: 'Seven', 8: 'Eight', 9: 'Nine', 10: 'Ten', 11: 'Eleven', 12: 'Twelve', 13: 'Thirteen', 14: 'Fourteen', 15: 'Fifteen', 16: 'Sixteen', 17: 'Seventeen', 18: 'Eighteen', 19: 'Nineteen', 20: 'Twenty', 30: 'Thirty', 40: 'Forty', 50: 'Fifty', 60: 'Sixty', 70: 'Seventy', 80: 'Eighty', 90: 'Ninety', 0: 'Zero'}
    try: return num2words[n]
    except KeyError:
        try: return num2words[n-n%10]+num2words[n%10].lower()
        except KeyError: return 'Number out of range'

def add_diffByN(N):
    '''returns PDDL lines "(Difference by n1 n1) from 0 up to the nuber given to this function'''
    R=''
    for n in range(N):
        word=num2word(n)
        line=create_diffBy_list(n,word)
        R+='\n\t\t;Difference by {}:\n'.format(word)+line
    return R

def add_diffByN_hor_ver():
    R=''
    for diff in range(board_size):
        R+='\n\t\t;Diff by {}:\n'.format(num2word(diff))
        for n1 in range(board_size):
            for n2 in range(board_size):
                if abs((n1+1)-(n2+1))==diff:
                    R+='\t\t(diff_by_N n{} n{})\n'.format((n1+1),(n2+1))
    return R

def board():
    '''returns the chess board as A1 up to An in a square format'''
    R=''
    for rank in range(board_size):
        r=''
        for file in range(board_size):
            r+=chr(rank+1+64)+str(file+1)+' '
        R+='\t\t'+r+'\n'
    return R

#print(add_FEN_pos_to_PDDL('5/5/5/PPPPP/1N1N1')) #ToDo: may still be wrong but should be right
#print(add_diffByN_hor_ver())
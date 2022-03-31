from curses.ascii import isalpha, isdigit
import FEN
import numpy as np

board_size=5 #to change the board size

def add_FEN_pos_to_PDDL(fen,type='None'):
    '''returns the PDDL line format of the occupied board positions of a given FEN string'''
    done=[]
    R=''
    board=FEN.FEN_to_Chess_board(fen)
    for fig in ['p','n','b','r','q','k','P','N','B','R','Q','K']: #iterate over all figures to search for them in the board individually. we need to go trough once for every figure to get the counting right
        if fig in fen:
            idx=0 #index of the current figure 'fig'
            file=0 #current file
            #save=None 
            for col in zip(*board): #iterate over columns instead of rows of the board
                for rank in range(len(col)): #iterate over ranks
                    #TODO: if I want to allow multiple queens and kings of the same color
                    #try: 
                    #    vars(figures)[fig][idx]
                    #except: 
                    #    save=vars(figures)[fig][idx-1]
                    #    print('\t',save,)
                    if col[rank]==fig and vars(figures)[fig][idx] not in done:
                        figure=vars(figures)[fig][idx]
                        done.append(figure)
                        R+='\t\t(at '+figure+' n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                        idx+=1
                file+=1 #update file
    return R
class figures_plain:
    #black pieces:
    p='pawn_b'
    n='knight_b'
    b='bishop_b'
    r='rook_b'
    q='queen_b'
    k='king_b'
    #white pieces:
    P='pawn_w'
    N='knight_w'
    B='bishop_w'
    R='rook_w'
    Q='queen_w'
    K='king_w'
class figures:
    #black pieces:
    p=['pawn_b1','pawn_b2','pawn_b3','pawn_b4','pawn_b5','pawn_b6','pawn_b7','pawn_b8']
    n=['knight_b1','knight_b2']
    b=['bishop_b1','bishop_b2']
    r=['rook_b1','rook_b2']
    q=['queen_b1']
    k=['king_b1']
    #white pieces:
    P=['pawn_w1','pawn_w2','pawn_w3','pawn_w4','pawn_w5','pawn_w6','pawn_w7','pawn_w8']
    N=['knight_w1','knight_w2']
    B=['bishop_w1','bishop_w2']
    R=['rook_w1','rook_w2']
    Q=['queen_w1']
    K=['king_w1']

def add_double_pawn_moves():
    R=''
    R+='\n\t\t;Pawn double moves start for white:\n'
    R+=pawn_double('white')
    R+='\n\t\t;Pawn double moves start for black:\n'
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
                    prelude='plusOne'
                    R+='\t\t({} {}{} {}{})\n'.format(prelude,'n',from_rank+1,'n',to_rank+1)
            else: #black
                if to_rank-from_rank==-1:# and (from_rank+1)<board_size:
                    prelude='minusOne'
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
        diffBy_array.append((file+1,from_rank,file+1,to_rank))
    for t in diffBy_array:
        prelude='pawn_start_pos_{}'.format(type)
        R+='\t\t({} {}{} {}{})\n'.format(prelude,'n',t[0],'n',t[1])#,'n',t[2],'n',t[3])
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
        n+=1
        word=num2word(n)
        line=create_diffBy_list(n,word)
        R+='\n\t\t;Difference by {}:\n'.format(word)+line
    return R

def add_diffByN_hor_ver(N):
    R=''
    for diff in range(N):
        diff+=1
        R+='\n\t\t;Diff by {}:\n'.format(num2word(diff))
        for n1 in range(N):
            for n2 in range(N):
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

def add_color_predicates(start_FEN):
    R=''
    elements=[i for i in list(start_FEN) if i!='/' and not isdigit(i)]
    done=[]
    for e in elements:
        if e.isupper():
            for figure in vars(figures)[e]: #TODO: here I add all figures instead of only the ones I need. this is only a cosmetic issue I think
                if int(figure[-1:])<=board_size and figure not in done:
                    done.append(figure)
                    R+='\t\t(is_white {})\n'.format(figure)
        if e.islower():
            for figure in vars(figures)[e]:
                if int(figure[-1:])<=board_size and figure not in done:
                    done.append(figure)
                    R+='\t\t(is_black {})\n'.format(figure)
    return R

def add_piece_types(start_FEN):
    R=''
    done=[]
    elements=[i for i in list(start_FEN) if i!='/' and not isdigit(i)]
    for e in elements:
        for figure in vars(figures)[e]:
            if int(figure[-1:])<=board_size and figure not in done:
                done.append(figure)
                if 'bishop' in figure:
                    R+='\t\t(is_{} {})\n'.format('bishop',figure)
                else:
                    R+='\t\t(is_{} {})\n'.format(figure[:-3],figure)
    return R

def add_removed_pieces(start_FEN,goal_FEN):
    start_board=np.ndarray.flatten(np.array(FEN.FEN_to_Chess_board(start_FEN)))
    end_board=np.ndarray.flatten(np.array(FEN.FEN_to_Chess_board(goal_FEN)))
    R=''
    #print(start_board,'\n',end_board)
    done=[]
    diff=[]

    for i in range(len(start_board)):
        if start_board[i] not in end_board:
            diff.append(start_board[i])

    board=FEN.FEN_to_Chess_board(start_FEN)
    for fig in diff: #iterate over all figures to search for them in the board individually. we need to go trough once for every figure to get the counting right
        print(fig)
        idx=0 #index of the current figure 'fig'
        file=0 #current file
        for col in zip(*board): #iterate over columns instead of rows of the board
            for rank in range(len(col)): #iterate over ranks
                if col[rank]==fig and vars(figures_plain)[fig][idx] not in done:
                    print('>>  ',fig)
                    figure=vars(figures)[fig][idx]
                    done.append(figure)
                    R+='\t\t(removed {}{})\n'.format(vars(figures_plain)[fig],idx+1)
                    idx+=1
            file+=1 #update file

    return R

#print('this is solved in ~2secs:')
#print(add_FEN_pos_to_PDDL('5/1pppp/1R1N1/PPP2/5'))
#print(add_FEN_pos_to_PDDL('5/Ppp2/RPpN1/4p/5'))
#
#print('just one little change: the additional en-passant move: b3->c4: it runs forever')
#print(add_FEN_pos_to_PDDL('5/PpP2/R1pN1/4p/5'))

#start_FEN='5/5/Q4/R4/5'
#goal_FEN='5/5/R4/5/5'
#print(add_piece_types(start_FEN))
#print(add_removed_pieces(start_FEN,goal_FEN))

print(add_FEN_pos_to_PDDL('b4/b4/R4/5/5'))
print(add_removed_pieces('b4/b4/R4/5/5','R4/5/5/5/5'))
from curses.ascii import isdigit
from tracemalloc import start #TODO: replace with .isnumeric() to remove this line
import FEN

board_size=4 #to change the board size
extra_pieces=0 #to change the amount of extra pieces available for pawn promotion

def add_FEN_pos_to_PDDL(fen,type=None):
    '''returns the PDDL line format of the occupied board positions of a given FEN string'''
    done=[]
    F=[]
    R=''
    board=FEN.FEN_to_Chess_board(fen,board_size)
    for fig in ['p','n','b','r','q','k','P','N','B','R','Q','K',FEN.Filler]: #iterate over all figures to search for them in the board individually. we need to go trough once for every figure to get the counting right
        if fig in fen or fig==FEN.Filler:
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
                    if col[rank]==fig and col[rank]==FEN.Filler:
                        R+='\t\t(empty_square'+' n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                    elif col[rank]==fig and vars(figures)[fig][idx] not in done:
                        figure=vars(figures)[fig][idx]
                        done.append(figure)
                        F.append(figure)
                        R+='\t\t(at '+figure+' n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                        #R+='\t\t(occupied_square n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                        #if fig.lower() != 'p':
                        #    R+='\t\t(at '+figure+' n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                        #elif fig.lower() == 'p' and type=='goal':
                        #    if fig.isupper():
                        #        R+='\t\t(white_pawn_at'+' n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                        #    else:
                        #        R+='\t\t(black_pawn_at'+' n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                        #else:
                        #    R+='\t\t(at '+figure+' n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                        idx+=1
                file+=1 #update file
    if type =='remove':
        return F
    return R

def add_FEN_pos_to_PDDL_goal(fen,type=None):
    '''returns the PDDL line format of the occupied board positions of a given FEN string'''
    done=[]
    F=[]
    R=''
    board=FEN.FEN_to_Chess_board(fen,board_size)
    for fig in ['p','n','b','r','q','k','P','N','B','R','Q','K',FEN.Filler]: #iterate over all figures to search for them in the board individually. we need to go trough once for every figure to get the counting right
        if fig in fen or fig==FEN.Filler:
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
                    if col[rank]==fig and col[rank]==FEN.Filler:
                        R+='\t\t(empty_square'+' n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                    elif col[rank]==fig and vars(figures)[fig][idx] not in done:
                        figure=vars(figures)[fig][idx]
                        done.append(figure)
                        F.append(figure)
                        if fig.lower() == 'p':
                            if fig.isupper():
                                R+='\t\t(white_pawn_at'+' n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                            else:
                                R+='\t\t(black_pawn_at'+' n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                        elif fig.lower() == 'n':
                            if fig.isupper():
                                R+='\t\t(white_knight_at'+' n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                            else:
                                R+='\t\t(black_knight_at'+' n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                        elif fig.lower() == 'b':
                            if fig.isupper():
                                R+='\t\t(white_bishop_at'+' n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                            else:
                                R+='\t\t(black_bishop_at'+' n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                        elif fig.lower() == 'r':
                            if fig.isupper():
                                R+='\t\t(white_rook_at'+' n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                            else:
                                R+='\t\t(black_rook_at'+' n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                        elif fig.lower() == 'q':
                            if fig.isupper():
                                R+='\t\t(white_queen_at'+' n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                            else:
                                R+='\t\t(black_queen_at'+' n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                        elif fig.lower() == 'k':
                            if fig.isupper():
                                R+='\t\t(white_king_at'+' n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                            else:
                                R+='\t\t(black_king_at'+' n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                        #elif fig.lower() == 'k':
                        #    R+='\t\t(at '+figure+' n'+str((file+1))+' n'+str(board_size-rank)+')\n'
                        idx+=1
                file+=1 #update file
    if type =='remove':
        return F
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
    b=['b_bishop_b1','w_bishop_b2']
    r=['rook_b1','rook_b2']
    q=['queen_b1']
    k=['king_b1']
    #white pieces:
    P=['pawn_w1','pawn_w2','pawn_w3','pawn_w4','pawn_w5','pawn_w6','pawn_w7','pawn_w8']
    N=['knight_w1','knight_w2']
    B=['b_bishop_w1','w_bishop_w2']
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

def adjacent():
    R=''
    horiz_adj = []
    vert_adj = []
    diag_adj = []
    for r in range(board_size):
        for f in range(board_size):
            from_f = r+1
            from_r = f+1
            if ((from_f>=1) & (from_r<=board_size)): #left
                to_f = from_f-1
                to_r = from_r
                if (((to_f) <= board_size) & ((to_r) <= board_size) & ((to_f) >= 1) & ((to_r) >= 1)):
                    horiz_adj.append('\t\t(horiz_adj n{} n{} n{} n{})\n'.format(from_f,from_r, to_f,to_r))
            if ((from_f>=1) & (from_r<=board_size)): #right
                to_f = from_f+1
                to_r = from_r
                if (((to_f) <= board_size) & ((to_r) <= board_size) & ((to_f) >= 1) & ((to_r) >= 1)):
                    horiz_adj.append('\t\t(horiz_adj n{} n{} n{} n{})\n'.format(from_f,from_r, to_f,to_r))
    horiz_adj = set(horiz_adj)
    for r in range(board_size):
        for f in range(board_size):
            from_f = r+1
            from_r = f+1
            if ((from_f>=1) & (from_r<=board_size)): #up
                to_f = from_f
                to_r = from_r+1
                if (((to_f) <= board_size) & ((to_r) <= board_size) & ((to_f) >= 1) & ((to_r) >= 1)):
                    vert_adj.append('\t\t(vert_adj n{} n{} n{} n{})\n'.format(from_f,from_r, to_f,to_r))
            if ((from_f>=1) & (from_r<=board_size)): #down
                to_f = from_f
                to_r = from_r-1
                if (((to_f) <= board_size) & ((to_r) <= board_size) & ((to_f) >= 1) & ((to_r) >= 1)):
                    vert_adj.append('\t\t(vert_adj n{} n{} n{} n{})\n'.format(from_f,from_r, to_f,to_r))
    vert_adj = set(vert_adj)
    for r in range(board_size):
        for f in range(board_size):
            from_f = r+1
            from_r = f+1
            if ((from_f>=1) & (from_r<=board_size)): #left up
                to_f = from_f-1
                to_r = from_r+1
                if (((to_f) <= board_size) & ((to_r) <= board_size) & ((to_f) >= 1) & ((to_r) >= 1)):
                    diag_adj.append('\t\t(diag_adj n{} n{} n{} n{})\n'.format(from_f,from_r, to_f,to_r))
            if ((from_f>=1) & (from_r<=board_size)): #left down
                to_f = from_f-1
                to_r = from_r-1
                if (((to_f) <= board_size) & ((to_r) <= board_size) & ((to_f) >= 1) & ((to_r) >= 1)):
                    diag_adj.append('\t\t(diag_adj n{} n{} n{} n{})\n'.format(from_f,from_r, to_f,to_r))
            if ((from_f>=1) & (from_r<=board_size)): #right up
                to_f = from_f+1
                to_r = from_r+1
                if (((to_f) <= board_size) & ((to_r) <= board_size) & ((to_f) >= 1) & ((to_r) >= 1)):
                    diag_adj.append('\t\t(diag_adj n{} n{} n{} n{})\n'.format(from_f,from_r, to_f,to_r))
            if ((from_f>=1) & (from_r<=board_size)): #right down
                to_f = from_f+1
                to_r = from_r-1
                if (((to_f) <= board_size) & ((to_r) <= board_size) & ((to_f) >= 1) & ((to_r) >= 1)):
                    diag_adj.append('\t\t(diag_adj n{} n{} n{} n{})\n'.format(from_f,from_r, to_f,to_r))
    diag_adj = set(diag_adj)

    for i in horiz_adj:
        R+=i
    for i in vert_adj:
        R+=i
    for i in diag_adj:
        R+=i

    return R

def between():
    R=''
    between=[]
    for n1 in range(1,board_size+1):
        for n2 in range(1,board_size+1):
            for i in range(1,board_size+1):
                if ((n1<i) & (n2>i)):
                    between.append('\t\t(between n{} n{} n{})\n'.format(n1,i,n2))
                if ((n2<i) & (n1>i)):
                    between.append('\t\t(between n{} n{} n{})\n'.format(n1,i,n2))
    between = set(between)

    for i in between:
        R+=i
    return R

def same_diag():
    R=''
    same_diag=[]
    for r in range(board_size):
        for f in range(board_size):
            for i in range(1,board_size+1): #for all destination files (board size max)
                from_f = r+1
                from_r = f+1
                if ((from_f>=1) & (from_r<=board_size)): #left up
                    to_f = from_f-i
                    to_r = from_r+i
                    if (((to_f) <= board_size) & ((to_r) <= board_size) & ((to_f) >= 1) & ((to_r) >= 1)):
                        same_diag.append('\t\t(same_diag n{} n{} n{} n{})\n'.format(from_f,from_r, to_f,to_r))
                if ((from_f>=1) & (from_r<=board_size)): #left down
                    to_f = from_f-i
                    to_r = from_r-i
                    if (((to_f) <= board_size) & ((to_r) <= board_size) & ((to_f) >= 1) & ((to_r) >= 1)):
                        same_diag.append('\t\t(same_diag n{} n{} n{} n{})\n'.format(from_f,from_r, to_f,to_r))
                if ((from_f>=1) & (from_r<=board_size)): #right up
                    to_f = from_f+i
                    to_r = from_r+i
                    if (((to_f) <= board_size) & ((to_r) <= board_size) & ((to_f) >= 1) & ((to_r) >= 1)):
                        same_diag.append('\t\t(same_diag n{} n{} n{} n{})\n'.format(from_f,from_r, to_f,to_r))
                if ((from_f>=1) & (from_r<=board_size)): #right down
                    to_f = from_f+i
                    to_r = from_r-i
                    if (((to_f) <= board_size) & ((to_r) <= board_size) & ((to_f) >= 1) & ((to_r) >= 1)):
                        same_diag.append('\t\t(same_diag n{} n{} n{} n{})\n'.format(from_f,from_r, to_f,to_r))
    same_diag = set(same_diag)

    for i in same_diag:
        R+=i
    #print(R)
    #exit()

    return R


def pawn_double(type):
    '''returns the PDDL line format of type={'white', 'black'} for pawn double moves'''
    R=''
    diffBy_array=[]
    to_rank=4
    from_rank=2
    if type=='black': 
        to_rank=board_size-3; 
        from_rank=board_size-1
    for file in range(board_size):
        diffBy_array.append((file+1,from_rank,file+1,to_rank))
    for t in diffBy_array:
        prelude='pawn_start_pos_{}'.format(type)
        R+='\t\t({} {}{} {}{})\n'.format(prelude,'n',t[0],'n',t[1])#,'n',t[2],'n',t[3])
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

def add_last_pawn_line():
    R=''
    for i in range(board_size): #black pawns
        R+='\t\t(last_pawn_line n{} n1)\n'.format(i+1)
    for i in range(board_size): #black pawns
        R+='\t\t(last_pawn_line n{} n{})\n'.format(i+1,board_size)
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
    for n in range(N+1):
        n+=1
        word=num2word(n)
        line=create_diffBy_list(n,word)
        R+='\n\t\t;Difference by {}:\n'.format(word)+line

    return R

def add_not_same(N):
    R=''
    for n in range(N):
        n+=1
        line=create_diffBy_list(n,'N')
        R+=line
    #print(R)
    #exit()
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

def add_color_predicates(start_FEN,goal_FEN):
    R=''
    objs=sorted(get_individual_objects(start_FEN,goal_FEN))
    elements=sorted([(i,objs.count(i)) for i in objs if i!='/' and not isdigit(i)])
    done=[]
    for e in elements:
        prefix='is_white'
        if e[0].islower():
            prefix='is_black'
        Figures=vars(figures)[e[0]]
        if len(vars(figures)[e[0]])>e[1]:
            for i in range(len(vars(figures)[e[0]])-e[1]):
                Figures.pop()
        elif len(vars(figures)[e[0]])<e[1]:
            root=vars(figures)[e[0]][-1][:-1]
            length=len(vars(figures)[e[0]])
            for i in range(e[1]-len(vars(figures)[e[0]])):
                Figures.append(root+str(i+1+length))
        for figure in Figures:
            if int(figure[-1:])<=e[1] and figure not in done:
                done.append(figure)
                R+='\t\t({} {})\n'.format(prefix,figure)
    return R

def add_piece_types(start_FEN,goal_FEN):
    R=''
    done=[]
    objs=sorted(get_individual_objects(start_FEN,goal_FEN))
    elements=sorted([(i,objs.count(i)) for i in objs if i!='/' and not isdigit(i)])
    for e in elements:
        Figures=vars(figures)[e[0]]
        if len(vars(figures)[e[0]])>e[1]:
            for i in range(len(vars(figures)[e[0]])-e[1]):
                Figures.pop()
        elif len(vars(figures)[e[0]])<e[1]:
            root=vars(figures)[e[0]][-1][:-1]
            length=len(vars(figures)[e[0]])
            for i in range(e[1]-len(vars(figures)[e[0]])):
                Figures.append(root+str(i+1+length))
        for figure in Figures:
            if int(figure[-1:])<=e[1] and figure not in done:
                done.append(figure)
                if 'bishop' in figure:
                    R+='\t\t(is_{} {})\n'.format('bishop',figure)
                else:
                    f=figure[:-3]
                    if figure[:-3][-1]=='_':
                        f=figure[:-4]
                    R+='\t\t(is_{} {})\n'.format(f,figure)            
    return R

def add_removed_pieces(start_FEN,goal_FEN):
    '''
    this checks the difference between start and end pos fen and returns the removed pieces as predicates with their repective numbers. Now there is a problem: if we have two rooks in the starting position and only one in the ending position, we don't know which rook of the two has been captured. With the rooks this is not a problem because they can reach any file anyways so they can just exchange spots, but with pawns and with bishops this is a problem. I don't know yet how to solve it with the pawns so I'll just nuber them in the same way as I do number the original pawns which leads to some problems. With the bishops I do know which one has been captured since they have a fixed square color asigned to them.
    '''
    start=add_FEN_pos_to_PDDL(start_FEN,'remove')
    goal=add_FEN_pos_to_PDDL(goal_FEN,'remove')
    R=''
    for row in range(len(start)):
        if start[row] not in goal and start[row][0].lower() != 'p': #TODO: see todo in 
            R+='\t\t(removed {})\n'.format(start[row])
    return R

def get_individual_objects(start_FEN,goal_FEN):
    '''this function looks at how many distinct objects are present within the whole statespace and also adds n=extra_pieces additional pieces for pawn promotions'''
    s=[e for e in list(start_FEN) if e!='/' and not e.isdigit()]
    g=[e for e in list(goal_FEN) if e!='/' and not e.isdigit()]
    r=[]
    for fig in  ['p','n','b','r','q','k','P','N','B','R','Q','K']:
        if s.count(fig)>=g.count(fig):
            for i in range(s.count(fig)):
                r.append(fig)
        if s.count(fig)<g.count(fig):
            for i in range(g.count(fig)):
                r.append(fig)
        if fig not in ['k','K','p','P']: #add pawn promotion pieces
            for i in range(extra_pieces): 
                r.append(fig)
    return r

def add_objects(start_FEN,goal_FEN=None):
    R=''
    l=None
    if goal_FEN==None:
        l=[]
        goal_FEN=start_FEN
    done=[]
    done2=[]
    objs=sorted(get_individual_objects(start_FEN,goal_FEN))
    elements=sorted([(i,objs.count(i)) for i in objs if i!='/' and not isdigit(i)])
    for e in elements:
        Figures=vars(figures)[e[0]]
        if len(vars(figures)[e[0]])>e[1]:
            for i in range(len(vars(figures)[e[0]])-e[1]):
                Figures.pop()
        elif len(vars(figures)[e[0]])<e[1]:
            root=vars(figures)[e[0]][-1][:-1]
            length=len(vars(figures)[e[0]])
            for i in range(e[1]-len(vars(figures)[e[0]])):
                Figures.append(root+str(i+1+length))
        r='\t\t'
        for figure in Figures:
            f=figure[:-1]
            if figure[:-1][-1]=='_':
                f=figure[:-2]
            if 'bishop' in figure:
                f=f[2:]
            r+=' {}'.format(figure)
            if int(figure[-1:])<=e[1] and figure not in done:
                done.append(figure)
        if l==None:
            line=''.join([i for i in r])+' - '+f[:-2]+'\n'
            if line not in done2:
                R+=''.join([i for i in r])+' - '+f[:-2]+'\n'
                done2.append(line)
        else:
            l.append(r.split())
    if l!=None:
        return l
    return R

def add_objects_old(start_FEN,goal_FEN=None):
    R=''
    l=None
    if goal_FEN==None:
        l=[]
        goal_FEN=start_FEN
    done=[]
    done2=[]
    objs=sorted(get_individual_objects(start_FEN,goal_FEN))
    elements=sorted([(i,objs.count(i)) for i in objs if i!='/' and not isdigit(i)])
    for e in elements:
        Figures=vars(figures)[e[0]]
        if len(vars(figures)[e[0]])>e[1]:
            for i in range(len(vars(figures)[e[0]])-e[1]):
                Figures.pop()
        elif len(vars(figures)[e[0]])<e[1]:
            root=vars(figures)[e[0]][-1][:-1]
            length=len(vars(figures)[e[0]])
            for i in range(e[1]-len(vars(figures)[e[0]])):
                Figures.append(root+str(i+1+length))
        r='\t\t'
        for figure in Figures:
            f=figure[:-1]
            if figure[:-1][-1]=='_':
                f=figure[:-2]
            if 'bishop' in figure:
                f=f[2:]
            r+=' {}'.format(figure)
            if int(figure[-1:])<=e[1] and figure not in done:
                done.append(figure)
        if l==None:
            line=''.join([i for i in r])+' - '+f+'\n'
            if line not in done2:
                R+=''.join([i for i in r])+' - '+f+'\n'
                done2.append(line)
        else:
            l.append(r.split())
    if l!=None:
        return l
    return R

def add_castling(FEN):
    R=''; b=''; w=''
    black=FEN.split('/')[0]
    white=FEN.split('/')[-1]
    for i in range(len(black)):
        if not black[i].isdigit():
            b+=black[i]
    for i in range(len(white)):
        if not white[i].isdigit():
            w+=white[i]
    if b=='rkr': #TODO: add other combinations as well where only queenside rook or kingside rook but currently the rooks are not assigned correctly anyways so its of no use right now.
        R+='\t\t(not_moved king_b1)\n\t\t(not_moved rook_b1)\n\t\t(not_moved rook_b2)\n\t\t(kingside_rook rook_b2)\n\t\t(queenside_rook rook_b1)'
    if w=='RKR':
        R+='\n\t\t(not_moved king_w1)\n\t\t(not_moved rook_w1)\n\t\t(not_moved rook_w2)\n\t\t(kingside_rook rook_w2)\n\t\t(queenside_rook rook_w1)'
    return R

def add_locations():
    R='\t\t'
    for i in range(board_size):
        R+='n'+str(i+1)+' '
    R+=' - location\n'
    return R

def add_turn(turn=None):
    if turn.lower()=='b' or turn.lower()=='black':
        return ''
    else:
        return '\t\t(white_s_turn)'

def add_figures_on_board(FEN):
    l=add_objects(FEN)
    L=[]
    R=''
    for i in l:
        for j in range(len(i)):
            L.append(i[j])
    L=set(L)
    for i in L:
        R+='\t\t(is_on_board {})\n'.format(i)
    return R

def add_same_color(sFEN,gFEN):
    l=add_color_predicates(sFEN,gFEN)
    l=l.split("\n")
    l=l[:-1]
    pairs=[]
    for i in l:
        pairs.append((i.split()[0][4:],i.split()[1][:-1]))
    pairs=sorted(pairs, key=lambda tup: tup[1])
    blackPieces=[]
    whitePieces=[]
    for i in pairs:
        if i[0]=='white':
            whitePieces.append(i)
        else:
            blackPieces.append(i)
    R=''
    for a in blackPieces:
        for b in blackPieces:
            R+='\t\t(same_color {} {})\n'.format(a[1],b[1])
    for a in whitePieces:
        for b in whitePieces:
            R+='\t\t(same_color {} {})\n'.format(a[1],b[1])
    for a in whitePieces:
        for b in blackPieces:
            R+='\t\t(not_same_color {} {})\n'.format(a[1],b[1])
    for a in blackPieces:
        for b in whitePieces:
            R+='\t\t(not_same_color {} {})\n'.format(a[1],b[1])
    return R

#start_FEN='PPPPP/5/5/5/3bb'
#goal_FEN ='b4/4P/5/5/5'
#start=FEN.add_coordinate_System(FEN.printable_board(FEN.FEN_to_Chess_board(start_FEN),True,True))
#goal=FEN.add_coordinate_System(FEN.printable_board(FEN.FEN_to_Chess_board(goal_FEN),True,True))
#FEN.print_neighbor(start,goal)
#print(add_FEN_pos_to_PDDL(start_FEN))
#print(add_FEN_pos_to_PDDL(goal_FEN))
#print(add_removed_pieces(start_FEN,goal_FEN))

#print(add_FEN_pos_to_PDDL('PPPPP/5/5/5/3bb'))

#print(add_color_predicates('PPPPP/PPPPP/PPPPP/PPPPP/PPPPP','QQK2/KKKKK/Q4/5/5'))
#print(add_piece_types('PPPPP/PPPPP/PPPPP/PPPPP/PPPPP','QQK2/KKKKK/Q4/5/5'))
#print(add_objects('5/5/K4/5/pQq2','5/5/PPPPP/ppppp/pKk2'))

#start_FEN=start_FEN='1K3/5/5/5/r4'#'2p2/3pK/5/5/5'   #'1r3/2r2/3K1/5/5' -->same situation with bishops is much faster:'b4/1b3/2K2/5/5'
#goal_FEN ='K3/5/5/5/r4'
#print(add_objects(start_FEN,goal_FEN))

#print(add_figures_on_board('K3/5/r4/5/r4'))
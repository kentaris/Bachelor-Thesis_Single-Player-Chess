class valid_moves: 
    def __init__(self):
        self.Pawn_moves=(8*2)+(8*5*1)
        self.Pawn_takes=(2*6*1)+(6*6*2)
        self.Pawn=(2*3)+(2*5*2)+(6*4)+(6*5*3)
        self.Knight=(4*2)+(4*2*3)+(4*4*4)+(4*4)+(4*4*6)+(4*4*8)
        self.Bishop=(4*(8-1)*7)+(4*(6-1)*9)+(4*(4-1)*11)+(4*(2-1)*13)
        self.w_Bishop=(14*7)+(10*9)+(6*11)+(2*13)
        self.b_Bishop=(14*7)+(10*9)+(6*11)+(2*13)
        self.Rook=64*(7+7)
        self.Queen=self.Bishop+self.Rook
        self.King=(4*3)+(4*6*5)+(6*6*8)

def test(List, Type):
    print('\033[2m > {}/{} [{} Test Passed: \033[93m{}\033[0m\033[2m]\033[0m'.format(\
        len(List),\
        getattr(valid_moves,Type),\
        Type,\
        getattr(valid_moves,Type)==len(List)))

def printable_list(List,prefix,indent=''): 
    List=sorted(list(List))
    printable_list=''
    for i in List:
        From=str(chr(i[0][0]+64))+str(i[0][1])
        To=str(chr(i[1][0]+64))+str(i[1][1])
        printable_list+=indent+'({} {} {})\n'.format(prefix,From,To)
    return printable_list

def inside_board(From,To): 
    if (((From[0]>0 and From[0]<9) and (To[0]>0 and To[0]<9)) and \
        ((From[1]>0 and From[1]<9) and (To[1]>0 and To[1]<9))):
        return True
    return False

def pawn(color,Type):
    List=[]
    for number in range(8):
        for letter in range(8):
            rank=number+1
            file=letter+1
            if color[0]=='w':
                if rank==1: #skip first rank
                    continue
                From=(file,rank)
                To_move=   [(file  ,rank+1)]
                if rank==2: #starting position (double move possible)
                    To_move.append((file,rank+2))
                To_capture=[(file+1,rank+1),\
                            (file-1,rank+1)
                ]
            else: #black
                if rank==8: #skip last rank
                    continue
                From=(file,rank)
                To_move=   [(file  ,rank-1)]
                if rank==7: #starting position 
                    To_move.append((file,rank-2))
                To_capture=[(file+1,rank-1),\
                            (file-1,rank-1)]
            
            if Type[0]=='a': #all moves
                To=To_move+To_capture
            elif Type[0]=='c': #capture moves only
                To=To_capture
            else: #normal moves only
                To=To_move

            for to in To:
                if inside_board(From, to):
                    List.append((From, to))
    List=set(List)
    return List

def knight():
    List=[]
    for number in range(8):
        for letter in range(8):
            From=(letter+1,number+1)
            To=[((letter+1)-1,(number+1)+2),\
                ((letter+1)+1,(number+1)+2),  \
                ((letter+1)-1,(number+1)-2),  \
                ((letter+1)+1,(number+1)-2),\
                ((letter+1)-2,(number+1)+1),\
                ((letter+1)-2,(number+1)-1),\
                ((letter+1)+2,(number+1)+1),\
                ((letter+1)+2,(number+1)-1)]
            for i in range(len(To)):
                if inside_board(From, To[i]):
                    List.append((From,To[i]))
    List=set(List)
    return List

def bishop(color=None): 
    List=[]
    name='Bishop'
    if color:
        name=str(color[0]+'_'+name)
    valid_bishop_moves=getattr(valid_moves,name)
    for number in range(8):
        for letter in range(8):
            if color=='white':
                valid_bishop_moves=valid_moves.w_Bishop
                if (letter+1)%2==0:
                    continue
            elif color=='black':
                valid_bishop_moves=valid_moves.b_Bishop
                if (letter+1)%2==1:
                    continue
            else:
                valid_bishop_moves=valid_moves.Bishop
            From=(letter+1,number+1)
            To=[((letter+1)+1,(number+1)+1),\
                ((letter+1)+2,(number+1)+2),\
                ((letter+1)+3,(number+1)+3),\
                ((letter+1)+4,(number+1)+4),\
                ((letter+1)+5,(number+1)+5),\
                ((letter+1)+6,(number+1)+6),\
                ((letter+1)+7,(number+1)+7),\

                ((letter+1)-1,(number+1)+1),\
                ((letter+1)-2,(number+1)+2),\
                ((letter+1)-3,(number+1)+3),\
                ((letter+1)-4,(number+1)+4),\
                ((letter+1)-5,(number+1)+5),\
                ((letter+1)-6,(number+1)+6),\
                ((letter+1)-7,(number+1)+7),\

                ((letter+1)+1,(number+1)-1),\
                ((letter+1)+2,(number+1)-2),\
                ((letter+1)+3,(number+1)-3),\
                ((letter+1)+4,(number+1)-4),\
                ((letter+1)+5,(number+1)-5),\
                ((letter+1)+6,(number+1)-6),\
                ((letter+1)+7,(number+1)-7),\

                ((letter+1)-1,(number+1)-1),\
                ((letter+1)-2,(number+1)-2),\
                ((letter+1)-3,(number+1)-3),\
                ((letter+1)-4,(number+1)-4),\
                ((letter+1)-5,(number+1)-5),\
                ((letter+1)-6,(number+1)-6),\
                ((letter+1)-7,(number+1)-7)]
            for i in range(len(To)):
                if inside_board(From, To[i]):
                    List.append((From,To[i]))
    
    List=set(List)
    return List

def rook(): 
    List=[]
    for number in range(8):
        for letter in range(8):
            From=(letter+1,number+1)
            To=[((letter+1),(number+1)+1),\
                ((letter+1),(number+1)+2),\
                ((letter+1),(number+1)+3),\
                ((letter+1),(number+1)+4),\
                ((letter+1),(number+1)+5),\
                ((letter+1),(number+1)+6),\
                ((letter+1),(number+1)+7),\

                ((letter+1)+1,(number+1)),\
                ((letter+1)+2,(number+1)),\
                ((letter+1)+3,(number+1)),\
                ((letter+1)+4,(number+1)),\
                ((letter+1)+5,(number+1)),\
                ((letter+1)+6,(number+1)),\
                ((letter+1)+7,(number+1)),\

                ((letter+1),(number+1)-1),\
                ((letter+1),(number+1)-2),\
                ((letter+1),(number+1)-3),\
                ((letter+1),(number+1)-4),\
                ((letter+1),(number+1)-5),\
                ((letter+1),(number+1)-6),\
                ((letter+1),(number+1)-7),\

                ((letter+1)-1,(number+1)),\
                ((letter+1)-2,(number+1)),\
                ((letter+1)-3,(number+1)),\
                ((letter+1)-4,(number+1)),\
                ((letter+1)-5,(number+1)),\
                ((letter+1)-6,(number+1)),\
                ((letter+1)-7,(number+1))]
            for i in range(len(To)):
                if inside_board(From, To[i]):
                    List.append((From,To[i]))

    List=set(List)
    return List

def queen():
    List=list(rook())+list(bishop())
    List=set(List)
    return List

def king(): 
    List=[]
    for number in range(8):
        for letter in range(8):
            From=(letter+1,number+1)
            To=[((letter+1)  ,(number+1)+1),\
                ((letter+1)-1,(number+1)),  \
                ((letter+1)+1,(number+1)),  \
                ((letter+1)  ,(number+1)-1),\
                ((letter+1)-1,(number+1)+1),\
                ((letter+1)+1,(number+1)+1),\
                ((letter+1)-1,(number+1)-1),\
                ((letter+1)+1,(number+1)-1)]
            for i in range(len(To)):
                if inside_board(From, To[i]):
                    List.append((From,To[i]))

    List=set(List)
    return List

def board():
    List=[]
    for number in range(8):
        row=[]
        for letter in range(8):
            row.append((letter+1,number+1))
        List.append(row)
    return List

def printable_board(List,indent=''):
    board=''
    for row in List:
        R=''
        for e in row:
            R=R+str(chr(e[0]+64))+str(e[1])+' '
        board+=indent+R+'\n'
    board=board[:-2]
    return(board)

def matrix_form_board(List):
    board=[]
    for row in List:
        R=[]
        for e in row:
            R.append(str(chr(e[0]+64))+str(e[1]))
        board.append(R)
    return(board)

def printable_board_vertical(List, prefix,indent=''):
    board=''
    for row in List:
        R=''
        for e in row:
            R=R+indent+'('+prefix+' '+str(chr(e[0]+64))+str(e[1])+')\n'
        board+=R
    return board[:-1]

def remove_reversable_moves(List):
    remove_reverse_moves=[]
    for pair in List:
        if (pair[1],pair[0]) not in remove_reverse_moves:
            remove_reverse_moves.append(pair)
            print((pair[1],pair[0]))
    return len(remove_reverse_moves)

def main(): 
    print('Options:    | valid moves:\n------------|-------------\n1. Pawn\t    | \t   {}+{}={}\n2. Knight   | \t   {}\n3. w_Bishop | \t   {}\n4. b_Bishop | \t   {}\n5. rook\t    | \t   {}\n6. queen    | \t   {}\n7. king\t    | \t   {}\n------------|-------------\n'.format(valid_moves.Pawn_moves,valid_moves.Pawn_moves,valid_moves.Pawn,valid_moves.Knight,valid_moves.w_Bishop,valid_moves.b_Bishop,valid_moves.Rook,valid_moves.Queen,valid_moves.King))
    Input=input('type a number (1-6):\n > ')
    prefix='valid move'
    while True:      
        if Input=='1':
            Input2=input('\n   Options:\n1. white\'s Pawns\n2. black\'s Pawns\n > ')
            Input3=input('\n   Options:\n1. all Pawn moves\n2. Pawn capture moves\n3. Pawn moves without captures\n > ')
            name='Pawn'
            if Input2=='1': #white
                if Input3=='1':
                    List=pawn('w','all')
                elif Input3=='2':
                    List=pawn('w','capture moves')
                    name='Pawn_takes'
                else:
                    List=pawn('w','no capture moves')
                    name='Pawn_moves'
            else: #black
                if Input3=='1':
                    List=pawn('b','all')
                    print(printable_list(List,prefix))
                elif Input3=='2':
                    List=pawn('b','capture moves')
                    name='Pawn_takes'
                else:
                    List=pawn('b','no capture moves')
                    name='Pawn_moves'
            print(printable_list(List,prefix))
            test(List,name)
        elif Input=='2':
            List=knight()
            print(printable_list(List,prefix))
            test(List,'Knight')
        elif Input=='3':
            List=bishop('white')
            print(printable_list(List,prefix))
            test(List,'w_Bishop')
        elif Input=='4':
            List=bishop('black')
            print(printable_list(List,prefix))
            test(List,'b_Bishop')
        elif Input=='5':
            List=rook()
            print(printable_list(List,prefix))
            test(List,'Rook')
        elif Input=='6':
            List=queen()
            print(printable_list(List,prefix))
            test(List,'Queen')
        elif Input=='7':
            List=king()
            print(printable_list(List,prefix))
            test(List,'King')
        elif Input.lower()=='q' or Input.lower()=='quit' or Input.lower()=='e' or Input.lower()=='exit':
            print('\n\u001b[32m > sucessfully quit the program!\033[0m\n')
            break
        elif Input.lower()=='bishop':
            List=bishop()
            print(printable_list(List,prefix))
            test(List,'Bishop')
        elif Input.lower()=='board' or Input.lower()=='b' or Input.lower()=='boardv' or Input.lower()=='bv':
            if Input[-1]=='v':
                print(printable_board_vertical(board(),'visited'))
            else:
                print(printable_board(board()))
        elif Input.lower()=='-h':
            print('\033[2m\nHelp:\n- \'e\',\'exit\',\'q\',\'quit\' (exit)\n- \'bishop\' (print b&w Bishop valid moves)\n- \'b\',\'board\' (print the chess board > if \'v\' appended: vertical print)\033[0m')
        else:
            print('\033[93mNumber not recognized, try again...\033[0m')
        print('\n > Options: [1:Pawn, 2:Knight, 3:w_Bishop, 3:b_Bishop, 5:Rook, 6:Queen, 7:King] (\'-h\' for more functions)')
        Input=input('type a number (1-7): ')

if __name__ == "__main__":
    valid_moves = valid_moves()
    main()
else:
    valid_moves = valid_moves()
import valid_moves as VMG
import FEN

def replace(txt_file,old_content,new_content):
    """takes f.readlines() (1D array of strings) as input and replaces the given 'old_content' string with the 'new_content' string"""
    index=txt_file.index(old_content)
    txt_file.pop(index)
    txt_file.insert(index, new_content)
    return txt_file

def write_pddl(txt_file,Type):
    """writes the given 1D array of strings to a .pddl file in the output folder"""
    with open( 'Load_into_PDDL/Output_PDDL_Files/chess_{}.pddl'.format(Type), mode='w') as f:
        for line in txt_file:
            f.write("".join(line))
    f.close()

def load_file(Type,starting_position=None,goal_position=None):
    """loads the .pddl file in the template folder and changes it's content accordingly using the replace() function"""
    with open('Load_into_PDDL/template_PDDL_Files/chess_{}.pddl'.format(Type), mode='r') as f:
        txt_file = f.readlines()
    f.close()
    if Type=='problem':
        #objects:
        txt_file=replace(txt_file,';[-:objects: board square names-]\n',VMG.printable_board(VMG.board(),'        '))
        txt_file=replace(txt_file,';[-:objects: figures-]\n',FEN.printable_list_of_allFigures(FEN.all_figures(),'        '))

        starting_position_board=FEN.FEN_to_Chess_board(starting_position)
        starting_position_list=FEN.printable_true_board_squares(FEN.true_board_squares(starting_position_board),'at','        ')
        txt_file=replace(txt_file,';[-:init: board starting position-]\n',starting_position_list)

        #initialize:
        #txt_file=replace(txt_file,';[-:init: valid moves (black pawns)]\n',VMG.printable_list(VMG.pawn('black','all'),'valid_move','        '))
        #txt_file=replace(txt_file,';[-:init: valid moves (white pawns)]\n',VMG.printable_list(VMG.pawn('white','all'),'valid_move','        '))
        txt_file=replace(txt_file,';[-:init: valid moves (knight)]\n',VMG.printable_list(VMG.knight(),'valid_move','        '))
        #txt_file=replace(txt_file,';[-:init: valid moves (b_bishop)]\n',VMG.printable_list(VMG.bishop('black'),'valid_move','        '))
        #txt_file=replace(txt_file,';[-:init: valid moves (w_bishop)]\n',VMG.printable_list(VMG.bishop('white'),'valid_move','        '))
        #txt_file=replace(txt_file,';[-:init: valid moves (rook)]\n',VMG.printable_list(VMG.rook(),'valid_move','        '))
        #txt_file=replace(txt_file,';[-:init: valid moves (queen)]\n',VMG.printable_list(VMG.queen(),'valid_move','        '))
        #txt_file=replace(txt_file,';[-:init: valid moves (king)]\n',VMG.printable_list(VMG.king(),'valid_move','        '))

        #goal:
        ending_position_board=FEN.FEN_to_Chess_board(goal_position) #one move: white pawn B2 > B3
        ending_position_list=FEN.printable_true_board_squares(FEN.true_board_squares(ending_position_board),'at','               ')
        txt_file=replace(txt_file,';[-:goal-]\n',ending_position_list)
    else:
        pass
    write_pddl(txt_file,Type)

def main():
    starting_position='rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1'
    board1=FEN.add_coordinate_System(FEN.printable_board(FEN.FEN_to_Chess_board(starting_position),True,True))

    #goal_position='rnbqkbnr/pppppppp/8/8/8/1P6/P1PPPPPP/RNBQKBNR w KQkq - 0 1' #one move: b3
    goal_position='8/7R/5q1k/3Q2N1/3p4/PP3pPP/5n1K/4R3 w - - 0 1' #Deep Blue–Kasparov, 1996, rd 1 (Kasparov mated by white)

    board2=FEN.add_coordinate_System(FEN.printable_board(FEN.FEN_to_Chess_board(goal_position),True,True))
    
    FEN.print_neighbor(board1,board2)

    load_file('problem',starting_position,goal_position)
    load_file('domain')

if __name__ == "__main__":
    main()
else:
    exit()
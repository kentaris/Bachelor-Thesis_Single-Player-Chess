import os
import population_generator as PG
import FEN

def replace(txt_file,old_content,new_content):
    """takes f.readlines() (1D array of strings) as input and replaces the given 'old_content' string with the 'new_content' string"""
    index=txt_file.index(old_content)
    txt_file.pop(index)
    txt_file.insert(index, new_content)
    return txt_file

def write_pddl(txt_file,Type):
    """writes the given 1D array of strings to a .pddl file in the output folder"""
    files=os.listdir('template/')
    for f in files:
        if Type.lower() in f.lower():
            name=f
    with open('output/{}'.format(name), mode='w') as f:
        for line in txt_file:
            f.write("".join(line))
    f.close()

def load_file(Type):
    """loads the .pddl file in the template folder and changes it's content accordingly using the replace() function"""
    files=os.listdir('template/')
    for f in files:
        if Type.lower() in f.lower():
            name=f
    with open('template/{}'.format(name), mode='r') as f:
        txt_file = f.readlines()
    f.close()
    #Problem File content:
    if Type.lower()=='problem':
        #init:
        start_FEN='5/5/5/PPPPP/1NKN1'
        goal_FEN='2P2/5/1PNP1/P2NP/4K'
        txt_file=replace(txt_file,';[:init_start_state]\n',PG.add_FEN_pos_to_PDDL(start_FEN))
        txt_file=replace(txt_file,';[:init_diffByN]\n',PG.add_diffByN(3))
        txt_file=replace(txt_file,';[:init_pawn_double_move]\n',PG.add_double_pawn_moves())
        txt_file=replace(txt_file,';[:init_plusOne]\n',PG.add_one_forward())

        #goal:
        #txt_file=replace(txt_file,';[:goal_position]\n',PG.add_FEN_pos_to_PDDL('2P2/5/1PNP1/P2NP/5')) #TODO: this does work only limitedly: I canot assign right numbers to pieces so let's do this by hand right now

        #Visualize :init & :goal pos
        s=FEN.add_coordinate_System(FEN.printable_board(FEN.FEN_to_Chess_board(start_FEN),True,True))
        g=FEN.add_coordinate_System(FEN.printable_board(FEN.FEN_to_Chess_board(goal_FEN),True,True))
        FEN.print_neighbor(s,g)
    else:
        pass #for now we have nothing to change in the domain file so we just skip resulting in a copy paste in the next line
    write_pddl(txt_file,Type)

def main():
    load_file('problem')
    load_file('domain')

if __name__ == "__main__":
    main()
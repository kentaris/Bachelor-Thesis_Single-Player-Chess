import sys
import os
#sys.path.insert(1, '../First attempt')
#import valid_moves as VMG
import FEN
import population_generator as PG

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
        txt_file=replace(txt_file,';[start_state]\n',PG.FEN_pos_to_PDDL('5/5/5/PPPPP/1N1N1'))
        #txt_file=replace(txt_file,';[:objects_board_sqares]\n',PG.board())
        #txt_file=replace(txt_file,';[:objects_figures]\n',FEN.printable_list_of_allFigures(FEN.all_figures(),'        '))
        #goal:
        txt_file=replace(txt_file,';[:goal_position]\n',PG.FEN_pos_to_PDDL('2P2/5/1PNP1/P2NP/5'))
        txt_file=replace(txt_file,';[:init_diffByZeroOneTwo]\n',PG.diffByZeroOneTwo())
    #Domain File content:
    else:
        pass
    write_pddl(txt_file,Type)

def main():
    load_file('problem')
    load_file('domain')

if __name__ == "__main__":
    main()
else:
    exit()
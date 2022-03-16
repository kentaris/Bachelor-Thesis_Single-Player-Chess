import os
import population_generator as PG
import FEN
from subprocess import call
import subprocess

def replace(txt_file,old_content,new_content):
    """takes f.readlines() (1D array of strings) as input and replaces the given 'old_content' string with the 'new_content' string"""
    index=txt_file.index(old_content)
    txt_file.pop(index)
    txt_file.insert(index, new_content)
    return txt_file

def write_pddl(txt_file,Type):
    """writes the given 1D array of strings to a .pddl file in the output folder"""
    files=os.listdir('template/')
    for file in files:
        if Type.lower() in file.lower():
            name=file
    with open('output/{}'.format(name), mode='w') as f:
        for line in txt_file:
            f.write("".join(line))
    f.close()
    with open('../../../downward/{}'.format(name), mode='w') as f:
        for line in txt_file:
            f.write("".join(line))
    f.close()

def execute_planner():
    '''executes the pddl planner which Augusto sent me'''
    print('\n\n >>> executing planner')
    call('./fast-downward.py chess-domain.pddl chess-problem.pddl --search "eager_greedy([ff])"',cwd="../../../downward/",shell=True)
    files=os.listdir('../../../downward/')
    if 'sas_plan' in files:
        call('mv sas_plan /home/ken/Documents/Bachelor-Thesis_Single-Player-Chess/Stages/6_all_rules_implemented/output',cwd="../../../downward/",shell=True)
    else:
        print(' >>> \'sas_plan\' not fount')
        exit()
    #os.system('./fast-downward.py chess-domain.pddl chess-problem.pddl --search "eager_greedy([ff])"')

def convert_plan():
    '''converts plan so I can nicely view it in vs code. completely unneccessary but I wanted it.'''
    #print(' >>> creating .plan file...')
    with open('output/sas_plan', mode='r') as f:
        txt_file = f.readlines()
        copy_txt_file=txt_file.copy()
        txt_file.insert(0,';;!problem: chess-problem\n')
        txt_file.insert(0,';;!domain: chess\n')
    f.close()
    for line in range(len(txt_file)):
        if line >1 and line<len(txt_file)-1:
            txt_file[line]=str(line-1)+': '+txt_file[line]
    with open('output/sas_plan', mode='w') as f:
        for line in txt_file:
            f.write("".join(line))
        os.rename('output/sas_plan','output/sas_plan.plan')
    f.close()
    #print(' >>> .plan file created')
    return copy_txt_file
    

def load_file(Type,start_FEN=None,goal_FEN=None):
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
        #start_FEN='5/4p/5/PPPPP/RQKNB'
        #goal_FEN='B1P2/4Q/1PNPp/P3P/3RK'
        txt_file=replace(txt_file,';[:init_start_state]\n',PG.add_FEN_pos_to_PDDL(start_FEN))
        #print('start:\n',PG.add_FEN_pos_to_PDDL(start_FEN))
        #print('goal:\n',PG.add_FEN_pos_to_PDDL(goal_FEN))
        txt_file=replace(txt_file,';[:init_diffByN]\n',PG.add_diffByN(8))
        txt_file=replace(txt_file,';[:init_diffByN_hor_ver]\n',PG.add_diffByN_hor_ver())
        txt_file=replace(txt_file,';[:init_pawn_start_pos]\n',PG.add_double_pawn_moves())
        txt_file=replace(txt_file,';[:init_plusOne]\n',PG.add_one_forward())

        #goal:
        #txt_file=replace(txt_file,';[:goal_position]\n',PG.add_FEN_pos_to_PDDL(goal_FEN)) #TODO: this does work only limitedly: I canot assign right numbers to pieces so let's do this by hand right now

        #Visualize :init & :goal pos
        s=FEN.add_coordinate_System(FEN.printable_board(FEN.FEN_to_Chess_board(start_FEN),True,True))
        g=FEN.add_coordinate_System(FEN.printable_board(FEN.FEN_to_Chess_board(goal_FEN),True,True))
        FEN.print_neighbor(s,g)
    else:
        txt_file=replace(txt_file,';[:action_bishop_move]\n',PG.add_bishop_moves(7))
        txt_file=replace(txt_file,';[:action_queen_move]\n',PG.add_bishop_moves(8)) #add diagonal moves of bishop (plus horizontal and vertical moves of Rook)
    write_pddl(txt_file,Type)

def print_plan(plan):
    '''print plan so it is readable'''
    print('The Plan:\n==========')
    for line in plan[:-1]:
        elem =line.split()
        print('{}:\t{}{}->{}{}'.format(elem[1],chr(int(elem[2][1:])+64),elem[3][1:],chr(int(elem[4][1:])+64),elem[5][1:-1]))

def main():
    load_file('problem','5/p1p1p/1pPPp/1P1P1/5','3P1/2pP1/ppP1p/1P2p/5')#'5/4p/5/PPPPP/RQKNB','B1P2/4Q/1PNPp/P3P/3RK') #goal position only works without en passant...
    load_file('domain')
    execute_planner()
    plan=convert_plan()
    print_plan(plan)

if __name__ == "__main__":
    main()
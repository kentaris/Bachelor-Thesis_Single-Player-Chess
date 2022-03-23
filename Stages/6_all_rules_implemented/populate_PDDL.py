import os
import sys
import population_generator as PG
import FEN
from subprocess import call
import time

class Global():
    
    path_to_downward='../../../downward/'
    full_path_to_output_folder = '/home/ken/Documents/Bachelor-Thesis_Single-Player-Chess/Stages/6_all_rules_implemented/output'
    s=''
    g=''
    t=[0,0]

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
    with open(Global.path_to_downward+'/'+name, mode='w') as f:
        for line in txt_file:
            f.write("".join(line))
    f.close()

def execute_planner():
    '''executes the pddl planner which Augusto sent me'''
    Global.t[0]=time.perf_counter_ns()
    print('\n\n >>> executing planner')
    call('./fast-downward.py chess-domain.pddl chess-problem.pddl --search "eager_greedy([ff])"',cwd=Global.path_to_downward,shell=True)
    files=os.listdir(Global.path_to_downward)
    if 'sas_plan' in files:
        call('mv sas_plan {}'.format(Global.full_path_to_output_folder),cwd=Global.path_to_downward,shell=True)
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
    
def compare_time(T):
    '''prints the difference of last recorded execution times and records it if not yet measured'''
    try:
        with open('time', mode='r') as f:
            txt_file = f.readlines()
    except: #if file doesn't exist
        with open('time', mode='w+') as f:
            txt_file = f.readlines()
    if len(txt_file)==0 or len(txt_file)==1:
        txt_file.append(str(T)+'\n')
        with open('time', mode='w') as f:
            f.write("".join(txt_file))
        print('   (measurement {} recorded)'.format(len(txt_file)))
    elif len(txt_file)>=2:
        #txt_file[0]=txt_file[1]
        #txt_file[1]=(str(T)+'\n')
        txt_file.append(str(T)+'\n')
        with open('time', mode='w') as f:
            f.write("".join(txt_file))
        diff=(int(txt_file[-1])-int(txt_file[-2]))/(1000*1000)
        #print(int(diff/(1000*1000)) #--->average difference in ms (if run multiple times on same files it stays at 10 ms)
        avg_diff=100
        #diff*=(-1)
        sign=''
        if diff>0:
            sign='+'
        print('   | {}{}ms'.format(sign,int(diff)))
    f.close()

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
        txt_file=replace(txt_file,';[:init_start_state]\n',PG.add_FEN_pos_to_PDDL(start_FEN,'start'))
        #print('start:\n',PG.add_FEN_pos_to_PDDL(start_FEN))
        #print('goal:\n',PG.add_FEN_pos_to_PDDL(goal_FEN))
        txt_file=replace(txt_file,';[:init_diffByN]\n',PG.add_diffByN(4))
        #txt_file=replace(txt_file,';[:init_diffByN_hor_ver]\n',PG.add_diffByN_hor_ver(9))
        txt_file=replace(txt_file,';[:init_pawn_start_pos]\n',PG.add_double_pawn_moves())
        txt_file=replace(txt_file,';[:init_plusOne]\n',PG.add_one_forward())

        #goal:
        txt_file=replace(txt_file,';[:goal_position]\n',PG.add_FEN_pos_to_PDDL(goal_FEN,'start')) #TODO: this does work only limitedly: I canot assign right numbers to pieces so let's do this by hand right now

        #Visualize :init & :goal pos
        Global.s=FEN.add_coordinate_System(FEN.printable_board(FEN.FEN_to_Chess_board(start_FEN),True,True))
        Global.g=FEN.add_coordinate_System(FEN.printable_board(FEN.FEN_to_Chess_board(goal_FEN),True,True))
        FEN.print_neighbor(Global.s,Global.g)
    else:
        pass
        #txt_file=replace(txt_file,';[:action_bishop_move]\n',PG.add_bishop_moves(7))
        #txt_file=replace(txt_file,';[:action_queen_move]\n',PG.add_bishop_moves(8)) #add diagonal moves of bishop (plus horizontal and vertical moves of Rook)
    write_pddl(txt_file,Type)

def print_plan(plan):
    '''print plan so it is readable'''
    print('The Plan:\n==========')
    for line in plan[:-1]:
        elem =line.split()
        if 'castling' in elem[0]:
            print('castling:\t{}{} & {}{}'.format(chr(int(elem[-4][1:])+64),elem[-3][1:],chr(int(elem[-2][1:])+64),elem[-1][1:-1]))
        else:
            print('{}:\t{}{}->{}{}'.format(elem[1],chr(int(elem[-4][1:])+64),elem[-3][1:],chr(int(elem[-2][1:])+64),elem[-1][1:-1]))

def translate_time(T,print_=False):
    '''translates nanoseconds to s,ms,µs & ns'''
    s=T/(1000*1000*1000)
    s_int=int(s)
    ms=(s-s_int)*1000
    ms_int=int(ms)
    µs=(ms-ms_int)*1000
    µs_int=int(µs)
    ns=(µs-µs_int)*1000
    ns_int=int(ns)
    if print_:
        return '{}s {}ms {}µs {}ns'.format(s_int,ms_int,µs_int,ns_int)
    else:
        return [s_int,ms_int,µs_int,ns_int]

def time_it():
    '''calculates the time it took to execute program'''
    Global.t[1]=time.perf_counter_ns()
    T=Global.t[1]-Global.t[0]
    t=translate_time(T,True)
    #print('\033[93m >>> rounded time:',translate_time(round(int(T),-8),True)) #Round to 100 ms accuracy so output is more constant
    print('\033[93m >>> time:',t,' ',end='')
    compare_time(T)
    print('\033[0m',end='')

def main():
    '''
    Problems:
    - Pieces can dissapear if they are present in start_FEN but not in goal_FEN
    - Pieces can move out of the way to acheave certain goal state even tough in the goal state it says that the piece needs to be "captured"/not be on the board
        -start_FEN='5/5/Q4/R4/5'
        -goal_FEN ='5/5/R4/5/5'   --> queen moves out of the way instead of returning 'no plan found' or 'unreachable position'
    - 
    '''
    start_FEN='3r1/5/5/3p1/2K2'#'1q3/4B/2Q2/5/Rb2r'#'2K2/krpb1/3R1/PNR2/rQ1Bn'
    goal_FEN ='3r1/5/5/3K1/5'#'1Q3/4b/5/5/4R'#'PKbQr/kr3/1R1RN/2n1B/2p2'
    if len(sys.argv)==1: #do all
        load_file('problem',start_FEN,goal_FEN)
        load_file('domain')
        execute_planner()
        FEN.print_neighbor(Global.s,Global.g)
        plan=convert_plan()
        print_plan(plan)
        time_it()
    elif sys.argv[1]=='planner': #just execute the planner on the existing .pddl files (so I can edit them and test stuff quickly)
        execute_planner()
        FEN.print_neighbor(Global.s,Global.g)
        plan=convert_plan()
        print_plan(plan)
        time_it()
    elif sys.argv[1]=='create': #just create .pddl files
        load_file('problem',start_FEN,goal_FEN)
        load_file('domain')
        
if __name__ == "__main__":
    main()
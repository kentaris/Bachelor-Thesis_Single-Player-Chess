import os
import sys
import population_generator as PG
import FEN
from subprocess import call
import time
import unit_test
import validator

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
        #txt_file.insert(0,';;!problem: chess-problem\n')
        #txt_file.insert(0,';;!domain: chess\n')
    f.close()
    #for line in range(len(txt_file)):
    #    if line >1 and line<len(txt_file)-1:
    #        txt_file[line]=str(line-1)+': '+txt_file[line]
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
        txt_file=replace(txt_file,';[:init_start_state]\n',PG.add_FEN_pos_to_PDDL(start_FEN))
        #print('start:\n',PG.add_FEN_pos_to_PDDL(start_FEN))
        #print('goal:\n',PG.add_FEN_pos_to_PDDL(goal_FEN))
        txt_file=replace(txt_file,';[:init_diffByN]\n',PG.add_diffByN(3))
        #txt_file=replace(txt_file,';[:init_diffByN_hor_ver]\n',PG.add_diffByN_hor_ver(9))
        txt_file=replace(txt_file,';[:init_pawn_start_pos]\n',PG.add_double_pawn_moves())
        txt_file=replace(txt_file,';[:init_plusOne]\n',PG.add_one_forward())
        #txt_file=replace(txt_file,';[:can_double_move]\n',PG.add_double_moves_pawn(PG.board_size))

        txt_file=replace(txt_file,';[:colors]\n',PG.add_color_predicates(start_FEN))
        txt_file=replace(txt_file,';[:piece_types]\n',PG.add_piece_types(start_FEN))

        #goal:
        txt_file=replace(txt_file,';[:goal_position]\n',PG.add_FEN_pos_to_PDDL(goal_FEN,'goal'))
        txt_file=replace(txt_file,';[:removed]\n',PG.add_removed_pieces(start_FEN,goal_FEN))

        #Visualize :init & :goal pos
        Global.s=FEN.add_coordinate_System(FEN.printable_board(FEN.FEN_to_Chess_board(start_FEN,PG.board_size),True,True))
        Global.g=FEN.add_coordinate_System(FEN.printable_board(FEN.FEN_to_Chess_board(goal_FEN,PG.board_size),True,True))
        FEN.print_neighbor(Global.s,Global.g)
    else:
        pass
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

def time_it(R=None):
    '''calculates the time it took to execute program'''
    Global.t[1]=time.perf_counter_ns()
    T=Global.t[1]-Global.t[0]
    t=translate_time(T,True)
    #print('\033[93m >>> rounded time:',translate_time(round(int(T),-8),True)) #Round to 100 ms accuracy so output is more constant
    if R==None:
        print('\033[93m >>> time:',t,' ',end='')
        compare_time(T)
    else:
        return t
    
    print('\033[0m',end='')

class Validation_Error(Exception):
    pass

def main():
    start_FEN=start_FEN='p4/5/5/5/4B'#'5/5/5/rRrR1/5'#'5/5/4r/4P/5'#'5/5/1pp2/1Pp2/5'#'5/1pppp/1R1N1/PPP2/5'#'b4/5/2n2/5/4K'#'P4/5/5/5/4b'#'1r3/2r2/3K1/5/5'#'b4/1b3/2K2/5/5'#'3r1/5/5/3p1/2K2'
    goal_FEN ='5/5/B4/5/5'#'5/rRrR1/5/5/5'#'5/4P/4r/5/5'#'5/5/1pP2/5/2p2'#'5/PpP2/R1pN1/4p/5'#'b4/5/5/5/1n2K'#'b4/5/5/5/5'#'1P3/5/5/5/5'#'2K2/5/5/5/5'#'K4/5/5/5/5'#'3r1/5/5/3K1/5'
    if len(sys.argv)==1: #do all
        load_file('problem',start_FEN,goal_FEN)
        load_file('domain',start_FEN)
        execute_planner()
        FEN.print_neighbor(Global.s,Global.g)
        plan=convert_plan()
        print_plan(plan)
        time_it()
    elif sys.argv[1]=='planner': #just execute the planner on the existing .pddl files (so I can edit them and test stuff quickly)
        execute_planner()
        Global.s=FEN.add_coordinate_System(FEN.printable_board(FEN.FEN_to_Chess_board(start_FEN,PG.board_size),True,True))
        Global.g=FEN.add_coordinate_System(FEN.printable_board(FEN.FEN_to_Chess_board(goal_FEN,PG.board_size),True,True))
        FEN.print_neighbor(Global.s,Global.g)
        plan=convert_plan()
        print_plan(plan)
        time_it()
    elif sys.argv[1]=='create': #just create .pddl files
        load_file('problem',start_FEN,goal_FEN)
        load_file('domain',start_FEN)
    elif sys.argv[1]=='test': #perform all unit tests
        succ=[]
        fail=[]
        for i in range(len(vars(unit_test.units))-4):
            try:
                test=unit_test.get(i)
                load_file('problem',test[0],test[1])
                load_file('domain',test[0])
                execute_planner()
                succ.append([i,test[0],test[1],time_it('return value')])
                print('\u001b[32m >>> Test #{} successfull (\'{}\',\'{}\')\033[0m'.format(chr(i+65),test[0],test[1]))
                plan=convert_plan()
                print_plan(plan)
                if not validator.validate(test[0],test[1],plan):
                    raise Validation_Error #TODO: this doesn't catch cases where the python-chess module crashes because of a move that is no valid...
            except Validation_Error:
                fail.append([i,'\033[01;31mvalidation error\033[0m'])
                print('\033[01;31m \t\t>>> validation error\033[0m')
            except KeyboardInterrupt:
                t=time_it('return value')
                fail.append([i,test[0],test[1],'keyboard interrupt after: {}'.format(t)])
                print('\033[93m \t\t>>> keyboard interrupt after: {}\033[0m'.format(t))
                #exit()
            except:# Exception as e:
                t=time_it('return value')
                fail.append([i,test[0],test[1],'\'sas-plan\' not found after {}'.format(t)])
                print('\033[93m >>> Test #{} failed (\'{}\',\'{}\')\033[0m after {}'.format(chr(i+65),test[0],test[1],t))
        print('\nsummary:\n========')
        txt=[]
        for i in range(len(succ)):
            s=succ[i]
            txt.append('\u001b[32m \u2705 #{} succeeded: (\'{}\',\'{}\' | {})\033[0m'.format(chr(int(s[0])+65),s[1],s[2],s[3]))
            print(txt[i])
        for i in range(len(fail)):
            s=fail[i]
            txt.append('\033[93m \u274c #{} failed:\t  (\'{}\',\'{}\' | {})\033[0m'.format(chr(int(s[0])+65),s[1],s[2],s[3]))
            print(txt[i+len(succ)])
        with open('test_results.txt', mode='w') as f:
            for line in txt:
                l=line[(1+len(line.split()[0])):-4]+'\n'
                f.write("".join(l))
        f.close()
        
if __name__ == "__main__":
    main()
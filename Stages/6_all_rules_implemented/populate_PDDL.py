import os
import sys
import population_generator as PG
import FEN
from subprocess import call
import time
import unit_test
import validator
import threading

class Global():
    path_to_downward='../../../downward/'
    full_path_to_output_folder = '/home/ken/Documents/Bachelor-Thesis_Single-Player-Chess/Stages/6_all_rules_implemented/output'
    s=''
    g=''
    t=[0,0]
    cont=False

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
    '''executes the ai basel pddl planner'''
    Global.t[0]=time.perf_counter_ns()
    print('\n\n >>> executing planner')
    call('./fast-downward.py chess-domain.pddl chess-problem.pddl --search "eager_greedy([ff])"',cwd=Global.path_to_downward,shell=True)
    files=os.listdir(Global.path_to_downward)
    if 'sas_plan' in files:
        call('cp sas_plan {}'.format(Global.full_path_to_output_folder),cwd=Global.path_to_downward,shell=True)
    else:
        print(' >>> \'sas_plan\' not fount')
        exit()
    Global.cont=True
    #os.system('./fast-downward.py chess-domain.pddl chess-problem.pddl --search "eager_greedy([ff])"')

def convert_plan():
    '''converts plan so I can nicely view it in vs code. completely unneccessary but I wanted it.'''
    #print(' >>> creating .plan file...')
    with open('output/sas_plan', mode='r') as f:
        txt_file = f.readlines()
        txt_file_corrected=[]
        for l in txt_file:
            if 'check_if_last_move_was_valid' not in l and 'release_action' not in l:
                txt_file_corrected.append(l+'\n')
        #copy_txt_file=txt_file.copy()
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
    return txt_file_corrected
    
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

def load_file(Type,start_FEN=None,goal_FEN=None,turn_start=None):
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
        #objects:
        txt_file=replace(txt_file,';[:locations]\n',PG.add_locations())
        txt_file=replace(txt_file,';[:object_pieces]\n',PG.add_objects(start_FEN,goal_FEN))
        #init:
        txt_file=replace(txt_file,';[:init_start_state]\n',PG.add_FEN_pos_to_PDDL(start_FEN))
        txt_file=replace(txt_file,';[:init_diffByN]\n',PG.add_diffByN(3))#PG.board_size-1))
        #txt_file=replace(txt_file,';[:init_diffBy]\n',PG.add_not_same(3))#PG.board_size-1))
        txt_file=replace(txt_file,';[:init_pawn_start_pos]\n',PG.add_double_pawn_moves())
        txt_file=replace(txt_file,';[:init_plusOne]\n',PG.add_one_forward())
        txt_file=replace(txt_file,';[:is_on_board]\n',PG.add_figures_on_board(start_FEN))
        txt_file=replace(txt_file,';[:init_Same_color]\n',PG.add_same_color(start_FEN,goal_FEN))

        txt_file=replace(txt_file,';[:colors]\n',PG.add_color_predicates(start_FEN,goal_FEN))
        txt_file=replace(txt_file,';[:piece_types]\n',PG.add_piece_types(start_FEN,goal_FEN))
        txt_file=replace(txt_file,';[:last_pawn_line]\n',PG.add_last_pawn_line())
        txt_file=replace(txt_file,';[:castling]\n',PG.add_castling(start_FEN))
        txt_file=replace(txt_file,';[:whos_turn]\n',PG.add_turn(turn_start))
        #txt_file=replace(txt_file,';[:adjacent]\n',PG.adjacent())
        #txt_file=replace(txt_file,';[:same_diag]\n',PG.same_diag())
        #txt_file=replace(txt_file,';[:between]\n',PG.between())

        #goal:
        txt_file=replace(txt_file,';[:goal_position]\n',PG.add_FEN_pos_to_PDDL_goal(goal_FEN,'goal'))
        #txt_file=replace(txt_file,';[:removed]\n',PG.add_removed_pieces(start_FEN,goal_FEN))

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
    for l in range(len(plan[:-1])):
        line = plan[l]
        elem =line.split()
        if 'castling' in elem[0]:
            print('{}: castling:\t{}{} & {}{}'.format(l+1, chr(int(elem[-4][1:])+64),elem[-3][1:],chr(int(elem[-2][1:])+64),elem[-1][1:-1]))
        elif 'promotion' in elem[0]:
            print('{}: prom. {}:\t{}{}->{}{}'.format(l+1, elem[1],chr(int(elem[-4][1:])+64),elem[-3][1:],chr(int(elem[-2][1:])+64),elem[-1][1:-1]))
        else:
            print('{}: {}:\t{}{}->{}{}'.format(l+1, elem[1],chr(int(elem[-4][1:])+64),elem[-3][1:],chr(int(elem[-2][1:])+64),elem[-1][1:-1]))

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

class Timeout_Error(Exception):
    pass

def after_timeout():
    Global.cont=True
    raise Timeout_Error

def main():#'3k/4/PPPP/3K' ,'3k/P3/1PPP/3K'
    start_FEN=start_FEN='k2K4/8/8/8/8/8/7r/RR6'#'8/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR'#'p2k/4/4/K2B'#'p6k/8/8/8/8/8/8/6KB'#'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR'#'K2/1rr/2r'#'1kR/1r1/2R'#'k2/2r/RR1' # 'k2/2r/RR1'  'kR1/r2/2P' #      #'1r3/2r2/3K1/5/5' -->same situation with bishops is much faster:'b4/1b3/2K2/5/5'
    goal_FEN =          'k2K4/8/8/8/8/8/r7/RR6'#'8/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR'#'B2k/4/4/K3'#'B6k/8/8/8/8/8/8/6K1'#'rnbqkbnr/pppppppp/8/8/8/P/1PPPPPPP/RNBQKBNR'#'K2/1rr/r2'#'k1R/1r1/2R'#'k2/1r1/R1R'         # 'k2/1r1/R1R' 'kR1/1rP/3' #       #'3r1/5/5/3K1/5'#'1K3/5/5/5/5' --> ""  '2K2/5/5/5/5'
    #start_FEN=start_FEN='k2/r2/R2'#'k2/2r/RR1' # 'k2/2r/RR1'  'kR1/r2/2P' #     
    #goal_FEN ='k2/1r1/R2'#'k2/r2/RR1' #'k2/1r1/R1R'    'kR1/1rP/3'#   
    turn_start='black'
    if len(sys.argv)==1: #do all
        load_file('problem',start_FEN,goal_FEN,turn_start)
        load_file('domain',start_FEN)
        execute_planner()
        FEN.print_neighbor(Global.s,Global.g)
        plan=convert_plan()
        print_plan(plan)
        time_it()
        if validator.validate(start_FEN,goal_FEN,plan,turn_start):
            print('     \u001b[32m--> This is a VALID plan!\033[0m')
        print(goal_FEN)
    elif 'planner' in sys.argv[1].lower(): #just execute the planner on the existing .pddl files (so I can edit them and test stuff quickly)
        Global.s=FEN.add_coordinate_System(FEN.printable_board(FEN.FEN_to_Chess_board(start_FEN,PG.board_size),True,True))
        Global.g=FEN.add_coordinate_System(FEN.printable_board(FEN.FEN_to_Chess_board(goal_FEN,PG.board_size),True,True))
        FEN.print_neighbor(Global.s,Global.g)
        plan=convert_plan()
        print_plan(plan)
        time_it()
    elif 'create' in sys.argv[1].lower(): #just create .pddl files
        load_file('problem',start_FEN,goal_FEN,turn_start)
        load_file('domain',start_FEN)
    elif 'test' in sys.argv[1].lower(): #perform all unit tests
        succ=[]
        fail=[]
        for i in range(len(vars(unit_test.units))-4):
            try:
                test=unit_test.get(i)
                load_file('problem',test[0],test[1],turn_start)
                load_file('domain',test[0])
                #t=threading.Thread(target=execute_planner)
                #t.daemon=True
                #t.start()
                #threading.Timer(10, after_timeout).start() #abort planner if takes longer than 2 minutes...
                #while not Global.cont:
                #    time.sleep(1)
                #t.join()
                #Global.cont=False
                plan=convert_plan()
                print_plan(plan)
                if not validator.validate(test[0],test[1],plan,turn_start):
                    raise Validation_Error #TODO: this doesn't catch cases where the python-chess module crashes because of a move that is no valid...
                print('\u001b[32m >>> Test #{} successfull (\'{}\',\'{}\')\033[0m'.format(unit_test.get(i,True),test[0],test[1]))
                succ.append([i,test[0],test[1],time_it('return value')])
            except Validation_Error:
                fail.append([i,test[0],test[1],'\033[01;31mValidation error\033[0m'])
                print('\033[01;31m \t\t>>> validation error\033[0m')
            except Timeout_Error:
                fail.append([i,test[0],test[1],'\033[01;31m<Timeout> error\033[0m'])
                print('\033[01;31m \t\t>>> Timeout error\033[0m')
            except KeyboardInterrupt:
                t=time_it('return value')
                fail.append([i,test[0],test[1],'keyboard interrupt after: {}'.format(t)])
                print('\033[93m \t\t>>> keyboard interrupt after: {}\033[0m'.format(t))
                #exit()
            except:# Exception as e:
                t=time_it('return value')
                fail.append([i,test[0],test[1],'\'sas-plan\' not found after {}'.format(t)])
                print('\033[93m >>> Test #{} failed (\'{}\',\'{}\')\033[0m after {}'.format(unit_test.get(i,True),test[0],test[1],t))
        print('\nsummary:\n========')
        txt=[]
        for i in range(len(succ)):
            s=succ[i]
            txt.append('\u001b[32m \u2705 #{} succeeded: (\'{}\',\'{}\' | {})\033[0m'.format(unit_test.get(s[0],True),s[1],s[2],s[3]))
            print(txt[i])
        for j in range(len(fail)):
            s=fail[j]
            txt.append('\033[93m \u274c #{} failed:\t  (\'{}\',\'{}\' | {})\033[0m'.format(unit_test.get(s[0],True),s[1],s[2],s[3]))
            print(txt[j+len(succ)])
        with open('test_results.txt', mode='w') as f:
            for line in txt:
                l=line[(1+len(line.split()[0])):-4]+'\n'
                f.write("".join(l))
        f.close()
        
if __name__ == "__main__":
    main()
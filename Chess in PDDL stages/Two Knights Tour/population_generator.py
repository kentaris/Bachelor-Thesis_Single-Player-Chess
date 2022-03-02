def knight_visited_goal():
    R=''
    for rank in range(8):
        for file in range(8):
            #R+='\t\t\t\t(visited n{} n{})\n'.format(rank+1,file+1) #Two knights goal
            R+='\t\t\t\t(visited knight_b1 n{} n{})\n'.format(rank+1,file+1) #visited knight_b1 n1 n8
            R+='\t\t\t\t(visited knight_b2 n{} n{})\n'.format(rank+1,file+1)
    return R

def knight_diffByOneTwo():
    R=''
    diff_by_one=''
    for i in range(8): #rank
        for j in range(8): #file
            entry='(diff_by_one n{} n{})'.format(i+1,j+1)
            entry_reversed='(diff_by_one n{} n{})'.format(j+1,i+1)
            if (abs(i-j)==1) and entry not in diff_by_one:
                diff_by_one+='\t\t'+entry+'\n'
                diff_by_one+='\t\t'+entry_reversed+'\n'
    diff_by_two=''
    for i in range(8): #rank
        for j in range(8): #file
            entry='(diff_by_two n{} n{})'.format(i+1,j+1)
            entry_reversed='(diff_by_two n{} n{})'.format(j+1,i+1)
            if (abs(i-j)==2) and entry not in diff_by_two:
                diff_by_two+='\t\t'+entry+'\n'
                diff_by_two+='\t\t'+entry_reversed+'\n'
    R+='\t\t;Difference by one:\n'+diff_by_one+'\t\t;Difference by two:\n'+diff_by_two
    return R

def board():
    R=''
    for rank in range(8):
        r=''
        for file in range(8):
            r+=chr(rank+1+64)+str(file+1)+' '
        R+='\t\t'+r+'\n'
    return R
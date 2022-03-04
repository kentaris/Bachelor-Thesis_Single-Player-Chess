import FEN
from os import system, name
  
def clear():
    # for windows
    if name == 'nt':
        _ = system('cls')
    # for mac and linux(here, os.name is 'posix')
    else:
        _ = system('clear')

def main():
    starting_position=FEN.FEN_to_Chess_board('rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR')
    starting_position=FEN.printable_board(starting_position,True,True)
    starting_position=FEN.add_coordinate_System(starting_position)
    
    print(starting_position)
    


if __name__ == "__main__":
    main()
else:
    exit()
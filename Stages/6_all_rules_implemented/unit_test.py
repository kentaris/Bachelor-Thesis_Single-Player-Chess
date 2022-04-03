class units:
    A=['P4/5/5/5/4b','b4/5/5/5/5'] #bishop captures a pawn
    B=['5/5/5/PPPPP/5','5/PPPPP/5/5/5'] #5 white pawns do a double move
    C=['5/2pp1/5/2P2/5','5/2p2/2p2/5/5'] #pawns column (2 black ones) by capture of a white pawn
    D=['5/R4/5/5/p4','5/5/5/5/R4'] #rook captures pawn horizontally
    E=['5/5/4r/4P/5','5/4P/4r/5/5'] #pawn double move with rook in front of it (rook has to move out of the way)
    F=['3r1/5/5/3p1/2K2','3r1/5/5/3K1/5'] #white king captures pawn which is protected by a rook
    G=['5/5/5/5/R3p','5/5/5/5/4R'] #rook capturing a pawn vertically
    H=['2p2/3pK/5/5/5','5/2K2/5/5/5'] #white king captures pawn which is protected by a pawn
    I=['5/1pppp/1R1N1/PPP2/5','5/Ppp2/RPpN1/4p/5']#['2ppp/1p1RB/P4/3PP/5','1P1p1/4B/3p1/3P1/5'] #crazy pawns
    J=['2br1/3PK/1N3/P1n2/5','3r1/1b1K1/1N3/P4/4n'] #some pieces with one king
    K=['2ppp/1p1RB/P4/3PP/5','1P1p1/4B/3p1/3P1/5']
    K=['1q3/4B/2Q2/5/Rb2r','1Q3/4b/5/5/4R'] #2 queens 2 rooks 2 bishops
    L=['2N2/nrpb1/3R1/PNR2/rQ1Bn','PNbQr/nr3/1R1RN/2n1B/2p2'] #lots of movements with all pieces EXCLUDING king
    M=['5/2p2/3p1/4K/5','5/2K2/5/5/5'] #king captures black pawnn chain from behind (protected)
    N=['2K2/krpb1/3R1/PNR2/rQ1Bn','PKbQr/kr3/1R1RN/2n1B/2p2'] #lots of movements with all pieces INCLUDING king
    O=['1K3/2p2/3p1/5/5','5/5/3K1/5/5'] #king captures black pawnn chain from behind (not protected)
    P=['k3K/2ppp/1p1BR/r4/5','k3K/5/5/r4/p1ppp'] #more time consuming planning (I don't know why since it's simpler than previous test) with 2 kings on the board
    Q=['3n1/1n3/2K2/5/5','3K1/5/5/5/5'] #king captures knight protected by a knight. TODO: here something strange happens... the king moves more than he needs to. I'm not sure if the problem is the planner itself or if I'm somehow preventing my piece from capturing. I tested a bit with unprotected knights in the same position and the same happens...
    R=['1b3/2r2/3K1/5/5','1K3/5/5/5/5'] #king captures rook protected by a bishop
    R=['q4/1r3/2K2/5/5','K4/5/5/5/5'] #king captures rook protected by a queen

def get(N):
    return vars(units)[chr(N+65)]

#running forever:
#================
#this is solved in ~2secs:
#start_FEN='5/1pppp/1R1N1/PPP2/5'
#goal_FEN ='5/Ppp2/RPpN1/4p/5'

#just one little change: the additional en-passant move: b3->c4 (white pawn captures black pawn) and it runs forever
#start_FEN='5/1pppp/1R1N1/PPP2/5'
#goal_FEN ='5/PpP2/R1pN1/4p/5'
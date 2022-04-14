class units: #TODO: currently only positions where white starts are allowed. easy to change tough
    AA=['p4/5/5/5/4B','B4/5/5/5/5'] #white bishop captures a pawn
    AB=['5/r4/5/PPPPP/5','5/PPPPP/5/5/4r'] #5 white pawns do a double move with a black rook present
    AC=['5/2pp1/5/2P2/5','5/2p2/2p2/5/5'] #pawns column (2 black ones) by capture of a white pawn
    AD=['5/R4/5/5/p4','5/5/5/5/R4'] #rook captures pawn horizontally
    AE=['4r/5/4R/4P/5','4r/4P/4R/5/5'] #white pawn double move with white rook in front of it (rook has to move out of the way)
    AF=['3r1/5/5/3p1/2K2','3r1/5/5/3K1/5'] #!!! very long ...white king captures pawn which is protected by a rook !!!!!-->takes 108 minutes...
    AG=['5/5/5/5/R3p','5/5/5/5/4R'] #rook capturing a pawn vertically
    AH=['2p2/3pK/5/5/5','5/5/2K2/5/5'] #white king captures pawn which is protected by a pawn <-------
    AI=['5/1pppp/1R1N1/PPP2/5','5/Ppp2/RPpN1/4p/5']#['5/1pppp/1R1N1/PPP2/5','5/Ppp2/RPpN1/4p/5'] #crazy pawns
    AJ=['5/3p1/5/2P2/5','5/5/2p2/5/5'] #en-passant (to test white: ['5/3p1/5/1QP2/5','5/5/3P1/Q4/5'])
    AK=['2ppp/1p1RB/P4/3PP/5','1P1p1/4B/3p1/3P1/5'] #lots of movements without a king
    AL=['1q3/4B/2Q2/5/Rb2r','1Q3/4b/5/5/4R'] #2 queens 2 rooks 2 bishops
    AM=['2N2/nrpb1/3R1/PNR2/rQ1Bn','PNbQr/nr3/1R1RN/2n1B/2p2'] #lots of movements with all pieces EXCLUDING king
    AN=['1K3/2p2/3p1/5/5','5/5/2K2/3p/5'] #king captures black pawnn chain from behind (protected)
    AO=['2K2/krpb1/3R1/PNR2/rQ1Bn','PKbQr/kr3/1R1RN/2n1B/2p2'] #!!!-->lots of movements with all pieces INCLUDING king
    AP=['3n1/1n3/2K2/5/5','3K1/5/5/5/5'] #king captures knight protected by a knight. TODO: here something strange happens... the king moves more than he needs to. I'm not sure if the problem is the planner itself or if I'm somehow preventing my piece from capturing. I tested a bit with unprotected knights in the same position and the same happens...
    AQ=['1b3/2r2/3K1/5/5','1K3/5/5/5/5'] #king captures rook protected by a bishop
    AR=['q4/1r3/2K2/5/5','K4/5/5/5/5'] #king captures rook protected by a queen
    AS=['2br1/3PK/1N3/P1n2/5','3r1/1b1K1/1N3/P4/4n'] #some pieces with one king
    AT=['5/1P3/5/5/5','1Q3/5/5/5/5'] #pawn promotion
    AU=['5/5/1pp2/1Pp2/5','5/5/1pP2/5/2p2'] #3 black pawns and a white one. the middle black pawn gets captured
    AV=['b4/5/2n2/5/4K','b4/5/5/5/1n2K'] #discovered check by moving a rook which blocks the line of a bishop
    AW=['5/5/5/5/r1n1K','5/5/3n1/5/r3K'] #discovered check by moving knight out of rook line
    AX=['5/4K/5/P4/4k','5/P3K/5/5/4k'] #!!!-->pawn double move with 2 kings present--->not working
    AY=['k4/3K1/5/P4/4n','5/5/k3K/5/5'] #!!!-->2 kings, a pawn and a knight
    AZ=['5/5/5/5/RK2R','5/5/5/5/R1RK1'] #castling
    BA=['5/5/5/5/pK3','5/5/5/5/pK3'] #problem case where start pos = end pos

def get(N,r=False):
    first=chr((int(N/26)+65))
    second=chr(((N-((int(N/26)+65)*26))%26)+65)
    if r:
        return first+second
    return vars(units)[first+second]
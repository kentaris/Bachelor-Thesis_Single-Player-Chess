class units:
    A=['P4/5/5/5/4b','b4/5/5/5/5'] #bishop
    B=['R4/5/p4/5/5','5/5/R4/5/5'] #rook capturing a pawn
    C=['5/R4/5/5/p4','5/5/5/5/R4'] #rook captures pawn
    D=['5/5/4r/4P/5','5/4P/4r/5/5'] #pawn double move with rook in front of it (rook has to move out of the way)
    E=['3r1/5/5/3p1/2K2','3r1/5/5/3K1/5'] #white king captures pawn which is protected by a rook
    F=['5/1pppp/1R1N1/PPP2/5','5/Ppp2/RPpN1/4p/5']#['2ppp/1p1RB/P4/3PP/5','1P1p1/4B/3p1/3P1/5'] #crazy pawns
    G=['1q3/4B/2Q2/5/Rb2r','1Q3/4b/5/5/4R'] #2 queens 2 rooks 2 bishops
    H=['2br1/3PK/1N3/P1n2/5','3r1/1b1K1/1N3/P4/4n'] #some pieces with one king
    I=['2N2/nrpb1/3R1/PNR2/rQ1Bn','PNbQr/nr3/1R1RN/2n1B/2p2'] #lots of movements with all pieces EXCLUDING king
    #J=['2K2/krpb1/3R1/PNR2/rQ1Bn','PKbQr/kr3/1R1RN/2n1B/2p2'] #lots of movements with all pieces INCLUDING king

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
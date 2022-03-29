class units:
    A=['5/R4/5/5/k4','5/5/5/5/R4']
    B=['3r1/5/5/3p1/2K2','3r1/5/5/3K1/5']
    C=['1q3/4B/2Q2/5/Rb2r','1Q3/4b/5/5/4R']
    D=['2K2/krpb1/3R1/PNR2/rQ1Bn','PKbQr/kr3/1R1RN/2n1B/2p2']
    E=['R4/5/p4/5/5','5/5/R4/5/5'] #rook capturing a pawn
    F=['5/1pppp/1R1N1/PPP2/5','5/Ppp2/RPpN1/4p/5']#['2ppp/1p1RB/P4/3PP/5','1P1p1/4B/3p1/3P1/5'] #crazy pawns
    G=['P4/5/5/5/4b','b4/5/5/5/5'] #bishop

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
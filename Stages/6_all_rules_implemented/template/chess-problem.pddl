(define (problem chess-problem)
    (:domain chess)
    (:objects
        n1 n2 n3 n4 n5 - location
    	;white pieces:
        knight_w1 knight_w2 - knight_w
        pawn_w1 pawn_w2 pawn_w3 pawn_w4 pawn_w5 - pawn_w
        king_w1 - king_w
        rook_w1 rook_w2 - rook_w
        w_bishop_w1 b_bishop_w1 - bishop_w
        queen_w1 - queen_w
    	;black pieces:
    	knight_b1 knight_b2 - knight_b
        pawn_b1 pawn_b2 pawn_b3 pawn_b4 pawn_b5 - pawn_b
    	king_b1 - king_b
        rook_b1 rook_b2 - rook_b
        w_bishop_b1 b_bishop_b1 - bishop_b
        queen_b1 - queen_b
    )
    (:init
;[:init_start_state]
;[:init_diffByN]
;[:init_pawn_start_pos]
;[:init_plusOne]
    )
    (:goal (and
;[:goal_position]
           )
    )
)
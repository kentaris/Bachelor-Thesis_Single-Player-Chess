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
;[:init_pawn_start_pos]
;[:init_plusOne]
;[:init_diffByN]
;[:init_diffByN_hor_ver]
        
        (not_moved king_w1)
        (not_moved king_b1)
        (not_moved rook_w1)
        (not_moved rook_w2)
        (not_moved rook_b1)
        (not_moved rook_b2)
        (kingside_rook rook_w2)
        (queenside_rook rook_w1)
        (kingside_rook rook_b2)
        (queenside_rook rook_b1)

        ;colors:
        (is_white knight_w1)
        (is_white knight_w2)
        (is_white pawn_w1)
        (is_white pawn_w2)
        (is_white pawn_w3)
        (is_white pawn_w4)
        (is_white pawn_w5)
        (is_white king_w1)
        (is_white rook_w1 rook_w2)
        (is_white w_bishop_w1)
        (is_white b_bishop_w1)
        (is_white queen_w1)
        
        (is_black knight_b1)
        (is_black knight_b2)
        (is_black pawn_b1)
        (is_black pawn_b2)
        (is_black pawn_b3)
        (is_black pawn_b4)
        (is_black pawn_b5)
        (is_black king_b1)
        (is_black rook_b1 rook_b2)
        (is_black w_bishop_b1)
        (is_black b_bishop_b1)
        (is_black queen_b1)
    )
    (:goal (and
;[:goal_position]
           )
    )
)
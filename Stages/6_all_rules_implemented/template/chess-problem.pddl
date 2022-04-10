(define (problem chess-problem)
    (:domain chess)
    (:objects
        n1 n2 n3 n4 n5 - location
    	;white pieces:
        pawn_w1 pawn_w2 pawn_w3 pawn_w4 pawn_w5 - pawn_w
        knight_w1 knight_w2 - knight_w
        b_bishop_w1 w_bishop_w2 - bishop_w
        rook_w1 rook_w2 - rook_w
        queen_w1 - queen_w
        king_w1 - king_w
    	;black pieces:
    	pawn_b1 pawn_b2 pawn_b3 pawn_b4 pawn_b5 - pawn_b
        knight_b1 knight_b2 - knight_b
        b_bishop_b1 w_bishop_b2 - bishop_b
        rook_b1 rook_b2 - rook_b
        queen_b1 - queen_b
        king_b1 - king_b
    )
    (:init
;[:init_start_state]
;[:init_pawn_start_pos]
;[:init_plusOne]
;[:init_diffByN]
;[:init_diffByN_hor_ver]
;[:last_pawn_line]
        
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
;[:can_double_move]
        ;colors:
;[:colors]
;[:piece_types]

        (white_s_turn)

        (TRUE)
    )
    (:goal (and
;[:goal_position]
;TODO: the removed predicate still has to be given the exact pawn that has been removed. If I do the same as with the white_pawn_at derived predicate I need to give it a location but I don't know that location. How can I fix this? For now I just leave away the removed predicates for pawns but this can lead to pawns being at locations I don't want them to be.
;[:removed]
           )
    )
)
(define (problem chess-problem)
    (:domain chess)
    (:objects
        ;locations:
		n1 n2 n3  - location
        
        ;object pieces:
		 rook_w1 - rook
		 king_b1 - king
		 rook_b1 - rook
    )
    (:init
        (valid_position)
        ;initial state s_0:
		(at rook_b1 n1 n2)
		(at king_b1 n1 n3)
		(at rook_w1 n1 n1)
		(empty_square n2 n3)
		(empty_square n2 n2)
		(empty_square n2 n1)
		(empty_square n3 n3)
		(empty_square n3 n2)
		(empty_square n3 n1)

		;Pawn double moves start for white:
		(pawn_start_pos_white n1 n2)
		(pawn_start_pos_white n2 n2)
		(pawn_start_pos_white n3 n2)

		;Pawn double moves start for black:
		(pawn_start_pos_black n1 n2)
		(pawn_start_pos_black n2 n2)
		(pawn_start_pos_black n3 n2)

		;Pawn single moves for white:
		(plusOne n1 n2)
		(plusOne n2 n3)

		;Pawn single moves for black:
		(minusOne n2 n1)
		(minusOne n3 n2)

		;Difference by One:
		(diff_by_One n1 n2)
		(diff_by_One n2 n1)
		(diff_by_One n2 n3)
		(diff_by_One n3 n2)

		;Difference by Two:
		(diff_by_Two n1 n3)
		(diff_by_Two n3 n1)

		;Difference by Three:
		(same_color king_b1 king_b1)
		(same_color king_b1 rook_b1)
		(same_color rook_b1 king_b1)
		(same_color rook_b1 rook_b1)
		(same_color rook_w1 rook_w1)
		(not_same_color rook_w1 king_b1)
		(not_same_color rook_w1 rook_b1)
		(not_same_color king_b1 rook_w1)
		(not_same_color rook_b1 rook_w1)
            
        ;figures on the board:
		(is_on_board king_b1)
		(is_on_board rook_b1)
		(is_on_board rook_w1)
        
        ;last pawn line:
		(last_pawn_line n1 n1)
		(last_pawn_line n2 n1)
		(last_pawn_line n3 n1)
		(last_pawn_line n1 n3)
		(last_pawn_line n2 n3)
		(last_pawn_line n3 n3)
        
        ;castling:
        
        ;colors:
		(is_white rook_w1)
		(is_black king_b1)
		(is_black rook_b1)
        
        ;piece types:
		(is_rook rook_w1)
		(is_king king_b1)
		(is_rook rook_b1)
        
        ;turn:

;[:pawn_promotion_extra_pieces]

        (TRUE)
    )
    (:goal (and
                (valid_position)
        ;goal state s_*:
            ;board:
		(black_rook_at n2 n2)
		(black_king_at n1 n3)
		(white_rook_at n1 n1)
		(empty_square n1 n2)
		(empty_square n2 n3)
		(empty_square n2 n1)
		(empty_square n3 n3)
		(empty_square n3 n2)
		(empty_square n3 n1)
            ;removed pieces:
;[:removed]
           )
    )
)
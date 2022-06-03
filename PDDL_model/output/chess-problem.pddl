(define (problem chess-problem)
    (:domain chess)
    (:objects
        ;locations:
		n1 n2 n3 n4  - location
        
        ;object pieces:
		 king_w1 - king
		 pawn_w1 pawn_w2 pawn_w3 pawn_w4 - pawn
		 king_b1 - king
    )
    (:init
        (valid_position)
        ;initial state s_0:
		(at king_b1 n4 n4)
		(at pawn_w1 n1 n2)
		(at pawn_w2 n2 n2)
		(at pawn_w3 n3 n2)
		(at pawn_w4 n4 n2)
		(at king_w1 n4 n1)
		(empty_square n1 n4)
		(empty_square n1 n3)
		(empty_square n1 n1)
		(empty_square n2 n4)
		(empty_square n2 n3)
		(empty_square n2 n1)
		(empty_square n3 n4)
		(empty_square n3 n3)
		(empty_square n3 n1)
		(empty_square n4 n3)

		;Pawn double moves start for white:
		(pawn_start_pos_white n1 n2)
		(pawn_start_pos_white n2 n2)
		(pawn_start_pos_white n3 n2)
		(pawn_start_pos_white n4 n2)

		;Pawn double moves start for black:
		(pawn_start_pos_black n1 n3)
		(pawn_start_pos_black n2 n3)
		(pawn_start_pos_black n3 n3)
		(pawn_start_pos_black n4 n3)

		;Pawn single moves for white:
		(plusOne n1 n2)
		(plusOne n2 n3)
		(plusOne n3 n4)

		;Pawn single moves for black:
		(minusOne n2 n1)
		(minusOne n3 n2)
		(minusOne n4 n3)

		;Difference by One:
		(diff_by_One n1 n2)
		(diff_by_One n2 n1)
		(diff_by_One n2 n3)
		(diff_by_One n3 n2)
		(diff_by_One n3 n4)
		(diff_by_One n4 n3)

		;Difference by Two:
		(diff_by_Two n1 n3)
		(diff_by_Two n2 n4)
		(diff_by_Two n3 n1)
		(diff_by_Two n4 n2)

		;Difference by Three:
		(diff_by_Three n1 n4)
		(diff_by_Three n4 n1)

		;Difference by Four:
;[:init_diffBy]
		(same_color king_b1 king_b1)
		(same_color king_w1 king_w1)
		(same_color king_w1 pawn_w1)
		(same_color king_w1 pawn_w2)
		(same_color king_w1 pawn_w3)
		(same_color king_w1 pawn_w4)
		(same_color pawn_w1 king_w1)
		(same_color pawn_w1 pawn_w1)
		(same_color pawn_w1 pawn_w2)
		(same_color pawn_w1 pawn_w3)
		(same_color pawn_w1 pawn_w4)
		(same_color pawn_w2 king_w1)
		(same_color pawn_w2 pawn_w1)
		(same_color pawn_w2 pawn_w2)
		(same_color pawn_w2 pawn_w3)
		(same_color pawn_w2 pawn_w4)
		(same_color pawn_w3 king_w1)
		(same_color pawn_w3 pawn_w1)
		(same_color pawn_w3 pawn_w2)
		(same_color pawn_w3 pawn_w3)
		(same_color pawn_w3 pawn_w4)
		(same_color pawn_w4 king_w1)
		(same_color pawn_w4 pawn_w1)
		(same_color pawn_w4 pawn_w2)
		(same_color pawn_w4 pawn_w3)
		(same_color pawn_w4 pawn_w4)
		(not_same_color king_w1 king_b1)
		(not_same_color pawn_w1 king_b1)
		(not_same_color pawn_w2 king_b1)
		(not_same_color pawn_w3 king_b1)
		(not_same_color pawn_w4 king_b1)
		(not_same_color king_b1 king_w1)
		(not_same_color king_b1 pawn_w1)
		(not_same_color king_b1 pawn_w2)
		(not_same_color king_b1 pawn_w3)
		(not_same_color king_b1 pawn_w4)
            
        ;figures on the board:
		(is_on_board king_b1)
		(is_on_board pawn_w3)
		(is_on_board king_w1)
		(is_on_board pawn_w2)
		(is_on_board pawn_w1)
		(is_on_board pawn_w4)
        
        ;last pawn line:
		(last_pawn_line n1 n1)
		(last_pawn_line n2 n1)
		(last_pawn_line n3 n1)
		(last_pawn_line n4 n1)
		(last_pawn_line n1 n4)
		(last_pawn_line n2 n4)
		(last_pawn_line n3 n4)
		(last_pawn_line n4 n4)
        
        ;castling:
        
        ;colors:
		(is_white king_w1)
		(is_white pawn_w1)
		(is_white pawn_w2)
		(is_white pawn_w3)
		(is_white pawn_w4)
		(is_black king_b1)
        
        ;piece types:
		(is_king king_w1)
		(is_pawn pawn_w1)
		(is_pawn pawn_w2)
		(is_pawn pawn_w3)
		(is_pawn pawn_w4)
		(is_king king_b1)
        
        ;turn:
		(white_s_turn)
;[:pawn_promotion_extra_pieces]
        
;[:adjacent]
;[:same_diag]
;[:between]

        (TRUE)
    )
    (:goal (and
                (valid_position)
        ;goal state s_*:
            ;board:
		(black_king_at n4 n4)
		(white_pawn_at n1 n3)
		(white_pawn_at n2 n2)
		(white_pawn_at n3 n2)
		(white_pawn_at n4 n2)
		(white_king_at n1 n1)
		(empty_square n1 n4)
		(empty_square n1 n2)
		(empty_square n2 n4)
		(empty_square n2 n3)
		(empty_square n2 n1)
		(empty_square n3 n4)
		(empty_square n3 n3)
		(empty_square n3 n1)
		(empty_square n4 n3)
		(empty_square n4 n1)
            ;removed pieces:
;[:removed]
           )
    )
)
(define (problem chess-problem)
    (:domain chess)
    (:objects
        ;locations:
		n1 n2 n3 n4 n5  - location
        
        ;object pieces:
		 rook_b1 - rook_b
    )
    (:init
        ;initial state s_0:
		(at rook_b1 n1 n4)
		(empty_square n1 n5)
		(empty_square n1 n3)
		(empty_square n1 n2)
		(empty_square n1 n1)
		(empty_square n2 n5)
		(empty_square n2 n4)
		(empty_square n2 n3)
		(empty_square n2 n2)
		(empty_square n2 n1)
		(empty_square n3 n5)
		(empty_square n3 n4)
		(empty_square n3 n3)
		(empty_square n3 n2)
		(empty_square n3 n1)
		(empty_square n4 n5)
		(empty_square n4 n4)
		(empty_square n4 n3)
		(empty_square n4 n2)
		(empty_square n4 n1)
		(empty_square n5 n5)
		(empty_square n5 n4)
		(empty_square n5 n3)
		(empty_square n5 n2)
		(empty_square n5 n1)

		;Pawn double moves start for white:
		(pawn_start_pos_white n1 n2)
		(pawn_start_pos_white n2 n2)
		(pawn_start_pos_white n3 n2)
		(pawn_start_pos_white n4 n2)
		(pawn_start_pos_white n5 n2)

		;Pawn double moves start for black:
		(pawn_start_pos_black n1 n4)
		(pawn_start_pos_black n2 n4)
		(pawn_start_pos_black n3 n4)
		(pawn_start_pos_black n4 n4)
		(pawn_start_pos_black n5 n4)

		;Pawn single moves for white:
		(plusOne n1 n2)
		(plusOne n2 n3)
		(plusOne n3 n4)
		(plusOne n4 n5)

		;Pawn single moves for black:
		(minusOne n2 n1)
		(minusOne n3 n2)
		(minusOne n4 n3)
		(minusOne n5 n4)

		;Difference by One:
		(diff_by_One n1 n2)
		(diff_by_One n2 n1)
		(diff_by_One n2 n3)
		(diff_by_One n3 n2)
		(diff_by_One n3 n4)
		(diff_by_One n4 n3)
		(diff_by_One n4 n5)
		(diff_by_One n5 n4)

		;Difference by Two:
		(diff_by_Two n1 n3)
		(diff_by_Two n2 n4)
		(diff_by_Two n3 n1)
		(diff_by_Two n3 n5)
		(diff_by_Two n4 n2)
		(diff_by_Two n5 n3)

		;Difference by Three:
		(diff_by_Three n1 n4)
		(diff_by_Three n2 n5)
		(diff_by_Three n4 n1)
		(diff_by_Three n5 n2)

		;Difference by Four:
		(diff_by_Four n1 n5)
		(diff_by_Four n5 n1)
            
        ;figures on the board:
;[:is_on_board]
        
        ;last pawn line:
		(last_pawn_line n1 n1)
		(last_pawn_line n2 n1)
		(last_pawn_line n3 n1)
		(last_pawn_line n4 n1)
		(last_pawn_line n5 n1)
		(last_pawn_line n1 n5)
		(last_pawn_line n2 n5)
		(last_pawn_line n3 n5)
		(last_pawn_line n4 n5)
		(last_pawn_line n5 n5)
        
        ;castling:
        
        ;colors:
		(is_black rook_b1)
        
        ;piece types:
		(is_rook rook_b1)
        
        ;turn:

;[:pawn_promotion_extra_pieces]

        (TRUE)
    )
    (:goal (and
        ;goal state s_*:
            ;board:
		(at rook_b1 n4 n4)
		(empty_square n1 n5)
		(empty_square n1 n4)
		(empty_square n1 n3)
		(empty_square n1 n2)
		(empty_square n1 n1)
		(empty_square n2 n5)
		(empty_square n2 n4)
		(empty_square n2 n3)
		(empty_square n2 n2)
		(empty_square n2 n1)
		(empty_square n3 n5)
		(empty_square n3 n4)
		(empty_square n3 n3)
		(empty_square n3 n2)
		(empty_square n3 n1)
		(empty_square n4 n5)
		(empty_square n4 n3)
		(empty_square n4 n2)
		(empty_square n4 n1)
		(empty_square n5 n5)
		(empty_square n5 n4)
		(empty_square n5 n3)
		(empty_square n5 n2)
		(empty_square n5 n1)
            ;removed pieces:
           )
    )
)
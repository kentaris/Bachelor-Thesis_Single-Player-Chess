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
		(at pawn_b1 n4 n2)
		(at rook_b1 n4 n5)
		(at king_w1 n3 n1)
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
		(empty_square n4 n4)
		(empty_square n4 n3)
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
;[:init_diffByN_hor_ver]
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
		(is_black rook_b1)
		(is_black rook_b2)
		(is_black pawn_b1)
		(is_black pawn_b2)
		(is_black pawn_b3)
		(is_black pawn_b4)
		(is_black pawn_b5)
		(is_white king_w1)
		(is_rook rook_b1)
		(is_rook rook_b2)
		(is_pawn pawn_b1)
		(is_pawn pawn_b2)
		(is_pawn pawn_b3)
		(is_pawn pawn_b4)
		(is_pawn pawn_b5)
		(is_king king_w1)

        (white_s_turn)

        (TRUE)
    )
    (:goal (and
		(at rook_b1 n4 n5)
		(at king_w1 n4 n2)
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
		(empty_square n4 n4)
		(empty_square n4 n3)
		(empty_square n4 n1)
		(empty_square n5 n5)
		(empty_square n5 n4)
		(empty_square n5 n3)
		(empty_square n5 n2)
		(empty_square n5 n1)
;TODO: the removed predicate still has to be given the exact pawn that has been removed. If I do the same as with the white_pawn_at derived predicate I need to give it a location but I don't know that location. How can I fix this? For now I just leave away the removed predicates for pawns but this can lead to pawns being at locations I don't want them to be.
           )
    )
)
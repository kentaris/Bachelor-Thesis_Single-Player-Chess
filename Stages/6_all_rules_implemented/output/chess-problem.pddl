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
        ;Initial position of the Knight piece (upper left corner) => (n1 n8) = A8  // (at pawn_w1 n1 n2) = A2
		(at pawn_b1 n1 n4)
		(at pawn_b2 n3 n4)
		(at pawn_b3 n5 n4)
		(at pawn_b4 n2 n3)
		(at pawn_w1 n3 n3)
		(at pawn_w2 n4 n3)
		(at pawn_b5 n5 n3)
		(at pawn_w3 n2 n2)
		(at pawn_w4 n4 n2)

		;Difference by Zero:
		(diff_by_Zero n1 n1)
		(diff_by_Zero n2 n2)
		(diff_by_Zero n3 n3)
		(diff_by_Zero n4 n4)
		(diff_by_Zero n5 n5)

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

		;Difference by Five:

		;Difference by Six:

		;Difference by Seven:

		;Diff by Zero:
		(diff_by_N n1 n1)
		(diff_by_N n2 n2)
		(diff_by_N n3 n3)
		(diff_by_N n4 n4)
		(diff_by_N n5 n5)

		;Diff by One:
		(diff_by_N n1 n2)
		(diff_by_N n2 n1)
		(diff_by_N n2 n3)
		(diff_by_N n3 n2)
		(diff_by_N n3 n4)
		(diff_by_N n4 n3)
		(diff_by_N n4 n5)
		(diff_by_N n5 n4)

		;Diff by Two:
		(diff_by_N n1 n3)
		(diff_by_N n2 n4)
		(diff_by_N n3 n1)
		(diff_by_N n3 n5)
		(diff_by_N n4 n2)
		(diff_by_N n5 n3)

		;Diff by Three:
		(diff_by_N n1 n4)
		(diff_by_N n2 n5)
		(diff_by_N n4 n1)
		(diff_by_N n5 n2)

		;Diff by Four:
		(diff_by_N n1 n5)
		(diff_by_N n5 n1)

		;Pawn double moves for white:
		(pawn_start_pos_white n1 n2)
		(pawn_start_pos_white n2 n2)
		(pawn_start_pos_white n3 n2)
		(pawn_start_pos_white n4 n2)
		(pawn_start_pos_white n5 n2)

		;Pawn double moves for black:
		(pawn_start_pos_black n1 n4)
		(pawn_start_pos_black n2 n4)
		(pawn_start_pos_black n3 n4)
		(pawn_start_pos_black n4 n4)
		(pawn_start_pos_black n5 n4)

		;Pawn single moves for white:
		(plusOne_white n1 n2)
		(plusOne_white n2 n3)
		(plusOne_white n3 n4)
		(plusOne_white n4 n5)

		;Pawn single moves for black:
		(plusOne_black n2 n1)
		(plusOne_black n3 n2)
		(plusOne_black n4 n3)
		(plusOne_black n5 n4)
    )
    (:goal (and
        ;Adjust numbers by hand!
;[:goal_position]
        ;(at pawn_w3 n3 n5)
		;(at pawn_w2 n2 n3)
		;(at knight_w1 n3 n3)
		;(at pawn_w4 n4 n3)
		;(at pawn_w1 n1 n2)
		;(at pawn_w5 n5 n2)
		;(at rook_w1 n4 n1)
		;(at king_w1 n5 n1)
		;(at w_bishop_w1 n1 n5)
		;(at queen_w1 n5 n4)
		;
		;(at pawn_b1 n5 n3)

        (at pawn_b1 n1 n3)
		(at pawn_b2 n3 n4)
		(at pawn_b3 n5 n3)
		(at pawn_b4 n2 n3)
		(at pawn_w1 n3 n3)
		(at pawn_w2 n4 n5)
		(at pawn_b5 n5 n2)
		(at pawn_w3 n2 n2)
		(at pawn_w4 n4 n4)

           )
    )
)
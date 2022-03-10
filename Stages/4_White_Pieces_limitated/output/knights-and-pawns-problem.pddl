(define (problem knights-and-pawns-problem)
    (:domain knights-and-pawns)
    (:objects
        n1 n2 n3 n4 n5 -  location
        knight_w1 knight_w2 - knight_w
        ;knight_b1 knight_b2 - knight_b
        pawn_w1 pawn_w2 pawn_w3 pawn_w4 pawn_w5 - pawn_w
        ;pawn_b1 pawn_b2 pawn_b3 pawn_b4 pawn_b5 - pawn black
    )
    (:init
        ;Initial position of the Knight piece (upper left corner) => (n1 n8) = A8  // (at pawn_w1 n1 n2) = A2
		(at pawn_w1 n4 n1)
		(at pawn_w2 n4 n2)
		(at pawn_w3 n4 n3)
		(at pawn_w4 n4 n4)
		(at pawn_w5 n4 n5)
		(at knight_w1 n5 n2)
		(at knight_w2 n5 n4)

		;Difference by Zero:
		(diff_by_Zero n1 n1)
		(diff_by_Zero n2 n2)
		(diff_by_Zero n3 n3)
		(diff_by_Zero n4 n4)
		(diff_by_Zero n5 n5)
		(diff_by_Zero n6 n6)
		(diff_by_Zero n7 n7)
		(diff_by_Zero n8 n8)

		;Difference by One:
		(diff_by_One n1 n2)
		(diff_by_One n2 n1)
		(diff_by_One n2 n3)
		(diff_by_One n3 n2)
		(diff_by_One n3 n4)
		(diff_by_One n4 n3)
		(diff_by_One n4 n5)
		(diff_by_One n5 n4)
		(diff_by_One n5 n6)
		(diff_by_One n6 n5)
		(diff_by_One n6 n7)
		(diff_by_One n7 n6)
		(diff_by_One n7 n8)
		(diff_by_One n8 n7)

		;Difference by Two:
		(diff_by_Two n1 n3)
		(diff_by_Two n2 n4)
		(diff_by_Two n3 n1)
		(diff_by_Two n3 n5)
		(diff_by_Two n4 n2)
		(diff_by_Two n4 n6)
		(diff_by_Two n5 n3)
		(diff_by_Two n5 n7)
		(diff_by_Two n6 n4)
		(diff_by_Two n6 n8)
		(diff_by_Two n7 n5)
		(diff_by_Two n8 n6)

		;Pawn double moves for white:
		(pawn_double_white n1 n1 n2 n4)
		(pawn_double_white n2 n2 n2 n4)
		(pawn_double_white n3 n3 n2 n4)
		(pawn_double_white n4 n4 n2 n4)
		(pawn_double_white n5 n5 n2 n4)
		(pawn_double_white n6 n6 n2 n4)
		(pawn_double_white n7 n7 n2 n4)
		(pawn_double_white n8 n8 n2 n4)

		;Pawn double moves for black:
		(pawn_double_black n1 n1 n7 n5)
		(pawn_double_black n2 n2 n7 n5)
		(pawn_double_black n3 n3 n7 n5)
		(pawn_double_black n4 n4 n7 n5)
		(pawn_double_black n5 n5 n7 n5)
		(pawn_double_black n6 n6 n7 n5)
		(pawn_double_black n7 n7 n7 n5)
		(pawn_double_black n8 n8 n7 n5)

		;Pawn single moves for white:
		(plusOne_white n2 n3)
		(plusOne_white n3 n4)
		(plusOne_white n4 n5)
		(plusOne_white n5 n6)
		(plusOne_white n6 n7)
		(plusOne_white n7 n8)

		;Pawn single moves for black:
		(plusOne_black n2 n1)
		(plusOne_black n3 n2)
		(plusOne_black n4 n3)
		(plusOne_black n5 n4)
		(plusOne_black n6 n5)
		(plusOne_black n7 n6)
    )
    (:goal (and
		(at pawn_w1 n1 n3)
		(at pawn_w2 n3 n2)
		(at knight_w1 n3 n3)
		(at pawn_w3 n3 n4)
		(at pawn_w4 n4 n1)
		(at knight_w2 n4 n4)
		(at pawn_w5 n4 n5)
           )
    )
)
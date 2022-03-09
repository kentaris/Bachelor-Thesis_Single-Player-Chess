(define (problem two-knights-and-a-pawn-problem)
    (:domain two-knights-and-a-pawn)
    (:objects
        f1 f2 f3 f4 f5 -  file
        r1 r2 r3 r4 r5 -  rank
        knight_w1 knight_w2 - knight_w
        ;knight_b1 knight_b2 - knight_b
        pawn_w1 pawn_w2 pawn_w3 pawn_w4 pawn_w5 - pawn_w
        ;pawn_b1 pawn_b2 pawn_b3 pawn_b4 pawn_b5 - pawn black
    )
    (:init
        ;Initial position of the Knight piece (upper left corner) => (n1 n8) = A8
        ;(at pawn_w1 n1 n2) ;A2
        ;(at pawn_w2 n2 n2) ;B2
        ;(at pawn_w3 n3 n2) ;C2
        ;(at pawn_w4 n4 n2) ;D2
        ;(at pawn_w5 n5 n2) ;E2
        ;(at knight_w1 f2 r1) ;B1
        ;(at knight_w2 f4 r1) ;D1
		(at pawn_w1 f4 r1)
		(at pawn_w2 f4 r2)
		(at pawn_w3 f4 r3)
		(at pawn_w4 f4 r4)
		(at pawn_w5 f4 r5)
		(at knight_w1 f5 r2)
		(at knight_w2 f5 r4)

        ; Here, we have to list all instances of the static predicates diff_by_two and diff_by_one:
		;Difference by zero:
		(diff_by_zero f1 f1)
		(diff_by_zero f1 r1)
		(diff_by_zero r1 f1)
		(diff_by_zero r1 r1)
		(diff_by_zero f2 f2)
		(diff_by_zero f2 r2)
		(diff_by_zero r2 f2)
		(diff_by_zero r2 r2)
		(diff_by_zero f3 f3)
		(diff_by_zero f3 r3)
		(diff_by_zero r3 f3)
		(diff_by_zero r3 r3)
		(diff_by_zero f4 f4)
		(diff_by_zero f4 r4)
		(diff_by_zero r4 f4)
		(diff_by_zero r4 r4)
		(diff_by_zero f5 f5)
		(diff_by_zero f5 r5)
		(diff_by_zero r5 f5)
		(diff_by_zero r5 r5)

		;Difference by one:
		(diff_by_one f1 f2)
		(diff_by_one f1 r2)
		(diff_by_one r1 f2)
		(diff_by_one r1 r2)
		(diff_by_one f2 f1)
		(diff_by_one f2 r1)
		(diff_by_one r2 f1)
		(diff_by_one r2 r1)
		(diff_by_one f2 f3)
		(diff_by_one f2 r3)
		(diff_by_one r2 f3)
		(diff_by_one r2 r3)
		(diff_by_one f3 f2)
		(diff_by_one f3 r2)
		(diff_by_one r3 f2)
		(diff_by_one r3 r2)
		(diff_by_one f3 f4)
		(diff_by_one f3 r4)
		(diff_by_one r3 f4)
		(diff_by_one r3 r4)
		(diff_by_one f4 f3)
		(diff_by_one f4 r3)
		(diff_by_one r4 f3)
		(diff_by_one r4 r3)
		(diff_by_one f4 f5)
		(diff_by_one f4 r5)
		(diff_by_one r4 f5)
		(diff_by_one r4 r5)
		(diff_by_one f5 f4)
		(diff_by_one f5 r4)
		(diff_by_one r5 f4)
		(diff_by_one r5 r4)

		;Difference by two:
		(diff_by_two f1 f3)
		(diff_by_two f1 r3)
		(diff_by_two r1 f3)
		(diff_by_two r1 r3)
		(diff_by_two f2 f4)
		(diff_by_two f2 r4)
		(diff_by_two r2 f4)
		(diff_by_two r2 r4)
		(diff_by_two f3 f1)
		(diff_by_two f3 r1)
		(diff_by_two r3 f1)
		(diff_by_two r3 r1)
		(diff_by_two f3 f5)
		(diff_by_two f3 r5)
		(diff_by_two r3 f5)
		(diff_by_two r3 r5)
		(diff_by_two f4 f2)
		(diff_by_two f4 r2)
		(diff_by_two r4 f2)
		(diff_by_two r4 r2)
		(diff_by_two f5 f3)
		(diff_by_two f5 r3)
		(diff_by_two r5 f3)
		(diff_by_two r5 r3)
    )
    (:goal (and
		(at pawn_w1 f1 r3)
		(at pawn_w2 f3 r2)
		(at knight_w1 f3 r3)
		(at pawn_w3 f3 r4)
		(at pawn_w4 f4 r1)
		(at knight_w2 f4 r4)
		(at pawn_w5 f4 r5)
           )
    )
)
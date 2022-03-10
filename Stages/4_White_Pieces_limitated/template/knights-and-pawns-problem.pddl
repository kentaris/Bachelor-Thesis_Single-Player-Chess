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
;[:init_start_state]
;[:init_diffByN]
;[:init_pawn_double_move]
;[:init_plusOne]
    )
    (:goal (and
        ;Adjust numbers by hand!
;[:goal_position]
        (at pawn_w3 n3 n5)
		(at pawn_w2 n2 n3)
		(at knight_w1 n3 n3)
		(at pawn_w4 n4 n3)
		(at pawn_w1 n1 n2)
		(at knight_w2 n4 n2)
		(at pawn_w5 n5 n2)
           )
    )
)
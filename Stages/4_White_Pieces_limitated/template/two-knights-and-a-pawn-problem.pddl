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
;[start_state]

        ; Here, we have to list all instances of the static predicates diff_by_two and diff_by_one:
;[:init_diffByZeroOneTwo]
    )
    (:goal (and
;[:goal_position]
           )
    )
)
;credit: Single knight problem as defined in the book 'An Introduction to the Planning Domain Deﬁnition Language' (Patrik Haslum)
(define (problem knights-tour-problem-8x8)
    (:domain knights-tour)
    (:objects 
        n1 n2 n3 n4 n5 -  location
        knight_b1 knight_b2 - figure
    )
    (:init
        ;Initial position of the Knight piece (upper left corner) => (n1 n8) = A8
        (at knight_b1 n1 n5)
        (at knight_b2 n2 n5)
        (visited knight_b1 n1 n5)
        (visited knight_b2 n2 n5)
        ; Here, we have to list all instances of the static predicates diff_by_two and diff_by_one:
;[:init_diffByOneTwo_knight]
    )
    (:goal (and
;[:goal_visited_knight]
           )
    )
)
;credit: As defined in the book 'An Introduction to the Planning Domain Deﬁnition Language' (Patrik Haslum)
(define (problem knights-tour-problem-8x8)
    (:domain knights-tour)
    ; Define a set of "numbers" 1..8:
    (:objects n1 n2 n3 n4 n5 n6 n7 n8)
    (:init
        ; Initial position of the Knight piece (upper left corner):2.1. DOMAIN AND PROBLEM DEFINITION
        (at n1 n8)
        (visited n1 n8)
        ; Here, we have to list all instances of the static
        ; predicates diff_by_two and diff_by_one:
;[:init_diffByOneTwo_knight]
    )
    (:goal (and
;[:goal_visited_knight]
           )
    )
)